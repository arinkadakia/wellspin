package org.wellspin.backend.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Question {
	
	@Id
	private Integer id;
	private String name;
	private String text;
	private String hint;
	private String type;
	private String format;
	private String answer[];
	private String nextid[];

	
	public Integer getId() throws Exception {
		Integer questionId = Integer.valueOf(-1);
		if (this.id != null) {
			questionId = this.id;
		}
		return questionId;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getName() {
		return this.name;
	}

	public void setName(String questionName) {
		this.name = questionName;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String[] getAnswer() {
		return this.answer;
	}

	public void setAnswer(String answer[]) {
		this.answer = answer;
	}

	public Integer[] getNextid() {
		Integer answerNextId[];
		answerNextId = new Integer[9];
		for (int i=0; i<9; i++) {
			answerNextId[i] = Integer.valueOf(-1);
		}
		
		if (nextid != null && nextid.length > 0 && nextid.length < 10) {
			for (int i=0; i<nextid.length; i++) {
				if (this.nextid[i] != null && !this.nextid[i].isEmpty()) {
					answerNextId[i] = Integer.valueOf(this.nextid[i]);
				} else {
					answerNextId[i] = Integer.valueOf(-1);
				}
			}
		}
		return answerNextId;
	}

	public void setNextid(Integer answerNextId[]) {
		String nextid[] = new String[9];
		for (int i=0; i<9; i++) {
			nextid[i] = String.valueOf(-1);
		}
		
		if (answerNextId != null && answerNextId.length > 0 && answerNextId.length < 10) {
			for (int i=0; i<9; i++) {
				nextid[i] = String.valueOf(answerNextId[i]);
			} 
		}
		this.nextid = nextid;
	}

}
