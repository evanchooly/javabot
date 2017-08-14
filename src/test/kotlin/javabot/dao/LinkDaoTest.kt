package javabot.dao

import javabot.model.Link
import org.testng.annotations.Test
import javax.inject.Inject
import org.testng.Assert.*
import java.time.LocalDateTime

class LinkDaoTest : BaseServiceTest() {
    @Inject
    protected lateinit var linkDao: LinkDao

    @Test
    fun testCreateRetrieveAllDelete() {
        linkDao.deleteAll()
        linkDao.addLink("##java", "botuser", "http://foo.com", "http://foo.com is really cool, y'all")
        var links: List<Link> = linkDao.findAll()
        assertEquals(links.size, 1)
        linkDao.deleteAll()
        assertEquals(linkDao.findAll().size, 0)
    }

    @Test
    fun testFindLink() {
        linkDao.deleteAll()
        val linkData = Link("##java", "botuser", "http://bar.com", "http://foo.com is really cool, y'all")
        linkDao.addLink(linkData.channel, linkData.username, linkData.url, linkData.info)
        val retrievedLink = linkDao[Link(url = linkData.url)]
        assertNotNull(retrievedLink.updated)
        assertEquals(retrievedLink.info, linkData.info)
        assertEquals(retrievedLink.url, linkData.url)
        assertFalse(retrievedLink.approved ?: true)
    }

    @Test
    fun testApproveLink() {
        linkDao.deleteAll()
        val linkData = Link("##java", "botuser", "http://bar.com", "http://foo.com is really cool, y'all", false)
        linkDao.addLink(linkData.channel, linkData.username, linkData.url, linkData.info)
        var retrievedLink = linkDao[Link(url = linkData.url)]
        assertEquals(retrievedLink.url, linkData.url)
        assertFalse(retrievedLink.approved ?: true)

        linkDao.approveLink(retrievedLink.id.toString().substring(15))

        retrievedLink = linkDao[Link(url = linkData.url)]
        assertEquals(retrievedLink.info, linkData.info)
        assertEquals(retrievedLink.url, linkData.url)
        assertTrue(retrievedLink.approved ?: false)

        try {
            linkDao.approveLink(linkData.url)
            fail("should have thrown an exception on re-approval of link")
        } catch(ignored: IllegalArgumentException) {
        }
    }

    @Test
    fun getListsOfLinks() {
        linkDao.deleteAll()
        listOf("a", "b", "c", "d").forEach {
            linkDao.addLink("##java", "botuser", "http://$it.com", "this is $it")
        }
        // all links, regardless of status
        var links=linkDao.findAll()
        assertEquals(links.size, 4)

        links=linkDao.unapprovedLinks()
        assertEquals(links.size, 4)

        linkDao.approveLink(links[3].id.toString())
        val nextKey=links[2].id.toString()
        links=linkDao.unapprovedLinks()
        println(links.joinToString("\n"))
        assertEquals(links.size, 3)
        assertTrue((links[0].updated?: LocalDateTime.MIN).isAfter(links[2].updated?:LocalDateTime.MIN))

        links=linkDao.approvedLinks()
        assertEquals(links.size, 1)
        linkDao.approveLink(nextKey)
        links=linkDao.approvedLinks()
        println(links.joinToString("\n"))
        assertEquals(links.size, 2)
        assertTrue((links[0].updated?: LocalDateTime.MIN).isBefore(links[1].updated?:LocalDateTime.MIN))

    }
}