package javabot.web.views;

import com.google.inject.Injector;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import java.io.IOException;

public class IndexView extends MainView {
    public IndexView(final Injector injector, final HttpServletRequest request) {
        super(injector, request);
    }

    @Override
    public String getChildView() {
        return "/index.ftl";
    }
}
