package com.boom.compass

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

class MyApp: Application() {

    override fun onCreate() {
        AppCompatDelegate.setDefaultNightMode(getSharedPreferences("prefs",Context.MODE_PRIVATE).getInt("mode",AppCompatDelegate.MODE_NIGHT_NO))
        super.onCreate()
    }
}