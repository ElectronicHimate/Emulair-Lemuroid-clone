package com.bigbratan.emulair.common.managers.saves

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import com.bigbratan.emulair.common.metadata.retrograde.CoreID
import com.bigbratan.emulair.common.metadata.retrograde.db.entity.Game
import com.bigbratan.emulair.common.managerStorage.DirectoriesManager
import java.io.File
import java.io.FileOutputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StatesPreviewManager(private val directoriesManager: DirectoriesManager) {

    suspend fun getPreviewForSlot(
        game: Game,
        coreID: CoreID,
        index: Int,
        size: Int
    ): Bitmap? = withContext(Dispatchers.IO) {
        val screenshotName = getSlotScreenshotName(game, index)
        val file = getPreviewFile(screenshotName, coreID.coreName)
        val bitmap = BitmapFactory.decodeFile(file.absolutePath)
        ThumbnailUtils.extractThumbnail(bitmap, size, size)
    }

    suspend fun setPreviewForSlot(
        game: Game,
        bitmap: Bitmap,
        coreID: CoreID,
        index: Int
    ) = withContext(Dispatchers.IO) {
        val screenshotName = getSlotScreenshotName(game, index)
        val file = getPreviewFile(screenshotName, coreID.coreName)
        FileOutputStream(file).use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, it)
        }
    }

    private fun getPreviewFile(fileName: String, coreName: String): File {
        val statesDirectories = File(directoriesManager.getStatesPreviewDirectory(), coreName)
        statesDirectories.mkdirs()
        return File(statesDirectories, fileName)
    }

    private fun getSlotScreenshotName(
        game: Game,
        index: Int
    ) = "${game.fileName}.slot${index + 1}.jpg"

    companion object {
        val PREVIEW_SIZE_DP = 96f
    }
}
