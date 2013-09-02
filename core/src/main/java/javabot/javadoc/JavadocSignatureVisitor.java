package javabot.javadoc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.objectweb.asm.signature.SignatureVisitor;

class JavadocSignatureVisitor implements SignatureVisitor {
  private static final Map<Character, String> PRIMITIVES = new HashMap<>();

  boolean trace = !true;

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

  private List<JavadocType> types = new ArrayList<>();

  private JavadocType type;

  private boolean done = false;

  private final String className;

  private final String name;

  private String signature;

  private Stack<String> stack = new Stack<>();

  public JavadocSignatureVisitor(final String className, final String name, final String signature) {
    if (trace) {
      System.out.println("\n\n*** JavadocSignatureVisitor.JavadocSignatureVisitor");
    }
    if (trace) {
      System.out.println("className = [" + className + "], name = [" + name + "],\n signature = [" + signature + "]");
    }
    this.className = className;
    this.name = name;
    this.signature = signature;
  }

  @Override
  public void visitFormalTypeParameter(final String formalParameter) {
    push("JavadocSignatureVisitor.visitFormalTypeParameter(" + formalParameter + ")");
    type = new JavadocType(formalParameter);
  }

  @Override
  public SignatureVisitor visitClassBound() {
    push("JavadocSignatureVisitor.visitClassBound");
    return this;
  }

  @Override
  public SignatureVisitor visitInterfaceBound() {
    push("JavadocSignatureVisitor.visitInterfaceBound");
    return this;
  }

  @Override
  public SignatureVisitor visitSuperclass() {
    push("JavadocSignatureVisitor.visitSuperclass");
    return this;
  }

  @Override
  public SignatureVisitor visitInterface() {
    push("JavadocSignatureVisitor.visitInterface");
    return this;
  }

  @Override
  public SignatureVisitor visitParameterType() {
    push("JavadocSignatureVisitor.visitParameterType");
    type = new JavadocType();
    types.add(type);
    return this;
  }

  @Override
  public SignatureVisitor visitReturnType() {
    push("JavadocSignatureVisitor.visitReturnType");
    done = true;
    type = new JavadocType();
    return this;
  }

  @Override
  public SignatureVisitor visitExceptionType() {
    push("JavadocSignatureVisitor.visitExceptionType");
    type = new JavadocType();
    if (1 == 1) {
      throw new RuntimeException("JavadocSignatureVisitor.visitExceptionType");
    }
    return this;
  }

  @Override
  public void visitBaseType(final char baseType) {
    if (trace) {
      System.out.println("JavadocSignatureVisitor.visitBaseType(" + baseType + ")");
    }
    if(type == null) {
      type = new JavadocType();
      types.add(type);
    }
    type.setName(PRIMITIVES.get(baseType));
  }

  @Override
  public void visitTypeVariable(final String typeVariable) {
    if (trace) {
      System.out.println("JavadocSignatureVisitor.visitTypeVariable(" + typeVariable + ")");
    }
    if (!done) {
      type.addTypeVariable(typeVariable);
    }
  }

  @Override
  public SignatureVisitor visitArrayType() {
    push("JavadocSignatureVisitor.visitArrayType");
    type.setArray(true);
//    if(1==1) throw new RuntimeException("JavadocSignatureVisitor.visitArrayType");
    return this;
  }

  @Override
  public void visitClassType(final String classType) {
    push("JavadocSignatureVisitor.visitClassType(" + classType + ")");
    if(type == null) {
      type = new JavadocType();
      types.add(type);
    }
    type.setName(classType.replace('/', '.'));
  }

  @Override
  public void visitInnerClassType(final String innerClassType) {
    if (trace) {
      System.out.println("JavadocSignatureVisitor.visitInnerClassType(" + innerClassType + ")");
    }
    if (1 == 1) {
      throw new RuntimeException("JavadocSignatureVisitor.visitInnerClassType");
    }
  }

  @Override
  public void visitTypeArgument() {
    push("JavadocSignatureVisitor.visitTypeArgument");
  }

  private void push(final String item) {
    stack.push(item);
    if (trace) {
      System.out.println("push stack = " + stack);
    }
  }

  private String pop() {
    String pop = stack.pop();
    if (trace) {
      System.out.println("pop stack = " + stack);
    }
    return pop;
  }

  @Override
  public SignatureVisitor visitTypeArgument(final char typeArgument) {
    if (trace) {
      System.out.println("JavadocSignatureVisitor.visitTypeArgument(" + typeArgument + ")");
    }
    return this;
  }

  @Override
  public void visitEnd() {
    String stage = pop();
    if (trace) {
      System.out.println("---- JavadocSignatureVisitor.visitEnd : " + stage);
    }
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("JavadocSignatureVisitor{");
    sb.append("className='").append(className).append('\'');
    sb.append(", methodName='").append(name).append('\'');
    sb.append(", signature='").append(signature).append('\'');
    sb.append('}');
    return sb.toString();
  }

  public List<JavadocType> getTypes() {
    return types;
  }

  public static class JavadocType {
    private String name;

    private List<String> typeVariables = new ArrayList<>();

    private boolean array;

    public JavadocType() {
    }

    public JavadocType(final String s) {
      name = s;
    }

    public void addTypeVariable(final String typeVariable) {
      typeVariables.add(typeVariable);
    }

    public void setName(final String name) {
      if(this.name == null) {
        this.name = name;
      } else {
        typeVariables.add(name);
      }
    }

    public String getName() {
      return name;
    }

    public void setArray(final boolean array) {
      this.array = array;
    }

    public boolean isArray() {
      return array;
    }

    public String toString2() {
      final StringBuilder sb = new StringBuilder("JavadocType{");
      sb.append("array=").append(array);
      sb.append(", name='").append(name).append('\'');
      sb.append(", typeVariables=").append(typeVariables);
      sb.append('}');
      return sb.toString();
    }

    @Override
    public String toString() {
      System.out.println(toString2());
      if (name == null && typeVariables.size() == 1) {
        return typeVariables.get(0);
      }
      StringBuilder builder = null;
      try {
        builder = new StringBuilder(name);
      } catch (NullPointerException e) {
        throw new RuntimeException(e.getMessage(), e);
      }
      StringBuilder generics = new StringBuilder();
      for (String typeVariable : typeVariables) {
        if (generics.length() != 0) {
          generics.append(", ");
        }
        generics.append(typeVariable);
      }
      if (generics.length() != 0) {
        builder.append("<")
            .append(generics)
            .append(">");
      }
      if (isArray()) {
        builder.append("[]");
      }
      return builder.toString();
    }
  }
}