package javabot.qtest.dao

import io.quarkus.test.junit.QuarkusTest
import javabot.dao.BaseServiceTest
import javabot.dao.LinkDao
import javabot.model.Link
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test

@QuarkusTest
class LinkDaoTest : BaseServiceTest() {
    private val linkDao: LinkDao by lazy { injector.getInstance(LinkDao::class.java) }

    @Test
    fun testCreateRetrieveAllDelete() {
        linkDao.deleteAll()
        linkDao.addLink(
            "##java",
            "botuser",
            "http://foo.com",
            "http://foo.com is really cool, y'all",
        )

        assertEquals(1, linkDao.findAll().size)
        linkDao.deleteAll()
        assertEquals(0, linkDao.findAll().size)
    }

    @Test
    fun testFindLink() {
        linkDao.deleteAll()
        val linkData =
            Link("##java", "botuser", "http://bar.com", "http://foo.com is really cool, y'all")
        linkDao.addLink(linkData.channel, linkData.username, linkData.url, linkData.info)
        val retrievedLink = linkDao.get(Link(url = linkData.url))

        assertNotNull(retrievedLink?.updated)
        assertEquals(linkData.info, retrievedLink?.info)
        assertEquals(linkData.url, retrievedLink?.url)
        assertFalse(retrievedLink?.approved ?: true)
    }

    @Test
    fun testApproveLink() {
        linkDao.deleteAll()
        val linkData =
            Link(
                "##java",
                "botuser",
                "http://bar.com",
                "http://foo.com is really cool, y'all",
                false,
            )
        linkDao.addLink(linkData.channel, linkData.username, linkData.url, linkData.info)
        var retrievedLink = linkDao.get(Link(url = linkData.url))
        assertEquals(linkData.url, retrievedLink?.url)
        assertFalse(retrievedLink?.approved ?: true)

        linkDao.approveLink("##java", retrievedLink?.id.toString().substring(15))

        retrievedLink = linkDao.get(Link(url = linkData.url, approved = true))
        assertEquals(linkData.info, retrievedLink?.info)
        assertEquals(linkData.url, retrievedLink?.url)
        assertTrue(retrievedLink?.approved ?: true)

        try {
            linkDao.approveLink("##java", linkData.url)
            fail("should have thrown an exception on re-approval of link")
        } catch (ignored: IllegalArgumentException) {}
        try {
            linkDao.approveLink("##java", "abcdef")
            fail("should have thrown an exception on approval of nonexistent link")
        } catch (ignored: IllegalArgumentException) {}
    }

    @Test
    fun getListsOfLinks() {
        linkDao.deleteAll()
        listOf("a", "b", "c", "d").forEach {
            linkDao.addLink("##java", "botuser", "http://$it.com", "this is $it")
            Thread.sleep(100)
        }
        // all links, regardless of status
        var links = linkDao.findAll()
        assertEquals(4, links.size)

        links = linkDao.unapprovedLinks("##java")
        assertEquals(4, links.size)

        linkDao.approveLink("##java", links[3].id.toString())
        val nextKey = links[2].id.toString()
        links = linkDao.unapprovedLinks("##java")

        assertEquals(3, links.size)
        assertTrue(links[0].updated.isAfter(links[2].updated))

        links = linkDao.approvedLinks("##java")
        assertEquals(1, links.size)
        linkDao.approveLink("##java", nextKey)
        links = linkDao.approvedLinks("##java")

        assertEquals(2, links.size)
        assertTrue(links[0].updated.isBefore(links[1].updated))
    }
}
