package com.example.aj_application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.aj_application.ui.theme.AJ_ApplicationTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AJ_ApplicationTheme {
                ResponsiveAppScreen()
            }
        }
    }
}

@Composable
fun ResponsiveAppScreen() {
    BoxWithConstraints {
        val isWideScreen = maxWidth > 600.dp
        
        if (isWideScreen) {
            WideLayout()
        } else {
            NarrowLayout()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WideLayout() {
    var selectedItem by remember { mutableStateOf("Dashboard") }
    val menuItems = listOf("Dashboard", "Reports", "Users", "Analytics", "Help")

    Row(modifier = Modifier.fillMaxSize()) {
        NavigationRail(
            modifier = Modifier.fillMaxHeight(),
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ) {
            menuItems.forEach { item ->
                NavigationRailItem(
                    selected = selectedItem == item,
                    onClick = { selectedItem = item },
                    icon = {
                        val icon = when(item) {
                            "Dashboard" -> Icons.Default.Home
                            "Reports" -> Icons.Default.List
                            "Users" -> Icons.Default.Person
                            "Analytics" -> Icons.Default.Settings
                            else -> Icons.Default.Info
                        }
                        Icon(icon, contentDescription = item)
                    },
                    label = { Text(item) }
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(24.dp)
        ) {
            Text(
                text = "Project $selectedItem",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(24.dp))
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    .padding(16.dp)
            ) {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(20) { index ->
                        ListItem(
                            headlineContent = { Text("Task #${index + 1} for $selectedItem") },
                            supportingContent = { Text("Secondary info about this specific task") },
                            leadingContent = { Icon(Icons.Default.CheckCircle, contentDescription = null) },
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surface)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NarrowLayout() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mobile View") },
                navigationIcon = { IconButton(onClick = {}) { Icon(Icons.Default.Menu, contentDescription = null) } }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(selected = true, onClick = {}, icon = { Icon(Icons.Default.Home, null) }, label = { Text("Home") })
                NavigationBarItem(selected = false, onClick = {}, icon = { Icon(Icons.Default.Person, null) }, label = { Text("Profile") })
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Mobile Dashboard", style = MaterialTheme.typography.titleLarge)
                    Text("Optimized for single column viewing.", style = MaterialTheme.typography.bodyMedium)
                }
            }

            repeat(10) { i ->
                ListItem(
                    headlineContent = { Text("Mobile Item $i") },
                    supportingContent = { Text("Adapts to narrow width") },
                    trailingContent = { Badge { Text("!") } }
                )
                HorizontalDivider()
            }
            
            Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                Text("Refresh Data")
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TagBrowserScreen() {
    val allTags = listOf(
        "Android", "Compose", "Kotlin", "Material 3", "UI/UX", 
        "Mobile", "Development", "Coding", "Java", "Gradle",
        "Architecture", "Navigation", "State", "Flow", "Retrofit"
    )
    val filters = listOf("Popular", "Recent", "Trending", "Featured")
    
    var selectedTags by remember { mutableStateOf(setOf<String>()) }
    var activeFilter by remember { mutableStateOf("Popular") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Tag Browser") },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Column {
                Text(
                    text = "Browse All Tags",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    allTags.forEach { tag ->
                        val isSelected = selectedTags.contains(tag)
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                selectedTags = if (isSelected) {
                                    selectedTags - tag
                                } else {
                                    selectedTags + tag
                                }
                            },
                            label = { Text(tag) },
                            leadingIcon = if (isSelected) {
                                { Icon(Icons.Default.Check, contentDescription = null, Modifier.size(18.dp)) }
                            } else null
                        )
                    }
                }
            }

            HorizontalDivider()

            Column {
                Text(
                    text = "Filter Controls (FlowColumn)",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                
                Box(modifier = Modifier.height(140.dp)) {
                    FlowColumn(
                        modifier = Modifier.fillMaxHeight(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        filters.forEach { filter ->
                            AssistChip(
                                onClick = { activeFilter = filter },
                                label = { Text(filter) },
                                leadingIcon = {
                                    RadioButton(
                                        selected = activeFilter == filter,
                                        onClick = null,
                                        modifier = Modifier.size(20.dp)
                                    )
                                },
                                border = if (activeFilter == filter) 
                                    BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                                else AssistChipDefaults.assistChipBorder(enabled = true)
                            )
                        }
                    }
                }
            }

            if (selectedTags.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Favorite, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Selected Tags (${selectedTags.size})",
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            selectedTags.forEach { tag ->
                                SuggestionChip(
                                    onClick = { selectedTags = selectedTags - tag },
                                    label = { Text(tag, style = MaterialTheme.typography.bodySmall) },
                                    icon = { Icon(Icons.Default.Close, contentDescription = null, Modifier.size(14.dp)) }
                                )
                            }
                        }
                    }
                }
            }

            FilledTonalButton(
                onClick = { selectedTags = emptySet() },
                modifier = Modifier.align(Alignment.End),
                enabled = selectedTags.isNotEmpty()
            ) {
                Text("Clear All")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Profile", style = MaterialTheme.typography.titleLarge) },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color(0xFF9FB1D1), Color(0xFFC7D3E8))
                            )
                        )
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .align(Alignment.BottomCenter)
                        .offset(y = (-20).dp),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8EAF0))
                ) {
                    Row(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Ashish Joshi",
                                style = MaterialTheme.typography.headlineMedium,
                                color = Color.DarkGray
                            )
                            Text(
                                text = "Android Developer",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            AssistChip(
                                onClick = { },
                                label = { Text("Available for hire") },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Check,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                },
                                shape = RoundedCornerShape(8.dp)
                            )
                        }
                    }
                }

                Surface(
                    modifier = Modifier
                        .size(100.dp)
                        .align(Alignment.CenterEnd)
                        .offset(x = (-40).dp, y = (-30).dp)
                        .zIndex(2f)
                        .clip(CircleShape)
                        .border(4.dp, Color.White, CircleShape),
                    color = Color(0xFFD0D8FF),
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxSize(),
                        tint = Color(0xFF4A5568)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Email, contentDescription = null, tint = Color.DarkGray)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "ashish19@bu.edu",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Badge(
                        containerColor = Color(0xFFB3261E),
                        contentColor = Color.White
                    ) {
                        Text("3")
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 1.dp)

                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDDE3F9), contentColor = Color.DarkGray)
                ) {
                    Text("View Portfolio", style = MaterialTheme.typography.titleMedium)
                }

                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5B6990))
                ) {
                    Text("Send Message", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    var notificationsEnabled by remember { mutableStateOf(true) }
    var darkModeEnabled by remember { mutableStateOf(false) }
    var volume by remember { mutableFloatStateOf(70f) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("App Settings") },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        scope.launch {
                            snackbarHostState.showSnackbar("Settings Info Pressed")
                        }
                    }) {
                        Icon(Icons.Default.Info, contentDescription = "Information")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        ) {
            Text(
                text = "General Preferences",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(bottom = 8.dp, start = 4.dp),
                color = MaterialTheme.colorScheme.primary
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Column {
                    SettingRow(
                        title = "Notifications",
                        subtitle = "Enable or disable all app notifications"
                    ) {
                        Switch(
                            checked = notificationsEnabled,
                            onCheckedChange = { notificationsEnabled = it }
                        )
                    }

                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                    SettingRow(
                        title = "Dark Mode",
                        subtitle = "Apply dark theme across the application"
                    ) {
                        Checkbox(
                            checked = darkModeEnabled,
                            onCheckedChange = { darkModeEnabled = it }
                        )
                    }

                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                    SettingRow(
                        title = "System Volume",
                        subtitle = "Adjust global audio level"
                    ) {
                        Slider(
                            value = volume,
                            onValueChange = { volume = it },
                            valueRange = 0f..100f,
                            modifier = Modifier.width(100.dp)
                        )
                    }

                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                    SettingRow(
                        title = "Factory Reset",
                        subtitle = "Restore settings to original state"
                    ) {
                        AssistChip(
                            onClick = {
                                notificationsEnabled = true
                                darkModeEnabled = false
                                volume = 70f
                                scope.launch {
                                    snackbarHostState.showSnackbar("Settings have been reset.")
                                }
                            },
                            label = { Text("Reset") },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Settings,
                                    contentDescription = null,
                                    Modifier.size(AssistChipDefaults.IconSize)
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SettingRow(
    title: String,
    subtitle: String,
    control: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(16.dp)
            .heightIn(min = 72.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Box(
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            control()
        }
    }
}
