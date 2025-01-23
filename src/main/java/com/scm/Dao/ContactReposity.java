package com.scm.Dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.scm.Entities.Contact;
import com.scm.Entities.User;

public interface ContactReposity extends JpaRepository<Contact, Integer> {

	public org.springframework.data.domain.Page<Contact> findByUsersId(int userId, Pageable pageable);

	public List<Contact> findByNameContainingAndUsers(String name, User user);
}
