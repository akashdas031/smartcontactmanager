package com.smartContactMnager.Controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smartContactMnager.Dao.contactRepository;
import com.smartContactMnager.Dao.userRepository;
import com.smartContactMnager.Entities.contact;
import com.smartContactMnager.Entities.user;
import com.smartContactMnager.helper.Message;

@Controller
@RequestMapping(value="/user")
public class myController {
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	private userRepository userRepo;
	
	
	@Autowired
	private contactRepository contactRepo;
	//response method to adding common data response
	
	
	@ModelAttribute
	public void commonData(Model model,Principal principal) {
		
String name = principal.getName();
		
		user userByEmail = this.userRepo.getUserByEmail(name);
		
		System.out.println(userByEmail);
		
		model.addAttribute("user",userByEmail);
		
		
	}
	
	
	@RequestMapping(value="/dashboard")
	public String dashboard(Model model,Principal principal) {
		
		
		return "normal/dashboard";
	}
	
	@GetMapping("/openContact")
	public String openContact(Model model) {
		model.addAttribute("title","add contact");
		model.addAttribute("contact",new contact());
		return "normal/addContact";
	}
	
	/* processing contact form */
	
	@PostMapping("/processContact")
	public String ProcessContact(@Valid@ModelAttribute("contact")contact contact,BindingResult result,@RequestParam("profileImage")MultipartFile file,Model model,Principal principal,HttpSession session) {
		
		try {
		if(result.hasErrors()) {
			model.addAttribute("contact",contact);
			return "normal/addContact";
		}
		String name = principal.getName();
		user userByEmail = this.userRepo.getUserByEmail(name);
		
		if(file.isEmpty()) {
			System.out.println("file is empty");
			contact.setcImagePath("contact.png");
			
		}else {
			//upload the file to the folder
			
			File image = new ClassPathResource("static/image").getFile();
			System.out.println("getting path");
			Path path = Paths.get(image.getAbsolutePath()+File.separator+file.getOriginalFilename());
			System.out.println("configure and save file");
			Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
			System.out.println("Image uploaded successful");
			contact.setcImagePath(file.getOriginalFilename());
			
		}
		
		contact.setUsers(userByEmail);
		userByEmail.getContact().add(contact);
		
		
		
		this.userRepo.save(userByEmail);
		System.out.println(contact);
		System.out.println("contact added successfully");
		model.addAttribute("contact",new contact());
		
		// Success Message
		
		session.setAttribute("message", new Message("Contact added Successfully !!!","success"));
		return "normal/addContact";
		
		}catch(Exception e) {
			e.printStackTrace();
			//Error message
			session.setAttribute("message", new Message("Something went wrong !! Please try again..","danger"));
			return "normal/addContact";
		}
		
	}
	
	/* Show contacts */
	
	@GetMapping("/showContact/{page}")
	//Perpage some specific amount of contact suppose 5
	//current page
	public String showContact(@PathVariable("page") Integer page,Model model,Principal principal) {
		
		String name = principal.getName();
		user data = this.userRepo.getUserByEmail(name);
		
		Pageable of = PageRequest.of(page, 5);
		
	 Page<contact> contacts = this.contactRepo.findContactsByUser(data.getId(),of);
		model.addAttribute("contacts",contacts);
		model.addAttribute("currentPage",page);
		model.addAttribute("totalPages",contacts.getTotalPages());
		
		model.addAttribute("title","Contacts");
		return "normal/showContact";
	}
	/* Show perticular contact details */
	
	@GetMapping("/{cid}/contact")
	public String showPerticulaerContact(@PathVariable("cid") Integer cid,Model model,Principal principal) {
		
		Optional<contact> contactOptional = this.contactRepo.findById(cid);
		
		contact contact = contactOptional.get();
		String name = principal.getName();
		user userByEmail = this.userRepo.getUserByEmail(name);
		if(userByEmail.getId()==contact.getUsers().getId()) {
			
			model.addAttribute("contact",contact);
			model.addAttribute("title",contact.getCname());
		}
		
		
		return "normal/contactDetails";
	}
	
	// DElete contact handler
	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable("cid")Integer cid,Model model,Principal principal,HttpSession session) {
		
		Optional<contact> findById = this.contactRepo.findById(cid);
		
		contact contact = findById.get();
		String name = principal.getName();
		user userByEmail = this.userRepo.getUserByEmail(name);
		
		if(userByEmail.getId() == contact.getUsers().getId()) {
			contact.setUsers(null);
			try {
			File images = new ClassPathResource("static/image").getFile();
			
			File file1=new File(images,contact.getcImagePath());
			file1.delete();
			}catch(Exception e) {
				e.printStackTrace();
			}
		this.contactRepo.delete(contact);
		session.setAttribute("message", new Message("Contact Deleted Successfully","success"));
		}
		return "redirect:/user/showContact/0";
	}
	
	@PostMapping("/update/{cid}")
	public String update(@ModelAttribute("contact")contact contact,@PathVariable("cid")Integer cid,Model model,Principal principal) {
		model.addAttribute("title","update");
		model.addAttribute("contact",new contact());
		
		String name = principal.getName();
		user user = this.userRepo.getUserByEmail(name);
		Optional<contact> findById = this.contactRepo.findById(cid);
		contact contacts = findById.get();
		
		System.out.println(contacts);
		model.addAttribute("contact",contacts);
		return "normal/updateContact";
	}

	
	@PostMapping("/updateContact")
	public String updateContact(@ModelAttribute("contact")contact contact,@RequestParam("profileImage")MultipartFile file,Model model,HttpSession session,Principal principal) {
		
		try {
			contact oldContact = this.contactRepo.findById(contact.getCid()).get();
			
			if(file.isEmpty()) {
				
				contact.setcImagePath(oldContact.getcImagePath());
				
				
			}else {
				/* delete Old Image from the location */
				
				File images = new ClassPathResource("static/image").getFile();
				
				File file1=new File(images,oldContact.getcImagePath());
				file1.delete();
						
				
				
                /* update new image */
				
				File image = new ClassPathResource("static/image").getFile();
				
				Path path = Paths.get(image.getAbsolutePath()+File.separator+file.getOriginalFilename());
				
				Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
				System.out.println("Image name"+file.getOriginalFilename());
				contact.setcImagePath(file.getOriginalFilename());
				
			}
			
			String name = principal.getName();
			user user = this.userRepo.getUserByEmail(name);
			contact.setUsers(user);
			this.contactRepo.save(contact);
			System.out.println(contact.getCid());
			session.setAttribute("message", new Message("Successfully Updated","success"));
			
		}catch(Exception e) {
			
		}
		return "redirect:/user/showContact/0";
	}
	
	/* Dashboard */
	
	@GetMapping("/dashboard")
	public String dashBoard(Principal principal,Model model) {
		
		String name = principal.getName();
		user user = this.userRepo.getUserByEmail(name);
		
		
		 long count = this.contactRepo.countById(user.getId());
		System.out.println(count);
		model.addAttribute("count",count);
		
		return "normal/dashboard";
	}
	
	/* profile Section */
	@GetMapping("/userProfile")
	public String profile(Principal principal,Model model) {
		String name = principal.getName();
		user userByEmail = this.userRepo.getUserByEmail(name);
		
		model.addAttribute("title","User Profile");
		model.addAttribute("contact",userByEmail);
		
		
		return "normal/userProfile";
	}
	
	/* show Update Form */
	
	@PostMapping("/updateUser/{id}")
	public String showUpdateForm(@PathVariable("id")Integer id,Model model,Principal principal) {
		
		String name = principal.getName();
		user userByEmail = this.userRepo.getUserByEmail(name);
		
		
		model.addAttribute("user",userByEmail);
		model.addAttribute("image","default.jpg");
		return "normal/updateUser";
	}
	
	
	
	/* update user */
	
	@PostMapping("/updateUser")
	public String updateUser(@Valid@ModelAttribute("user")user user,BindingResult result,@RequestParam("imagePath")MultipartFile file,Model model,HttpSession session,Principal principal) {
		
		try {
			String name = principal.getName();
			user olduser = this.userRepo.getUserByEmail(name);
			if(file.isEmpty()) {
				
				user.setImagePath(olduser.getImagePath());
				
				
			}else {
				/* delete Old Image from the location */
				
				File images = new ClassPathResource("static/image").getFile();
				
				File file1=new File(images,olduser.getImagePath());
				file1.delete();
						
				
				
                /* update new image */
				
				File image = new ClassPathResource("static/image").getFile();
				
				Path path = Paths.get(image.getAbsolutePath()+File.separator+file.getOriginalFilename());
				
				Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
				System.out.println("Image name"+file.getOriginalFilename());
				user.setImagePath(file.getOriginalFilename());
				
			}
			
			user.setAbout(user.getAbout());
			user.setEmail(user.getEmail());
			user.setName(user.getName());
			
			this.userRepo.save(user);
			System.out.println(user.getId());
			model.addAttribute("user",user);
			session.setAttribute("message", new Message("Successfully Updated","success"));
			
		}catch(Exception e) {
			
		}
		return "redirect:/user/userProfile";
	}
	
	/* Setting section */
	
	@GetMapping("/setting")
	public String setting() {
		return "normal/setting";
	}
	@PostMapping("/change")
	public String changePassword(@ModelAttribute("user")user user,@RequestParam("oldPassword")String pass,@RequestParam("newPassword") String newPass,Principal principal,HttpSession session) {
		
		String name = principal.getName();
	com.smartContactMnager.Entities.user userByEmail = this.userRepo.getUserByEmail(name);
		String password = userByEmail.getPassword();
		System.out.println(pass);
	String pas=encoder.encode(pass);
	System.out.println(pas);
	
	if(encoder.matches(pass, password)) {
		userByEmail.setPassword(encoder.encode(newPass));
		this.userRepo.save(userByEmail);
		session.setAttribute("message", new Message("Password Change Successfully...","success"));
		return "redirect:/user/dashboard";
	}
		
		System.out.println(password);
		session.setAttribute("message", new Message("The Password you have entered doesn't match with old password... ","danger"));
		return "normal/setting";
	}
	
	
}

