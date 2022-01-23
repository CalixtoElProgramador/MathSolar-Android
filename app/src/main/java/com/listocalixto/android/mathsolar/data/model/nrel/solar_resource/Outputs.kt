package com.listocalixto.android.mathsolar.data.model.nrel.solar_resource

import com.google.gson.annotations.SerializedName

data class Outputs(
    @SerializedName("avg_dni")
    val avgDni: AvgData,
    @SerializedName("avg_ghi")
    val avgGhi: AvgData,
    @SerializedName("avg_lat_tilt")
    val avgLatTilt: AvgData
)