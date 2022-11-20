package org.wellspin.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;
import org.wellspin.backend.entity.User;

@Repository
public interface UsersRepository extends MongoRepository<User, String> {
	
	public List<User> findAll();
	@SuppressWarnings("unchecked")
	public User save(@RequestParam("user") User user);
	public Optional<User> findById(@RequestParam("userId") String userId);
	public boolean existsById(@RequestParam("userId") String userId);
	public void deleteById(@RequestParam("userId") String userId);
	public User findByName(@RequestParam("name") String name);
//	public User findByNameAndUserId(@RequestParam("name") String name, @RequestParam("userId") int userId);
	
}
