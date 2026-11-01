package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AspectRatioOption
import com.example.model.CameraMotion
import com.example.model.VideoProject
import com.example.ui.theme.CinemaGold
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.StudioBackground
import com.example.ui.theme.StudioCardBorder
import com.example.ui.theme.StudioSurface
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun VideoPlayerView(
    project: VideoProject,
    modifier: Modifier = Modifier,
    initialPlayState: Boolean = true,
    showControls: Boolean = true,
    onFullScreenClick: (() -> Unit)? = null
) {
    var isPlaying by remember { mutableStateOf(initialPlayState) }
    var currentProgress by remember { mutableFloatStateOf(0f) }
    var playbackSpeed by remember { mutableFloatStateOf(1.0f) }
    var isMuted by remember { mutableStateOf(false) }
    var isLooping by remember { mutableStateOf(true) }

    val totalDurationSeconds = project.durationSeconds.toFloat()

    // Smooth playback ticker
    LaunchedEffect(isPlaying, playbackSpeed, totalDurationSeconds, isLooping) {
        val intervalMs = 33L // ~30 fps updates
        while (isActive && isPlaying) {
            delay(intervalMs)
            val step = (intervalMs / 1000f * playbackSpeed) / totalDurationSeconds
            val next = currentProgress + step
            if (next >= 1.0f) {
                if (isLooping) {
                    currentProgress = 0f
                } else {
                    currentProgress = 1.0f
                    isPlaying = false
                }
            } else {
                currentProgress = next
            }
        }
    }

    // Dynamic Camera Motion simulation transforms based on currentProgress
    val motionIntensity = project.motionIntensity
    val (offsetX, offsetY, scaleFactor, rotationAngle) = remember(currentProgress, project.cameraMotion, motionIntensity) {
        val t = currentProgress // 0f..1f
        when (project.cameraMotion) {
            CameraMotion.PAN_RIGHT -> {
                val shift = (t - 0.5f) * 60f * motionIntensity
                MotionTransform(x = -shift, y = 0f, scale = 1.15f, rotation = 0f)
            }
            CameraMotion.PAN_LEFT -> {
                val shift = (t - 0.5f) * 60f * motionIntensity
                MotionTransform(x = shift, y = 0f, scale = 1.15f, rotation = 0f)
            }
            CameraMotion.TILT_UP -> {
                val shift = (t - 0.5f) * 50f * motionIntensity
                MotionTransform(x = 0f, y = shift, scale = 1.15f, rotation = 0f)
            }
            CameraMotion.TILT_DOWN -> {
                val shift = (t - 0.5f) * 50f * motionIntensity
                MotionTransform(x = 0f, y = -shift, scale = 1.15f, rotation = 0f)
            }
            CameraMotion.DOLLY_ZOOM -> {
                val s = 1.05f + (t * 0.28f * motionIntensity)
                MotionTransform(x = 0f, y = 0f, scale = s, rotation = 0f)
            }
            CameraMotion.ZOOM_OUT -> {
                val s = 1.30f - (t * 0.25f * motionIntensity)
                MotionTransform(x = 0f, y = 0f, scale = s, rotation = 0f)
            }
            CameraMotion.ORBIT_360 -> {
                val rot = (t * 6f - 3f) * motionIntensity
                val shiftX = (t - 0.5f) * 30f * motionIntensity
                MotionTransform(x = shiftX, y = 0f, scale = 1.18f, rotation = rot)
            }
            CameraMotion.FPV_DRONE -> {
                val rot = kotlin.math.sin(t * 6.28f * 2) * 3f * motionIntensity
                val s = 1.1f + (t * 0.2f * motionIntensity)
                val shiftY = kotlin.math.cos(t * 6.28f * 2) * 12f * motionIntensity
                MotionTransform(x = 0f, y = shiftY, scale = s, rotation = rot)
            }
            CameraMotion.STATIC -> {
                MotionTransform(x = 0f, y = 0f, scale = 1.05f, rotation = 0f)
            }
        }
    }

    val currentSeconds = currentProgress * totalDurationSeconds
    val timecode = remember(currentSeconds, totalDurationSeconds) {
        val curM = (currentSeconds / 60).toInt()
        val curS = (currentSeconds % 60).toInt()
        val curMs = ((currentSeconds % 1) * 10).toInt()
        val totM = (totalDurationSeconds / 60).toInt()
        val totS = (totalDurationSeconds % 60).toInt()
        String.format(Locale.US, "%02d:%02d.%d / %02d:%02d", curM, curS, curMs, totM, totS)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(StudioSurface)
            .border(1.dp, StudioCardBorder, RoundedCornerShape(16.dp))
    ) {
        // Viewport Frame with Aspect Ratio
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(project.aspectRatio.ratio.coerceIn(0.6f, 2.4f))
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .background(StudioBackground)
                .clickable { isPlaying = !isPlaying },
            contentAlignment = Alignment.Center
        ) {
            // Animated Canvas Viewport containing Keyframe Asset with transforms
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(0.dp))
            ) {
                Image(
                    painter = painterResource(id = project.thumbnailRes),
                    contentDescription = project.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(scaleFactor)
                        .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                        .rotate(rotationAngle)
                )

                // Cinematic Scanline & Vignette Gradient Overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.45f),
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.65f)
                                )
                            )
                        )
                )
            }

            // Top HUD Overlay
            Row(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Style & Camera Motion Badges
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = Color.Black.copy(alpha = 0.65f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(NeonCyan)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = project.style.label,
                                color = TextPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Surface(
                        color = Color.Black.copy(alpha = 0.65f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = project.cameraMotion.label,
                            color = CinemaGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                // FPS & Aspect Ratio Badges
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = Color.Black.copy(alpha = 0.65f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "${project.fps} FPS",
                            color = TextSecondary,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp)
                        )
                    }

                    Surface(
                        color = Color.Black.copy(alpha = 0.65f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = project.aspectRatio.label,
                            color = TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // Big Center Play Icon (if paused)
            if (!isPlaying) {
                Surface(
                    color = Color.Black.copy(alpha = 0.55f),
                    shape = CircleShape,
                    modifier = Modifier.size(56.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play",
                            tint = NeonCyan,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
            }

            // Bottom Right Audio & Waveform indicator
            if (project.hasAudioSoundtrack) {
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Surface(
                        color = Color.Black.copy(alpha = 0.65f),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.clickable { isMuted = !isMuted }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (isMuted) Icons.Default.VolumeMute else Icons.Default.VolumeUp,
                                contentDescription = "Audio toggle",
                                tint = if (isMuted) TextSecondary else NeonCyan,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isMuted) "صامت" else "صوت ذكي",
                                color = if (isMuted) TextSecondary else NeonCyan,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Bottom Left Timecode
            Text(
                text = timecode,
                color = Color.White,
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            )
        }

        // Controls Bar
        if (showControls) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                // Interactive Scrubber Slider
                Slider(
                    value = currentProgress,
                    onValueChange = {
                        currentProgress = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(28.dp)
                        .testTag("video_scrubber"),
                    colors = SliderDefaults.colors(
                        thumbColor = NeonCyan,
                        activeTrackColor = NeonCyan,
                        inactiveTrackColor = StudioCardBorder
                    )
                )

                // Buttons row: Play/Pause, Loop, Speed, Fullscreen
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(
                            onClick = { isPlaying = !isPlaying },
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(StudioSurface)
                                .testTag("play_pause_button")
                        ) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isPlaying) "Pause" else "Play",
                                tint = NeonCyan,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        // Loop toggle
                        IconButton(
                            onClick = { isLooping = !isLooping },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Repeat,
                                contentDescription = "Loop",
                                tint = if (isLooping) NeonCyan else TextSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        // Speed selector
                        Surface(
                            color = StudioCardBorder.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.clickable {
                                playbackSpeed = when (playbackSpeed) {
                                    0.5f -> 1.0f
                                    1.0f -> 1.5f
                                    1.5f -> 2.0f
                                    else -> 0.5f
                                }
                            }
                        ) {
                            Text(
                                text = "${playbackSpeed}x",
                                color = TextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    // Resolution & Soundscape label
                    Text(
                        text = project.audioSoundtrackName,
                        color = TextSecondary,
                        fontSize = 11.sp,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

private data class MotionTransform(
    val x: Float,
    val y: Float,
    val scale: Float,
    val rotation: Float
)
