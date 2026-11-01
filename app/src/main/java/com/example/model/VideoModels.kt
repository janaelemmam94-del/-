package com.example.model

import androidx.annotation.DrawableRes
import com.example.R

enum class AspectRatioOption(
    val label: String,
    val subtitle: String,
    val ratio: Float,
    val widthAspect: Int,
    val heightAspect: Int
) {
    LANDSCAPE_16_9("16:9", "يوتيوب وسينما", 16f / 9f, 16, 9),
    PORTRAIT_9_16("9:16", "ريلز وتيك توك", 9f / 16f, 9, 16),
    SQUARE_1_1("1:1", "مربع انستغرام", 1f, 1, 1),
    CLASSIC_4_3("4:3", "سينما كلاسيكية", 4f / 3f, 4, 3),
    ULTRAWIDE_21_9("21:9", "شاشة عريضة", 21f / 9f, 21, 9)
}

enum class CameraMotion(
    val label: String,
    val description: String,
    val iconName: String
) {
    STATIC("كاميرا ثابتة", "تثبيت ترايبود مستقر بدون حركة", "videocam"),
    PAN_RIGHT("بان لليمين", "مسح سينمائي أفقي نحو اليمين", "arrow_forward"),
    PAN_LEFT("بان لليسار", "مسح سينمائي أفقي نحو اليسار", "arrow_back"),
    TILT_UP("صعود لأعلى", "زاوية منخفضة ترتفع نحو السماء", "arrow_upward"),
    TILT_DOWN("هبوط لأسفل", "حركة رافعة تهبط للأسفل", "arrow_downward"),
    DOLLY_ZOOM("زووم سينمائي", "تقريب درامي مع تغيير المنظور", "zoom_in"),
    ZOOM_OUT("ابتعاد للخلف", "كشف المشهد المحيط بالكامل", "zoom_out"),
    ORBIT_360("دوران 360°", "دوران محيطي دائري حول الهدف", "sync"),
    FPV_DRONE("طيران درون FPV", "تحليق بهلواني سريع بالطائرة المسيرة", "flight")
}

enum class VideoStyle(
    val label: String,
    val promptModifier: String,
    val colorHex: Long
) {
    CINEMATIC("سينمائي 8K", "cinematic lighting, anamorphic lens, 8k resolution, photorealistic, color graded 35mm film", 0xFF00E5FF),
    CYBERPUNK("سايبربانك نيون", "neon rain reflections, holographic billboards, neo-Tokyo atmosphere, moody teal and magenta", 0xFFFF2A85),
    ANIME_STUDIO("أنمي استوديو", "Makoto Shinkai aesthetic, hand-drawn anime skies, vibrant colors, expressive lighting", 0xFF8B5CF6),
    PHOTOREAL("واقعي فوتوغرافي", "natural lighting, documentary style, ultra high fidelity, subtle depth of field", 0xFF10B981),
    RENDER_3D("ثلاثي الأبعاد 3D", "Unreal Engine 5 render, raytraced shadows, subsurface scattering, stylized 3D animation", 0xFFFF9E00),
    VINTAGE_FILM("فيلم كلاسيكي 1970", "Kodachrome 64, film grain, subtle halation, warm retro tones, light leaks", 0xFFFFD166),
    CLAYMATION("صلصال متوقف", "tactile claymation texture, whimsical stop motion, handcrafted miniature lighting", 0xFFE879F9)
}

enum class TransitionType(val label: String) {
    CROSSFADE("تلاشي متقاطع"),
    WHIP_PAN("حركة سريعة (Whip)"),
    GLITCH("خلل تقني (Glitch)"),
    DISSOLVE("ذوبان سينمائي"),
    CUT("قطع مباشر")
}

data class SceneItem(
    val id: String,
    val sceneNumber: Int,
    val prompt: String,
    val cameraMotion: CameraMotion = CameraMotion.PAN_RIGHT,
    val durationSeconds: Int = 4,
    val transition: TransitionType = TransitionType.CROSSFADE,
    @DrawableRes val keyframeRes: Int? = null
)

data class GenerationProgress(
    val isGenerating: Boolean = false,
    val phase: String = "جاهز",
    val progress: Float = 0f,
    val currentFrame: Int = 0,
    val totalFrames: Int = 120,
    val etaSeconds: Int = 0,
    val previewBitmapPrompt: String = ""
)

data class VideoProject(
    val id: String,
    val title: String,
    val prompt: String,
    val scenes: List<SceneItem> = emptyList(),
    val aspectRatio: AspectRatioOption = AspectRatioOption.LANDSCAPE_16_9,
    val fps: Int = 30,
    val durationSeconds: Int = 6,
    val style: VideoStyle = VideoStyle.CINEMATIC,
    val cameraMotion: CameraMotion = CameraMotion.PAN_RIGHT,
    val motionIntensity: Float = 1.0f,
    val lightingTag: String = "أشعة شمس حجمية",
    val hasAudioSoundtrack: Boolean = true,
    val audioSoundtrackName: String = "موسيقى محيطية مستقبلية",
    val createdAt: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false,
    @DrawableRes val thumbnailRes: Int = R.drawable.img_cyberpunk_city,
    val seed: Long = 42891234L,
    val resolution: String = "1080p Full HD"
)

data class VideoTemplate(
    val id: String,
    val title: String,
    val category: String,
    val prompt: String,
    val style: VideoStyle,
    val cameraMotion: CameraMotion,
    val durationSeconds: Int,
    val aspectRatio: AspectRatioOption,
    @DrawableRes val previewDrawableRes: Int,
    val badge: String
)
