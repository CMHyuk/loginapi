package com.example.memberapi.repository;

import com.example.memberapi.domain.Post;
import com.example.memberapi.dto.request.post.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}
