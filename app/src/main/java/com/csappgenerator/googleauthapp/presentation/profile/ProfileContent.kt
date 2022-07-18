package com.csappgenerator.googleauthapp.presentation.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.csappgenerator.googleauthapp.R
import com.csappgenerator.googleauthapp.component.GoogleButton
import com.csappgenerator.googleauthapp.component.MessageBar
import com.csappgenerator.googleauthapp.domain.model.ApiResponse
import com.csappgenerator.googleauthapp.domain.model.MessageBarState
import com.csappgenerator.googleauthapp.ui.theme.LoadingBlue
import com.csappgenerator.googleauthapp.util.RequestState

@Composable
fun ProfileContent(
    apiResponse: RequestState<ApiResponse>,
    messageBarState: MessageBarState,
    firstName: String,
    onFirstNameChanged: (String) -> Unit,
    lastName: String,
    onLastNameChanged: (String) -> Unit,
    emailAddress: String?,
    profilePhoto: String?,
    onSignOutClicked: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(modifier = Modifier.weight(0.1f)) {
            when (apiResponse) {
                is RequestState.Loading -> {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        color = LoadingBlue
                    )
                }
                else -> {
                    MessageBar(messageBarState = messageBarState)
                }
            }
        }
        Column(
            modifier = Modifier
                .weight(0.9f)
                .fillMaxWidth(0.7f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CentralContent(
                firstName = firstName,
                onFirstNameChanged = onFirstNameChanged,
                lastName = lastName,
                onLastNameChanged = onLastNameChanged,
                emailAddress = emailAddress,
                profilePhoto = profilePhoto,
                onSignOutClicked = onSignOutClicked
            )
        }
    }

}

@Composable
fun CentralContent(
    firstName: String,
    onFirstNameChanged: (String) -> Unit,
    lastName: String,
    onLastNameChanged: (String) -> Unit,
    emailAddress: String?,
    profilePhoto: String?,
    onSignOutClicked: () -> Unit
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(data = profilePhoto)
            .crossfade(1000)
            .placeholder(R.drawable.ic_placeholder)
            .transformations(CircleCropTransformation())
            .build(),
        contentDescription = "Profile photo",
        modifier = Modifier
            .padding(24.dp)
            .size(160.dp),
        contentScale = ContentScale.Crop
    )

    OutlinedTextField(
        value = firstName,
        onValueChange = { onFirstNameChanged(it) },
        label = {
            Text(text = "First name")
        },
        textStyle = MaterialTheme.typography.body1,
        singleLine = true
    )

    OutlinedTextField(
        value = lastName,
        onValueChange = { onLastNameChanged(it) },
        label = {
            Text(text = "Last name")
        },
        textStyle = MaterialTheme.typography.body1,
        singleLine = true
    )

    OutlinedTextField(
        value = emailAddress.toString(),
        onValueChange = {},
        label = {
            Text(text = "Email address")
        },
        textStyle = MaterialTheme.typography.body1,
        singleLine = true,
        enabled = false
    )

    GoogleButton(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        primaryText = "Sign Out",
        onClick = onSignOutClicked
    )
}