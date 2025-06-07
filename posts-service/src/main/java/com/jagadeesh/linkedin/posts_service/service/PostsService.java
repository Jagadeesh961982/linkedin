package com.jagadeesh.linkedin.posts_service.service;

import java.util.List;

import com.jagadeesh.linkedin.posts_service.dto.PostCreateRequestDto;
import com.jagadeesh.linkedin.posts_service.dto.PostDto;

public interface PostsService {

    PostDto createPost(PostCreateRequestDto postDto);

    PostDto getPostById(Long postId);

    List<PostDto> getAllPostsOfUser(Long userId);
}
