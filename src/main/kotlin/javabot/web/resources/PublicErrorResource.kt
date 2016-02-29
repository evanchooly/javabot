package javabot.web.resources

import io.dropwizard.views.View
import javabot.web.views.ErrorView

import java.util.Random

class PublicErrorResource {
    fun view500(): View {
        return ErrorView("/error/500.ftl", getRandomImage(IMAGE_500))
    }

    private fun getRandomImage(images: Array<String>): String {
        return images[Random().nextInt(images.size)]
    }

    companion object {
        private val IMAGE_500 = arrayOf("500.gif")
    }
}
