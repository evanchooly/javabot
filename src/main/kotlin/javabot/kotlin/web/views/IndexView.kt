package javabot.kotlin.web.views

import com.google.inject.Injector

import javax.servlet.http.HttpServletRequest

public class IndexView(injector: Injector, request: HttpServletRequest) : MainView(injector, request) {

    override fun getChildView(): String {
        return "/index.ftl"
    }
}
