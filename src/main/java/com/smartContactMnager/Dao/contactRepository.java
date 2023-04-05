package com.smartContactMnager.Dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smartContactMnager.Entities.contact;
import com.smartContactMnager.Entities.user;

public interface contactRepository extends JpaRepository<contact,Integer> {

	//Pegination ...
	//contact per page
	//current page 	
	@Query("from contact as d where d.users.id=:userId")
	public Page<contact> findContactsByUser(@Param("userId")int userId,Pageable page);
	
	@Query("select COUNT(c) from contact c where c.users.id=:id")
	long countById(@Param("id")Integer id);
	
	//Search Functionality
	public List<contact> findByCnameContainingAndUsers(String name,user user);
}
