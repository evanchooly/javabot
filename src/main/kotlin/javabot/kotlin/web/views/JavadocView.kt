package javabot.kotlin.web.views

import com.google.inject.Inject
import com.google.inject.Injector
import javabot.dao.ApiDao
import javabot.javadoc.JavadocApi

import javax.servlet.http.HttpServletRequest

public class JavadocView(injector: Injector, request: HttpServletRequest) : MainView(injector, request) {
    @Inject
    private val apiDao: ApiDao? = null

    override fun getChildView(): String {
        return "admin/javadoc.ftl"
    }

    public fun apis(): List<JavadocApi> {
        return apiDao!!.findAll()
    }
}
