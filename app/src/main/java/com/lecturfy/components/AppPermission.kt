package com.lecturfy.components

enum class AppPermission(val permission: String) {
    CAMERA(android.Manifest.permission.CAMERA),
    RECORD_AUDIO(android.Manifest.permission.RECORD_AUDIO),
    READ_CONTACTS(android.Manifest.permission.READ_CONTACTS),
    WRITE_CONTACTS(android.Manifest.permission.WRITE_CONTACTS),
    READ_EXTERNAL_STORAGE(android.Manifest.permission.READ_EXTERNAL_STORAGE),
    WRITE_EXTERNAL_STORAGE(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
    ACCESS_FINE_LOCATION(android.Manifest.permission.ACCESS_FINE_LOCATION),
    ACCESS_COARSE_LOCATION(android.Manifest.permission.ACCESS_COARSE_LOCATION),
    INTERNET(android.Manifest.permission.INTERNET),
    ACCESS_NETWORK_STATE(android.Manifest.permission.ACCESS_NETWORK_STATE),
    ACCESS_WIFI_STATE(android.Manifest.permission.ACCESS_WIFI_STATE),
    SEND_SMS(android.Manifest.permission.SEND_SMS),
    RECEIVE_SMS(android.Manifest.permission.RECEIVE_SMS),
    READ_SMS(android.Manifest.permission.READ_SMS),
    CALL_PHONE(android.Manifest.permission.CALL_PHONE);
}