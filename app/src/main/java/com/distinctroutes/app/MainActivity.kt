package com.distinctroutes.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.distinctroutes.app.ui.favorites.FavoritesScreen
import com.distinctroutes.app.ui.favorites.FavoritesViewModel
import com.distinctroutes.app.ui.route_planner.RoutePlannerScreen
import com.distinctroutes.app.ui.route_planner.RoutePlannerViewModel
import com.distinctroutes.app.ui.settings.SettingsScreen
import com.distinctroutes.app.ui.settings.SettingsViewModel
import com.distinctroutes.app.ui.theme.DistinctRoutesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val routePlannerViewModel: RoutePlannerViewModel by viewModels()
    private val favoritesViewModel: FavoritesViewModel by viewModels()
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeMode by settingsViewModel.themeMode.collectAsState()
            val colourMode by settingsViewModel.colourMode.collectAsState()
            DistinctRoutesTheme(colourMode = colourMode, themeMode = themeMode) {
                DistinctRoutesMainContent(
                    routePlannerViewModel = routePlannerViewModel,
                    favoritesViewModel = favoritesViewModel,
                    settingsViewModel = settingsViewModel
                )
            }
        }
    }
}

@Composable
fun DistinctRoutesMainContent(
    routePlannerViewModel: RoutePlannerViewModel,
    favoritesViewModel: FavoritesViewModel,
    settingsViewModel: SettingsViewModel
) {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.PLANNER) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach { destination ->
                val isSelected = destination == currentDestination
                item(
                    icon = {
                        when (destination) {
                            AppDestinations.PLANNER -> {
                                Icon(
                                    painter = painterResource(R.drawable.route_24px),
                                    contentDescription = stringResource(R.string.title_waypoints),
                                    modifier = Modifier.size(24.dp),
                                    tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            AppDestinations.FAVORITES -> {
                                Icon(
                                    painter = painterResource(R.drawable.map_pin_heart_24px),
                                    contentDescription = stringResource(R.string.title_favourites),
                                    modifier = Modifier.size(24.dp),
                                    tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            AppDestinations.PROFILE -> {
                                Icon(
                                    painter = painterResource(R.drawable.settings_24px),
                                    contentDescription = stringResource(R.string.title_settings),
                                    modifier = Modifier.size(24.dp),
                                    tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    },
                    label = {
                        val labelText = when (destination) {
                            AppDestinations.PLANNER -> stringResource(R.string.title_waypoints)
                            AppDestinations.FAVORITES -> stringResource(R.string.title_favourites)
                            AppDestinations.PROFILE -> stringResource(R.string.title_settings)
                        }
                        Text(labelText)
                    },
                    selected = isSelected,
                    onClick = { currentDestination = destination }
                )
            }
        }
    ) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                // Keep RoutePlannerScreen composed in background so MapView stays alive without deadlock
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            alpha = if (currentDestination == AppDestinations.PLANNER) 1f else 0f
                        }
                ) {
                    RoutePlannerScreen(
                        viewModel = routePlannerViewModel,
                        favoritesViewModel = favoritesViewModel,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                if (currentDestination == AppDestinations.FAVORITES) {
                    FavoritesScreen(
                        viewModel = favoritesViewModel,
                        routePlannerViewModel = routePlannerViewModel,
                        onNavigateToPlanner = { currentDestination = AppDestinations.PLANNER },
                        modifier = Modifier.fillMaxSize()
                    )
                }

                if (currentDestination == AppDestinations.PROFILE) {
                    SettingsScreen(
                        viewModel = settingsViewModel,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

enum class AppDestinations(
    val labelKey: Int,
    val icon: Int,
) {
    PLANNER(R.string.title_waypoints, R.drawable.direct_route),
    FAVORITES(R.string.title_favourites, R.drawable.ic_favorite),
    PROFILE(R.string.title_settings, R.drawable.ic_account_box),
}