package com.listocalixto.android.mathsolar.data.source.auth.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.listocalixto.android.mathsolar.app.CoroutinesQualifiers.IoDispatcher
import com.listocalixto.android.mathsolar.core.Resource
import com.listocalixto.android.mathsolar.data.source.auth.AuthDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRemoteDataSource @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : AuthDataSource {

    override suspend fun signIn(email: String, password: String): Resource<FirebaseUser?> =
        withContext(ioDispatcher) {
            return@withContext try {
                val authResult =
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).await()
                Resource.Success(authResult.user)
            } catch (e: Exception) {
                Resource.Error(e)
            }
        }
}