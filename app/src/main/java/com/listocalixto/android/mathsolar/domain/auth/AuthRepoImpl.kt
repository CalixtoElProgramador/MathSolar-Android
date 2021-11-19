package com.listocalixto.android.mathsolar.domain.auth

import com.google.firebase.auth.FirebaseUser
import com.listocalixto.android.mathsolar.app.CoroutinesQualifiers.IoDispatcher
import com.listocalixto.android.mathsolar.core.Resource
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
}