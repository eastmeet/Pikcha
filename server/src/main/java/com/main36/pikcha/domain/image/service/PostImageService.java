package com.main36.pikcha.domain.image.service;

import com.main36.pikcha.domain.image.entity.PostImage;
import com.main36.pikcha.domain.image.repository.PostImageRepository;
import com.main36.pikcha.global.config.S3Service;
import com.main36.pikcha.global.exception.BusinessLogicException;
import com.main36.pikcha.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostImageService {
    private final PostImageRepository postImageRepository;
    private final String dirName = "images";
    private final S3Service s3Service;

    public PostImage createPostImage(MultipartFile file) throws IOException {
        PostImage postImage = new PostImage();
        // 현재 날짜, 시간을 기준으로 생성한 10자리 숫자를 생성
         int dateTimeInteger = (int) (new Date().getTime()/1000);

        // 생성된 날짜 + 원래 파일 이름 = 생성되는 파일 이름
        String imageFileName = dateTimeInteger+file.getOriginalFilename();
        String imageUrl = s3Service.upload(file, dirName, imageFileName);

        postImage.setPostImageFileName(imageFileName);
        postImage.setPostImageUrl(imageUrl);
        return postImageRepository.save(postImage);
    }

    public void deleteOnlyS3Images(List<PostImage> postImages){
        for(PostImage postImage : postImages) {
            PostImage findPostImage = findVerifiedPostImage(postImage.getPostImageId());
            String fileName = findPostImage.getPostImageFileName();
            String filePath = dirName + "/" + fileName;
            s3Service.delete(filePath);
        }
    }

    public void deletePostImagesByUrls(List<String> postImageUrls) {
        for(String postImageUrl : postImageUrls) {
            PostImage findPostImage = findPostImageByUrl(postImageUrl);
            String fileName = findPostImage.getPostImageFileName();
            String filePath = dirName + "/" + fileName;
            s3Service.delete(filePath);
            postImageRepository.delete(findPostImage);
        }
    }

    public PostImage findVerifiedPostImage(long postImageId){
        return postImageRepository.findById(postImageId)
                .orElseThrow(()-> new BusinessLogicException(ExceptionCode.POST_IMAGE_NOT_FOUND));
    }

    public PostImage findPostImageByUrl(String url){
        return postImageRepository.findByPostImageUrl(url)
                .orElseThrow(()->new BusinessLogicException(ExceptionCode.POST_IMAGE_NOT_FOUND));
    }
}
