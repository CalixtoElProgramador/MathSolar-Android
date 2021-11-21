package com.listocalixto.android.mathsolar.utils

import androidx.annotation.StringRes
import com.listocalixto.android.mathsolar.utils.SnackbarType.DEFAULT

data class SnackbarMessage(
    @StringRes val message: Int,
    val type: SnackbarType = DEFAULT,
    val isError: Boolean = false
)