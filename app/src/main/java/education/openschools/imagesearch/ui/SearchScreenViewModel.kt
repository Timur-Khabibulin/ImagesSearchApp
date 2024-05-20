package education.openschools.imagesearch.ui

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import education.openschools.imagesearch.domain.ItemsPagingSource
import education.openschools.imagesearch.domain.Repository
import education.openschools.imagesearch.domain.entities.Image
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

private const val PAGE_SIZE = 10

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery
    private val searchTrigger = MutableStateFlow(false)

    @OptIn(ExperimentalCoroutinesApi::class)
    val images = searchTrigger.flatMapLatest {
        loadImages(_searchQuery.value)
    }.cachedIn(viewModelScope)

    fun makeSearch(text: String) {
        if (text.isNotBlank()) {
            _searchQuery.value = text
            searchTrigger.value = !searchTrigger.value
        }
    }

    fun openDeepLink(context: Context, url: String) {
        val deepLinkIntent = Intent(
            Intent.ACTION_VIEW,
            url.toUri()
        )
        context.startActivity(deepLinkIntent)
    }

    private fun loadImages(query: String): Flow<PagingData<Image>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = {
                ItemsPagingSource { page ->
                    repository.searchImages(query, page, PAGE_SIZE)
                }
            }
        ).flow.flowOn(Dispatchers.IO)
    }
}
