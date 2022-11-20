package org.wellspin.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wellspin.backend.entity.Condition;
import org.wellspin.backend.entity.Session;
import org.wellspin.backend.entity.SessionResponse;
import org.wellspin.backend.repository.ConditionsRepository;

@Service
public class ConditionsService {
	
	@Autowired
	ConditionsRepository conditionsRepository; 
	
	Logger log = LoggerFactory.getLogger(ConditionsService.class);
	
	/*
	 * Process all conditions for this session. Return a list of condition_ids that are satisfied.
	 */
	public Integer[] processAllConditionsForThisSession(Session session) {
	
		//log.info("------- processAllConditionsForThisSession called ---------");
		List<Integer> processedConditionsArr = new ArrayList<Integer>();
		
		// get all responses in the session
		Map<Integer, SessionResponse> responses = session.getResponses();
		if (responses != null && !responses.isEmpty()) {
			// get all conditions
			List<Condition> conditions = conditionsRepository.findAll();
			if (conditions!= null && !conditions.isEmpty()) {
				
				//log.info("Conditions #: " + conditions.size() + ", Session responses: " + responses.size());
				// iterate over each condition to see if a response associated with it exists 
				// and if so whether it is true for this user
				for (int i=0; i<conditions.size(); i++) {
					Condition condition = conditions.get(i);
					Integer questionId = condition.getQuestionid();
					if (questionId != null && questionId > 0) {
						//log.info("Checking for condition: " + condition.getId() + ", questionId: " + condition.getQuestionid());

						// check if the responses includes an answer to this question yet...
						SessionResponse response = responses.get(questionId);
						if (response != null) {
							//log.info("*** Found a response for the questionId: " + questionId);
							// there is a response for this question by the user in the session
							Integer[] answerIds = response.getAnswerIds();
							String answerInput = response.getAnswerInput();
							
							// let's compare the user answer with the condition
							Integer conditionAnswer = -1;
							String conditionOperator = "";
							if (condition.getAnswer().startsWith("answer.")) {
								conditionOperator = "equals";
								conditionAnswer = Integer.valueOf(condition.getAnswer().substring(7).trim());
							} else if (condition.getAnswer().startsWith("gt")) {
								conditionOperator = "gt";
								conditionAnswer = Integer.valueOf(condition.getAnswer().substring(3).trim());
							} else if (condition.getAnswer().startsWith("gte")) {
								conditionOperator = "gte";
								conditionAnswer = Integer.valueOf(condition.getAnswer().substring(4).trim());
							} else if (condition.getAnswer().startsWith("lt")) {
								conditionOperator = "lt";
								conditionAnswer = Integer.valueOf(condition.getAnswer().substring(3).trim());							
							} else if (condition.getAnswer().startsWith("lte")) {
								conditionOperator = "lte";
								conditionAnswer = Integer.valueOf(condition.getAnswer().substring(4).trim());								
							} else {
								// ERROR
							}
							
							//log.info("Operator: " + conditionOperator + ", Answer: " + conditionAnswer);
							if (conditionOperator != "" && conditionAnswer != -1) {
								Integer answerInputInt = -1;
								switch(conditionOperator) {
									case "equals" :
										if (answerIds[0] == conditionAnswer) {
											processedConditionsArr.add(condition.getId());
										}
										break;
									case "gt" :
										answerInputInt = Integer.valueOf(answerInput);
										if (answerInputInt > conditionAnswer) {
											processedConditionsArr.add(condition.getId());
										}
										break;
									case "gte" :
										answerInputInt = Integer.valueOf(answerInput);
										if (answerInputInt >= conditionAnswer) {
											processedConditionsArr.add(condition.getId());
										}
										break;
									case "lt" : 
										answerInputInt = Integer.valueOf(answerInput);
										if (answerInputInt < conditionAnswer) {
											processedConditionsArr.add(condition.getId());
										}
										break;
									case "lte" :
										answerInputInt = Integer.valueOf(answerInput);
										if (answerInputInt <= conditionAnswer) {
											processedConditionsArr.add(condition.getId());
										}
										break;
									default:
										// ERROR
										break;
								}
								//log.info("processedConditionsArr: " + processedConditionsArr);
							}
						}
					}
					
				}		
				
				// return the list of conditionId(s) that are true
				if (processedConditionsArr != null && processedConditionsArr.size() > 0) {
					Integer[] processedConditionsArrInt = new Integer[processedConditionsArr.size()];
					processedConditionsArrInt = processedConditionsArr.toArray(processedConditionsArrInt);
					return processedConditionsArrInt;
				}
			}						
		}

		return null;
	}
	
}
