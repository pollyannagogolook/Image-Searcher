package com.pollyannawu.gogolook.compose.home

import android.util.Log
import android.widget.ToggleButton
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.pollyannawu.gogolook.MainViewModel
import com.pollyannawu.gogolook.R
import com.pollyannawu.gogolook.compose.loading.LoadingScreen
import com.pollyannawu.gogolook.data.model.image_search.ITAG


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
        }
    ) { contentPadding ->
        Column(modifier = Modifier.padding(top = 16.dp)) {
            SearchBar(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .fillMaxWidth()
                    .wrapContentHeight()
            )
            LayoutToggleButton()
            if (!isOnSearch) {
                HomePagerScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding)
                )
            } else {
                LoadingScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding)
                )
            }
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

    // return a State object, not value. it will provide a getter to snapshot the current value.
    val isLinear by viewModel.isLinear.collectAsState(initial = true)
    val isOnSearch by viewModel.isSearch.collectAsState(initial = false)
    val widthByLayout = if (isLinear) 1f else 0.5f

    if (isOnSearch) {
        // show loading page
        images.refresh()
    } else {

        if (isLinear) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(count = images.itemCount) {
                    images[it]?.let { hit ->
                        // show each image card
                        SingleImageScreen(
                            hit = hit,
                            isLinear = isLinear,
                            modifier = Modifier
                                .fillMaxWidth(widthByLayout)
                                .padding(all = 16.dp)
                        )
                    }
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2)) {
                items(count = images.itemCount) {
                    images[it]?.let { hit ->
                        // show each image card
                        SingleImageScreen(
                            hit = hit,
                            isLinear = isLinear,
                            modifier = Modifier
                                .fillMaxWidth(widthByLayout)
                                .padding(all = 16.dp)
                        )
                    }
                }
            }
        }
    }

}

@Composable
fun LayoutToggleButton(viewModel: MainViewModel = hiltViewModel()) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(vertical = 8.dp)
    ) {

        Image(
            painter = painterResource(id = R.drawable.horizontal_layout_icon),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize(0.1f),
            contentDescription = "linear layout icon"
        )
        Switch(
            checked = viewModel.isLinear.collectAsState().value,
            onCheckedChange = {
                viewModel.toggleLayout()
            }
        )


    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBar(
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
                                viewModel.turnOnSearch()
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


