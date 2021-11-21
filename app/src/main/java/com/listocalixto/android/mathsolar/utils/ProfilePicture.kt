package com.listocalixto.android.mathsolar.utils

import android.graphics.Bitmap
import android.net.Uri

data class ProfilePicture(
    var bitmap: Bitmap? = null,
    var uri: Uri? = null,
    var drawable: Int? = null
)