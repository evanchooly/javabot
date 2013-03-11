package javabot.maven;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;

import com.antwerkz.maven.BaseProcessor;
import com.google.code.morphia.annotations.Entity;

@SupportedAnnotationTypes("javax.persistence.Entity")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class PlayModelGenerator extends BaseProcessor {
    private static final String TEMPLATE = "package models;%n"
        + "%n"
        + "import models.bases.%s;%n"
        + "%n"
        + "public class %s extends %sBase {%n"
        + "}%n";
    private static final String BASE_TEMPLATE = "package models.bases;%n"
        + "%n"
        + "import play.db.jpa.Model;%n"
        + "%n"
        + "import com.google.code.morphia.annotations.Entity;%n"
        + "import java.util.Date;%n"
        + "import models.EventType;%n"
        + "%n"
        + "@Entity(%s)%n"
        + "public class %sBase extends Model {%n"
        + "  %s%n"
        + "}%n";

    @Override
    public void generate(Element element, AnnotationMirror mirror) {
        String name = element.getSimpleName().toString();
        Entity annotation = element.getAnnotation(Entity.class);
        try {
            final String table = annotation != null ? annotation.value() : "";
            generateBaseFile(element, name, table);
            generateFile(element, name, table);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void generateBaseFile(Element element, String name, String table) throws IOException {
        File modelBaseDir = new File("../web-play/app/models/bases/");
        File file = new File(modelBaseDir, name + "Base.java");
        file.getParentFile().mkdirs();
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(String.format(BASE_TEMPLATE, table, name, getFields(element)));
        }
    }

    private void generateFile(Element element, String name, String table) throws IOException {
        File modelBaseDir = new File("../web-play/app/models/");
        File file = new File(modelBaseDir, name + ".java");
        if (!file.exists()) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(String.format(TEMPLATE, name, name, name));
            }
        }
    }

    private Map<String, Object> buildDataMap(Element source) {
        Map<String, Object> map = new HashMap<String, Object>();
        StringBuilder fields = getFields(source);
        map.put("fields", fields);
        return map;
    }

    private StringBuilder getFields(Element source) {
        StringBuilder fields = new StringBuilder();
        for (Element element : source.getEnclosedElements()) {
            if (element.getKind().isField()) {
                String name = element.getSimpleName().toString();
                if (!"id".equals(name)) {
                    fields.append(new Field(name, element.asType().toString())).toString();
                }
            }
        }
        return fields;
    }

    private static class Field {
        public final String name;
        public final String type;

        private Field(String name, String type) {
            this.name = name;
            this.type = type.substring(type.lastIndexOf(".") + 1);
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        @Override
        public String toString() {
            return String.format("public %s %s;%n  ", type, name);
        }
    }
}