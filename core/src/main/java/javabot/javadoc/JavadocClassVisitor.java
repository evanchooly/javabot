package javabot.javadoc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

import javabot.dao.ApiDao;
import javabot.dao.JavadocClassDao;
import javabot.javadoc.JavadocSignatureVisitor.JavadocType;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.EmptyVisitor;
import org.objectweb.asm.signature.SignatureReader;
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

  private String pkg;

  private String className;

  @Override
  public void visit(final int version, final int access, final String name, final String signature,
      final String superName, final String[] interfaces) {
    try {
      pkg = getPackage(name);
      if (isPublic(access)) {
        className = name.substring(name.lastIndexOf("/") + 1).replace('$', '.');
        JavadocClass javadocClass = parser.getOrCreate(parser.getApi(), pkg, className);
        if (superName != null) {
          String superPkg = getPackage(superName);
          String parentName = superName.substring(superName.lastIndexOf("/") + 1);
          final JavadocClass parent = parser.getOrQueue(parser.getApi(), superPkg, parentName, javadocClass);
          if (parent != null) {
            javadocClass.setSuperClassId(parent);
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

  protected boolean isProtected(final int access) {
    return (access & Opcodes.ACC_PROTECTED) == Opcodes.ACC_PROTECTED;
  }

  private String getPackage(final String name) {
    return name.contains("/")
        ? name.substring(0, name.lastIndexOf("/")).replace('/', '.')
        : "";
  }

  @Override
  public FieldVisitor visitField(final int access, final String name, final String desc, final String signature,
      final Object value) {
    if (className != null) {
      JavadocClass javadocClass = getJavadocClass();
      if (javadocClass != null && isPublic(access)) {
        try {
          JavadocType javadocType = extractTypes(className, "", desc, false).get(0);
          dao.save(new JavadocField(javadocClass, name, javadocType.toString()));
        } catch (IndexOutOfBoundsException e) {
          throw new RuntimeException(e.getMessage(), e);
        }
      }
    }
    return null;
  }

  private JavadocClass getJavadocClass() {
    JavadocClass[] classes = dao.getClass(parser.getApi(), pkg, className);
    if (classes.length == 1) {
      return classes[0];
    }
    throw new RuntimeException(String.format("Wrong number of classes (%d) found for %s.%s", classes.length,
        pkg, className));
  }

  @Override
  public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature,
      final String[] exceptions) {
    if (className != null) {
      JavadocClass javadocClass = getJavadocClass();
      if (javadocClass != null && (isPublic(access) || isProtected(access))) {
        List<JavadocType> types = extractTypes(className, name, signature != null ? signature : desc,
            (access & Opcodes.ACC_VARARGS) == Opcodes.ACC_VARARGS);

        List<String> longTypes = new ArrayList<>();
        List<String> shortTypes = new ArrayList<>();
        for (JavadocType type : types) {
          update(longTypes, shortTypes, type.toString());
        }
        dao.save(new JavadocMethod(javadocClass, "<init>".equals(name) ? javadocClass.getName() : name,
            types.size(), longTypes, shortTypes));
      }
    }
    return null;
  }

  private void update(final List<String> longTypes, final List<String> shortTypes, final String arg) {
    longTypes.add(arg);
    shortTypes.add(calculateNameAndPackage(arg)[1]);
  }

  public static String[] calculateNameAndPackage(final String value) {
    String clsName = value;
    while (clsName.contains(".") && Character.isLowerCase(clsName.charAt(0))) {
      clsName = clsName.substring(clsName.indexOf(".") + 1);
    }
    String pkgName = value.equals(clsName) ? null : value.substring(0, value.indexOf(clsName) - 1);
    return new String[]{pkgName, clsName};
  }

  static List<JavadocType> extractTypes(final String className, final String methodName, final String signature,
      final boolean varargs) {
    SignatureReader reader = new SignatureReader(signature);
    JavadocSignatureVisitor v = new JavadocSignatureVisitor(className, methodName, signature, varargs);
    if (!signature.isEmpty()) {
      reader.accept(v);
    }
    return v.getTypes();
  }

}