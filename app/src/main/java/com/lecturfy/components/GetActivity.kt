package com.lecturfy.components

import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.lecturfy.MainActivity

@Composable
fun getActivity(): MainActivity {
    var context = LocalContext.current
    while (context !is MainActivity) {
        context = (context as ContextWrapper).baseContext
    }
    return context
}