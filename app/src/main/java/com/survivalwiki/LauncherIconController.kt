package com.survivalwiki

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager

/**
 * Switches the launcher entry (activity-alias) so the home screen icon uses
 * the same accent color as in-app settings. Standard launcher icons cannot be
 * tinted at runtime; aliases swap the static adaptive icon per accent.
 */
object LauncherIconController {

    private const val ORANGE = "orange"
    private const val OLIVE = "olive"
    private const val BLUE = "blue"

    private fun alias(context: Context, simpleName: String): ComponentName {
        return ComponentName(context.packageName, "${context.packageName}.$simpleName")
    }

    fun syncFromAccent(context: Context, accent: String?) {
        val normalized = when (accent?.lowercase()) {
            OLIVE -> OLIVE
            BLUE -> BLUE
            else -> ORANGE
        }
        val pm = context.packageManager
        val aliases = mapOf(
            ORANGE to alias(context, "LauncherOrange"),
            OLIVE to alias(context, "LauncherOlive"),
            BLUE to alias(context, "LauncherBlue")
        )
        aliases.forEach { (key, component) ->
            val newState = if (key == normalized) {
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED
            } else {
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED
            }
            try {
                val current = pm.getComponentEnabledSetting(component)
                if (current != newState) {
                    pm.setComponentEnabledSetting(
                        component,
                        newState,
                        PackageManager.DONT_KILL_APP
                    )
                }
            } catch (_: Exception) {
                // ignore
            }
        }
    }
}
