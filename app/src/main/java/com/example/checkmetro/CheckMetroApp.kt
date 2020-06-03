package com.example.checkmetro

import android.app.Application
import com.facebook.stetho.Stetho

class CheckMetroApp : Application() {


    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
    }
}