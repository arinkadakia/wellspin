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
import org.wellspin.backend.entity.Location;
import org.wellspin.backend.repository.LocationsRepository;

@RestController
@RequestMapping("/locations")

public class LocationsController extends BaseController {
	
	Logger log = LoggerFactory.getLogger(LocationsController.class);
	
	@Autowired
	LocationsRepository locationsRepository; 

	@GetMapping("/getAllLocations")
	public List<Location> getAllLocations() {
		return locationsRepository.findAll();
	}
	
	@PostMapping("/createLocation")
	public ResponseEntity<?> createLocation(@RequestBody Location location) {
		location = locationsRepository.save(location);
		if (location != null) {
			try {
				return new ResponseEntity<Integer>(location.getId(), HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	
	@PostMapping("/updateLocation")
	public ResponseEntity<?> updateLocation(@RequestBody Location location) {
		Location locationData;
		try {
			locationData = locationsRepository.findById(location.getId());		
			if (locationData != null) {
				location = locationsRepository.save(location);
				if (location != null) {
					return new ResponseEntity<Integer>(location.getId(), HttpStatus.OK);
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	@GetMapping("/findById")
	public ResponseEntity<?> findById(@RequestParam("locationId") Integer locationId) {
		Location location = locationsRepository.findById(locationId);
		if (location != null) {
			return new ResponseEntity<Location>(location, HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	@GetMapping("/existsById")
	public boolean existsById(@RequestParam("locationId") int locationId) {
		return locationsRepository.existsById(locationId);
	}

	@GetMapping("/deleteById")
	public void deleteById(@RequestParam("locationId") int locationId) {
		locationsRepository.deleteById(locationId);
	}
	
	@GetMapping("/findByNameAndType")
	public ResponseEntity<?> findByNameAndType(@RequestParam("name") String name, @RequestParam("type") String type) {
		Location location = locationsRepository.findByNameAndType(name, type);
		if (location != null) {
			return new ResponseEntity<Location>(location, HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	
	@GetMapping("/findBySurveyId")
	public ResponseEntity<?> findBySurveyId(@RequestParam("surveyId") Integer surveyId) {
		Location location =  locationsRepository.findBySurveyid(surveyId);
		if (location != null) {
			return new ResponseEntity<Location>(location, HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
}
