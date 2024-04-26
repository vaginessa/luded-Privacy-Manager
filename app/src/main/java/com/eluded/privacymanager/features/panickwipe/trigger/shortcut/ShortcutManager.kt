package com.eluded.privacymanager.features.panickwipe.trigger.shortcut

import android.content.Context
import android.content.Intent
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat

import com.eluded.privacymanager.Preferences
import com.eluded.privacymanager.R
import com.eluded.privacymanager.features.panickwipe.broadcast.BroadcastReceiver

class ShortcutManager(private val ctx: Context) {
    companion object {
        private const val SHORTCUT_ID = "panic"
    }

    private val prefs by lazy { Preferences(ctx) }

    fun push() =
        ShortcutManagerCompat.pushDynamicShortcut(
            ctx,
            ShortcutInfoCompat.Builder(ctx, SHORTCUT_ID)
                .setShortLabel(ctx.getString(R.string.shortcut_label))
                .setIcon(IconCompat.createWithResource(ctx, android.R.drawable.ic_delete))
                .setIntent(
                    Intent(BroadcastReceiver.ACTION)
                        .setClass(ctx, ShortcutActivity::class.java)
                        .putExtra(BroadcastReceiver.KEY, prefs.secret)
                )
                .build(),
        )

    fun remove() = ShortcutManagerCompat.removeDynamicShortcuts(ctx, arrayListOf(SHORTCUT_ID))
}