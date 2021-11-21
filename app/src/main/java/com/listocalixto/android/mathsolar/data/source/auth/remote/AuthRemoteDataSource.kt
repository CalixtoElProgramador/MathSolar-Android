package com.listocalixto.android.mathsolar.data.source.auth.remote

import android.graphics.Bitmap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.listocalixto.android.mathsolar.app.CoroutinesQualifiers.IoDispatcher
import com.listocalixto.android.mathsolar.core.Resource
import com.listocalixto.android.mathsolar.data.model.User
import com.listocalixto.android.mathsolar.data.source.auth.AuthDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
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

    override suspend fun singUp(
        name: String,
        lastname: String,
        email: String,
        password: String,
        profilePicture: Bitmap
    ): Resource<FirebaseUser?> = withContext(ioDispatcher) {
        return@withContext try {
            val authResult =
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).await()
            authResult.user?.uid?.let { uid ->
                val imageRef =
                    FirebaseStorage.getInstance().reference.child("${uid}/profile_picture")
                val baos = ByteArrayOutputStream()
                profilePicture.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val downloadUrl =
                    imageRef.putBytes(baos.toByteArray()).await().storage.downloadUrl.await()
                        .toString()
                FirebaseFirestore.getInstance().collection("users").document(uid)
                    .set(User(name, lastname, email, downloadUrl, uid)).await()
                Resource.Success(authResult.user)
            } ?: throw Exception()
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    override suspend fun isEmailRegister(email: String): Resource<Boolean> =
        withContext(ioDispatcher) {
            return@withContext try {
                val isRegister =
                    FirebaseFirestore.getInstance().collection("users").whereEqualTo("email", email)
                        .get()
                        .await()
                Resource.Success(isRegister.isEmpty)
            } catch (e: Exception) {
                Resource.Error(e)
            }
        }
}