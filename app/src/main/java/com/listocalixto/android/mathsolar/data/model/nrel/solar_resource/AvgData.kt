package com.listocalixto.android.mathsolar.data.model.nrel.solar_resource

import com.google.gson.annotations.SerializedName

data class AvgData(
    @SerializedName("annual")
    val annual: Double,
    @SerializedName("monthly")
    val monthly: Monthly
)
