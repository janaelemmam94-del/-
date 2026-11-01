package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.R
import com.example.data.VideoRepository
import com.example.model.AspectRatioOption
import com.example.model.CameraMotion
import com.example.model.GenerationProgress
import com.example.model.SceneItem
import com.example.model.TransitionType
import com.example.model.VideoProject
import com.example.model.VideoStyle
import com.example.model.VideoTemplate
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

enum class AppScreen {
    STUDIO,
    STORYBOARD,
    GALLERY,
    TEMPLATES,
    PLAYER_DETAIL
}

data class StudioUiState(
    val prompt: String = "مركبة طائرة مستقبلية تتسابق بين ناطحات السحاب المضيئة بالنيون في ليلة ممطرة بدقة 8k",
    val aspectRatio: AspectRatioOption = AspectRatioOption.LANDSCAPE_16_9,
    val style: VideoStyle = VideoStyle.CYBERPUNK,
    val cameraMotion: CameraMotion = CameraMotion.PAN_RIGHT,
    val durationSeconds: Int = 8,
    val fps: Int = 30,
    val motionIntensity: Float = 1.0f,
    val lighting: String = "سايبر نيون",
    val audioSoundtrack: Boolean = true,
    val scenes: List<SceneItem> = listOf(
        SceneItem("sc_init_1", 1, "لقطة تأسيسية واسعة لمدينة المستقبل مع مسح سينمائي", CameraMotion.PAN_RIGHT, 4, TransitionType.WHIP_PAN, R.drawable.img_cyberpunk_city),
        SceneItem("sc_init_2", 2, "مطاردة درون سريعة تلاحق المركبة الطائرة", CameraMotion.FPV_DRONE, 4, TransitionType.GLITCH, R.drawable.img_cyberpunk_city)
    ),
    val generationProgress: GenerationProgress = GenerationProgress(),
    val currentScreen: AppScreen = AppScreen.STUDIO,
    val selectedProject: VideoProject? = null,
    val isExporting: Boolean = false,
    val exportProgress: Float = 0f,
    val exportCompletedMessage: String? = null
)

class VideoGenViewModel(
    private val repository: VideoRepository = VideoRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(StudioUiState())
    val uiState: StateFlow<StudioUiState> = _uiState.asStateFlow()

    val projects: StateFlow<List<VideoProject>> = repository.projects
    val templates: List<VideoTemplate> = repository.templates

    private var generationJob: Job? = null
    private var exportJob: Job? = null

    init {
        val firstProj = repository.projects.value.firstOrNull()
        if (firstProj != null) {
            _uiState.update { it.copy(selectedProject = firstProj) }
        }
    }

    fun updatePrompt(newPrompt: String) {
        _uiState.update { it.copy(prompt = newPrompt) }
    }

    fun setAspectRatio(ratio: AspectRatioOption) {
        _uiState.update { it.copy(aspectRatio = ratio) }
    }

    fun setStyle(style: VideoStyle) {
        _uiState.update { it.copy(style = style) }
    }

    fun setCameraMotion(motion: CameraMotion) {
        _uiState.update { it.copy(cameraMotion = motion) }
    }

    fun setDuration(seconds: Int) {
        _uiState.update { it.copy(durationSeconds = seconds) }
    }

    fun setFps(fps: Int) {
        _uiState.update { it.copy(fps = fps) }
    }

    fun setMotionIntensity(intensity: Float) {
        _uiState.update { it.copy(motionIntensity = intensity) }
    }

    fun setLighting(lighting: String) {
        _uiState.update { it.copy(lighting = lighting) }
    }

    fun toggleAudio() {
        _uiState.update { it.copy(audioSoundtrack = !it.audioSoundtrack) }
    }

    fun navigateTo(screen: AppScreen) {
        _uiState.update { it.copy(currentScreen = screen) }
    }

    fun selectProject(project: VideoProject) {
        _uiState.update {
            it.copy(
                selectedProject = project,
                currentScreen = AppScreen.PLAYER_DETAIL
            )
        }
    }

    fun enhancePrompt() {
        val current = _uiState.value
        val enhanced = repository.enhancePrompt(
            current.prompt,
            current.style,
            current.cameraMotion
        )
        _uiState.update { it.copy(prompt = enhanced) }
    }

    fun applyTemplate(template: VideoTemplate) {
        _uiState.update {
            it.copy(
                prompt = template.prompt,
                style = template.style,
                cameraMotion = template.cameraMotion,
                durationSeconds = template.durationSeconds,
                aspectRatio = template.aspectRatio,
                currentScreen = AppScreen.STUDIO
            )
        }
    }

    fun addScene() {
        val currentScenes = _uiState.value.scenes.toMutableList()
        val nextNumber = currentScenes.size + 1
        val newScene = SceneItem(
            id = UUID.randomUUID().toString(),
            sceneNumber = nextNumber,
            prompt = "المشهد $nextNumber: زاوية سينمائية ديناميكية بحركة ${uiState.value.cameraMotion.label}",
            cameraMotion = uiState.value.cameraMotion,
            durationSeconds = 4,
            transition = TransitionType.CROSSFADE,
            keyframeRes = when (uiState.value.style) {
                VideoStyle.CYBERPUNK -> R.drawable.img_cyberpunk_city
                VideoStyle.CINEMATIC -> R.drawable.img_space_odyssey
                else -> R.drawable.img_nature_drone
            }
        )
        currentScenes.add(newScene)
        _uiState.update { it.copy(scenes = currentScenes) }
    }

    fun updateScenePrompt(sceneId: String, newPrompt: String) {
        val updated = _uiState.value.scenes.map {
            if (it.id == sceneId) it.copy(prompt = newPrompt) else it
        }
        _uiState.update { it.copy(scenes = updated) }
    }

    fun updateSceneMotion(sceneId: String, motion: CameraMotion) {
        val updated = _uiState.value.scenes.map {
            if (it.id == sceneId) it.copy(cameraMotion = motion) else it
        }
        _uiState.update { it.copy(scenes = updated) }
    }

    fun removeScene(sceneId: String) {
        if (_uiState.value.scenes.size <= 1) return
        val filtered = _uiState.value.scenes.filter { it.id != sceneId }
        val renumbered = filtered.mapIndexed { index, sceneItem ->
            sceneItem.copy(sceneNumber = index + 1)
        }
        _uiState.update { it.copy(scenes = renumbered) }
    }

    fun toggleFavorite(projectId: String) {
        repository.toggleFavorite(projectId)
        if (_uiState.value.selectedProject?.id == projectId) {
            _uiState.update {
                it.copy(
                    selectedProject = it.selectedProject?.copy(
                        isFavorite = !(it.selectedProject.isFavorite)
                    )
                )
            }
        }
    }

    fun deleteProject(projectId: String) {
        repository.deleteProject(projectId)
        if (_uiState.value.selectedProject?.id == projectId) {
            _uiState.update {
                it.copy(
                    selectedProject = repository.projects.value.firstOrNull(),
                    currentScreen = AppScreen.GALLERY
                )
            }
        }
    }

    fun generateVideo() {
        if (_uiState.value.generationProgress.isGenerating) return

        val state = _uiState.value
        val totalFrames = state.durationSeconds * state.fps

        generationJob?.cancel()
        generationJob = viewModelScope.launch {
            // Phase 1: تحليل وتوجيه متجهات الحركة
            _uiState.update {
                it.copy(
                    generationProgress = GenerationProgress(
                        isGenerating = true,
                        phase = "تحليل النص وهندسة متجهات الحركة الكاميرية",
                        progress = 0.05f,
                        currentFrame = 0,
                        totalFrames = totalFrames,
                        etaSeconds = 8
                    )
                )
            }
            delay(900)

            // Phase 2: الانتشار العصبي وتماسك الإطارات
            for (step in 1..4) {
                val prog = 0.15f + (step * 0.12f)
                val frame = (totalFrames * prog).toInt()
                _uiState.update {
                    it.copy(
                        generationProgress = it.generationProgress.copy(
                            phase = "توليد الإطارات الأساسية بالانتشار العصبي (المرحلة $step/4)",
                            progress = prog,
                            currentFrame = frame,
                            etaSeconds = 8 - step
                        )
                    )
                }
                delay(600)
            }

            // Phase 3: توليد التدفق الزمني بين الإطارات
            for (step in 1..3) {
                val prog = 0.63f + (step * 0.08f)
                val frame = (totalFrames * prog).toInt()
                _uiState.update {
                    it.copy(
                        generationProgress = it.generationProgress.copy(
                            phase = "استكمال التدفق الزمني وسلاسة الإطارات (${state.fps} إطار/ث)",
                            progress = prog,
                            currentFrame = frame,
                            etaSeconds = 3 - step
                        )
                    )
                }
                delay(500)
            }

            // Phase 4: التلوين السينمائي وتوليد الصوت
            _uiState.update {
                it.copy(
                    generationProgress = it.generationProgress.copy(
                        phase = "المعالجة اللونية وتوليد المؤثرات الصوتية الذكية",
                        progress = 0.94f,
                        currentFrame = totalFrames - 4,
                        etaSeconds = 1
                    )
                )
            }
            delay(700)

            val chosenRes = when {
                state.style == VideoStyle.CYBERPUNK || state.prompt.contains("طوكيو") || state.prompt.contains("نيون") || state.prompt.contains("cyber", ignoreCase = true) -> R.drawable.img_cyberpunk_city
                state.style == VideoStyle.CINEMATIC || state.prompt.contains("فضاء") || state.prompt.contains("قمر") || state.prompt.contains("space", ignoreCase = true) -> R.drawable.img_space_odyssey
                else -> R.drawable.img_nature_drone
            }

            val newProject = VideoProject(
                id = "gen_" + UUID.randomUUID().toString().take(8),
                title = if (state.prompt.length > 28) state.prompt.take(28) + "..." else state.prompt.ifBlank { "فيديو ذكاء اصطناعي جديد" },
                prompt = state.prompt,
                scenes = state.scenes,
                aspectRatio = state.aspectRatio,
                fps = state.fps,
                durationSeconds = state.durationSeconds,
                style = state.style,
                cameraMotion = state.cameraMotion,
                motionIntensity = state.motionIntensity,
                lightingTag = state.lighting,
                hasAudioSoundtrack = state.audioSoundtrack,
                audioSoundtrackName = if (state.audioSoundtrack) "موسيقى ${state.style.label} الذكية" else "بدون صوت",
                createdAt = System.currentTimeMillis(),
                isFavorite = false,
                thumbnailRes = chosenRes,
                seed = kotlin.random.Random.nextLong(100000000L, 999999999L)
            )

            repository.addProject(newProject)

            _uiState.update {
                it.copy(
                    generationProgress = GenerationProgress(
                        isGenerating = false,
                        phase = "اكتمل التوليد بنجاح!",
                        progress = 1.0f,
                        currentFrame = totalFrames,
                        totalFrames = totalFrames,
                        etaSeconds = 0
                    ),
                    selectedProject = newProject,
                    currentScreen = AppScreen.PLAYER_DETAIL
                )
            }
        }
    }

    fun cancelGeneration() {
        generationJob?.cancel()
        _uiState.update {
            it.copy(generationProgress = GenerationProgress())
        }
    }

    fun exportVideo(resolution: String, format: String) {
        if (_uiState.value.isExporting) return
        exportJob?.cancel()
        exportJob = viewModelScope.launch {
            _uiState.update { it.copy(isExporting = true, exportProgress = 0f, exportCompletedMessage = null) }
            for (step in 1..10) {
                delay(300)
                _uiState.update { it.copy(exportProgress = step / 10f) }
            }
            _uiState.update {
                it.copy(
                    isExporting = false,
                    exportProgress = 1f,
                    exportCompletedMessage = "تم تصدير الفيديو بدقة $resolution ($format) وحفظه بنجاح على الهاتف!"
                )
            }
            delay(3500)
            _uiState.update { it.copy(exportCompletedMessage = null) }
        }
    }
}
