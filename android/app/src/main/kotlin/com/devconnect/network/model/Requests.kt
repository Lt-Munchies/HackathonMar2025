package com.devconnect.network.model

import com.devconnect.data.model.CodeBlock
import java.time.Instant
import java.util.UUID

data class PostRequest(
    val title: String,
    val content: String?,
    val codeBlocks: List<CodeBlock>,
    val authorId: UUID,
    val authorAlias: String
)

data class CommentRequest(
    val content: String?,
    val codeBlocks: List<CodeBlock>,
    val authorId: UUID,
    val authorAlias: String
)

data class AiCommentRequest(
    val codeBlockIndex: Int?,
    val specificQuestion: String?
)

data class PostSearchRequest(
    val query: String?,
    val language: String?,
    val hasAiComments: Boolean?,
    val fromDate: Instant?,
    val toDate: Instant?,
    val page: Int = 0
)

data class PageResponse<T>(
    val content: List<T>,
    val pageNumber: Int,
    val pageSize: Int,
    val totalElements: Long,
    val totalPages: Int,
    val hasNext: Boolean
)
