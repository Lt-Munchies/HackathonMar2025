package com.devconnect.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.devconnect.data.model.Post
import com.devconnect.ui.components.CodeBlockView
import com.devconnect.ui.components.SearchBar
import com.devconnect.ui.util.ClipboardUtil
import com.devconnect.ui.viewmodels.MainFeedViewModel

@Composable
fun MainFeedScreen(
    onPostClick: (String) -> Unit,
    onCreatePost: () -> Unit,
    viewModel: MainFeedViewModel = hiltViewModel(),
    clipboardUtil: ClipboardUtil
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("DevConnect") }
                )
                SearchBar(
                    onSearch = { request ->
                        viewModel.searchPosts(request)
                    },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreatePost) {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Outlined.Add,
                    contentDescription = "Create post"
                )
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(uiState.posts) { post ->
                PostCard(
                    post = post,
                    onClick = { onPostClick(post.id.toString()) },
                    clipboardUtil = clipboardUtil
                )
            }
        }

        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator()
            }
        }

        if (uiState.error != null) {
            Snackbar {
                Text(uiState.error!!)
            }
        }
    }
}

@Composable
private fun PostCard(
    post: Post,
    onClick: () -> Unit,
    clipboardUtil: ClipboardUtil,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = post.title,
                style = MaterialTheme.typography.titleLarge
            )

            if (!post.content.isNullOrBlank()) {
                Text(
                    text = post.content,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 3
                )
            }

            if (post.codeBlocks.isNotEmpty()) {
                CodeBlockView(
                    codeBlock = post.codeBlocks.first(),
                    modifier = Modifier.fillMaxWidth(),
                    clipboardUtil = clipboardUtil
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "by ${post.authorAlias}",
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    text = "${post.commentCount} comments",
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}
