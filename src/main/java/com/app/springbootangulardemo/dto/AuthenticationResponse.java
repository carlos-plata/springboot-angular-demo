package com.app.springbootangulardemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationResponse {

	private String authenticationToken;
	private String userName;
}
