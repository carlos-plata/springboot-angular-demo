package com.app.springbootangulardemo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.springbootangulardemo.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUserName(String username);
}
