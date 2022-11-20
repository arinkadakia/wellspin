package org.wellspin.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;
import org.wellspin.backend.entity.Session;

@Repository
public interface SessionsRepository extends MongoRepository<Session, String> {
	
	public List<Session> findAll();
	@SuppressWarnings("unchecked")
	public Session save(@RequestParam("session") Session session);
	public Optional<Session> findById(@RequestParam("sessionId") String sessionId);
	public boolean existsById(@RequestParam("sessionId") String sessionId);
	public void deleteById(@RequestParam("sessionId") String sessionId);
	
}
