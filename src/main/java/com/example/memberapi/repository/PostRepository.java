package com.example.memberapi.repository;

import com.example.memberapi.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    List<Post> findByTitleContaining(String title);
}
