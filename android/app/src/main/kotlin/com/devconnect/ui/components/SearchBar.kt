package com.devconnect.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.devconnect.data.model.CodeBlock
import com.devconnect.network.model.PostSearchRequest
import java.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    onSearch: (PostSearchRequest) -> Unit,
    modifier: Modifier = Modifier
) {
    var query by remember { mutableStateOf("") }
    var showFilters by remember { mutableStateOf(false) }
    var selectedLanguage by remember { mutableStateOf<String?>(null) }
    var hasAiComments by remember { mutableStateOf<Boolean?>(null) }

    Column(modifier = modifier) {
        SearchBar(
            query = query,
            onQueryChange = { query = it },
            onSearch = {
                onSearch(
                    PostSearchRequest(
                        query = query.takeIf { it.isNotBlank() },
                        language = selectedLanguage,
                        hasAiComments = hasAiComments,
                        fromDate = null,
                        toDate = null
                    )
                )
            },
            active = showFilters,
            onActiveChange = { showFilters = it },
            placeholder = { Text("Search posts...") },
            leadingIcon = {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Outlined.Search,
                    contentDescription = "Search"
                )
            },
            trailingIcon = {
                IconButton(onClick = { showFilters = !showFilters }) {
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Outlined.FilterList,
                        contentDescription = "Filters"
                    )
                }
            }
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Filters",
                    style = MaterialTheme.typography.titleMedium
                )

                // Language filter
                Column {
                    Text(
                        text = "Programming Language",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Row(
                        modifier = Modifier.padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CodeBlock.SUPPORTED_LANGUAGES.forEach { language ->
                            FilterChip(
                                selected = language == selectedLanguage,
                                onClick = {
                                    selectedLanguage = if (language == selectedLanguage) {
                                        null
                                    } else {
                                        language
                                    }
                                },
                                label = { Text(language) }
                            )
                        }
                    }
                }

                // AI comments filter
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Has AI Comments",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Switch(
                        checked = hasAiComments ?: false,
                        onCheckedChange = {
                            hasAiComments = if (it) true else null
                        }
                    )
                }

                Button(
                    onClick = {
                        onSearch(
                            PostSearchRequest(
                                query = query.takeIf { it.isNotBlank() },
                                language = selectedLanguage,
                                hasAiComments = hasAiComments,
                                fromDate = null,
                                toDate = null
                            )
                        )
                        showFilters = false
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Apply Filters")
                }
            }
        }
    }
}
