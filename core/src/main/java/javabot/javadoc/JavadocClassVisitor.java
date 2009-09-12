package javabot.javadoc;

import javabot.dao.ClazzDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.asm.FieldVisitor;
import org.springframework.asm.MethodVisitor;
import org.springframework.asm.Opcodes;
import org.springframework.asm.commons.EmptyVisitor;
import org.springframework.beans.factory.annotation.Autowired;

public class JavadocClassVisitor extends EmptyVisitor {
    private static final Logger log = LoggerFactory.getLogger(JavadocClassVisitor.class);
    @Autowired
    private ClazzDao dao;
    private Clazz clazz;
    private JavadocParser parser;

    public JavadocClassVisitor(final JavadocParser javadocParser, final ClazzDao clazzDao) {
        parser = javadocParser;
        dao = clazzDao;
    }

    @Override
    public void visit(final int version, final int access, final String name, final String signature,
        final String superName, final String[] interfaces) {
        try {
            final String pkg = getPackage(name);
            if (parser.acceptPackage(pkg) && isPublic(access)) {
                final String className = name.substring(name.lastIndexOf("/") + 1).replace('$', '.');
                clazz = parser.getOrCreate(parser.getApi(), pkg, className);
                if(superName != null) {
                    String superPkg = getPackage(superName);
                    String parentName = superName.substring(superName.lastIndexOf("/") + 1);
                    final Clazz parent = parser.getOrQueue(parser.getApi(), superPkg, parentName, clazz);
                    clazz.setSuperClass(parent);
                    dao.save(clazz);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private boolean isPublic(final int access) {
        return ((access & Opcodes.ACC_PUBLIC) == Opcodes.ACC_PUBLIC);
    }

    private String getPackage(final String name) {
        return name.substring(0, name.lastIndexOf("/")).replace('/', '.');
    }

    @Override
    public FieldVisitor visitField(final int access, final String name, final String desc, final String signature,
        final Object value) {
        if (clazz != null && isPublic(access)) {
        }
        return null;
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature,
        final String[] exceptions) {
        if (clazz != null && isPublic(access)) {
            String[] params = desc.substring(1, desc.lastIndexOf(")")).split(";");
            StringBuilder longTypes = new StringBuilder();
            StringBuilder longStripped = new StringBuilder();
            StringBuilder shortTypes = new StringBuilder();
            StringBuilder shortStripped = new StringBuilder();
            for (String param : params) {
                String arg = convertToClassName(param);
                String shortName = calculateNameAndPackage(arg)[1];
                append(longTypes, arg, ", ");
                append(longStripped, arg, ",");
                append(shortTypes, shortName, ",");
                append(shortStripped, shortName, ",");
            }
            dao.save(new Method(name, clazz, params.length, longTypes.toString(), longStripped.toString(),
                shortTypes.toString(), shortStripped.toString()));
        }
        return null;
    }

    private void append(final StringBuilder builder, final String arg, final String delim) {
        if (builder.length() != 0) {
            builder.append(delim);
        }
        builder.append(arg);
    }

    public static String[] calculateNameAndPackage(final String href) {
        String clsName = href;
        while (clsName.contains(".") && Character.isLowerCase(clsName.charAt(0))) {
            clsName = clsName.substring(clsName.indexOf(".") + 1);
        }
        String pkgName = href.equals(clsName) ? null : href.substring(0, href.indexOf(clsName) - 1);
        return new String[]{pkgName, clsName};
    }

    private String convertToClassName(final String param) {
        String name = param;
        if (name.startsWith("L")) {
            name = name.substring(1);
        }
        name = name.replace('/', '.');
        return name;
    }
}