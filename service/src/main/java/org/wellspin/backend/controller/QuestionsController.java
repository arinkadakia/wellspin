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
import org.wellspin.backend.entity.Question;
import org.wellspin.backend.entity.QuestionResponse;
import org.wellspin.backend.repository.QuestionsRepository;
import org.wellspin.backend.service.QuestionsService;

@RestController
@RequestMapping("/questions")

public class QuestionsController extends BaseController {

	Logger log = LoggerFactory.getLogger(QuestionsController.class);
	
	@Autowired
	QuestionsRepository questionsRepository; 

	@Autowired
	QuestionsService questionsService;
	
	@GetMapping("/getAllQuestions")
	public List<Question> getAllQuestions() {
		return questionsRepository.findAll();
	}
	
	@PostMapping("/createQuestion")
	public ResponseEntity<?> createQuestion(@RequestBody Question question) {
		question = questionsRepository.save(question);
		if (question != null) {
			try {
				return new ResponseEntity<Integer>(question.getId(), HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	
	@PostMapping("/updateQuestion")
	public ResponseEntity<?> updateQuestion(@RequestBody Question question) {
		Question questionData;
		try {
			questionData = questionsRepository.findById(question.getId());		
			if (questionData != null) {
				question = questionsRepository.save(question);
				if (question != null) {
					return new ResponseEntity<Integer>(question.getId(), HttpStatus.OK);
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	@GetMapping("/findById")
	public ResponseEntity<?> findById(@RequestParam("questionId") Integer questionId) {
		Question question = questionsRepository.findById(questionId);
		if (question != null) {
			return new ResponseEntity<Question>(question, HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	@GetMapping("/existsById")
	public boolean existsById(@RequestParam("questionId") Integer questionId) {
		return questionsRepository.existsById(questionId);
	}

	@GetMapping("/deleteById")
	public void deleteById(@RequestParam("questionId") Integer questionId) {
		questionsRepository.deleteById(questionId);
	}

	@GetMapping("/getNextQuestion")
	public ResponseEntity<?> getNextQuestion(
									@RequestParam("sessionId") String sessionId,
									@RequestParam("surveyId") Integer surveyId, 
									@RequestParam("lastQuestionId") Integer lastQuestionId, 
									@RequestParam("lastAnswerIds") String lastAnswerIds, 
									@RequestParam("lastAnswerInput") String lastAnswerInput) {
		
		Question nextQuestion = questionsService.getNextQuestion(
									sessionId, surveyId, lastQuestionId, 
									lastAnswerIds, lastAnswerInput);
		if (nextQuestion == null) {
			return new ResponseEntity<>(null, HttpStatus.OK);
		} else {
			return new ResponseEntity<Question>(nextQuestion, HttpStatus.OK);
		}
	}
	
	@GetMapping("/getQuestionResponse")	
	public ResponseEntity<?> getQuestionResponse(@RequestParam("sessionId") String sessionId,
												 @RequestParam("questionId") Integer questionId) {
	
		QuestionResponse questionResponse = questionsService.getQuestionResponse(sessionId, questionId);			
		if (questionResponse == null) {
			return new ResponseEntity<>(null, HttpStatus.OK);
		} else {
			return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.OK);
		}
	}
	
}
