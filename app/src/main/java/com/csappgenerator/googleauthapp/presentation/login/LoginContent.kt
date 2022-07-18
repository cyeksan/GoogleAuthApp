package com.csappgenerator.googleauthapp.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.csappgenerator.googleauthapp.R
import com.csappgenerator.googleauthapp.component.GoogleButton
import com.csappgenerator.googleauthapp.component.MessageBar
import com.csappgenerator.googleauthapp.domain.model.MessageBarState


@Composable
fun LoginContent(
    signedInState: Boolean,
    messageBarState: MessageBarState,
    onButtonClicked: () -> Unit
) {
    Column(
        Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(modifier = Modifier.weight(0.1f)) {
            MessageBar(messageBarState = messageBarState)
        }
        Column(
            modifier = Modifier
                .weight(0.9f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CentralContent(signedInState = signedInState, onButtonClicked = onButtonClicked)
        }
    }
}

@Composable
fun CentralContent(
    signedInState: Boolean,
    onButtonClicked: () -> Unit
) {
    Image(
        modifier = Modifier
            .padding(bottom = 20.dp)
            .size(120.dp),
        painter = painterResource(id = R.drawable.ic_google_logo),
        contentDescription = stringResource(id = R.string.google_logo)
    )
    Text(
        text = stringResource(R.string.sign_in_title),
        fontWeight = FontWeight.Bold,
        fontSize = MaterialTheme.typography.h5.fontSize
    )
    Text(
        modifier = Modifier
            .alpha(ContentAlpha.medium)
            .padding(bottom = 40.dp, top = 4.dp),
        text = stringResource(R.string.sign_in_description),
        fontSize = MaterialTheme.typography.subtitle1.fontSize,
        textAlign = TextAlign.Center
    )
    GoogleButton(
        loadingState = signedInState,
        onClick = onButtonClicked
    )
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun LoginContentPreview() {
    LoginContent(signedInState = false, messageBarState = MessageBarState()) {

    }
}