package com.example.projectwayfinder.main

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.projectwayfinder.R

sealed class NavItem (
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    val navRoute: String
) {
    object Maps: NavItem(R.string.maps, R.drawable.ic_map, "maps")
    object Communication: NavItem(R.string.communication, R.drawable.ic_communication, "communication")
    object Locations: NavItem(R.string.locations, R.drawable.ic_locations, "locations")
}