package com.lecturfy.components.utils

import android.content.Context
import android.content.res.Configuration
import java.util.Locale


fun updateLocale(context: Context, language: String): Context {
    val locale = Locale(language)
    Locale.setDefault(locale)

    val resources = context.resources
    val configuration = Configuration(resources.configuration)
    configuration.setLocale(locale)
    resources.updateConfiguration(configuration, resources.displayMetrics)

    return context
}

fun getSavedLocale(context: Context): String {
    val prefs = context.getSharedPreferences("localization_prefs", Context.MODE_PRIVATE)
    return prefs.getString("locale_key", Locale.getDefault().language)
        ?: Locale.getDefault().language
}

fun saveLocale(context: Context, language: String) {
    val prefs = context.getSharedPreferences("localization_prefs", Context.MODE_PRIVATE)
    prefs.edit().putString("locale_key", language).apply()
}