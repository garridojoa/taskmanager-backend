package com.zagalabs.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zagalabs.taskmanager.model.User;

/**
 * Repository to handle User records.
 * @author jgarrido
 */
public interface UserRepository extends JpaRepository<User, String> {

}
