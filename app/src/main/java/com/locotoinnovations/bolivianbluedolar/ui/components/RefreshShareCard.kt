package com.locotoinnovations.bolivianbluedolar.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp

@Composable
fun ActionButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    contentDescription: String,
    buttonText: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(56.dp) // Set a fixed height to make the button more square
            .fillMaxWidth(), // Fill the width
        shape = MaterialTheme.shapes.small // Optional: Set a shape for corners
    ) {
        Icon(
            imageVector = icon, // Icon passed in as parameter
            contentDescription = contentDescription,
            modifier = Modifier.padding(end = 8.dp) // Space between icon and text
        )
        Text(text = buttonText, textAlign = TextAlign.Center)
    }
}

@Composable
fun RefreshShareCard(
    modifier: Modifier = Modifier,
    shouldShowNotificationPermissionButton: Boolean,
    onUpdateClick: () -> Unit,
    onShareClick: () -> Unit,
    onReceiveNotificationClick: () -> Unit
) {
    // Button Card
    Card(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(start = 16.dp, top = 16.dp, end = 16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Button for updating
                ActionButton(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    icon = Icons.Default.Refresh,
                    contentDescription = "Refresh",
                    buttonText = "Actualizar Datos",
                    onClick = onUpdateClick
                )

                // Button for sharing
                ActionButton(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    icon = Icons.Default.Share,
                    contentDescription = "Share",
                    buttonText = "Compartir",
                    onClick = onShareClick
                )
            }

            // Button for receiving notifications
            if (shouldShowNotificationPermissionButton) {
                ActionButton(
                    modifier = Modifier.padding(all = 16.dp),
                    icon = Icons.Default.Notifications,
                    contentDescription = "Recibir Notificaciones",
                    buttonText = "Recibir Notificaciones",
                    onClick = onReceiveNotificationClick
                )
            } else {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}



@PreviewLightDark
@Composable
private fun previewRefreshShareCard() {
    RefreshShareCard(modifier = Modifier, shouldShowNotificationPermissionButton = true, {}, {}, {})
}