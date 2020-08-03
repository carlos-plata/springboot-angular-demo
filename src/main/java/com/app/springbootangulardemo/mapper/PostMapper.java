package com.app.springbootangulardemo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.app.springbootangulardemo.dto.PostRequest;
import com.app.springbootangulardemo.dto.PostResponse;
import com.app.springbootangulardemo.model.Post;
import com.app.springbootangulardemo.model.Subreddit;
import com.app.springbootangulardemo.model.User;
import com.app.springbootangulardemo.model.Vote;
import com.app.springbootangulardemo.model.VoteType;
import com.app.springbootangulardemo.repository.CommentRepository;
import com.app.springbootangulardemo.repository.VoteRepository;
import com.app.springbootangulardemo.service.AuthorizacionService;
import com.github.marlonlom.utilities.timeago.TimeAgo;

import static com.app.springbootangulardemo.model.VoteType.DOWNVOTE;
import static com.app.springbootangulardemo.model.VoteType.UPVOTE;

import java.util.Optional;

@Mapper(componentModel = "spring")
public abstract class PostMapper {
	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private VoteRepository voteRepository;
	@Autowired
	private AuthorizacionService authorizationService;

	@Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
	@Mapping(target = "description", source = "postRequest.description")
	@Mapping(target = "subreddit", source = "subreddit")
	@Mapping(target = "voteCount", constant = "0")
	@Mapping(target = "user", source = "user")
	public abstract Post map(PostRequest postRequest, Subreddit subreddit, User user);

	@Mapping(target = "id", source = "postId")
	@Mapping(target = "subredditName", source = "subreddit.name")
	@Mapping(target = "userName", source = "user.username")
	@Mapping(target = "commentCount", expression = "java(commentCount(post))")
	@Mapping(target = "duration", expression = "java(getDuration(post))")
	@Mapping(target = "upVote", expression = "java(isPostUpVoted(post))")
	@Mapping(target = "downVote", expression = "java(isPostDownVoted(post))")
	public abstract PostResponse mapToDto(Post post);

	Integer commentCount(Post post) {
		return commentRepository.findByPost(post).size();
	}

	String getDuration(Post post) {
		return TimeAgo.using(post.getCreatedDate().toEpochMilli());
	}

	boolean isPostUpVoted(Post post) {
		return checkVoteType(post, UPVOTE);
	}

	boolean isPostDownVoted(Post post) {
		return checkVoteType(post, DOWNVOTE);
	}

	private boolean checkVoteType(Post post, VoteType voteType) {
		if (authorizationService.isLoggedIn()) {
			Optional<Vote> voteForPostByUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post,
					authorizationService.getCurrentUser());
			return voteForPostByUser.filter(vote -> vote.getVoteType().equals(voteType)).isPresent();
		}
		return false;
	}

}
