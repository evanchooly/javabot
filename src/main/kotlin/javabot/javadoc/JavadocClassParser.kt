package javabot.javadoc

import com.mongodb.MongoException
import javabot.dao.ApiDao
import javabot.dao.JavadocClassDao
import javabot.model.javadoc.JavadocApi
import javabot.model.javadoc.JavadocClass
import javabot.model.javadoc.JavadocField
import javabot.model.javadoc.JavadocMethod
import org.jsoup.nodes.Document
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JavadocClassParser @Inject constructor(private var javadocClassDao: JavadocClassDao, var apiDao: ApiDao) {

    companion object {
        private val LOG = LoggerFactory.getLogger(JavadocClassParser::class.java)

        fun calculateNameAndPackage(value: String): Pair<String?, String> {
            var clsName = value
            while (clsName.contains(".") && Character.isLowerCase(clsName[0])) {
                clsName = clsName.substring(clsName.indexOf(".") + 1)
            }
            val pkgName = if (value != clsName) {
                value.substring(0, value.indexOf(clsName) - 1)
            } else {
                null
            }

            return pkgName to clsName
        }
    }

}
