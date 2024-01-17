package com.pollyannawu.gogolook.compose

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pollyannawu.gogolook.MainViewModel
import com.pollyannawu.gogolook.compose.home.HomeScreen
import com.pollyannawu.gogolook.compose.loading.LoadingScreen

@Composable
fun ImageSearcherApp(
    viewModel: MainViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    ImageSearcherNavHost(
        navController = navController,
        viewModel = viewModel
    )

}

@Composable
fun ImageSearcherNavHost(
    navController: NavHostController,
    viewModel: MainViewModel
){
   val activity = (LocalContext.current) as Activity
    NavHost(navController = navController, startDestination = Screen.Home.route){
        composable(route = Screen.Home.route){
            HomeScreen()
        }
    }
}