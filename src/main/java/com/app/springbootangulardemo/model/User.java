package com.app.springbootangulardemo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import static javax.persistence.GenerationType.SEQUENCE;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

	@Id
	@GeneratedValue(strategy = SEQUENCE)
	private Long userId;
	@NotBlank(message = "Username is required")
	private String userName;
	@NotBlank(message = "Password is required")
	private String password;
	@Email
	@NotEmpty(message = "Email is required")
	private String email;
	private Instant created;
	private boolean enabled;
}
