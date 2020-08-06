package com.app.springbootangulardemo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.springbootangulardemo.dto.CommentsDto;
import com.app.springbootangulardemo.service.CommentService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/comments/")
@AllArgsConstructor
public class CommentsController {

	private final CommentService commentService;

	@PostMapping
	public ResponseEntity<Void> createComment(@RequestBody CommentsDto commentsDto) {
		commentService.save(commentsDto);
		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}

	@GetMapping("by-post/{postId}")
	public ResponseEntity<List<CommentsDto>> getAllCommenstForPost(@RequestParam("postId") Long postId) {
		return ResponseEntity.status(HttpStatus.OK).body(commentService.getAllCommenstForPost(postId));
	}

	@GetMapping("by-user/{userName}")
	public ResponseEntity<List<CommentsDto>> getAllCommentsForUser(@RequestParam("userName") String userName) {
		return ResponseEntity.status(HttpStatus.OK).body(commentService.getAllCommentsForUser(userName));
	}
}
