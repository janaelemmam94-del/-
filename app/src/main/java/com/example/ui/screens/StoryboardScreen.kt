package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CameraMotion
import com.example.model.SceneItem
import com.example.model.TransitionType
import com.example.ui.theme.CinemaGold
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.LaserRose
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.StudioBackground
import com.example.ui.theme.StudioCardBorder
import com.example.ui.theme.StudioSurface
import com.example.ui.theme.StudioSurfaceVariant
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.AppScreen
import com.example.viewmodel.StudioUiState
import com.example.viewmodel.VideoGenViewModel

@Composable
fun StoryboardScreen(
    viewModel: VideoGenViewModel,
    uiState: StudioUiState,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateTo(AppScreen.STUDIO)
    }

    val totalStoryboardSeconds = uiState.scenes.sumOf { it.durationSeconds }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(StudioBackground)
            .padding(16.dp)
    ) {
        // شريط العنوان
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { viewModel.navigateTo(AppScreen.STUDIO) },
                    modifier = Modifier.testTag("storyboard_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "رجوع",
                        tint = TextPrimary
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text(
                        text = "مخرج مشاهد القصة (Storyboard)",
                        color = TextPrimary,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${uiState.scenes.size} مشاهد • الإجمالي ${totalStoryboardSeconds} ثانية",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }
            }

            // زر إضافة مشهد
            Surface(
                color = ElectricViolet.copy(alpha = 0.2f),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .border(1.dp, ElectricViolet, RoundedCornerShape(10.dp))
                    .clickable { viewModel.addScene() }
                    .testTag("add_scene_button")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "إضافة مشهد",
                        tint = ElectricViolet,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "مشهد جديد",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // الشريط الزمني التسلسلي
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, StudioCardBorder, RoundedCornerShape(12.dp)),
            color = StudioSurface
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "المخطط الزمني للمشاهد",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "00:00 → 00:${String.format(java.util.Locale.US, "%02d", totalStoryboardSeconds)}",
                        color = CinemaGold,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(26.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(StudioBackground),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    uiState.scenes.forEachIndexed { index, scene ->
                        val colors = listOf(NeonCyan, ElectricViolet, LaserRose, CinemaGold)
                        val color = colors[index % colors.size]

                        Box(
                            modifier = Modifier
                                .weight(scene.durationSeconds.toFloat())
                                .background(color.copy(alpha = 0.75f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "مشهد ${scene.sceneNumber} (${scene.durationSeconds}ث)",
                                color = Color.Black,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // قائمة بطاقات المشاهد
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(uiState.scenes) { index, scene ->
                SceneCardItem(
                    scene = scene,
                    canDelete = uiState.scenes.size > 1,
                    onPromptChange = { viewModel.updateScenePrompt(scene.id, it) },
                    onMotionChange = { viewModel.updateSceneMotion(scene.id, it) },
                    onDelete = { viewModel.removeScene(scene.id) }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // زر تجميع وتوليد القصة
        Button(
            onClick = {
                val combinedPrompt = uiState.scenes.joinToString(" ثم ") { "المشهد ${it.sceneNumber}: ${it.prompt}" }
                viewModel.updatePrompt(combinedPrompt)
                viewModel.setDuration(totalStoryboardSeconds.coerceIn(4, 16))
                viewModel.generateVideo()
            },
            enabled = !uiState.generationProgress.isGenerating,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .clip(RoundedCornerShape(14.dp))
                .testTag("render_storyboard_button"),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            listOf(NeonCyan, ElectricViolet)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "إنتاج ومعالجة تسلسل القصة بالكامل (${totalStoryboardSeconds} ثانية)",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun SceneCardItem(
    scene: SceneItem,
    canDelete: Boolean,
    onPromptChange: (String) -> Unit,
    onMotionChange: (CameraMotion) -> Unit,
    onDelete: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, StudioCardBorder, RoundedCornerShape(16.dp)),
        color = StudioSurface
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(NeonCyan),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${scene.sceneNumber}",
                            color = Color.Black,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 12.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "المشهد رقم ${scene.sceneNumber}",
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        color = StudioSurfaceVariant,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "${scene.durationSeconds} ثوانٍ",
                            color = CinemaGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                if (canDelete) {
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "حذف المشهد",
                            tint = TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // وصف المشهد
            OutlinedTextField(
                value = scene.prompt,
                onValueChange = onPromptChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "صف أحداث المشهد رقم ${scene.sceneNumber}...",
                        color = TextSecondary.copy(alpha = 0.6f),
                        fontSize = 12.sp
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedBorderColor = NeonCyan,
                    unfocusedBorderColor = StudioCardBorder,
                    focusedContainerColor = StudioSurfaceVariant,
                    unfocusedContainerColor = StudioSurfaceVariant
                ),
                shape = RoundedCornerShape(10.dp),
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(10.dp))

            // اختيار الحركة والانتقال
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(
                        CameraMotion.PAN_RIGHT,
                        CameraMotion.DOLLY_ZOOM,
                        CameraMotion.FPV_DRONE,
                        CameraMotion.ORBIT_360,
                        CameraMotion.TILT_UP
                    ).forEach { motion ->
                        val isSelected = scene.cameraMotion == motion
                        Surface(
                            color = if (isSelected) CinemaGold.copy(alpha = 0.2f) else StudioSurfaceVariant,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .border(
                                    1.dp,
                                    if (isSelected) CinemaGold else Color.Transparent,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { onMotionChange(motion) }
                        ) {
                            Text(
                                text = motion.label,
                                color = if (isSelected) CinemaGold else TextSecondary,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Surface(
                    color = StudioSurfaceVariant,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.SwapHoriz,
                            contentDescription = null,
                            tint = ElectricViolet,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = scene.transition.label,
                            color = TextSecondary,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }
}
