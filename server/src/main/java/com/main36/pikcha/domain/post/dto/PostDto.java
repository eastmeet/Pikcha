package com.main36.pikcha.domain.post.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.util.List;

public class PostDto {
    @Data
    public static class Post{
        @NotBlank(message = "not allowed null values and spaces.")
        private String postTitle;
        private List<String> postHashTags;
        private List<String> postContents;
        private List<MultipartFile> postImageFiles;
    }

    @Data
    public static class Patch{
        private String postTitle;
        private List<String> postHashTags;
        private List<String> postContents;
        private List<MultipartFile> postImageFiles;
        private List<String> deleteUrls;
    }

}
