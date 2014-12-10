package javabot.web.resources;

import io.dropwizard.views.View;
import javabot.web.views.ErrorView;

import java.util.Random;

public class PublicErrorResource {
    private static final String[] IMAGE_500 = {"500.gif"};
    public View view500() {
        return new ErrorView("/error/500.ftl", getRandomImage(IMAGE_500));
    }

    private String getRandomImage(final String[] images) {
        return images[new Random().nextInt(images.length)];
    }
}
