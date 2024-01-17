package com.pollyannawu.gogolook.compose.home

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.pollyannawu.gogolook.MainViewModel
import com.pollyannawu.gogolook.compose.paging.SingleImageScreen
import com.pollyannawu.gogolook.data.dataclass.Hit
import com.pollyannawu.gogolook.data.model.image_search.ITAG
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel()
) {

    val isOnSearch by viewModel.isSearch.collectAsState(initial = false)
    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        topBar = {
            HomeTopAppBar()
        }
    ) { contentPadding ->

        if (!isOnSearch) {
            HomePagerScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomePagerScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel()
) {
    val images = viewModel.images.collectAsLazyPagingItems()
    val isLinear by viewModel.isLinear.collectAsState(initial = true)
    val isOnSearch by viewModel.isSearch.collectAsState(initial = false)

    if (isOnSearch) {
        // show loading page
        Log.i(ITAG, "show loading page")
        images.refresh()
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(count = images.itemCount) {
                images[it]?.let { hit ->
                    // show each image card
                    SingleImageScreen(
                        hit = hit,
                        isLinear = isLinear
                    )
                }
            }
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopAppBar(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel(),
) {
    var text by rememberSaveable { mutableStateOf("") }
    var active by rememberSaveable { mutableStateOf(false) }


    val suggestions = viewModel.searchSuggestions.collectAsState(initial = emptyList()).value

    Box(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .semantics { isTraversalGroup = true })
    {
        DockedSearchBar(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .align(Alignment.TopCenter)
                .semantics { traversalIndex = 1f },
            query = text,
            onQueryChange = {
                text = it
            },
            onSearch = {
                active = false
                viewModel.turnOnSearch()
                viewModel.getImagesBySearch(text)
            },
            active = active,
            onActiveChange = {
                active = it
                viewModel.updateSearchHistorySuggestion(text)
            },
            placeholder = { Text("Let's search images !") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                Icon(
                    modifier = Modifier.clickable {
                        if (text.isNotEmpty()) {
                            text = ""
                        } else {
                            active = false
                        }
                    },
                    imageVector = Icons.Default.Close,
                    contentDescription = "close icon"
                )
            }
        ) {
            // search history
            LazyColumn(
                modifier = Modifier
                    .padding(all = 16.dp)
                    .wrapContentHeight()
            ) {
                items(suggestions.size) { index ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable {
                                text = suggestions[index]
                                viewModel.getImagesBySearch(text)
                                active = false
                            },
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = suggestions[index])
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = "history icon"
                        )
                    }
                }
            }


        }

    }
}



