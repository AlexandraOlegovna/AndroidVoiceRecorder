package ru.spbau.mit.voicerecord


import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Environment
import java.io.File
import android.support.v4.content.ContextCompat
import com.chibde.visualizer.CircleBarVisualizer


class MainActivity : AppCompatActivity(), MediaPlayer.OnCompletionListener {
    lateinit var recorder: MediaRecorder
    lateinit var player: MediaPlayer
    lateinit var lastRecord: File
    lateinit var recordB: Button
    lateinit var playB: Button
    lateinit var circleVisualizer : CircleBarVisualizer
    var isRecording = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recordB = findViewById(R.id.record)
        playB = findViewById(R.id.play)

        circleVisualizer = findViewById(R.id.visualizer)
        circleVisualizer.setColor(ContextCompat.getColor(this, R.color.button_material_dark))

        val path = File(Environment.getExternalStorageDirectory().path)
        lastRecord = File.createTempFile("tmp", ".3gp", path)

        recordB.setOnClickListener {
            if (!isRecording) {
                recordB.text = "STOP"
                isRecording = true
                playB.isEnabled = false

                recorder = MediaRecorder()
                initRecorder()

                recorder.setOutputFile(lastRecord.absolutePath)
                recorder.prepare()
                recorder.start()
            }
            else {
                recordB.text = "RECORD"
                isRecording = false
                playB.isEnabled = true

                recorder.stop()
                recorder.release()
                player = MediaPlayer()
                player.setOnCompletionListener(this)
                player.setDataSource(lastRecord.absolutePath)
                player.prepare()
                circleVisualizer.setPlayer(player.audioSessionId)
            }

        }

        playB.setOnClickListener {
            player.start()
            recordB.isEnabled = false
            playB.isEnabled = false
        }
    }

    override fun onCompletion(mp: MediaPlayer) {
        recordB.isEnabled = true
        playB.isEnabled = true
    }

    private fun initRecorder(){
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC)
        recorder.setAudioEncodingBitRate(32)
        recorder.setAudioSamplingRate(44100)
    }
}