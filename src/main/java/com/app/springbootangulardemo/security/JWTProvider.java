package com.app.springbootangulardemo.security;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.annotation.PostConstruct;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.app.springbootangulardemo.exception.SpringRedditException;

import io.jsonwebtoken.Jwts;

import static io.jsonwebtoken.Jwts.parser;

@Service
public class JWTProvider {

	private KeyStore keyStore;

	@PostConstruct
	public void init() {
		try {
			keyStore = KeyStore.getInstance("JKS");
			keyStore.load(getClass().getResourceAsStream("/my-reddit-clone.jks"), "secret".toCharArray());
		} catch (NoSuchAlgorithmException | CertificateException | IOException | KeyStoreException e) {
			throw new SpringRedditException("Exception while loading keystore: " + e.getMessage());
		}
	}

	public String generateToken(Authentication authentication) {
		org.springframework.security.core.userdetails.User principal = (User) authentication.getPrincipal();
		return Jwts.builder().setSubject(principal.getUsername()).signWith(getPrivateKey()).compact();
	}

	private PrivateKey getPrivateKey() {
		try {
			return (PrivateKey) keyStore.getKey("my-reddit-clone", "secret".toCharArray());
		} catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException e) {
			throw new SpringRedditException("Exception while retrieving private key from keystore: " + e.getMessage());
		}
	}

	private PublicKey getPublicKey() {
		try {
			return keyStore.getCertificate("my-reddit-clone").getPublicKey();
		} catch (KeyStoreException e) {
			throw new SpringRedditException("Exception while retrieving public key from keystore: " + e.getMessage());
		}
	}

	public boolean validateToken(String jwtToken) {
		parser().setSigningKey(getPublicKey()).parseClaimsJws(jwtToken);
		return true;
	}

	public String getUsernameFromJwtToken(String jwtToken) {
		return parser().setSigningKey(getPublicKey()).parseClaimsJws(jwtToken).getBody().getSubject();
	}
}
