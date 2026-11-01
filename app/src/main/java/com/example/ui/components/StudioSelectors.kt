package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Crop169
import androidx.compose.material.icons.filled.Crop32
import androidx.compose.material.icons.filled.Crop54
import androidx.compose.material.icons.filled.Crop75
import androidx.compose.material.icons.filled.CropOriginal
import androidx.compose.material.icons.filled.CropSquare
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.SlowMotionVideo
import androidx.compose.material.icons.filled.StayCurrentPortrait
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AspectRatioOption
import com.example.model.CameraMotion
import com.example.model.VideoStyle
import com.example.ui.theme.CinemaGold
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.StudioCardBorder
import com.example.ui.theme.StudioSurface
import com.example.ui.theme.StudioSurfaceVariant
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun AspectRatioSelector(
    selectedRatio: AspectRatioOption,
    onRatioSelected: (AspectRatioOption) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        AspectRatioOption.entries.forEach { option ->
            val isSelected = option == selectedRatio
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .border(
                        width = if (isSelected) 1.5.dp else 1.dp,
                        color = if (isSelected) NeonCyan else StudioCardBorder,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable { onRatioSelected(option) }
                    .testTag("ratio_option_${option.name.lowercase()}"),
                color = if (isSelected) StudioSurfaceVariant else StudioSurface
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Geometric Aspect Box Graphic
                    Box(
                        modifier = Modifier
                            .width((option.widthAspect * 1.8).dp)
                            .height((option.heightAspect * 1.8).dp)
                            .clip(RoundedCornerShape(2.dp))
                            .border(
                                width = 1.2.dp,
                                color = if (isSelected) NeonCyan else TextSecondary,
                                shape = RoundedCornerShape(2.dp)
                            )
                            .background(if (isSelected) NeonCyan.copy(alpha = 0.2f) else Color.Transparent)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = option.label,
                            color = if (isSelected) NeonCyan else TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Text(
                            text = option.subtitle,
                            color = TextSecondary,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CameraMotionSelector(
    selectedMotion: CameraMotion,
    onMotionSelected: (CameraMotion) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CameraMotion.entries.forEach { motion ->
            val isSelected = motion == selectedMotion
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .border(
                        width = if (isSelected) 1.5.dp else 1.dp,
                        color = if (isSelected) CinemaGold else StudioCardBorder,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable { onMotionSelected(motion) }
                    .testTag("motion_option_${motion.name.lowercase()}"),
                color = if (isSelected) StudioSurfaceVariant else StudioSurface
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) CinemaGold else TextSecondary)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = motion.label,
                            color = if (isSelected) CinemaGold else TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = motion.description,
                        color = TextSecondary,
                        fontSize = 10.sp,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
fun StyleSelector(
    selectedStyle: VideoStyle,
    onStyleSelected: (VideoStyle) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        VideoStyle.entries.forEach { style ->
            val isSelected = style == selectedStyle
            val styleColor = Color(style.colorHex)

            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .border(
                        width = if (isSelected) 1.5.dp else 1.dp,
                        color = if (isSelected) styleColor else StudioCardBorder,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable { onStyleSelected(style) }
                    .testTag("style_option_${style.name.lowercase()}"),
                color = if (isSelected) styleColor.copy(alpha = 0.15f) else StudioSurface
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(styleColor)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = style.label,
                        color = if (isSelected) Color.White else TextPrimary,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}
