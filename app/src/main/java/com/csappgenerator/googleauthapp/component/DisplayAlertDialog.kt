package com.csappgenerator.googleauthapp.component

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight

@Composable
fun DisplayAlertDialog(
    title: String = "Delete your account?",
    message: String = "Are your sure you want to delete your account?",
    openDialog: Boolean,
    onDeleteConfirmed: () -> Unit,
    onDialogClosed: () -> Unit
) {
    if (openDialog) {
        AlertDialog(
            title = {
                Text(
                    text = title,
                    fontSize = MaterialTheme.typography.h5.fontSize,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = message,
                    fontSize = MaterialTheme.typography.subtitle1.fontSize,
                    fontWeight = FontWeight.Normal
                )
            },
            onDismissRequest = { },
            confirmButton = {
                Button(onClick = {
                    onDeleteConfirmed()
                    onDialogClosed()
                }) {
                    Text(text = "Yes")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = {
                    onDialogClosed()
                }) {
                    Text(text = "No")
                }
            }
        )
    }
}