package org.wellspin.backend.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Survey {
	
	@Id
	private Integer id;
	private Integer nextid[];
	
	public Integer getId() throws Exception {
		Integer surveyId = Integer.valueOf(-1);
		if (this.id != null) {
			surveyId = this.id;
		}
		return surveyId;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer[] getNextid() {
		Integer surveyNextId[]=null;
		if (this.nextid != null && this.nextid.length > 0) {
			surveyNextId = new Integer[this.nextid.length];
			for (int i=0; i<this.nextid.length; i++) {
				if (this.nextid[i] != null) {
					surveyNextId[i] = this.nextid[i];
				} else {
					surveyNextId[i] = Integer.valueOf(-1);
				}
			}
		}
		return surveyNextId;
	}

	public void setNextid(Integer surveyNextId[]) {
		Integer nextid[]=null;
		if (surveyNextId != null && surveyNextId.length > 0) {
			nextid = new Integer[surveyNextId.length];
			for (int i=0; i<surveyNextId.length; i++) {
				nextid[i] = surveyNextId[i];
			} 
		}
		this.nextid = nextid;
	}

}
