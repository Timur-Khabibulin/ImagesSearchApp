package education.openschools.imagesearch.domain.entities

data class SearchResponse(
    val images: List<Image>,
    val searchParameters: SearchParameters
)
