package com.slimviewer.core

import android.content.Context
import androidx.compose.runtime.Composable

interface DocumentEngine {
    fun canHandle(type: DocumentType): Boolean
    
    /**
     * Renders the document. 
     * In a real implementation, this might return a specialized View or Compose UI.
     */
    @Composable
    fun Render(metadata: DocumentMetadata)
}
