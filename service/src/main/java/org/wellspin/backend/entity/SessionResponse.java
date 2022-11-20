package org.wellspin.backend.entity;


public class SessionResponse {

	private Integer questionId;
	private Integer[] answerIds;
	private String answerInput;
	
	public SessionResponse(Integer questionId, Integer[] answerIds, String answerInput) {
		this.questionId = questionId;
		this.answerIds = answerIds;
		this.answerInput = answerInput;
	}
	
	public Integer getQuestionId() {
		return questionId;
	}
	
	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}

	public Integer[] getAnswerIds() {
		return answerIds;
	}

	public void setAnswerIds(Integer answerIds[]) {
		this.answerIds = answerIds;
	}

	public String getAnswerInput() {
		return answerInput;
	}

	public void setAnswerInput(String answerInput) {
		this.answerInput = answerInput;
	}
}
