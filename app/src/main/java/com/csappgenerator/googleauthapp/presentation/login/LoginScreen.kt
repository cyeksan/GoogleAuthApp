package com.csappgenerator.googleauthapp.presentation.login

import android.app.Activity
import android.util.Log
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.csappgenerator.googleauthapp.domain.model.ApiRequest
import com.csappgenerator.googleauthapp.domain.model.ApiResponse
import com.csappgenerator.googleauthapp.navigation.Screen
import com.csappgenerator.googleauthapp.util.Exceptions
import com.csappgenerator.googleauthapp.util.RequestState
import com.csappgenerator.googleauthapp.util.navigateTo

@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val activity = (LocalContext.current as Activity)
    val signedInState by viewModel.signedInState
    val messageBarState by viewModel.messageBarState
    val apiResponse by viewModel.apiResponse

    Scaffold(topBar = { LoginTopBar() },
        content = {
            LoginContent(
                signedInState = signedInState,
                messageBarState = messageBarState,
                onButtonClicked = {
                    viewModel.saveSignedInState(signedInState = true)
                }
            )
        })

    StartActivityForResult(
        key = signedInState,
        onResultReceived = { tokenId ->
            Log.d("LoginScreen", tokenId)
            viewModel.verifyTokenOnBackend(ApiRequest(tokenId = tokenId))
        },
        onDialogDismissed = {
            viewModel.saveSignedInState(signedInState = false)
        },
    ) { activityLauncher ->
        if (signedInState) {
            viewModel.signIn(
                activity = activity,
                launchActivityResult = { intentSenderRequest ->
                    activityLauncher.launch(intentSenderRequest)
                },
                accountNotFound = {
                    viewModel.updateMessageBarState(e = Exceptions.GoogleAccountNotFoundException())
                    viewModel.saveSignedInState(signedInState = false)
                }
            )
        }
    }

    LaunchedEffect(key1 = apiResponse) {
        when (apiResponse) {
            is RequestState.Success -> {
                val response = (apiResponse as RequestState.Success<ApiResponse>).data.success
                if (response) {
                    navController.navigateTo(Screen.Profile.route)
                } else {
                    viewModel.saveSignedInState(signedInState = false)
                    (apiResponse as RequestState.Success<ApiResponse>).data.error?.let {
                        viewModel.updateMessageBarState(e = it)
                    }
                }
            }
            else -> {
            }
        }
    }
}