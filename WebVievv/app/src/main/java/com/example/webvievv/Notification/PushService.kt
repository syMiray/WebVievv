package com.example.webvievv.Notification

import com.google.firebase.messaging.FirebaseMessagingService

class PushService : FirebaseMessagingService() {
    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
    }
}