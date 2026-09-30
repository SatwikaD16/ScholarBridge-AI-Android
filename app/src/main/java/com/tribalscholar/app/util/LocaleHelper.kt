package com.tribalscholar.app.util

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

data class SupportedLanguage(
    val code: String,
    val displayName: String,
    val nativeName: String
)

object LocaleHelper {
    private const val PREFS_NAME = "scholarbridge_language_prefs"
    private const val KEY_LANG = "selected_language"

    val languages = listOf(
        SupportedLanguage(code = "en", displayName = "English", nativeName = "English"),
        SupportedLanguage(code = "ta", displayName = "Tamil", nativeName = "தமிழ்"),
        SupportedLanguage(code = "te", displayName = "Telugu", nativeName = "తెలుగు"),
        SupportedLanguage(code = "hi", displayName = "Hindi", nativeName = "हिन्दी")
    )

    fun getSavedLanguage(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_LANG, "en") ?: "en"
    }

    fun setLanguage(context: Context, languageCode: String): Context {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_LANG, languageCode).apply()

        return createLocalizedContext(context, languageCode)
    }

    fun createLocalizedContext(context: Context, languageCode: String): Context {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        config.setLayoutDirection(locale)

        return context.createConfigurationContext(config)
    }
}
