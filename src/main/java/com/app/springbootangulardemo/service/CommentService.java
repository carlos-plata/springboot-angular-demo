package com.app.springbootangulardemo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.app.springbootangulardemo.dto.CommentsDto;
import com.app.springbootangulardemo.exception.PostNotFoundException;
import com.app.springbootangulardemo.exception.SpringRedditException;
import com.app.springbootangulardemo.mapper.CommentMapper;
import com.app.springbootangulardemo.model.Comment;
import com.app.springbootangulardemo.model.NotificationEmail;
import com.app.springbootangulardemo.model.Post;
import com.app.springbootangulardemo.model.User;
import com.app.springbootangulardemo.repository.CommentRepository;
import com.app.springbootangulardemo.repository.PostRepository;
import com.app.springbootangulardemo.repository.UserRepository;

import lombok.AllArgsConstructor;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class CommentService {
	private static final String POST_URL = "";
	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final AuthorizacionService authorizacionService;
	private final CommentMapper commentMapper;
	private final CommentRepository commentRepository;
	private final MailContentBuilder mailContentBuilder;
	private final MailService mailService;

	public void save(CommentsDto commentsDto) {
		Post post = postRepository.findById(commentsDto.getPostId())
				.orElseThrow(() -> new PostNotFoundException(commentsDto.getPostId().toString()));
		Comment comment = commentMapper.map(commentsDto, post, authorizacionService.getCurrentUser());
		commentRepository.save(comment);
		sendCommentNotification(
				mailContentBuilder.build(post.getUser().getUserName() + " posted a comment on your post." + POST_URL),
				post.getUser());
	}

	private void sendCommentNotification(String message, User user) {
		mailService.sendMail(
				new NotificationEmail(user.getUserName() + " Commented on your post", user.getEmail(), message));
	}

	public List<CommentsDto> getAllCommenstForPost(Long postId) {
		Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId.toString()));
		return commentRepository.findByPost(post).stream().map(commentMapper::mapToDto).collect(toList());
	}

	public List<CommentsDto> getAllCommentsForUser(String userName) {
		User user = userRepository.findByUserName(userName).orElseThrow(() -> new SpringRedditException(userName));
		return commentRepository.findAllByUser(user).stream().map(commentMapper::mapToDto).collect(toList());
	}

}
