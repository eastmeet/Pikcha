package com.main36.pikcha.api.post;


import com.amazonaws.AmazonServiceException;
import com.main36.pikcha.domain.attraction.dto.ProvinceFilterDto;
import com.main36.pikcha.domain.attraction.service.AttractionService;

import com.main36.pikcha.domain.comment.service.CommentService;
import com.main36.pikcha.domain.hashtag.entity.HashTag;
import com.main36.pikcha.domain.hashtag.service.HashTagService;
import com.main36.pikcha.domain.member.entity.Member;
import com.main36.pikcha.domain.member.service.MemberService;
import com.main36.pikcha.domain.post.dto.*;
import com.main36.pikcha.domain.post.entity.Post;
import com.main36.pikcha.domain.post.mapper.PostMapper;
import com.main36.pikcha.domain.post.service.PostService;


import com.main36.pikcha.domain.image.entity.PostImage;
import com.main36.pikcha.domain.image.service.PostImageService;
import com.main36.pikcha.global.aop.LoginUser;


import com.main36.pikcha.global.exception.BusinessLogicException;
import com.main36.pikcha.global.exception.ExceptionCode;
import com.main36.pikcha.global.response.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Positive;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
@Tag(name = "[Post] 게시물 ", description = "사용자 게시물 관련 API입니다.")
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;
    private final MemberService memberService;
    private final AttractionService attractionService;
    private final HashTagService hashTagService;
    private final PostImageService postImageService;
    private final CommentService commentService;

    @Operation(summary = "포스트 등록")
    @LoginUser
    @PostMapping("/register/{attraction-id}")
    @PostApiResponse
    @SwaggerErrorResponses
    public ResponseEntity<DataResponseDto<?>> registerPost2(Member loginUser,
                                                            @PathVariable("attraction-id") @Positive long attractionId,
                                                            PostDto.Post postDto) {
        Post post = new Post();

        // 포스트 제목 설정
        post.setPostTitle(postDto.getPostTitle());

        // 빈 리스트 생성
        List<HashTag> hashTagList = new ArrayList<>();
        List<String> postContentList = new ArrayList<>();
        List<PostImage> postImageList = new ArrayList<>();

        // 포스트 해시태그 생성 후 추가
        if (postDto.getPostHashTags() != null) {
            for (String hashtag : postDto.getPostHashTags()) {
                HashTag newHashTag = new HashTag();
                newHashTag.setHashTagContent(hashtag);
                hashTagList.add(hashTagService.createHashTag(newHashTag));
            }
        }
        // 포스트 캡션 추가
        if (postDto.getPostContents() != null) {
            for (String postContent : postDto.getPostContents()) {
                postContentList.add(postContent);
            }
        }
        // 포스트 이미지 s3에 저장 후 추가
        if (postDto.getPostImageFiles() != null) {
            for (MultipartFile file : postDto.getPostImageFiles()) {
                try {
                    postImageList.add(postImageService.createPostImage(file));
                } catch (AmazonServiceException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
        post.setHashTags(hashTagList);
        post.setPostContents(postContentList);
        post.setPostImages(postImageList);

        // post.setPostImages(postImageList);
        //  PostImages.setPost(post)

        post.setAttraction(attractionService.findAttraction(attractionId));
        post.setMember(loginUser);
        post.setComments(new ArrayList<>());

        Post createdPost = postService.createPost(post);
        // response 생성
        PostResponseDto.Detail response = postMapper.postToPostDetailResponseDto(createdPost);
        // 좋아요 누른 여부를 false로 반환(처음 생성해서 false)
        response.setIsVoted(false);

        return new ResponseEntity<>(new DataResponseDto<>(response), HttpStatus.CREATED);
    }

    @Operation(summary = "포스트 수정")
    @LoginUser
    @PatchMapping("/edit/{post-id}")
    @PatchApiResponse
    @SwaggerErrorResponses
    public ResponseEntity patchPosts(Member loginUser,
            /*@PathVariable("member-id") @Positive Long memberId,*/
                                     @PathVariable("post-id") @Positive Long postId,
                                     PostDto.Patch patchDto) {
        Post findPost = verifiedById(loginUser.getMemberId(), postId);
//        Post findPost = verifiedById(memberId, postId);

        // 1. 포스트 제목 변경점 수정 (완료)
        if (patchDto.getPostTitle() != null) {
            if (!patchDto.getPostTitle().equals(findPost.getPostTitle())) {
                // 받은 제목이 원래 제목과 다르다면 수정
                findPost.setPostTitle(patchDto.getPostTitle());
            }
        }

        // 2. 해시태그 변경점 수정
        if (patchDto.getPostHashTags() != null) {
            // 원래 있던 해시태그 중에
            List<HashTag> removed = new ArrayList<>();
            for (HashTag hashTag : findPost.getHashTags()) {
                // patchDto에 이 해시태그가 없다면
                if (!patchDto.getPostHashTags().contains(hashTag.getHashTagContent())) {
                    // 해시태그 삭제
                    removed.add(hashTag);
                }
            }
            findPost.getHashTags().removeAll(removed);
//            hashTagService.deleteHashTags(removed, findPost.getPostId());
            // patchDto에서
            for (String hashTag : patchDto.getPostHashTags()) {
                // 새로운 해시태그가 있다면
                if (hashTagService.findHashTag(hashTag).isEmpty()) {
                    // 해시태그 생성
                    HashTag newTag = new HashTag();
                    newTag.setHashTagContent(hashTag);
                    // post에 추가
                    findPost.getHashTags().add(hashTagService.createHashTag(newTag));
                    // ★ 업데이트를 날려야 하나? ★
                }
            }
        }

        // 3. 포스트 캡션 수정
        if (patchDto.getPostContents() != null) {
            // 원래 있던 캡션 중에
            List<String> removed = new ArrayList<>();
            for (String content : findPost.getPostContents()) {
                // patchDto에 이 캡션이 없다면
                if (!patchDto.getPostContents().contains(content)) {
                    // content 삭제
                    removed.add(content);
                }
            }
            findPost.getPostContents().removeAll(removed);
            // patchDto에서
            for (String content : patchDto.getPostContents()) {
                // 새로운 content가 있다면
                if (!findPost.getPostContents().contains(content)) {
                    // content 추가
                    findPost.getPostContents().add(content);
                }
            }
        }

        // 4. 포스트 이미지 수정 (완료)
        if (patchDto.getDeleteUrls() != null) {
            // deleteUrls 에 있는 주소로 s3와 데이터베이스에서 삭제
            postImageService.deletePostImagesByUrls(patchDto.getDeleteUrls());
        }

        // 5. 포스트 이미지 새로 등록 (완료)
        if (patchDto.getPostImageFiles() != null) {
            for (MultipartFile file : patchDto.getPostImageFiles()) {
                try {
                    findPost.getPostImages().add(postImageService.createPostImage(file));
                } catch (AmazonServiceException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // 6. 포스트 업데이트 (완료)
        postService.updatePost(findPost);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "포스트 상세조회")
    @GetMapping(value = {"/details/{post-id}", "/details/{post-id}/{member-id}"})
    @GetApiResponse
    @SwaggerErrorResponses
    public ResponseEntity<DataResponseDto<?>> getPost(@PathVariable("post-id") @Positive long postId,
                                                      @PathVariable("member-id") Optional<Long> memberId) {
        Post post = postService.findPost(postId);
        PostResponseDto.Detail response = postMapper.postToPostDetailResponseDto(post);
        // 로그인 여부에 따라 좋아요 하트 채워지는 여부 결정
        if (memberId.isEmpty()) {
            response.setIsVoted(false);
        } else response.setIsVoted(postService.isVoted(memberId.get(), postId));
        response.setCommentCount(commentService.countAllCommentsByPost(post));

        return ResponseEntity.ok(new DataResponseDto<>(response));
    }

    @Operation(summary = "포스트 리스트 조회")
    @GetMapping(value = {"/home", "/home/{member-id}"})
    @GetApiResponse
    @SwaggerErrorResponses
    public ResponseEntity<MultiResponseDto<?>> getHomePosts(@PathVariable("member-id") Optional<Long> memberId,
                                                            @RequestParam(defaultValue = "newest", required = false) String sort,
                                                            @RequestParam(defaultValue = "1", required = false) @Positive int page,
                                                            @RequestParam(defaultValue = "8", required = false) @Positive int size) {
        sort = getString(sort);
        Page<Post> allPostsBySort = postService.findAllPostsBySort(page - 1, size, sort);
        List<Post> content = allPostsBySort.getContent();
        return responseMethod(content, allPostsBySort, memberId);
    }

    @Operation(summary = "명소 페이지 포스트 리스트 지역구 조회")
    @PostMapping(value = {"/filter", "/filter/{member-id}"})
    @PostApiResponse
    @SwaggerErrorResponses
    public ResponseEntity<MultiResponseDto<?>> getFilteredPosts(@PathVariable("member-id") Optional<Long> memberId,
                                                                @RequestParam(defaultValue = "newest", required = false) String sort,
                                                                @RequestParam(defaultValue = "1", required = false) @Positive int page,
                                                                @RequestParam(defaultValue = "9", required = false) @Positive int size,
                                                                @RequestBody ProvinceFilterDto filterDto) {
        sort = getString(sort);

        List<Post> content;
        Page<Post> postPage;
        if (filterDto.getProvinces().size() == 0) {
            postPage = postService.findAllPostsBySort(page - 1, size, sort);
        } else {
            postPage = postService.findAllPostsByProvincesSort(filterDto.getProvinces(), page - 1, size, sort);
        }
        content = postPage.getContent();

        return responseMethod(content, postPage, memberId);
    }

    @Operation(summary = "명소 상세페이지 포스트 리스트 조회")
    @GetMapping(value = {"/{attraction-id}", "/{attraction-id}/{member-id}"})
    @GetApiResponse
    @SwaggerErrorResponses
    public ResponseEntity<MultiResponseDto<?>> getPostsByAttractionDetailsPage(@PathVariable("attraction-id") long attractionId,
                                                                               @PathVariable("member-id") Optional<Long> memberId,
                                                                               @RequestParam(defaultValue = "newest", required = false) String sort,
                                                                               @RequestParam(defaultValue = "1", required = false) @Positive int page,
                                                                               @RequestParam(defaultValue = "8", required = false) @Positive int size) {
        sort = getString(sort);
        Page<Post> allPostsBySort = postService.findAllPostsByAttractionId(attractionId, page - 1, size, sort);
        List<Post> content = allPostsBySort.getContent();

        return responseMethod(content, allPostsBySort, memberId);
    }

    @Operation(summary = "포스트 삭제")
    @LoginUser
    @DeleteMapping("/delete/{post-id}")
    @DeleteApiResponse
    @SwaggerErrorResponses
    public ResponseEntity<Object> deletePost(Member loginUser,
                                                 @PathVariable("post-id") long postId) {
        String dirName = "images";
        Post post = verifiedById(loginUser.getMemberId(), postId);
        // CascadeType.REMOVE 라서 객체는 지울 필요 없고, s3에서 이미지만 지우면 된다
        if (!post.getPostImages().isEmpty()) {
            postImageService.deleteOnlyS3Images(post.getPostImages());
        }
        postService.erasePost(post);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "포스트 좋아요")
    @LoginUser
    @PostMapping("/likes/{post-id}")
    @PostApiResponse
    @SwaggerErrorResponses
    public ResponseEntity<DataResponseDto<?>> votePost(Member loginUser,
                                                       @PathVariable("post-id") @Positive long postId) {
        // 회원 정보를 받아온다
        Member member = memberService.findMemberByMemberId(loginUser.getMemberId());

        // 포스트 정보를 찾는다
        Post post = postService.findPostNoneSetView(postId);

        // 회원과 포스트 정보를 바탕으로 좋아요가 눌러져 있다면 true, 아니면 false를 받는다.
        boolean status = postService.votePost(member, post);

        // responseDto 생성
        PostLikesResponseDto response = new PostLikesResponseDto();
        response.setIsVoted(status);

        return new ResponseEntity<>(new DataResponseDto<>(response), HttpStatus.OK);
    }

    private Post verifiedById(long clientId, long postId) {
        Post post = postService.findPostNoneSetView(postId);
        if (clientId == 1) return post;
        if (!post.getMember().getMemberId().equals(clientId)) {
            throw new BusinessLogicException(ExceptionCode.USER_IS_NOT_EQUAL);
        }

        return post;
    }

    private List<PostResponseDto.Home> loginMapping(List<Post> postList, long memberId) {
        return postList.stream()
                .map(post -> {
                    return PostResponseDto.Home.builder()
                            .postId(post.getPostId())
                            .memberId(post.getMember().getMemberId())
                            .username(post.getMember().getUsername())
                            .memberPicture(post.getMember().getPicture())
                            .pictureUrl(post.getPostImages().isEmpty() ? "" : post.getPostImages().get(0).getPostImageUrl())
                            .views(post.getViews())
                            .likes(post.getLikes())
                            .isVoted(postService.isVoted(memberId, post.getPostId()))
                            .postTitle(post.getPostTitle())
                            .createdAt(post.getCreatedAt())
                            .modifiedAt(post.getModifiedAt())
                            .build();
                }).collect(Collectors.toList());
    }

    private List<PostResponseDto.Home> guestMapping(List<Post> postList) {
        return postList.stream()
                .map(post -> {
                    return PostResponseDto.Home.builder()
                            .postId(post.getPostId())
                            .memberId(post.getMember().getMemberId())
                            .username(post.getMember().getUsername())
                            .memberPicture(post.getMember().getPicture())
                            .pictureUrl(post.getPostImages().isEmpty() ? "" : post.getPostImages().get(0).getPostImageUrl())
                            .views(post.getViews())
                            .likes(post.getLikes())
                            .isVoted(false)
                            .postTitle(post.getPostTitle())
                            .createdAt(post.getCreatedAt())
                            .modifiedAt(post.getModifiedAt())
                            .build();
                }).collect(Collectors.toList());
    }

    private ResponseEntity<MultiResponseDto<?>> responseMethod(List<Post> content, Page<Post> page, Optional<Long> memberId) {
        return memberId.<ResponseEntity<MultiResponseDto<?>>>map(aLong -> new ResponseEntity<>(new MultiResponseDto<>(
                loginMapping(content, aLong), page), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(new MultiResponseDto<>(
                guestMapping(content), page), HttpStatus.OK));
    }

    private static String getString(String sort) {
        switch (sort) {
            case "newest":
                sort = "postId";
                break;
            case "likes":
                sort = "likes";
                break;
            case "views":
                sort = "views";
                break;
        }
        return sort;
    }

}
