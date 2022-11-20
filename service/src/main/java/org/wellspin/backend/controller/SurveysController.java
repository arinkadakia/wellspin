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
import org.wellspin.backend.entity.Survey;
import org.wellspin.backend.repository.SurveysRepository;
import org.wellspin.backend.service.QuestionsService;

@RestController
@RequestMapping("/surveys")

public class SurveysController extends BaseController {

	Logger log = LoggerFactory.getLogger(SurveysController.class);
	
	@Autowired
	SurveysRepository surveysRepository; 
			
	@Autowired
	QuestionsService questionsService;
	
	@GetMapping("/getAllSurveys")
	public List<Survey> getAllSurveys() {
		return surveysRepository.findAll();
	}
	
	@PostMapping("/createSurvey")
	public ResponseEntity<?> createSurvey(@RequestBody Survey survey) {
		survey = surveysRepository.save(survey);
		if (survey != null) {
			try {
				return new ResponseEntity<Integer>(survey.getId(), HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	
	@PostMapping("/updateSurvey")
	public ResponseEntity<?> updateSurvey(@RequestBody Survey survey) {
		Survey surveyData;
		try {
			surveyData = surveysRepository.findById(survey.getId());		
			if (surveyData != null) {
				survey = surveysRepository.save(survey);
				if (survey != null) {
					return new ResponseEntity<Integer>(survey.getId(), HttpStatus.OK);
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	@GetMapping("/findById")
	public ResponseEntity<?> findById(@RequestParam("surveyId") Integer surveyId) {		
		Survey survey = surveysRepository.findById(surveyId);
		if (survey != null) {
			return new ResponseEntity<Survey>(survey, HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	
	@GetMapping("/existsById")
	public boolean existsById(@RequestParam("surveyId") int surveyId) {
		return surveysRepository.existsById(surveyId);
	}

	@GetMapping("/deleteById")
	public void deleteById(@RequestParam("surveyId") int surveyId) {
		surveysRepository.deleteById(surveyId);
	}

}
