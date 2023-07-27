package com.example.webvievv.Notification

import android.app.Application
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging

class Not: Application() {

    override fun onCreate() {
        super.onCreate()

        FirebaseMessaging.getInstance().token.addOnCompleteListener{ task ->
            if(!task.isSuccessful){
                return@addOnCompleteListener
        }
            val token = task.result
            Log.e("TAG","Token -> $token")
    }
}
}