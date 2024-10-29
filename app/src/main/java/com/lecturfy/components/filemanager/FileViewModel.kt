package com.lecturfy.components.filemanager

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

data class FileViewModelState(
    val files: List<File> = emptyList(),
    val selectedFileContent: String? = null,
    val selectedFileName: String? = null,
    val currentPath: File = File(""),
    val selectedSortOption: String = "Name",
    val selectedFilterOption: String = "All",
    val errorMessage: String? = null
)
@HiltViewModel
class FileViewModel @Inject constructor(
    private val fileRepository: FileRepository,
    private val savedStateHandle: SavedStateHandle,
    application: Application
) : ViewModel() {

    private val _state = MutableStateFlow(FileViewModelState(currentPath = application.cacheDir!!))
    val state = _state.asStateFlow()

    init {
        loadFiles()
    }

    fun clearErrorMessage() {
        _state.update { it.copy(errorMessage = null) }
    }

    fun loadFiles(applySortAndFilter: Boolean = true) {
        viewModelScope.launch {
            try {
                val filesList = fileRepository.getFiles(_state.value.currentPath)
                _state.update { it.copy(files = filesList) }
                if (applySortAndFilter) {
                    applySortAndFilter()
                }
            } catch (e: Exception) {
                _state.update { it.copy(errorMessage = "Failed to load files: ${e.message}") }
            }
        }
    }

    fun createFile(fileName: String, content: String) {
        viewModelScope.launch {
            try {
                fileRepository.createFile(_state.value.currentPath, fileName, content)
                loadFiles()
            } catch (e: Exception) {
                _state.update { it.copy(errorMessage = "Failed to create file: ${e.message}") }
            }
        }
    }

    fun deleteFile(fileName: String) {
        viewModelScope.launch {
            try {
                fileRepository.deleteFile(_state.value.currentPath, fileName)
                loadFiles()
            } catch (e: Exception) {
                _state.update { it.copy(errorMessage = "Failed to delete file: ${e.message}") }
            }
        }
    }

    fun selectFile(fileName: String) {
        viewModelScope.launch {
            try {
                if (_state.value.selectedFileName == fileName) {
                    _state.update { it.copy(selectedFileName = null, selectedFileContent = null) }
                } else {
                    val content = fileRepository.readFile(_state.value.currentPath, fileName)
                    _state.update { it.copy(selectedFileName = fileName, selectedFileContent = content) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(errorMessage = "Failed to select file: ${e.message}") }
            }
        }
    }

    fun updateFileContent(context: Context, fileName: String, content: String) {
        viewModelScope.launch {
            try {
                val file = File(_state.value.currentPath, fileName)
                if (file.exists()) {
                    file.writeText(content)
                    _state.update { it.copy(selectedFileContent = content) }
                    loadFiles()
                } else {
                    _state.update { it.copy(errorMessage = "File not found") }
                }
            } catch (e: Exception) {
                _state.update { it.copy(errorMessage = "Failed to update file content: ${e.message}") }
            }
        }
    }

    fun addMediaFile(context: Context, fileName: String, mediaUri: Uri) {
        viewModelScope.launch {
            try {
                fileRepository.saveMediaFile(context, _state.value.currentPath, fileName, mediaUri)
                loadFiles()
            } catch (e: Exception) {
                _state.update { it.copy(errorMessage = "Error saving media file: ${e.message}") }
            }
        }
    }

    fun createFolder(folderName: String) {
        viewModelScope.launch {
            try {
                fileRepository.createFolder(_state.value.currentPath, folderName)
                loadFiles()
            } catch (e: Exception) {
                _state.update { it.copy(errorMessage = "Failed to create folder: ${e.message}") }
            }
        }
    }

    fun navigateToFolder(folderPath: String) {
        val newPath = File(folderPath)
        _state.update {
            it.copy(
                currentPath = newPath,
                selectedSortOption = it.selectedSortOption,
                selectedFilterOption = it.selectedFilterOption
            )
        }
        loadFiles()
    }

    fun navigateUp() {
        _state.value.currentPath.parentFile?.let { parentFile ->
            _state.update {
                it.copy(
                    currentPath = parentFile,
                    selectedSortOption = it.selectedSortOption,
                    selectedFilterOption = it.selectedFilterOption
                )
            }
            loadFiles()
        }
    }

    fun renameFile(oldName: String, newName: String) {
        viewModelScope.launch {
            try {
                fileRepository.renameFile(_state.value.currentPath, oldName, newName)
                loadFiles()
            } catch (e: Exception) {
                _state.update { it.copy(errorMessage = "Failed to rename file: ${e.message}") }
            }
        }
    }

    fun sortFiles(option: String) {
        _state.update { it.copy(selectedSortOption = option) }
        applySortAndFilter()
    }

    fun filterFiles(option: String) {
        _state.update { it.copy(selectedFilterOption = option) }
        applySortAndFilter()
    }

    private fun applySortAndFilter() {
        val files = fileRepository.getFiles(_state.value.currentPath)
        val sortedFiles = when (_state.value.selectedSortOption) {
            "Name" -> files.sortedBy { it.name }
            "Size" -> files.sortedBy { it.length() }
            "Creation Date" -> files.sortedBy { it.lastModified() }
            else -> files
        }

        val filteredFiles = when (_state.value.selectedFilterOption) {
            "Text" -> sortedFiles.filter { it.isDirectory || it.extension == "txt" }
            "All" -> sortedFiles
            "Images" -> sortedFiles.filter { it.isDirectory || it.extension in listOf("jpg", "jpeg", "png", "gif") }
            "Videos" -> sortedFiles.filter { it.isDirectory || it.extension in listOf("mp4", "avi", "mkv") }
            "Audio" -> sortedFiles.filter { it.isDirectory || it.extension in listOf("mp3", "wav", "m4a", "3gp") }
            else -> sortedFiles
        }

        _state.update { it.copy(files = filteredFiles) }
    }

    fun findFileInCurrentDirectory(context: Context, fileName: String) {
        viewModelScope.launch {
            try {
                val currentDir = _state.value.currentPath
                val file = searchFile(currentDir, fileName)
                if (file != null) {
                    selectFile(file.name)
                } else {
                    _state.update { it.copy(errorMessage = "File not found") }
                }
            } catch (e: Exception) {
                _state.update { it.copy(errorMessage = "Failed to find file: ${e.message}") }
            }
        }
    }

    private fun searchFile(directory: File, fileName: String): File? {
        directory.listFiles()?.forEach { file ->
            if (file.isDirectory) {
                val found = searchFile(file, fileName)
                if (found != null) return found
            } else if (file.name == fileName) {
                return file
            }
        }
        return null
    }
}

