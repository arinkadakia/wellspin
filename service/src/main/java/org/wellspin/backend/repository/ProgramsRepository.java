package org.wellspin.backend.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;
import org.wellspin.backend.entity.Program;

@Repository
public interface ProgramsRepository extends MongoRepository<Program, String> {

	public List<Program> findAll();
	@SuppressWarnings("unchecked")
	public Program save(@RequestParam("program") Program program);
	public Program findById(@RequestParam("programId") int programId);
	public boolean existsById(@RequestParam("programId") int programId);
	public void deleteById(@RequestParam("programId") int programId);
	public List<Program> findByLocationid(@RequestParam("locationId") int locationId); 

}
