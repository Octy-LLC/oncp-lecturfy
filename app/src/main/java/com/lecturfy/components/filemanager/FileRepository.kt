package com.lecturfy.components.filemanager

import android.content.Context
import android.net.Uri
import java.io.File
import javax.inject.Inject

class FileRepository @Inject constructor() {

    fun getFiles(currentPath: File): List<File> {
        return currentPath.listFiles()?.filter { it.extension in listOf("txt", "jpg", "jpeg", "png", "mp4", "3gp", "mp3") || it.isDirectory }?.toList() ?: emptyList()
    }

    fun createFile(currentPath: File, fileName: String, content: String) {
        val file = File(currentPath, fileName)
        file.writeText(content)
    }

    fun deleteFile(currentPath: File, fileName: String) {
        val file = File(currentPath, fileName)
        file.delete()
    }

    fun readFile(currentPath: File, fileName: String): String {
        val file = File(currentPath, fileName)
        return file.readText()
    }

    fun saveMediaFile(context: Context, currentPath: File, fileName: String, mediaUri: Uri) {
        val inputStream = context.contentResolver.openInputStream(mediaUri)
        val file = File(currentPath, fileName)
        inputStream?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
    }

    fun createFolder(currentPath: File, folderName: String) {
        val folder = File(currentPath, folderName)
        folder.mkdir()
    }

    fun renameFile(currentPath: File, oldName: String, newName: String) {
        val oldFile = File(currentPath, oldName)
        val newFile = File(currentPath, newName)
        oldFile.renameTo(newFile)
    }
}
