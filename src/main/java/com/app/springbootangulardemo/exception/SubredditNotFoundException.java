package com.app.springbootangulardemo.exception;

public class SubredditNotFoundException extends RuntimeException {
	public SubredditNotFoundException(String message) {
		super(message);
	}
}
