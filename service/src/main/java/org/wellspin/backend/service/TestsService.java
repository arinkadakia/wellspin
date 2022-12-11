package org.wellspin.backend.service;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wellspin.backend.entity.Test;
import org.wellspin.backend.entity.Session;
import org.wellspin.backend.repository.TestsRepository;
import org.wellspin.backend.entity.SessionResponse;

@Service
public class TestsService {

	@Autowired
	TestsRepository testsRepository;
	
	@Autowired
	LocationsService locationsService;
	
	Logger log = LoggerFactory.getLogger(TestsService.class);
		
	/*
	 * Check what test is eligible for this session. Return the test that is eligible
	 */
	Integer getEligibleTestForThisSession(Session session) {
		Integer eligibleTest = null;
		// get all responses in the session
		Map<Integer, SessionResponse> responses = session.getResponses();
		if (responses != null && !responses.isEmpty()) {
			// check if the responses includes an answer to the first question yet...
			Integer firstQuestionId = 1;
			SessionResponse response = responses.get(firstQuestionId);
			if (response != null) {
				log.info("*** Found a response for the questionId: " + firstQuestionId);
				// there is a response for this question by the user in the session
				Integer[] answerIds = response.getAnswerIds();
				// ASSERT response.getAnswerInput == ""
				if (answerIds != null && answerIds.length > 0 && answerIds[0] == 1) {
					// children's test => surveyId = 1
					eligibleTest = 1;
							
				} else if (answerIds != null && answerIds.length > 0 && answerIds[0] == 2) {
					// adults test => surveyId = 2
					eligibleTest = 2;
				} else {
					log.info("*** Error in the response for questionId: " + firstQuestionId);
				}
			}
		}
		return eligibleTest;
	}
	
	/*
	 * For each test in the testsArr, check if this user is positive or negative for the test
	 * Set test.testresultscore to -1 (negative) or 1 (positive). Set test.testresulttext = "<blah>"
	 * Return the tests result as a Test[]
	 */
	public Test evaluateTestForThisSession(Session session, Integer testId) {
		Test testResult = null;
		if (testId != null && testId > 0) {
			switch (testId) {
				case 1:
					testResult = evaluateUserForChildrensTest(session);
					break;
					
				case 2:
					testResult = evaluateUserForAdultsTest(session);
					break;
					
				default:
					break;
			}
		}
		return testResult;
	}
	
	/*
	 * Check if this child is positive or negative for the test
	 * Set test.testresultscore to -1 (negative) or 1 (positive)
	 * Set test.testresulttext = "<blah>"
	 * Return the test result as Test
	 */
	public Test evaluateUserForChildrensTest(Session session) {
		Test testResult = null;
		Integer posResponse = 0;
		
		if (session != null) {
			// Initialize posResp = 0;
			// For questionIds = {2 - 28}, if session.getResponses.getQuestionId().answerIds[0] > 0 
			// 		=> posResponse = posResponse + session.getResponses.getQuestionId().answerIds[0] - 1;
			// If posResp
			//     < 40, score = -1, text = "Low ADHD score"
			//     40-59, score = -1, text = "Average ADHD score"
			// 	   60-64, score = 1, text = "High Average ADHD score"
			//     65-69, score = 1, text = "Elevated ADHD score"
			//     70-90, score = 1, text = "Very elevated ADHD score"
			
			testResult = testsRepository.findById(1);  // testId for children's test = 1
			if (testResult != null) {
				Map<Integer, SessionResponse> sessionResponses = session.getResponses();
				if (sessionResponses != null && sessionResponses.size() > 0) {
					for (Map.Entry<Integer, SessionResponse> entry : sessionResponses.entrySet()) {
						SessionResponse sessionResponse = entry.getValue();
						if (sessionResponse != null) {
							Integer questionId = sessionResponse.getQuestionId();
							if (questionId >= 2 && questionId <= 28) {
								Integer[] answerIds = sessionResponse.getAnswerIds();
								if (answerIds != null && answerIds.length > 0) {
									posResponse = posResponse + answerIds[0] - 1;
								}
							}
						}
					}
					
				}

				if (posResponse < 40) {
					testResult.setTestresultscore(-1);
					testResult.setTestresulttext("Low ADHD");
				} else if (posResponse >= 40 && posResponse <=59) {
					testResult.setTestresultscore(-1);
					testResult.setTestresulttext("Average ADHD");					
				} else if (posResponse >= 60 && posResponse <= 64) {
					testResult.setTestresultscore(1);
					testResult.setTestresulttext("High Average ADHD");					
				} else if (posResponse >= 65 && posResponse <= 69) {
					testResult.setTestresultscore(1);
					testResult.setTestresulttext("Elevated ADHD");					
				} else if (posResponse >= 70) {
					testResult.setTestresultscore(1);
					testResult.setTestresulttext("Very elevated ADHD");					
				}
			}
		}
		return testResult;
	}
	
	/*
	 * Check if this adult is positive or negative for the test
	 * Set test.testresultscore to -1 (negative) or 1 (positive)
	 * Set test.testresulttext = "<blah>"
	 * Return the test result as Test
	 */
	public Test evaluateUserForAdultsTest(Session session) {
		Test testResult = null;
		Integer posResponse = 0;
		
		if (session != null) {
			// Initialize posResp = 0;
			// For questionIds = 29 - 31, if session.getResponses.getQuestionId().answerIds[0] >= 3 => posResp++
			// For questionIds = 32 - 34, if session.getResponses.getQuestionId().answerIds[0] >= 4 => posResp++
			// If posResp >=4, score = 1, text = "Positive ADHD score", else score = 0, text = "Negative ADHD score"
			
			testResult = testsRepository.findById(2); // testId for adults test = 2
			if (testResult != null) {
				Map<Integer, SessionResponse> sessionResponses = session.getResponses();
				if (sessionResponses != null && sessionResponses.size() > 0) {
					for (Map.Entry<Integer, SessionResponse> entry : sessionResponses.entrySet()) {
						SessionResponse sessionResponse = entry.getValue();
						if (sessionResponse != null) {
							Integer questionId = sessionResponse.getQuestionId();
							if (questionId >= 29 && questionId <= 31) {
								Integer[] answerIds = sessionResponse.getAnswerIds();
								if (answerIds != null && answerIds.length > 0) {
									if (answerIds[0] >= 3) {
										posResponse = posResponse + 1;
									}
								}
							} else if (questionId >= 32 && questionId <= 34) {
								Integer[] answerIds = sessionResponse.getAnswerIds();
								if (answerIds != null && answerIds.length > 0) {
									if (answerIds[0] >= 4) {
										posResponse = posResponse + 1;
									}
								}
							}
						}
					}
				}

				if (posResponse >= 4) {
					testResult.setTestresultscore(1);
					testResult.setTestresulttext("Based on your answers, "
							+ "your symptoms are highly consistent with ADHD in adults "
							+ "and further investigation is warranted.");
				} else {
					testResult.setTestresultscore(-1);
					testResult.setTestresulttext("Based on your answers, "
							+ "your symptoms are NOT consistent with ADHD in adults.");					
				} 
			}
			
		}
		return testResult;
	}
	
}
