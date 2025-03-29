package com.devconnect.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.devconnect.data.model.CodeBlock
import com.devconnect.ui.components.CodeBlockView
import com.devconnect.ui.viewmodels.CreatePostViewModel

@Composable
fun CreatePostScreen(
    onNavigateUp: () -> Unit,
    onPostCreated: (String) -> Unit,
    authorAlias: String,
    viewModel: CreatePostViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddCodeDialog by remember { mutableStateOf(false) }

    LaunchedEffect(authorAlias) {
        viewModel.setAuthorAlias(authorAlias)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Post") },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Outlined.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            viewModel.createPost(
                                onSuccess = { postId ->
                                    onPostCreated(postId)
                                }
                            )
                        },
                        enabled = !uiState.isLoading && uiState.canSubmit
                    ) {
                        Text("POST")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                OutlinedTextField(
                    value = uiState.title,
                    onValueChange = { viewModel.updateTitle(it) },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = uiState.titleError != null,
                    supportingText = uiState.titleError?.let { { Text(it) } }
                )
            }

            item {
                OutlinedTextField(
                    value = uiState.content,
                    onValueChange = { viewModel.updateContent(it) },
                    label = { Text("Description (optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
            }

            items(uiState.codeBlocks) { codeBlock ->
                CodeBlockView(
                    codeBlock = codeBlock,
                    onRequestAiHelp = {
                        // TODO: Implement AI help for code blocks
                    }
                )
            }

            item {
                OutlinedButton(
                    onClick = { showAddCodeDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Outlined.Add,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add Code Block")
                }
            }
        }

        if (showAddCodeDialog) {
            var code by remember { mutableStateOf("") }
            var language by remember { mutableStateOf("") }
            var error by remember { mutableStateOf<String?>(null) }

            AlertDialog(
                onDismissRequest = { showAddCodeDialog = false },
                title = { Text("Add Code Block") },
                text = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = language,
                            onValueChange = {
                                language = it
                                error = null
                            },
                            label = { Text("Programming Language") },
                            modifier = Modifier.fillMaxWidth(),
                            isError = error != null,
                            supportingText = error?.let { { Text(it) } }
                        )

                        OutlinedTextField(
                            value = code,
                            onValueChange = { code = it },
                            label = { Text("Code") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 5
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (language.isBlank()) {
                                error = "Language is required"
                                return@Button
                            }
                            if (code.isBlank()) {
                                error = "Code cannot be empty"
                                return@Button
                            }
                            viewModel.addCodeBlock(
                                CodeBlock(
                                    language = language,
                                    content = code
                                )
                            )
                            showAddCodeDialog = false
                        }
                    ) {
                        Text("Add")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddCodeDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

        if (uiState.error != null) {
            Snackbar {
                Text(uiState.error!!)
            }
        }
    }
}
