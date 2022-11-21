package org.wellspin.backend.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wellspin.backend.entity.Question;
import org.wellspin.backend.entity.QuestionResponse;
import org.wellspin.backend.entity.Session;
import org.wellspin.backend.entity.SessionResponse;
import org.wellspin.backend.entity.Survey;
import org.wellspin.backend.repository.LocationsRepository;
import org.wellspin.backend.repository.QuestionsRepository;
import org.wellspin.backend.repository.SessionsRepository;
import org.wellspin.backend.repository.SurveysRepository;

@Service
public class QuestionsService {

	@Autowired
	QuestionsRepository questionsRepository; 
	
	@Autowired
	SurveysRepository surveysRepository;
	
	@Autowired
	LocationsRepository locationsRepository;
	
	@Autowired
	SessionsRepository sessionsRepository;
	
	@Autowired
	SessionsService sessionsService;


	Logger log = LoggerFactory.getLogger(QuestionsService.class);

	
	/* 
	 * Parse answerInput. Remove unnecessary ""
	 */
	public String parseAnswerInput(String answerInput) {
		String parsedAnswerInput = answerInput.replaceAll("\"", "");
		return parsedAnswerInput;
	}
	
	/*
	 * This method returns the surveyId from the age that the user entered in the first question
	 */
	public Integer getSurveyIdFromAge(Integer ageInt) {
		
		Integer surveyIdInt = null;		
		if (ageInt != null) {
			if (ageInt == 1) {
				surveyIdInt = Integer.valueOf(1);
			} else if (ageInt == 2) {
				surveyIdInt = Integer.valueOf(2);
			}
		}
		log.info("Returning surveyId: " + surveyIdInt);
		return surveyIdInt;
	}
	
	/*
	 * This method returns the next question based on the answer choice to the last question. 
	 * It returns null if there was no next question for the answer choice. In that case,
	 * use the method, getDefaultNextQuestion() to get the next question
	 */
	Question getNextQuestionBasedOnAnswerChoice(Integer lastQuestionId, Integer[] lastAnswerIds) {
		
		Question nextQuestion = null;
		
		// If lastAnswerIds == non-0, check the nextId first. 
		// If nextId[lastAnswerIds[0]] is not -1, that's the nextQuestionId				
		// else, if nextid == -1, use the default nextQuestion (from survey collection).
		
		// Validate lastAnswerIds
		if (lastAnswerIds != null && lastAnswerIds.length > 0 && lastAnswerIds[0] > 0) {		
			// (1) Go to question collection, get the document for lastQuestionId
			Question lastQuestion = questionsRepository.findById(lastQuestionId);
			
			// (2) Parse the nextId array for lastAnswerIndex[0], see if there is a nextid.X value
			if (lastQuestion != null) {
				Integer[] nextIdArray = lastQuestion.getNextid();
				if (nextIdArray.length > lastAnswerIds[0]) {	
					if (nextIdArray[lastAnswerIds[0]] != -1) {
						// (3) If not empty, set the next question id to that value and return the question
						Integer nextQuestionId = nextIdArray[lastAnswerIds[0]];
						// (4) Go to the question collection, return the document for nextid.Y 
						nextQuestion = questionsRepository.findById(nextQuestionId);
						log.info("Returning question with questionId: " + nextQuestionId);
					} else {
						// There's no next question id specified for this answer choice. 
					}
				}
			}
		}
		return nextQuestion;
	}
	
	/*
	 * Parses the comma-separated string of answer ids (e.g. "-1", "0", "1, 2", "", etc.)
	 * Returns an array of integers parsed from the string. If the string was empty, method 
	 * returns an empty array.
	 */
	public Integer[] getLastAnswerIdInts(String lastAnswerIds) {	
		log.info("getLastAnswerIdInts: " + lastAnswerIds);

		if (lastAnswerIds != null) {
			lastAnswerIds = lastAnswerIds.replaceAll("\"", "");
			if (!lastAnswerIds.isEmpty()) {
				String[] ids = lastAnswerIds.split(",");		
				if (ids.length > 0) {
					Integer[] parsedAnswerIds = new Integer[ids.length];
					for (int i = 0; i < ids.length; i++) {
						parsedAnswerIds[i] = Integer.valueOf(ids[i]);
					}
					log.info("getLastAnswerIdInts: Returning " + parsedAnswerIds);
					return parsedAnswerIds;
				} 
			}
		}
		log.info("getLastAnswerIdInts: Returning null");
		return null;
	}
	
	/*
	 * This method checks in the survey collection for the default next question given surveyId and lastQuestionId
	 * Method returns the next question (Question) if it successfully finds it or null if it can't.
	 */
	Question getDefaultNextQuestion(Integer surveyId, Integer lastQuestionId) {		
		Question nextQuestion = null;		
		if (surveyId != null && lastQuestionId != null) {
			
			// Get the surveyId document			
			Survey surveyDoc = surveysRepository.findById(surveyId);		
			
			// Parse the nextId array for the lastQuestionId index, return the nextid.Y value
			Integer[] nextIdArray = surveyDoc.getNextid();
			if (nextIdArray.length > lastQuestionId) {
				Integer nextQuestionId = nextIdArray[lastQuestionId];
				nextQuestion = questionsRepository.findById(nextQuestionId);
				log.info("Returning question with questionId = " + nextQuestionId);
			}
		}		
		return nextQuestion;
	}
	
	/*
	 * Possible scenarios:
	 *   (1) Call to get the first question. Don't know surveyId yet.
	 *   (2) Call to get the second question (after the age question). Don't know surveyId yet. 
	 *         In this case, lastAnswerIds = "1" or "2"
	 *   (3) Call to get question 3 - N. We know the surveyId.
	 *         In this case, lastAnswerIds = {"0" or "1, 2, 3" or "3"}
	 *         If 0, use the default nextQuestion (from survey collection). 
	 *         If non-0, check the nextid first. If that is not -1, that's the nextQuestionId
	 *               else, nextid -1 use the default nextQuestion (from survey collection).
	 */
	public Question getNextQuestion(
			String sessionId, 
			Integer surveyId, 
			Integer lastQuestionId, 
			String lastAnswerIds, 
			String lastAnswerInput) {
		
		Question nextQuestion = null;
		boolean firstQuestion = false;
		
		Integer[] lastAnswerIdInts = getLastAnswerIdInts(lastAnswerIds);
		
		if (surveyId != null && lastQuestionId != null && lastAnswerIdInts != null &&
			surveyId.intValue() == -1 && lastQuestionId.intValue() == -1 && lastAnswerIdInts.length == 1 &&
			lastAnswerIdInts[0] == -1) {
			
			// (1) Call to get the first (age) question. Don't know surveyId yet.
			nextQuestion = questionsRepository.findById(1);
			firstQuestion = true;
			
		} else if (surveyId != null && lastQuestionId != null && lastAnswerIdInts != null &&
				   surveyId.intValue() == -1 && lastQuestionId.intValue() == 1 && lastAnswerIdInts.length == 1 &&
				   (lastAnswerIdInts[0] == 1 ||lastAnswerIdInts[0] == 2) ) {
			
			// (2) Call to get the second question (after the age question). Don't know surveyId yet. 
			
			// lastAnswerIdInts[0] must be 1 (child) or 2 (adult)
			if (lastAnswerIdInts != null) {
				Integer ageInt = lastAnswerIdInts[0];
				
				// get surveyId from age
				surveyId = getSurveyIdFromAge(ageInt);
				if (surveyId != null) {						
					// Check if there's a next question based on last answer choice 
					nextQuestion = getNextQuestionBasedOnAnswerChoice(lastQuestionId, lastAnswerIdInts);
					if (nextQuestion == null) {
						// Get the default next question
						nextQuestion = getDefaultNextQuestion(surveyId, lastQuestionId);
					}
				}
			}				
			
		} else if (surveyId != null && lastQuestionId != null && lastAnswerIdInts != null && 
			surveyId.intValue() != -1 && lastQuestionId.intValue() != -1 && lastAnswerIdInts[0] != -1) {	
			
			//  (3) Call to get question 3 - N. We know the surveyId.
			if (lastAnswerIdInts[0] == 0) {				
				// If lastAnswerIndex == 0, this is a question with a text input answer
				// Get the default nextQuestion (from survey collection)
				nextQuestion = getDefaultNextQuestion(surveyId, lastQuestionId);
				
			} else {
				// If lastAnswerIndex == non-0, check if there's a next question based on last answer choice. 
				nextQuestion = getNextQuestionBasedOnAnswerChoice(lastQuestionId, lastAnswerIdInts);
				if (nextQuestion == null) {
					// Get the default next question
					nextQuestion = getDefaultNextQuestion(surveyId, lastQuestionId);
				}
			}
		} else {
			// Get the default next question
			nextQuestion = getDefaultNextQuestion(surveyId, lastQuestionId);
		}
		
		if (sessionId != null && !sessionId.isEmpty() && !firstQuestion) {
			// Add the previous answer to the session
			String lastAnswerInputStr = parseAnswerInput(lastAnswerInput);
			sessionsService.addToSession(sessionId, surveyId, lastQuestionId, lastAnswerIdInts, lastAnswerInputStr);
		}
		
		return nextQuestion;
	}
	
	/*
	 * This method returns the question with id=questionId along with the answer to that question in this session
	 * if sessionId != null and the answer was saved using /sessions/addToSession
	 */
	public QuestionResponse getQuestionResponse(String sessionId, Integer questionId) {
		
		QuestionResponse questionResponse = null;
		
		Question question = questionsRepository.findById(questionId);
		if (question != null) {
			questionResponse = new QuestionResponse();
			questionResponse.setQuestion(question);
			
			// Check to see if this question has an associated response in this session
			if (sessionId != null) {
				Optional<Session> sessionResponseData = sessionsRepository.findById(sessionId);
				if (sessionResponseData.isPresent()) {
					Session session = sessionResponseData.get();
					if (session != null) {
						SessionResponse sResponse = session.getResponse(questionId);
						if (sResponse != null) {
							questionResponse.setResponse(sResponse);
						}
					}
				}
			}		
		}
		return questionResponse;
	}

}
