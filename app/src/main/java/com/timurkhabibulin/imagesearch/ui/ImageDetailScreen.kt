package com.timurkhabibulin.imagesearch.ui

import android.graphics.drawable.ColorDrawable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.timurkhabibulin.imagesearch.R
import com.timurkhabibulin.imagesearch.domain.entities.Image
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ImageDetailScreen {

    @Preview(
        showSystemUi = true
    )
    @Composable
    private fun ImageDetailScreenPreview() {
        ImageDetailScreen(
            imagesFlow = flow { PagingData.empty<Image>() },
            position = 0,
            onBack = {},
            openInBrowser = {}
        )
    }

    companion object {

        @Composable
        fun ImageDetailScreen(
            position: Int,
            onBack: () -> Unit,
            viewModel: SearchScreenViewModel
        ) {
            val context = LocalContext.current
            ImageDetailScreen(
                imagesFlow = viewModel.images,
                position = position,
                onBack = onBack,
                openInBrowser = {
                    viewModel.openDeepLink(context, it)
                }
            )
        }

        @OptIn(ExperimentalFoundationApi::class)
        @Composable
        private fun ImageDetailScreen(
            imagesFlow: Flow<PagingData<Image>>,
            position: Int,
            onBack: () -> Unit,
            openInBrowser: (String) -> Unit
        ) {
            val images = imagesFlow.collectAsLazyPagingItems()
            val pagerState = rememberPagerState(
                initialPage = position,
                pageCount = { images.itemCount }
            )
            Scaffold(
                topBar = {
                    TopBar(
                        onBack = onBack,
                        onOpenInBrowser = {
                            openInBrowser(images[pagerState.currentPage]?.link ?: "")
                        }
                    )
                }
            ) { paddingValues ->
                if (images.loadState.refresh is LoadState.Error) {
                    val error =
                        (images.loadState.refresh as LoadState.Error).error.message ?: ""
                    Text(
                        text = error,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(20.dp)
                    )
                } else {
                    HorizontalPager(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        state = pagerState
                    ) { page ->
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            images[page]?.let { image ->
                                Image(image)
                            }
                        }
                    }
                }
            }
        }

        @Composable
        private fun TopBar(
            modifier: Modifier = Modifier,
            onBack: () -> Unit,
            onOpenInBrowser: () -> Unit
        ) {
            Row(
                modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 40.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    modifier = Modifier.clickable { onBack() },
                    painter = painterResource(R.drawable.back),
                    contentDescription = null
                )
                Icon(
                    modifier = Modifier.clickable { onOpenInBrowser() },
                    painter = painterResource(R.drawable.open_in),
                    contentDescription = null
                )
            }
        }

        @Composable
        private fun Image(image: Image) {
            val ratio = image.imageWidth.toFloat() / image.imageHeight

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(image.imageUrl)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .placeholder(ColorDrawable(Color.Gray.toArgb()))
                    .crossfade(250)
                    .build(),
                contentDescription = null,
                modifier = Modifier.aspectRatio(if (ratio > 0) ratio else 1 / ratio)
            )
        }
    }
}
