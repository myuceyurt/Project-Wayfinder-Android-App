package com.example.projectwayfinder.main

import java.io.File

interface AudioRecorderInterface{
    fun start(audioFile: File)
    fun stop()
}