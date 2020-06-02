package com.app.springbootangulardemo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.springbootangulardemo.model.Comment;
import com.app.springbootangulardemo.model.Post;
import com.app.springbootangulardemo.model.User;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	List<Comment> findByPost(Post post);

	List<Comment> findAllByUser(User user);
}
