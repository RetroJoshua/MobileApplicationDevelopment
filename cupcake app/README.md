 ### Main Components of Navigation 
 1. **NavController** 
  - Primary component for navigation control 
  -  Handles movement between different screens/destinations 
  2. **NavGraph** 
   - Maps all composable destinations 
   - - Defines the navigation structure 
   3. **NavHost**
   - Container composable 
   -  Displays current destination from NavGraph 
   ### Routes in Compose Navigation 
   - Routes are like URLs for your app screens 
   -  Each route is a unique string identifier 
   - - Maps to specific composable destinations 
   -  Typically defined using enum classes in Kotlin 
   ### Cupcake App Example Routes 
   1.  **Start Screen** 
   - Purpose: Cupcake quantity selection 
   - Features: Three quantity buttons 
   2. **Flavor Screen** 
   - Purpose: Flavor selection 
   - Features: List of flavor choices 
   3. **Pickup Screen** 
   - Purpose: Delivery date selection 
   -  Features: List of available dates 
   4. **Summary Screen** 
   - Purpose: Order review 
   -  Features: Order details, send/cancel options 
   ### Key Points 
   - Routes are finite in number 
   -  Each route corresponds to a specific screen 
   -  Enum classes provide organized route management 
   -  Routes help in structured navigation flow

Add an enum class to define the routes.

1. In CupcakeScreen.kt, above the CupcakeAppBar composable, add an enum class named CupcakeScreen.

   Before declaring enum class need to declare `choose_flavor` , `choose_pickup_date`, and `order_summary` in strings.xml
   ```
   <resources>
     <string name="choose_flavor">Choose Flavor</string>
     <string name="choose_pickup_date">Choose Pickup Date</string>
     <string name="order_summary">Order Summary</string>
   </resources>
   ```
   and then add an enum class named CupcakeScreen
   
```kotlin
/**
 * enum values that represent the screens in the app
 */
enum class CupcakeScreen(@StringRes val title: Int) {
    Start(title = R.string.app_name),
    Flavor(title = R.string.choose_flavor),
    Pickup(title = R.string.choose_pickup_date),
    Summary(title = R.string.order_summary)
}
```

2. Add a NavHost to your app:
   There are two notable parameters:
   - `navController`: An instance of the `NavHostController` class. You can use this object to navigate between screens, for example, by calling the `navigate()` method to navigate to another destination. You can obtain the `NavHostController` by calling `rememberNavController()` from a composable function.
   - `startDestination`: A string route defining the destination shown by default when the app first displays the `NavHost`. In the case of the Cupcake app, this should be the `Start` route.
   -  Open `CupcakeScreen.kt` within the `Scaffold`, below the `uiState` variable, add a `NavHost` composable.
   -   Pass in the  `navController`  variable for the  `navController`  parameter and  `CupcakeScreen.Start.name`  for the  `startDestination`  parameter. Pass the modifier that was passed into  `CupcakeApp()`  for the modifier parameter. Pass in an empty trailing lambda for the final parameter.
   -   Within the content function of a  `NavHost`, you call the  `composable()`  function. The  `composable()`  function has two required parameters.

      -   **`route`****:**  A string corresponding to the name of a route. This can be any unique string. You'll use the name property of the  `CupcakeScreen`  enum's constants.
      -   **`content`****:**  Here you can call a composable that you want to display for the given route.

  You'll call the  `composable()`  function once for each of the four routes.

  -  Call the  `composable()`  function, passing in  `CupcakeScreen.Start.name`  for the  `route`.
  -   Within the trailing lambda, call the  `StartOrderScreen`  composable, passing in  `quantityOptions`  for the  `quantityOptions`  property. For the  `modifier`  pass in  `Modifier.fillMaxSize().padding(dimensionResource(R.dimen.padding_medium)).

Before we start we need to insert few values in stings.xml:
```
<resources>
     <string name="one_cupcake">One Cupcake</string>
    <string name="six_cupcakes">Six Cupcakes</string>
    <string name="twelve_cupcakes">Twelve Cupcakes</string>
   <string name="vanilla">Vanilla</string>
   <string name="chocolate">Chocolate</string>
   <string name="red_velvet">Red Velvet</string>
   <string name="salted_caramel">Salted Caramel</string>
   <string name="coffee">Coffee</string>  
</resources>
```
After this you can add DataSource to it.
```kotlin
import com.example.cupcake.R

object DataSource {
    val flavors = listOf(
        R.string.vanilla,
        R.string.chocolate,
        R.string.red_velvet,
        R.string.salted_caramel,
        R.string.coffee
    )

    val quantityOptions = listOf(
        Pair(R.string.one_cupcake, 1),
        Pair(R.string.six_cupcakes, 6),
        Pair(R.string.twelve_cupcakes, 12)
    )
}
```



  After creating datasource:

```kotlin
        NavHost(
    // NavController manages the navigation between screens
    navController = navController,
    // Defines which screen to show first using enum name
    startDestination = CupcakeScreen.Start.name,
    // Styling for the NavHost container
    modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(innerPadding)
) {
    // First destination - Start Screen
    composable(route = CupcakeScreen.Start.name) {
        StartOrderScreen(
            // List of quantity options from data source
            quantityOptions = DataSource.quantityOptions,
            // Navigation logic when next button is clicked
            onNextButtonClicked = {
                viewModel.setQuantity(it)  // Update quantity in ViewModel
                navController.navigate(CupcakeScreen.Flavor.name)  // Navigate to flavor screen
            },
            // Screen styling
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.padding_medium))
        )
    }

    // Second destination - Flavor Screen
    composable(route = CupcakeScreen.Flavor.name) {
        // Get current context for string resources
        val context = LocalContext.current
        SelectOptionScreen(
            subtotal = uiState.price,  // Display current price
            // Navigate to pickup screen
            onNextButtonClicked = { navController.navigate(CupcakeScreen.Pickup.name) },
            // Cancel order and return to start
            onCancelButtonClicked = {
                cancelOrderAndNavigateToStart(viewModel, navController)
            },
            // Convert flavor resource IDs to strings
            options = DataSource.flavors.map { id -> context.resources.getString(id) },
            // Update selected flavor in ViewModel
            onSelectionChanged = { viewModel.setFlavor(it) },
            modifier = Modifier.fillMaxHeight()
        )
    }

    // Third destination - Pickup Screen
    composable(route = CupcakeScreen.Pickup.name) {
        SelectOptionScreen(
            subtotal = uiState.price,  // Display current price
            // Navigate to summary screen
            onNextButtonClicked = { navController.navigate(CupcakeScreen.Summary.name) },
            // Cancel order and return to start
            onCancelButtonClicked = {
                cancelOrderAndNavigateToStart(viewModel, navController)
            },
            // List of available pickup dates
            options = uiState.pickupOptions,
            // Update selected date in ViewModel
            onSelectionChanged = { viewModel.setDate(it) },
            modifier = Modifier.fillMaxHeight()
        )
    }

    // Fourth destination - Summary Screen
    composable(route = CupcakeScreen.Summary.name) {
        val context = LocalContext.current
        OrderSummaryScreen(
            // Pass current order state
            orderUiState = uiState,
            // Cancel order and return to start
            onCancelButtonClicked = {
                cancelOrderAndNavigateToStart(viewModel, navController)
            },
            // Share order details
            onSendButtonClicked = { subject: String, summary: String ->
                shareOrder(context, subject = subject, summary = summary)
            },
            modifier = Modifier.fillMaxHeight()
        )
    }
}
```

### Traditional Approach (Not Recommended) of handling navigation in Compose
-   Passing NavController directly to composables
-   Each screen gets direct access to navigation control
-   Works but not ideal for app architecture

### Recommended Approach of handling navigation in Compose

1.  **Centralized Navigation**
    
    -   Keep navigation logic in one place
    -   Use NavHost as central navigation manager
    -   Avoid passing NavController to individual screens
2.  **Function-Based Navigation**
    
    -   Pass navigation functions to composables
    -   Screens call these functions when needed
    -   Maintains separation of concerns

### Benefits

1.  **Maintainability**
    
    -   Easier to maintain code
    -   Reduces potential navigation bugs
    -   Centralized control over navigation flow
2.  **Flexibility**
    
    -   Better support for different form factors
    -   Adaptable to various screen sizes
    -   Works well with foldables and tablets
3.  **Screen Independence**
    
    -   Screens remain self-contained
    -   No direct dependencies between screens
    -   Better component reusability

### Key Principles

-   Screens shouldn't know about other screens
-   Navigation logic stays in NavHost
-   Use function types for navigation actions
-   Keep UI components independent and reusable
