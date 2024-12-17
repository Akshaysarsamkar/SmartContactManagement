package com.scm.Dao;




import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import com.scm.Entities.Contact;

public interface ContactReposity extends JpaRepository<Contact, Integer> {

	public org.springframework.data.domain.Page<Contact> findByUsersId(int userId,Pageable pageable);

}
