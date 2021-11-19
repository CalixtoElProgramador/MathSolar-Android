package com.listocalixto.android.mathsolar.data.source.pv_project.remote

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.listocalixto.android.mathsolar.app.CoroutinesQualifiers.IoDispatcher
import com.listocalixto.android.mathsolar.core.Resource
import com.listocalixto.android.mathsolar.data.model.PVProject
import com.listocalixto.android.mathsolar.data.model.PVProjectRemote
import com.listocalixto.android.mathsolar.data.source.pv_project.PVProjectDataSource
import com.listocalixto.android.mathsolar.utils.asLocalModel
import com.listocalixto.android.mathsolar.utils.asRemoteModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.util.*
import javax.inject.Inject

class PVProjectRemoteDataSource @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : PVProjectDataSource {

    private val observableProjects = MutableLiveData<Resource<List<PVProject>>>()

    override fun observePVProjects(): LiveData<Resource<List<PVProject>>> {
        return observableProjects
    }

    override fun observePVProject(projectId: String): LiveData<Resource<PVProject>> {
        return observableProjects.map { projects ->
            when (projects) {
                is Resource.Loading -> Resource.Loading
                is Resource.Error -> Resource.Error(projects.exception)
                is Resource.Success -> {
                    val project = projects.data.firstOrNull { it.uid == projectId }
                        ?: return@map Resource.Error(Exception("Not found"))
                    Resource.Success(project)
                }
            }
        }
    }

    override suspend fun getPVProjects(): Resource<List<PVProject>> = withContext(ioDispatcher) {
        return@withContext try {
            val user = FirebaseAuth.getInstance().currentUser
            val projectsRemoteList = mutableListOf<PVProjectRemote>()
            val querySnapshot =
                FirebaseFirestore.getInstance().collection("pv_projects")
                    .whereEqualTo("userUid", user?.uid)
                    .get().await()
            for (project in querySnapshot.documents) {
                project.toObject(PVProjectRemote::class.java)?.let {
                    projectsRemoteList.add(it)
                }
            }
            Resource.Success(projectsRemoteList.asLocalModel())
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }


    override suspend fun getPVProject(projectId: String): Resource<PVProject> {
        val user = FirebaseAuth.getInstance().currentUser
        val projectsRemoteList = mutableListOf<PVProjectRemote>()
        val querySnapshot =
            FirebaseFirestore.getInstance().collection("pv_projects")
                .whereEqualTo("uid", projectId)
                .whereEqualTo("userUid", user?.uid)
                .get().await()
        for (project in querySnapshot.documents) {
            project.toObject(PVProjectRemote::class.java)?.let {
                projectsRemoteList.add(it)
            }
        }
        return Resource.Success(projectsRemoteList[0].asLocalModel())
    }

    override suspend fun refreshPVProjects() {
        getPVProjects().let { projects ->
            observableProjects.value = projects
        }
    }

    override suspend fun refreshPVProject(projectId: String) {
        refreshPVProjects()
    }

    override suspend fun savePVProject(imageBitmap: Bitmap?, project: PVProject) {
        val user = FirebaseAuth.getInstance().currentUser
        val imageRef =
            FirebaseStorage.getInstance().reference.child("${user?.uid}/pv_project/${project.uid}")
        val baos = ByteArrayOutputStream()
        imageBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val downloadUrl =
            imageRef.putBytes(baos.toByteArray()).await().storage.downloadUrl.await().toString()
        user?.let { currentUser ->
            FirebaseFirestore.getInstance().collection("pv_projects").add(
                project.asRemoteModel(downloadUrl = downloadUrl, userUid = currentUser.uid)
            ).await()
        }
    }

    override suspend fun likePVProject(project: PVProject) {
        TODO("Not yet implemented")
    }

    override suspend fun likePVProject(projectId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun dislikePVProject(project: PVProject) {
        TODO("Not yet implemented")
    }

    override suspend fun dislikePVProject(projectId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllPVProjects() {
        TODO("Not yet implemented")
    }

    override suspend fun deletePVProject(projectId: String) {
        TODO("Not yet implemented")
    }
}