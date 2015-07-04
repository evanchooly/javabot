package javabot.javadoc;

import java.util.Iterator;
import java.util.List;

import javabot.javadoc.JavadocSignatureVisitor.JavadocType;
import org.testng.Assert;
import org.testng.annotations.Test;

public class JavadocClassVisitorTest {
  @Test
  public void generics() {
    compare("(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", true, "java.lang.String", "java.lang.Object...");
    compare("(Ljava/util/Map<Ljava/lang/String;Ljava/lang/String;>;)TT;", false, "java.util.Map<String, String>");
    compare("(TV;)V", false, "V");
    compare("(Ljava/util/List<Ljava/lang/Object;>;Ljava/lang/Exception;)V", false,
        "java.util.List<Object>", "java.lang.Exception");
    compare("(Ljava/util/List<TK;>;Ljava/lang/Exception;)V", false, "java.util.List<K>", "java.lang.Exception");
    compare("(Ljava/util/List<Ljava/lang/Object;>;)V", false, "java.util.List<Object>");
    compare("(J)Ljava/util/List<Ljavax/batch/runtime/StepExecution;>;", false, "long");
    compare("(JLjava/util/concurrent/TimeUnit;)TV;", false, "long", "java.util.concurrent.TimeUnit");
    compare("()V", false);
    compare("Ljava/lang/String;", false, "java.lang.String");
    compare("J", false, "long");
  }

  private void compare(final String signature, boolean varargs, String... types) {
    List<JavadocType> javadocTypes = JavadocClassVisitor.extractTypes("className", "methodName", signature, varargs);
    Iterator<JavadocType> actual = javadocTypes.iterator();
    for (String type : types) {
      Assert.assertEquals(actual.next().toString(), type);
    }
  }
}