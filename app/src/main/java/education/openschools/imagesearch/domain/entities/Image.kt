package education.openschools.imagesearch.domain.entities

data class Image(
    val copyright: String,
    val creator: String,
    val credit: String,
    val domain: String,
    val googleUrl: String,
    val imageHeight: Int,
    val imageUrl: String,
    val imageWidth: Int,
    val link: String,
    val position: Int,
    val source: String,
    val thumbnailHeight: Int,
    val thumbnailUrl: String,
    val thumbnailWidth: Int,
    val title: String
)
