package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.MovieCreation
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.filled.ViewCarousel
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.VideoProject
import com.example.ui.screens.GalleryScreen
import com.example.ui.screens.PlayerDetailScreen
import com.example.ui.screens.StoryboardScreen
import com.example.ui.screens.StudioScreen
import com.example.ui.screens.TemplatesScreen
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.StudioBackground
import com.example.ui.theme.StudioCardBorder
import com.example.ui.theme.StudioSurface
import com.example.ui.theme.StudioSurfaceVariant
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VideoGenTheme
import com.example.viewmodel.AppScreen
import com.example.viewmodel.StudioUiState
import com.example.viewmodel.VideoGenViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VideoGenTheme {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    val viewModel: VideoGenViewModel = viewModel()
                    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                    val projects by viewModel.projects.collectAsStateWithLifecycle()

                    VideoGenApp(
                        viewModel = viewModel,
                        uiState = uiState,
                        projects = projects
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoGenApp(
    viewModel: VideoGenViewModel,
    uiState: StudioUiState,
    projects: List<VideoProject>
) {
    BackHandler(enabled = uiState.currentScreen != AppScreen.STUDIO) {
        when (uiState.currentScreen) {
            AppScreen.PLAYER_DETAIL -> viewModel.navigateTo(AppScreen.GALLERY)
            AppScreen.STORYBOARD -> viewModel.navigateTo(AppScreen.STUDIO)
            AppScreen.GALLERY -> viewModel.navigateTo(AppScreen.STUDIO)
            AppScreen.TEMPLATES -> viewModel.navigateTo(AppScreen.STUDIO)
            AppScreen.STUDIO -> { }
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(StudioBackground),
        topBar = {
            if (uiState.currentScreen != AppScreen.PLAYER_DETAIL) {
                StudioTopBar(
                    onNewProject = {
                        viewModel.updatePrompt("")
                        viewModel.navigateTo(AppScreen.STUDIO)
                    }
                )
            }
        },
        bottomBar = {
            if (uiState.currentScreen != AppScreen.PLAYER_DETAIL) {
                StudioBottomNavBar(
                    currentScreen = uiState.currentScreen,
                    onNavigate = { viewModel.navigateTo(it) }
                )
            }
        },
        containerColor = StudioBackground
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState.currentScreen) {
                AppScreen.STUDIO -> {
                    StudioScreen(
                        viewModel = viewModel,
                        uiState = uiState
                    )
                }
                AppScreen.STORYBOARD -> {
                    StoryboardScreen(
                        viewModel = viewModel,
                        uiState = uiState
                    )
                }
                AppScreen.GALLERY -> {
                    GalleryScreen(
                        viewModel = viewModel,
                        projects = projects
                    )
                }
                AppScreen.TEMPLATES -> {
                    TemplatesScreen(
                        viewModel = viewModel,
                        templates = viewModel.templates
                    )
                }
                AppScreen.PLAYER_DETAIL -> {
                    val activeProject = uiState.selectedProject ?: projects.firstOrNull()
                    if (activeProject != null) {
                        PlayerDetailScreen(
                            viewModel = viewModel,
                            project = activeProject,
                            uiState = uiState
                        )
                    } else {
                        StudioScreen(
                            viewModel = viewModel,
                            uiState = uiState
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudioTopBar(
    onNewProject: () -> Unit
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(NeonCyan, ElectricViolet)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MovieCreation,
                        contentDescription = "شعار التطبيق",
                        tint = Color.Black,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "VideoGen",
                            color = TextPrimary,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 17.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Surface(
                            color = NeonCyan.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "AI",
                                color = NeonCyan,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                    }
                    Text(
                        text = "استوديو التوليد السينمائي بالذكاء الاصطناعي",
                        color = TextSecondary,
                        fontSize = 10.sp
                    )
                }
            }
        },
        actions = {
            Surface(
                color = StudioSurfaceVariant,
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, StudioCardBorder, RoundedCornerShape(20.dp))
                    .clickable { onNewProject() }
                    .testTag("topbar_new_video_button")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "فيديو جديد",
                        color = TextPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = StudioSurface,
            titleContentColor = TextPrimary
        ),
        windowInsets = TopAppBarDefaults.windowInsets
    )
}

@Composable
fun StudioBottomNavBar(
    currentScreen: AppScreen,
    onNavigate: (AppScreen) -> Unit
) {
    NavigationBar(
        containerColor = StudioSurface,
        tonalElevation = 8.dp,
        windowInsets = NavigationBarDefaults.windowInsets
    ) {
        val navItems = listOf(
            NavigationItem(
                screen = AppScreen.STUDIO,
                label = "الاستوديو",
                icon = Icons.Default.MovieCreation,
                tag = "nav_studio"
            ),
            NavigationItem(
                screen = AppScreen.STORYBOARD,
                label = "القصة",
                icon = Icons.Default.ViewCarousel,
                tag = "nav_storyboard"
            ),
            NavigationItem(
                screen = AppScreen.GALLERY,
                label = "المعرض",
                icon = Icons.Default.VideoLibrary,
                tag = "nav_gallery"
            ),
            NavigationItem(
                screen = AppScreen.TEMPLATES,
                label = "القوالب",
                icon = Icons.Default.Dashboard,
                tag = "nav_templates"
            )
        )

        navItems.forEach { item ->
            val isSelected = currentScreen == item.screen
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item.screen) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Black,
                    selectedTextColor = NeonCyan,
                    indicatorColor = NeonCyan,
                    unselectedIconColor = TextSecondary,
                    unselectedTextColor = TextSecondary
                ),
                modifier = Modifier.testTag(item.tag)
            )
        }
    }
}

private data class NavigationItem(
    val screen: AppScreen,
    val label: String,
    val icon: ImageVector,
    val tag: String
)
