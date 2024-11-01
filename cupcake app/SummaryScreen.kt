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
package com.example.cupcake.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.cupcake.R
import com.example.cupcake.data.OrderUiState
import com.example.cupcake.ui.components.FormattedPriceLabel
import com.example.cupcake.ui.theme.CupcakeTheme

/**
 * This composable expects [orderUiState] that represents the order state, [onCancelButtonClicked]
 * lambda that triggers canceling the order and passes the final order to [onSendButtonClicked]
 * lambda
 */
@Composable
fun OrderSummaryScreen(
    orderUiState: OrderUiState, // State containing order details
    onCancelButtonClicked: () -> Unit, // Callback for cancel button click
    onSendButtonClicked: (String, String) -> Unit, // Callback for send button click with subject and summary
    modifier: Modifier = Modifier // Modifier for additional customization
) {
    // Accessing the current context's resources
    val resources = LocalContext.current.resources

    // Get a formatted string for the number of cupcakes based on the quantity
    val numberOfCupcakes = resources.getQuantityString(
        R.plurals.cupcakes, // Resource ID for plural string
        orderUiState.quantity, // The quantity of cupcakes
        orderUiState.quantity // The quantity used in the string
    )

    // Load and format the order summary string with specific parameters
    val orderSummary = stringResource(
        R.string.order_details, // Resource ID for the order details string
        numberOfCupcakes, // Number of cupcakes
        orderUiState.flavor, // Flavor selected
        orderUiState.date, // Pickup date selected
        orderUiState.quantity // Quantity of cupcakes
    )

    // Create a string resource for the new cupcake order title
    val newOrder = stringResource(R.string.new_cupcake_order)

    // Create a list of items to display in the order summary
    val items = listOf(
        // Summary line 1: display selected quantity
        Pair(stringResource(R.string.quantity), numberOfCupcakes),
        // Summary line 2: display selected flavor
        Pair(stringResource(R.string.flavor), orderUiState.flavor),
        // Summary line 3: display selected pickup date
        Pair(stringResource(R.string.pickup_date), orderUiState.date)
    )

    // Main column to arrange child components vertically
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween // Space between child components
    ) {
        // Column for order summary items
        Column(
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)), // Padding around the column
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)) // Spacing between items
        ) {
            // Iterate through the summary items and display them
            items.forEach { item ->
                Text(item.first.uppercase()) // Display the label in uppercase
                Text(text = item.second, fontWeight = FontWeight.Bold) // Display the value in bold
                // Divider between each summary item
                Divider(thickness = dimensionResource(R.dimen.thickness_divider)) 
            }
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small))) // Spacer for vertical spacing
            // Display the formatted price label
            FormattedPriceLabel(
                subtotal = orderUiState.price, // Pass the subtotal to display
                modifier = Modifier.align(Alignment.End) // Align to the end of the column
            )
        }

        // Row for action buttons (Send and Cancel)
        Row(
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)) // Padding around the row
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)) // Spacing between buttons
            ) {
                // Send button
                Button(
                    modifier = Modifier.fillMaxWidth(), // Fill the width of the parent
                    onClick = { onSendButtonClicked(newOrder, orderSummary) } // Trigger send callback with subject and summary
                ) {
                    Text(stringResource(R.string.send)) // Display send text
                }
                // Cancel button
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(), // Fill the width of the parent
                    onClick = onCancelButtonClicked // Trigger cancel callback
                ) {
                    Text(stringResource(R.string.cancel)) // Display cancel text
                }
            }
        }
    }
}

// Preview function to visualize the OrderSummaryScreen
@Preview
@Composable
fun OrderSummaryPreview() {
    CupcakeTheme { // Apply the theme for the preview
        OrderSummaryScreen(
            orderUiState = OrderUiState(0, "Test", "Test", "$300.00"), // Example order state
            onSendButtonClicked = { subject: String, summary: String -> }, // Empty implementation for preview
            onCancelButtonClicked = {}, // Empty implementation for preview
            modifier = Modifier.fillMaxHeight() // Fill the available height in the preview
        )
    }
}
