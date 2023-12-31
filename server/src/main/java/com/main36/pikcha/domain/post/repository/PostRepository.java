package com.main36.pikcha.domain.post.repository;

import com.main36.pikcha.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAll(Pageable pageable);

    @Query(value= "select p from Post as p where p.attraction.attractionId = :attractionId")
    Page<Post> findAllByAttractionId(long attractionId, Pageable pageable);

    @Query(value= "select p from Post as p where p.attraction.attractionId = :attractionId")
    List<Post> findAllByAttractionId(long attractionId);

    @Query(value = "select p from Post as p where p.attraction.province in :provinces")
    Page<Post> findAllByProvinceIn(List<String> provinces, Pageable pageable);
}
