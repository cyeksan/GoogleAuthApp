package com.csappgenerator.googleauthapp.presentation.profile

import android.app.Activity
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.csappgenerator.googleauthapp.domain.model.ApiRequest
import com.csappgenerator.googleauthapp.navigation.Screen
import com.csappgenerator.googleauthapp.presentation.login.StartActivityForResult
import com.csappgenerator.googleauthapp.util.RequestState
import com.csappgenerator.googleauthapp.util.navigateTo
import com.google.android.gms.auth.api.identity.Identity
import retrofit2.HttpException


@Composable
fun ProfileScreen(
    navController: NavHostController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val apiResponse by viewModel.apiResponse
    val clearSessionResponse by viewModel.clearSessionResponse
    val messageBarState by viewModel.messageBarState

    val user by viewModel.user
    val firstName by viewModel.firstName
    val lastName by viewModel.lastName

    val activity = LocalContext.current as Activity

    StartActivityForResult(
        key = apiResponse,
        onResultReceived = { tokenId ->
            viewModel.verifyTokenOnBackend(ApiRequest(tokenId = tokenId))

        },
        onDialogDismissed = {
            viewModel.saveSignedInState(signedInState = false)
            navController.navigateTo(Screen.Login.route)
        },
    ) { activityLauncher ->
        if (apiResponse is RequestState.Success) {
            val response = (apiResponse as RequestState.Success).data
            if (response.error is HttpException && response.error.code() == 401) {
                viewModel.signIn(
                    activity = activity,
                    accountNotFound = {
                        viewModel.saveSignedInState(signedInState = false)
                        navController.navigateTo(Screen.Login.route)
                    },
                    launchActivityResult = {
                        activityLauncher.launch(it)
                    }
                )
            }
        }
    }

    LaunchedEffect(key1 = clearSessionResponse) {
        if (clearSessionResponse is RequestState.Success &&
            (clearSessionResponse as RequestState.Success).data.success
        ) {
            val oneTapClient = Identity.getSignInClient(activity)
            oneTapClient.signOut()
            viewModel.saveSignedInState(signedInState = false)
            navController.navigateTo(Screen.Login.route)
        }
    }

    Scaffold(
        topBar = {
            ProfileTopBar(
                onDeleteConfirmed = {
                    viewModel.deleteAccount()
                },
                onUpdate = {
                    viewModel.updateUserInfo(firstName, lastName)
                }
            )
        },
        content = {
            ProfileContent(
                apiResponse = apiResponse,
                messageBarState = messageBarState,
                firstName = firstName,
                onFirstNameChanged = { viewModel.updateFirstName(it) },
                lastName = lastName,
                onLastNameChanged = { viewModel.updateLastName(it) },
                emailAddress = user?.email,
                profilePhoto = user?.profilePhoto,
                onSignOutClicked = {
                    viewModel.signOut()
                }
            )
        }
    )
}