package com.csappgenerator.googleauthapp.domain.repository

import com.csappgenerator.googleauthapp.domain.model.ApiRequest
import com.csappgenerator.googleauthapp.domain.model.ApiResponse
import com.csappgenerator.googleauthapp.domain.model.UserUpdate

interface UserRepository {
    suspend fun verifyUserOnBackend(request: ApiRequest): ApiResponse
    suspend fun getUserInfo(): ApiResponse
    suspend fun updateUser(userUpdate: UserUpdate): ApiResponse
    suspend fun deleteUser(): ApiResponse
    suspend fun clearSession(): ApiResponse
}