package com.myapplication.ui

import android.app.Application
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application() {
    override fun onCreate() {
        // Initialize Firebase
        FirebaseApp.initializeApp(this)  // This initializes Firebase
        super.onCreate()
    }
}