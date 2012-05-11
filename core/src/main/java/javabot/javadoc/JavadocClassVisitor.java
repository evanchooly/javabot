package javabot.javadoc;

import java.util.HashMap;
import java.util.Map;

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
    private static final Map<Character, String> PRIMITIVES = new HashMap<Character, String>();

    static {
        PRIMITIVES.put('B', "byte");
        PRIMITIVES.put('C', "char");
        PRIMITIVES.put('D', "double");
        PRIMITIVES.put('F', "float");
        PRIMITIVES.put('I', "int");
        PRIMITIVES.put('J', "long");
        PRIMITIVES.put('S', "short");
        PRIMITIVES.put('Z', "boolean");
    }

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
                if (superName != null) {
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
        return (access & Opcodes.ACC_PUBLIC) == Opcodes.ACC_PUBLIC;
    }

    private String getPackage(final String name) {
        return name.contains("/")
                ? name.substring(0, name.lastIndexOf("/")).replace('/', '.')
                : "";
    }

    @Override
    public FieldVisitor visitField(final int access, final String name, final String desc, final String signature,
        final Object value) {
        if (clazz != null && isPublic(access)) {
            StringBuilder longTypes = new StringBuilder();
            processParam(name, desc, signature, longTypes, new StringBuilder(), new StringBuilder(), new StringBuilder(), desc);
            final String type = longTypes.toString();
            final Field field = new Field(clazz, name, type);
            dao.save(field);
        }
        return null;
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature,
        final String[] exceptions) {
        if (clazz != null && isPublic(access)) {
            String params = desc.substring(1, desc.lastIndexOf(")"));
            StringBuilder longTypes = new StringBuilder();
            StringBuilder longStripped = new StringBuilder();
            StringBuilder shortTypes = new StringBuilder();
            StringBuilder shortStripped = new StringBuilder();
            int count = processParam(name, desc, signature, longTypes, longStripped, shortTypes, shortStripped, params);
            dao.save(new Method("<init>".equals(name) ? clazz.getClassName() : name, clazz, count,
                longTypes.toString(), longStripped.toString(), shortTypes.toString(), shortStripped.toString()));
        }
        return null;
    }

    private int processParam(final String name, final String desc, final String signature,
        final StringBuilder longTypes, final StringBuilder longStripped, final StringBuilder shortTypes,
        final StringBuilder shortStripped, final String param) {
        String arg = param;
        int count = 0;
        while (!arg.isEmpty()) {
            StringBuilder type = new StringBuilder("");
            while (arg.startsWith("[")) {
                type.append("[]");
                arg = arg.substring(1);
            }
            String base;
            if(arg.startsWith("L")) {
                final int index = arg.indexOf(";");
                base = convertToClassName(arg.substring(1, index));
                arg = arg.substring(index + 1);
            } else {
                base = PRIMITIVES.get(arg.charAt(0));
                arg = arg.substring(1);
            }
            if (base == null) {
                log.debug("clazz = " + clazz);
                log.debug("name = " + name);
                log.debug("desc = " + desc);
                log.debug("signature = " + signature);
                throw new RuntimeException("I don't know what " + arg + " is");
            }
            update(longTypes, longStripped, shortTypes, shortStripped, type.insert(0, base).toString());
            count++;
        }

        return count;
    }

    private void update(final StringBuilder longTypes, final StringBuilder longStripped, final StringBuilder shortTypes,
        final StringBuilder shortStripped, final String arg) {
        String shortName = calculateNameAndPackage(arg)[1];
        append(longTypes, arg, ", ");
        append(longStripped, arg, ",");
        append(shortTypes, shortName, ",");
        append(shortStripped, shortName, ",");
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
        if (name.startsWith("[L")) {
            name = name.substring(2);
        }
        name = name.replace('/', '.');
        return name;
    }
}