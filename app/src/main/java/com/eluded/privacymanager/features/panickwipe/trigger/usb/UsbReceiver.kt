package com.eluded.privacymanager.features.panickwipe.trigger.usb

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbManager
import android.util.Log
import android.widget.Toast
import com.eluded.privacymanager.Preferences

import com.eluded.privacymanager.Trigger
import com.eluded.privacymanager.Utils

class UsbReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.extras?.keySet()?.contains("host_connected") != true) return
        val utils = Utils(context ?: return)
        if (utils.prefs.wipeOnUSBOnlyWhenLocked and !utils.isDeviceLocked()) return
        utils.fire(Trigger.USB)
    }
}