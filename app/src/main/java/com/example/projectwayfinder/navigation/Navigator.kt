package com.example.projectwayfinder.navigation
import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.projectwayfinder.main.MyBottomNavigation

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigator(){
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { MyBottomNavigation(navController = navController)}
    ) {
        NavHost(navController = navController, startDestination = "maps"){
            composable("maps"){
                Maps(navController)
            }
            composable("locations"){
                Locations(navController)
            }
            composable("communication"){
                Communication(navController)
            }
        }
    }


}