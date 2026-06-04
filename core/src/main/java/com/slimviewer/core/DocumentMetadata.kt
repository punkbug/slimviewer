package com.slimviewer.core

import android.net.Uri

data class DocumentMetadata(
    val uri: Uri,
    val name: String,
    val type: DocumentType,
    val size: Long = 0
)
