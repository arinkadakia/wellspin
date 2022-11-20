package org.wellspin.backend.entity;

public class QuestionResponse {

	private Question question;
	private SessionResponse response;
	
	public Question getQuestion() {
		return question;
	}
	
	public void setQuestion(Question question) {
		this.question = question;
	}

	public SessionResponse getResponse() {
		return response;
	}

	public void setResponse(SessionResponse response) {
		this.response = response;
	}
	
}
