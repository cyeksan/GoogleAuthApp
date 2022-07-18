package com.csappgenerator.googleauthapp.presentation.profile

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.csappgenerator.googleauthapp.R
import com.csappgenerator.googleauthapp.component.DisplayAlertDialog
import com.csappgenerator.googleauthapp.ui.theme.topAppBarBackgroundColor
import com.csappgenerator.googleauthapp.ui.theme.topAppBarContentColor


@Composable
fun ProfileTopBar(
    onUpdate: () -> Unit,
    onDeleteConfirmed: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "Profile",
                color = MaterialTheme.colors.topAppBarContentColor
            )
        },
        backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor,
        actions = {
            var openDialog by remember {
                mutableStateOf(false)
            }
            DisplayAlertDialog(
                openDialog = openDialog,
                onDeleteConfirmed = onDeleteConfirmed,
                onDialogClosed = { openDialog = false })

            UpdateAction(
                onUpdateClick = onUpdate
            )
            DeleteAction(
                openConfirmationDialog = { openDialog = true }
            )
        }
    )
}

@Composable
fun UpdateAction(onUpdateClick: () -> Unit) {
    IconButton(onClick = onUpdateClick) {
        Icon(
            painter = painterResource(id = R.drawable.ic_save),
            contentDescription = "Update User Info",
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}

@Composable
fun DeleteAction(
    openConfirmationDialog: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    IconButton(onClick = {
        expanded = true
    }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_vertical_menu),
            contentDescription = "Delete Account",
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = {
            expanded = false
        }) {
        DropdownMenuItem(onClick = {
            expanded = false
            openConfirmationDialog()
        }) {
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = "Delete Account",
                style = MaterialTheme.typography.subtitle2
            )
        }
    }
}

@Preview
@Composable
fun ProfileTopBarPreview() {
    ProfileTopBar(onUpdate = { }) {
    }
}