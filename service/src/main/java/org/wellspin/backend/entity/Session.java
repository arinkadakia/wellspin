package org.wellspin.backend.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Session {

	@Id
	private String id;
	private Integer surveyId = null;
	private String userId = null;
	private Date created = new Date();
	private Map<Integer, SessionResponse> responses = null;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(Integer surveyId) {
		this.surveyId = surveyId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Map<Integer, SessionResponse> getResponses() {
		return responses;
	}

	public void setResponses(Map<Integer, SessionResponse> responses) {
		this.responses = responses;
	}

	public void addResponse(Integer questionId, SessionResponse response) {
		if (this.responses == null) {
			this.responses = new HashMap<>();
		}
		this.responses.put(questionId, response);
	}

	public SessionResponse getResponse(Integer questionId) {
		SessionResponse response = null;
		if (this.responses != null) {
			if (this.responses.get(questionId) != null) {
				response = this.responses.get(questionId);
			}
		}
		return response;
	}

	public void deleteResponse(Integer questionId) {
		if (this.responses != null) {
			if (this.responses.get(questionId) != null) {
				this.responses.remove(questionId);
			}
		}
	}

}
