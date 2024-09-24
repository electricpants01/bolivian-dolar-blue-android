package com.locotoinnovations.bolivianbluedolar.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp

@Composable
fun ExchangeRateCard(
    modifier: Modifier = Modifier,
    buyPrice: String,
    sellPrice: String
) {
    Card(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Row(
            modifier = modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(modifier = Modifier.weight(1f))

            Column {
                Text(text = "Buy", style = MaterialTheme.typography.bodyLarge)
                Text(text = buyPrice, style = MaterialTheme.typography.bodySmall)
            }

            Box(modifier = Modifier.weight(1f))

            Column {
                Text(text = "Sell", style = MaterialTheme.typography.bodyLarge)
                Text(text = sellPrice, style = MaterialTheme.typography.bodySmall)
            }

            Box(modifier = Modifier.weight(1f))
        }
    }
}

@PreviewLightDark
@Composable
private fun previewExchangeRateCard() {
    ExchangeRateCard(modifier = Modifier,"6.96", "6.98")
}