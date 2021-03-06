package com.listocalixto.android.mathsolar.domain.auth

import android.graphics.Bitmap
import com.google.firebase.auth.FirebaseUser
import com.listocalixto.android.mathsolar.app.CoroutinesQualifiers.IoDispatcher
import com.listocalixto.android.mathsolar.core.Resource
import com.listocalixto.android.mathsolar.data.model.User
import com.listocalixto.android.mathsolar.data.source.auth.AuthDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepoImpl @Inject constructor(
    private val remoteDataSource: AuthDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : AuthRepo {

    override suspend fun signIn(email: String, password: String): Resource<FirebaseUser?> =
        withContext(ioDispatcher) {
            remoteDataSource.signIn(email, password)
        }

    override suspend fun singUp(
        name: String,
        lastname: String,
        email: String,
        password: String,
        profilePicture: Bitmap
    ): Resource<FirebaseUser?> = withContext(ioDispatcher) {
        remoteDataSource.singUp(name, lastname, email, password, profilePicture)
    }

    override suspend fun isEmailRegister(email: String): Resource<Boolean> =
        withContext(ioDispatcher) {
            remoteDataSource.isEmailRegister(email)
        }

    override suspend fun getCurrentUserData(): Resource<User?> = withContext(ioDispatcher) {
        remoteDataSource.getCurrentUserData()
    }
}