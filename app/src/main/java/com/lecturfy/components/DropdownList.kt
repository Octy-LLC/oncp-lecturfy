package com.lecturfy.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.focusable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownList(
    options: Array<String>,
    selected: String,
    onSelectionChange: (String) -> Unit,
    textFieldColors: TextFieldColors = TextFieldDefaults.textFieldColors(
        containerColor = Color.White,
        disabledTextColor = Color.Gray,
        focusedTextColor = Color.Black,
        unfocusedTextColor = Color.Black
    ),
    dropdownItemTextColor: Color = Color.Black,
    dropdownBackgroundColor: Color = Color.White,
    textFontSize: TextUnit = 16.sp,
    itemsFontSize: Int = 16,
) {
    var expanded by remember { mutableStateOf(false) }

    Box() {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                value = selected,
                onValueChange = {},
                readOnly = true,
                colors = textFieldColors,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor().focusable(false),
                textStyle = TextStyle(fontSize = textFontSize),
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(color = dropdownBackgroundColor) // Applying background color to dropdown
            ) {
                options.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item, fontStyle = FontStyle(itemsFontSize), color = dropdownItemTextColor) },
                        onClick = {
                            onSelectionChange(item)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
