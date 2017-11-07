package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.BaseTest
import javabot.MockIrcAdapter
import javabot.dao.LinkDao
import org.testng.Assert.*
import org.testng.annotations.BeforeMethod
import org.testng.annotations.BeforeTest
import org.testng.annotations.Test
import javax.inject.Inject

class LinksOperationTest @Inject constructor(private val linkDao: LinkDao,
                                             val operation: LinksOperation
) : BaseTest() {

    @BeforeMethod
    fun deleted() {
        linkDao.deleteAll()
    }

    @Test
    fun testSubmitLink() {
        val response = operation.handleMessage(message("~submit http://foo.com This is a test"))
        assertEquals(response[0].value, Sofia.linksAccepted("http://foo.com", TEST_CHANNEL.name))
        assertEquals(linkDao.unapprovedLinks(TEST_CHANNEL.name).size, 1)
    }

    @Test
    fun testSubmitNoLink() {
        val response = operation.handleMessage(message("~submit foo.com This is a test"))
        assertEquals(response[0].value, Sofia.linksRejectedNoUrl())
        assertEquals(linkDao.unapprovedLinks(TEST_CHANNEL.name).size, 0)
    }

    @Test
    fun testSubmitPrivateMessageLink() {
        var response = operation.handleMessage(privateMessage("submit http://foo.com This is a test"))
        assertEquals(response[0].value, Sofia.linksNoChannel())
        assertEquals(linkDao.unapprovedLinks(TEST_CHANNEL.name).size, 0)

        response = operation.handleMessage(privateMessage("submit ${TEST_CHANNEL.name} http://foo.com This is a test"))
        assertEquals(response[0].value, Sofia.linksAccepted("http://foo.com", TEST_CHANNEL.name))
        assertEquals(linkDao.unapprovedLinks(TEST_CHANNEL.name).size, 1)
    }

    @Test
    fun testListLinks() {
        var response = operation.handleMessage(message("~submit http://foo.com This is a test"))
        assertEquals(response[0].value, Sofia.linksAccepted("http://foo.com", TEST_CHANNEL.name))
        assertEquals(linkDao.unapprovedLinks(TEST_CHANNEL.name).size, 1)

        response = operation.handleMessage(message("~submit http://bar.com This is another test"))
        assertEquals(response[0].value, Sofia.linksAccepted("http://bar.com", TEST_CHANNEL.name))
        assertEquals(linkDao.unapprovedLinks(TEST_CHANNEL.name).size, 2)

        response = operation.handleMessage(message("~list unapproved ${TEST_CHANNEL.name}"))
        assertEquals(response.size, 2)

        val firstKey = response[0].value.split(" ")[0]
        val secondKey = response[1].value.split(" ")[0]
        operation.handleMessage(message("~list approve ${TEST_CHANNEL.name} ${firstKey}"))

        response = operation.handleMessage(message("~list approved ${TEST_CHANNEL.name} 10"))
        assertEquals(response.size, 1)

        response = operation.handleMessage(message("~list unapproved ${TEST_CHANNEL.name}"))
        assertEquals(response.size, 1)


        operation.handleMessage(message("~list reject ${TEST_CHANNEL.name} ${secondKey}"))
        response = operation.handleMessage(message("~list unapproved ${TEST_CHANNEL.name}"))
        assertEquals(response.size, 1)
        assertEquals(response[0].value, Sofia.linksNoLinksOfStatus("unapproved", TEST_CHANNEL.name))
    }

    @Test
    fun testApproveInvalidKey() {
        operation.handleMessage(message("~submit http://foo.com This is a test"))
        var response = operation.handleMessage(message("~list unapproved ${TEST_CHANNEL.name}"))

        assertEquals(response.size, 1)

        val firstKey = response[0].value.split(" ")[0]
        response = operation.handleMessage(message("~list approve ${TEST_CHANNEL.name} ${firstKey}a"))

        assertEquals(response.size, 1)
        assertEquals(response[0].value, Sofia.linksNotFound("${firstKey}a"))
        response = operation.handleMessage(message("~list reject ${TEST_CHANNEL.name} ${firstKey}a"))

        assertEquals(response.size, 1)
        assertEquals(response[0].value, Sofia.linksNotFound("${firstKey}a"))
    }

    @Test
    fun testApproveNoKey() {
        operation.handleMessage(message("~submit http://foo.com This is a test"))
        var response = operation.handleMessage(message("~list unapproved ${TEST_CHANNEL.name}"))

        assertEquals(response.size, 1)

        response = operation.handleMessage(message("~list approve ${TEST_CHANNEL.name}"))

        assertEquals(response.size, 1)
        assertEquals(response[0].value, Sofia.linksNoKeySpecified("approve"))
        response = operation.handleMessage(message("~list reject ${TEST_CHANNEL.name}"))

        assertEquals(response.size, 1)
        assertEquals(response[0].value, Sofia.linksNoKeySpecified("reject"))
    }

    @Test
    fun testWrongChannelName() {
        operation.handleMessage(message("~submit http://foo.com This is a test"))
        var response = operation.handleMessage(message("~list unapproved ${TEST_CHANNEL.name}"))

        assertEquals(response.size, 1)
        response = operation.handleMessage(message("~list unapproved ${TEST_CHANNEL.name}a"))

        assertEquals(response.size, 1)
        assertEquals(response[0].value, Sofia.linksWrongChannel(TEST_CHANNEL.name))
        response = operation.handleMessage(message("~list approved ${TEST_CHANNEL.name}a"))

        assertEquals(response.size, 1)
        assertEquals(response[0].value, Sofia.linksWrongChannel(TEST_CHANNEL.name))
    }

    @Test
    fun testCountModifier() {
        listOf("foo", "bar", "baz", "bletch", "quux", "corge", "grault", "garply", "plugh", "xyzzy", "wibble")
                .forEachIndexed { index, domain ->
                    var response = operation.handleMessage(message("~submit http://$domain.com This is a test for $domain"))
                    assertEquals(response[0].value, Sofia.linksAccepted("http://$domain.com", TEST_CHANNEL.name))
                    assertEquals(linkDao.unapprovedLinks(TEST_CHANNEL.name).size, index + 1)
                }
        var response = operation.handleMessage(message("~list unapproved ${TEST_CHANNEL.name}"))

        assertEquals(response.size, 5)
        response = operation.handleMessage(message("~list unapproved 3"))

        assertEquals(response.size, 3)

        response = operation.handleMessage(message("~list unapproved a3"))

        assertEquals(response.size, 5)
    }

    @Test
    fun testPrivateListLinks() {
        var response = operation.handleMessage(message("submit http://foo.com This is a test"))
        assertEquals(response[0].value, Sofia.linksAccepted("http://foo.com", TEST_CHANNEL.name))
        assertEquals(linkDao.unapprovedLinks(TEST_CHANNEL.name).size, 1)
        response = operation.handleMessage(message("submit http://bar.com This is another test"))
        assertEquals(response[0].value, Sofia.linksAccepted("http://bar.com", TEST_CHANNEL.name))
        assertEquals(linkDao.unapprovedLinks(TEST_CHANNEL.name).size, 2)

        response = operation.handleMessage(privateMessage("list unapproved ${TEST_CHANNEL.name}a"))
        assertEquals(response.size, 1)
        assertEquals(response[0].value, Sofia.linksNoChannel())

        response = operation.handleMessage(privateMessage("list unapproved ${TEST_CHANNEL.name}"))

        assertEquals(response.size, 2)
        val firstKey = response[0].value.split(" ")[0]
        val secondKey = response[1].value.split(" ")[0]

        response = operation.handleMessage(privateMessage("list approve ${firstKey}"))
        assertEquals(response.size, 1)
        assertEquals(response[0].value, Sofia.linksNoChannel())

        response = operation.handleMessage(privateMessage("list approve ${TEST_CHANNEL.name} ${firstKey}"))
        assertEquals(response.size, 1)
        assertEquals(response[0].value, Sofia.linksVerbApplied(firstKey, "approved", TEST_CHANNEL.name))

        response = operation.handleMessage(privateMessage("list approved ${TEST_CHANNEL.name} 10"))
        assertEquals(response.size, 1)

        response = operation.handleMessage(privateMessage("list unapproved ${TEST_CHANNEL.name}"))

        assertEquals(response.size, 1)
        operation.handleMessage(privateMessage("list reject ${TEST_CHANNEL.name} ${secondKey}"))


        response = operation.handleMessage(privateMessage("list unapproved ${TEST_CHANNEL.name}"))

        assertEquals(response.size, 1)
        assertEquals(response[0].value, Sofia.linksNoLinksOfStatus("unapproved", TEST_CHANNEL.name))

        response = operation.handleMessage(privateMessage("list unapproved"))

        assertEquals(response.size, 1)
        assertEquals(response[0].value, Sofia.linksNoChannel())
    }

    @Test
    fun testHelp() {
        assertEquals(operation.handleMessage(message("list help")).size, 6)
    }

    @Test
    fun testInvalidCommand() {
        val response = operation.handleMessage(message("list helf"))

        assertEquals(response.size, 1)
        assertEquals(response[0].value, Sofia.linksInvalidCommand("helf"))
    }

    @Test
    fun testSubmitPrivateMessageLinkNotOnChannel() {
        val mockIrcAdapter = bot.get().adapter as MockIrcAdapter
        mockIrcAdapter.disableOperation("isOnChannel")

        val response = operation.handleMessage(privateMessage("submit ${TEST_CHANNEL.name} http://foo.com This is a test"))
        assertEquals(response[0].value, Sofia.linksNotOnChannel())
        assertEquals(linkDao.unapprovedLinks(TEST_CHANNEL.name).size, 0)
    }

}