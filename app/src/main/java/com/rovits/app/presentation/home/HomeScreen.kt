package com.rovits.app.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rovits.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onLogout: () -> Unit,
    onNavigateToLanguage: () -> Unit
) {
    val logoutCompleted by viewModel.logoutCompleted.collectAsStateWithLifecycle()
    val testResult by viewModel.testResult.collectAsStateWithLifecycle()

    LaunchedEffect(logoutCompleted) {
        if (logoutCompleted) {
            onLogout()
            viewModel.onLogoutCompleted()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("API Endpoint Test") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Welcome Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.welcome_message),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "7 Endpoint'i test edebilirsiniz",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            HorizontalDivider()

            // Places API Section
            Text(
                text = "📍 Places API (/api/places)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            EndpointTestCard(
                title = "Nearby Places",
                description = "GET /api/places/nearby",
                icon = Icons.Default.Place,
                onClick = { viewModel.testNearbyPlaces() },
                params = "lat=41.0082, lng=28.9784, radius=5000"
            )

            EndpointTestCard(
                title = "Text Search",
                description = "GET /api/places/text-search",
                icon = Icons.Default.Search,
                onClick = { viewModel.testTextSearch() },
                params = "query=restaurant, languageCode=tr"
            )

            EndpointTestCard(
                title = "Place Details",
                description = "GET /api/places/details/{placeId}",
                icon = Icons.Default.Info,
                onClick = { viewModel.testPlaceDetails() },
                params = "placeId=ChIJAQAAACW0yhQR2ZKKrI2y0NI"
            )

            HorizontalDivider()

            // Location Sync API Section
            Text(
                text = "🔄 Location Sync API (/api/sync)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            EndpointTestCard(
                title = "Sync Locations",
                description = "POST /api/sync/locations",
                icon = Icons.Default.Sync,
                onClick = { viewModel.testLocationSync() },
                params = "lat=41.0082, lng=28.9784, radius=5000"
            )

            HorizontalDivider()

            // Test Result Section
            if (testResult != EndpointTestResult.Idle) {
                Text(
                    text = "📊 Test Sonucu",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = when (testResult) {
                            is EndpointTestResult.Success -> MaterialTheme.colorScheme.tertiaryContainer
                            is EndpointTestResult.Error -> MaterialTheme.colorScheme.errorContainer
                            is EndpointTestResult.Loading -> MaterialTheme.colorScheme.secondaryContainer
                            else -> MaterialTheme.colorScheme.surface
                        }
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        when (testResult) {
                            is EndpointTestResult.Loading -> {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                                    Text("Yükleniyor...")
                                }
                            }
                            is EndpointTestResult.Success -> {
                                Column {
                                    Text(
                                        text = (testResult as EndpointTestResult.Success).data,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Button(
                                        onClick = { viewModel.clearTestResult() },
                                        modifier = Modifier.align(Alignment.End)
                                    ) {
                                        Text("Temizle")
                                    }
                                }
                            }
                            is EndpointTestResult.Error -> {
                                Column {
                                    Text(
                                        text = (testResult as EndpointTestResult.Error).message,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Button(
                                        onClick = { viewModel.clearTestResult() },
                                        modifier = Modifier.align(Alignment.End)
                                    ) {
                                        Text("Temizle")
                                    }
                                }
                            }
                            else -> {}
                        }
                    }
                }
            }

            HorizontalDivider()

            // Bottom Actions
            OutlinedButton(
                onClick = onNavigateToLanguage,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = stringResource(R.string.change_language)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.change_language))
            }

            Button(
                onClick = { viewModel.logout() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(stringResource(R.string.logout))
            }
        }
    }
}

@Composable
fun EndpointTestCard(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    params: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = params,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
        }
    }
}
