package com.pollyannawu.gogolook.compose.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.pollyannawu.gogolook.MainViewModel
import com.pollyannawu.gogolook.R
import com.pollyannawu.gogolook.compose.loading.LoadingView
import com.pollyannawu.gogolook.data.dataclass.Hit
import kotlinx.coroutines.flow.Flow


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel
) {
    // use in search bar to show loading page
    val isOnSearch by remember {
        viewModel.isSearch
    }
    val historySuggestion by remember {
        viewModel.searchSuggestions
    }

    // use in layout toggle button
    val isLinear by remember {
        viewModel.isLinear
    }


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
                    .padding(vertical = 8.dp, horizontal = 16.dp)
                    .semantics { traversalIndex = 1f }
                ,
                turnOnSearch = {viewModel.turnOnSearch()},
                historySuggestion = { historySuggestion },
                getImageBySearch = { text -> viewModel.getImagesBySearch(text) },
                showSearchSuggestion = { query -> viewModel.getSearchHistorySuggestion(query) },
                saveSearchHistory = { query -> viewModel.saveSearchQuery(query) }
            )
            LayoutToggleButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                toggleLayout = { viewModel.changeLayout() }
            )
            if (!isOnSearch) {
                HomePagerView(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding),
                    pagingFlow = { viewModel.pagingFlow },
                    isLinear = { isLinear }
                )
            } else {
                LoadingView(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding),
                    turnOffSearch = { viewModel.turnOffSearch() },
                )
            }
        }

    }
}


@Composable
fun HomePagerView(
    modifier: Modifier = Modifier,
    pagingFlow: () -> Flow<PagingData<Hit>>,
    isLinear: () -> Boolean

) {
    val images = pagingFlow().collectAsLazyPagingItems()


    // return a State object, not value. it will provide a getter to snapshot the current value.
    val widthByLayout = if (isLinear()) 1f else 0.5f



    if (isLinear()) {
        LazyColumn(modifier = modifier) {
            items(count = images.itemCount) { index ->
                images[index]?.let { hit ->
                    SingleImageView(
                        hit = hit,
                        isLinear = true,
                        modifier = Modifier
                            .fillMaxWidth(widthByLayout)
                            .padding(all = 16.dp)
                    )
                }
            }
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = modifier
        ) {
            items(count = images.itemCount) { index ->
                images[index]?.let { hit ->
                    // show each image card
                    SingleImageView(
                        hit = hit,
                        isLinear = false,
                        modifier = Modifier
                            .fillMaxWidth(widthByLayout)
                            .wrapContentHeight()
                            .padding(all = 8.dp)
                    )
                }
            }
        }
    }
}



@Composable
fun LayoutToggleButton(
    modifier: Modifier = Modifier,
    toggleLayout: () -> Boolean
) {
    var isLinear by remember {
        mutableStateOf(true)
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {

        Image(
            painter = painterResource(id = R.drawable.horizontal_layout_icon),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize(0.1f),
            contentDescription = "linear layout icon"
        )
        Switch(
            checked = isLinear,
            onCheckedChange = {
                isLinear = toggleLayout()
            }
        )


    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBar(
    modifier: Modifier = Modifier,
    turnOnSearch: () -> Unit,
    historySuggestion: () -> List<String>,
    getImageBySearch: (String) -> Unit,
    showSearchSuggestion: (String) -> Unit,
    saveSearchHistory: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    DockedSearchBar(
        modifier = modifier,
        query = text,
        onQueryChange = {
            text = it
            showSearchSuggestion(it)
        },
        onSearch = {
            active = false
            turnOnSearch()
            getImageBySearch(text)
            saveSearchHistory(text)
        },
        active = active,
        onActiveChange = {
            active = it
            if (it) {
                showSearchSuggestion(text)
            }
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
            items(historySuggestion().size) { index ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            active = false
                            text = historySuggestion()[index]
                            turnOnSearch()
                            getImageBySearch(text)

                        },
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = historySuggestion()[index])
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = "history icon"
                    )
                }
            }
        }
    }

}


