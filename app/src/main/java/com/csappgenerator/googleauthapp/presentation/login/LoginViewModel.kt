package com.csappgenerator.googleauthapp.presentation.login

import android.app.Activity
import android.util.Log
import androidx.activity.result.IntentSenderRequest
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csappgenerator.googleauthapp.domain.model.ApiRequest
import com.csappgenerator.googleauthapp.domain.model.ApiResponse
import com.csappgenerator.googleauthapp.domain.model.MessageBarState
import com.csappgenerator.googleauthapp.domain.repository.AuthRepository
import com.csappgenerator.googleauthapp.domain.repository.UserRepository
import com.csappgenerator.googleauthapp.util.RequestState
import com.csappgenerator.googleauthapp.util.oneTapSignIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _signedInState = mutableStateOf(false)
    val signedInState: State<Boolean> = _signedInState

    private val _messageBarState = mutableStateOf(MessageBarState())
    val messageBarState: State<MessageBarState> = _messageBarState

    private val _apiResponse: MutableState<RequestState<ApiResponse>> =
        mutableStateOf(RequestState.Idle)
    val apiResponse: State<RequestState<ApiResponse>> = _apiResponse

    init {
        viewModelScope.launch {
            authRepository.readSignedInState().collect { signedInState ->
                _signedInState.value = signedInState
            }
        }
    }

    fun saveSignedInState(signedInState: Boolean) {
        viewModelScope.launch {
            authRepository.saveSignedInState(signedInState)
        }
    }

    fun updateMessageBarState(e: Exception) {
        _messageBarState.value =
            MessageBarState(error = e)
    }

    fun signIn(
        activity: Activity,
        launchActivityResult: (IntentSenderRequest) -> Unit,
        accountNotFound: (Exception) -> Unit
    ) {
        oneTapSignIn(
            activity = activity,
            launchActivityResult = launchActivityResult,
            setFilterByAuthorizedAccounts = true,
            setAutoSelectEnabled = true,
            addOnFailure = {
                // It means that not able to find the proper Google account on the smartphone.
                // So signUp is needed:
                Log.d("SignIn", "Signing up...")
                signUp(
                    activity = activity,
                    launchActivityResult = launchActivityResult,
                    accountNotFound = accountNotFound
                )
            }
        )
    }

    private fun signUp(
        activity: Activity,
        launchActivityResult: (IntentSenderRequest) -> Unit,
        accountNotFound: (Exception) -> Unit
    ) {
        oneTapSignIn(
            activity = activity,
            launchActivityResult = launchActivityResult,
            setFilterByAuthorizedAccounts = false,
            setAutoSelectEnabled = false,
            addOnFailure = { exception ->
                // It means that not there is no Google account on the smartphone (very low possibility).
                Log.d("SignUp", exception.message.toString())
                accountNotFound(exception)
            }
        )
    }


    fun verifyTokenOnBackend(request: ApiRequest) {
        _apiResponse.value = RequestState.Loading
        try {
            viewModelScope.launch {
                val response = userRepository.verifyUserOnBackend(request)
                _apiResponse.value = RequestState.Success(data = response)
                _messageBarState.value = MessageBarState(
                    message = response.message,
                )
            }
        } catch (e: Exception) {
            _apiResponse.value = RequestState.Error(t = e)
            _messageBarState.value = MessageBarState(
                error = e,
            )
        }
    }
}



