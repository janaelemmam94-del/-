package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.VideoProject
import com.example.ui.components.VideoPlayerView
import com.example.ui.theme.CinemaGold
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.LaserRose
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.StudioBackground
import com.example.ui.theme.StudioCardBorder
import com.example.ui.theme.StudioSurface
import com.example.ui.theme.StudioSurfaceVariant
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.AppScreen
import com.example.viewmodel.StudioUiState
import com.example.viewmodel.VideoGenViewModel
import kotlin.math.roundToInt

@Composable
fun PlayerDetailScreen(
    viewModel: VideoGenViewModel,
    project: VideoProject,
    uiState: StudioUiState,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var selectedResolution by remember { mutableStateOf("1080p Full HD") }
    var selectedFormat by remember { mutableStateOf("MP4 (H.264)") }

    BackHandler {
        viewModel.navigateTo(AppScreen.GALLERY)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(StudioBackground)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // شريط العنوان العلوي
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { viewModel.navigateTo(AppScreen.GALLERY) },
                    modifier = Modifier.testTag("player_back_button")
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
                        text = project.title,
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                    Text(
                        text = "كود البذرة: ${project.seed} • ${project.durationSeconds} ثوانٍ",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { viewModel.toggleFavorite(project.id) }) {
                    Icon(
                        imageVector = if (project.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "المفضلة",
                        tint = if (project.isFavorite) LaserRose else TextSecondary
                    )
                }

                IconButton(
                    onClick = {
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, "شاهد فيديو الذكاء الاصطناعي الذي أنشأته باستخدام VideoGen AI:\n\"${project.prompt}\"")
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(sendIntent, "مشاركة الفيديو")
                        context.startActivity(shareIntent)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "مشاركة",
                        tint = NeonCyan
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // مشغل الفيديو التفاعلي
        VideoPlayerView(
            project = project,
            modifier = Modifier.fillMaxWidth(),
            initialPlayState = true,
            showControls = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // إشعار اكتمال التصدير
        if (uiState.exportCompletedMessage != null) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, SuccessGreen, RoundedCornerShape(12.dp)),
                color = SuccessGreen.copy(alpha = 0.15f)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = SuccessGreen,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = uiState.exportCompletedMessage,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // بطاقة تفاصيل الوصف
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
                    Text(
                        text = "الوصف التوليدي (Prompt)",
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Surface(
                        color = StudioSurfaceVariant,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("VideoGen Prompt", project.prompt)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "تم نسخ الوصف إلى الحافظة بنجاح!", Toast.LENGTH_SHORT).show()
                            }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "نسخ الوصف",
                                tint = NeonCyan,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "نسخ",
                                color = NeonCyan,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = project.prompt,
                    color = TextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ParameterBadge("النمط", project.style.label, NeonCyan)
                    ParameterBadge("الحركة", project.cameraMotion.label, CinemaGold)
                    ParameterBadge("الأبعاد", project.aspectRatio.label, TextSecondary)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // استوديو التصدير والحفظ
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, StudioCardBorder, RoundedCornerShape(16.dp)),
            color = StudioSurface
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "استوديو التصدير والمعالجة",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // اختيار الدقة
                Text(
                    text = "دقة التصدير الرئيسية",
                    color = TextSecondary,
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("720p HD", "1080p Full HD", "4K UHD Pro").forEach { res ->
                        val isSelected = selectedResolution == res
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .border(
                                    1.dp,
                                    if (isSelected) NeonCyan else StudioCardBorder,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { selectedResolution = res },
                            color = if (isSelected) NeonCyan.copy(alpha = 0.2f) else StudioSurfaceVariant
                        ) {
                            Box(
                                modifier = Modifier.padding(vertical = 7.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = res,
                                    color = if (isSelected) NeonCyan else TextSecondary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // اختيار الصيغة
                Text(
                    text = "صيغة الفيديو والكوديك",
                    color = TextSecondary,
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("MP4 (H.264)", "ProRes 422", "متحرك GIF").forEach { fmt ->
                        val isSelected = selectedFormat == fmt
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .border(
                                    1.dp,
                                    if (isSelected) ElectricViolet else StudioCardBorder,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { selectedFormat = fmt },
                            color = if (isSelected) ElectricViolet.copy(alpha = 0.2f) else StudioSurfaceVariant
                        ) {
                            Box(
                                modifier = Modifier.padding(vertical = 7.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = fmt,
                                    color = if (isSelected) Color.White else TextSecondary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (uiState.isExporting) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "جاري تشفير وتصدير إطارات الفيديو...",
                                color = NeonCyan,
                                fontSize = 11.sp
                            )
                            Text(
                                text = "${(uiState.exportProgress * 100).roundToInt()}%",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { uiState.exportProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = NeonCyan,
                            trackColor = StudioSurfaceVariant
                        )
                    }
                } else {
                    Button(
                        onClick = { viewModel.exportVideo(selectedResolution, selectedFormat) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .testTag("export_video_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ElectricViolet
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Download,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "تصدير وحفظ على الجهاز ($selectedResolution)",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // زر إعادة التعديل في الاستوديو
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, StudioCardBorder, RoundedCornerShape(12.dp))
                .clickable {
                    viewModel.updatePrompt(project.prompt)
                    viewModel.setStyle(project.style)
                    viewModel.setCameraMotion(project.cameraMotion)
                    viewModel.setAspectRatio(project.aspectRatio)
                    viewModel.navigateTo(AppScreen.STUDIO)
                }
                .testTag("remix_in_studio_button"),
            color = StudioSurfaceVariant
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    tint = NeonCyan,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "إعادة التعديل والتوليد في الاستوديو (Remix)",
                    color = NeonCyan,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun ParameterBadge(label: String, value: String, accentColor: Color) {
    Surface(
        color = StudioSurfaceVariant,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.border(1.dp, StudioCardBorder, RoundedCornerShape(8.dp))
    ) {
        Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)) {
            Text(text = label, color = TextSecondary, fontSize = 9.sp)
            Text(
                text = value,
                color = accentColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
