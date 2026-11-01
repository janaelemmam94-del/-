package com.example.ui.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MovieCreation
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.ViewCarousel
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.AspectRatioSelector
import com.example.ui.components.CameraMotionSelector
import com.example.ui.components.GenerationProgressCard
import com.example.ui.components.StyleSelector
import com.example.ui.theme.CinemaGold
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.ElectricVioletDark
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
fun StudioScreen(
    viewModel: VideoGenViewModel,
    uiState: StudioUiState,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    val promptSuggestions = listOf(
        "تاكسي طائر في مدينة مستقبلية تحت المطر وإضاءات النيون، تصوير سينمائي بدقة 8K",
        "رائد فضاء يقف أمام بوابة كونية مشعة تحت مجرة بنفسجية متلألئة",
        "طائرة درون تحلق فوق شلالات الجبال الخضراء في ضباب الصباح الساحر",
        "فقاعة ماء تطفو بانعدام الجاذبية مع انعكاسات ضوئية مجهرية داخل محطة فضائية",
        "سيارة كلاسيكية سريعة تعبر طريقاً صحراوياً مشمساً بأسلوب أفلام السبعينات"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(StudioBackground)
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // بطاقة تقدم المعالجة عند التوليد
        if (uiState.generationProgress.isGenerating) {
            GenerationProgressCard(
                progress = uiState.generationProgress,
                onCancel = { viewModel.cancelGeneration() },
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // قسم وصف المشهد
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .border(1.dp, StudioCardBorder, RoundedCornerShape(18.dp)),
            color = StudioSurface
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(NeonCyan.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.MovieCreation,
                                contentDescription = null,
                                tint = NeonCyan,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "وصف المشهد (Prompt)",
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // زر تحسين الوصف بالذكاء الاصطناعي
                    Surface(
                        color = ElectricViolet.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .border(1.dp, ElectricViolet, RoundedCornerShape(20.dp))
                            .clickable { viewModel.enhancePrompt() }
                            .testTag("ai_enhance_prompt_button")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "تحسين بالذكاء الاصطناعي",
                                tint = ElectricViolet,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "تحسين سينمائي AI",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = uiState.prompt,
                    onValueChange = { viewModel.updatePrompt(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("prompt_text_field"),
                    placeholder = {
                        Text(
                            text = stringResource(R.string.prompt_hint),
                            color = TextSecondary.copy(alpha = 0.6f),
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )
                    },
                    trailingIcon = {
                        if (uiState.prompt.isNotEmpty()) {
                            IconButton(onClick = { viewModel.updatePrompt("") }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "مسح",
                                    tint = TextSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = StudioCardBorder,
                        focusedContainerColor = StudioSurfaceVariant,
                        unfocusedContainerColor = StudioSurfaceVariant
                    ),
                    shape = RoundedCornerShape(12.dp),
                    minLines = 3,
                    maxLines = 6
                )

                Spacer(modifier = Modifier.height(10.dp))

                // اقتراحات الوصف
                Text(
                    text = "أفكار ملهمة للمشهد:",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    promptSuggestions.forEach { suggestion ->
                        Surface(
                            color = StudioSurfaceVariant,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { viewModel.updatePrompt(suggestion) }
                        ) {
                            Text(
                                text = suggestion.take(30) + "...",
                                color = TextSecondary,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // النمط البصري
        Text(
            text = "النمط البصري الفني",
            color = TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        StyleSelector(
            selectedStyle = uiState.style,
            onStyleSelected = { viewModel.setStyle(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // حركة الكاميرا
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "حركة الكاميرا الإخراجية",
                color = TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = uiState.cameraMotion.label,
                color = CinemaGold,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        CameraMotionSelector(
            selectedMotion = uiState.cameraMotion,
            onMotionSelected = { viewModel.setCameraMotion(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // أبعاد الشاشة
        Text(
            text = "أبعاد الفيديو (Aspect Ratio)",
            color = TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        AspectRatioSelector(
            selectedRatio = uiState.aspectRatio,
            onRatioSelected = { viewModel.setAspectRatio(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // معايير الإنتاج
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .border(1.dp, StudioCardBorder, RoundedCornerShape(18.dp)),
            color = StudioSurface
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "معايير الإنتاج والتصوير",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // مدة المقطع
                Text(
                    text = "مدة المقطع (بالثواني)",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(4, 6, 8, 12, 16).forEach { sec ->
                        val isSelected = uiState.durationSeconds == sec
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .border(
                                    1.dp,
                                    if (isSelected) NeonCyan else StudioCardBorder,
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable { viewModel.setDuration(sec) }
                                .testTag("duration_${sec}s"),
                            color = if (isSelected) NeonCyan.copy(alpha = 0.2f) else StudioSurfaceVariant
                        ) {
                            Box(
                                modifier = Modifier.padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$sec ث",
                                    color = if (isSelected) NeonCyan else TextPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // معدل الإطارات
                Text(
                    text = "معدل الإطارات (إطار في الثانية)",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(
                        24 to "24 سينمائي",
                        30 to "30 قياسي",
                        60 to "60 فائق السلاسة"
                    ).forEach { (fpsValue, label) ->
                        val isSelected = uiState.fps == fpsValue
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .border(
                                    1.dp,
                                    if (isSelected) ElectricViolet else StudioCardBorder,
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable { viewModel.setFps(fpsValue) }
                                .testTag("fps_${fpsValue}"),
                            color = if (isSelected) ElectricViolet.copy(alpha = 0.2f) else StudioSurfaceVariant
                        ) {
                            Box(
                                modifier = Modifier.padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = label,
                                    color = if (isSelected) Color.White else TextSecondary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // سرعة حركة الكاميرا
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "سرعة وديناميكية حركة الكاميرا",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "${String.format(java.util.Locale.US, "%.1f", uiState.motionIntensity)}x",
                        color = CinemaGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Slider(
                    value = uiState.motionIntensity,
                    onValueChange = { viewModel.setMotionIntensity(it) },
                    valueRange = 0.4f..2.0f,
                    colors = SliderDefaults.colors(
                        thumbColor = CinemaGold,
                        activeTrackColor = CinemaGold,
                        inactiveTrackColor = StudioCardBorder
                    ),
                    modifier = Modifier.testTag("motion_velocity_slider")
                )

                Spacer(modifier = Modifier.height(8.dp))

                // توليد المؤثرات الصوتية
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.GraphicEq,
                            contentDescription = null,
                            tint = if (uiState.audioSoundtrack) NeonCyan else TextSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "توليد موسيقى ومؤثرات صوتية ذكية",
                                color = TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "خلفية صوتية محيطية ملائمة للمشهد",
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Switch(
                        checked = uiState.audioSoundtrack,
                        onCheckedChange = { viewModel.toggleAudio() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = NeonCyan,
                            uncheckedThumbColor = TextSecondary,
                            uncheckedTrackColor = StudioSurfaceVariant
                        ),
                        modifier = Modifier.testTag("audio_soundscape_switch")
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // رابط محرر القصة متعدد المشاهد
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .border(1.dp, ElectricVioletDark, RoundedCornerShape(14.dp))
                .clickable { viewModel.navigateTo(AppScreen.STORYBOARD) },
            color = StudioSurfaceVariant
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(ElectricViolet.copy(alpha = 0.25f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ViewCarousel,
                            contentDescription = null,
                            tint = ElectricViolet,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "محرر مشاهد القصة (Storyboard)",
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "${uiState.scenes.size} مشاهد مجهزة • انتقالات سينمائية",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                Text(
                    text = "تعديل ←",
                    color = NeonCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // زر توليد الفيديو الرئيسي
        val totalEstFrames = uiState.durationSeconds * uiState.fps
        Button(
            onClick = { viewModel.generateVideo() },
            enabled = !uiState.generationProgress.isGenerating && uiState.prompt.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(16.dp))
                .testTag("generate_video_button"),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                disabledContainerColor = StudioSurfaceVariant
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            listOf(NeonCyan, ElectricViolet, LaserRose)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "توليد",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "توليد الفيديو بالذكاء الاصطناعي (${uiState.durationSeconds} ثانية • $totalEstFrames إطار)",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}
