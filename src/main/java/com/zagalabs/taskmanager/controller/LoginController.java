package com.zagalabs.taskmanager.controller;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.servlet.ServletException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zagalabs.taskmanager.repository.TaskRepository;
import com.zagalabs.taskmanager.repository.UserRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;

@RestController
public class LoginController {
	@Value("${jwt.secret}")
	private String secret;

	@Autowired
	TaskRepository taskRepo;
 
	@Autowired
	private UserRepository userService;
 
	@PostMapping(value = "/login", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> login(@RequestParam @Valid final String username, @RequestParam @Valid final String password) throws ServletException {
		final boolean successLogin = userService.findById(username).map(user -> {
			return user.getPassword().equals(password);
		}).orElse(false);
		
		if (successLogin) {
			final Instant now = Instant.now();
			final String jwt = Jwts.builder()
					.setSubject(username)
					.setIssuedAt(Date.from(now))
					.setExpiration(Date.from(now.plus(1, ChronoUnit.DAYS)))
					.signWith(SignatureAlgorithm.HS256, TextCodec.BASE64.encode(secret))
					.compact();
			return new ResponseEntity<>(jwt, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}
}