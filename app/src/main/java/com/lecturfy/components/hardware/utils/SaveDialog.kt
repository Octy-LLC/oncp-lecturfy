package com.lecturfy.components.hardware.utils

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.lecturfy.MainActivity
import com.lecturfy.components.filemanager.FileManagerSmallScreen
import java.io.File

@Composable
fun SaveFileDialog(
    title: String,
    directory: String,
    fileName: String,
    fileExtension: String,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit,
    context: MainActivity,
    onPathChange: (String) -> Unit,
    onFileNameChange: (String) -> Unit,
    dialogBackgroundColor: Color = Color.White,
    dialogContentColor: Color = Color.Black,
    buttonTextColor: Color = Color.White,
    buttonBackgroundColor: Color = Color.Blue
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title, color = dialogContentColor) },
        text = {
            Column {
                FileManagerSmallScreen(
                    context = context,
                    onPathChange = onPathChange,
                    backgroundColor = dialogBackgroundColor,
                    buttonTextColor = buttonTextColor
                )
                TextField(
                    value = fileName,
                    onValueChange = onFileNameChange,
                    label = { Text("File Name", color = dialogContentColor) },
                    textStyle = TextStyle(color = dialogContentColor)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val file = File(directory, "$fileName.$fileExtension")
                    if (file.exists()) {
                        Toast.makeText(context, "A file with this name already exists. Please choose a different name.", Toast.LENGTH_SHORT).show()
                    } else {
                        onConfirm(directory, fileName)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = buttonBackgroundColor)
            ) {
                Text("Confirm", color = buttonTextColor)
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = buttonBackgroundColor)
            ) {
                Text("Cancel", color = buttonTextColor)
            }
        },
        modifier = Modifier.background(dialogBackgroundColor)
    )
}


