package com.scm.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.scm.Entities.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	
	@Query("select U from user U where U.email = :e")
	public User getUserByUserName(@Param("e") String Email );
	
}
