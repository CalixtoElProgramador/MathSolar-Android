package com.listocalixto.android.mathsolar.data.source.pv_project.local

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.listocalixto.android.mathsolar.app.CoroutinesQualifiers.IoDispatcher
import com.listocalixto.android.mathsolar.core.Resource
import com.listocalixto.android.mathsolar.data.model.PVProject
import com.listocalixto.android.mathsolar.data.source.pv_project.PVProjectDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import com.listocalixto.android.mathsolar.core.Resource.Success
import com.listocalixto.android.mathsolar.core.Resource.Error
import javax.inject.Inject

class PVProjectLocalDataSource @Inject internal constructor(
    private val pvProjectDao: PVProjectDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : PVProjectDataSource {

    override fun observePVProjects(): LiveData<Resource<List<PVProject>>> {
        return pvProjectDao.observePVProjects().map {
            Success(it)
        }
    }

    override fun observePVProject(projectId: String): LiveData<Resource<PVProject>> {
        return pvProjectDao.observeProjectById(projectId).map {
            Success(it)
        }
    }

    override suspend fun getPVProjects(): Resource<List<PVProject>> = withContext(ioDispatcher) {
        return@withContext try {
            Success(pvProjectDao.getPVProjects())
        } catch (e: Exception) {
            Error(e)
        }
    }

    override suspend fun getPVProject(projectId: String): Resource<PVProject> =
        withContext(ioDispatcher) {
            try {
                val project = pvProjectDao.getPVProjectById(projectId)
                if (project != null) {
                    return@withContext Success(project)
                } else {
                    return@withContext Error(Exception("Project not found!"))
                }
            } catch (e: Exception) {
                return@withContext Error(e)
            }
        }

    override suspend fun refreshPVProjects() {
        //NO-OP
    }

    override suspend fun refreshPVProject(projectId: String) {
        //NO-OP
    }

    override suspend fun savePVProject(imageBitmap: Bitmap?, project: PVProject) = withContext(ioDispatcher) {
        pvProjectDao.insertPVProject(project)
    }

    override suspend fun likePVProject(project: PVProject) = withContext(ioDispatcher) {
        pvProjectDao.updateFavorite(project.uid, true)
    }

    override suspend fun likePVProject(projectId: String) = withContext(ioDispatcher) {
        pvProjectDao.updateFavorite(projectId, true)
    }

    override suspend fun dislikePVProject(project: PVProject) = withContext(ioDispatcher) {
        pvProjectDao.updateFavorite(project.uid, false)
    }

    override suspend fun dislikePVProject(projectId: String) = withContext(ioDispatcher) {
        pvProjectDao.updateFavorite(projectId, false)
    }

    override suspend fun deleteAllPVProjects() = withContext(ioDispatcher) {
        pvProjectDao.deletePVProjects()
    }

    override suspend fun deletePVProject(projectId: String) = withContext<Unit>(ioDispatcher) {
        pvProjectDao.deletePVProjectById(projectId)
    }
}