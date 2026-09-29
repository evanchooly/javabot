package javabot.web.views

import java.util.UUID
import javabot.BaseTest
import javabot.web.model.Authority
import javabot.web.model.InMemoryUserCache
import javabot.web.model.User

/**
 * Builds a session token for a logged-in user, registering the user in [InMemoryUserCache] so that
 * [TemplateService] resolves it. Returns null when a logged-out session is wanted.
 */
fun mockSessionToken(loggedIn: Boolean): String? {
    if (!loggedIn) return null
    val tempUser = User(UUID.randomUUID(), BaseTest.BOT_EMAIL, UUID.randomUUID().toString())
    tempUser.authorities.add(Authority.ROLE_PUBLIC)
    InMemoryUserCache.INSTANCE.put(tempUser)
    return tempUser.sessionToken.toString()
}
