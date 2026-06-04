package com.slimviewer.core

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

object FileCacheAdapter {
    /**
     * Copies a content:// URI to a temporary file in the app's cache directory.
     * This is necessary because many native/Java libraries expect a File or a path,
     * not a ContentResolver-managed stream.
     */
    fun copyToCache(context: Context, uri: Uri, fileName: String): File? {
        return try {
            val cacheDir = File(context.cacheDir, "doc_cache")
            if (!cacheDir.exists()) cacheDir.mkdirs()
            
            // Clean up old cached files
            cacheDir.listFiles()?.forEach { it.delete() }

            val tempFile = File(cacheDir, fileName)
            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(tempFile).use { output ->
                    input.copyTo(output)
                }
            }
            tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
