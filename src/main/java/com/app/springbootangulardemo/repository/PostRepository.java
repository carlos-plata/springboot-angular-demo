package com.app.springbootangulardemo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.springbootangulardemo.model.Post;
import com.app.springbootangulardemo.model.Subreddit;
import com.app.springbootangulardemo.model.User;

public interface PostRepository extends JpaRepository<Post, Long> {
	List<Post> findAllBySubreddit(Subreddit subreddit);

	List<Post> findByUser(User user);
}