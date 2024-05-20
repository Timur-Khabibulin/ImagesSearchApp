package education.openschools.imagesearch.domain

import androidx.paging.PagingSource
import androidx.paging.PagingState

private const val START_PAGE_INDEX = 1

class ItemsPagingSource<T : Any>(
    private val startPageIndex: Int = START_PAGE_INDEX,
    private val getItems: suspend (page: Int) -> Result<List<T>>
) : PagingSource<Int, T>() {
    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val page = params.key ?: startPageIndex
        val result = getItems(page)

        return if (result.isSuccess) LoadResult.Page(
            data = result.getOrNull()!!,
            prevKey = if (page == startPageIndex) null else page - 1,
            nextKey = if (result.getOrNull()!!.isEmpty()) null else page + 1
        ) else {
            LoadResult.Error(result.exceptionOrNull()!!)
        }
    }
}
