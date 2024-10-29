package com.lecturfy.components.hardware.media

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.core.content.FileProvider
import androidx.media3.common.util.UnstableApi
import com.lecturfy.MainActivity
import com.lecturfy.components.hardware.utils.SaveFileDialog
import java.io.File

@OptIn(UnstableApi::class)
@Composable
fun VideoButton(context: MainActivity, directory: String, onFinish: () -> Unit) {
    var videoUri by remember { mutableStateOf<Uri?>(null) }

    val recordVideoLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.CaptureVideo()) {
            onFinish()
        }

    val createVideoFile: (String) -> File = { directory ->
        val videoFile =
            File.createTempFile("${System.currentTimeMillis()}", ".mp4", File(directory))
        videoUri = FileProvider.getUriForFile(context, "${context.packageName}.provider", videoFile)
        videoFile
    }

    Button(onClick = {
        createVideoFile(directory)
        recordVideoLauncher.launch(videoUri)
    }) {
        Text(text = "Record Video")
    }
}

@Composable
fun PhotoButton(context: MainActivity, directory: String, onFinish: () -> Unit) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val takePictureLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success -> }

    val createImageFile: (String) -> File = { directory ->
        File.createTempFile("${System.currentTimeMillis()}", ".jpg", File(directory)).apply {
            imageUri = FileProvider.getUriForFile(context, "${context.packageName}.provider", this)
        }
    }

    Button(onClick = {
        createImageFile(directory)
        takePictureLauncher.launch(imageUri)
        onFinish()
    }) {
        Text(text = "Take Photo")
    }
}

@OptIn(UnstableApi::class)
@Composable
fun PhotoButton(
    context: MainActivity,
    buttonBackgroundColor: Color = Color.Green,
    buttonTextColor: Color = Color.White,
    dialogBackgroundColor: Color = Color.White,
    dialogContentColor: Color = Color.Black
) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var directory by remember { mutableStateOf(context.cacheDir.absolutePath) }
    var fileName by remember { mutableStateOf("") }
    val fileExtension = "jpg"

    val takePictureLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success -> }

    val createImageFile: (String, String) -> File = { dir, name ->
        val imageFile = File(dir, "$name.$fileExtension")
        imageUri = FileProvider.getUriForFile(context, "${context.packageName}.provider", imageFile)
        imageFile
    }

    if (showDialog) {
        SaveFileDialog(
            title = "Save Photo",
            directory = directory,
            fileName = fileName,
            fileExtension = fileExtension,
            onDismiss = { showDialog = false },
            onConfirm = { dir, name ->
                directory = dir
                fileName = name
                if (fileName.isNotBlank()) {
                    createImageFile(directory, fileName)
                    takePictureLauncher.launch(imageUri)
                    showDialog = false
                }
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

    Button(
        onClick = { showDialog = true },
        colors = ButtonDefaults.buttonColors(containerColor = buttonBackgroundColor)
    ) {
        Text(text = "Take Photo", color = buttonTextColor)
    }
}


@OptIn(UnstableApi::class)
@Composable
fun VideoButton(
    context: MainActivity,
    buttonBackgroundColor: Color = Color.Red,
    buttonTextColor: Color = Color.White,
    dialogBackgroundColor: Color = Color.White,
    dialogContentColor: Color = Color.Black
) {
    var videoUri by remember { mutableStateOf<Uri?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var directory by remember { mutableStateOf(context.cacheDir.absolutePath) }
    var fileName by remember { mutableStateOf("") }
    val fileExtension = "mp4"

    val recordVideoLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.CaptureVideo()) {}

    val createVideoFile: (String, String) -> File = { dir, name ->
        val videoFile = File(dir, "$name.$fileExtension")
        videoUri = FileProvider.getUriForFile(context, "${context.packageName}.provider", videoFile)
        videoFile
    }

    if (showDialog) {
        SaveFileDialog(
            title = "Save Video",
            directory = directory,
            fileName = fileName,
            fileExtension = fileExtension,
            onDismiss = { showDialog = false },
            onConfirm = { dir, name ->
                directory = dir
                fileName = name
                if (fileName.isNotBlank()) {
                    createVideoFile(directory, fileName)
                    recordVideoLauncher.launch(videoUri)
                    showDialog = false
                }
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

    Button(
        onClick = { showDialog = true },
        colors = ButtonDefaults.buttonColors(containerColor = buttonBackgroundColor)
    ) {
        Text(text = "Record Video", color = buttonTextColor)
    }
}
