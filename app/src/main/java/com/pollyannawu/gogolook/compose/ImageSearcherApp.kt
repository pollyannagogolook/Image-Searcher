package com.pollyannawu.gogolook.compose

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun ImageSearcherApp() {
    val navController = rememberNavController()

}

@Composable
fun ImageSearcherNavHost(
    navController: NavHostController
){
   val activity = (LocalContext.current) as Activity
    NavHost(navController = navController, startDestination = Screen.Home.route){
        composable(route = Screen.Home.route){

        }
    }
}