package com.app.springbootangulardemo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.springbootangulardemo.model.Post;
import com.app.springbootangulardemo.model.User;
import com.app.springbootangulardemo.model.Vote;

public interface VoteRepository extends JpaRepository<Vote, Long> {
	Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
}
