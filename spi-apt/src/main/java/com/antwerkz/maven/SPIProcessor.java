package com.antwerkz.maven;

import javax.annotation.processing.Filer;
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
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

@SupportedAnnotationTypes("com.antwerkz.maven.SPI")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class SPIProcessor extends BaseProcessor {
    private final Map<String, Set<String>> impls = new TreeMap<>();

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
    @SuppressWarnings("unchecked")
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
            set = new TreeSet<>();
            impls.put(name, set);
        }
        set.add(qualifiedName);
    }

    private void write(final Entry<String, Set<String>> entry) throws IOException {
        final Filer filer = processingEnv.getFiler();
        final Set<String> current = readCurrentEntries(entry, filer);
        FileObject file = filer.createResource(StandardLocation.CLASS_OUTPUT, "", "META-INF/services/" + entry.getKey());
        try (PrintWriter writer = new PrintWriter(file.openWriter(), true)) {
            for (final String impl : current) {
                writer.println(impl);
            }
        }
    }

    private Set<String> readCurrentEntries(Entry<String, Set<String>> entry, Filer filer) throws IOException {
        final FileObject file = filer.getResource(StandardLocation.CLASS_OUTPUT, "", "META-INF/services/" + entry.getKey());
        final Set<String> current = new TreeSet<>(entry.getValue());
        try {
            final InputStream stream = file.openInputStream();
            if (stream != null) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
                    String spi;
                    while ((spi = reader.readLine()) != null) {
                        current.add(spi);
                    }
                    current.addAll(entry.getValue());
                }
            }
        } catch (FileNotFoundException e) {
            // Noting to update
        }
        return current;
    }
}
