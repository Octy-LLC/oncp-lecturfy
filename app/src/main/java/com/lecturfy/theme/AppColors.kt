package com.lecturfy.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

data class AppColorScheme(
    val backgroundColor1: Color = Color(android.graphics.Color.parseColor("#F4F6F7")),
    val backgroundColor2: Color = Color(android.graphics.Color.parseColor("#7DDEB0")),
    val headerColor1: Color = Color(android.graphics.Color.parseColor("#1EAD68")),
    val headerColor2: Color = Color(android.graphics.Color.parseColor("#17191D")),
    val textColor1: Color = Color(android.graphics.Color.parseColor("#323B49")),
    val textColor2: Color = Color(android.graphics.Color.parseColor("#4D5664")),
    val borderColor1: Color = Color(android.graphics.Color.parseColor("#EFEFEF")),
    val accentColor1: Color = Color(android.graphics.Color.parseColor("#FFFFFF")),
    val accentColor2: Color = Color(android.graphics.Color.parseColor("#27C87B")),
    val borderColor2: Color = Color(android.graphics.Color.parseColor("#DEDFE0")),
    val borderColor3: Color = Color(android.graphics.Color.parseColor("#FDC0C0")),
    val contentColor1: Color = Color(android.graphics.Color.parseColor("#F04545")),
)

val LocalAppColors = compositionLocalOf {
    AppColorScheme()
}