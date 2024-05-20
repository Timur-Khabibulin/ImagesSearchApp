package com.timurkhabibulin.imagesearch.domain.entities

data class SearchParameters(
    val engine: String,
    val gl: String,
    val hl: String,
    val num: Int,
    val q: String,
    val type: String
)
