package com.devconnect.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.devconnect.data.model.CodeBlock
import com.devconnect.ui.util.ClipboardUtil
import com.devconnect.ui.util.SyntaxHighlighter
import javax.inject.Inject

@Composable
fun CodeBlockView(
    codeBlock: CodeBlock,
    modifier: Modifier = Modifier,
    onRequestAiHelp: () -> Unit = {},
    clipboardUtil: ClipboardUtil
) {
    var isExpanded by remember { mutableStateOf(true) }
    
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Language badge
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = codeBlock.language,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Copy button
                    IconButton(onClick = {
                        clipboardUtil.copyToClipboard(codeBlock.content)
                    }) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Outlined.ContentCopy,
                            contentDescription = "Copy code"
                        )
                    }
                    
                    // AI help button
                    IconButton(onClick = onRequestAiHelp) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Outlined.SmartToy,
                            contentDescription = "Get AI help"
                        )
                    }
                    
                    // Expand/collapse button
                    IconButton(onClick = { isExpanded = !isExpanded }) {
                        Icon(
                            imageVector = if (isExpanded) 
                                androidx.compose.material.icons.Icons.Outlined.ExpandLess
                            else 
                                androidx.compose.material.icons.Icons.Outlined.ExpandMore,
                            contentDescription = if (isExpanded) "Collapse" else "Expand"
                        )
                    }
                }
            }
            
            // Code content
            if (isExpanded) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Box(modifier = Modifier.padding(8.dp)) {
                        Text(
                            text = SyntaxHighlighter.highlight(
                                code = codeBlock.content,
                                language = codeBlock.language
                            ),
                            fontFamily = FontFamily.Monospace,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                // Folding ranges
                if (codeBlock.foldingRanges.isNotEmpty()) {
                    Column(
                        modifier = Modifier.padding(top = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        codeBlock.foldingRanges.forEach { range ->
                            if (range.label != null) {
                                Text(
                                    text = "${range.kind}: ${range.label} (${range.startLine}-${range.endLine})",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
