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
package com.example.cupcake

import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cupcake.data.DataSource
import com.example.cupcake.ui.OrderSummaryScreen
import com.example.cupcake.ui.OrderViewModel
import com.example.cupcake.ui.SelectOptionScreen
import com.example.cupcake.ui.StartOrderScreen


// Enum class to represent the different screens in the cupcake app.
// Each screen has an associated title resource.
enum class CupcakeScreen(@StringRes val title: Int) {
    Start(title = R.string.app_name), // The starting screen with the app name.
    Flavor(title = R.string.choose_flavor), // Screen to choose a flavor.
    Pickup(title = R.string.choose_pickup_date), // Screen to select a pickup date.
    Summary(title = R.string.order_summary) // Screen to view the order summary.
}

/**
 * Composable function to display the app's top bar with navigation options.
 * @param currentScreen The current screen being displayed.
 * @param canNavigateBack Boolean indicating if back navigation is possible.
 * @param navigateUp Function to call for back navigation.
 * @param modifier Modifier to customize the layout appearance.
 */
@Composable
fun CupcakeAppBar(
    currentScreen: CupcakeScreen, // The current screen passed as a parameter.
    canNavigateBack: Boolean, // Indicates if we can navigate back.
    navigateUp: () -> Unit, // Function to navigate back.
    modifier: Modifier = Modifier // Default modifier to allow customization.
) {
    // Create a TopAppBar component for the app's header.
    TopAppBar(
        title = { Text(stringResource(id = currentScreen.title)) }, // Display the title based on the current screen.
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer // Set the background color.
        ),
        modifier = modifier, // Apply the modifier for styling.
        navigationIcon = {
            // Show a back button only if navigation is possible.
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) { // Back button on click navigates up.
                    Icon(
                        imageVector = Icons.Filled.ArrowBack, // Use the back arrow icon.
                        contentDescription = stringResource(R.string.back_button) // Accessibility description for the icon.
                    )
                }
            }
        }
    )
}

/**
 * Main composable function that sets up the cupcake ordering app.
 * @param viewModel The ViewModel for managing the app's state.
 * @param navController The NavHostController to manage navigation between screens.
 */
@Composable
fun CupcakeApp(
    viewModel: OrderViewModel = viewModel(), // Create or get the existing OrderViewModel.
    navController: NavHostController = rememberNavController() // Initialize NavHostController for navigation.
) {
    // Get the current back stack entry from the NavController.
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Determine the current screen based on the navigation route or default to Start.
    val currentScreen = CupcakeScreen.valueOf(
        backStackEntry?.destination?.route ?: CupcakeScreen.Start.name // Safe call to get the current screen.
    )

    // Scaffold provides the basic structure for the app's layout.
    Scaffold(
        topBar = {
            // Set up the app bar with the current screen's title and navigation options.
            CupcakeAppBar(
                currentScreen = currentScreen, // Pass the current screen.
                canNavigateBack = navController.previousBackStackEntry != null, // Check if there's a previous screen to navigate back to.
                navigateUp = { navController.navigateUp() } // Define the back navigation action.
            )
        }
    ) { innerPadding -> // The inner padding allows for proper spacing with the top bar.
        // Collect the UI state from the ViewModel.
        val uiState by viewModel.uiState.collectAsState()
        // Set up the navigation host for the app.
        NavHost(
            navController = navController, // Pass the NavController for managing navigation.
            startDestination = CupcakeScreen.Start.name, // Set the initial screen to Start.
            modifier = Modifier
                .fillMaxSize() // Fill the available space.
                .verticalScroll(rememberScrollState()) // Enable vertical scrolling.
                .padding(innerPadding) // Apply padding to avoid overlaps with the top bar.
        ) {
            // Define the different screens available in the app.
            composable(route = CupcakeScreen.Start.name) { // Start screen.
                StartOrderScreen(
                    quantityOptions = DataSource.quantityOptions, // Pass quantity options from the data source.
                    onNextButtonClicked = { // Handle the next button click action.
                        viewModel.setQuantity(it) // Set the selected quantity in the ViewModel.
                        navController.navigate(CupcakeScreen.Flavor.name) // Navigate to the Flavor screen.
                    },
                    modifier = Modifier
                        .fillMaxSize() // Fill the available space in the screen.
                        .padding(dimensionResource(id = R.dimen.padding_medium)) // Apply medium padding.
                )
            }
            composable(route = CupcakeScreen.Flavor.name) { // Flavor selection screen.
                val context = LocalContext.current // Get the current context.
                SelectOptionScreen(
                    subtotal = uiState.price, // Pass the current price subtotal from the UI state.
                    onNextButtonClicked = { navController.navigate(CupcakeScreen.Pickup.name) }, // Navigate to Pickup screen.
                    onCancelButtonClicked = { // Handle cancel action.
                        cancelOrderAndNavigateToStart(viewModel, navController) // Cancel the order and navigate back to Start.
                    },
                    options = DataSource.flavors.map { id -> context.resources.getString(id) }, // Map flavor resources to string options.
                    onSelectionChanged = { viewModel.setFlavor(it) }, // Update the selected flavor in the ViewModel.
                    modifier = Modifier.fillMaxHeight() // Fill the height of the screen.
                )
            }
            composable(route = CupcakeScreen.Pickup.name) { // Pickup date selection screen.
                SelectOptionScreen(
                    subtotal = uiState.price, // Pass the current price subtotal from the UI state.
                    onNextButtonClicked = { navController.navigate(CupcakeScreen.Summary.name) }, // Navigate to Summary screen.
                    onCancelButtonClicked = { // Handle cancel action.
                        cancelOrderAndNavigateToStart(viewModel, navController) // Cancel the order and navigate back to Start.
                    },
                    options = uiState.pickupOptions, // Pass pickup options from the UI state.
                    onSelectionChanged = { viewModel.setDate(it) }, // Update the selected date in the ViewModel.
                    modifier = Modifier.fillMaxHeight() // Fill the height of the screen.
                )
            }
            composable(route = CupcakeScreen.Summary.name) { // Order summary screen.
                val context = LocalContext.current // Get the current context.
                OrderSummaryScreen(
                    orderUiState = uiState, // Pass the current order UI state.
                    onCancelButtonClicked = { // Handle cancel action.
                        cancelOrderAndNavigateToStart(viewModel, navController) // Cancel the order and navigate back to Start.
                    },
                    onSendButtonClicked = { subject: String, summary: String -> // Handle send button click.
                        sharedOrder(context, subject = subject, summary = summary) // Share the order details.
                    },
                    modifier = Modifier.fillMaxHeight() // Fill the height of the screen.
                )
            }
        }
    }
}

/**
 * Function to cancel the current order and navigate back to the Start screen.
 * @param viewModel The ViewModel managing the order state.
 * @param navController The NavHostController for navigation.
 */
private fun cancelOrderAndNavigateToStart(
    viewModel: OrderViewModel,
    navController: NavHostController
) {
    viewModel.resetOrder() // Reset the order in the ViewModel.
    navController.popBackStack(CupcakeScreen.Start.name, inclusive = false) // Navigate back to the Start screen.
}

/**
 * Function to share the order details via an implicit intent.
 * @param context The context from which to start the sharing action.
 * @param subject The subject for the sharing intent.
 * @param summary The summary text for the sharing intent.
 */
private fun sharedOrder(context: Context, subject: String, summary: String) {
    // Create an ACTION_SEND implicit intent to share the order details.
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain" // Specify the type of data being shared.
        putExtra(Intent.EXTRA_SUBJECT, subject) // Add the subject to the intent.
        putExtra(Intent.EXTRA_TEXT, summary) // Add the summary text to the intent.
    }
    context.startActivity(
        Intent.createChooser(
            intent, // The intent to be shared.
            context.getString(R.string.new_cupcake_order) // Title for the chooser dialog.
        )
    )
}
