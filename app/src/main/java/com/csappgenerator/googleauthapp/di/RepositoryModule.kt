package com.csappgenerator.googleauthapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.csappgenerator.googleauthapp.data.remote.KtorApi
import com.csappgenerator.googleauthapp.data.repository.AuthRepositoryImpl
import com.csappgenerator.googleauthapp.data.repository.UserRepositoryImpl
import com.csappgenerator.googleauthapp.domain.repository.AuthRepository
import com.csappgenerator.googleauthapp.domain.repository.UserRepository
import com.csappgenerator.googleauthapp.util.Constants.PREFERENCES_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile(PREFERENCES_NAME) }
        )
    }

    @Provides
    @Singleton
    fun provideAuthRepository(dataStore: DataStore<Preferences>): AuthRepository {
        return AuthRepositoryImpl(dataStore)
    }

    @Provides
    @Singleton
    fun provideUserRepository(ktorApi: KtorApi): UserRepository {
        return UserRepositoryImpl(ktorApi)
    }
}