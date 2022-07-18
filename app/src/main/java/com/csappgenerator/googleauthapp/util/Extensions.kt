package com.csappgenerator.googleauthapp.util

import android.app.Activity
import android.util.Log
import androidx.activity.result.IntentSenderRequest
import androidx.compose.runtime.MutableState
import androidx.navigation.NavController
import com.csappgenerator.googleauthapp.navigation.Screen
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity

fun NavController.navigateTo(route: String) {
    if (route == Screen.Login.route) {
        this.navigate(Screen.Login.route) {
            popUpTo(Screen.Profile.route) {
                inclusive = true
            }
        }
    } else {
        this.navigate(Screen.Profile.route) {
            popUpTo(Screen.Login.route) {
                inclusive = true
            }
        }
    }
}

fun MutableState<String>.updateWith(entry: String) {
    if (entry.length <= Constants.MAX_NAME_LENGTH) {
        this.value = entry
    }
}

fun oneTapSignIn(
    activity: Activity,
    launchActivityResult: (IntentSenderRequest) -> Unit,
    setFilterByAuthorizedAccounts: Boolean,
    setAutoSelectEnabled: Boolean,
    addOnFailure: (Exception) -> Unit
) {
    val oneTapClient = Identity.getSignInClient(activity)
    val signInRequest = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(Constants.CLIENT_ID)
                .setFilterByAuthorizedAccounts(setFilterByAuthorizedAccounts)
                .build()
        )
        .setAutoSelectEnabled(setAutoSelectEnabled)
        .build()

    oneTapClient.beginSignIn(signInRequest)
        .addOnSuccessListener { result ->
            try {
                launchActivityResult(
                    IntentSenderRequest.Builder(
                        result.pendingIntent.intentSender
                    ).build()
                )
            } catch (e: Exception) {
                Log.d("OneTapSignIn", "Couldn't start One Tap UI: ${e.message}")
            }
        }
        .addOnFailureListener { exception ->
            addOnFailure(exception)
        }
}
