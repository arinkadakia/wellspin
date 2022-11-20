package org.wellspin.backend.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Condition {

	@Id
	private Integer id;
	private Integer questionid;
	private String description;
	private String answer;

	public Integer getId() {
		Integer conditionId = Integer.valueOf(-1);
		if (this.id != null) {
			conditionId = Integer.valueOf(this.id);
		}
		return conditionId;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getQuestionid() {
		Integer questionIdInt = Integer.valueOf(-1);
		if (this.questionid != null) {
			questionIdInt = Integer.valueOf(this.questionid);
		}
		return questionIdInt;
	}

	public void setQuestionid(Integer questionIdInt) {
		this.questionid = questionIdInt;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

}
