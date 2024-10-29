package com.lecturfy.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun SmallDatePicker(
    date: Long,
    onDateChange: (Long) -> Unit,
    label: String = "Date"
) {
    val isOpen = remember { mutableStateOf(false) }
    val localDate = Instant.ofEpochMilli(date)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

    Row {
        OutlinedTextField(
            readOnly = true,
            value = localDate.format(DateTimeFormatter.ISO_DATE),
            label = { Text(label) },
            onValueChange = {}
        )

        IconButton(
            onClick = { isOpen.value = true }
        ) {
            Icon(imageVector = Icons.Default.DateRange, contentDescription = "Calendar")
        }
    }

    if (isOpen.value) {
        SmallDatePickerDialog(
            initialTimestamp = date,
            onAccept = {
                isOpen.value = false
                if (it != null) {
                    onDateChange(it)
                }
            },
            onCancel = {
                isOpen.value = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmallDatePickerDialog(
    initialTimestamp: Long,
    onAccept: (Long?) -> Unit,
    onCancel: () -> Unit
) {
    val initialSelectedDateMillis = initialTimestamp
    val state = rememberDatePickerState(initialSelectedDateMillis)

    DatePickerDialog(
        onDismissRequest = { },
        confirmButton = {
            Button(onClick = { onAccept(state.selectedDateMillis) }) {
                Text("Accept")
            }
        },
        dismissButton = {
            Button(onClick = onCancel) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = state)
    }
}