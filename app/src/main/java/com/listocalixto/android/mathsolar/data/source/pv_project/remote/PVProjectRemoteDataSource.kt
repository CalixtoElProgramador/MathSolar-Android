package com.listocalixto.android.mathsolar.data.source.pv_project.remote

import androidx.lifecycle.LiveData
import com.listocalixto.android.mathsolar.core.Resource
import com.listocalixto.android.mathsolar.data.model.PVProject
import com.listocalixto.android.mathsolar.data.source.pv_project.PVProjectDataSource
import javax.inject.Inject

class PVProjectRemoteDataSource @Inject constructor(): PVProjectDataSource {

    override fun observePVProjects(): LiveData<Resource<List<PVProject>>> {
        TODO("Not yet implemented")
    }

    override fun observePVProject(projectId: String): LiveData<Resource<PVProject>> {
        TODO("Not yet implemented")
    }

    override suspend fun getPVProjects(): Resource<List<PVProject>> {
        TODO("Not yet implemented")
    }

    override suspend fun getPVProject(projectId: String): Resource<PVProject> {
        TODO("Not yet implemented")
    }

    override suspend fun refreshPVProjects() {
        TODO("Not yet implemented")
    }

    override suspend fun refreshPVProject(projectId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun savePVProject(project: PVProject) {
        TODO("Not yet implemented")
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