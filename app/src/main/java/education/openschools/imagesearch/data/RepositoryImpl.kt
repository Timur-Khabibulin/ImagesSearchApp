package education.openschools.imagesearch.data

import education.openschools.imagesearch.domain.Repository
import education.openschools.imagesearch.domain.entities.Image
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject


class RepositoryImpl @Inject constructor(
    private val searchApi: ImagesSearchApi,
    private val dispatcher: CoroutineDispatcher,
) : Repository {
    override suspend fun searchImages(
        query: String,
        page: Int,
        pageSize: Int
    ): Result<List<Image>> = withContext(dispatcher) {
        try {
            val response = searchApi.searchImages(query, page, pageSize).execute()
            response.body()?.images?.let {
                return@withContext Result.success(it)
            } ?: Result.failure(Exception())
        } catch (e: Exception) {
            return@withContext Result.failure(e)
        }
    }
}
