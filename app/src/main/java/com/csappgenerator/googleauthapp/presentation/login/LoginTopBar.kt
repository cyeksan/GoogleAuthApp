package com.csappgenerator.googleauthapp.presentation.login

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.csappgenerator.googleauthapp.ui.theme.topAppBarBackgroundColor
import com.csappgenerator.googleauthapp.ui.theme.topAppBarContentColor


@Composable
fun LoginTopBar() {
    TopAppBar(
        title = {
            Text(
                text = "Sign In",
                color = MaterialTheme.colors.topAppBarContentColor
            )
        },
        backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor
    )
}

@Composable
@Preview
fun LoginTopAppBarPreview() {
    LoginTopBar()
}