package com.survivalwiki.ui.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

fun getIconForName(iconResName: String): ImageVector {
    return when (iconResName) {
        "ic_psychology" -> Icons.Default.Psychology
        "ic_fire", "ic_local_fire_department" -> Icons.Default.LocalFireDepartment
        "ic_water", "ic_water_drop" -> Icons.Default.WaterDrop
        "ic_shelter", "ic_house" -> Icons.Default.House
        "ic_map" -> Icons.Default.Map
        "ic_build" -> Icons.Default.Build
        "ic_phone_android" -> Icons.Default.PhoneAndroid
        "ic_medical_services" -> Icons.Default.MedicalServices
        "ic_restaurant" -> Icons.Default.Restaurant
        "ic_forest" -> Icons.Default.Forest
        "ic_pets" -> Icons.Default.Pets
        "ic_directions_car" -> Icons.Default.DirectionsCar
        "ic_warning" -> Icons.Default.Warning
        "ic_wb_sunny" -> Icons.Default.WbSunny
        "ic_park" -> Icons.Default.Park
        "ic_ac_unit" -> Icons.Default.AcUnit
        "ic_sailing" -> Icons.Default.Sailing
        "ic_explore" -> Icons.Default.Explore
        "ic_campaign" -> Icons.Default.Campaign
        "ic_visibility_off" -> Icons.Default.VisibilityOff
        "ic_people" -> Icons.Default.People
        "ic_bug_report" -> Icons.Default.BugReport
        "ic_set_meal" -> Icons.Default.SetMeal
        "ic_bolt" -> Icons.Default.Bolt
        "ic_gesture" -> Icons.Default.Gesture
        "ic_cancel" -> Icons.Default.Cancel
        "ic_link" -> Icons.Default.Link
        else -> Icons.Default.LocalFireDepartment
    }
}
