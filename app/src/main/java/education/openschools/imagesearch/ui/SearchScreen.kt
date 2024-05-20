package education.openschools.imagesearch.ui

import android.graphics.drawable.ColorDrawable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import education.openschools.imagesearch.R
import education.openschools.imagesearch.domain.entities.Image
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchScreen {

    @Preview(
        showSystemUi = true
    )
    @Composable
    private fun SearchScreenPreview() {
        SearchScreen(
            imagesFlow = flow { PagingData.empty<Image>() },
            onImageClick = {},
            onSearch = {},
            initialQuery = ""
        )
    }

    companion object {

        @Composable
        fun SearchScreen(
            onImageClick: (Int) -> Unit,
            viewModel: SearchScreenViewModel
        ) {
            val initialQuery = viewModel.searchQuery.collectAsState()
            SearchScreen(
                imagesFlow = viewModel.images,
                onSearch = viewModel::makeSearch,
                onImageClick = onImageClick,
                initialQuery = initialQuery.value
            )
        }

        @Composable
        private fun SearchScreen(
            imagesFlow: Flow<PagingData<Image>>,
            initialQuery: String,
            onImageClick: (Int) -> Unit,
            onSearch: (String) -> Unit
        ) {
            Scaffold(
                modifier = Modifier.fillMaxSize()
            ) { paddingValues ->
                val images = imagesFlow.collectAsLazyPagingItems()
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SearchBar(
                        modifier = Modifier.padding(10.dp),
                        onStartSearch = onSearch,
                        initialQuery = initialQuery
                    )
                    if (images.loadState.refresh is LoadState.Error) {
                        val error =
                            (images.loadState.refresh as LoadState.Error).error.message ?: ""
                        Text(
                            text = error,
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.padding(20.dp)
                        )
                    } else {
                        LazyVerticalStaggeredGrid(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            columns = StaggeredGridCells.Adaptive(150.dp),
                            verticalItemSpacing = 10.dp
                        ) {
                            items(images.itemCount) { index ->
                                images[index]?.let { image ->
                                    ImageCard(
                                        image = image,
                                        onClick = {
                                            onImageClick(index)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        @Composable
        private fun SearchBar(
            modifier: Modifier = Modifier,
            initialQuery: String,
            onStartSearch: (String) -> Unit
        ) {
            var query by remember {
                mutableStateOf(initialQuery)
            }
            OutlinedTextField(
                modifier = modifier.fillMaxWidth(),
                value = query,
                onValueChange = {
                    query = it
                },
                placeholder = {
                    Text(
                        modifier = Modifier.padding(start = 20.dp),
                        text = stringResource(R.string.search),
                        style = MaterialTheme.typography.titleSmall
                    )
                },
                shape = RoundedCornerShape(50.dp),
                trailingIcon = {
                    Icon(
                        modifier = Modifier.clickable { onStartSearch(query) },
                        painter = painterResource(id = R.drawable.search),
                        contentDescription = ""
                    )
                },
                maxLines = 1,
                keyboardActions = KeyboardActions(onSearch = {
                    onStartSearch(query)
                }),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
            )
        }

        @Composable
        private fun ImageCard(
            modifier: Modifier = Modifier,
            image: Image,
            onClick: (Image) -> Unit
        ) {
            val ratio = image.thumbnailWidth.toFloat() / image.thumbnailHeight
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(image.thumbnailUrl)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .placeholder(ColorDrawable(Color.Gray.toArgb()))
                    .crossfade(250)
                    .build(),
                contentDescription = null,
                modifier = modifier
                    .fillMaxWidth()
                    .aspectRatio(if (ratio > 0) ratio else 1 / ratio)
                    .clip(RoundedCornerShape(size = 10.dp))
                    .clickable(onClick = { onClick(image) }),
                contentScale = ContentScale.Crop
            )
        }
    }
}
