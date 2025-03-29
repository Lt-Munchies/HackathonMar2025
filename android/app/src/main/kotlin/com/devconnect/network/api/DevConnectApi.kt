package com.devconnect.network.api

import com.devconnect.data.model.Comment
import com.devconnect.data.model.Post
import com.devconnect.network.model.AiCommentRequest
import com.devconnect.network.model.CommentRequest
import com.devconnect.network.model.PageResponse
import com.devconnect.network.model.PostRequest
import com.devconnect.network.model.PostSearchRequest
import retrofit2.http.*
import java.util.UUID

interface DevConnectApi {
    // Posts
    @GET("api/posts")
    suspend fun getPosts(
        @Query("page") page: Int = 0,
        @Query("sortBy") sortBy: String = "LATEST"
    ): PageResponse<Post>

    @GET("api/posts/{id}")
    suspend fun getPost(@Path("id") id: UUID): Post

    @POST("api/posts")
    suspend fun createPost(@Body request: PostRequest): Post

    @PUT("api/posts/{id}")
    suspend fun updatePost(
        @Path("id") id: UUID,
        @Body request: PostRequest
    ): Post

    @DELETE("api/posts/{id}")
    suspend fun deletePost(@Path("id") id: UUID)

    @POST("api/posts/search")
    suspend fun searchPosts(@Body request: PostSearchRequest): PageResponse<Post>

    @POST("api/posts/{id}/reactions/{type}")
    suspend fun addPostReaction(
        @Path("id") id: UUID,
        @Path("type") type: Post.ReactionType
    ): Post

    @DELETE("api/posts/{id}/reactions/{type}")
    suspend fun removePostReaction(
        @Path("id") id: UUID,
        @Path("type") type: Post.ReactionType
    ): Post

    // Comments
    @GET("api/posts/{postId}/comments")
    suspend fun getComments(@Path("postId") postId: UUID): List<Comment>

    @POST("api/posts/{postId}/comments")
    suspend fun createComment(
        @Path("postId") postId: UUID,
        @Body request: CommentRequest
    ): Comment

    @PUT("api/posts/{postId}/comments/{id}")
    suspend fun updateComment(
        @Path("postId") postId: UUID,
        @Path("id") id: UUID,
        @Body request: CommentRequest
    ): Comment

    @DELETE("api/posts/{postId}/comments/{id}")
    suspend fun deleteComment(
        @Path("postId") postId: UUID,
        @Path("id") id: UUID
    )

    @POST("api/posts/{postId}/comments/ai")
    suspend fun generateAiComment(
        @Path("postId") postId: UUID,
        @Body request: AiCommentRequest
    ): Comment

    @POST("api/posts/{postId}/comments/{id}/reactions/{type}")
    suspend fun addCommentReaction(
        @Path("postId") postId: UUID,
        @Path("id") id: UUID,
        @Path("type") type: Comment.ReactionType
    ): Comment

    @DELETE("api/posts/{postId}/comments/{id}/reactions/{type}")
    suspend fun removeCommentReaction(
        @Path("postId") postId: UUID,
        @Path("id") id: UUID,
        @Path("type") type: Comment.ReactionType
    ): Comment
}
