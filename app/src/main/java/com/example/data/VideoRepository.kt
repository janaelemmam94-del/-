package com.example.data

import com.example.R
import com.example.model.AspectRatioOption
import com.example.model.CameraMotion
import com.example.model.SceneItem
import com.example.model.TransitionType
import com.example.model.VideoProject
import com.example.model.VideoStyle
import com.example.model.VideoTemplate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class VideoRepository {

    private val initialProjects = listOf(
        VideoProject(
            id = "proj_cyberpunk_1",
            title = "طيران ليلي فوق طوكيو المستقبلية",
            prompt = "مدينة سايبربانك مستقبلية بالنيون تحت المطر ليلاً مع مركبات طائرة ولوحات إعلانية هولوغرافية ضخمة، إضاءة سينمائية، لقطة سينمائية بدقة 8k",
            scenes = listOf(
                SceneItem("sc_1", 1, "لقطة تأسيسية جوية واسعة لناطحات سحاب النيون وسط الضباب البنفسجي", CameraMotion.PAN_RIGHT, 4, TransitionType.WHIP_PAN, R.drawable.img_cyberpunk_city),
                SceneItem("sc_2", 2, "كاميرا تتبع سريعة تطارد مركبة هوائية بين اللوحات الهولوغرافية", CameraMotion.FPV_DRONE, 4, TransitionType.GLITCH, R.drawable.img_cyberpunk_city)
            ),
            aspectRatio = AspectRatioOption.LANDSCAPE_16_9,
            fps = 30,
            durationSeconds = 8,
            style = VideoStyle.CYBERPUNK,
            cameraMotion = CameraMotion.PAN_RIGHT,
            motionIntensity = 1.2f,
            lightingTag = "سايبر نيون",
            hasAudioSoundtrack = true,
            audioSoundtrackName = "إيقاع سينث ويف مستقبلي",
            createdAt = System.currentTimeMillis() - 3600000L * 2,
            isFavorite = true,
            thumbnailRes = R.drawable.img_cyberpunk_city,
            seed = 981245012L
        ),
        VideoProject(
            id = "proj_space_2",
            title = "أصداء كوكب الفضاء المجهول",
            prompt = "مشهد خيال علمي سينمائي لرائد فضاء يستكشف كوكباً فضائياً بنباتات مضيئة حيوياً وقمرين في السماء الأرجوانية، بدقة فائقة 8k",
            scenes = listOf(
                SceneItem("sc_3", 1, "زووم سينمائي بطيء على المستكشف الوحيد أمام الأزهار المضيئة والقمرين", CameraMotion.DOLLY_ZOOM, 6, TransitionType.DISSOLVE, R.drawable.img_space_odyssey)
            ),
            aspectRatio = AspectRatioOption.LANDSCAPE_16_9,
            fps = 24,
            durationSeconds = 6,
            style = VideoStyle.CINEMATIC,
            cameraMotion = CameraMotion.DOLLY_ZOOM,
            motionIntensity = 0.8f,
            lightingTag = "ضوء القمر الدرامي",
            hasAudioSoundtrack = true,
            audioSoundtrackName = "أجواء كونية عميقة ومؤثرات أثيرية",
            createdAt = System.currentTimeMillis() - 3600000L * 14,
            isFavorite = true,
            thumbnailRes = R.drawable.img_space_odyssey,
            seed = 551982734L
        ),
        VideoProject(
            id = "proj_nature_3",
            title = "تحليق درون فوق مضيق الجبال والضباب",
            prompt = "لقطة جوية سينمائية بطائرة درون لقمم الجبال الخضراء الشاهقة المحاطة بضباب الصباح الذهبي والشلالات المتدفقة بدقة 4K",
            scenes = listOf(
                SceneItem("sc_4", 1, "هبوط درون من عين الطائر عبر السحاب ليكشف شلال الجبل الكريستالي", CameraMotion.FPV_DRONE, 8, TransitionType.CROSSFADE, R.drawable.img_nature_drone)
            ),
            aspectRatio = AspectRatioOption.LANDSCAPE_16_9,
            fps = 60,
            durationSeconds = 8,
            style = VideoStyle.PHOTOREAL,
            cameraMotion = CameraMotion.FPV_DRONE,
            motionIntensity = 1.4f,
            lightingTag = "الساعة الذهبية",
            hasAudioSoundtrack = true,
            audioSoundtrackName = "صوت المياه المتدفقة والرياح الطبيعية",
            createdAt = System.currentTimeMillis() - 3600000L * 28,
            isFavorite = false,
            thumbnailRes = R.drawable.img_nature_drone,
            seed = 771239011L
        )
    )

    private val _projects = MutableStateFlow<List<VideoProject>>(initialProjects)
    val projects: StateFlow<List<VideoProject>> = _projects.asStateFlow()

    val templates: List<VideoTemplate> = listOf(
        VideoTemplate(
            id = "tpl_cyber_reel",
            title = "سايبربانك نيون في المطر",
            category = "خيال علمي وريلز",
            prompt = "زقاق سايبربانك مليء بأضواء النيون تحت المطر، وانعكاسات على المياه، وبخار يتصاعد من الأرضيات مع لوحات هولوغرافية",
            style = VideoStyle.CYBERPUNK,
            cameraMotion = CameraMotion.PAN_RIGHT,
            durationSeconds = 8,
            aspectRatio = AspectRatioOption.PORTRAIT_9_16,
            previewDrawableRes = R.drawable.img_cyberpunk_city,
            badge = "ريلز رائج"
        ),
        VideoTemplate(
            id = "tpl_space_epic",
            title = "ملحمة الفضاء السحيق",
            category = "أفلام سينمائية",
            prompt = "رائد فضاء يقف على حافة وادٍ سحيق متوهج في كوكب مجهول، وسديم فضائي مذهل يدور في السماء الكونية",
            style = VideoStyle.CINEMATIC,
            cameraMotion = CameraMotion.DOLLY_ZOOM,
            durationSeconds = 6,
            aspectRatio = AspectRatioOption.LANDSCAPE_16_9,
            previewDrawableRes = R.drawable.img_space_odyssey,
            badge = "سينما 4K"
        ),
        VideoTemplate(
            id = "tpl_nature_drone",
            title = "درون جبال الألب الخضراء",
            category = "طبيعة ودرون",
            prompt = "لقطة جوية خاطفة لدرون سينمائي يمر بسرعة فائقة فوق قمم جبال شاهقة يداعبها ضوء شروق الشمس وشلالات المياه",
            style = VideoStyle.PHOTOREAL,
            cameraMotion = CameraMotion.FPV_DRONE,
            durationSeconds = 10,
            aspectRatio = AspectRatioOption.LANDSCAPE_16_9,
            previewDrawableRes = R.drawable.img_nature_drone,
            badge = "60 إطار/ث"
        ),
        VideoTemplate(
            id = "tpl_anime_intro",
            title = "أنمي قطار الريف والسحب",
            category = "أنمي استوديو",
            prompt = "قطار ريفي ياباني يمر عبر حقول أرز خضراء زاهية تحت سحب صيفية ضخمة متحركة بأسلوب ماكوتو شينكاي",
            style = VideoStyle.ANIME_STUDIO,
            cameraMotion = CameraMotion.PAN_LEFT,
            durationSeconds = 6,
            aspectRatio = AspectRatioOption.LANDSCAPE_16_9,
            previewDrawableRes = R.drawable.img_nature_drone,
            badge = "أنمي"
        ),
        VideoTemplate(
            id = "tpl_commercial",
            title = "عرض إعلاني لمنتج فاخر",
            category = "إعلانات تجارية",
            prompt = "كاميرا تدور 360 درجة حول ساعة يد مستقبلية من الكروم مع واجهة مضيئة تطفو في استوديو فخم وخلفية مخملية داكنة",
            style = VideoStyle.RENDER_3D,
            cameraMotion = CameraMotion.ORBIT_360,
            durationSeconds = 6,
            aspectRatio = AspectRatioOption.SQUARE_1_1,
            previewDrawableRes = R.drawable.img_cyberpunk_city,
            badge = "جاهز للإعلان"
        )
    )

    fun addProject(project: VideoProject) {
        val current = _projects.value.toMutableList()
        current.add(0, project)
        _projects.value = current
    }

    fun toggleFavorite(projectId: String) {
        _projects.value = _projects.value.map {
            if (it.id == projectId) it.copy(isFavorite = !it.isFavorite) else it
        }
    }

    fun deleteProject(projectId: String) {
        _projects.value = _projects.value.filter { it.id != projectId }
    }

    fun enhancePrompt(basePrompt: String, style: VideoStyle, motion: CameraMotion): String {
        val trimmed = basePrompt.trim()
        val motionDescription = when (motion) {
            CameraMotion.PAN_RIGHT -> "حركة بان سينمائية ناعمة نحو اليمين"
            CameraMotion.PAN_LEFT -> "مسح أفقي واسع نحو اليسار عبر الأفق"
            CameraMotion.DOLLY_ZOOM -> "تقريب درامي مع تغيير عمق المنظور (Dolly Zoom)"
            CameraMotion.ZOOM_OUT -> "ابتعاد بالرافعة للخلف لكشف اتساع المشهد"
            CameraMotion.TILT_UP -> "صعود تدريجي بزاوية منخفضة نحو السماء"
            CameraMotion.TILT_DOWN -> "هبوط بالرافعة لأسفل للتركيز على التفاصيل الدقيقة"
            CameraMotion.ORBIT_360 -> "دوران مداري 360 درجة حول الهدف"
            CameraMotion.FPV_DRONE -> "انقضاض درون FPV سريع بهلواني فائق السرعة"
            CameraMotion.STATIC -> "تثبيت كاميرا ترايبود متوازن مع حركة داخلية غنية للمشهد"
        }

        return if (trimmed.isEmpty()) {
            "مشهد سينمائي رئيسي، $motionDescription، بنمط ${style.label}، ${style.promptModifier}، إضاءة استوديو متقدمة وتكوين بصري احترافي بدقة 8K."
        } else {
            "$trimmed، مصور بتقنية $motionDescription، بأسلوب ${style.label} الفني، ${style.promptModifier}، عمق مجال حجمي، وجودة سينمائية ماستر 4K."
        }
    }
}
