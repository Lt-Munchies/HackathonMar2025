package com.devconnect.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.devconnect.data.model.Post
import com.devconnect.data.model.Comment

@Composable
fun PostReactionBar(
    reactions: Map<Post.ReactionType, Int>,
    onReactionClick: (Post.ReactionType) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Post.ReactionType.values().forEach { type ->
            val count = reactions[type] ?: 0
            ReactionChip(
                type = type.name,
                count = count,
                onClick = { onReactionClick(type) }
            )
        }
    }
}

@Composable
fun CommentReactionBar(
    reactions: Map<Comment.ReactionType, Int>,
    onReactionClick: (Comment.ReactionType) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Comment.ReactionType.values().forEach { type ->
            val count = reactions[type] ?: 0
            ReactionChip(
                type = type.name,
                count = count,
                onClick = { onReactionClick(type) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReactionChip(
    type: String,
    count: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChip(
        selected = count > 0,
        onClick = onClick,
        label = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(type)
                if (count > 0) {
                    Text(count.toString())
                }
            }
        },
        modifier = modifier
    )
}
