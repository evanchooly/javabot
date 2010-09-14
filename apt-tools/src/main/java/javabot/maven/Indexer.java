package javabot.maven;

import javax.annotation.processing.Processor;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;

import com.antwerkz.maven.BaseProcessor;
import com.antwerkz.maven.SPI;

@SupportedAnnotationTypes("org.hibernate.annotations.Index")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
@SPI(Processor.class)
public class Indexer extends BaseProcessor {
    @Override
    public void generate(final Element element, final AnnotationMirror annotationMirror) {
        System.out.println("*************** Indexer.generate");
    }
}
