package com.antwerkz.maven;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

@SupportedAnnotationTypes("com.antwerkz.maven.SPI")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class SPIProcessor extends BaseProcessor {
    private final Map<String, Set<String>> impls = new TreeMap<String, Set<String>>();

    @Override
    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
        final boolean b = super.process(annotations, roundEnv);
        try {
            for (final Entry<String, Set<String>> entry : impls.entrySet()) {
                write(entry);
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            impls.clear();
        }

        return b;
    }

    @Override
    public void generate(final Element element, final AnnotationMirror mirror) {
        final Map<? extends ExecutableElement, ? extends AnnotationValue> map = mirror.getElementValues();
        for (final AnnotationValue value : map.values()) {
            final List<AnnotationValue> list = (List<AnnotationValue>) value.getValue();
            for (final AnnotationValue v : list) {
                final DeclaredType o = (DeclaredType) v.getValue();
                put(getFQN((TypeElement) o.asElement()), getFQN((TypeElement) element));
            }
        }
    }

    private void put(final String name, final String qualifiedName) {
        Set<String> set = impls.get(name);
        if (set == null) {
            set = new TreeSet<String>();
            impls.put(name, set);
        }
        set.add(qualifiedName);
    }

    private void write(final Entry<String, Set<String>> entry) throws IOException {
        final Filer filer = processingEnv.getFiler();
        final FileObject file = filer.createResource(StandardLocation.CLASS_OUTPUT, "", "META-INF/services/" + entry.getKey());

        final PrintWriter writer = new PrintWriter(file.openWriter());
        try {
            for (final String impl : entry.getValue()) {
                writer.println(impl);
            }
        } finally {
            writer.close();
        }
    }
}
