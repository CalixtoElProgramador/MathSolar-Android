package com.listocalixto.android.mathsolar.utils

import androidx.annotation.StringRes


data class ErrorMessage(
    @StringRes val stringRes: Int? = null,
    val exception: Exception? = null
)