package com.example.projectwayfinder.navigation

import android.util.Log
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.togitech.ccp.component.TogiCountryCodePicker
import java.lang.Exception

@Composable
fun Locations(navController: NavHostController = rememberNavController()) {
    var (isPressedHome, setIsPressedHome) = remember{ mutableStateOf(false) }
    var (isPressedWork, setIsPressedWork) = remember{ mutableStateOf(false) }
    var (isPressedNumber, setIsPressedNumber) = remember{ mutableStateOf(false) }
    var (isPressedSchool, setIsPressedSchool) = remember{ mutableStateOf(false) }
    Surface(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            LocationCard(
                text = "Home Location",
                onClick = {
                    if (isPressedHome) {
                        setIsPressedHome(false)
                    }
                    else {
                        setIsPressedHome(true)
                    }}
            )

            if (isPressedHome){
                RenderMap("home")
            }
            LocationCard(
                text = "Work Location",
                onClick = {
                    if (isPressedWork){
                        setIsPressedWork(false)
                    }
                    else{
                        setIsPressedWork(true)
                    }
                }
            )
            if (isPressedWork){
                RenderMap("work")
            }
            LocationCard(
                text = "School Location",
                onClick = {
                    if (isPressedSchool){
                        setIsPressedSchool(false)
                    }
                    else{
                        setIsPressedSchool(true)
                    }
                }
            )
            if (isPressedSchool){
                RenderMap("school")
            }
            LocationCard(
                text = "Emergency Number",
                onClick = {
                    if(isPressedNumber){
                        setIsPressedNumber(false)
                    }
                    else{
                        setIsPressedNumber(true)
                    }
                }
            )
            if (isPressedNumber){
                RenderNumber()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LocationCard(text: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = text)
        }
    }
}

@Composable
fun RenderNumber(){
    var phoneNumber: String by rememberSaveable { mutableStateOf("") }
    var fullPhoneNumber: String by rememberSaveable { mutableStateOf("") }
    var isNumberValid: Boolean by rememberSaveable { mutableStateOf(false) }

    TogiCountryCodePicker(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        onValueChange = { (code, phone), isValid ->
            phoneNumber = phone
            try {
                fullPhoneNumber = code + phone.substring(1)
            }
            catch (e: Exception){
                Log.e("A","B")
            }
            isNumberValid = isValid
        }
    )
    Button(onClick = {
        Firebase.database.getReference("emergency-number").setValue(fullPhoneNumber)
    },
        modifier = Modifier.padding(20.dp)) {
        Text(text = "Confirm New Number",
)
    }
}

@Composable
fun RenderMap(type: String){
    val selectedMarker = LatLng(53.417402, -7.904757)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(selectedMarker, 15f)
    }
    var selectedLocation: LatLng? by remember { mutableStateOf(null) }
    GoogleMap(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp),
        cameraPositionState = cameraPositionState,
        onMapClick = {it->
            selectedLocation = it

        }
    ) {
        if(selectedLocation != null){
            Marker(
                state = MarkerState(position = selectedLocation!!),
                title = "Selected Location",
                snippet = null
            )
        }

    }
    
    if(selectedLocation != null){
        Button(onClick = {
            Firebase.database.getReference(type).setValue("${selectedLocation!!.latitude},${selectedLocation!!.longitude}")
        }) {
            Text(text = "Set as Location")
        }
    }
    
    
}

@Preview(showBackground = true)
@Composable
fun LocationsPreview() {
    Locations()
}
