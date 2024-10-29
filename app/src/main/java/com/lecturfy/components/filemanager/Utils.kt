package com.lecturfy.components.filemanager

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore

fun shortenFileName(fileName: String, maxLength: Int = 15): String {
    return if (fileName.length > maxLength) {
        val extension = fileName.substringAfterLast('.', "")

        val prefixLength = 3
        val suffixLength = 3 + extension.length
        "${fileName.take(prefixLength)}…${fileName.takeLast(suffixLength)}"
    } else {
        fileName
    }
}

fun loadImageBitmap(context: Context, uri: Uri): Bitmap? {
    return try {
        if (Build.VERSION.SDK_INT < 28) {
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        } else {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}