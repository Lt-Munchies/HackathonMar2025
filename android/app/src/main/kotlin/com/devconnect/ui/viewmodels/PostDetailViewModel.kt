package com.devconnect.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devconnect.data.model.Comment
import com.devconnect.data.model.Post
import com.devconnect.network.api.DevConnectApi
import com.devconnect.network.model.AiCommentRequest
import com.devconnect.network.util.NetworkResult
import com.devconnect.network.util.withRetry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val api: DevConnectApi
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun loadPost(postId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            when (val result = withRetry {
                val post = api.getPost(UUID.fromString(postId))
                val comments = api.getComments(UUID.fromString(postId))
                Pair(post, comments)
            }) {
                is NetworkResult.Success -> {
                    val (post, comments) = result.data
                    _uiState.value = _uiState.value.copy(
                        post = post,
                        comments = comments,
                        isLoading = false,
                        error = null
                    )
                }
                is NetworkResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                NetworkResult.Loading -> Unit
            }
        }
    }

    fun generateAiComment() {
        val postId = _uiState.value.post?.id ?: return
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                isGeneratingAiComment = true
            )
            
            when (val result = withRetry {
                api.generateAiComment(
                    postId = postId,
                    request = AiCommentRequest(
                        codeBlockIndex = _uiState.value.post?.codeBlocks?.firstOrNull()?.let { 0 },
                        specificQuestion = null
                    )
                )
            }) {
                is NetworkResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        comments = _uiState.value.comments + result.data,
                        isLoading = false,
                        isGeneratingAiComment = false,
                        error = null
                    )
                }
                is NetworkResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isGeneratingAiComment = false,
                        error = result.message
                    )
                }
                NetworkResult.Loading -> Unit
            }
        }
    }

    fun togglePostReaction(type: Post.ReactionType) {
        val post = _uiState.value.post ?: return
        val hasReaction = (post.reactions[type] ?: 0) > 0

        viewModelScope.launch {
            when (val result = withRetry {
                if (hasReaction) {
                    api.removePostReaction(post.id, type)
                } else {
                    api.addPostReaction(post.id, type)
                }
            }) {
                is NetworkResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        post = result.data,
                        error = null
                    )
                }
                is NetworkResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        error = result.message
                    )
                }
                NetworkResult.Loading -> Unit
            }
        }
    }

    fun toggleCommentReaction(commentId: UUID, type: Comment.ReactionType) {
        val post = _uiState.value.post ?: return
        val comment = _uiState.value.comments.find { it.id == commentId } ?: return
        val hasReaction = (comment.reactions[type] ?: 0) > 0

        viewModelScope.launch {
            when (val result = withRetry {
                if (hasReaction) {
                    api.removeCommentReaction(post.id, commentId, type)
                } else {
                    api.addCommentReaction(post.id, commentId, type)
                }
            }) {
                is NetworkResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        comments = _uiState.value.comments.map {
                            if (it.id == commentId) result.data else it
                        },
                        error = null
                    )
                }
                is NetworkResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        error = result.message
                    )
                }
                NetworkResult.Loading -> Unit
            }
        }
    }

    data class UiState(
        val post: Post? = null,
        val comments: List<Comment> = emptyList(),
        val isLoading: Boolean = false,
        val isGeneratingAiComment: Boolean = false,
        val error: String? = null
    )
}
