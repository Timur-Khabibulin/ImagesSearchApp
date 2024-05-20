package com.timurkhabibulin.imagesearch.domain.entities

data class SearchResponse(
    val images: List<Image>,
    val searchParameters: SearchParameters
)
