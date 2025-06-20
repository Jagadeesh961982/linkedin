package com.jagadeesh.linkedin.posts_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jagadeesh.linkedin.posts_service.entity.Post;

@Repository
public interface PostsRepository extends JpaRepository<Post,Long>{

    List<Post> findByUserId(Long userId);
}
