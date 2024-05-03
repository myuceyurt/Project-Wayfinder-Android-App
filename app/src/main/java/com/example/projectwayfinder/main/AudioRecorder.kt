package com.example.projectwayfinder.main

import android.content.Context
import android.media.MediaRecorder
import java.io.File
import java.io.FileOutputStream

class AudioRecorder(
    val context: Context
) : AudioRecorderInterface {
    private var recorder: MediaRecorder? = null

    private fun createRecorder(): MediaRecorder{
        return MediaRecorder(context)
    }

    override fun start(audioFile: File) {
        createRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4) //MP3
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)//CHANGE IF QUALITY IS BAD
            setOutputFile(FileOutputStream(audioFile).fd)

            prepare()
            start()
            recorder = this
        }
    }

    override fun stop() {
        recorder?.stop()
        recorder?.reset()
    }
}