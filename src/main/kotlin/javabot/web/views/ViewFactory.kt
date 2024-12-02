package javabot.web.views

import jakarta.servlet.http.HttpServletRequest
import java.time.LocalDateTime
import javabot.model.Admin
import javabot.model.Channel
import javabot.model.Factoid

interface ViewFactory {
    fun createChangesView(
        request: HttpServletRequest,
        page: Int,
        message: String? = null,
        date: LocalDateTime? = null
    ): ChangesView

    fun createAdminIndexView(
        request: HttpServletRequest,
        current: Admin,
        editing: Admin?
    ): AdminIndexView

    fun createChannelEditView(request: HttpServletRequest, channel: Channel): ChannelEditView

    fun createConfigurationView(request: HttpServletRequest): ConfigurationView

    fun createFactoidsView(request: HttpServletRequest, page: Int, filter: Factoid): FactoidsView

    fun createIndexView(request: HttpServletRequest): IndexView

    fun createJavadocAdminView(request: HttpServletRequest): JavadocAdminView

    fun createKarmaView(request: HttpServletRequest, page: Int): KarmaView

    fun createLogsView(request: HttpServletRequest, channel: String, date: LocalDateTime): LogsView
}
