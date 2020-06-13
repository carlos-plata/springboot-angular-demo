package com.app.springbootangulardemo.service;

import static java.time.Instant.now;

import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.springbootangulardemo.dto.RegisterRequest;
import com.app.springbootangulardemo.model.User;
import com.app.springbootangulardemo.model.VerificationToken;
import com.app.springbootangulardemo.repository.UserRepository;
import com.app.springbootangulardemo.repository.VerificationTokenRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class AuthorizacionService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final VerificationTokenRepository verificationTokenRepository;

	@Transactional
	public void signup(RegisterRequest registerRequest) {
		User user = new User();
		user.setUserName(registerRequest.getUserName());
		user.setEmail(registerRequest.getEmail());
		user.setPassword(registerRequest.getPassword());
		user.setCreated(now());
		user.setEnabled(false);
		userRepository.save(user);
		String token = generateVerificationToken(user);
	}

	private String encodePassword(String password) {
		return passwordEncoder.encode(password);
	}

	public String generateVerificationToken(User user) {
		String token = UUID.randomUUID().toString();
		VerificationToken verificationToken = new VerificationToken();
		verificationToken.setToken(token);
		verificationToken.setUser(user);
		verificationTokenRepository.save(verificationToken);
		return token;
	}

}
