package com.scm.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.scm.Dao.ContactReposity;
import com.scm.Dao.UserRepository;
import com.scm.Entities.Contact;
import com.scm.Entities.User;
import com.scm.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository repository;

	@Autowired
	private ContactReposity contactReposity;

	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {
		String name = principal.getName();
		User user = repository.findByEmail(name);
		model.addAttribute("user", user);
	}

	// dasshboard
	@RequestMapping("/index")
	public String dashboard(Model model, Principal principal) {
		model.addAttribute("title", "Dashboard - Smart Contact Manager");
		return "normal/user_dashboard";
	}

	// contact details
	@GetMapping("/addcontact")
	public String openAddContact(Model model) {
		model.addAttribute("contact", new Contact());
		model.addAttribute("title", "Add Contact - Smart Contact Manager");
		return "/normal/contact";
	}

	// Add contact
	@PostMapping("/process-contact")
	public String addContactData(@ModelAttribute Contact contact, @RequestParam("cimage") MultipartFile file,
			Principal principal, Model m, HttpSession session) {
		try {
			String username = principal.getName();
			User user = repository.findByEmail(username);

			// File processing logic
			if (file.isEmpty()) {
				System.out.println("File is empty");
				contact.setImage("banner.png"); // Optional: Set default image
			} else {
				String uniqueFileName = UUID.randomUUID().toString()+ file.getOriginalFilename();
				File staticImgDir = new ClassPathResource("/static/img").getFile();
				if (!staticImgDir.exists()) {
					staticImgDir.mkdirs();
				}
				Path filePath = Paths.get(staticImgDir.getAbsolutePath() + File.separator + uniqueFileName);
				Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
				contact.setImage(uniqueFileName);
				session.setAttribute("message", new Message("Your contact is added successfully!", "alert-success"));

			}

			// Associate contact with user
			contact.setUsers(user);
			user.getContacts().add(contact);

			repository.save(user);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error occure");
			session.setAttribute("message", new Message("Something went wrong. Try again!", "alert-danger"));

			// return "error-page"; // Replace with actual error view
		}
		return "/normal/contact"; // Redirect to a success view

	}

	@GetMapping("/contact/{page}")
	public String ViewContact(@PathVariable("page") Integer page, Model m, Principal p) {

		String name = p.getName();
		User user = repository.findByEmail(name);

		Pageable pageable = PageRequest.of(page, 5);
		Page<Contact> contacts = contactReposity.findByUsersId(user.getId(), pageable);

		m.addAttribute("contacts", contacts);
		m.addAttribute("currentpage", page);
		m.addAttribute("totalpages", contacts.getTotalPages());

//		List<Contact> contacts = user.getContacts();
//		
//		m.addAttribute("contact-list", contacts);

		m.addAttribute("title", "ViewContact-Samrt Contact Manager");
		return "/normal/show_contact";
	}

	// show specific contact details
	@GetMapping("/{cid}/contact")
	public String showContactDetails(@PathVariable("cid") int cid, Model m, Principal p) {

		Optional<Contact> byId = contactReposity.findById(cid);
		Contact contact = byId.get();

		String name = p.getName();

		User user = repository.findByEmail(name);

		System.out.println(user.getId());
		System.out.println(contact.getUsers().getId());

		if (user.getId() == contact.getUsers().getId()) {
			m.addAttribute("contact", contact);
			m.addAttribute("title", contact.getName());
		}
		return "normal/show_contactdetails";
	}

	@GetMapping("/delete/{cid}")
	public String DeleteContact(@PathVariable("cid") int id, Model m, Principal p, HttpSession httpSession) {

		Contact contact = contactReposity.findById(id).get();
		int i = contact.getUsers().getId();
		System.out.println(contact.getUsers().getId());

//		contact.setUsers(null);
		User user = repository.findByEmail(p.getName());

		user.getContacts().remove(contact);

		if (user.getId() == i) {
			contactReposity.delete(contact);
			httpSession.setAttribute("message", new Message("Contact Deleted Succesfully......", "alert-success"));

		} else {
			httpSession.setAttribute("message", new Message("Not Allow to delete this contact......", "alert-danger"));

		}
		return "redirect:/user/contact/0";
	}

	// Update Contact
	@PostMapping("/update-contact/{cid}")
	public String UpdateContactNumber(Model model, @PathVariable("cid") int id) {

		model.addAttribute("title", "Update Contact Details");

		Contact contact = contactReposity.findById(id).get();

		model.addAttribute("contact", contact);

		return "normal/update-form";
	}

	// update Contact form
	@PostMapping("/process-update")
	public String UpdateContactHandler(@ModelAttribute Contact contact, @RequestParam("cimage") MultipartFile file,
			Model m, HttpSession session, Principal p) {

		try {

			if (file.isEmpty()) {

			}

			User user = repository.findByEmail(p.getName());

			System.out.println("1111111111111");

			contact.setUsers(user);

			System.out.println("222222222222222222222");

			contactReposity.save(contact);

			System.out.println("333333333333333333333");

		} catch (Exception e) {
			// TODO: handle exception
		}

//		System.out.println("Contact name " + contact.getName());
//        System.out.println("COntact id " + contact.getCid());
		return "";
	}

	// Your Profile
	@GetMapping("/profile")
	public String Profile(Model m) {

		m.addAttribute("title", "User Profile - Smart Contact Manager");

		return "/normal/profile";

	}

}
