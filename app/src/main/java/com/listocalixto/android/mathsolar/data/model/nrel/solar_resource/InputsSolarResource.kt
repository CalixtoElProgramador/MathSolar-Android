package com.listocalixto.android.mathsolar.data.model.nrel.solar_resource

import androidx.annotation.StringRes
import com.listocalixto.android.mathsolar.R

data class InputsSolarResource(
    @StringRes val apiKey: Int = R.string.nrel_key,
    val latitude: Double,
    val longitude: Double,
)
