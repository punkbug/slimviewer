package com.slimviewer.core

enum class DocumentType(val extensions: List<String>) {
    HWP(listOf("hwp")),
    OFFICE_WORD(listOf("doc", "docx")),
    OFFICE_EXCEL(listOf("xls", "xlsx")),
    OFFICE_ODF(listOf("odt", "ods")),
    UNKNOWN(emptyList());

    companion object {
        fun fromExtension(extension: String): DocumentType {
            val ext = extension.lowercase()
            return entries.find { it.extensions.contains(ext) } ?: UNKNOWN
        }
    }
}
