package javabot.qtest.operations

import io.quarkus.test.junit.QuarkusTest
import javabot.BaseTest
import javabot.operations.JavadocOperation
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@QuarkusTest
class MorphiaJavadocTest : BaseTest() {
    private val operation: JavadocOperation by lazy {
        injector.getInstance(JavadocOperation::class.java)
    }

    @BeforeEach
    fun load() {
        loadApi("Morphia", "dev.morphia.morphia", "morphia-core", "2.2.10")
    }

    @Test
    fun constructors() {
        scanForResponse(
            operation.handleMessage(message("~javadoc CountOptions()")),
            "dev/morphia/query/CountOptions.html#<init>",
        )
        scanForResponse(
            operation.handleMessage(message("~javadoc dev.morphia.query.CountOptions()")),
            "dev/morphia/query/CountOptions.html",
        )
    }

    @Test
    fun methods() {
        scanForResponse(
            operation.handleMessage(message("~javadoc Query.filter(*)")),
            "dev/morphia/query/Query.html#filter(",
        )
    }

    @Test
    fun primitives() {
        scanForResponse(
            operation.handleMessage(message("~javadoc CountOptions.limit(int)")),
            "dev/morphia/query/CountOptions.html#limit(",
        )
        scanForResponse(
            operation.handleMessage(message("~javadoc GeoNear.to(double[])")),
            "dev/morphia/aggregation/experimental/stages/GeoNear.html#to(",
        )
    }

    @Test
    fun fields() {
        scanForResponse(
            operation.handleMessage(message("~javadoc SystemVariables.CLUSTER_TIME")),
            "dev/morphia/aggregation/experimental/expressions/SystemVariables.html#CLUSTER_TIME",
        )
    }

    @Test
    fun nonpublic() {
        scanForResponse(
            operation.handleMessage(
                message("~javadoc DatastoreImpl.setInitialVersion(MappedField, T)")
            ),
            "I have no documentation for ",
        )
    }
}
