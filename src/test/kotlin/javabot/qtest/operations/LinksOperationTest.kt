package javabot.qtest.operations

import com.antwerkz.sofia.Sofia
import io.quarkus.test.junit.QuarkusTest
import javabot.BaseTest
import javabot.dao.LinkDao
import javabot.mocks.MockIrcAdapter
import javabot.operations.LinksOperation
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@QuarkusTest
class LinksOperationTest : BaseTest() {

    private val linkDao: LinkDao by lazy { injector.getInstance(LinkDao::class.java) }
    private val operation: LinksOperation by lazy {
        injector.getInstance(LinksOperation::class.java)
    }

    @BeforeEach
    fun deleted() {
        linkDao.deleteAll()
    }

    @Test
    fun testSubmitLink() {
        val response = operation.handleMessage(message("~submit http://foo.com This is a test"))
        assertEquals(Sofia.linksAccepted("http://foo.com", TEST_CHANNEL.name), response[0].value)
        assertEquals(1, linkDao.unapprovedLinks(TEST_CHANNEL.name).size)
    }

    @Test
    fun testSubmitNoLink() {
        val response = operation.handleMessage(message("~submit foo.com This is a test"))
        assertEquals(Sofia.linksRejectedNoUrl(), response[0].value)
        assertEquals(0, linkDao.unapprovedLinks(TEST_CHANNEL.name).size)
    }

    @Test
    fun testSubmitPrivateMessageLink() {
        var response =
            operation.handleMessage(privateMessage("submit http://foo.com This is a test"))
        assertEquals(Sofia.linksNoChannel(), response[0].value)
        assertEquals(0, linkDao.unapprovedLinks(TEST_CHANNEL.name).size)

        response =
            operation.handleMessage(
                privateMessage("submit ${TEST_CHANNEL.name} http://foo.com This is a test")
            )
        assertEquals(Sofia.linksAccepted("http://foo.com", TEST_CHANNEL.name), response[0].value)
        assertEquals(1, linkDao.unapprovedLinks(TEST_CHANNEL.name).size)
    }

    @Test
    fun testListLinks() {
        var response = operation.handleMessage(message("~submit http://foo.com This is a test"))
        assertEquals(Sofia.linksAccepted("http://foo.com", TEST_CHANNEL.name), response[0].value)
        assertEquals(1, linkDao.unapprovedLinks(TEST_CHANNEL.name).size)

        response = operation.handleMessage(message("~submit http://bar.com This is another test"))
        assertEquals(Sofia.linksAccepted("http://bar.com", TEST_CHANNEL.name), response[0].value)
        assertEquals(2, linkDao.unapprovedLinks(TEST_CHANNEL.name).size)

        response = operation.handleMessage(message("~list unapproved ${TEST_CHANNEL.name}"))
        assertEquals(2, response.size)

        val firstKey = response[0].value.split(" ")[0]
        val secondKey = response[1].value.split(" ")[0]
        response = operation.handleMessage(message("~list approve ${TEST_CHANNEL.name} $firstKey"))
        assertEquals(1, response.size)
        assertEquals(
            Sofia.linksVerbApplied(firstKey, "approved", TEST_CHANNEL.name),
            response[0].value,
        )

        response = operation.handleMessage(message("~list approved ${TEST_CHANNEL.name} 10"))
        assertEquals(1, response.size)

        response = operation.handleMessage(message("~list unapproved ${TEST_CHANNEL.name}"))
        assertEquals(1, response.size)

        response = operation.handleMessage(message("~list reject ${TEST_CHANNEL.name} $secondKey"))
        assertEquals(1, response.size)
        assertEquals(
            Sofia.linksVerbApplied(secondKey, "rejected", TEST_CHANNEL.name),
            response[0].value,
        )

        response = operation.handleMessage(message("~list unapproved ${TEST_CHANNEL.name}"))
        assertEquals(1, response.size)
        assertEquals(Sofia.linksNoLinksOfStatus("unapproved", TEST_CHANNEL.name), response[0].value)
    }

    @Test
    fun testApproveInvalidKey() {
        operation.handleMessage(message("~submit http://foo.com This is a test"))
        var response = operation.handleMessage(message("~list unapproved ${TEST_CHANNEL.name}"))

        assertEquals(1, response.size)

        val firstKey = response[0].value.split(" ")[0]
        response =
            operation.handleMessage(message("~list approve ${TEST_CHANNEL.name} ${firstKey}a"))

        assertEquals(1, response.size)
        assertEquals(Sofia.linksNotFound("${firstKey}a"), response[0].value)
        response =
            operation.handleMessage(message("~list reject ${TEST_CHANNEL.name} ${firstKey}a"))

        assertEquals(1, response.size)
        assertEquals(Sofia.linksNotFound("${firstKey}a"), response[0].value)
    }

    @Test
    fun testApproveNoKey() {
        operation.handleMessage(message("~submit http://foo.com This is a test"))
        var response = operation.handleMessage(message("~list unapproved ${TEST_CHANNEL.name}"))

        assertEquals(1, response.size)

        response = operation.handleMessage(message("~list approve ${TEST_CHANNEL.name}"))

        assertEquals(1, response.size)
        assertEquals(Sofia.linksNoKeySpecified("approve"), response[0].value)
        response = operation.handleMessage(message("~list reject ${TEST_CHANNEL.name}"))

        assertEquals(1, response.size)
        assertEquals(Sofia.linksNoKeySpecified("reject"), response[0].value)
    }

    @Test
    fun testWrongChannelName() {
        operation.handleMessage(message("~submit http://foo.com This is a test"))
        var response = operation.handleMessage(message("~list unapproved ${TEST_CHANNEL.name}"))

        assertEquals(1, response.size)
        response = operation.handleMessage(message("~list unapproved ${TEST_CHANNEL.name}a"))

        assertEquals(1, response.size)
        assertEquals(Sofia.linksWrongChannel(TEST_CHANNEL.name), response[0].value)
        response = operation.handleMessage(message("~list approved ${TEST_CHANNEL.name}a"))

        assertEquals(1, response.size)
        assertEquals(Sofia.linksWrongChannel(TEST_CHANNEL.name), response[0].value)
    }

    @Test
    fun testCountModifier() {
        listOf(
                "foo",
                "bar",
                "baz",
                "bletch",
                "quux",
                "corge",
                "grault",
                "garply",
                "plugh",
                "xyzzy",
                "wibble",
            )
            .forEachIndexed { index, domain ->
                val response =
                    operation.handleMessage(
                        message("~submit http://$domain.com This is a test for $domain")
                    )
                assertEquals(
                    Sofia.linksAccepted("http://$domain.com", TEST_CHANNEL.name),
                    response[0].value,
                )
                assertEquals(index + 1, linkDao.unapprovedLinks(TEST_CHANNEL.name).size)
            }
        var response = operation.handleMessage(message("~list unapproved ${TEST_CHANNEL.name}"))

        assertEquals(5, response.size)
        response = operation.handleMessage(message("~list unapproved 3"))

        assertEquals(3, response.size)

        response = operation.handleMessage(message("~list unapproved a3"))

        assertEquals(5, response.size)
    }

    @Test
    fun testPrivateListLinks() {
        var response = operation.handleMessage(message("submit http://foo.com This is a test"))
        assertEquals(Sofia.linksAccepted("http://foo.com", TEST_CHANNEL.name), response[0].value)
        assertEquals(1, linkDao.unapprovedLinks(TEST_CHANNEL.name).size)
        response = operation.handleMessage(message("submit http://bar.com This is another test"))
        assertEquals(Sofia.linksAccepted("http://bar.com", TEST_CHANNEL.name), response[0].value)
        assertEquals(2, linkDao.unapprovedLinks(TEST_CHANNEL.name).size)

        response = operation.handleMessage(privateMessage("list unapproved ${TEST_CHANNEL.name}a"))
        assertEquals(1, response.size)
        assertEquals(Sofia.linksNoChannel(), response[0].value)

        response = operation.handleMessage(privateMessage("list unapproved ${TEST_CHANNEL.name}"))

        assertEquals(2, response.size)
        val firstKey = response[0].value.split(" ")[0]
        val secondKey = response[1].value.split(" ")[0]

        response = operation.handleMessage(privateMessage("list approve $firstKey"))
        assertEquals(1, response.size)
        assertEquals(Sofia.linksNoChannel(), response[0].value)

        response =
            operation.handleMessage(privateMessage("list approve ${TEST_CHANNEL.name} $firstKey"))
        assertEquals(1, response.size)
        assertEquals(
            Sofia.linksVerbApplied(firstKey, "approved", TEST_CHANNEL.name),
            response[0].value,
        )

        response = operation.handleMessage(privateMessage("list approved ${TEST_CHANNEL.name} 10"))
        assertEquals(1, response.size)

        response = operation.handleMessage(privateMessage("list unapproved ${TEST_CHANNEL.name}"))

        assertEquals(1, response.size)
        operation.handleMessage(privateMessage("list reject ${TEST_CHANNEL.name} $secondKey"))

        response = operation.handleMessage(privateMessage("list unapproved ${TEST_CHANNEL.name}"))

        assertEquals(1, response.size)
        assertEquals(Sofia.linksNoLinksOfStatus("unapproved", TEST_CHANNEL.name), response[0].value)

        response = operation.handleMessage(privateMessage("list unapproved"))

        assertEquals(1, response.size)
        assertEquals(Sofia.linksNoChannel(), response[0].value)
    }

    @Test
    fun testHelp() {
        assertEquals(6, operation.handleMessage(message("list help")).size)
    }

    @Test
    fun testInvalidCommand() {
        val response = operation.handleMessage(message("list helf"))

        assertEquals(1, response.size)
        assertEquals(Sofia.linksInvalidCommand("helf"), response[0].value)
    }

    @Test
    fun testApproveWithoutBeingOp() {
        val mockIrcAdapter = bot.get().adapter as MockIrcAdapter

        var response = operation.handleMessage(message("~submit http://foo.com This is a test"))
        assertEquals(Sofia.linksAccepted("http://foo.com", TEST_CHANNEL.name), response[0].value)
        assertEquals(1, linkDao.unapprovedLinks(TEST_CHANNEL.name).size)

        response = operation.handleMessage(message("~submit http://bar.com This is another test"))
        assertEquals(Sofia.linksAccepted("http://bar.com", TEST_CHANNEL.name), response[0].value)
        assertEquals(2, linkDao.unapprovedLinks(TEST_CHANNEL.name).size)

        response = operation.handleMessage(message("~list unapproved ${TEST_CHANNEL.name}"))
        assertEquals(2, response.size)

        val firstKey = response[0].value.split(" ")[0]
        val secondKey = response[1].value.split(" ")[0]
        response = operation.handleMessage(message("~list approve ${TEST_CHANNEL.name} $firstKey"))
        assertEquals(1, response.size)
        assertEquals(
            Sofia.linksVerbApplied(firstKey, "approved", TEST_CHANNEL.name),
            response[0].value,
        )
        mockIrcAdapter.disableOperation("isOp")
        response = operation.handleMessage(message("~list approve ${TEST_CHANNEL.name} $secondKey"))
        assertEquals(1, response.size)
        assertEquals(Sofia.linksNotAnOp(TEST_CHANNEL.name), response[0].value)

        mockIrcAdapter.resetDisabledOperations()
    }

    @Test
    fun testSubmitPrivateMessageLinkNotOnChannel() {
        val mockIrcAdapter = bot.get().adapter as MockIrcAdapter
        mockIrcAdapter.disableOperation("isOnChannel")

        val response =
            operation.handleMessage(
                privateMessage("submit ${TEST_CHANNEL.name} http://foo.com This is a test")
            )
        assertEquals(Sofia.linksNotOnChannel(), response[0].value)
        assertEquals(0, linkDao.unapprovedLinks(TEST_CHANNEL.name).size)
        mockIrcAdapter.resetDisabledOperations()
    }

    @Test
    fun testNoResultsAvailable() {
        val response =
            operation.handleMessage(privateMessage("list unapproved ${TEST_CHANNEL.name}"))

        assertEquals(1, response.size)
        assertEquals(Sofia.linksNoLinksOfStatus("unapproved", TEST_CHANNEL.name), response[0].value)
    }
}
