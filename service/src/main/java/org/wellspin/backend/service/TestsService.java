package org.wellspin.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wellspin.backend.entity.Test;
import org.wellspin.backend.entity.Session;
import org.wellspin.backend.repository.TestsRepository;

@Service
public class TestsService {

	@Autowired
	TestsRepository testsRepository;
	
	@Autowired
	LocationsService locationsService;
	
	Logger log = LoggerFactory.getLogger(TestsService.class);
	
	/*
	 * Returns testArray from testIds
	 */
	public Test[] getTestFromIds(Integer[] testIds) {
		List<Test> testList = new ArrayList<Test>();
	
		if (testIds != null && testIds.length > 0) {
			for (int i=0; i<testIds.length; i++) {
				Test pgm = testsRepository.findById(testIds[i]);
				testList.add(pgm);
			}
			
			// return the list of test(s) that are true
			if (testList != null && testList.size() > 0) {
				Test[] testsArr= new Test[testList.size()];
				testsArr = testList.toArray(testsArr);
				return testsArr;
			}
		}	
		return null;
	}
	
	/*
	 * Process all tests for this session. Return a list of eligible test_ids.
	 * e.g. processedConditionsArr = [1, 9, 12, 19, 20, 24, 7] <= matching condition_ids
	 * e.g. processedConditionsArr = null <= no matching condition_ids
	 */
	Integer[] processAllTestsForThisSession(Session session, Integer[] processedConditionsArr) {
		List<Integer> eligibleTestsList = null;
		if (session != null && processedConditionsArr != null && session.getSurveyId() != null) {
			Integer locationId = locationsService.getLocationIdFromSurveyId(session.getSurveyId());
		
			// get all tests for the locationId
			List<Test> testsList = testsRepository.findByLocationid(locationId);
			
			// for each test:
			for (int i=0; i<testsList.size(); i++) {
				Test pgm = testsList.get(i);
			
				// get the eligibility criteria (e.g. "1 AND (2 OR 3 OR 4) AND ( 5 OR 6 OR 7 OR 8 OR (9 AND 10) ) AND 11")
				String eligibilityCriteria = pgm.getEligibility();
	
				// check if the conditions meet the eligibility criteria
				boolean eligible = checkforTestEligibility(eligibilityCriteria, processedConditionsArr);
				
				// if eligible, add the test to the eligibleTests list
				if (eligible) {
					if (eligibleTestsList == null) {
						eligibleTestsList = new ArrayList<Integer>();
					}
					eligibleTestsList.add(pgm.getId());
				}
			}
		}
		
		if (eligibleTestsList != null && eligibleTestsList.size() > 0) {
			Integer[] eligibleTestsArr = new Integer[eligibleTestsList.size()];
			eligibleTestsArr = eligibleTestsList.toArray(eligibleTestsArr);
			return eligibleTestsArr;
		} else {
			return null;
		}
	}
	
	/*
	 * Checks for test eligibility: 	
	 *    - eligibilityCriteria = "1 AND (2 OR 3 OR 4) AND ( 5 OR 6 OR 7 OR 8 OR (9 AND 10) ) AND 11"
	 *    - processedConditionsArr = [1, 9, 12, 19, 20, 24, 7] <= matching condition_ids	
	 */
	public boolean checkforTestEligibility(String eligibilityCriteria, Integer[] processedConditionsArr) {		
		boolean eligible = false;	
		List<String> matches = new ArrayList<>();
		String regex = "\\([^()]*\\)";
		Pattern p = Pattern.compile(regex);
		
		while (eligibilityCriteria.contains("(")) {
			Matcher m = p.matcher(eligibilityCriteria);		
			while (m.find()) {
			    String fullMatch = m.group();
			    //log.info("fullMatch: " + fullMatch);
			    matches.add(fullMatch);
			    Integer processedExprValue = processExpression(fullMatch.substring(1, fullMatch.length()-1), processedConditionsArr);
			    eligibilityCriteria = eligibilityCriteria.replace(fullMatch, processedExprValue.toString());
			    //log.info("Updated inputStr: " + eligibilityCriteria);
			}
		}
		//log.info("Final processing of: " + eligibilityCriteria);
		Integer finalExpressionEval = processExpression(eligibilityCriteria, processedConditionsArr);
		//log.info("finalExpressionEval: " + finalExpressionEval);
		
		if (finalExpressionEval > 0) {
			eligible = true;
		}		
		return eligible;
	}
	
	/*
	 * Processes a partial or complete eligibility expression of conditions such as "2 OR 3 OR 4" 
	 * or "9 AND 10" and returns 0 or 1 depending on whether those conditions are fulfilled by conditionsArr
	 * e.g. conditionsArr = [1, 9, 12, 19, 20, 24, 7] <= matching condition_ids
	 * ASSERT: "expression" does not contain a sub-expression
	 * ASSERT: "expression" does not contain both operators "||" and "&"
	 */
	public Integer processExpression(String expression, Integer[] conditionsArr) {
		Integer processedValue = -1;	
		//log.info("Expression: " + expression);
		if (expression.contains("OR")) {
			String[] exprStrArr = expression.split("OR");
			if (exprStrArr != null && exprStrArr.length > 0) {
				Integer[] exprIntArr = new Integer[exprStrArr.length];
				for (int i=0; i<exprStrArr.length; i++) {
					//log.info("exprStrArr[" + i + "]: " + exprStrArr[i].trim());
					exprIntArr[i] = Integer.parseInt(exprStrArr[i].trim());
				}
				// OPERATOR = OR => if ANY component in exprIntArr matches a condition, processedValue = 1
				processedValue = 0;
				for (int i=0; i<exprIntArr.length; i++) {
					for (int j=0; j<conditionsArr.length; j++) {
						if (exprIntArr[i] == conditionsArr[j]) {
							//log.info("Found an OR condition match, expr component: " + exprIntArr[i] + ", condition: " + conditionsArr[j]);
							processedValue = 1;
							break;
						}
					}
					if (processedValue == 1) {
						break;
					}
				}
			}
		} else if (expression.contains("AND")) {
			String[] exprStrArr = expression.split("AND");
			if (exprStrArr != null && exprStrArr.length > 0) {
				Integer[] exprIntArr = new Integer[exprStrArr.length];
				for (int i=0; i<exprStrArr.length; i++) {
					//log.info("exprStrArr[" + i + "]: " + exprStrArr[i].trim());
					exprIntArr[i] = Integer.parseInt(exprStrArr[i].trim());
				}
				// OPERATOR = AND => if ALL components in exprIntArr have a matching condition, processedValue = 1
				processedValue = 1;
				for (int i=0; i<exprIntArr.length; i++) {
					boolean foundMatch = false;
					for (int j=0; j<conditionsArr.length; j++) {
						if (exprIntArr[i] == conditionsArr[j]) {
							//log.info("Found an AND condition match, expr component: " + exprIntArr[i] + ", condition: " + conditionsArr[j]);
							foundMatch = true;
							break;
						}
					}
					if (!foundMatch) {
						//log.info("Did not find a match for expr component: " + exprIntArr[i]);
						processedValue = 0;
						break;
					}
				}
			}
		} else {
			// No operator - must be a single number
			processedValue = 0;
			Integer exprInt = Integer.parseInt(expression.trim());
			if (exprInt != null && exprInt > 0) {
				for (int j=0; j<conditionsArr.length; j++) {
					if (exprInt == conditionsArr[j]) {
						//log.info("Found a condition match, expr: " + exprInt + ", condition: " + conditionsArr[j]);
						processedValue = 1;
						break;
					}
				}
			}
		}	
		return processedValue;
	}

}
