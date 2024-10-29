package com.lecturfy.components.filemanager

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.io.File

@Composable
fun FileItem(
    file: File,
    isSelected: Boolean,
    onClick: () -> Unit,
    backgroundColor: Color = Color.Gray,
    selectedBackgroundColor: Color = Color.LightGray,
    textColor: Color = Color.Black,
    iconSize: Dp = 48.dp,
    fontSize: TextUnit = 12.sp
) {
    val bgColor = if (isSelected) selectedBackgroundColor else backgroundColor

    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .background(bgColor)
            .padding(8.dp)
            .aspectRatio(1f)
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box {
            Icon(
                imageVector = when {
                    file.isDirectory -> Icons.Default.Folder
                    file.extension == "txt" -> Icons.Default.Description
                    file.extension == "jpg" || file.extension == "jpeg" || file.extension == "png" -> Icons.Default.Image
                    file.extension == "mp4" -> Icons.Default.Movie
                    file.extension == "3gp" || file.extension == "mp3" -> Icons.Default.MusicNote
                    else -> Icons.Default.InsertDriveFile
                },
                contentDescription = null,
                modifier = Modifier.size(iconSize),
                tint = textColor
            )
        }

        Text(
            text = shortenFileName(file.name),
            fontSize = fontSize,
            textAlign = TextAlign.Center,
            color = textColor
        )
    }
}


@Composable
fun FileItemSmall(
    file: File,
    isSelected: Boolean,
    onClick: () -> Unit,
    folderNameFontSize: TextUnit = 12.sp,
    selectedBackgroundColor: Color = Color.LightGray,
    defaultBackgroundColor: Color = Color.Gray
) {
    val backgroundColor = if (isSelected) selectedBackgroundColor else defaultBackgroundColor

    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .background(backgroundColor)
            .padding(8.dp)
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box {
            Icon(
                imageVector = when {
                    file.isDirectory -> Icons.Default.Folder
                    file.extension == "txt" -> Icons.Default.Description
                    file.extension == "jpg" || file.extension == "jpeg" || file.extension == "png" -> Icons.Default.Image
                    file.extension == "mp4" -> Icons.Default.Movie
                    file.extension == "3gp" || file.extension == "mp3" -> Icons.Default.MusicNote
                    else -> Icons.Default.InsertDriveFile
                },
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )
        }

        Text(
            text = shortenFileName(file.name),
            fontSize = folderNameFontSize,
            textAlign = TextAlign.Center,
        )
    }
}
