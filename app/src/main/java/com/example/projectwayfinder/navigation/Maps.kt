package com.example.projectwayfinder.navigation

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun Maps(navController: NavHostController) {

    val currLocation = remember {
        mutableStateOf(LatLng(1.35, 103.87))
    }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(currLocation.value, 17f)
    }

    LaunchedEffect(currLocation.value) {
        val valueRef = Firebase.database.getReference("current-location")
        valueRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val rawValue = snapshot.getValue(String::class.java)
                if (rawValue != null) {
                    val lat = rawValue.split(",").get(0).toDouble()
                    val lang = rawValue.split(",").get(1).toDouble()
                    currLocation.value = LatLng(lat, lang)
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(currLocation.value, 17f)
                    Log.d("FIREBASE MAPS", rawValue)
                } else {
                    Log.d("FIREBASE MAPS", "DATA COULDN'T GET RETRIEVED!!")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("database error")
            }
        })
    }


    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        Marker(
            state = MarkerState(position = currLocation.value),
            title = "Current",
            snippet = "Marker in Current Location"
        )
    }
}
