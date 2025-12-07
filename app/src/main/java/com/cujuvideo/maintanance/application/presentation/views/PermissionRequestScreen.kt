package com.cujuvideo.maintanance.application.presentation.views

import android.Manifest
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cujuvideo.maintanance.application.R
import com.cujuvideo.maintanance.application.presentation.Util.RECORD_VIDEO_SCREEN_ROUTE_KEY
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.shouldShowRationale

/**
 * PermissionRequestScreen
 *
 * Composable function responsible for displaying a screen requesting camera and audio permissions.
 * Once the permissions are granted, it navigates to the Record Video Screen.
 *
 * @param navController The NavController used for navigation.
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionRequestScreen(navController: NavController) {
    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )
    )
    LaunchedEffect(Unit) {
        if (!permissionState.allPermissionsGranted) {
            permissionState.launchMultiplePermissionRequest()
        }
    }

    LaunchedEffect(permissionState.allPermissionsGranted) {
        if (permissionState.allPermissionsGranted) {
            navController.navigate(RECORD_VIDEO_SCREEN_ROUTE_KEY)
        }
    }

    when {
        permissionState.shouldShowRationale || !permissionState.allPermissionsGranted -> {
            PermissionsDeniedRationaleScreen(permissionState)
        }

        else -> {
            Text(stringResource(R.string.permission_dialog_text))
        }
    }
}

/**
 * PermissionsDeniedRationaleScreen
 *
 * Composable function responsible for displaying a rationale screen when permissions are denied.
 *
 * @param permissionState The MultiplePermissionsState representing the permission state.
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionsDeniedRationaleScreen(permissionState: MultiplePermissionsState) {
    val anyPermanentlyDenied = permissionState.permissions.any {
        !it.status.isGranted && !it.status.shouldShowRationale
    }
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(stringResource(R.string.permission_dialog_text))
        Spacer(Modifier.height(8.dp))
        Button(onClick = {
            // Manual button to re-launch the permission request dialog
            permissionState.launchMultiplePermissionRequest()
        }) {
            Text(
                if (anyPermanentlyDenied) stringResource(R.string.grant_permission_text)
                else stringResource(R.string.request_permission_text)
            )
        }
    }
}
