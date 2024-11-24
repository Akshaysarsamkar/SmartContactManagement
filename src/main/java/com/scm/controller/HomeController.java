package com.scm.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
	private BCryptPasswordEncoder passwordEncoder;

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
	public String signup(Model m) {
		m.addAttribute("title", "Signup - Smart Contact Manegment");
		m.addAttribute("user", new User());
		return "signup";
	}

	@PostMapping("/do_register")
	public String registerUser(@Valid @ModelAttribute User user, BindingResult result1 , @RequestParam(defaultValue = "false") boolean agreement,
			Model model,HttpSession session) {

		try {

			if (!agreement) {
				//System.out.println("You have not accept term and condition");
				throw new Exception("You have not accept term and condition");
			}

			if(result1.hasErrors()) {
				System.out.println("Error" + result1.toString());
				model.addAttribute("user", user);
				return "signup";
			}
			
			user.setRole("ROLE__USER");
			user.setEnable(true);
			user.setImaage("default.jpg");
			user.setPassword(passwordEncoder.encode(user.getPassword()));

			System.out.println("Agreement " + agreement);
			System.out.println("User " + user);

			User result = repository.save(user);

			model.addAttribute("user", new User());
			session.setAttribute("message", new Message("succefully Register !!","alert-success"));

			return "signup";


		} catch (Exception e) {

			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Somthing want wrong !!" + e.getMessage(),"alert-danger"));
			return "signup";

		}


	}

}
