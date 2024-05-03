package com.example.projectwayfinder.main

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.projectwayfinder.R

@Composable
fun MyBottomNavigation(
    navController: NavController
){
    val items = listOf(NavItem.Maps,
        NavItem.Locations,
        NavItem.Communication)

    BottomNavigation(
        backgroundColor = colorResource(id = R.color.teal_700)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach{item->
            BottomNavigationItem(selected = currentRoute == item.navRoute,
                label = { Text(text = stringResource(id = item.title))},
                selectedContentColor = Color.White,
                unselectedContentColor = Color.White.copy(0.4f),
                onClick = {
                          navController.navigate(item.navRoute)
                },
                icon = { Icon(painter = painterResource(id = item.icon), contentDescription = "") })
        }
    }
}