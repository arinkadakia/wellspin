package org.wellspin.backend.controller;

import java.util.List;

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
import org.wellspin.backend.entity.Test;
import org.wellspin.backend.repository.TestsRepository;
import org.wellspin.backend.service.TestsService;

@RestController
@RequestMapping("/tests")

public class TestController extends BaseController {

	Logger log = LoggerFactory.getLogger(TestController.class);
	
	@Autowired
	TestsRepository testsRepository; 
	
	@Autowired
	TestsService testsService;
			
	@GetMapping("/getAllTests")
	public List<Test> getAllTests() {
		return testsRepository.findAll();
	}
	
	@GetMapping ("/getTotalTestsCount")
	public Integer getTotalTestsCount() {
		Integer totalTestsCount = Integer.valueOf(0);
		
		List<Test> tests = getAllTests();
		if (tests != null && !tests.isEmpty()) {
			totalTestsCount = Integer.valueOf(tests.size());
		}
		
		return totalTestsCount;
	}
	
	@PostMapping("/createTest")
	public ResponseEntity<?> createTest(@RequestBody Test test) {
		test = testsRepository.save(test);
		if (test != null) {
			try {
				return new ResponseEntity<Integer>(test.getId(), HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	
	@PostMapping("/updateTest")
	public ResponseEntity<?> updateTest(@RequestBody Test test) {
		Test testData;
		try {
			testData = testsRepository.findById(test.getId());		
			if (testData != null) {
				test = testsRepository.save(test);
				if (test != null) {
					return new ResponseEntity<Integer>(test.getId(), HttpStatus.OK);
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	@GetMapping("/findById")
	public ResponseEntity<?> findById(@RequestParam("testId") Integer testId) {
		Test test = testsRepository.findById(testId);
		if (test != null) {
			return new ResponseEntity<Test>(test, HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	@GetMapping("/existsById")
	public boolean existsById(@RequestParam("testId") int testId) {
		return testsRepository.existsById(testId);
	}

	@GetMapping("/deleteById")
	public void deleteById(@RequestParam("testId") int testId) {
		testsRepository.deleteById(testId);
	}

	@GetMapping("/findByLocationId")
	public List<Test> findByLocationId(@RequestParam("locationId") int locationId) {
		return testsRepository.findByLocationid(locationId);
	}

}
