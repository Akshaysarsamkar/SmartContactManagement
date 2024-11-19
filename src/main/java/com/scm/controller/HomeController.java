package com.scm.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.Dao.UserRepository;
import com.scm.Entities.User;
import com.scm.helper.Message;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

	@Autowired
	private UserRepository repository;

	@GetMapping("/")
	public String home(Model m) {
		m.addAttribute("title", "Home - Smart Contact Manegment");
		return "home";
	}

	
	@GetMapping("/about")
	public String about(Model m) {
		m.addAttribute("About", "Home - Smart Contact Manegment");
		return "about";
	}
	

	@GetMapping("/signup")
	public String Signup(Model m) {
		m.addAttribute("Signup", "Register - Smart Contact Manegment");
		m.addAttribute("user", new User());
		return "signup";
	}
	
	

	// handler for register user
	@PostMapping("/do_register")
	public String registerUser(@Valid @ModelAttribute("user") User users,BindingResult result1,
			@RequestParam(value = "Agreement",defaultValue = "false") boolean agreement,HttpSession session, Model model
			) {
		try {
			if (!agreement) {
				System.out.println("Accept the terms and conditions");
				throw new Exception("You have not agreed to the terms and conditions");
			}
			
			if(result1.hasErrors()) {
				System.out.println("Error : " + result1.toString());
				model.addAttribute("user", users);
			//	System.out.println(result1);
				return "signup";
			}

			users.setRole("ROLE_USER");
			users.setEnable(true);
			users.setImaage("default.jpg");
		
			User result = repository.save(users);
			
			//System.out.println("111111111111111111111111111111111111");
			
			model.addAttribute("user", new User());

			//System.out.println("222222222222222222222222222222222222222222222");

			session.setAttribute("message", new Message("Successfully Registered", "alert-success"));
			
			//System.out.println("333333333333333333333333333333333333333333333");

			return "signup"; // Returns to the signup page
			
			


		} catch (Exception e) {
			e.printStackTrace();
			
		//	System.out.println("111111111111111111111111111111111111");

			model.addAttribute("user", users);

		//	System.out.println("222222222222222222222222222222222222222222222");

			session.setAttribute("message", new Message("Something went wrong: " + e.getMessage(), "alert-danger"));
			
		//	System.out.println("33333333333333333333333333333333333333333333333");

			return "signup"; 
		}
	}
	
}
