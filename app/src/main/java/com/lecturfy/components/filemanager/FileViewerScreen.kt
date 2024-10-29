package com.lecturfy.components.filemanager

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.core.content.FileProvider
import com.lecturfy.MainActivity
import com.lecturfy.components.Audio
import com.lecturfy.components.AudioPlayer
import com.lecturfy.components.VideoPlayer
import java.io.File
import java.util.*

@Composable
fun FileViewerScreen(
    context: MainActivity,
    сurrentDir: String,
    fileName: String,
    fileContent: String?,
    onBack: () -> Unit,
    onSave: (String) -> Unit,
    onDelete: () -> Unit
) {
    var editedContent by remember { mutableStateOf(fileContent ?: "") }
    val file = File(сurrentDir, fileName)
    val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    val metadata = "Size: ${formatFileSize(file.length())}\nLast Modified: ${Date(file.lastModified())}"

    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = fileName, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = metadata, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.height(500.dp)){
            when {
                fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png") -> {
                    uri?.let {
                        val bitmap = loadImageBitmap(context, it)
                        bitmap?.let { bmp ->
                            Image(
                                bitmap = bmp.asImageBitmap(),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
                fileName.endsWith(".mp4") -> {
                    VideoPlayer(context,videoUrls = listOf(uri.toString()))
                }
                fileName.endsWith(".3gp") || fileName.endsWith(".mp3") -> {
                    AudioPlayer(playList = listOf(Audio(title= "", url = uri.toString())))
                }
                else -> {
                    TextField(
                        value = editedContent,
                        onValueChange = { editedContent = it },
                        modifier = Modifier.fillMaxSize(),
                        label = { Text("File Content") },
                        textStyle = LocalTextStyle.current.copy(fontSize = 16.sp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (fileName.endsWith(".txt")) {
                Button(onClick = { onSave(editedContent) }) {
                    Text("Save Changes")
                }
            }
            Button(onClick = onDelete) {
                Text("Delete")
            }
            Button(onClick = onBack) {
                Text("Back")
            }
        }
    }
}

fun formatFileSize(size: Long): String {
    if (size <= 0) return "0 B"
    val units = arrayOf("B", "KB", "MB", "GB", "TB")
    val digitGroups = (Math.log10(size.toDouble()) / Math.log10(1024.0)).toInt()
    return String.format("%.1f %s", size / Math.pow(1024.0, digitGroups.toDouble()), units[digitGroups])
}