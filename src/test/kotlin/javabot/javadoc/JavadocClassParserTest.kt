package javabot.javadoc

import com.google.inject.Inject
import javabot.BaseTest
import javabot.JavabotConfig
import javabot.JavabotConfigTest
import javabot.dao.ApiDao
import javabot.dao.JavadocClassDao
import javabot.model.ApiEvent
import javabot.model.downloadZip
import javabot.model.javadoc.JavadocApi
import org.bson.types.ObjectId
import org.jboss.forge.roaster.Roaster
import org.testng.Assert
import org.testng.annotations.Test
import java.io.File
import java.util.jar.JarFile

class JavadocClassParserTest : BaseTest() {
    @Inject
    lateinit var parser: JavadocClassParser
    @Inject
    lateinit var apiDao: ApiDao
    @Inject
    lateinit var classDao: JavadocClassDao
    @Inject
    lateinit var config: JavabotConfig

    @Test
    fun testParse() {
        val api = JavadocApi("JDK", "${config.url()}/javadoc/JDK")
        apiDao.save(api)
        val query = classDao.getQuery()
                .filter("apiId", api.id)
        Assert.assertEquals(query.asList().size, 0)
        JarFile(ApiEvent.locateJDK().downloadZip())
                .extractToSource(api,
                        { it.name.endsWith("/Map.java") },
                        { parser.parse(api, Roaster.parse(it.text)) }
                )
        classDao.deleteNotVisible(api)
        Assert.assertEquals(query.asList().size, 2)

        Assert.assertNotNull(classDao.getClassByFqcn("java.util.Map"))
        Assert.assertNotNull(classDao.getClassByFqcn("java.util.Map.Entry"))
        Assert.assertNotNull(classDao.getClass(null, "Map.Entry"))
        Assert.assertNull(classDao.getQuery()
                .filter("fqcn", "javax.swing.text.html.Map")
                .get())
    }
}
