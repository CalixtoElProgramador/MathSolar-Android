package com.listocalixto.android.mathsolar.utils

import androidx.annotation.StringRes


data class ErrorMessage(
    val message: String? = null,
    val exception: Exception? = null,
    @StringRes val stringRes: Int? = null
)