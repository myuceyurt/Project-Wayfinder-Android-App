package com.example.projectwayfinder.main

import java.io.File

interface AudioPlayerInterface {
    fun startAudio(audioFile: File)
    fun stopAudio()
}