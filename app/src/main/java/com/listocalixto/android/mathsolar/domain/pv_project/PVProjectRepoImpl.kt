package com.listocalixto.android.mathsolar.domain.pv_project

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import com.listocalixto.android.mathsolar.app.CoroutinesQualifiers.RemoteDataSourcePVProject
import com.listocalixto.android.mathsolar.app.CoroutinesQualifiers.LocalDataSourcePVProject
import com.listocalixto.android.mathsolar.app.CoroutinesQualifiers.IoDispatcher
import com.listocalixto.android.mathsolar.core.Resource
import com.listocalixto.android.mathsolar.data.model.PVProject
import com.listocalixto.android.mathsolar.data.source.pv_project.PVProjectDataSource
import com.listocalixto.android.mathsolar.core.Resource.Success
import com.listocalixto.android.mathsolar.utils.ErrorMessage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class PVProjectRepoImpl @Inject constructor(
    @RemoteDataSourcePVProject private val remoteDataSource: PVProjectDataSource,
    @LocalDataSourcePVProject private val localDataSource: PVProjectDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : PVProjectRepo {

    override fun observePVProjects(): LiveData<Resource<List<PVProject>>> =
        localDataSource.observePVProjects()

    override fun observePVProject(projectId: String): LiveData<Resource<PVProject>> =
        localDataSource.observePVProject(projectId)

    override suspend fun getPVProjects(forceUpdate: Boolean): Resource<List<PVProject>> {
        if (forceUpdate) {
            try {
                updateProjectsFromRemoteDataSource()
            } catch (e: Exception) {
                return Resource.Error(ErrorMessage(exception = e))
            }
        }
        return localDataSource.getPVProjects()
    }

    private suspend fun updateProjectsFromRemoteDataSource() {
        val remoteProjects = remoteDataSource.getPVProjects()
        if (remoteProjects is Success) {
            localDataSource.deleteAllPVProjects()
            remoteProjects.data.forEach { project ->
                localDataSource.savePVProject(null, project)
            }
        } else if (remoteProjects is Resource.Error) {
            throw remoteProjects.errorMessage.exception!!
        }
    }

    /**
     * Relies on [getPVProjects] to fetch data and picks the task with the same ID.
     */
    override suspend fun getPVProject(
        projectId: String,
        forceUpdate: Boolean
    ): Resource<PVProject> {
        if (forceUpdate) {
            updateProjectFromRemoteDataSource(projectId)
        }
        return localDataSource.getPVProject(projectId)
    }

    private suspend fun updateProjectFromRemoteDataSource(projectId: String) {
        val remoteProject = remoteDataSource.getPVProject(projectId)
        if (remoteProject is Success) {
            localDataSource.savePVProject(null, remoteProject.data)
        }
    }

    override suspend fun refreshPVProjects() {
        updateProjectsFromRemoteDataSource()
    }

    override suspend fun refreshPVProject(projectId: String) {
        updateProjectFromRemoteDataSource(projectId)
    }

    override suspend fun savePVProject(imageBitmap: Bitmap?, project: PVProject) {
        coroutineScope {
            launch { localDataSource.savePVProject(null, project) }
            launch { remoteDataSource.savePVProject(imageBitmap, project) }
        }
    }

    override suspend fun likePVProject(project: PVProject) = withContext<Unit>(ioDispatcher) {
        coroutineScope {
            launch { localDataSource.likePVProject(project) }
            launch { remoteDataSource.likePVProject(project) }
        }
    }

    override suspend fun likePVProject(projectId: String) {
        withContext(ioDispatcher) {
            (getProjectWithId(projectId) as? Success)?.let {
                likePVProject(it.data)
            }
        }
    }

    override suspend fun dislikePVProject(project: PVProject) = withContext<Unit>(ioDispatcher) {
        coroutineScope {
            launch { localDataSource.dislikePVProject(project) }
            launch { remoteDataSource.dislikePVProject(project) }
        }
    }

    override suspend fun dislikePVProject(projectId: String) {
        withContext(ioDispatcher) {
            (getProjectWithId(projectId) as? Success)?.let {
                dislikePVProject(it.data)
            }
        }
    }

    override suspend fun deleteAllPVProjects() {
        withContext(ioDispatcher) {
            coroutineScope {
                launch { localDataSource.deleteAllPVProjects() }
                launch { remoteDataSource.deleteAllPVProjects() }
            }
        }
    }

    override suspend fun deletePVProject(projectId: String) {
        coroutineScope {
            launch { localDataSource.deletePVProject(projectId) }
            launch { remoteDataSource.deletePVProject(projectId) }
        }
    }

    private suspend fun getProjectWithId(projectId: String): Resource<PVProject> {
        return localDataSource.getPVProject(projectId)
    }

}