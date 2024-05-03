package com.example.projectwayfinder.navigation

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
// Import for color palette
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.projectwayfinder.MainActivity
import com.example.projectwayfinder.R
import com.example.projectwayfinder.main.AudioPlayer
import com.example.projectwayfinder.main.AudioRecorder
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.storageMetadata
import java.io.File

@Composable
fun Communication(navController: NavHostController) {
    val context = LocalContext.current
    var myRecorder = AudioRecorder(context)
    var myPlayer = AudioPlayer(context)
    var myCache = context.cacheDir

    val db = Firebase.database
    var storageRef = Firebase.storage.reference
    var audioFile: File? = null

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background // Use MaterialTheme colors
    ) {
        Box( // Use a Box to position elements freely
            modifier = Modifier.padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Card( // Wrap content in a Card for elevation and rounded corners
                modifier = Modifier.align(Alignment.Center),
                shape = RoundedCornerShape(16.dp),

            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(painter = painterResource(id = R.drawable.ic_communication),
                        contentDescription = "",
                        modifier = Modifier.size(100.dp)
                            .padding(20.dp))
                    Button(onClick = {
                        File(myCache, "audio.mp3").also {
                            myRecorder.start(it)
                            audioFile = it
                        }
                    }) {
                        Text("START RECORDING")
                    }
                    Button(onClick = {
                        myRecorder.stop()
                    }) {
                        Text("STOP RECORDING")
                    }
                    Button(onClick = {
                        myPlayer.startAudio(audioFile ?: return@Button) // nullsa cik degilse calistir
                    }) {
                        Text("PLAY")
                    }
                    Button(onClick = { myPlayer.stopAudio() }) {
                        Text("STOP")
                    }
                    Button(onClick = {
                        var metadata = storageMetadata {
                            contentType = "audio/mpeg"
                        }
                        audioFile?.let { file ->
                            val cloudRef = storageRef.child("audio/${file.name}")
                            cloudRef.delete().addOnSuccessListener {
                                Log.e(TAG, "Old File Deletion Successful")
                                cloudRef.putFile(file.toUri(), metadata).addOnSuccessListener(){
                                    it.storage.downloadUrl.addOnSuccessListener(){downloadUrl ->
                                        val myURL = downloadUrl
                                        db.getReference("audio").setValue(downloadUrl.toString())

                                    }.addOnFailureListener(){
                                        Log.e(TAG, "Download URL couldn't be received")
                                    }
                                }.addOnSuccessListener(){
                                    Log.e(TAG, "New File Upload Failed!")
                                }
                            }.addOnFailureListener(){
                                Log.e(TAG, "Old File Deletion Failed!")
                            }
                        }
                    }) {
                        Text(text = "Send")
                    }


                }



            }
            }
        }
    }


@Composable
@Preview(showBackground = true)
fun CommPreview() {
    Communication(navController = rememberNavController())
}
