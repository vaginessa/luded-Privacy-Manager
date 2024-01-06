package com.eluded.privacymanager.trigger.shortcut

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.eluded.privacymanager.Trigger
import com.eluded.privacymanager.trigger.broadcast.BroadcastReceiver

class ShortcutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BroadcastReceiver.panic(this, intent, Trigger.SHORTCUT)
        finishAndRemoveTask()
    }
}