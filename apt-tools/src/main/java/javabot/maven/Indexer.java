package javabot.maven;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Processor;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.persistence.Table;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

import com.antwerkz.maven.BaseProcessor;
import com.antwerkz.maven.SPI;

@SupportedAnnotationTypes("org.hibernate.annotations.Index")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
@SPI(Processor.class)
public class Indexer extends BaseProcessor {
    @Override
    public void generate(final Element element, final AnnotationMirror annotationMirror) {
        final String table = element.getEnclosingElement().getAnnotation(Table.class).name();
        final String name = getIndexName(annotationMirror);
        final String sql = String.format("CREATE INDEX \"%s\" ON %s USING btree (%s);", name, table,
            getColumnNames(element, annotationMirror));
        write(table, name, sql);
    }

    private void write(final String table, final String name, final String sql) {
        try {
            final PrintWriter writer = new PrintWriter(getResourceFile(table).openWriter());
            try {
                writer.printf("%s:%s\n", name, sql);
            } finally {
                writer.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

    }

    private FileObject getResourceFile(final String table) throws IOException {
        final Filer filer = processingEnv.getFiler();
        FileObject file;
        final String name = String.format("postgresql/%s.indexes", table);
        try {
            file = filer.createResource(StandardLocation.CLASS_OUTPUT, "", name);
        } catch (IOException e) {
            file = filer.getResource(StandardLocation.CLASS_OUTPUT, "", name);
        }
        return file;
    }

    private String getIndexName(final AnnotationMirror mirror) {
        return getValue("name", mirror).getValue().toString();
    }

    private String getColumnNames(final Element element, final AnnotationMirror annotationMirror) {
        final StringBuilder names = new StringBuilder();
        final AnnotationValue value = getValue("columnNames", annotationMirror);
        if (value != null) {
            for (final AnnotationValue col : (List<AnnotationValue>) value.getValue()) {
                if (names.length() != 0) {
                    names.append(", ");
                }
                names.append(col.getValue());
            }
        }
        if (names.length() == 0) {
            String name = element.getSimpleName().toString();
            if (name.startsWith("get")) {
                name = name.substring(3).toLowerCase();
            }
            names.append(name);
        }
        return names.toString();
    }

    private AnnotationValue getValue(final String name, final AnnotationMirror mirror) {
        final Map<? extends ExecutableElement, ? extends AnnotationValue> map = mirror.getElementValues();
        for (final Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : map.entrySet()) {
            if (name.equals(entry.getKey().getSimpleName().toString())) {
                return entry.getValue();
            }
        }
        return null;
    }
}
