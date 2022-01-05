package com.listocalixto.android.mathsolar.domain

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.listocalixto.android.mathsolar.app.Constants.PREFERENCES_LOCATION_ENABLED
import com.listocalixto.android.mathsolar.app.Constants.PREFERENCES_NAME
import com.listocalixto.android.mathsolar.app.CoroutinesQualifiers.IoDispatcher
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    private object PreferencesKey {
        val isLocationPermissionEnabled = booleanPreferencesKey(PREFERENCES_LOCATION_ENABLED)
    }

    private val Context.datastore: DataStore<Preferences> by preferencesDataStore(PREFERENCES_NAME)

    suspend fun saveIsLocationPermissionEnabled(request: Boolean) = withContext<Unit>(ioDispatcher) {
            context.datastore.edit { preferences ->
                preferences[PreferencesKey.isLocationPermissionEnabled] = request
            }
        }

    val readIsLocationPermissionEnabled: Flow<Boolean> = context.datastore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val response = preferences[PreferencesKey.isLocationPermissionEnabled] ?: false
            response
        }

}
