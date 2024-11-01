/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.tiptime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tiptime.ui.theme.TipTimeTheme
import java.text.NumberFormat

// Main activity class that serves as the entry point of the app
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge() // Enables edge-to-edge display
        super.onCreate(savedInstanceState) // Calls parent class onCreate
        setContent {  // Sets up the Compose UI
            TipTimeTheme {  // Applies the app's theme
                Surface(  // Creates a surface container
                    modifier = Modifier.fillMaxSize(), // Takes up entire screen
                ) {
                    TipTimeLayout() // Calls main layout composable
                }
            }
        }
    }
}

// Main layout composable function
@Composable
fun TipTimeLayout() {
    // State variables to hold user input and UI state
    var amountInput by remember { mutableStateOf("") }  // Holds bill amount
    var tipInput by remember { mutableStateOf("") }     // Holds tip percentage
    var roundUp by remember { mutableStateOf(false) }   // Holds round-up switch state

    // Convert string inputs to doubles (or 0.0 if invalid)
    val amount = amountInput.toDoubleOrNull() ?: 0.0
    val tipPercent = tipInput.toDoubleOrNull() ?: 0.0
    val tip = calculateTip(amount, tipPercent, roundUp) // Calculate the final tip

    // Main column layout
    Column(
        modifier = Modifier
            .statusBarsPadding()  // Adds padding for status bar
            .padding(horizontal = 40.dp)  // Horizontal padding
            .verticalScroll(rememberScrollState())  // Makes content scrollable
            .safeDrawingPadding(),  // Ensures content is drawn in safe area
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Title text
        Text(
            text = stringResource(R.string.calculate_tip),
            modifier = Modifier
                .padding(bottom = 16.dp, top = 40.dp)
                .align(alignment = Alignment.Start)
        )

        // Bill amount input field
        EditNumberField(
            label = R.string.bill_amount,
            leadingIcon = R.drawable.money,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            value = amountInput,
            onValueChanged = { amountInput = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )

        // Tip percentage input field
        EditNumberField(
            label = R.string.how_was_the_service,
            leadingIcon = R.drawable.percent,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            value = tipInput,
            onValueChanged = { tipInput = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth(),
        )

        // Round up switch row
        RoundTheTipRow(
            roundup = roundUp,
            onRoundUpChanged = { roundUp = it },
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Display calculated tip amount
        Text(
            text = stringResource(R.string.tip_amount, tip),
            style = MaterialTheme.typography.displaySmall
        )

        // Bottom spacer
        Spacer(modifier = Modifier.height(150.dp))
    }
}

// Reusable number input field composable
@Composable
fun EditNumberField(
    @StringRes label: Int,  // Resource ID for label text
    @DrawableRes leadingIcon: Int,  // Resource ID for icon
    keyboardOptions: KeyboardOptions,  // Keyboard configuration
    value: String,  // Current input value
    onValueChanged: (String) -> Unit,  // Callback for value changes
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        singleLine = true,  // Single line input
        leadingIcon = { Icon(painter = painterResource(id = leadingIcon), null) },
        modifier = modifier,
        onValueChange = onValueChanged,
        label = { Text(text = stringResource(id = label)) },
        keyboardOptions = keyboardOptions
    )
}

// Round up switch row composable
@Composable
fun RoundTheTipRow(
    roundup: Boolean,  // Current switch state
    onRoundUpChanged: (Boolean) -> Unit,  // Callback for switch changes
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(id = R.string.round_up_tip))
        Switch(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End),
            checked = roundup,
            onCheckedChange = onRoundUpChanged
        )
    }
}

// Function to calculate the tip amount
private fun calculateTip(
    amount: Double,
    tipPercent: Double = 15.0,
    roundup: Boolean
): String {
    var tip = tipPercent / 100 * amount  // Calculate basic tip
    if (roundup) {
        tip = kotlin.math.ceil(tip)  // Round up if selected
    }
    return NumberFormat.getCurrencyInstance().format(tip)  // Format as currency
}

// Preview function for development
@Preview(showBackground = true)
@Composable
fun TipTimeLayoutPreview() {
    TipTimeTheme {
        TipTimeLayout()
    }
}

