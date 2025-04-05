package com.example.harumeonglog.domain.post.controller;

import com.example.harumeonglog.domain.common.controller.response.CustomResponse;
import com.example.harumeonglog.domain.post.controller.dto.request.PostRequest;
import com.example.harumeonglog.domain.post.controller.dto.response.PostResponse;
import com.example.harumeonglog.domain.post.controller.port.PostCommandService;
import com.example.harumeonglog.domain.post.controller.port.PostQueryService;
import com.example.harumeonglog.domain.post.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostCommandService postCommandService;
    private final PostQueryService postQueryService;

    @GetMapping
    public CustomResponse<PostResponse.PostListResponse> getPosts(
            @RequestParam(name = "cursor") Long cursor,
            @RequestParam(name = "size") Integer size
    ) {
        Slice<Post> postSlice = postQueryService.getPosts(cursor, size);
        Long nextCursor = postSlice.toList().get(postSlice.getSize() - 1).getId();
        PostResponse.PostListResponse from = PostResponse.PostListResponse.from(nextCursor, postSlice.hasNext(), postSlice.toList());
        return CustomResponse.ok(from);
    }

    @GetMapping("/{postId}")
    public CustomResponse<PostResponse.PostDetailResponse> getPost() {
        Post post = postQueryService.getPost();
        PostResponse.PostDetailResponse from = PostResponse.PostDetailResponse.from(post);
        return CustomResponse.ok(from);
    }

    @PostMapping
    public CustomResponse<Long> createPost(
            @RequestBody PostRequest.PostCreateRequest postCreateRequest
            ) {
        Post post = postCommandService.createPost(postCreateRequest);
        return CustomResponse.ok(post.getId());
    }

    @PatchMapping("/{postId}")
    public CustomResponse<PostResponse.PostPreviewResponse> updatePost(
            @PathVariable Long postId,
            @RequestBody PostRequest.PostUpdateRequest postUpdateRequest
    ) {
        Post post = postCommandService.updatePost(postId, postUpdateRequest);
        return CustomResponse.ok(PostResponse.PostPreviewResponse.from(post));
    }

    @DeleteMapping("/{postId}")
    public CustomResponse<Void> deletePost(
            @PathVariable Long postId
    ) {
        postCommandService.deletePost(postId);
        return CustomResponse.ok(null);
    }

    @PostMapping("/{postId}/likes")
    public CustomResponse<Void> likePost(
            @PathVariable Long postId
    ) {
        postCommandService.likePost(postId);
        return CustomResponse.ok(null);
    }

    @PostMapping("/{postId}/reports")
    public CustomResponse<Void> reportPost(
            @PathVariable Long postId
    ) {
        postCommandService.reportPost(postId);
        return CustomResponse.ok(null);
    }

    @GetMapping("/me")
    public CustomResponse<PostResponse.PostListResponse> getMyPost(
            @RequestParam(name = "cursor") Long cursor,
            @RequestParam(name = "size") Integer size
    ) {
        Slice<Post> postSlice = postQueryService.getMyPost(cursor, size);
        Long nextCursor = postSlice.toList().get(postSlice.getSize() - 1).getId();
        PostResponse.PostListResponse from = PostResponse.PostListResponse.from(nextCursor, postSlice.hasNext(), postSlice.toList());
        return CustomResponse.ok(from);
    }

    @GetMapping("/me/likes")
    public CustomResponse<PostResponse.PostListResponse> getMyLikePost(
            @RequestParam(name = "cursor") Long cursor,
            @RequestParam(name = "size") Integer size
    ) {
        Slice<Post> postSlice = postQueryService.getMyLikePost(cursor, size);
        Long nextCursor = postSlice.toList().get(postSlice.getSize() - 1).getId();
        PostResponse.PostListResponse from = PostResponse.PostListResponse.from(nextCursor, postSlice.hasNext(), postSlice.toList());
        return CustomResponse.ok(from);
    }

}
