package com.tribalscholar.app

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.tribalscholar.app.ui.navigation.AppNavigation
import com.tribalscholar.app.ui.theme.TribalScholarTheme
import com.tribalscholar.app.util.LocaleHelper

class MainActivity : ComponentActivity() {
    override fun attachBaseContext(newBase: Context) {
        val lang = LocaleHelper.getSavedLanguage(newBase)
        val localized = LocaleHelper.createLocalizedContext(newBase, lang)
        super.attachBaseContext(localized)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TribalScholarTheme {
                AppNavigation()
            }
        }
    }
}
