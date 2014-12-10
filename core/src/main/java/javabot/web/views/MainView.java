package javabot.web.views;

import com.antwerkz.sofia.Sofia;
import com.google.common.base.Charsets;
import com.google.common.base.Optional;
import com.google.inject.Injector;
import io.dropwizard.views.View;
import javabot.dao.AdminDao;
import javabot.dao.ChannelDao;
import javabot.dao.FactoidDao;
import javabot.model.Channel;
import javabot.web.JavabotConfiguration;
import javabot.web.model.InMemoryUserCache;
import javabot.web.model.User;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class MainView extends View {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd hh:mm");
    private HttpServletRequest request;

    @Inject
    private AdminDao adminDao;

    @Inject
    private ChannelDao channelDao;

    @Inject
    private FactoidDao factoidDao;

    private List<String> errors = new ArrayList<>();

    public MainView() {
        super("/main.ftl", Charsets.ISO_8859_1);
    }

    public MainView(final Injector injector, final HttpServletRequest request) {
        this();
        injector.injectMembers(this);
        this.request = request;
    }

    public Sofia sofia() {
        return new Sofia();
    }

    public abstract String getChildView();

    public long getFactoidCount() {
        return factoidDao.count();
    }

    public boolean loggedIn() {
        Cookie cookie = getSessionCookie();
        return cookie != null && InMemoryUserCache.INSTANCE.getBySessionToken(cookie.getValue()).isPresent();
    }

    public boolean isAdmin() {
        Cookie cookie = getSessionCookie();
        if (cookie != null) {
            Optional<User> optional = InMemoryUserCache.INSTANCE.getBySessionToken(cookie.getValue());
            return optional.isPresent() && adminDao.getAdminByEmailAddress(optional.get().getEmail()) != null;
        } else {
            return false;
        }
    }

    public String getCurrentChannel() {
        return "";
    }

    public List<Channel> getChannels() {
        return channelDao.getChannels(isAdmin());
    }

    public String encode(String value) throws UnsupportedEncodingException {
        return java.net.URLEncoder.encode(value, "UTF-8");
    }

    private Cookie getSessionCookie() {
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(JavabotConfiguration.SESSION_TOKEN_NAME)) {
                return cookie;
            }
        }
        return null;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void addError(String message) {
        errors.add(message);
    }

    public List<String> getErrors() {
        return errors;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }
    public String format(final LocalDateTime date) {
        return DATE_TIME_FORMATTER.format(date);
    }
}
