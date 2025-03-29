package com.devconnect.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.devconnect.data.UserPreferences
import com.devconnect.ui.navigation.NavGraph
import com.devconnect.ui.theme.DevConnectTheme
import com.devconnect.ui.util.ClipboardUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var userPreferences: UserPreferences
    @Inject lateinit var clipboardUtil: ClipboardUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DevConnectTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var showAliasDialog by remember { mutableStateOf(false) }
                    var authorAlias by remember { mutableStateOf<String?>(null) }

                    LaunchedEffect(Unit) {
                        userPreferences.authorAlias.collect { alias ->
                            if (alias == null) {
                                showAliasDialog = true
                            } else {
                                authorAlias = alias
                            }
                        }
                    }

                    if (showAliasDialog) {
                        var alias by remember { mutableStateOf("") }
                        var error by remember { mutableStateOf<String?>(null) }

                        AlertDialog(
                            onDismissRequest = {},
                            title = { Text("Welcome to DevConnect") },
                            text = {
                                Column {
                                    Text("Please enter your alias to continue:")
                                    OutlinedTextField(
                                        value = alias,
                                        onValueChange = {
                                            alias = it
                                            error = null
                                        },
                                        isError = error != null,
                                        supportingText = error?.let { { Text(it) } }
                                    )
                                }
                            },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        if (alias.isBlank()) {
                                            error = "Alias cannot be empty"
                                            return@Button
                                        }
                                        if (alias.length > 50) {
                                            error = "Alias too long (max 50 characters)"
                                            return@Button
                                        }
                                        showAliasDialog = false
                                        authorAlias = alias
                                        // Save in background
                                        userPreferences.setAuthor(alias)
                                    }
                                ) {
                                    Text("Continue")
                                }
                            }
                        )
                    }

                    authorAlias?.let { alias ->
                        NavGraph(
                            clipboardUtil = clipboardUtil,
                            authorAlias = alias
                        )
                    }
                }
            }
        }
    }
}
