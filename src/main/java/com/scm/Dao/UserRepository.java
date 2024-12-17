package com.scm.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.scm.Entities.Contact;
import com.scm.Entities.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	
	public User findByEmail(String username);
	
	
	

	
	
}
