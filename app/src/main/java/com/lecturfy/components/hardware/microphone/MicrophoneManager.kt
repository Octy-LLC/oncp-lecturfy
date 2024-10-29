package com.lecturfy.components.hardware.microphone

import android.content.pm.PackageManager
import android.media.MediaRecorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.lecturfy.MainActivity
import com.lecturfy.components.hardware.utils.SaveFileDialog
import java.io.File
import java.util.UUID

class MicrophoneManager {

    private var recorder: MediaRecorder? = null
    private var isRecording = false

    fun startRecording(saveDirectory: File): File? {
        if (recorder == null) {
            recorder = MediaRecorder()
        } else {
            recorder?.reset()
        }

        val audioFile = if (saveDirectory.path.endsWith(".3gp")) {
            saveDirectory
        } else {
            File(saveDirectory, "${UUID.randomUUID()}.3gp")
        }

        return try {
            recorder?.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                setOutputFile(audioFile.absolutePath)
                prepare()
                start()
            }
            isRecording = true
            audioFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun stopRecording(): File? {
        return try {
            recorder?.apply {
                stop()
                reset()
                release()
            }
            isRecording = false
            null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            recorder = null
        }
    }

    fun isRecording(): Boolean {
        return isRecording
    }
}

@Composable
fun AudioButton(
    context: MainActivity,
    buttonBackgroundColor: Color = Color.Blue,
    buttonTextColor: Color = Color.White,
    dialogBackgroundColor: Color = Color.White,
    dialogContentColor: Color = Color.Black
) {
    var showDialog by remember { mutableStateOf(false) }
    var directory by remember { mutableStateOf(context.filesDir.absolutePath) }
    var fileName by remember { mutableStateOf("") }
    val fileExtension = "3gp"
    val microphoneManager = remember { MicrophoneManager() }
    var isRecording by remember { mutableStateOf(false) }

    val startRecording = {
        val perm =
            ContextCompat.checkSelfPermission(context, android.Manifest.permission.RECORD_AUDIO)
        if (perm == PackageManager.PERMISSION_GRANTED) {
            if (fileName.isNotBlank()) {
                val saveDir = File(directory)
                if (!saveDir.exists()) {
                    saveDir.mkdirs()
                }
                val audioFile = microphoneManager.startRecording(File(saveDir, "$fileName.$fileExtension"))
                if (audioFile != null) {
                    isRecording = true
                    showDialog = false
                }
            }
        } else {
            ActivityCompat.requestPermissions(
                context,
                arrayOf(android.Manifest.permission.RECORD_AUDIO),
                200
            )
        }
    }

    if (showDialog) {
        SaveFileDialog(
            title = "Save Audio",
            directory = directory,
            fileName = fileName,
            fileExtension = fileExtension,
            onDismiss = { showDialog = false },
            onConfirm = { dir, name ->
                directory = dir
                fileName = name
                startRecording()
            },
            context = context,
            onPathChange = { directory = it },
            onFileNameChange = { fileName = it },
            dialogBackgroundColor = dialogBackgroundColor,
            dialogContentColor = dialogContentColor,
            buttonTextColor = buttonTextColor,
            buttonBackgroundColor = buttonBackgroundColor
        )
    }

    if (isRecording) {
        Button(
            onClick = {
                val resultFile = microphoneManager.stopRecording()
                isRecording = false
            },
            colors = ButtonDefaults.buttonColors(containerColor = buttonBackgroundColor)
        ) {
            Text(text = "Stop Recording", color = buttonTextColor)
        }
    } else {
        Button(
            onClick = { showDialog = true },
            colors = ButtonDefaults.buttonColors(containerColor = buttonBackgroundColor)
        ) {
            Text(text = "Record Audio", color = buttonTextColor)
        }
    }
}


