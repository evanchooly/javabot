package javabot.javadoc

import org.testng.Assert
import org.testng.annotations.Test

class JavadocClassVisitorTest {
    @Test fun generics() {
        compare("(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", true, "java.lang.String", "java.lang.Object[]")
        compare("(Ljava/util/Map<Ljava/lang/String;Ljava/lang/String;>;)TT;", false, "java.util.Map<String, String>")
        compare("(TV;)V", false, "V")
        compare("(Ljava/util/List<Ljava/lang/Object;>;Ljava/lang/Exception;)V", false,
              "java.util.List<Object>", "java.lang.Exception")
        compare("(Ljava/util/List<TK;>;Ljava/lang/Exception;)V", false, "java.util.List<K>", "java.lang.Exception")
        compare("(Ljava/util/List<Ljava/lang/Object;>;)V", false, "java.util.List<Object>")
        compare("(J)Ljava/util/List<Ljavax/batch/runtime/StepExecution;>;", false, "long")
        compare("(JLjava/util/concurrent/TimeUnit;)TV;", false, "long", "java.util.concurrent.TimeUnit")
        compare("()V", false)
        compare("Ljava/lang/String;", false, "java.lang.String")
        compare("J", false, "long")
    }

    private fun compare(signature: String, varargs: Boolean, vararg types: String) {
        val javadocTypes = JavadocClassVisitor.extractTypes("className", "methodName", signature, varargs)
        val actual = javadocTypes.iterator()
        for (type in types) {
            Assert.assertEquals(actual.next().toString(), type)
        }
    }
}