package com.app.springbootangulardemo.service;

import static java.time.Instant.now;

import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.springbootangulardemo.dto.RegisterRequest;
import com.app.springbootangulardemo.exception.SpringRedditException;
import com.app.springbootangulardemo.model.NotificationEmail;
import com.app.springbootangulardemo.model.User;
import com.app.springbootangulardemo.model.VerificationToken;
import com.app.springbootangulardemo.repository.UserRepository;
import com.app.springbootangulardemo.repository.VerificationTokenRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.app.springbootangulardemo.util.Constants.ACTIVATION_EMAIL;

@Service
@AllArgsConstructor
@Slf4j
public class AuthorizacionService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final VerificationTokenRepository verificationTokenRepository;
	private final MailContentBuilder mailContentBuilder;
	private final MailService mailService;

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
		String message = mailContentBuilder.build(
				"Thank you for signing up to Spring Reddit Clone, please click on the below url to activate your account: "
						+ ACTIVATION_EMAIL + "/" + token);
		mailService.sendMail(new NotificationEmail("Please Activate your account", user.getEmail(), message));
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

	public void verifyAccount(String token) {
		Optional<VerificationToken> verificationTokenOptional = verificationTokenRepository.findByToken(token);
		verificationTokenOptional.orElseThrow(() -> new SpringRedditException("Invalid Token"));
		fetchUserAndEnable(verificationTokenOptional.get());
	}

	@Transactional
	private void fetchUserAndEnable(VerificationToken verificationToken) {
		String username = verificationToken.getUser().getUserName();
		User user = userRepository.findByUserName(username)
				.orElseThrow(() -> new SpringRedditException("User Not Found with id - " + username));
		user.setEnabled(true);
		userRepository.save(user);

	}

}
