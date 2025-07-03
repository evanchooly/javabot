package javabot.dao

import javabot.model.Link
import javax.inject.Inject
import org.testng.Assert.assertEquals
import org.testng.Assert.assertFalse
import org.testng.Assert.assertNotNull
import org.testng.Assert.assertTrue
import org.testng.Assert.fail
import org.testng.annotations.Test

class LinkDaoTest : BaseServiceTest() {
    @Inject lateinit var linkDao: LinkDao

    @Test
    fun testCreateRetrieveAllDelete() {
        linkDao.deleteAll()
        linkDao.addLink(
            "##java",
            "botuser",
            "http://foo.com",
            "http://foo.com is really cool, y'all",
        )

        assertEquals(linkDao.findAll().size, 1)
        linkDao.deleteAll()
        assertEquals(linkDao.findAll().size, 0)
    }

    @Test
    fun testFindLink() {
        linkDao.deleteAll()
        val linkData =
            Link("##java", "botuser", "http://bar.com", "http://foo.com is really cool, y'all")
        linkDao.addLink(linkData.channel, linkData.username, linkData.url, linkData.info)
        val retrievedLink = linkDao.get(Link(url = linkData.url))

        assertNotNull(retrievedLink?.updated)
        assertEquals(retrievedLink?.info, linkData.info)
        assertEquals(retrievedLink?.url, linkData.url)
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
        assertEquals(retrievedLink?.url, linkData.url)
        assertFalse(retrievedLink?.approved ?: true)

        linkDao.approveLink("##java", retrievedLink?.id.toString().substring(15))

        retrievedLink = linkDao.get(Link(url = linkData.url, approved = true))
        assertEquals(retrievedLink?.info, linkData.info)
        assertEquals(retrievedLink?.url, linkData.url)
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
        assertEquals(links.size, 4)

        links = linkDao.unapprovedLinks("##java")
        assertEquals(links.size, 4)

        linkDao.approveLink("##java", links[3].id.toString())
        val nextKey = links[2].id.toString()
        links = linkDao.unapprovedLinks("##java")

        assertEquals(links.size, 3)
        assertTrue(links[0].updated.isAfter(links[2].updated))

        links = linkDao.approvedLinks("##java")
        assertEquals(links.size, 1)
        linkDao.approveLink("##java", nextKey)
        links = linkDao.approvedLinks("##java")

        assertEquals(links.size, 2)
        assertTrue(links[0].updated.isBefore(links[1].updated))
    }
}
