package com.listocalixto.android.mathsolar.app

import javax.inject.Qualifier

object CoroutinesQualifiers {

    @Retention(AnnotationRetention.RUNTIME)
    @Qualifier
    annotation class IoDispatcher

    @Retention(AnnotationRetention.RUNTIME)
    @Qualifier
    annotation class LocalDataSourcePVProject

    @Retention(AnnotationRetention.RUNTIME)
    @Qualifier
    annotation class RemoteDataSourcePVProject

}
