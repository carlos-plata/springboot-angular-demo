package com.app.springbootangulardemo.service;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.springbootangulardemo.dto.PostRequest;
import com.app.springbootangulardemo.dto.PostResponse;
import com.app.springbootangulardemo.exception.PostNotFoundException;
import com.app.springbootangulardemo.exception.SpringRedditException;
import com.app.springbootangulardemo.exception.SubredditNotFoundException;
import com.app.springbootangulardemo.mapper.PostMapper;
import com.app.springbootangulardemo.model.Post;
import com.app.springbootangulardemo.model.Subreddit;
import com.app.springbootangulardemo.model.User;
import com.app.springbootangulardemo.repository.PostRepository;
import com.app.springbootangulardemo.repository.SubredditRepository;
import com.app.springbootangulardemo.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PostService {
	private final PostRepository postRepository;
	private final SubredditRepository subredditRepository;
	private final UserRepository userRepository;
	private final AuthorizacionService authorizacionService;
	private final PostMapper postMapper;

	public void save(PostRequest postRequest) {
		Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
				.orElseThrow(() -> new SubredditNotFoundException(postRequest.getSubredditName()));
		postRepository.save(postMapper.map(postRequest, subreddit, authorizacionService.getCurrentUser()));
	}

	@Transactional(readOnly = true)
	public List<PostResponse> getAllPosts() {
		return postRepository.findAll().stream().map(postMapper::mapToDto).collect(toList());
	}

	@Transactional(readOnly = true)
	public PostResponse getPost(Long id) {
		Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id.toString()));
		return postMapper.mapToDto(post);
	}

	@Transactional(readOnly = true)
	public List<PostResponse> getPostsBySubreddit(Long subredditId) {
		Subreddit subreddit = subredditRepository.findById(subredditId)
				.orElseThrow(() -> new SubredditNotFoundException(subredditId.toString()));
		List<Post> posts = postRepository.findAllBySubreddit(subreddit);
		return posts.stream().map(postMapper::mapToDto).collect(toList());
	}

	@Transactional(readOnly = true)
	public List<PostResponse> getPostsByUsername(String username) {
		User user = userRepository.findByUserName(username).orElseThrow(() -> new SpringRedditException(username));
		return postRepository.findByUser(user).stream().map(postMapper::mapToDto).collect(toList());
	}

}
