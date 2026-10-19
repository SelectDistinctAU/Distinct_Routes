package com.distinctroutes.app.ui.settings

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Accessible
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.distinctroutes.app.R
import com.distinctroutes.app.domain.model.BikeType
import com.distinctroutes.app.util.Constants

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    modifier: Modifier = Modifier
) {
    val selectedCity by viewModel.selectedCity.collectAsState()
    val themeMode by viewModel.themeMode.collectAsState()
    val colourMode by viewModel.colourMode.collectAsState()
    val preferences by viewModel.preferences.collectAsState()
    val cities by viewModel.availableCities.collectAsState()

    var cityDropdownExpanded by remember { mutableStateOf(false) }
    var bikeTypeDropdownExpanded by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header Branding Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.direct_route),
                contentDescription = "Distinct Routes logo",
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "App Settings & Defaults",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // ==========================================
        // CATEGORY 1: ROUTE & VEHICLE DEFAULTS
        // ==========================================
        Text(
            text = "1. Route & Vehicle Defaults",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        // Default BBBike City Selection Card
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Box {
                    OutlinedTextField(
                        value = selectedCity,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Default BBBike City") },
                        trailingIcon = {
                            IconButton(onClick = { cityDropdownExpanded = true }) {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = "Select default city")
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .semantics { contentDescription = "Default city selection: $selectedCity" }
                    )
                    DropdownMenu(
                        expanded = cityDropdownExpanded,
                        onDismissRequest = { cityDropdownExpanded = false }
                    ) {
                        cities.forEach { city ->
                            DropdownMenuItem(
                                text = { Text(city) },
                                onClick = {
                                    viewModel.updateDefaultCity(city)
                                    cityDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        // Default Vehicle & Mobility Device Selection Card
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { bikeTypeDropdownExpanded = true },
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
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
                            disabledBorderColor = MaterialTheme.colorScheme.outline,
                            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        label = { Text("Default Vehicle / Mobility Device") },
                        trailingIcon = {
                            Row(
                                modifier = Modifier.padding(end = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (preferences.isMobilityDevice()) Icons.AutoMirrored.Filled.Accessible else Icons.AutoMirrored.Filled.DirectionsBike,
                                    contentDescription = null
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Expand vehicle list"
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { bikeTypeDropdownExpanded = true }
                            .semantics { contentDescription = "Default vehicle type: ${preferences.bikeType.displayName}. Tap to change." }
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
            }
        }

        // ==========================================
        // CATEGORY 2: APPEARANCE & SAFETY DEFAULTS
        // ==========================================
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "2. Appearance & Safety Defaults",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        // Brand Color Palette Selector Card
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Brand Color Palette",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = colourMode == "blue",
                        onClick = { viewModel.updateColourMode("blue") },
                        label = { Text("Metallic Blue", maxLines = 1, style = MaterialTheme.typography.labelMedium) },
                        leadingIcon = { Icon(Icons.Default.Palette, contentDescription = null, modifier = Modifier.size(16.dp)) },
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = colourMode == "green",
                        onClick = { viewModel.updateColourMode("green") },
                        label = { Text("Emerald Green", maxLines = 1, style = MaterialTheme.typography.labelMedium) },
                        leadingIcon = { Icon(Icons.Default.Palette, contentDescription = null, modifier = Modifier.size(16.dp)) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // App Theme Mode Selector Card
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "App Theme Mode",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = themeMode == "light",
                        onClick = { viewModel.updateThemeMode("light") },
                        label = { Text("Light", maxLines = 1, style = MaterialTheme.typography.labelMedium) },
                        leadingIcon = { Icon(Icons.Default.LightMode, contentDescription = null, modifier = Modifier.size(16.dp)) },
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = themeMode == "dark",
                        onClick = { viewModel.updateThemeMode("dark") },
                        label = { Text("Dark", maxLines = 1, style = MaterialTheme.typography.labelMedium) },
                        leadingIcon = { Icon(Icons.Default.DarkMode, contentDescription = null, modifier = Modifier.size(16.dp)) },
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = themeMode == "system",
                        onClick = { viewModel.updateThemeMode("system") },
                        label = { Text("System", maxLines = 1, style = MaterialTheme.typography.labelMedium) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Default Safety & Riding Preferences Toggles Card
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Default Safety Preferences",
                    style = MaterialTheme.typography.titleSmall
                )

                SettingToggleRow(
                    label = "Prefer Green & Scenic Routes",
                    checked = preferences.preferGreenRoutes,
                    onCheckedChange = { viewModel.updateSafetyPreferences(preferGreenRoutes = it) },
                    contentDescription = "Toggle default prefer green routes"
                )

                SettingToggleRow(
                    label = "Avoid Unlit Streets (Night Safety)",
                    checked = preferences.avoidUnlitStreets,
                    onCheckedChange = { viewModel.updateSafetyPreferences(avoidUnlitStreets = it) },
                    contentDescription = "Toggle default avoid unlit streets"
                )

                SettingToggleRow(
                    label = "Avoid Cobblestones / Unpaved Surface",
                    checked = preferences.avoidCobblestones,
                    onCheckedChange = { viewModel.updateSafetyPreferences(avoidCobblestones = it) },
                    contentDescription = "Toggle default avoid cobblestones"
                )

                SettingToggleRow(
                    label = "Avoid Ferries",
                    checked = preferences.avoidFerries,
                    onCheckedChange = { viewModel.updateSafetyPreferences(avoidFerries = it) },
                    contentDescription = "Toggle default avoid ferries"
                )

                SettingToggleRow(
                    label = "Avoid Traffic Lights",
                    checked = preferences.avoidTrafficLights,
                    onCheckedChange = { viewModel.updateSafetyPreferences(avoidTrafficLights = it) },
                    contentDescription = "Toggle default avoid traffic lights"
                )

                SettingToggleRow(
                    label = "Avoid Highways",
                    checked = preferences.effectiveAvoidHighways(),
                    onCheckedChange = { viewModel.updateSafetyPreferences(avoidHighways = it) },
                    contentDescription = "Toggle default avoid highways"
                )

                SettingToggleRow(
                    label = "Avoid Steep Hills",
                    checked = preferences.effectiveAvoidSteepHills(),
                    onCheckedChange = { viewModel.updateSafetyPreferences(avoidSteepHills = it) },
                    contentDescription = "Toggle default avoid steep hills"
                )

                SettingToggleRow(
                    label = "Dog Trailer Mode (Weight Penalty)",
                    checked = preferences.hasDogTrailer,
                    onCheckedChange = { viewModel.updateSafetyPreferences(hasDogTrailer = it) },
                    contentDescription = "Toggle default dog trailer mode"
                )
            }
        }

        // ==========================================
        // CATEGORY 3: ABOUT & PRIVACY
        // ==========================================
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "3. About & Privacy",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        // Why Distinct Routes? / Our Mission Card
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Why Distinct Routes?",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Text(
                    text = "• Safety-First Pathfinding: Prioritizes quiet neighborhood backstreets, designated cycleways, and flat paths over busy arterial traffic.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "• Vehicle & Mobility Intelligence: Custom speed and slope rules tailored for wheelchairs, handbikes, mobility scooters, cargo bikes, and dog trailers.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "• Zero-Tracking Privacy: 100% open-source (GPLv3), ad-free, zero registration, and zero location telemetry.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        // Privacy Policy Card
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Privacy & Data Protection",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Distinct Routes is built on strict privacy-first principles. Your location, routes, and preferences remain 100% on your device.",
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedButton(
                    onClick = { showPrivacyDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Security, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("View Privacy Statement")
                }
            }
        }

        // About & Attributions Card
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "About & Attributions",
                    style = MaterialTheme.typography.titleSmall
                )
                Text("Version: ${Constants.VERSION_NAME} (Build ${Constants.VERSION_CODE})")
                Text("License: GNU General Public License v3.0 (GPLv3)")
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Attributions:",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
                Text("• BBBike.org — Safety-aware routing data engine")
                Text("• OpenStreetMap — Map data foundation")
                Text("• OsmAnd — Navigation handoff partner")
                Text("• Good Ware (Flaticon) — Location icons created by Good Ware")
                Text("• iconfield (Flaticon) — Spoke-wheel icons created by iconfield")
            }
        }

        // Safety Disclaimer Card
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = "Safety disclaimer: Sourced from BBBike. Cyclists remain responsible for assessing local conditions." }
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
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
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }

    if (showPrivacyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyDialog = false },
            title = { Text("Distinct Routes Privacy Statement") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("• Official Policy: https://github.com/SelectDistinctAU/Distinct_Routes/blob/main/02_Specifications/USER_PRIVACY_POLICY.md", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    Text("• Zero User Tracking: No user accounts, no registration, no location analytics, and no advertising IDs.", style = MaterialTheme.typography.bodySmall)
                    Text("• On-Device Storage: Saved routes and ride history are stored locally on your device in private application storage.", style = MaterialTheme.typography.bodySmall)
                    Text("• Open Community Data: Map tiles and routing services connect directly to open-data servers (BBBike.org & OpenStreetMap) with standard User-Agent headers.", style = MaterialTheme.typography.bodySmall)
                    Text("• Open Source Transparency: 100% free and open-source software under the GNU General Public License v3.0 (GPLv3).", style = MaterialTheme.typography.bodySmall)
                }
            },
            confirmButton = {
                TextButton(onClick = { showPrivacyDialog = false }) {
                    Text("I Understand")
                }
            }
        )
    }
}

@Composable
fun SettingToggleRow(
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