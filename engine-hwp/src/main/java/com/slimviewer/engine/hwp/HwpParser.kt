package com.slimviewer.engine.hwp

import android.content.Context
import com.slimviewer.core.FileCacheAdapter
import com.slimviewer.core.DocumentMetadata
import kr.dogfoot.hwplib.`object`.HWPFile
import kr.dogfoot.hwplib.reader.HWPReader
import kr.dogfoot.hwplib.tool.textextractor.TextExtractor
import kr.dogfoot.hwplib.tool.textextractor.TextExtractMethod
import java.io.File

sealed class HwpParseResult {
    data class Success(val paragraphs: List<String>) : HwpParseResult()
    data class Error(val message: String) : HwpParseResult()
    object Loading : HwpParseResult()
}

object HwpParser {
    fun parseToText(context: Context, metadata: DocumentMetadata): HwpParseResult {
        val tempFile = FileCacheAdapter.copyToCache(context, metadata.uri, metadata.name)
            ?: return HwpParseResult.Error("Failed to cache file")

        return try {
            val hwpFile: HWPFile = HWPReader.fromFile(tempFile.absolutePath)
            
            // Extract text from each section
            // TODO: In the future, extract layout info, tables, and images.
            // For Beta, we just extract text using the TextExtractor tool.
            val extractedText = TextExtractor.extract(hwpFile, TextExtractMethod.InsertControlTextBetweenParagraphText)
            
            // Split by newline to get paragraphs for easier rendering in a LazyColumn
            val paragraphs = extractedText.split("\n")
                .filter { it.isNotBlank() }
            
            HwpParseResult.Success(paragraphs)
        } catch (e: Exception) {
            HwpParseResult.Error("Parsing failed: ${e.message}")
        } finally {
            // Optional: delete temp file immediately if memory is tight, 
            // but usually cache cleanup is handled by FileCacheAdapter.
        }
    }
}
