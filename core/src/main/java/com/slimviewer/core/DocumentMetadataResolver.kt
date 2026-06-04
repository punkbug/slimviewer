package com.slimviewer.core

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns

object DocumentMetadataResolver {
    fun resolve(context: Context, uri: Uri): DocumentMetadata? {
        val contentResolver = context.contentResolver
        var name = "Unknown"
        var size = 0L

        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
            if (cursor.moveToFirst()) {
                name = cursor.getString(nameIndex) ?: "Unknown"
                size = cursor.getLong(sizeIndex)
            }
        }

        val extension = name.substringAfterLast('.', "").lowercase()
        val type = DocumentType.fromExtension(extension)

        return DocumentMetadata(
            uri = uri,
            name = name,
            type = type,
            size = size
        )
    }
}
