package com.example.util

import android.media.MediaMetadataRetriever
import android.content.Context
import com.antonkarpenko.ffmpegkit.FFmpegKit
import com.antonkarpenko.ffmpegkit.ReturnCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

object VerticalVideoExporter {
    data class SceneInput(
        val imagePath: String,
        val audioPath: String?
    )

    suspend fun export(
        context: Context,
        scenes: List<SceneInput>,
        outputFile: File,
        onProgress: (String) -> Unit = {}
    ): Result<File> = withContext(Dispatchers.IO) {
        if (scenes.isEmpty()) {
            return@withContext Result.failure(IllegalArgumentException("At least one scene is required"))
        }

        val workDir = File(context.cacheDir, "video_export_${System.currentTimeMillis()}")
        workDir.mkdirs()
        val renderedFiles = mutableListOf<File>()

        try {
            scenes.forEachIndexed { index, scene ->
                val image = scene.imagePath.takeIf { it.startsWith("/") }?.let(::File)
                if (image == null || !image.exists() || image.length() == 0L) {
                    return@withContext Result.failure(Exception("Scene ${index + 1} image is unavailable"))
                }

                val sceneFile = File(workDir, "scene_${index + 1}.mp4")
                val audio = scene.audioPath?.takeIf { it.startsWith("/") }?.let(::File)
                val duration = audio?.let(::durationSeconds)?.takeIf { it > 0.0 } ?: 3.0
                onProgress("Rendering scene ${index + 1} of ${scenes.size}...")

                val command = if (audio != null && audio.exists() && audio.length() > 0L) {
                    "-y -loop 1 -i ${quote(image.absolutePath)} -i ${quote(audio.absolutePath)} " +
                        "-t $duration -vf \"scale=1080:1920:force_original_aspect_ratio=decrease," +
                        "pad=1080:1920:(ow-iw)/2:(oh-ih)/2,format=yuv420p\" " +
                        "-c:v libx264 -preset veryfast -r 30 -c:a aac -shortest -movflags +faststart ${quote(sceneFile.absolutePath)}"
                } else {
                    "-y -loop 1 -i ${quote(image.absolutePath)} -f lavfi -i " +
                        "anullsrc=channel_layout=stereo:sample_rate=44100 -t $duration " +
                        "-vf \"scale=1080:1920:force_original_aspect_ratio=decrease," +
                        "pad=1080:1920:(ow-iw)/2:(oh-ih)/2,format=yuv420p\" " +
                        "-c:v libx264 -preset veryfast -r 30 -c:a aac -shortest -movflags +faststart ${quote(sceneFile.absolutePath)}"
                }

                val session = FFmpegKit.execute(command)
                if (!ReturnCode.isSuccess(session.returnCode) || !sceneFile.exists() || sceneFile.length() == 0L) {
                    return@withContext Result.failure(Exception("FFmpeg failed while rendering scene ${index + 1}"))
                }
                renderedFiles.add(sceneFile)
            }

            val concatFile = File(workDir, "concat.txt")
            concatFile.writeText(renderedFiles.joinToString("\n") { "file '${it.absolutePath.replace("'", "'\\''")}'" })
            outputFile.parentFile?.mkdirs()
            onProgress("Joining ${renderedFiles.size} scenes into final MP4...")
            val concatCommand = "-y -f concat -safe 0 -i ${quote(concatFile.absolutePath)} -c copy -movflags +faststart ${quote(outputFile.absolutePath)}"
            val concatSession = FFmpegKit.execute(concatCommand)
            if (!ReturnCode.isSuccess(concatSession.returnCode) || !outputFile.exists() || outputFile.length() == 0L) {
                return@withContext Result.failure(Exception("FFmpeg failed while joining scenes"))
            }

            onProgress("Final MP4 ready")
            Result.success(outputFile)
        } catch (e: Exception) {
            Result.failure(e)
        } finally {
            workDir.deleteRecursively()
        }
    }

    private fun durationSeconds(file: File): Double {
        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(file.absolutePath)
            (retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toDoubleOrNull() ?: 0.0) / 1000.0
        } catch (_: Exception) {
            0.0
        } finally {
            retriever.release()
        }
    }

    private fun quote(value: String): String = "'${value.replace("'", "'\\''")}'"
}
