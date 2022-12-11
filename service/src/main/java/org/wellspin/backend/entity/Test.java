package org.wellspin.backend.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Test {
	
	@Id
	private Integer id;
	private String name;
	private String description;
	private String url;
	private String eligibility;
	private String type;
	private Integer locationid;
	
	public Integer getId() {
		Integer testId = Integer.valueOf(-1);
		if (this.id != null) {
			testId = this.id;
		}
		return testId;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getEligibility() {
		return eligibility;
	}

	public void setEligibility(String eligibility) {
		this.eligibility = eligibility;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getLocationid() {
		Integer locationIdInt = Integer.valueOf(-1);
		if (this.locationid != null) {
			locationIdInt = this.locationid;
		}
		return locationIdInt;

	}

	public void setLocationid(Integer locationIdInt) {
		this.locationid = locationIdInt;
	}

	
}
