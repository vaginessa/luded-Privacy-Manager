package com.eluded.privacymanager.features.panickwipe.trigger.application

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.eluded.privacymanager.Trigger
import com.eluded.privacymanager.Utils

class ApplicationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utils(this).fire(Trigger.APPLICATION)
        finishAndRemoveTask()
    }
}