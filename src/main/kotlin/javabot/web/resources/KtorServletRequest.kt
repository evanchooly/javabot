package javabot.web.resources

import io.ktor.server.application.*
import io.ktor.server.request.*
import java.io.BufferedReader
import java.security.Principal
import java.util.*
import javax.servlet.*
import javax.servlet.http.*

/**
 * Adapter class to make Ktor ApplicationCall work like HttpServletRequest
 * Only implements the methods used by the views
 */
class KtorServletRequest(private val call: ApplicationCall) : HttpServletRequest {
    
    override fun getCookies(): Array<Cookie>? {
        val cookies = call.request.cookies.rawCookies.map { (name, value) ->
            Cookie(name, value)
        }.toTypedArray()
        return if (cookies.isEmpty()) null else cookies
    }

    override fun getParameter(name: String?): String? {
        return call.request.queryParameters[name]
    }

    override fun getParameterMap(): MutableMap<String, Array<String>> {
        return call.request.queryParameters.entries()
            .associate { it.key to it.value.toTypedArray() }
            .toMutableMap()
    }

    override fun getSession(): HttpSession {
        return DummySession()
    }

    override fun getSession(create: Boolean): HttpSession? {
        return if (create) DummySession() else null
    }

    // Minimal session implementation
    private class DummySession : HttpSession {
        private val attributes = mutableMapOf<String, Any?>()
        
        override fun getAttribute(name: String?): Any? = attributes[name]
        override fun setAttribute(name: String?, value: Any?) { attributes[name] = value }
        override fun removeAttribute(name: String?) { attributes.remove(name) }
        override fun getAttributeNames(): Enumeration<String> = Collections.enumeration(attributes.keys)
        override fun getCreationTime(): Long = System.currentTimeMillis()
        override fun getId(): String = UUID.randomUUID().toString()
        override fun getLastAccessedTime(): Long = System.currentTimeMillis()
        override fun getMaxInactiveInterval(): Int = 3600
        override fun getServletContext(): ServletContext? = null
        override fun invalidate() {}
        override fun isNew(): Boolean = false
        override fun setMaxInactiveInterval(interval: Int) {}
        @Deprecated("Deprecated")
        override fun getSessionContext(): HttpSessionContext? = null
        @Deprecated("Deprecated")
        override fun getValue(name: String?): Any? = getAttribute(name)
        @Deprecated("Deprecated")
        override fun getValueNames(): Array<String> = attributes.keys.toTypedArray()
        @Deprecated("Deprecated")
        override fun putValue(name: String?, value: Any?) = setAttribute(name, value)
        @Deprecated("Deprecated")
        override fun removeValue(name: String?) = removeAttribute(name)
    }

    // Required methods - not all implemented
    override fun getAuthType(): String? = null
    override fun getContextPath(): String = ""
    override fun getHeader(name: String?): String? = call.request.headers[name]
    override fun getHeaderNames(): Enumeration<String> = Collections.enumeration(call.request.headers.names())
    override fun getHeaders(name: String?): Enumeration<String> = Collections.enumeration(call.request.headers.getAll(name) ?: emptyList())
    override fun getMethod(): String = call.request.httpMethod.value
    override fun getPathInfo(): String? = call.request.path()
    override fun getPathTranslated(): String? = null
    override fun getQueryString(): String? = call.request.queryString()
    override fun getRemoteUser(): String? = null
    override fun getRequestedSessionId(): String? = null
    override fun getRequestURI(): String = call.request.uri
    override fun getRequestURL(): StringBuffer = StringBuffer(call.request.origin.uri)
    override fun getServletPath(): String = ""
    override fun getUserPrincipal(): Principal? = null
    override fun isRequestedSessionIdFromCookie(): Boolean = false
    override fun isRequestedSessionIdFromURL(): Boolean = false
    @Deprecated("Deprecated")
    override fun isRequestedSessionIdFromUrl(): Boolean = false
    override fun isRequestedSessionIdValid(): Boolean = false
    override fun isUserInRole(role: String?): Boolean = false
    override fun authenticate(response: HttpServletResponse?): Boolean = false
    override fun changeSessionId(): String = ""
    override fun getIntHeader(name: String?): Int = -1
    override fun getDateHeader(name: String?): Long = -1
    override fun login(username: String?, password: String?) {}
    override fun logout() {}
    override fun getParts(): MutableCollection<Part> = mutableListOf()
    override fun getPart(name: String?): Part? = null
    override fun <T : HttpUpgradeHandler?> upgrade(handlerClass: Class<T>?): T = TODO()
    override fun getAttribute(name: String?): Any? = null
    override fun getAttributeNames(): Enumeration<String> = Collections.emptyEnumeration()
    override fun getCharacterEncoding(): String = "UTF-8"
    override fun getContentLength(): Int = -1
    override fun getContentLengthLong(): Long = -1
    override fun getContentType(): String? = call.request.contentType().toString()
    override fun getInputStream(): ServletInputStream = TODO()
    override fun getLocalAddr(): String = ""
    override fun getLocalName(): String = ""
    override fun getLocalPort(): Int = 0
    override fun getLocale(): Locale = Locale.getDefault()
    override fun getLocales(): Enumeration<Locale> = Collections.enumeration(listOf(Locale.getDefault()))
    override fun getParameterNames(): Enumeration<String> = Collections.enumeration(call.request.queryParameters.names())
    override fun getParameterValues(name: String?): Array<String>? = call.request.queryParameters.getAll(name)?.toTypedArray()
    override fun getProtocol(): String = "HTTP/1.1"
    override fun getReader(): BufferedReader = TODO()
    override fun getRealPath(path: String?): String? = null
    override fun getRemoteAddr(): String = call.request.local.remoteHost
    override fun getRemoteHost(): String = call.request.local.remoteHost
    override fun getRemotePort(): Int = call.request.local.remotePort
    override fun getRequestDispatcher(path: String?): RequestDispatcher? = null
    override fun getScheme(): String = call.request.origin.scheme
    override fun getServerName(): String = call.request.local.serverHost
    override fun getServerPort(): Int = call.request.local.serverPort
    override fun getServletContext(): ServletContext? = null
    override fun isAsyncStarted(): Boolean = false
    override fun isAsyncSupported(): Boolean = false
    override fun isSecure(): Boolean = call.request.origin.scheme == "https"
    override fun removeAttribute(name: String?) {}
    override fun setAttribute(name: String?, o: Any?) {}
    override fun setCharacterEncoding(env: String?) {}
    override fun startAsync(): AsyncContext = TODO()
    override fun startAsync(servletRequest: ServletRequest?, servletResponse: ServletResponse?): AsyncContext = TODO()
    override fun getDispatcherType(): DispatcherType = DispatcherType.REQUEST
}
