package org.wellspin.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wellspin.backend.entity.Program;
import org.wellspin.backend.entity.Session;
import org.wellspin.backend.repository.ProgramsRepository;

@Service
public class ProgramsService {

	@Autowired
	ProgramsRepository programsRepository;
	
	@Autowired
	LocationsService locationsService;
	
	Logger log = LoggerFactory.getLogger(ProgramsService.class);
	
	/*
	 * Returns programArray from programIds
	 */
	public Program[] getProgramFromIds(Integer[] programIds) {
		List<Program> programList = new ArrayList<Program>();
	
		if (programIds != null && programIds.length > 0) {
			for (int i=0; i<programIds.length; i++) {
				Program pgm = programsRepository.findById(programIds[i]);
				programList.add(pgm);
			}
			
			// return the list of program(s) that are true
			if (programList != null && programList.size() > 0) {
				Program[] programsArr= new Program[programList.size()];
				programsArr = programList.toArray(programsArr);
				return programsArr;
			}
		}	
		return null;
	}
	
	/*
	 * Process all programs for this session. Return a list of eligible program_ids.
	 * e.g. processedConditionsArr = [1, 9, 12, 19, 20, 24, 7] <= matching condition_ids
	 * e.g. processedConditionsArr = null <= no matching condition_ids
	 */
	Integer[] processAllProgramsForThisSession(Session session, Integer[] processedConditionsArr) {
		List<Integer> eligibleProgramsList = null;
		if (session != null && processedConditionsArr != null && session.getSurveyId() != null) {
			Integer locationId = locationsService.getLocationIdFromSurveyId(session.getSurveyId());
		
			// get all programs for the locationId
			List<Program> programsList = programsRepository.findByLocationid(locationId);
			
			// for each program:
			for (int i=0; i<programsList.size(); i++) {
				Program pgm = programsList.get(i);
			
				// get the eligibility criteria (e.g. "1 AND (2 OR 3 OR 4) AND ( 5 OR 6 OR 7 OR 8 OR (9 AND 10) ) AND 11")
				String eligibilityCriteria = pgm.getEligibility();
	
				// check if the conditions meet the eligibility criteria
				boolean eligible = checkforProgramEligibility(eligibilityCriteria, processedConditionsArr);
				
				// if eligible, add the program to the eligiblePrograms list
				if (eligible) {
					if (eligibleProgramsList == null) {
						eligibleProgramsList = new ArrayList<Integer>();
					}
					eligibleProgramsList.add(pgm.getId());
				}
			}
		}
		
		if (eligibleProgramsList != null && eligibleProgramsList.size() > 0) {
			Integer[] eligibleProgramsArr = new Integer[eligibleProgramsList.size()];
			eligibleProgramsArr = eligibleProgramsList.toArray(eligibleProgramsArr);
			return eligibleProgramsArr;
		} else {
			return null;
		}
	}
	
	/*
	 * Checks for program eligibility: 	
	 *    - eligibilityCriteria = "1 AND (2 OR 3 OR 4) AND ( 5 OR 6 OR 7 OR 8 OR (9 AND 10) ) AND 11"
	 *    - processedConditionsArr = [1, 9, 12, 19, 20, 24, 7] <= matching condition_ids	
	 */
	public boolean checkforProgramEligibility(String eligibilityCriteria, Integer[] processedConditionsArr) {		
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
