package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.VideoRepository
import com.example.model.CameraMotion
import com.example.model.VideoStyle
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("VideoGen AI", appName)
    }

    @Test
    fun `test repository enhances prompt with cinematic style`() {
        val repository = VideoRepository()
        val enhanced = repository.enhancePrompt(
            "سيارة طائرة",
            VideoStyle.CYBERPUNK,
            CameraMotion.PAN_RIGHT
        )
        assertTrue(enhanced.contains("بان"))
        assertTrue(enhanced.contains("سايبربانك"))
    }
}
