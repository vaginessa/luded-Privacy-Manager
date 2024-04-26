package com.eluded.privacymanager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class TriggerReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        com.eluded.privacymanager.features.panickwipe.broadcast.BroadcastReceiver()
            .onReceive(context, intent)
    }
}