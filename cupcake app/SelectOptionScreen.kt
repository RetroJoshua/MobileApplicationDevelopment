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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.cupcake.R
import com.example.cupcake.ui.components.FormattedPriceLabel
import com.example.cupcake.ui.theme.CupcakeTheme

/**
 * Composable that displays the list of items as [RadioButton] options,
 * [onSelectionChanged] lambda that notifies the parent composable when a new value is selected,
 * [onCancelButtonClicked] lambda that cancels the order when user clicks cancel and
 * [onNextButtonClicked] lambda that triggers the navigation to next screen
 */
@Composable
fun SelectOptionScreen(
    subtotal: String, // The subtotal to display
    options: List<String>, // List of options for the user to select
    onSelectionChanged: (String) -> Unit = {}, // Callback for when the selection changes
    onCancelButtonClicked: () -> Unit = {}, // Callback for when the cancel button is clicked
    onNextButtonClicked: () -> Unit = {}, // Callback for when the next button is clicked
    modifier: Modifier = Modifier // Modifier for additional customization
) {
    // State to hold the currently selected value, persisted across recompositions
    var selectedValue by rememberSaveable { mutableStateOf("") }

    // Main column to arrange child components vertically
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween // Space between child components
    ) {
        // Column for options and price label
        Column(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))) {
            // Iterate through options and create selectable rows
            options.forEach { item ->
                Row(
                    modifier = Modifier.selectable(
                        selected = selectedValue == item, // Highlight if the item is selected
                        onClick = {
                            selectedValue = item // Update selected value
                            onSelectionChanged(item) // Trigger callback with selected item
                        }
                    ),
                    verticalAlignment = Alignment.CenterVertically // Align items vertically in the center
                ) {
                    // Radio button to represent selection
                    RadioButton(
                        selected = selectedValue == item, // Check if this option is selected
                        onClick = {
                            selectedValue = item // Update selected value
                            onSelectionChanged(item) // Trigger callback with selected item
                        }
                    )
                    // Text label for the option
                    Text(item)
                }
            }
            // Divider between options and price label
            Divider(
                thickness = dimensionResource(R.dimen.thickness_divider), // Set thickness of the divider
                modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_medium)) // Padding below the divider
            )
            // Display the formatted price label
            FormattedPriceLabel(
                subtotal = subtotal, // Pass subtotal to formatted price label
                modifier = Modifier
                    .align(Alignment.End) // Align the price label to the end of the column
                    .padding(
                        top = dimensionResource(R.dimen.padding_medium), // Padding above the price label
                        bottom = dimensionResource(R.dimen.padding_medium) // Padding below the price label
                    )
            )
        }
        // Row for buttons (Cancel and Next)
        Row(
            modifier = Modifier
                .fillMaxWidth() // Fill available width
                .padding(dimensionResource(R.dimen.padding_medium)), // Padding around the row
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)), // Spacing between buttons
            verticalAlignment = Alignment.Bottom // Align buttons to the bottom of the row
        ) {
            // Cancel button
            OutlinedButton(
                modifier = Modifier.weight(1f), // Equal weight for buttons to share space
                onClick = onCancelButtonClicked // Trigger cancel callback
            ) {
                Text(stringResource(R.string.cancel)) // Display cancel text
            }
            // Next button
            Button(
                modifier = Modifier.weight(1f), // Equal weight for buttons to share space
                enabled = selectedValue.isNotEmpty(), // Enable only if a selection is made
                onClick = onNextButtonClicked // Trigger next callback
            ) {
                Text(stringResource(R.string.next)) // Display next text
            }
        }
    }
}

// Preview function to visualize the SelectOptionScreen
@Preview
@Composable
fun SelectOptionPreview() {
    CupcakeTheme { // Apply the theme for the preview
        SelectOptionScreen(
            subtotal = "299.99", // Example subtotal value
            options = listOf("Option 1", "Option 2", "Option 3", "Option 4"), // Example options
            modifier = Modifier.fillMaxHeight() // Fill the available height in the preview
        )
    }
}
