package org.wellspin.backend.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.wellspin.backend.entity.User;
import org.wellspin.backend.repository.UsersRepository;

@RestController
@RequestMapping("/users")

public class UsersController extends BaseController {

	Logger log = LoggerFactory.getLogger(UsersController.class);
	
	@Autowired
	UsersRepository usersRepository; 
			
	@GetMapping("/getAllUsers")
	public List<User> getAllUsers() {
		return usersRepository.findAll();
	}
	
	@GetMapping("/createUser")
	public ResponseEntity<?> createUser(
				@RequestParam("name") String name, 
				@RequestParam("email") String email,
				@RequestParam("phone") String phone) {
		User user = new User();
		user.setName(name);
		user.setEmail(email);
		user.setPhone(phone);
		user = usersRepository.save(user);
		if (user != null) {
			return new ResponseEntity<String>(user.getId(), HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	@PostMapping("/updateUser")
	public ResponseEntity<?> updateUser(@RequestBody User user) {
		Optional<User> userData = usersRepository.findById(user.getId());
		if (userData.isPresent()) {
			user = usersRepository.save(user);
			if (user != null) {
				return new ResponseEntity<String>(user.getId(), HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	
	@GetMapping("/findById")
	public ResponseEntity<?> findById(@RequestParam("userId") String userId) {
		Optional<User> userResponse = usersRepository.findById(userId);
		if (userResponse.isPresent()) {
			User user = userResponse.get();
			return new ResponseEntity<User>(user, HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	
	@GetMapping("/existsById")
	public boolean existsById(@RequestParam("userId") String userId) {
		return usersRepository.existsById(userId);
	}

	@GetMapping("/deleteById")
	public void deleteById(@RequestParam("userId") String userId) {
		usersRepository.deleteById(userId);
	}
	 
	@GetMapping("/findByName")
	public ResponseEntity<?> findByName(@RequestParam("name") String name) {
		User user = usersRepository.findByName(name);
		if (user != null) {
			return new ResponseEntity<User>(user, HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

}