package javabot.web.views;

import com.google.common.base.Charsets;
import com.google.inject.Injector;
import io.dropwizard.views.View;

import javax.servlet.http.HttpServletRequest;

public class ErrorView extends View {

    private String image;

    public ErrorView(final String template, final String image) {
        super(template, Charsets.ISO_8859_1);
        this.image = image;
    }

    public String getImage() {
        return image;
    }}
