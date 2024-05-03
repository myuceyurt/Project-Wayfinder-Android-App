package com.example.projectwayfinder.main

import android.content.Context
import android.media.MediaPlayer
import androidx.core.net.toUri
import java.io.File

class AudioPlayer(
    private val context: Context
) : AudioPlayerInterface{

    private var player: MediaPlayer? = null

    override fun startAudio(audioFile: File) {
        MediaPlayer.create(context, audioFile.toUri()).apply{
            player = this
            start()
        }
    }

    override fun stopAudio() {
        player?.stop()
        player?.release()
        player = null
    }

}