package com.smartContactMnager.Controller;

import java.util.Random;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.smartContactMnager.Dao.EmailService;
import com.smartContactMnager.Dao.userRepository;
import com.smartContactMnager.Entities.user;
import com.smartContactMnager.helper.Message;

@Controller
public class homeController {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private userRepository userRepo;
	
	@Autowired
	private EmailService emailService;
	
	@GetMapping("/")
	public String homePage(Model model) {
		
		model.addAttribute("title","SmartContactManager");
		return "home";
	}
	@GetMapping("/about")
	public String about(Model model) {
		
		model.addAttribute("title","aboutPage");
		return "about";
	}
	@GetMapping("/signup")
	public String sign(Model model) {
		
		model.addAttribute("title","SignUp ");
		model.addAttribute("user",new user());
		return "signup";
	}
	@GetMapping("/signin")
	public String login(Model model) {
		
		model.addAttribute("title","Login page ");
		return "login";
	}
	
	/* Handeler for registeruing user */
	
	@RequestMapping(value="/do_register",method=RequestMethod.POST)
	public String registerUser(@Valid @ModelAttribute("user")user user,BindingResult results ,@RequestParam(value="agreement",defaultValue="false")boolean agreement,Model model,HttpSession session ){
		
		
		try {
			
			if(!agreement) {
				System.out.println("you have not agreed terms and condition");
				throw new Exception("you have not agreed terms and condition");
			}
			if(results.hasErrors()) {
				System.out.println("Error"+user.toString());
				model.addAttribute("user",user);
				return "signup";
			}
			
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImagePath("default.png");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			System.out.println("Agreement"+agreement);
			System.out.println("user"+user);
			user result = this.userRepo.save(user);
			
			model.addAttribute("user",new user());

			session.setAttribute("message", new Message("Registered successfully !!","alert-success"));
			
			return "signup";
			
		}catch(Exception e) {
			e.printStackTrace();
			session.setAttribute("message", new Message("Something went wrong"+e.getMessage(),"alert-danger"));
		
			return "signup";
		}
		
	}
	
	@RequestMapping(value="/forgotPassword")
	public String forgotPassword() {
		return "forgotPassword";
	}
	Random r=new Random(1000);
	
	@PostMapping("/sendOTP")
	public String sendOTP(@RequestParam("email") String email,HttpSession session){
		
		
		int otp = r.nextInt(999999);
		System.out.println("Email :"+email);
		System.out.println("OTP :"+otp);
		String subject="``this";
		String message=""
				+ "</div style='border:1px solid #e2e2e2;padding:20px'>"
				+ "<h1>"
				+"OTP is"
				+ "<b>"+otp
				+"</b>"
				+"</h1>"
				+"</div>";
		boolean sendEmail = this.emailService.sendEmail(message, email,subject);
		if(sendEmail) {
			session.setAttribute("myotp", otp);
			session.setAttribute("email", email);
			return "verification";
		}else {
			session.setAttribute("message", "Check your Email Id..");
			return "forgot_email";
		}
		
	}
	
	@PostMapping("/verifyOtp")
	public String verifyOtp(@RequestParam("otp")Integer otp,HttpSession session) {
		Integer myotp=(Integer) session.getAttribute("myotp");
		String email=(String)session.getAttribute("email");
		
		if(myotp==otp) {
			return "password_Change";
		}else {
			session.setAttribute("message", "You have entered wrong otp..");
			return "verification";
		}
	
		
	}
	

}
