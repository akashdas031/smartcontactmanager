package com.smartContactMnager.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smartContactMnager.Entities.user;

public interface userRepository extends JpaRepository<user, Integer>{

	@Query("select u from user u where u.email=:email")
	public user getUserByEmail(@Param("email")String email);
	
	
}
