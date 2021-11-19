package com.listocalixto.android.mathsolar.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.listocalixto.android.mathsolar.utils.PVProjectType
import java.util.*

@Entity(tableName = "pv_projects")
data class PVProject @JvmOverloads constructor(
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "location") var location: String = "",
    @ColumnInfo(name = "created_at") val createdAt: String = "",
    @ColumnInfo(name = "description") var description: String? = "",
    @ColumnInfo(name = "image_url") var imageUrl: String = "",
    @ColumnInfo(name = "type") var type: PVProjectType,
    @ColumnInfo(name = "is_deleted") var isDeleted: Boolean = false,
    @ColumnInfo(name = "is_favorite") var isFavorite: Boolean = false,
    @ColumnInfo(name = "amb_temperature") var ambTemperature: Float = -1.0f,
    @ColumnInfo(name = "battery_capacity") var batteryCapacity: Float = -1.0f,
    @ColumnInfo(name = "battery_depth_discharge") var batteryDepthDischarge: Float = -1.0f,
    @ColumnInfo(name = "battery_voltage") var batteryVoltage: Float = -1.0f,
    @ColumnInfo(name = "days_autonomy") var daysAutonomy: Int = -1,
    @ColumnInfo(name = "inverter_efficiency") var inverterEfficiency: Float = -1.0f,
    @ColumnInfo(name = "load_max") var loadMax: Float = -1.0f,
    @ColumnInfo(name = "load_month") var loadMonth: Float = -1.0f,
    @ColumnInfo(name = "latitude") var latitude: Double = -1.0,
    @ColumnInfo(name = "longitude") var longitude: Double = -1.0,
    @ColumnInfo(name = "module_current") var moduleCurrent: Float = -1.0f,
    @ColumnInfo(name = "module_power") var modulePower: Double = -1.0,
    @ColumnInfo(name = "module_voltage") var moduleVoltage: Double = -1.0,
    @ColumnInfo(name = "sun_hours") var sunHours: Int = -1,

    @PrimaryKey @ColumnInfo(name = "entry_id") val uid: String = UUID.randomUUID().toString()
)

