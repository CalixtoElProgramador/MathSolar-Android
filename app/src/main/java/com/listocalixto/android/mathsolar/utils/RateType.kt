package com.listocalixto.android.mathsolar.utils

import com.listocalixto.android.mathsolar.R

enum class RateType(val ratesIdResource: Int, var rateSelected: Int) {

    DOMESTIC(R.array.domestic_rates, 0),

    DAC(R.array.dac_rates, 0),

    COMMERCIAL(R.array.commercial_rates, 0),

    MEDIUM_VOLTAGE(R.array.medium_voltage_rates, 0)

}