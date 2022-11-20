package org.wellspin.backend.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;
import org.wellspin.backend.entity.Zipcode;


@Repository
public interface ZipcodesRepository extends MongoRepository<Zipcode, String> {
	
	public List<Zipcode> findAll();
	@SuppressWarnings("unchecked")
	public Zipcode save(@RequestParam("zipcode") Zipcode zipcode);
	public Zipcode findById(@RequestParam("zipcodeId") Integer zipcodeId);
	public boolean existsById(@RequestParam("zipcodeId") Integer zipcodeId);
	public void deleteById(@RequestParam("zipcodeId") Integer zipcodeId);
	public Zipcode findByZip(@RequestParam("zip") Integer zip);
	
}
