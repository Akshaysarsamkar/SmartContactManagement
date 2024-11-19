package com.scm.Dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scm.Entities.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	
}
