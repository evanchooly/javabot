package javabot.web.resources

import io.dropwizard.views.common.View
import java.util.Random
import javabot.web.views.ErrorView

class PublicErrorResource {
    companion object {
        private val IMAGE_403 = arrayOf("403_1.gif", "403_2.gif", "403_3.gif")
        private val IMAGE_404 = arrayOf("404_1.gif", "404_2.gif", "404_3.gif", "404_4.gif")
        private val IMAGE_500 = arrayOf("500.gif")

        fun view403(): View {
            return ErrorView("/error/403.ftl", getRandomImage(IMAGE_403))
        }

        fun view404(): View {
            return ErrorView("/error/404.ftl", getRandomImage(IMAGE_404))
        }

        fun view500(): View {
            return ErrorView("/error/500.ftl", getRandomImage(IMAGE_500))
        }

        private fun getRandomImage(images: Array<String>): String {
            return images[Random().nextInt(images.size)]
        }
    }
}
