package org.wellspin.backend.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;
import org.wellspin.backend.entity.Question;

@Repository
public interface QuestionsRepository extends MongoRepository<Question, String> {
	
	public List<Question> findAll();
	@SuppressWarnings("unchecked")
	public Question save(@RequestParam("question") Question question);
	public Question findById(@RequestParam("questionId") Integer questionId);
	public boolean existsById(@RequestParam("questionId") Integer questionId);
	public void deleteById(@RequestParam("questionId") Integer questionId);

}
