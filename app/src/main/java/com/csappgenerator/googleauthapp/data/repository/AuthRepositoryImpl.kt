package com.csappgenerator.googleauthapp.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.csappgenerator.googleauthapp.domain.repository.AuthRepository
import com.csappgenerator.googleauthapp.util.Constants.PREFERENCES_SIGNED_IN_KEY
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val ioDispatchers: CoroutineDispatcher = Dispatchers.IO
) : AuthRepository {
    private object PreferencesKey {
        val signedInKey = booleanPreferencesKey(PREFERENCES_SIGNED_IN_KEY)
    }

    override suspend fun saveSignedInState(signedInState: Boolean) {
        withContext(ioDispatchers) {
            dataStore.edit { preferences ->
                preferences[PreferencesKey.signedInKey] = signedInState
            }
        }
    }

    override fun readSignedInState(): Flow<Boolean> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val signedInState = preferences[PreferencesKey.signedInKey] ?: false
                signedInState
            }
    }

}