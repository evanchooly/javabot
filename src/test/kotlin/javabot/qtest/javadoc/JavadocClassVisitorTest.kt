package javabot.qtest.javadoc

import io.quarkus.test.junit.QuarkusTest
import javabot.BaseTest
import javabot.javadoc.JavadocClassVisitor
import javabot.javadoc.JavadocType.JAVA11
import javabot.model.javadoc.JavadocApi
import javabot.model.javadoc.JavadocClass
import org.bson.types.ObjectId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@QuarkusTest
class JavadocClassVisitorTest : BaseTest() {
    @Test
    fun testMapDescriptor() {
        val visitor = JavadocClassVisitor(apiDao, JavadocApi(), "", "", null, JAVA11)

        val list = visitor.mapDescriptor("(Ljava/lang/Object;Ljava/lang/Class;)Ljava/lang/Object;")

        assertEquals(listOf("java/lang/Object", "java/lang/Class"), list)
    }

    @Test
    fun testMapSignature() {
        val visitor = JavadocClassVisitor(apiDao, JavadocApi(), "", "", null, JAVA11)

        val (t, cls) = visitor.mapSignature("<T:Ljava/lang/Object;>(TT;Ljava/lang/Class<TT;>;)TT;")
        assertEquals("T", t)
        assertEquals("java/lang/Class", cls)

        val (faces, str, map, bool) =
            visitor.mapSignature(
                "(Ljavax/faces/context/FacesContext;Ljava/lang/String;" +
                    "Ljava/util/Map<Ljava/lang/String;Ljava/util/List<Ljava/lang/String;>;>;Z)Ljava/lang/String;"
            )
        assertEquals("javax/faces/context/FacesContext", faces)
        assertEquals("java/lang/String", str)
        assertEquals("java/util/Map", map)
        assertEquals("boolean", bool)
    }

    @Test
    fun testVisitMethod() {
        val visitor = JavadocClassVisitor(apiDao, JavadocApi(), "", "", null, JAVA11)
        visitor.javadocClass = JavadocClass()
        visitor.javadocClass.id = ObjectId()
        visitor.javadocClass.apiId = ObjectId()
        visitor.javadocClass.packageName = ""
        visitor.javadocClass.name = ""
        visitor.javadocClass.url = ""
        visitor.visitMethod(
            access = 1025,
            name = "createContextualProxy",
            descriptor = "(Ljava/lang/Object;Ljava/lang/Class;)Ljava/lang/Object;",
            signature = "<T:Ljava/lang/Object;>(TT;Ljava/lang/Class<TT;>;)TT;",
            exceptions = null,
        )

        visitor.visitMethod(
            access = 1,
            name = "getRedirectURL",
            descriptor =
                "(Ljavax/faces/context/FacesContext;Ljava/lang/String;Ljava/util/Map;Z)Ljava/lang/String;",
            signature =
                "(Ljavax/faces/context/FacesContext;Ljava/lang/String;Ljava/util/Map<Ljava/lang/String;Ljava/util/List<Ljava/lang/String;>;>;Z)Ljava/lang/String;",
            exceptions = null,
        )
    }
}
