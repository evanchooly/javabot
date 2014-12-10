package javabot.web.views;

import com.google.inject.Inject;
import com.google.inject.Injector;
import javabot.dao.ApiDao;
import javabot.javadoc.JavadocApi;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class JavadocView extends MainView {
    @Inject
    private ApiDao apiDao;

    public JavadocView(final Injector injector, final HttpServletRequest request) {
        super(injector, request);
    }

    @Override
    public String getChildView() {
        return "admin/javadoc.ftl";
    }

    public List<JavadocApi> apis() {
        return apiDao.findAll();
    }
}
