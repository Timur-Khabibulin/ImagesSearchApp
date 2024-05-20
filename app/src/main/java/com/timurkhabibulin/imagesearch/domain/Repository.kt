package com.timurkhabibulin.imagesearch.domain

import com.timurkhabibulin.imagesearch.domain.entities.Image

interface Repository {
    suspend fun searchImages(
        query: String,
        page: Int,
        pageSize: Int
    ): Result<List<Image>>
}
