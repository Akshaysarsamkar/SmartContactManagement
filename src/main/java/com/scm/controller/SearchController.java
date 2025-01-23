package com.scm.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.scm.Dao.ContactReposity;
import com.scm.Dao.UserRepository;
import com.scm.Entities.Contact;
import com.scm.Entities.User;

@RestController
public class SearchController {
	
	@Autowired
	private ContactReposity  contactReposity;
	
	@Autowired
	private UserRepository repository;
	
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable("query") String query , Principal principal){
		System.out.println(query);
		User user = repository.findByEmail(query);
		List<Contact> contacts = this.contactReposity.findByNameContainingAndUsers(query, user);
		return ResponseEntity.ok(contacts);
		
	}
	

}
