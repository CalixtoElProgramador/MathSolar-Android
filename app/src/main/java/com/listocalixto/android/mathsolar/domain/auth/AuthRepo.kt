package com.listocalixto.android.mathsolar.domain.auth

import com.google.firebase.auth.FirebaseUser
import com.listocalixto.android.mathsolar.core.Resource

interface AuthRepo {

    suspend fun signIn(email: String, password: String): Resource<FirebaseUser?>

}