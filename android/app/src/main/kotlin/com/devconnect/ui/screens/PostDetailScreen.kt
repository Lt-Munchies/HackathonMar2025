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
import com.devconnect.data.model.Comment
import com.devconnect.data.model.Post
import com.devconnect.ui.components.*
import com.devconnect.ui.util.ClipboardUtil
import com.devconnect.ui.viewmodels.PostDetailViewModel

@Composable
fun PostDetailScreen(
    postId: String,
    onNavigateUp: () -> Unit,
    viewModel: PostDetailViewModel = hiltViewModel(),
    clipboardUtil: ClipboardUtil
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(postId) {
        viewModel.loadPost(postId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Post Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Outlined.ArrowBack,
                            contentDescription = "Back"
                        )
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
            uiState.post?.let { post ->
                item {
                    PostContent(
                        post = post,
                        onReactionClick = { type ->
                            viewModel.togglePostReaction(type)
                        },
                        clipboardUtil = clipboardUtil
                    )
                }

                item {
                    Divider()
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Comments (${post.commentCount})",
                            style = MaterialTheme.typography.titleMedium
                        )
                        TextButton(
                            onClick = {
                                viewModel.generateAiComment()
                            }
                        ) {
                            Icon(
                                imageVector = androidx.compose.material.icons.Icons.Outlined.SmartToy,
                                contentDescription = "Generate AI Comment"
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Ask AI")
                        }
                    }
                }

                items(uiState.comments) { comment ->
                    CommentCard(
                        comment = comment,
                        onReactionClick = { type ->
                            viewModel.toggleCommentReaction(comment.id, type)
                        },
                        clipboardUtil = clipboardUtil
                    )
                }
            }
        }

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (uiState.isGeneratingAiComment) {
                    AiLoadingIndicator()
                } else {
                    LoadingIndicator(text = "Loading post...")
                }
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
private fun PostContent(
    post: Post,
    onReactionClick: (Post.ReactionType) -> Unit,
    clipboardUtil: ClipboardUtil,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = post.title,
            style = MaterialTheme.typography.headlineMedium
        )

        if (!post.content.isNullOrBlank()) {
            Text(
                text = post.content,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        post.codeBlocks.forEach { codeBlock ->
            CodeBlockView(
                codeBlock = codeBlock,
                modifier = Modifier.fillMaxWidth(),
                clipboardUtil = clipboardUtil
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "by ${post.authorAlias}",
                style = MaterialTheme.typography.labelMedium
            )
            PostReactionBar(
                reactions = post.reactions,
                onReactionClick = onReactionClick
            )
        }
    }
}

@Composable
private fun CommentCard(
    comment: Comment,
    onReactionClick: (Comment.ReactionType) -> Unit,
    clipboardUtil: ClipboardUtil,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (comment.isAiGenerated)
                MaterialTheme.colorScheme.secondaryContainer
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (comment.isAiGenerated) "AI Assistant" else comment.authorAlias,
                    style = MaterialTheme.typography.labelMedium
                )
                if (comment.isAiGenerated) {
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Outlined.SmartToy,
                        contentDescription = "AI Generated"
                    )
                }
            }

            if (!comment.content.isNullOrBlank()) {
                Text(
                    text = comment.content,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            comment.codeBlocks.forEach { codeBlock ->
                CodeBlockView(
                    codeBlock = codeBlock,
                    modifier = Modifier.fillMaxWidth(),
                    clipboardUtil = clipboardUtil
                )
            }

            CommentReactionBar(
                reactions = comment.reactions,
                onReactionClick = onReactionClick
            )
        }
    }
}
