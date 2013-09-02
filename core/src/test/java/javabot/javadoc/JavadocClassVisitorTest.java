package javabot.javadoc;

import java.util.Iterator;

import javabot.javadoc.JavadocSignatureVisitor.JavadocType;
import org.objectweb.asm.commons.Method;
import org.testng.Assert;
import org.testng.annotations.Test;

public class JavadocClassVisitorTest {
  @Test
  public void generics() {
    compare("(Ljava/util/Map<Ljava/lang/String;Ljava/lang/String;>;)TT;", "java.util.Map<java.lang.String,java.lang.String>");
    compare("(TV;)V", "V");
    compare("(Ljava/util/List<Ljava/lang/Object;>;Ljava/lang/Exception;)V",
        "java.util.List<java.lang.Object>", "java.lang.Exception");
    compare("(Ljava/util/List<TK;>;Ljava/lang/Exception;)V", "java.util.List<K>", "java.lang.Exception");
    compare("(Ljava/util/List<Ljava/lang/Object;>;)V", "java.util.List<java.lang.Object>");
    compare("(J)Ljava/util/List<Ljavax/batch/runtime/StepExecution;>;", "long");
    compare("(JLjava/util/concurrent/TimeUnit;)TV;", "long", "java.util.concurrent.TimeUnit");
    compare("()V");
    compare("Ljava/lang/String;", "java.lang.String");
    compare("J", "long");
  }

  @Test
  public void method() {
    Method method = Method.getMethod("(TT;Ljava/util/Map<Ljava/lang/String;Ljava/lang/String;>;Ljava/lang/Class<TT;>;)TT;");
    System.out.println("method = " + method);
  }

  private void compare(final String signature, String... types) {
    Iterator<JavadocType> actual = JavadocClassVisitor.extractTypes("className", "methodName", signature).iterator();
    for (String type : types) {
      Assert.assertEquals(actual.next().toString(), type);
    }
  }
}