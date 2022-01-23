package com.listocalixto.android.mathsolar.data.model.nrel.solar_resource

import com.google.gson.annotations.SerializedName

data class SolarResource(
    @SerializedName("outputs")
    val outputs: Outputs
)
