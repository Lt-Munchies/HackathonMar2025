package com.devconnect.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devconnect.data.model.Post
import com.devconnect.network.api.DevConnectApi
import com.devconnect.network.model.PostSearchRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainFeedViewModel @Inject constructor(
    private val api: DevConnectApi
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        loadPosts()
    }

    private fun loadPosts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val response = api.getPosts()
                _uiState.value = _uiState.value.copy(
                    posts = response.content,
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                Timber.e(e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to load posts: ${e.message}"
                )
            }
        }
    }

    fun searchPosts(request: PostSearchRequest) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val response = api.searchPosts(request)
                _uiState.value = _uiState.value.copy(
                    posts = response.content,
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                Timber.e(e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to search posts: ${e.message}"
                )
            }
        }
    }

    data class UiState(
        val posts: List<Post> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null
    )
}
