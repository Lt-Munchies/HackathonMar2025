package com.devconnect.data.model

import java.time.Instant
import java.util.UUID

data class Comment(
    val id: UUID,
    val content: String?,
    val codeBlocks: List<CodeBlock>,
    val authorId: UUID,
    val authorAlias: String,
    val postId: UUID,
    val createdAt: Instant,
    val isAiGenerated: Boolean,
    val aiMetadata: AiMetadata?,
    val reactions: Map<ReactionType, Int>
) {
    enum class ReactionType {
        HELPFUL, INSIGHTFUL
    }
}

data class AiMetadata(
    val model: String,
    val promptContext: String
)
