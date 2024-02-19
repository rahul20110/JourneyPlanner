## Journey App Implementation
The Journey App is a simple application implemented in Kotlin using Jetpack Compose for building the user interface. It allows users to plan their journey by inputting the distance traveled and the total distance. It then displays the stops along with the distance traveled, distance left, and the total distance covered.

### Components
1. **MainActivity**: This is the entry point of the application. It sets the content view to the `JourneyApp` composable function.

2. **Stop Data Class**: Represents a stop along the journey. It includes properties such as the stop name, distance from the previous stop, total distance covered until this stop, and the distance left to cover after this stop.

3. **JourneyApp Composable Function**:
   - Displays a UI for the journey planner.
   - It utilizes Jetpack Compose components such as `OutlinedTextField`, `Button`, `Text`, and `LazyColumn` for layout and user interaction.
   - Users can input the distance traveled and the total distance.
   - They can add stops along the journey, which will be displayed dynamically.
   - The app calculates and displays the progress, including the distance traveled, distance left, and total distance covered.
   - Users can switch between kilometers and miles.

### Key Composable Functions Used
- `Column`: Organizes UI elements vertically.
- `Row`: Organizes UI elements horizontally.
- `OutlinedTextField`: Text input field with an outlined border.
- `Button`: A clickable button.
- `Text`: Displays text.
- `LazyColumn`: A vertically scrolling list that only composes and lays out the currently visible items.

### State Management
- State is managed using `mutableStateOf` and `remember`.
- Mutable state variables are used to track the distance traveled, total distance, stops, and units (kilometers or miles).
- Changes to these variables trigger recomposition of the relevant UI elements.

### Logging
- Debugging information is logged using `Log.d`.
- Information such as distance traveled, distance left, and stop number is logged when adding a stop.

### User Interaction
- Users can input the distance traveled and the total distance.
- They can add stops to the journey.
- Progress, including distance traveled, distance left, and total distance covered, is dynamically updated and displayed.

### Unit Selection
- Users can switch between kilometers and miles for distance measurement.

