package javabot.web.resources

import io.quarkus.qute.TemplateInstance
import java.util.Random

class PublicErrorResource {
    companion object {
        private val IMAGE_403 = arrayOf("403_1.gif", "403_2.gif", "403_3.gif")
        private val IMAGE_404 = arrayOf("404_1.gif", "404_2.gif", "404_3.gif", "404_4.gif")
        private val IMAGE_500 = arrayOf("500.gif")

        @JvmStatic external fun error403(image: String): TemplateInstance

        @JvmStatic external fun error404(image: String): TemplateInstance

        @JvmStatic external fun error500(image: String): TemplateInstance

        fun view403() = error403(getRandomImage(IMAGE_403))

        fun view404() = error404(getRandomImage(IMAGE_404))

        fun view500() = error500(getRandomImage(IMAGE_500))

        private fun getRandomImage(images: Array<String>): String {
            return images[Random().nextInt(images.size)]
        }
    }
}
