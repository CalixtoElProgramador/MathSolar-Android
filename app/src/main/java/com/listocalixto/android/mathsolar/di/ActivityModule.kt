package com.listocalixto.android.mathsolar.di

import com.listocalixto.android.mathsolar.app.CoroutinesQualifiers.LocalDataSourceArticle
import com.listocalixto.android.mathsolar.app.CoroutinesQualifiers.RemoteDataSourceArticle
import com.listocalixto.android.mathsolar.app.CoroutinesQualifiers.RemoteDataSourcePVProject
import com.listocalixto.android.mathsolar.data.source.pv_project.PVProjectDataSource
import com.listocalixto.android.mathsolar.data.source.pv_project.local.PVProjectLocalDataSource
import com.listocalixto.android.mathsolar.data.source.pv_project.remote.PVProjectRemoteDataSource
import com.listocalixto.android.mathsolar.app.CoroutinesQualifiers.LocalDataSourcePVProject
import com.listocalixto.android.mathsolar.data.source.article.ArticleDataSource
import com.listocalixto.android.mathsolar.data.source.article.local.ArticleLocalDataSource
import com.listocalixto.android.mathsolar.data.source.article.remote.ArticleRemoteDataSource
import com.listocalixto.android.mathsolar.data.source.auth.AuthDataSource
import com.listocalixto.android.mathsolar.data.source.auth.remote.AuthRemoteDataSource
import com.listocalixto.android.mathsolar.data.source.nrel.solar_resource.SolarResourceDataSource
import com.listocalixto.android.mathsolar.data.source.nrel.solar_resource.remote.SolarResourceRemoteDataSource
import com.listocalixto.android.mathsolar.domain.article.ArticleRepo
import com.listocalixto.android.mathsolar.domain.article.ArticleRepoImpl
import com.listocalixto.android.mathsolar.domain.auth.AuthRepo
import com.listocalixto.android.mathsolar.domain.auth.AuthRepoImpl
import com.listocalixto.android.mathsolar.domain.nrel.solar_resource.SolarResourceRepo
import com.listocalixto.android.mathsolar.domain.nrel.solar_resource.SolarResourceRepoImpl
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

    @Binds
    abstract fun bindAuthRemoteDataSource(dataSourceImpl: AuthRemoteDataSource): AuthDataSource

    @Binds
    abstract fun bindAuthRepo(repoImpl: AuthRepoImpl): AuthRepo

    @Binds
    @RemoteDataSourceArticle
    abstract fun bindArticleRemoteDataSource(dataSourceImpl: ArticleRemoteDataSource): ArticleDataSource

    @Binds
    @LocalDataSourceArticle
    abstract fun bindArticleLocalDataSource(dataSourceImpl: ArticleLocalDataSource): ArticleDataSource

    @Binds
    abstract fun bindArticleRepo(repoImpl: ArticleRepoImpl): ArticleRepo

    @Binds
    abstract fun bindSolarResourceRemoteDataSource(dataSourceImpl: SolarResourceRemoteDataSource): SolarResourceDataSource

    @Binds
    abstract fun bindSolarResourceRepo(repoImpl: SolarResourceRepoImpl): SolarResourceRepo

}