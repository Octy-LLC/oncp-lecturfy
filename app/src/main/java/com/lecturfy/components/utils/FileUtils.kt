package com.lecturfy.components.utils

import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object FileUtils {
    fun getPathFromUri(context: Context, uri: Uri): String? {
        if (DocumentsContract.isDocumentUri(context, uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":")
            val type = split[0]

            if (type.equals("primary", true)) {
                return copyFileToInternalStorage(context, uri,split[1])
            }
        } else if (uri.scheme == "content") {
            val projection = arrayOf(MediaStore.Files.FileColumns.DATA)
            context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)
                    return cursor.getString(columnIndex)
                }
            }
        } else if (uri.scheme == "file") {
            return uri.path
        }
        return null
    }


    private fun copyFileToInternalStorage(context: Context, uri: Uri, newFileName: String): String? {
        val destinationFile = File(context.filesDir, newFileName.split("/").last())
        try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(destinationFile)
            val buffer = ByteArray(1024)
            var length: Int
            while (true) {
                length = inputStream?.read(buffer) ?: break
                if (length <= 0) break
                outputStream.write(buffer, 0, length)
            }
            outputStream.flush()
            outputStream.close()
            inputStream?.close()
            return destinationFile.path
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}
data class FileResult(
    val path: String = "",
    val extension: String = "",
    val success: Boolean,
    val filename: String = ""
)