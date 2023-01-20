package com.main36.picha.domain.post.service;


import com.main36.picha.domain.member.entity.Member;


import com.main36.picha.domain.post.entity.Post;
import com.main36.picha.domain.post.repository.PostRepository;
import com.main36.picha.domain.post_likes.entity.PostLikes;
import com.main36.picha.domain.post_likes.repository.PostLikesRepository;
import com.main36.picha.global.exception.BusinessLogicException;
import com.main36.picha.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostLikesRepository postLikesRepository;

    public Post createPost(Post post) {
        Long numOfPostsPlusOne = post.getAttraction().getNumOfPosts() + 1;
        post.getAttraction().setNumOfPosts(numOfPostsPlusOne);

        return postRepository.save(post);
    }

    public Post updatePost(Post post) {
        Post findPost = getVerifiedPostById(post);
        Optional.ofNullable(post.getPostTitle())
                .ifPresent(findPost::setPostTitle);
        Optional.ofNullable(post.getPostContent())
                .ifPresent(findPost::setPostContent);
        Optional.ofNullable(post.getHashTagContent())
                .ifPresent(findPost::setHashTagContent);

        return findPost;
    }

    private Post getVerifiedPostById(Post post) {
        Optional<Post> postById = postRepository.findById(post.getPostId());

        return postById.orElseThrow(() -> new BusinessLogicException(ExceptionCode.POST_NOT_FOUND));
    }

    public Post findPost(Long postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        Post post = optionalPost.orElseThrow(() -> new BusinessLogicException(ExceptionCode.POST_NOT_FOUND));
        post.setViews(post.getViews() + 1);

        return post;
    }

    public Post findPostNoneSetView(Long postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);

        return optionalPost.orElseThrow(() -> new BusinessLogicException(ExceptionCode.POST_NOT_FOUND));
    }

    public Page<Post> findAllPostsBySort(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).descending());

        return postRepository.findAll(pageable);
    }

    public void erasePost(Post post) {

        Long numOfPostsSubtractOne = post.getAttraction().getNumOfPosts() - 1;
        post.getAttraction().setNumOfPosts(numOfPostsSubtractOne);
        postRepository.delete(post);
    }


    public Post verifyClientId(Long clientId, Long postId) {
        Post post = findPost(postId);

        if (!post.getMember().getMemberId().equals(clientId)) {
            throw new BusinessLogicException(ExceptionCode.CLIENT_IS_NOT_EQUAL);
        }

        return post;
    }

    public boolean votePost(Member member, Post post) {
        // 좋아요를 누른적이 있나?
        Optional<PostLikes> likes = postLikesRepository.findByMemberAndPost(member, post);

        // 좋아요를 이미 눌렀다면
        if (likes.isPresent()) {
            // 좋아요 데이터를 삭제하고
            postLikesRepository.delete(likes.get());
            // post의 likes를 하나 감소시킨다
            post.setLikes(post.getLikes() - 1);
            // 지금은 좋아요를 누르지 않은 상태라는것을 반환한다.
            return false;
        }
        // 좋아요를 누르지 않았으면
        else {
            // 좋아요 데이터를 생성하고
            postLikesRepository.save(PostLikes.builder().post(post).member(member).build());
            // post의 likes를 하나 증가시킨다.
            post.setLikes(post.getLikes() + 1);
            // 지금은 좋아요를 누른 상태라는것을 반환한다.
            return true;
        }
    }
    
    public boolean isVoted(long memberId, long postId) {
        return postLikesRepository.findByMemberIdAndPostId(memberId, postId).isPresent();
    }

}
