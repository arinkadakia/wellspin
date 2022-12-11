package org.wellspin.backend.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;
import org.wellspin.backend.entity.Test;

@Repository
public interface TestsRepository extends MongoRepository<Test, String> {

	public List<Test> findAll();
	@SuppressWarnings("unchecked")
	public Test save(@RequestParam("test") Test test);
	public Test findById(@RequestParam("testId") int testId);
	public boolean existsById(@RequestParam("testId") int testId);
	public void deleteById(@RequestParam("testId") int testId);
	public List<Test> findByLocationid(@RequestParam("locationId") int locationId); 

}
