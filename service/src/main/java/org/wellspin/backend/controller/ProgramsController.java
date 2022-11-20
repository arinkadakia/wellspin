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
import org.wellspin.backend.entity.Program;
import org.wellspin.backend.repository.ProgramsRepository;
import org.wellspin.backend.service.ProgramsService;

@RestController
@RequestMapping("/programs")

public class ProgramsController extends BaseController {

	Logger log = LoggerFactory.getLogger(ProgramsController.class);
	
	@Autowired
	ProgramsRepository programsRepository; 
	
	@Autowired
	ProgramsService programsService;
			
	@GetMapping("/getAllPrograms")
	public List<Program> getAllPrograms() {
		return programsRepository.findAll();
	}
	
	@GetMapping ("/getTotalProgramsCount")
	public Integer getTotalProgramsCount() {
		Integer totalProgramsCount = Integer.valueOf(0);
		
		List<Program> programs = getAllPrograms();
		if (programs != null && !programs.isEmpty()) {
			totalProgramsCount = Integer.valueOf(programs.size());
		}
		
		return totalProgramsCount;
	}
	
	@PostMapping("/createProgram")
	public ResponseEntity<?> createProgram(@RequestBody Program program) {
		program = programsRepository.save(program);
		if (program != null) {
			try {
				return new ResponseEntity<Integer>(program.getId(), HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	
	@PostMapping("/updateProgram")
	public ResponseEntity<?> updateProgram(@RequestBody Program program) {
		Program programData;
		try {
			programData = programsRepository.findById(program.getId());		
			if (programData != null) {
				program = programsRepository.save(program);
				if (program != null) {
					return new ResponseEntity<Integer>(program.getId(), HttpStatus.OK);
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	@GetMapping("/findById")
	public ResponseEntity<?> findById(@RequestParam("programId") Integer programId) {
		Program program = programsRepository.findById(programId);
		if (program != null) {
			return new ResponseEntity<Program>(program, HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	@GetMapping("/existsById")
	public boolean existsById(@RequestParam("programId") int programId) {
		return programsRepository.existsById(programId);
	}

	@GetMapping("/deleteById")
	public void deleteById(@RequestParam("programId") int programId) {
		programsRepository.deleteById(programId);
	}

	@GetMapping("/findByLocationId")
	public List<Program> findByLocationId(@RequestParam("locationId") int locationId) {
		return programsRepository.findByLocationid(locationId);
	}

}
