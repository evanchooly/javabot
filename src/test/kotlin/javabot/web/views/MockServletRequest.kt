package javabot.web.views

import javabot.BaseTest
import javabot.web.JavabotConfiguration
import javabot.web.model.Authority
import javabot.web.model.InMemoryUserCache
import javabot.web.model.User
import org.brickred.socialauth.util.AccessGrant

import javax.servlet.AsyncContext
import javax.servlet.DispatcherType
import javax.servlet.RequestDispatcher
import javax.servlet.ServletContext
import javax.servlet.ServletException
import javax.servlet.ServletInputStream
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession
import javax.servlet.http.Part
import java.io.BufferedReader
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.security.Principal
import java.util.Enumeration
import java.util.Locale
import java.util.UUID

class MockServletRequest(loggedIn: Boolean) : HttpServletRequest {
    private val cookies: Array<Cookie>

    init {
        if (loggedIn) {
            val tempUser = User(UUID.randomUUID(), BaseTest.BOT_EMAIL, UUID.randomUUID().toString(), AccessGrant())
            tempUser.authorities.add(Authority.ROLE_PUBLIC)

            InMemoryUserCache.INSTANCE.put(tempUser)

            cookies = arrayOf(Cookie(JavabotConfiguration.SESSION_TOKEN_NAME, tempUser.sessionToken.toString()))
        } else {
            cookies = arrayOf<Cookie>()
        }
    }

    override fun getAuthType(): String? {
        return null
    }

    override fun getCookies(): Array<Cookie> {
        return cookies
    }

    override fun getDateHeader(name: String): Long {
        return 0
    }

    override fun getHeader(name: String): String? {
        return null
    }

    override fun getHeaders(name: String): Enumeration<String>? {
        return null
    }

    override fun getHeaderNames(): Enumeration<String>? {
        return null
    }

    override fun getIntHeader(name: String): Int {
        return 0
    }

    override fun getMethod(): String? {
        return null
    }

    override fun getPathInfo(): String? {
        return null
    }

    override fun getPathTranslated(): String? {
        return null
    }

    override fun getContextPath(): String? {
        return null
    }

    override fun getQueryString(): String? {
        return null
    }

    override fun getRemoteUser(): String? {
        return null
    }

    override fun isUserInRole(role: String): Boolean {
        return false
    }

    override fun getUserPrincipal(): Principal? {
        return null
    }

    override fun getRequestedSessionId(): String? {
        return null
    }

    override fun getRequestURI(): String? {
        return null
    }

    override fun getRequestURL(): StringBuffer? {
        return null
    }

    override fun getServletPath(): String? {
        return null
    }

    override fun getSession(create: Boolean): HttpSession? {
        return null
    }

    override fun getSession(): HttpSession? {
        return null
    }

    override fun isRequestedSessionIdValid(): Boolean {
        return false
    }

    override fun isRequestedSessionIdFromCookie(): Boolean {
        return false
    }

    override fun isRequestedSessionIdFromURL(): Boolean {
        return false
    }

    override fun isRequestedSessionIdFromUrl(): Boolean {
        return false
    }

    @Throws(IOException::class, ServletException::class)
    override fun authenticate(response: HttpServletResponse): Boolean {
        return false
    }

    @Throws(ServletException::class)
    override fun login(username: String, password: String) {

    }

    @Throws(ServletException::class)
    override fun logout() {

    }

    @Throws(IOException::class, ServletException::class)
    override fun getParts(): Collection<Part>? {
        return null
    }

    @Throws(IOException::class, ServletException::class)
    override fun getPart(name: String): Part? {
        return null
    }

    override fun getAttribute(name: String): Any? {
        return null
    }

    override fun getAttributeNames(): Enumeration<String>? {
        return null
    }

    override fun getCharacterEncoding(): String? {
        return null
    }

    @Throws(UnsupportedEncodingException::class)
    override fun setCharacterEncoding(env: String) {

    }

    override fun getContentLength(): Int {
        return 0
    }

    override fun getContentType(): String? {
        return null
    }

    @Throws(IOException::class)
    override fun getInputStream(): ServletInputStream? {
        return null
    }

    override fun getParameter(name: String): String? {
        return null
    }

    override fun getParameterNames(): Enumeration<String>? {
        return null
    }

    override fun getParameterValues(name: String): Array<String> {
        return arrayOf()
    }

    override fun getParameterMap(): Map<String, Array<String>>? {
        return null
    }

    override fun getProtocol(): String? {
        return null
    }

    override fun getScheme(): String? {
        return null
    }

    override fun getServerName(): String? {
        return null
    }

    override fun getServerPort(): Int {
        return 0
    }

    @Throws(IOException::class)
    override fun getReader(): BufferedReader? {
        return null
    }

    override fun getRemoteAddr(): String? {
        return null
    }

    override fun getRemoteHost(): String? {
        return null
    }

    override fun setAttribute(name: String, o: Any) {

    }

    override fun removeAttribute(name: String) {

    }

    override fun getLocale(): Locale? {
        return null
    }

    override fun getLocales(): Enumeration<Locale>? {
        return null
    }

    override fun isSecure(): Boolean {
        return false
    }

    override fun getRequestDispatcher(path: String): RequestDispatcher? {
        return null
    }

    override fun getRealPath(path: String): String? {
        return null
    }

    override fun getRemotePort(): Int {
        return 0
    }

    override fun getLocalName(): String? {
        return null
    }

    override fun getLocalAddr(): String? {
        return null
    }

    override fun getLocalPort(): Int {
        return 0
    }

    override fun getServletContext(): ServletContext? {
        return null
    }

    @Throws(IllegalStateException::class)
    override fun startAsync(): AsyncContext? {
        return null
    }

    @Throws(IllegalStateException::class)
    override fun startAsync(servletRequest: ServletRequest, servletResponse: ServletResponse): AsyncContext? {
        return null
    }

    override fun isAsyncStarted(): Boolean {
        return false
    }

    override fun isAsyncSupported(): Boolean {
        return false
    }

    override fun getAsyncContext(): AsyncContext? {
        return null
    }

    override fun getDispatcherType(): DispatcherType? {
        return null
    }
}
