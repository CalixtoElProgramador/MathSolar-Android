package com.listocalixto.android.mathsolar.data.model

import java.util.*

data class PVProjectRemote (
    var ambTemperature: Float = -1.0f,
    var batteryCapacity: Float = -1.0f,
    var batteryDepthDischarge: Float = -1.0f,
    var batteryVoltage: Float = -1.0f,
    val createdAt: String = "",
    var daysAutonomy: Int = -1,
    var deleted: Boolean = false,
    var description: String? = "",
    var favorite: Boolean = false,
    var imageUrl: String = "",
    var inverterEfficiency: Float = -1.0f,
    var latitude: Double = -1.0,
    var loadMax: Float = -1.0f,
    var loadMonth: Float = -1.0f,
    var location: String = "",
    var longitude: Double = -1.0,
    var moduleCurrent: Float = -1.0f,
    var modulePower: Double = -1.0,
    var moduleVoltage: Double = -1.0,
    var name: String = "",
    var sunHours: Int = -1,
    var type: Int = -1,
    val uid: String = UUID.randomUUID().toString(),
    val userUid: String = ""
)