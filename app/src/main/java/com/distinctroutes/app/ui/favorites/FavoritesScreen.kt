package com.distinctroutes.app.ui.favorites

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.distinctroutes.app.R
import com.distinctroutes.app.domain.model.FavoriteRoute
import com.distinctroutes.app.ui.route_planner.RoutePlannerViewModel

@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel,
    routePlannerViewModel: RoutePlannerViewModel,
    onNavigateToPlanner: () -> Unit,
    modifier: Modifier = Modifier
) {
    val favorites by viewModel.favorites.collectAsState()
    val journalHistory by viewModel.journalHistory.collectAsState()
    var selectedTab by remember { mutableStateOf(0) } // 0 = Favourites, 1 = Journal History

    LaunchedEffect(Unit) {
        viewModel.loadData()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
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
                    text = if (selectedTab == 0) stringResource(R.string.title_favourites) else "Ride Journal History",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Dual Tab Filter Chips: Favourites vs Ride Journal History
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                label = { Text(stringResource(R.string.title_favourites)) },
                leadingIcon = { Icon(Icons.Default.Favorite, contentDescription = null, modifier = Modifier.size(18.dp)) }
            )
            FilterChip(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                label = { Text("Ride Journal (${journalHistory.size})") },
                leadingIcon = { Icon(Icons.Default.History, contentDescription = null, modifier = Modifier.size(18.dp)) }
            )
        }

        val activeList = if (selectedTab == 0) favorites else journalHistory

        if (activeList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .semantics {
                        contentDescription = if (selectedTab == 0) "No saved favourite routes yet" else "No ride history yet"
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (selectedTab == 0) stringResource(R.string.msg_no_favorites) else "No ride journal history yet. Calculated routes will automatically be logged here on-device.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(32.dp)
                )
            }
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                if (selectedTab == 1) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { viewModel.clearJournalHistory() }) {
                            Icon(Icons.Default.Delete, contentDescription = "Clear ride journal history")
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Clear Journal")
                        }
                    }
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(activeList, key = { it.id }) { route ->
                        FavoriteRouteCard(
                            route = route,
                            isJournalMode = selectedTab == 1,
                            onDelete = {
                                if (selectedTab == 0) viewModel.removeFavorite(route.id)
                            },
                            onLoadPlanner = {
                                routePlannerViewModel.startAddressText.value = route.startAddress
                                routePlannerViewModel.startLat = route.startLat
                                routePlannerViewModel.startLon = route.startLon
                                routePlannerViewModel.endAddressText.value = route.endAddress
                                routePlannerViewModel.endLat = route.endLat
                                routePlannerViewModel.endLon = route.endLon

                                if (route.hasViaPoint && route.viaLat != null && route.viaLon != null) {
                                    routePlannerViewModel.addViaStop(
                                        address = route.viaAddress ?: "",
                                        lat = route.viaLat,
                                        lon = route.viaLon
                                    )
                                }

                                routePlannerViewModel.calculateRoute()
                                onNavigateToPlanner()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FavoriteRouteCard(
    route: FavoriteRoute,
    isJournalMode: Boolean,
    onDelete: () -> Unit,
    onLoadPlanner: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        modifier = Modifier
            .fillMaxWidth()
            .semantics {
                contentDescription = "Route ${route.title}, distance ${route.distanceKm} km, duration ${route.durationMinutes} minutes."
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = route.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                if (!isJournalMode) {
                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(R.string.btn_remove_favorite),
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text("From: ${route.startAddress}", style = MaterialTheme.typography.bodyMedium)
            if (route.hasViaPoint && !route.viaAddress.isNullOrEmpty()) {
                Text("Via: ${route.viaAddress}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
            }
            Text("To: ${route.endAddress}", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Distance: ${"%.2f".format(route.distanceKm)} km", style = MaterialTheme.typography.bodySmall)
                Text("Est: ${route.durationMinutes} mins", style = MaterialTheme.typography.bodySmall)
                Text("Profile: ${route.bikeTypeDisplayName}", style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onLoadPlanner,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Map, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(stringResource(R.string.btn_load_planner))
                }
            }
        }
    }
}