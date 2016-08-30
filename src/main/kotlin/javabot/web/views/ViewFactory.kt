package javabot.web.views

import com.google.inject.assistedinject.Assisted
import javabot.model.Admin
import javabot.model.Channel
import javabot.model.Factoid
import java.time.LocalDateTime
import javax.annotation.Nullable
import javax.servlet.http.HttpServletRequest

interface ViewFactory {
    fun createChangesView(request: HttpServletRequest, page: Int, message: String? = null, date: LocalDateTime? = null):
            ChangesView

    fun createAdminIndexView(request: HttpServletRequest, @Assisted("current") current: Admin,
                             @Assisted("editing") editing: Admin?): AdminIndexView

    fun createChannelEditView(request: HttpServletRequest, channel: Channel): ChannelEditView

    fun createConfigurationView(request: HttpServletRequest): ConfigurationView

    fun createFactoidsView(request: HttpServletRequest, page: Int, filter: Factoid): FactoidsView

    fun createIndexView(request: HttpServletRequest): IndexView

    fun createJavadocAdminView(request: HttpServletRequest): JavadocAdminView

    fun createKarmaView(request: HttpServletRequest, page: Int): KarmaView

    fun createLogsView(request: HttpServletRequest, channel: String, date: LocalDateTime): LogsView
}
