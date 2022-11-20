package org.wellspin.backend.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;
import org.wellspin.backend.entity.Condition;

@Repository
public interface ConditionsRepository extends MongoRepository<Condition, String> {
	
	public List<Condition> findAll();
	@SuppressWarnings("unchecked")
	public Condition save(@RequestParam("condition") Condition condition);
	public Condition findById(@RequestParam("conditionId") int conditionId);
	public boolean existsById(@RequestParam("conditionId") int conditionId);
	public void deleteById(@RequestParam("conditionId") int conditionId);
	
}
