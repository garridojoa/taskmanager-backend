package com.zagalabs.taskmanager.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zagalabs.taskmanager.exceptions.BadRequestException;
import com.zagalabs.taskmanager.exceptions.ResourceNotFoundException;
import com.zagalabs.taskmanager.model.User;
import com.zagalabs.taskmanager.repository.UserRepository;

/**
 * User Rest Api Controller
 * @author jgarrido
 */
@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	UserRepository userRepo;

	@GetMapping("/{userName}")
	public ResponseEntity<User> getUser(@PathVariable String userName) {
		if (StringUtils.isEmpty(userName)) {
			throw new BadRequestException(String.format("User retrieve failed. NULL or empty values are allowed: %s", userName));
		} else {
			return new ResponseEntity<User>(userRepo.findById(userName).map(user -> {
				return user;
			}).orElseThrow(() -> new ResourceNotFoundException(String.format("User %s does not exist.", userName))), HttpStatus.OK);
		}
	}

	@PutMapping("/add")
	public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
		if (user.getUserName() == null) {
			throw new BadRequestException("Failed creating user: should have valid username.");
		}
		if (user.getPassword() == null) {
			throw new BadRequestException("Failed creating user: should have valid password.");
		}

		User createdUser = null;
		try {
			createdUser = userRepo.save(user);
		} catch (Exception e) {
			throw new BadRequestException("User add failed: " + e.getMessage());
		}
		return new ResponseEntity<User>(createdUser, HttpStatus.CREATED);
	}
}
