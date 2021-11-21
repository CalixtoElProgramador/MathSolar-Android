package com.listocalixto.android.mathsolar.domain.auth

import android.graphics.Bitmap
import com.google.firebase.auth.FirebaseUser
import com.listocalixto.android.mathsolar.core.Resource

interface AuthRepo {

    suspend fun signIn(email: String, password: String): Resource<FirebaseUser?>

    suspend fun singUp(
        name: String,
        lastname: String,
        email: String,
        password: String,
        profilePicture: Bitmap
    ): Resource<FirebaseUser?>

    suspend fun isEmailRegister(email: String): Resource<Boolean>

}