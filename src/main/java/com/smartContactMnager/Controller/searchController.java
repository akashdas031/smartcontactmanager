package com.smartContactMnager.Controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.smartContactMnager.Dao.contactRepository;
import com.smartContactMnager.Dao.userRepository;
import com.smartContactMnager.Entities.contact;
import com.smartContactMnager.Entities.user;

@RestController
public class searchController {

	@Autowired
	private userRepository userRepo;
	
	@Autowired
	private contactRepository contactRepo;
	
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable("query")String query,Principal principal){
		
		
		System.out.println(query);
		
		user userByEmail = this.userRepo.getUserByEmail(principal.getName());
		
		List<contact> contact = this.contactRepo.findByCnameContainingAndUsers(query, userByEmail);
		
		return ResponseEntity.ok(contact);
	}
}
