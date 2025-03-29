package com.devconnect.data.model

import java.time.Instant
import java.util.UUID

data class Post(
    val id: UUID,
    val title: String,
    val content: String?,
    val codeBlocks: List<CodeBlock>,
    val authorId: UUID,
    val authorAlias: String,
    val createdAt: Instant,
    val commentCount: Int,
    val reactions: Map<ReactionType, Int>
) {
    enum class ReactionType {
        HEART, THUMBS_UP, ROCKET
    }
}

data class CodeBlock(
    val content: String,
    val language: String,
    val position: Int,
    val foldingRanges: List<FoldingRange> = emptyList()
) {
    data class FoldingRange(
        val startLine: Int,
        val endLine: Int,
        val kind: String,
        val label: String?
    )
}
