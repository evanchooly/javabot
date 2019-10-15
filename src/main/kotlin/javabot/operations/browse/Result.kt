package javabot.operations.browse

data class BrowseResult(
        val artifactId: String = "none",
        val binding: String = "none",
        val uri: String = "none",
        val components: IntArray = IntArray(0),
        val match: IntArray = IntArray(0)
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BrowseResult

        if (artifactId != other.artifactId) return false
        if (binding != other.binding) return false
        if (uri != other.uri) return false
        if (!components.contentEquals(other.components)) return false
        if (!match.contentEquals(other.match)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = artifactId.hashCode()
        result = 31 * result + binding.hashCode()
        result = 31 * result + uri.hashCode()
        result = 31 * result + components.contentHashCode()
        result = 31 * result + match.contentHashCode()
        return result
    }
}