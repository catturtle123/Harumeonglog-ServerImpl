package com.example.harumeonglog.domain.post.service;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.entity.enums.NoticeType;
import com.example.harumeonglog.domain.member.service.NoticeCommandService;
import com.example.harumeonglog.domain.post.converter.PostConverter;
import com.example.harumeonglog.domain.post.converter.PostImageConverter;
import com.example.harumeonglog.domain.post.converter.PostLikeConverter;
import com.example.harumeonglog.domain.post.converter.PostReportConverter;
import com.example.harumeonglog.domain.post.dto.request.PostRequest;
import com.example.harumeonglog.domain.post.dto.response.PostResponse;
import com.example.harumeonglog.domain.post.entity.Post;
import com.example.harumeonglog.domain.post.entity.PostImage;
import com.example.harumeonglog.domain.post.entity.PostLike;
import com.example.harumeonglog.domain.post.entity.PostReport;
import com.example.harumeonglog.domain.post.repository.PostLikeRepository;
import com.example.harumeonglog.domain.post.repository.PostReportRepository;
import com.example.harumeonglog.domain.post.repository.PostRepository;
import com.example.harumeonglog.global.error.code.PostErrorCode;
import com.example.harumeonglog.global.error.code.S3ErrorCode;
import com.example.harumeonglog.global.error.exception.PostException;
import com.example.harumeonglog.global.error.exception.S3Exception;
import com.example.harumeonglog.global.firebase.converter.FCMConverter;
import com.example.harumeonglog.global.firebase.service.FcmService;
import com.example.harumeonglog.global.util.OutboxUtil;
import com.example.harumeonglog.global.util.S3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.example.harumeonglog.global.error.code.PostErrorCode.OWN_POST_LIKE;

@Service
@Transactional
@RequiredArgsConstructor
public class PostCommandServiceImpl implements PostCommandService {
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostReportRepository postReportRepository;
    private final FcmService fcmService;
    private final NoticeCommandService noticeCommandService;
    private final S3Util s3Util;
    private final OutboxUtil outboxUtil;

    @Override
    public PostResponse.PostCreateResponse createPost(PostRequest.PostCreateRequest postCreateRequest, Member member) {
        Post post = PostConverter.toPost(postCreateRequest, member);

        addImage(postCreateRequest, post);

        // Outbox 상태 변경
        if (postCreateRequest.getPostImageList() != null) {
            postCreateRequest.getPostImageList()
                    .forEach(imageKey -> {
                        if (!s3Util.isObjectExists(imageKey)) {
                            throw new S3Exception(S3ErrorCode.NOT_FOUND);
                        }
                        outboxUtil.changeS3OutboxStatus(imageKey);
                    });
        }

        return PostConverter.toPostCreateResponse(postRepository.save(post));
    }

    @Override
    public PostResponse.PostUpdateResponse updatePost(Long postId, PostRequest.PostUpdateRequest postUpdateRequest, Member member) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostException(PostErrorCode.NOT_FOUND));

        isOwnPost(member, post);

        List<PostImage> postImageList = postUpdateRequest.getPostImageList().stream().map(PostImageConverter::toPostImage).toList();
        post.update(postUpdateRequest.getTitle(), postUpdateRequest.getContent(), postUpdateRequest.getPostCategory(), postImageList);

        // Outbox 상태를 변경
        if (postUpdateRequest.getPostImageList() != null) {
            postUpdateRequest.getPostImageList()
                    .forEach(imageKey -> {
                        if (!s3Util.isObjectExists(imageKey)) {
                            throw new S3Exception(S3ErrorCode.NOT_FOUND);
                        }
                        outboxUtil.changeS3OutboxStatus(imageKey);
                    });
        }

        return PostConverter.toPostUpdateResponse(post);
    }

    @Override
    public void deletePost(Long postId, Member member) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostException(PostErrorCode.NOT_FOUND));

        isOwnPost(member, post);

        post.softDelete();
    }

    @Override
    public void likePost(Long postId, Member member) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostException(PostErrorCode.NOT_FOUND));

        if (post.getMember().equals(member)) {
            throw new PostException(OWN_POST_LIKE);
        }

        Optional<PostLike> postLike = postLikeRepository.findByPostAndMember(post, member);

        postLike.ifPresentOrElse(
                like -> {
                    postLikeRepository.delete(like);
                    postRepository.updatePostUnLikeNumByPost(post);
                },
                () -> {
                    postLikeRepository.save(PostLikeConverter.toPostLike(post, member));
                    postRepository.updatePostLikeNumByPost(post);

                    String title = "게시글 좋아요";
                    String body = member.getNickname() + "님이 회원님의 게시글을 좋아합니다.";
                    fcmService.sendPushNotification(FCMConverter.toReceiverRequest(post.getMember()), title, body, NoticeType.ARTICLE);
                    noticeCommandService.createNotice(title, body, NoticeType.ARTICLE, member, post.getMember());
                }
        );
    }

    @Override
    public void reportPost(Long postId, Member member) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostException(PostErrorCode.NOT_FOUND));

        Optional<PostReport> postReport = postReportRepository.findByPostAndMember(post, member);

        postReport.ifPresentOrElse(
                report -> {
                    postReportRepository.delete(report);
                    postRepository.updatePostUnReportNumByPost(post);
                },
                () -> {
                    postReportRepository.save(PostReportConverter.toPostReport(post, member));
                    postRepository.updatePostReportNumByPost(post);
                }
        );
    }

    private void addImage(PostRequest.PostCreateRequest postCreateRequest, Post post) {
        postCreateRequest.getPostImageList().forEach(imageKeyName -> {
            PostImage postImage = PostImageConverter.toPostImage(imageKeyName);
            post.addPostImage(postImage);
        });
    }

    private void isOwnPost(Member member, Post post) {
        if (!post.getMember().getId().equals(member.getId())) {
            throw new PostException(PostErrorCode.NOT_OWN);
        }
    }
}
