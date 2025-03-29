package com.devconnect.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devconnect.data.model.CodeBlock
import com.devconnect.data.model.Post
import com.devconnect.network.api.DevConnectApi
import com.devconnect.network.util.NetworkResult
import com.devconnect.network.util.withRetry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val api: DevConnectApi
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun setAuthorAlias(alias: String) {
        _uiState.update { it.copy(authorAlias = alias) }
    }

    fun updateTitle(title: String) {
        _uiState.update { state ->
            state.copy(
                title = title,
                titleError = if (title.length > 255) {
                    "Title must be at most 255 characters"
                } else {
                    null
                }
            )
        }
    }

    fun updateContent(content: String) {
        _uiState.update { it.copy(content = content) }
    }

    fun addCodeBlock(codeBlock: CodeBlock) {
        _uiState.update { state ->
            state.copy(
                codeBlocks = state.codeBlocks + codeBlock.copy(
                    position = state.codeBlocks.size
                )
            )
        }
    }

    fun createPost(onSuccess: (String) -> Unit) {
        val state = _uiState.value
        if (state.title.isBlank()) {
            _uiState.update { it.copy(titleError = "Title is required") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val result = withRetry {
                api.createPost(
                    Post(
                        title = state.title,
                        content = state.content.takeIf { it.isNotBlank() },
                        codeBlocks = state.codeBlocks,
                        authorId = UUID.randomUUID(), // Generated for each post
                        authorAlias = state.authorAlias
                    )
                )
            }) {
                is NetworkResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    onSuccess(result.data.id.toString())
                }
                is NetworkResult.Error -> {
                    _uiState.update { it.copy(
                        isLoading = false,
                        error = result.message
                    ) }
                }
                NetworkResult.Loading -> Unit
            }
        }
    }

    data class UiState(
        val title: String = "",
        val titleError: String? = null,
        val content: String = "",
        val codeBlocks: List<CodeBlock> = emptyList(),
        val authorAlias: String = "",
        val isLoading: Boolean = false,
        val error: String? = null
    ) {
        val canSubmit: Boolean
            get() = title.isNotBlank() && titleError == null
    }
}
