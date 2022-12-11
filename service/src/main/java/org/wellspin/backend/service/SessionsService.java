package org.wellspin.backend.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.wellspin.backend.controller.TestController;
import org.wellspin.backend.entity.Location;
import org.wellspin.backend.entity.Test;
import org.wellspin.backend.entity.Session;
import org.wellspin.backend.entity.SessionResponse;
import org.wellspin.backend.repository.LocationsRepository;
import org.wellspin.backend.repository.SessionsRepository;

@Service
public class SessionsService {
	
	@Autowired
	SessionsRepository sessionsRepository;
	
	@Autowired
	TestsService testsService;
	
	@Autowired
	TestController testsController;
	
	@Autowired
	LocationsRepository locationsRepository;
	
	Logger log = LoggerFactory.getLogger(SessionsService.class);
	
	public ResponseEntity<?> addToSession(
			String sessionId, 
			Integer surveyId,
			Integer questionId,
			Integer[] answerIdInts,
			String answerInput) {
		Optional<Session> sessionData = sessionsRepository.findById(sessionId);
		if (sessionData.isPresent()) {
			Session session = sessionData.get();
			session.setSurveyId(surveyId);
			SessionResponse response = new SessionResponse(questionId, answerIdInts, answerInput);
			session.addResponse(response.getQuestionId(), response);
			session = sessionsRepository.save(session);
			if (session != null) {
				return new ResponseEntity<String>(session.getId(), HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(null, HttpStatus.OK);
	}	
	
	/*
	 * Evaluate this user for all tests so far...
	 */
	public Test evaluateTest(Session session) {		
		/*
		 * Fetch the relevant test for this user  
		 * Return test that:
		 *  - this user is eligible for
		 *  - along with the status of the test (testresultscore)
		 *  - and the positive and negative test result description (testresulttext, positiveresulttext, negativeresulttext)
		 */	
		Integer testId = testsService.getEligibleTestForThisSession(session);
		if (testId != null) {				
			Test evaluatedTest = testsService.evaluateTestForThisSession(session, testId);
			if (evaluatedTest != null) {
				return evaluatedTest;
			}
		}
		return null;	
	}
	
	int getLocationIdFromSessionId(String sessionId) {
		
		int locationId = -1;
		if (sessionId != null && !sessionId.isEmpty()) {
			// get surveyId from sessionId
			Optional<Session> sessionData = sessionsRepository.findById(sessionId);
			if (sessionData.isPresent()) {
				Session session = sessionData.get();
				if (session != null) {
					Integer surveyId = session.getSurveyId();
					if (surveyId != null && surveyId > 0) {
						// get locationId from surveyId
						Location location = locationsRepository.findBySurveyid(surveyId);
						if (location != null) {
							locationId = location.getId();
						}
					}
				}
			}
		}
		return locationId;
	}
	
	public List<Test> getAllTestsBySessionId(String sessionId) {	
		List<Test> tests = null;
		
		if (sessionId != null && !sessionId.isEmpty()) {
			// get locationId from this sessionId
			int locationId = getLocationIdFromSessionId(sessionId);
			
			if (locationId > 0) {
				// get list of tests from locationId
				tests = testsController.findByLocationId(locationId);
			}
		}
		
		return tests;
	}
}
