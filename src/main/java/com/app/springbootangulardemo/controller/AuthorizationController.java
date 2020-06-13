package com.app.springbootangulardemo.controller;

import static org.springframework.http.HttpStatus.OK;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.springbootangulardemo.dto.RegisterRequest;
import com.app.springbootangulardemo.service.AuthorizacionService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthorizationController {

	private final AuthorizacionService authorizacionService;

	@PostMapping("/signup")
	public ResponseEntity signup(@RequestBody RegisterRequest registerRequest) {
		authorizacionService.signup(registerRequest);
		return new ResponseEntity(OK);
	}

}
