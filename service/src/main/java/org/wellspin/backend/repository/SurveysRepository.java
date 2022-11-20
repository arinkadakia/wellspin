package org.wellspin.backend.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;
import org.wellspin.backend.entity.Survey;

@Repository
public interface SurveysRepository extends MongoRepository<Survey, String> {
	
	public List<Survey> findAll();
	@SuppressWarnings("unchecked")
	public Survey save(@RequestParam("survey") Survey survey);
	public Survey findById(@RequestParam("surveyId") int surveyId);
	public boolean existsById(@RequestParam("surveyId") int surveyId);
	public void deleteById(@RequestParam("surveyId") int surveyId);

}
