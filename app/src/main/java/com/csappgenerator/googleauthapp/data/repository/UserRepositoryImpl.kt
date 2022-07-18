package com.csappgenerator.googleauthapp.data.repository

import com.csappgenerator.googleauthapp.data.remote.KtorApi
import com.csappgenerator.googleauthapp.domain.model.ApiRequest
import com.csappgenerator.googleauthapp.domain.model.ApiResponse
import com.csappgenerator.googleauthapp.domain.model.UserUpdate
import com.csappgenerator.googleauthapp.domain.repository.UserRepository
import com.csappgenerator.googleauthapp.util.Exceptions
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val ktorApi: KtorApi,
    private val ioDispatchers: CoroutineDispatcher = Dispatchers.IO
) : UserRepository {
    @Throws(Exceptions.InvalidUserException::class)

    override suspend fun verifyUserOnBackend(request: ApiRequest): ApiResponse {
        return try {
            withContext(ioDispatchers) {
                ktorApi.verifyTokenOnBackend(request)
            }
        } catch (e: Exception) {
            ApiResponse(success = false, error = e)
        }
    }

    override suspend fun getUserInfo(): ApiResponse {
        return try {
            withContext(ioDispatchers) {
                ktorApi.getUserInfo()
            }
        } catch (e: Exception) {
            ApiResponse(success = false, error = e)
        }
    }

    override suspend fun updateUser(userUpdate: UserUpdate): ApiResponse {
        return try {
            withContext(ioDispatchers) {
                if (userUpdate.firstName.isBlank())
                    throw Exceptions.InvalidUserException("User name cannot be empty")

                if (userUpdate.lastName.isBlank())
                    throw Exceptions.InvalidUserException("User last name cannot be empty")

                ktorApi.updateUser(userUpdate)
            }
        } catch (e: Exception) {
            ApiResponse(success = false, error = e)
        }
    }

    override suspend fun deleteUser(): ApiResponse {
        return try {
            withContext(ioDispatchers) {
                ktorApi.deleteUser()
            }
        } catch (e: Exception) {
            ApiResponse(success = false, error = e)
        }
    }

    override suspend fun clearSession(): ApiResponse {
        return try {
            withContext(ioDispatchers) {
                ktorApi.clearSession()
            }
        } catch (e: Exception) {
            ApiResponse(success = false, error = e)
        }
    }
}