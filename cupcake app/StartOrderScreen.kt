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

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cupcake.R
import com.example.cupcake.data.DataSource
import com.example.cupcake.ui.theme.CupcakeTheme

/**
 * Composable that allows the user to select the desired cupcake quantity and expects
 * [onNextButtonClicked] lambda that expects the selected quantity and triggers the navigation to
 * next screen
 */
@Composable
fun StartOrderScreen(
    quantityOptions: List<Pair<Int, Int>>, // List of quantity options represented as pairs of label resource ID and quantity
    onNextButtonClicked: (Int) -> Unit, // Callback function to handle button clicks, passing the selected quantity
    modifier: Modifier = Modifier // Modifier for additional customization
) {
    // Main column to arrange child components vertically
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween // Space between child components
    ) {
        // Column for header content (image and title)
        Column(
            modifier = Modifier.fillMaxWidth(), // Fill available width
            horizontalAlignment = Alignment.CenterHorizontally, // Center items horizontally
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)) // Spacing between items
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium))) // Spacer for vertical spacing
            Image(
                painter = painterResource(R.drawable.cupcake), // Load cupcake image
                contentDescription = null, // No description for decorative image
                modifier = Modifier.width(300.dp) // Set width of the image
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium))) // Spacer for vertical spacing
            Text(
                text = stringResource(R.string.order_cupcakes), // Display title for the screen
                style = MaterialTheme.typography.headlineSmall // Use small headline typography style
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small))) // Spacer for vertical spacing
        }
        // Column for quantity selection buttons
        Column(
            modifier = Modifier.fillMaxWidth(), // Fill available width
            horizontalAlignment = Alignment.CenterHorizontally, // Center items horizontally
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium)) // Spacing between items
        ) {
            // Iterate through the quantity options and create buttons
            quantityOptions.forEach { item ->
                SelectQuantityButton(
                    labelResourceId = item.first, // Pass the label resource ID for the button
                    onClick = { onNextButtonClicked(item.second) }, // Trigger onNextButtonClicked with the quantity
                    modifier = Modifier.fillMaxWidth() // Fill available width for buttons
                )
            }
        }
    }
}

/**
 * Customizable button composable that displays the [labelResourceId]
 * and triggers [onClick] lambda when this composable is clicked
 */
@Composable
fun SelectQuantityButton(
    @StringRes labelResourceId: Int, // Resource ID for button label (string)
    onClick: () -> Unit, // Callback function to handle click events
    modifier: Modifier = Modifier // Modifier for additional customization
) {
    Button(
        onClick = onClick, // Trigger onClick when the button is clicked
        modifier = modifier.widthIn(min = 250.dp) // Set minimum width for the button
    ) {
        Text(stringResource(labelResourceId)) // Display the button label
    }
}

// Preview function to visualize the StartOrderScreen
@Preview
@Composable
fun StartOrderPreview() {
    CupcakeTheme { // Apply the theme for the preview
        StartOrderScreen(
            quantityOptions = DataSource.quantityOptions, // Use quantity options from data source
            onNextButtonClicked = {}, // Empty callback for preview
            modifier = Modifier
                .fillMaxSize() // Fill the available size
                .padding(dimensionResource(R.dimen.padding_medium)) // Add padding
        )
    }
}
