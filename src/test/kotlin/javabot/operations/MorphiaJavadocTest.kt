package javabot.operations

import jakarta.inject.Inject
import javabot.BaseTest
import org.testng.annotations.BeforeTest
import org.testng.annotations.Test

@Test
class MorphiaJavadocTest : BaseTest() {
    @Inject private lateinit var operation: JavadocOperation

    @BeforeTest
    fun load() {
        loadApi("Morphia", "dev.morphia.morphia", "morphia-core", "2.2.10")
    }

    fun constructors() {
        scanForResponse(
            operation.handleMessage(message("~javadoc CountOptions()")),
            "dev/morphia/query/CountOptions.html#<init>"
        )
        scanForResponse(
            operation.handleMessage(message("~javadoc dev.morphia.query.CountOptions()")),
            "dev/morphia/query/CountOptions.html"
        )
    }

    fun methods() {
        scanForResponse(
            operation.handleMessage(message("~javadoc Query.filter(*)")),
            "dev/morphia/query/Query.html#filter("
        )
    }

    fun primitives() {
        scanForResponse(
            operation.handleMessage(message("~javadoc CountOptions.limit(int)")),
            "dev/morphia/query/CountOptions.html#limit("
        )
        scanForResponse(
            operation.handleMessage(message("~javadoc GeoNear.to(double[])")),
            "dev/morphia/aggregation/experimental/stages/GeoNear.html#to("
        )
    }

    fun fields() {
        scanForResponse(
            operation.handleMessage(message("~javadoc SystemVariables.CLUSTER_TIME")),
            "dev/morphia/aggregation/experimental/expressions/SystemVariables.html#CLUSTER_TIME"
        )
    }

    fun nonpublic() {
        scanForResponse(
            operation.handleMessage(
                message("~javadoc DatastoreImpl.setInitialVersion(MappedField, T)")
            ),
            "I have no documentation for "
        )
    }
}
