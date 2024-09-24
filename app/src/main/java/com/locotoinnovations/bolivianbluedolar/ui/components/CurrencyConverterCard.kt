package com.locotoinnovations.bolivianbluedolar.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.locotoinnovations.bolivianbluedolar.R

@Composable
fun CurrencyConverterCard(
    modifier: Modifier = Modifier,
    sellPrice: Double,
) {
    Card(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "USD to BOB", style = MaterialTheme.typography.bodyLarge)

            Text(text = "1 USD = ${String.format(locale = null,"%.2f", sellPrice)} BOB", style = MaterialTheme.typography.bodySmall)

            Spacer(modifier = Modifier.height(16.dp))

            var usdValue by remember { mutableStateOf("") }
            var bobValue by remember { mutableStateOf("") }

            // Function to handle numeric input with commas
            fun isValidInput(input: String): Boolean {
                return input.all { it.isDigit() || it == ',' } && (input.count { it == ',' } <= 1)
            }

            // Update bobValue based on usdValue
            OutlinedTextField(
                value = usdValue,
                onValueChange = { newUsdValue ->
                    if (isValidInput(newUsdValue)) {
                        usdValue = newUsdValue
                        // Convert USD to BOB and update bobValue
                        bobValue = if (newUsdValue.isNotEmpty()) {
                            val valueWithoutComma = newUsdValue.replace(",", ".").toDoubleOrNull() ?: 0.0
                            val convertedValue = valueWithoutComma * sellPrice
                            String.format("%.2f", convertedValue)
                        } else {
                            ""
                        }
                    }
                },
                label = { Text("USD") },
//                leadingIcon = { Icon(imageVector = Icons.Default.Call, contentDescription = "USD Icon") },
                modifier = Modifier.fillMaxWidth(),
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End), // Align text to the end
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal) // Numeric keyboard
            )

            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .size(48.dp) // Adjust size for the circle
                    .border(
                        width = 1.dp, // Set border width
                        color = Color.Gray, // Set border color
                        shape = CircleShape // Makes the border circular
                    )
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp) // Padding inside the circle
            ) {
                Icon(
                    modifier = Modifier.size(24.dp).align(Alignment.Center),
                    painter = painterResource(id = R.drawable.baseline_currency_exchange_24),
                    contentDescription = null,
                )
            }
//            Spacer(modifier = Modifier.height(8.dp))

            // Update usdValue based on bobValue
            OutlinedTextField(
                value = bobValue,
                onValueChange = { newBobValue ->
                    if (isValidInput(newBobValue)) {
                        bobValue = newBobValue
                        // Convert BOB to USD and update usdValue
                        usdValue = if (newBobValue.isNotEmpty()) {
                            val valueWithoutComma = newBobValue.replace(",", ".").toDoubleOrNull() ?: 0.0
                            val convertedValue = valueWithoutComma / sellPrice
                            String.format("%.2f", convertedValue)
                        } else {
                            ""
                        }
                    }
                },
                label = { Text("BOB") },
//                leadingIcon = { Icon(imageVector = Icons.Default.Edit, contentDescription = "BOB Icon") },
                modifier = Modifier.fillMaxWidth(),
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End), // Align text to the end
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal) // Numeric keyboard
            )
        }
    }
}




@PreviewLightDark
@Composable
private fun previewCurrencyConverterCard() {
    CurrencyConverterCard(modifier = Modifier, sellPrice = 6.96)
}