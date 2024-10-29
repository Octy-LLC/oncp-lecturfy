package com.lecturfy.components.filemanager

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lecturfy.MainActivity
import com.lecturfy.components.hardware.media.VideoButton
import com.lecturfy.components.hardware.media.PhotoButton
import com.lecturfy.components.hardware.microphone.MicrophoneManager
import java.io.File

@Composable
fun FileManagerScreen(
    context: MainActivity,
    viewModel: FileViewModel = hiltViewModel(),
    dialogBackgroundColor: Color = Color.White,
    dialogContentColor: Color = Color.Black,
    buttonTextColor: Color = Color.White,
    buttonBackgroundColor: Color = Color.Blue,
    pathTextColor: Color = Color.Black,
    separatorTextColor: Color = Color.Gray,
    fileItemBackgroundColor: Color = Color.Gray,
    fileItemSelectedBackgroundColor: Color = Color.LightGray,
    fileItemTextColor: Color = Color.Black,
    fileItemIconSize: Dp = 48.dp,
    fileItemFontSize: TextUnit = 12.sp,
) {
    val microphoneManager = remember { MicrophoneManager() }
    val state by viewModel.state.collectAsState()
    var isViewerOpen by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var dialogType by remember { mutableStateOf("") }
    var fileName by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }
    var isRecording by remember { mutableStateOf(false) }

    state.errorMessage?.let { errorMessage ->
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        viewModel.clearErrorMessage()
    }

    DialogComponent(
        showDialog = showDialog,
        dialogType = dialogType,
        fileName = fileName,
        onFileNameChange = { fileName = it },
        onCreate = {
            if (fileName.isNotBlank()) {
                if (dialogType == "file") {
                    viewModel.createFile("$fileName.txt", "")
                } else {
                    viewModel.createFolder(fileName)
                }
                showDialog = false
            } else {
                errorMessage = "Name cannot be empty"
                showError = true
            }
        },
        onCancel = { showDialog = false },
        dialogBackgroundColor = dialogBackgroundColor,
        dialogContentColor = dialogContentColor,
        buttonTextColor = buttonTextColor,
        buttonBackgroundColor = buttonBackgroundColor
    )

    ErrorToast(
        showError = showError,
        context = context,
        errorMessage = errorMessage,
        onDismiss = { showError = false }
    )

    FileViewer(
        context = context,
        isViewerOpen = isViewerOpen,
        state = state,
        onClose = { isViewerOpen = false },
        onSave = { updatedContent ->
            viewModel.updateFileContent(context, state.selectedFileName!!, updatedContent)
            isViewerOpen = false
        },
        onDelete = {
            viewModel.deleteFile(state.selectedFileName!!)
            isViewerOpen = false
        }
    )

    if (!isViewerOpen) {
        Column(modifier = Modifier.padding(16.dp)) {
            PathNavigation(
                context,
                root = context.cacheDir!!,
                currentPath = state.currentPath,
                onNavigateToPath = {
                    viewModel.navigateToFolder(it)
                },
                pathTextColor = pathTextColor,
                separatorTextColor = separatorTextColor
            )

            Spacer(modifier = Modifier.height(16.dp))

            FileManagerControls(
                context = context,
                state = state,
                setDialogType = { dialogType = it },
                setShowDialog = { showDialog = it },
                microphoneManager = microphoneManager,
                isRecording = isRecording,
                setIsRecording = { isRecording = it },
                searchQuery = searchQuery,
                setSearchQuery = { searchQuery = it },
                selectedSortOption = state.selectedSortOption,
                selectedFilterOption = state.selectedFilterOption,
                onNavigateUp = {
                    viewModel.navigateUp()
                },
                onSearch = { viewModel.findFileInCurrentDirectory(context, searchQuery) },
                onFinishRecording = { viewModel.loadFiles() },
                onSortOptionSelected = { option ->
                    viewModel.sortFiles(option)
                },
                onFilterOptionSelected = { option ->
                    viewModel.filterFiles(option)
                },
                buttonBackgroundColor = buttonBackgroundColor,
                buttonTextColor = buttonTextColor
            )

            Spacer(modifier = Modifier.height(16.dp))

            FileList(
                state = state,
                onFileClick = { file ->
                    if (file.isDirectory) {
                        viewModel.navigateToFolder(file.absolutePath)
                    } else {
                        viewModel.selectFile(file.name)
                        isViewerOpen = true
                    }
                },
                fileItemBackgroundColor = fileItemBackgroundColor,
                fileItemSelectedBackgroundColor = fileItemSelectedBackgroundColor,
                fileItemTextColor = fileItemTextColor,
                fileItemIconSize = fileItemIconSize,
                fileItemFontSize = fileItemFontSize
            )
        }
    }
}

@Composable
fun DialogComponent(
    showDialog: Boolean,
    dialogType: String,
    fileName: String,
    onFileNameChange: (String) -> Unit,
    onCreate: () -> Unit,
    onCancel: () -> Unit,
    dialogBackgroundColor: Color = Color.White,
    dialogContentColor: Color = Color.Black,
    buttonTextColor: Color = Color.White,
    buttonBackgroundColor: Color = Color.Blue
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onCancel,
            title = { Text(if (dialogType == "file") "Create File" else "Create Folder", color = dialogContentColor) },
            text = {
                Column {
                    TextField(
                        value = fileName,
                        onValueChange = onFileNameChange,
                        label = { Text("Name", color = dialogContentColor) },
                        textStyle = TextStyle(color = dialogContentColor)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = onCreate,
                    colors = ButtonDefaults.buttonColors(containerColor = buttonBackgroundColor)
                ) {
                    Text("Create", color = buttonTextColor)
                }
            },
            dismissButton = {
                Button(
                    onClick = onCancel,
                    colors = ButtonDefaults.buttonColors(containerColor = buttonBackgroundColor)
                ) {
                    Text("Cancel", color = buttonTextColor)
                }
            },
            modifier = Modifier.background(dialogBackgroundColor)
        )
    }
}

@Composable
fun ErrorToast(showError: Boolean, context: Context, errorMessage: String, onDismiss: () -> Unit) {
    if (showError) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        onDismiss()
    }
}

@Composable
fun FileViewer(
    context: MainActivity,
    isViewerOpen: Boolean,
    state: FileViewModelState,
    onClose: () -> Unit,
    onSave: (String) -> Unit,
    onDelete: () -> Unit
) {
    if (isViewerOpen && state.selectedFileName != null) {
        FileViewerScreen(
            context,
            сurrentDir = state.currentPath.toString(),
            fileName = state.selectedFileName!!,
            fileContent = state.selectedFileContent,
            onBack = onClose,
            onSave = onSave,
            onDelete = onDelete
        )
    }
}

@Composable
fun FileManagerControls(
    context: MainActivity,
    state: FileViewModelState,
    setDialogType: (String) -> Unit,
    setShowDialog: (Boolean) -> Unit,
    microphoneManager: MicrophoneManager,
    isRecording: Boolean,
    setIsRecording: (Boolean) -> Unit,
    searchQuery: String,
    setSearchQuery: (String) -> Unit,
    selectedSortOption: String,
    selectedFilterOption: String,
    onNavigateUp: () -> Unit,
    onSearch: () -> Unit,
    onFinishRecording: () -> Unit,
    onSortOptionSelected: (String) -> Unit,
    onFilterOptionSelected: (String) -> Unit,
    buttonBackgroundColor: Color = Color.Blue,
    buttonTextColor: Color = Color.White
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onNavigateUp,
                colors = ButtonDefaults.buttonColors(containerColor = buttonBackgroundColor)
            ) {
                Text("Up", color = buttonTextColor)
            }
            Button(
                onClick = {
                    setDialogType("file")
                    setShowDialog(true)
                },
                colors = ButtonDefaults.buttonColors(containerColor = buttonBackgroundColor)
            ) {
                Text("Create File", color = buttonTextColor)
            }
            Button(
                onClick = {
                    setDialogType("folder")
                    setShowDialog(true)
                },
                colors = ButtonDefaults.buttonColors(containerColor = buttonBackgroundColor)
            ) {
                Text("Create Folder", color = buttonTextColor)
            }
            VideoButton(
                context,
                directory = state.currentPath.toString(),
                onFinish = { onFinishRecording() }
            )
            PhotoButton(
                context,
                directory = state.currentPath.toString(),
                onFinish = { onFinishRecording() }
            )
            Button(
                onClick = {
                    if (isRecording) {
                        microphoneManager.stopRecording()
                        setIsRecording(false)
                        onFinishRecording()
                    } else {
                        val audioFile = microphoneManager.startRecording(state.currentPath)
                        setIsRecording(audioFile != null)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = buttonBackgroundColor)
            ) {
                Text(if (isRecording) "Stop Recording" else "Capture Audio", color = buttonTextColor)
            }
            Button(
                onClick = onSearch,
                colors = ButtonDefaults.buttonColors(containerColor = buttonBackgroundColor)
            ) {
                Text("Find", color = buttonTextColor)
            }
            TextField(
                value = searchQuery,
                onValueChange = setSearchQuery,
                label = { Text("Search", color = buttonTextColor) }
            )
            SortComponent(
                selectedSortOption = selectedSortOption,
                onSortOptionSelected = onSortOptionSelected
            )
            FilterComponent(
                selectedFilterOption = selectedFilterOption,
                onFilterOptionSelected = onFilterOptionSelected
            )
        }
    }
}

@Composable
fun SortComponent(
    selectedSortOption: String,
    onSortOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(onClick = { expanded = true }) {
            Text("Sort by: $selectedSortOption")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            listOf("Name", "Size", "Creation Date").forEach { option ->
                DropdownMenuItem(text = {
                    Text(option)
                }, onClick = {
                    onSortOptionSelected(option)
                    expanded = false
                })
            }
        }
    }
}

@Composable
fun FilterComponent(
    selectedFilterOption: String,
    onFilterOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(onClick = { expanded = true }) {
            Text("Filter by: $selectedFilterOption")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            listOf(
                "All",
                "Text",
                "Images",
                "Videos",
                "Audio"
            ).forEach { option ->
                DropdownMenuItem(text = {
                    Text(option)
                }, onClick = {
                    onFilterOptionSelected(option)
                    expanded = false
                })
            }
        }
    }
}


@Composable
fun FileList(
    state: FileViewModelState,
    onFileClick: (File) -> Unit,
    fileItemBackgroundColor: Color = Color.Gray,
    fileItemSelectedBackgroundColor: Color = Color.LightGray,
    fileItemTextColor: Color = Color.Black,
    fileItemIconSize: Dp = 48.dp,
    fileItemFontSize: TextUnit = 12.sp
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(state.files.chunked(3)) { rowFiles ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                rowFiles.forEach { file ->
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxHeight()
                            .weight(1f)
                            .aspectRatio(1f)
                    ) {
                        FileItem(
                            file = file,
                            isSelected = file.nameWithoutExtension == state.selectedFileName,
                            onClick = { onFileClick(file) },
                            backgroundColor = fileItemBackgroundColor,
                            selectedBackgroundColor = fileItemSelectedBackgroundColor,
                            textColor = fileItemTextColor,
                            iconSize = fileItemIconSize,
                            fontSize = fileItemFontSize
                        )
                    }
                }
                repeat(3 - rowFiles.size) {
                    Spacer(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun FileManagerSmallScreen(
    context: MainActivity,
    onPathChange: (String) -> Unit,
    viewModel: FileViewModel = hiltViewModel(),
    backgroundColor: Color = Color.White,
    buttonTextColor: Color = Color.Black,
    folderNameFontSize: TextUnit = 12.sp
) {
    val state by viewModel.state.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var fileName by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(state.currentPath) {
        onPathChange(state.currentPath.toString())
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Create Folder") },
            text = {
                Column {
                    TextField(
                        value = fileName,
                        onValueChange = { fileName = it },
                        label = { Text("Name") }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (fileName.isNotBlank()) {
                            viewModel.createFolder(fileName)
                            showDialog = false
                        } else {
                            errorMessage = "Name cannot be empty"
                            showError = true
                        }
                    }
                ) {
                    Text("Create")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showError) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        showError = false
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .height(400.dp)
            .background(backgroundColor)
    ) {
        PathNavigation(
            context = context,
            root = context.cacheDir!!,
            currentPath = state.currentPath,
            onNavigateToPath = { viewModel.navigateToFolder(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = { viewModel.navigateUp() }) {
                Text("Up", color = buttonTextColor)
            }
            Button(onClick = {
                showDialog = true
            }) {
                Text("Create Folder", color = buttonTextColor)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(state.files.filter { file -> file.isDirectory }.chunked(3)) { rowFiles ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Max),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    rowFiles.forEach { file ->
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .fillMaxHeight()
                                .weight(1f)
                        ) {
                            FileItemSmall(
                                file = file,
                                isSelected = file.nameWithoutExtension == state.selectedFileName,
                                onClick = {
                                    viewModel.navigateToFolder(file.absolutePath)
                                },
                                folderNameFontSize = folderNameFontSize
                            )
                        }
                    }
                    repeat(3 - rowFiles.size) {
                        Spacer(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1f)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun PathNavigation(
    context: Context,
    root: File,
    currentPath: File,
    onNavigateToPath: (String) -> Unit,
    pathTextColor: Color = Color.Black,
    separatorTextColor: Color = Color.Gray
) {
    val relativePath = if (currentPath == root) "" else currentPath.relativeTo(root).path
    val pathSegments = if (relativePath.isNotEmpty()) relativePath.split(File.separator)
        .filter { it.isNotEmpty() } else emptyList()
    var path = root.absolutePath

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "root",
            color = pathTextColor,
            modifier = Modifier.clickable { onNavigateToPath(context.cacheDir.toString()) }
        )
        pathSegments.forEachIndexed { index, segment ->
            path = "$path/$segment"
            Text(text = " / ", fontWeight = FontWeight.Bold, color = separatorTextColor)
            Text(
                text = segment,
                color = pathTextColor,
                modifier = Modifier.clickable {
                    onNavigateToPath(path)
                }
            )
        }
    }
}

