package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.BaseTest
import javabot.dao.LinkDao
import javabot.dao.NickServDao
import org.testng.Assert.*
import org.testng.annotations.Test
import javax.inject.Inject

class LinksOperationTest @Inject constructor(val nickServDao: NickServDao,
                                             val linkDao: LinkDao,
                                             val operation: LinksOperation
) : BaseTest() {
    @Test
    fun testSubmitLink() {
        linkDao.deleteAll()
        val response = operation.handleMessage(message("~submit http://foo.com This is a test"))
        assertEquals(response[0].value, Sofia.linksAccepted("http://foo.com", TEST_CHANNEL.name))
        assertEquals(linkDao.unapprovedLinks(TEST_CHANNEL.name).size, 1)
    }

    @Test
    fun testSubmitNoLink() {
        linkDao.deleteAll()
        val response = operation.handleMessage(message("~submit foo.com This is a test"))
        assertEquals(response[0].value, Sofia.linksRejected())
        assertEquals(linkDao.unapprovedLinks(TEST_CHANNEL.name).size, 0)
    }

    @Test
    fun testListLinks() {
        linkDao.deleteAll()
        var response = operation.handleMessage(message("~submit http://foo.com This is a test"))
        assertEquals(response[0].value, Sofia.linksAccepted("http://foo.com", TEST_CHANNEL.name))
        assertEquals(linkDao.unapprovedLinks(TEST_CHANNEL.name).size, 1)
        response = operation.handleMessage(message("~submit http://bar.com This is another test"))
        assertEquals(response[0].value, Sofia.linksAccepted("http://bar.com", TEST_CHANNEL.name))
        assertEquals(linkDao.unapprovedLinks(TEST_CHANNEL.name).size, 2)
        response=operation.handleMessage(message("~list unapproved"))
        assertEquals(response.size, 2)
        println(response.joinToString("\n"))
        val firstKey=response[0].value.split(" ")[0]
        val secondKey=response[1].value.split(" ")[0]
        response=operation.handleMessage(message("~list approve ${firstKey}"))
        println(response.joinToString("\n"))
        response=operation.handleMessage(message("~list approved 10"))
        assertEquals(response.size, 1)
        println(response.joinToString("\n"))
        response=operation.handleMessage(message("~list unapproved"))
        println(response.joinToString("\n"))
        assertEquals(response.size, 1)
        response=operation.handleMessage(message("~list reject ${secondKey}"))
        println(response.joinToString("\n"))

        response=operation.handleMessage(message("~list unapproved"))
        println(response.joinToString("\n"))
        assertEquals(response.size, 0)

    }
}