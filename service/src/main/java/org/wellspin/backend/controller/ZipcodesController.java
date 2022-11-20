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
import org.wellspin.backend.entity.Zipcode;
import org.wellspin.backend.repository.ZipcodesRepository;

@RestController
@RequestMapping("/zipcodes")

public class ZipcodesController extends BaseController {
	Logger log = LoggerFactory.getLogger(ZipcodesController.class);
	
	@Autowired
	ZipcodesRepository zipcodesRepository; 

	@GetMapping("/getAllZipcodes")
	public List<Zipcode> getAllZipcodes() {
		return zipcodesRepository.findAll();
	}
	
	@PostMapping("/createZipcode")
	public ResponseEntity<?> createZipcode(@RequestBody Zipcode zipcode) {
		zipcode = zipcodesRepository.save(zipcode);
		if (zipcode != null) {
			try {
				return new ResponseEntity<Integer>(zipcode.getId(), HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	
	@PostMapping("/updateZipcode")
	public ResponseEntity<?> updateZipcode(@RequestBody Zipcode zipcode) {		
		Zipcode zipcodeData;
		try {
			zipcodeData = zipcodesRepository.findById(zipcode.getId());		
			if (zipcodeData != null) {
				zipcode = zipcodesRepository.save(zipcode);
				if (zipcode != null) {
					return new ResponseEntity<Integer>(zipcode.getId(), HttpStatus.OK);
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	@GetMapping("/findById")
	public ResponseEntity<?> findById(@RequestParam("zipcodeId") Integer zipcodeId) {
		Zipcode zipcode = zipcodesRepository.findById(zipcodeId);
		if (zipcode != null) {
			return new ResponseEntity<Zipcode>(zipcode, HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	@GetMapping("/existsById")
	public boolean existsById(@RequestParam("zipcodeId") Integer zipcodeId) {
		return zipcodesRepository.existsById(zipcodeId);
	}

	@GetMapping("/deleteById")
	public void deleteById(@RequestParam("zipcodeId") Integer zipcodeId) {
		zipcodesRepository.deleteById(zipcodeId);
	}
	
	@GetMapping("/findByZip")
	public ResponseEntity<?> findByZip(@RequestParam("zip") Integer zip) {
		Zipcode zipcode = zipcodesRepository.findByZip(zip);
		if (zipcode != null) {
			return new ResponseEntity<Zipcode>(zipcode, HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

}
