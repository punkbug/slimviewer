package com.slimviewer.core

class DocumentEngineRouter(
    private val engines: List<DocumentEngine>
) {
    fun findEngine(type: DocumentType): DocumentEngine? {
        return engines.find { it.canHandle(type) }
    }
}
