package com.slimviewer.engine.hwp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.slimviewer.core.DocumentEngine
import com.slimviewer.core.DocumentMetadata
import com.slimviewer.core.DocumentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HwpViewerEngine : DocumentEngine {
    override fun canHandle(type: DocumentType): Boolean {
        return type == DocumentType.HWP
    }

    @Composable
    override fun Render(metadata: DocumentMetadata) {
        val context = LocalContext.current
        var parseResult by remember { mutableStateOf<HwpParseResult>(HwpParseResult.Loading) }

        LaunchedEffect(metadata.uri) {
            parseResult = HwpParseResult.Loading
            parseResult = withContext(Dispatchers.IO) {
                HwpParser.parseToText(context, metadata)
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            when (val result = parseResult) {
                is HwpParseResult.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is HwpParseResult.Error -> {
                    Text(
                        text = "Error: ${result.message}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center).padding(16.dp)
                    )
                }
                is HwpParseResult.Success -> {
                    HwpContentView(result.paragraphs)
                }
            }
        }
    }

    @Composable
    private fun HwpContentView(paragraphs: List<String>) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                Text(
                    text = "Quick View Beta",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            items(paragraphs) { paragraph ->
                Text(
                    text = paragraph,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
            }
            
            item {
                // TODO: Add support for images and tables
                Text(
                    text = "\n[End of text extraction. Advanced layout, tables, and images are not yet supported in Beta.]",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}
