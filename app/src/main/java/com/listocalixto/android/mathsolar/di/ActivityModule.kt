package com.listocalixto.android.mathsolar.di

import com.listocalixto.android.mathsolar.app.CoroutinesQualifiers.RemoteDataSourcePVProject
import com.listocalixto.android.mathsolar.data.source.pv_project.PVProjectDataSource
import com.listocalixto.android.mathsolar.data.source.pv_project.local.PVProjectLocalDataSource
import com.listocalixto.android.mathsolar.data.source.pv_project.remote.PVProjectRemoteDataSource
import com.listocalixto.android.mathsolar.app.CoroutinesQualifiers.LocalDataSourcePVProject
import com.listocalixto.android.mathsolar.domain.pv_project.PVProjectRepo
import com.listocalixto.android.mathsolar.domain.pv_project.PVProjectRepoImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class ActivityModule {

    @Binds
    @LocalDataSourcePVProject
    abstract fun bindPVProjectLocalDataSource(dataSourceImpl: PVProjectLocalDataSource): PVProjectDataSource

    @Binds
    @RemoteDataSourcePVProject
    abstract fun bindPVProjectRemoteDataSource(dataSourceImpl: PVProjectRemoteDataSource): PVProjectDataSource

    @Binds
    abstract fun bindPVProjectRepo(repoImpl: PVProjectRepoImpl): PVProjectRepo

}