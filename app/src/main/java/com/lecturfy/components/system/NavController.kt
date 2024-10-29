package com.lecturfy.components.system

enum class NavControllerViewModelDirectionEnum {
    TO,
    BACK
}

data class NavController(
    val path: String,
    val direction: NavControllerViewModelDirectionEnum,
)