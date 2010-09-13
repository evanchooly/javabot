package com.antwerkz.maven;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;

@SupportedAnnotationTypes("com.antwerkz.maven.SPI")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class SPIProcessor extends AbstractProcessor {
    private final Map<String, Set<String>> impls = new TreeMap<String, Set<String>>();

    @Override
    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment env) {
        boolean handled = false;
        for (final TypeElement type : annotations) {
            handled = true;
            try {
                for (final Element typeDecl : env.getElementsAnnotatedWith(type)) {
                    final TypeElement classDecl = (TypeElement) typeDecl;
                    final List<? extends AnnotationMirror> mirrors = classDecl.getAnnotationMirrors();
                    for (final AnnotationMirror mirror : mirrors) {
                        final Name qName = ((TypeElement) mirror.getAnnotationType().asElement()).getQualifiedName();
                        if (qName.toString().equals(SPI.class.getName())) {
                            final Map<? extends ExecutableElement, ? extends AnnotationValue> map = mirror
                                .getElementValues();
                            for (final AnnotationValue value : map.values()) {
                                final List<AnnotationValue> list = (List<AnnotationValue>) value.getValue();
                                for (final AnnotationValue v : list) {
                                    final DeclaredType o = (DeclaredType) v.getValue();
                                    final TypeElement element = (TypeElement) o.asElement();
                                    put(element.getQualifiedName().toString(), classDecl.getQualifiedName().toString());
                                }
                            }
                        }
                    }
                }
                for (final Entry<String, Set<String>> entry : impls.entrySet()) {
                    write(entry);
                }
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        return handled;
    }

    private void write(final Entry<String, Set<String>> entry) throws IOException {
        final File file = new File("target/classes/META-INF/services/" + entry.getKey());
        file.getParentFile().mkdirs();
        final PrintWriter writer = new PrintWriter(
            new FileOutputStream(file));
        try {
            for (final String impl : entry.getValue()) {
                writer.println(impl);
            }
        } finally {
            if (writer != null) {
                writer.flush();
                writer.close();
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
    // new methods
}
