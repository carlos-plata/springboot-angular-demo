package com.app.springbootangulardemo.service;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.springbootangulardemo.dto.SubredditDto;
import com.app.springbootangulardemo.exception.SubredditNotFoundException;
import com.app.springbootangulardemo.mapper.SubredditMapper;
import com.app.springbootangulardemo.model.Subreddit;
import com.app.springbootangulardemo.repository.SubredditRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {
	private final SubredditRepository subredditRepository;
	private final SubredditMapper subredditMapper;

	@Transactional(readOnly = true)
	public List<SubredditDto> getAll() {
		return subredditRepository.findAll().stream().map(subredditMapper::mapSubredditToDto).collect(toList());
	}

	@Transactional
	public SubredditDto save(SubredditDto subredditDto) {
		Subreddit subreddit = subredditRepository.save(subredditMapper.mapDtoToSubreddit(subredditDto));
		subredditDto.setId(subreddit.getId());
		return subredditDto;
	}

	public SubredditDto getSubreddit(Long id) {
		Subreddit subreddit = subredditRepository.findById(id)
				.orElseThrow(() -> new SubredditNotFoundException("Subreddit not found with id -" + id));
		return subredditMapper.mapSubredditToDto(subreddit);
	}

}
