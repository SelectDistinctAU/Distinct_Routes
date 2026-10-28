package com.distinctroutes.app.ui.route_planner

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Accessible
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.distinctroutes.app.R
import com.distinctroutes.app.domain.model.BikeType
import com.distinctroutes.app.domain.model.FavoriteRoute
import com.distinctroutes.app.ui.favorites.FavoritesViewModel
import com.distinctroutes.app.ui.theme.CardShape
import com.distinctroutes.app.ui.theme.PillShape
import kotlinx.coroutines.delay

@Composable
fun RoutePlannerScreen(
    viewModel: RoutePlannerViewModel,
    favoritesViewModel: FavoritesViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val preferences by viewModel.preferences.collectAsState()
    val isPreferencesExpanded by viewModel.isPreferencesExpanded.collectAsState()

    val startAddress by viewModel.startAddressText.collectAsState()
    val endAddress by viewModel.endAddressText.collectAsState()
    val viaStops by viewModel.viaStops.collectAsState()

    val startSuggestions by viewModel.startSuggestions.collectAsState()
    val endSuggestions by viewModel.endSuggestions.collectAsState()
    val viaSuggestions by viewModel.viaSuggestions.collectAsState()

    var bikeTypeDropdownExpanded by remember { mutableStateOf(false) }
    var isBannerVisible by remember { mutableStateOf(true) }
    var showSavedSnackbar by remember { mutableStateOf(false) }
    var isTopCardCollapsed by remember { mutableStateOf(false) }

    // Auto-collapse search inputs card when route calculation succeeds
    LaunchedEffect(uiState) {
        if (uiState is RoutePlannerUiState.Success) {
            isTopCardCollapsed = true
        }
    }

    // Fade out safety disclaimer automatically after 4 seconds on launch
    LaunchedEffect(Unit) {
        delay(4000)
        isBannerVisible = false
    }

    // Auto-hide "Saved to Favourites!" snackbar after 3.5 seconds
    LaunchedEffect(showSavedSnackbar) {
        if (showSavedSnackbar) {
            delay(3500)
            showSavedSnackbar = false
        }
    }

    // Show disclaimer automatically while calculating/loading route or if manually toggled
    val showBanner = isBannerVisible || (uiState is RoutePlannerUiState.Loading)

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.useCurrentGpsLocation()
        } else {
            Toast.makeText(context, "Location permission denied; manual address entry required.", Toast.LENGTH_SHORT).show()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        // ==========================================
        // 100% FULL-SCREEN MAP CANVAS BACKGROUND
        // ==========================================
        MapViewContainer(
            startLat = viewModel.startLat,
            startLon = viewModel.startLon,
            endLat = viewModel.endLat,
            endLon = viewModel.endLon,
            viaWaypoints = viaStops.map { Pair(it.lat, it.lon) },
            routePoints = (uiState as? RoutePlannerUiState.Success)?.route?.polylinePoints ?: emptyList(),
            onStartPinMoved = { lat, lon -> viewModel.onStartPinMoved(lat, lon) },
            onEndPinMoved = { lat, lon -> viewModel.onEndPinMoved(lat, lon) },
            onViaPinMoved = { idx, lat, lon -> viewModel.onViaPinMoved(idx, lat, lon) },
            modifier = Modifier.fillMaxSize()
        )

        // ==========================================
        // TOP FLOATING OVERLAY CARD: Inputs & Preferences
        // ==========================================
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (isTopCardCollapsed && (uiState is RoutePlannerUiState.Success)) {
                // COLLAPSED COMPACT SUMMARY BAR (Maximizes Map View to 85%+)
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)),
                    shape = CardShape,
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isTopCardCollapsed = false }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Image(
                                painter = painterResource(R.drawable.direct_route),
                                contentDescription = "Distinct Routes logo",
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (startAddress.isNotBlank() && endAddress.isNotBlank()) "${startAddress.take(16)} → ${endAddress.take(16)}" else "Calculated Route",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "${preferences.bikeType.displayName} • Tap to edit inputs",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary,
                                    maxLines = 1
                                )
                            }
                        }
                        IconButton(onClick = { isTopCardCollapsed = false }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Expand and edit route search inputs",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            } else {
                // FULL EXPANDED INPUT CONTROLS CARD
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)),
                    shape = CardShape,
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 520.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(12.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Header Logo Bar with Collapse Button if Success
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painter = painterResource(R.drawable.direct_route),
                                    contentDescription = "Distinct Routes logo",
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = stringResource(R.string.app_name),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            if (uiState is RoutePlannerUiState.Success) {
                                IconButton(onClick = { isTopCardCollapsed = true }) {
                                    Icon(
                                        imageVector = Icons.Default.ExpandLess,
                                        contentDescription = "Minimize inputs and expand map view",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }

                        // FIRST CHOICE: Vehicle / Mobility Device Selector
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { bikeTypeDropdownExpanded = true }
                        ) {
                            OutlinedTextField(
                                value = preferences.bikeType.displayName,
                                onValueChange = {},
                                readOnly = true,
                                enabled = false,
                                colors = OutlinedTextFieldDefaults.colors(
                                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                                    disabledBorderColor = MaterialTheme.colorScheme.primary,
                                    disabledLabelColor = MaterialTheme.colorScheme.primary
                                ),
                                label = { Text("Selected Vehicle / Mobility Device", fontWeight = FontWeight.Bold) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = if (preferences.isMobilityDevice()) Icons.AutoMirrored.Filled.Accessible else Icons.AutoMirrored.Filled.DirectionsBike,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                },
                                trailingIcon = {
                                    Row(
                                        modifier = Modifier.padding(end = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ArrowDropDown,
                                            contentDescription = "Expand vehicle choices list",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { bikeTypeDropdownExpanded = true }
                                    .semantics { contentDescription = "Vehicle selection: ${preferences.bikeType.displayName}. Tap to change profile." }
                            )
                            DropdownMenu(
                                expanded = bikeTypeDropdownExpanded,
                                onDismissRequest = { bikeTypeDropdownExpanded = false }
                            ) {
                                BikeType.entries.forEach { bikeType ->
                                    DropdownMenuItem(
                                        text = { Text(bikeType.displayName) },
                                        onClick = {
                                            viewModel.updateSafetyPreferences(bikeType = bikeType)
                                            bikeTypeDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        // START Address Input with Explicit Search & Pin Drop Buttons
                        Box {
                            OutlinedTextField(
                                value = startAddress,
                                onValueChange = {
                                    viewModel.startAddressText.value = it
                                    viewModel.searchAddress(it, field = "start")
                                },
                                label = { Text("Start Address or Place") },
                                leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = "Start Flag A icon") },
                                trailingIcon = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        IconButton(
                                            onClick = { viewModel.searchAddress(startAddress, field = "start") }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Search,
                                                contentDescription = "Search start address",
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                        IconButton(
                                            onClick = { isTopCardCollapsed = true }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.PinDrop,
                                                contentDescription = "Drop start pin on map",
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                        IconButton(
                                            onClick = {
                                                if (viewModel.locationHelper.hasLocationPermission()) {
                                                    viewModel.useCurrentGpsLocation()
                                                } else {
                                                    permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                                                }
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.MyLocation,
                                                contentDescription = "Use current GPS location",
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .semantics { contentDescription = "Start address input. Current value: $startAddress" }
                            )

                            DropdownMenu(
                                expanded = startSuggestions.isNotEmpty(),
                                onDismissRequest = { viewModel.startSuggestions.value = emptyList() }
                            ) {
                                startSuggestions.forEach { location ->
                                    DropdownMenuItem(
                                        text = { Text(location.displayName) },
                                        onClick = { viewModel.selectAddressSuggestion(location, field = "start") }
                                    )
                                }
                            }
                        }

                        // Multi-Stop VIA Waypoints
                        viaStops.forEachIndexed { index, stop ->
                            Box {
                                OutlinedTextField(
                                    value = stop.addressText,
                                    onValueChange = { newAddr ->
                                        stop.addressText = newAddr
                                        viewModel.searchAddress(newAddr, field = stop.id)
                                    },
                                    label = { Text("Via Stop #${index + 1} Address") },
                                    leadingIcon = { Icon(Icons.Default.Place, contentDescription = "Yellow Via Stop Dot icon") },
                                    trailingIcon = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            IconButton(
                                                onClick = { viewModel.searchAddress(stop.addressText, field = stop.id) }
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Search,
                                                    contentDescription = "Search via stop address",
                                                    tint = MaterialTheme.colorScheme.primary
                                                )
                                            }
                                            if (index > 0) {
                                                IconButton(onClick = { viewModel.moveViaStopUp(index) }) {
                                                    Icon(
                                                        imageVector = Icons.Default.ArrowUpward,
                                                        contentDescription = "Move Via stop #${index + 1} up",
                                                        tint = MaterialTheme.colorScheme.primary
                                                    )
                                                }
                                            }
                                            if (index < viaStops.size - 1) {
                                                IconButton(onClick = { viewModel.moveViaStopDown(index) }) {
                                                    Icon(
                                                        imageVector = Icons.Default.ArrowDownward,
                                                        contentDescription = "Move Via stop #${index + 1} down",
                                                        tint = MaterialTheme.colorScheme.primary
                                                    )
                                                }
                                            }
                                            IconButton(onClick = { viewModel.removeViaStop(stop.id) }) {
                                                Icon(
                                                    imageVector = Icons.Default.Close,
                                                    contentDescription = "Remove Via stop #${index + 1}",
                                                    tint = MaterialTheme.colorScheme.error
                                                )
                                            }
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                DropdownMenu(
                                    expanded = viaSuggestions.isNotEmpty(),
                                    onDismissRequest = { viewModel.viaSuggestions.value = emptyList() }
                                ) {
                                    viaSuggestions.forEach { location ->
                                        DropdownMenuItem(
                                            text = { Text(location.displayName) },
                                            onClick = { viewModel.selectAddressSuggestion(location, field = stop.id) }
                                        )
                                    }
                                }
                            }
                        }

                        // Add Stop OutlinedButton
                        OutlinedButton(
                            onClick = { viewModel.addViaStop() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .semantics { contentDescription = "Add mid-point via stop button" }
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (viaStops.isEmpty()) stringResource(R.string.btn_add_via_stop) else "Add Another Stop (Trail/Loop)",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        // DESTINATION Address Input with Explicit Search & Pin Drop Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                OutlinedTextField(
                                    value = endAddress,
                                    onValueChange = {
                                        viewModel.endAddressText.value = it
                                        viewModel.searchAddress(it, field = "end")
                                    },
                                    label = { Text("Destination Address or Place") },
                                    leadingIcon = { Icon(Icons.Default.Flag, contentDescription = "Destination Flag B icon") },
                                    trailingIcon = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            IconButton(
                                                onClick = { viewModel.searchAddress(endAddress, field = "end") }
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Search,
                                                    contentDescription = "Search destination address",
                                                    tint = MaterialTheme.colorScheme.primary
                                                )
                                            }
                                            IconButton(
                                                onClick = { isTopCardCollapsed = true }
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.PinDrop,
                                                    contentDescription = "Drop destination pin on map",
                                                    tint = MaterialTheme.colorScheme.primary
                                                )
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .semantics { contentDescription = "Destination address input. Current value: $endAddress" }
                                )

                                DropdownMenu(
                                    expanded = endSuggestions.isNotEmpty(),
                                    onDismissRequest = { viewModel.endSuggestions.value = emptyList() }
                                ) {
                                    endSuggestions.forEach { location ->
                                        DropdownMenuItem(
                                            text = { Text(location.displayName) },
                                            onClick = { viewModel.selectAddressSuggestion(location, field = "end") }
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.width(4.dp))
                            IconButton(
                                onClick = { viewModel.swapStartAndEnd() },
                                modifier = Modifier.semantics { contentDescription = "Swap Start and Destination addresses" }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SwapVert,
                                    contentDescription = "Swap Start and Destination",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        // Explicit "Calculate Route Now" Button on Address Card
                        Button(
                            onClick = { viewModel.calculateRoute() },
                            shape = PillShape,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .semantics { contentDescription = "Calculate route now button" }
                        ) {
                            Icon(Icons.Default.Navigation, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Calculate Route Now ➔",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Collapsible Safety Preferences
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
                            modifier = Modifier
                                .fillMaxWidth()
                                .semantics {
                                    contentDescription = "Safety and Riding Preferences accordion card. Tap to ${if (isPreferencesExpanded) "collapse" else "expand"}."
                                },
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { viewModel.togglePreferencesExpanded() },
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = stringResource(R.string.title_safety_preferences),
                                            style = MaterialTheme.typography.titleSmall
                                        )
                                        val activeSummary = buildList {
                                            if (preferences.preferResidentialRoads == "N1") add("Quiet Streets")
                                            if (preferences.hasDogTrailer) add("Dog Trailer")
                                            if (preferences.preferGreenRoutes) add("Green Routes")
                                            if (preferences.avoidUnlitStreets) add("Night Safety")
                                            if (preferences.avoidCobblestones) add("Smooth Surface")
                                            if (preferences.avoidHighways) add("Avoids Highways")
                                            if (preferences.effectiveAvoidSteepHills()) add("Avoids Hills")
                                        }.joinToString(" • ").ifEmpty { "Default Safety Settings" }

                                        Text(
                                            text = activeSummary,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                    Icon(
                                        imageVector = if (isPreferencesExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                        contentDescription = if (isPreferencesExpanded) "Collapse preferences" else "Expand preferences"
                                    )
                                }

                                AnimatedVisibility(visible = isPreferencesExpanded) {
                                    Column(
                                        modifier = Modifier.padding(top = 8.dp),
                                        verticalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(
                                            text = "Scroll for more safety options ↓",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.padding(bottom = 2.dp)
                                        )

                                        SafetyToggleRow(
                                            label = "Prefer Residential Roads / Quiet Streets",
                                            checked = preferences.preferResidentialRoads == "N1",
                                            onCheckedChange = { viewModel.updateSafetyPreferences(preferResidentialRoads = if (it) "N1" else "") },
                                            contentDescription = "Toggle prefer residential roads"
                                        )

                                        SafetyToggleRow(
                                            label = "Prefer Green & Scenic Routes",
                                            checked = preferences.preferGreenRoutes,
                                            onCheckedChange = { viewModel.updateSafetyPreferences(preferGreenRoutes = it) },
                                            contentDescription = "Toggle prefer green routes"
                                        )

                                        SafetyToggleRow(
                                            label = "Avoid Unlit Streets (Night Safety)",
                                            checked = preferences.avoidUnlitStreets,
                                            onCheckedChange = { viewModel.updateSafetyPreferences(avoidUnlitStreets = it) },
                                            contentDescription = "Toggle avoid unlit streets"
                                        )

                                        SafetyToggleRow(
                                            label = "Avoid Cobblestones / Unpaved Surface",
                                            checked = preferences.avoidCobblestones,
                                            onCheckedChange = { viewModel.updateSafetyPreferences(avoidCobblestones = it) },
                                            contentDescription = "Toggle avoid cobblestones"
                                        )

                                        SafetyToggleRow(
                                            label = "Use Ferries",
                                            checked = preferences.useFerries,
                                            onCheckedChange = { viewModel.updateSafetyPreferences(useFerries = it) },
                                            contentDescription = "Toggle default use ferries"
                                        )

                                        SafetyToggleRow(
                                            label = "Avoid Traffic Lights",
                                            checked = preferences.avoidTrafficLights,
                                            onCheckedChange = { viewModel.updateSafetyPreferences(avoidTrafficLights = it) },
                                            contentDescription = "Toggle avoid traffic lights"
                                        )

                                        SafetyToggleRow(
                                            label = "Avoid Highways",
                                            checked = preferences.effectiveAvoidHighways(),
                                            onCheckedChange = { viewModel.updateSafetyPreferences(avoidHighways = it) },
                                            contentDescription = "Toggle avoid highways"
                                        )

                                        SafetyToggleRow(
                                            label = "Avoid Steep Hills",
                                            checked = preferences.effectiveAvoidSteepHills(),
                                            onCheckedChange = { viewModel.updateSafetyPreferences(avoidSteepHills = it) },
                                            contentDescription = "Toggle avoid steep hills"
                                        )

                                        SafetyToggleRow(
                                            label = "Dog Trailer Mode (Weight Penalty)",
                                            checked = preferences.hasDogTrailer,
                                            onCheckedChange = { viewModel.updateSafetyPreferences(hasDogTrailer = it) },
                                            contentDescription = "Toggle dog trailer mode weight penalty"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Disclaimer Banner
            AnimatedVisibility(
                visible = showBanner && !isTopCardCollapsed,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isBannerVisible = false }
                        .semantics {
                            contentDescription = "Safety disclaimer: Routing data sourced from BBBike. Safety-focused preferences available. Cyclists must assess local conditions. Tap to dismiss."
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.disclaimer_safety_banner),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // ==========================================
        // BOTTOM FLOATING OVERLAY CARD: CTA & Results
        // ==========================================
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (uiState !is RoutePlannerUiState.Success) {
                // Prominent M3 Expressive Pill-Shaped Calculate Route Button (shown before calculation)
                Button(
                    onClick = { viewModel.calculateRoute() },
                    shape = PillShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .semantics { contentDescription = "Calculate safety-focused route button" }
                ) {
                    Text(
                        text = stringResource(R.string.btn_calculate_route),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Result Card
            when (uiState) {
                is RoutePlannerUiState.Idle -> {}
                is RoutePlannerUiState.Loading -> {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)),
                        shape = CardShape,
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(
                                modifier = Modifier.semantics { contentDescription = "Calculating route, please wait" }
                            )
                        }
                    }
                }
                is RoutePlannerUiState.Success -> {
                    val route = (uiState as RoutePlannerUiState.Success).route
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.95f)),
                        shape = CardShape,
                        modifier = Modifier
                            .fillMaxWidth()
                            .semantics { contentDescription = "Route calculated successfully. Distance ${route.distanceKm} kilometers, duration ${route.durationMinutes} minutes." },
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = route.name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                                Text("${"%.2f".format(route.distanceKm)} km • ${route.durationMinutes} mins", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("Safety: ${route.safetySummary}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary, maxLines = 1, overflow = TextOverflow.Ellipsis)

                            if (!route.requestUrl.isNullOrBlank()) {
                                var showInspector by remember { mutableStateOf(false) }
                                TextButton(
                                    onClick = { showInspector = !showInspector },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(Icons.Default.Info, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (showInspector) "Hide BBBike API Request URL" else "Inspect BBBike API Request URL 🔍",
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }

                                AnimatedVisibility(visible = showInspector) {
                                    Card(
                                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
                                        modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                                    ) {
                                        Column(modifier = Modifier.padding(10.dp)) {
                                            Text(
                                                text = "Outgoing BBBike API v0.2 GET URL:",
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = route.requestUrl,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.primary,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Button(
                                    onClick = {
                                        val fav = FavoriteRoute(
                                            id = "${viewModel.startAddressText.value}|${viewModel.endAddressText.value}",
                                            title = "${viewModel.startAddressText.value.take(15)} → ${viewModel.endAddressText.value.take(15)}",
                                            startAddress = viewModel.startAddressText.value,
                                            startLat = viewModel.startLat,
                                            startLon = viewModel.startLon,
                                            endAddress = viewModel.endAddressText.value,
                                            endLat = viewModel.endLat,
                                            endLon = viewModel.endLon,
                                            viaAddress = if (viaStops.isNotEmpty()) viaStops.first().addressText else null,
                                            viaLat = if (viaStops.isNotEmpty()) viaStops.first().lat else null,
                                            viaLon = if (viaStops.isNotEmpty()) viaStops.first().lon else null,
                                            hasViaPoint = viaStops.isNotEmpty(),
                                            distanceKm = route.distanceKm,
                                            durationMinutes = route.durationMinutes,
                                            bikeTypeDisplayName = preferences.bikeType.displayName,
                                            safetySummary = route.safetySummary
                                        )
                                        favoritesViewModel.saveFavorite(fav)
                                        showSavedSnackbar = true
                                    },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(Icons.Default.Favorite, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = stringResource(R.string.btn_save_favorite),
                                        maxLines = 1,
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                }

                                OutlinedButton(
                                    onClick = {
                                        val shareIntent = viewModel.getNavigationShareIntent(route)
                                        try {
                                            context.startActivity(shareIntent)
                                        } catch (e: Exception) {
                                            Toast.makeText(context, "Exported GPX file to cache.", Toast.LENGTH_LONG).show()
                                        }
                                    },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Share",
                                        maxLines = 1,
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                }

                                Button(
                                    onClick = {
                                        val navIntent = viewModel.getOsmAndHandoffIntent(route)
                                        try {
                                            context.startActivity(navIntent)
                                        } catch (e: Exception) {
                                            Toast.makeText(context, "Exported GPX file to cache.", Toast.LENGTH_LONG).show()
                                        }
                                    },
                                    modifier = Modifier.weight(1.2f)
                                ) {
                                    Icon(Icons.Default.Navigation, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Navigate",
                                        maxLines = 1,
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                }
                            }
                        }
                    }
                }
                is RoutePlannerUiState.Error -> {
                    val errorMsg = (uiState as RoutePlannerUiState.Error).message
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                        shape = CardShape,
                        modifier = Modifier
                            .fillMaxWidth()
                            .semantics { contentDescription = "Error calculating route: $errorMsg" }
                    ) {
                        Text(
                            text = "Error: $errorMsg",
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }
        }

        // Floating Material 3 Pill Snackbar Overlay
        AnimatedVisibility(
            visible = showSavedSnackbar,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically(),
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 16.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                shape = PillShape,
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showSavedSnackbar = false }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.map_pin_heart_24px),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Saved to Favourites!",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}

@Composable
fun SafetyToggleRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    contentDescription: String = ""
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .semantics {
                this.contentDescription = contentDescription.ifEmpty {
                    "$label toggle, currently ${if (checked) "enabled" else "disabled"}"
                }
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}