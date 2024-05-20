package education.openschools.imagesearch.domain

import education.openschools.imagesearch.domain.entities.Image

interface Repository {
    suspend fun searchImages(
        query: String,
        page: Int,
        pageSize: Int
    ): Result<List<Image>>
}
