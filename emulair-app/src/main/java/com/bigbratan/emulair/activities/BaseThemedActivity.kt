package com.bigbratan.emulair.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bigbratan.emulair.R
import com.bigbratan.emulair.managers.settings.SettingsManager
import com.bigbratan.emulair.ui.CustomMaterialCardView
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

open class BaseThemedActivity : AppCompatActivity() {
    @Inject
    lateinit var settingsManager: SettingsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        applyTheme()
        super.onCreate(savedInstanceState)
        CustomMaterialCardView.baseThemedActivity = this
    }

    private fun applyTheme() {
        val chosenTheme = when (runBlocking { settingsManager.appTheme() }) {
            "dark_theme" -> R.style.Theme_EmulairMaterialYouDark
            "light_theme" -> R.style.Theme_EmulairMaterialYouLight
            "amoled_theme" -> R.style.Theme_EmulairMaterialYouAMOLED
            else -> R.style.Theme_EmulairMaterialYouDark
        }
        setTheme(chosenTheme)
        recreate()
        /*val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)*/
    }

    fun adjustLuminance(luminance: Float): Float {
        return when (runBlocking { settingsManager.appTheme() }) {
            "dark_theme" -> luminance * 0.5f
            "light_theme" -> luminance * 1.05f
            "amoled_theme" -> luminance * 0.5f
            else -> luminance * 0.5f
        }
    }
}
