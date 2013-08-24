package javabot.javadoc;

import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;

import javabot.dao.ApiDao;
import javabot.dao.JavadocClassDao;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.EmptyVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavadocClassVisitor extends EmptyVisitor {
  private static final Logger log = LoggerFactory.getLogger(JavadocClassVisitor.class);
  @Inject
  private JavadocClassDao dao;
  @Inject
  private ApiDao apiDao;
  @Inject
  private JavadocParser parser;

  private JavadocClass javadocClass;
  private static final Map<Character, String> PRIMITIVES = new HashMap<>();

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

  @Override
  public void visit(final int version, final int access, final String name, final String signature,
      final String superName, final String[] interfaces) {
    try {
      final String pkg = getPackage(name);
      if (isPublic(access)) {
        final String className = name.substring(name.lastIndexOf("/") + 1).replace('$', '.');
        javadocClass = parser.getOrCreate(parser.getApi(), pkg, className);
        if (superName != null) {
          String superPkg = getPackage(superName);
          String parentName = superName.substring(superName.lastIndexOf("/") + 1);
          final JavadocClass parent = parser.getOrQueue(parser.getApi(), superPkg, parentName, javadocClass);
          if(parent != null) {
            javadocClass.setSuperClass(parent);
          }
          dao.save(javadocClass);
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
    if (javadocClass != null && isPublic(access)) {
      StringBuilder longTypes = new StringBuilder();
      processParam(name, desc, signature, longTypes, new StringBuilder(), desc);
      final String type = longTypes.toString();
      final JavadocField field = new JavadocField(javadocClass, name, type);
      dao.save(field);
    }
    return null;
  }

  @Override
  public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature,
      final String[] exceptions) {
    if (javadocClass != null && isPublic(access)) {
      String params = desc.substring(1, desc.lastIndexOf(")"));
      StringBuilder longTypes = new StringBuilder();
      StringBuilder shortTypes = new StringBuilder();
      int count = processParam(name, desc, signature, longTypes, shortTypes, params);
      dao.save(new JavadocMethod(javadocClass,
          "<init>".equals(name) ? javadocClass.getName() : name, count, longTypes.toString(), shortTypes.toString()));
    }
    return null;
  }

  private int processParam(final String name, final String desc, final String signature,
      final StringBuilder longTypes, final StringBuilder shortTypes, final String param) {
    String arg = param;
    int count = 0;
    while (!arg.isEmpty()) {
      StringBuilder type = new StringBuilder("");
      while (arg.startsWith("[")) {
        type.append("[]");
        arg = arg.substring(1);
      }
      String base;
      if (arg.startsWith("L")) {
        final int index = arg.indexOf(";");
        base = convertToClassName(arg.substring(1, index));
        arg = arg.substring(index + 1);
      } else {
        base = PRIMITIVES.get(arg.charAt(0));
        arg = arg.substring(1);
      }
      if (base == null) {
        log.debug("javadocClass = " + javadocClass);
        log.debug("name = " + name);
        log.debug("desc = " + desc);
        log.debug("signature = " + signature);
        throw new RuntimeException("I don't know what " + arg + " is");
      }
      update(longTypes, shortTypes, type.insert(0, base).toString());
      count++;
    }
    return count;
  }

  private void update(final StringBuilder longTypes, final StringBuilder shortTypes, final String arg) {
    String shortName = calculateNameAndPackage(arg)[1];
    append(longTypes, arg, ",");
    append(shortTypes, shortName, ",");
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