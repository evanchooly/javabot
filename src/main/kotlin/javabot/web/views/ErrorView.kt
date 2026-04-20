package javabot.web.views

class ErrorView(val template: String, val image: String) {
    fun toModel(): Map<String, Any?> {
        return mapOf("image" to image)
    }
}
