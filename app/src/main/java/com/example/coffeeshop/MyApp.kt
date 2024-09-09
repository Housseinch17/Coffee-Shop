package com.example.coffeeshop

import android.app.Application
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyApp : Application(){
    override fun onCreate() {
        super.onCreate()
        //initialize firebase to read data
        FirebaseApp.initializeApp(this)
    }
}