import javabot.dao.FactoidDao
import org.testng.annotations.Test
import org.testng.Assert

@Test
class FactoidDaoTest {
  var factoidDao: FactoidDao = FactoidDao
  def testInsertfactoid: Unit = {
    factoidDao.addFactoid("joed2", "test2", "##javabot")
    Assert.assertTrue(factoidDao.hasFactoid("test2"))
    factoidDao.delete("joed2", "test2")
  }
}