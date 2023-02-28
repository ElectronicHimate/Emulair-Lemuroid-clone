package com.bigbratan.emulair.common.utils.graphics

import android.graphics.Bitmap
import android.opengl.GLSurfaceView
import android.view.PixelCopy
import com.bigbratan.emulair.common.utils.kotlin.runCatchingWithRetry
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.math.roundToInt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun GLSurfaceView.takeScreenshot(
    maxResolution: Int,
    retries: Int = 1
): Bitmap? = withContext(Dispatchers.Main) {
    runCatchingWithRetry(retries) {
        takeScreenshot(maxResolution)
    }.getOrNull()
}

private suspend fun GLSurfaceView.takeScreenshot(maxResolution: Int): Bitmap? = suspendCoroutine { cont ->
    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N) {
        cont.resume(null)
        return@suspendCoroutine
    }

    queueEvent {
        try {
            val outputScaling = maxResolution / maxOf(width, height).toFloat()
            val inputScaling = outputScaling * 2

            val inputBitmap = Bitmap.createBitmap(
                (width * inputScaling).roundToInt(),
                (height * inputScaling).roundToInt(),
                Bitmap.Config.ARGB_8888
            )

            val onCompleted = { result: Int ->
                if (result == PixelCopy.SUCCESS) {

                    // This rescaling limits the artifacts introduced by shaders.
                    val outputBitmap = Bitmap.createScaledBitmap(
                        inputBitmap,
                        (width * outputScaling).roundToInt(),
                        (height * outputScaling).roundToInt(),
                        true
                    )

                    cont.resume(outputBitmap)
                } else {
                    cont.resumeWithException(RuntimeException("Cannot take screenshot. Error code: $result"))
                }
            }
            PixelCopy.request(this, inputBitmap, onCompleted, handler)
        } catch (e: Exception) {
            cont.resumeWithException(e)
        }
    }
}
