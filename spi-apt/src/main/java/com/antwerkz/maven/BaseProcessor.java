package com.antwerkz.maven;

import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public abstract class BaseProcessor extends AbstractProcessor {
    private Elements elementUtils;
    private Types typeUtils;

    @Override
    public synchronized void init(final ProcessingEnvironment env) {
        super.init(env);
        elementUtils = env.getElementUtils();
        typeUtils = env.getTypeUtils();

    }

    @Override
	public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
		for (final TypeElement annotation : annotations) {
			processAnnotation(annotation, roundEnv);
		}
        return false;
	}

    protected void processAnnotation(final TypeElement annotation, final RoundEnvironment roundEnv) {
		final Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(annotation);

		for (final Element element : elements) {
			processElement(element, annotation);
		}
	}

    protected void processElement(final Element element, final TypeElement annotation) {
		final TypeMirror expectedAnnotationType = annotation.asType();

		for (final AnnotationMirror mirror : elementUtils.getAllAnnotationMirrors(element)) {
			final TypeMirror annotationType = mirror.getAnnotationType();

			if (typeUtils.isAssignable(annotationType, expectedAnnotationType)) {
                generate(element, mirror);
			}
		}
	}

    protected String getFQN(final TypeElement element) {
        return element.getQualifiedName().toString();
    }

    public abstract void generate(Element element, AnnotationMirror mirror);
}
