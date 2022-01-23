package com.listocalixto.android.mathsolar.app

import javax.inject.Qualifier

object CoroutinesQualifiers {

    @Retention(AnnotationRetention.RUNTIME)
    @Qualifier
    annotation class IoDispatcher

    @Retention(AnnotationRetention.RUNTIME)
    @Qualifier
    annotation class MainDispatcher

    @Retention(AnnotationRetention.RUNTIME)
    @Qualifier
    annotation class LocalDataSourcePVProject

    @Retention(AnnotationRetention.RUNTIME)
    @Qualifier
    annotation class RemoteDataSourcePVProject

    @Retention(AnnotationRetention.RUNTIME)
    @Qualifier
    annotation class RemoteDataSourceArticle

    @Retention(AnnotationRetention.RUNTIME)
    @Qualifier
    annotation class LocalDataSourceArticle

    @Retention(AnnotationRetention.RUNTIME)
    @Qualifier
    annotation class RetrofitNREL

    @Retention(AnnotationRetention.RUNTIME)
    @Qualifier
    annotation class RetrofiFreeNews

}

