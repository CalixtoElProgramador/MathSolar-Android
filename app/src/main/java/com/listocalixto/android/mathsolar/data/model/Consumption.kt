package com.listocalixto.android.mathsolar.data.model

import java.util.*

data class Consumption(
    val quantity: Double,
    val startMonth: Date? = null,
    val endMonth: Date? = null
)
