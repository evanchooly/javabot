package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.BaseTest
import javabot.JavabotConfig
import javabot.dao.ApiDao
import javabot.model.ApiEvent
import javabot.model.javadoc.JavadocApi
import org.slf4j.LoggerFactory
import org.testng.annotations.BeforeTest
import org.testng.annotations.Test
import javax.inject.Inject

@Test
class MorphiaJavadocTest : BaseTest() {
    @Inject
    private lateinit var operation: JavadocOperation

    @BeforeTest
    fun load() {
        apiDao.findAll().forEach { apiDao.delete(it) }
        loadApi("Morphia", "dev.morphia.morphia", "morphia-core", "2.0.0-BETA2")
    }

    fun constructors() {
        scanForResponse(operation.handleMessage(message("~javadoc CountOptions()")), "dev/morphia/query/CountOptions.html#<init>")
        scanForResponse(operation.handleMessage(message("~javadoc dev.morphia.query.CountOptions()")), "dev/morphia/query/CountOptions.html")
    }

    fun methods() {
        scanForResponse(operation.handleMessage(message("~javadoc Query.filter(*)")), "dev/morphia/query/Query.html#filter(")
    }

    fun primitives() {
        scanForResponse(operation.handleMessage(message("~javadoc CountOptions.limit(int)")), "dev/morphia/query/CountOptions.html#limit(")
        scanForResponse(operation.handleMessage(message("~javadoc GeoNear.to(double[])")),
                "dev/morphia/aggregation/experimental/stages/GeoNear.html#to(")
    }

    fun fields() {
        scanForResponse(operation.handleMessage(message("~javadoc SystemVariables.CLUSTER_TIME")),
                "dev/morphia/aggregation/experimental/expressions/SystemVariables.html#CLUSTER_TIME")
    }

    fun nonpublic() {
        scanForResponse(operation.handleMessage(message("~javadoc DatastoreImpl(MongoClient, MapperOptions, String)")),
                "I have no documentation for DatastoreImpl(")
        scanForResponse(operation.handleMessage(message("~javadoc DatastoreImpl(MongoDatabase, MongoClient, Mapper, QueryFactory)")),
                "dev/morphia/DatastoreImpl.html#<init>")
    }
}
