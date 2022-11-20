package org.wellspin.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wellspin.backend.entity.Location;
import org.wellspin.backend.repository.LocationsRepository;

@Service
public class LocationsService {
	
	@Autowired
	LocationsRepository locationsRepository;
	
	public Integer getLocationIdFromSurveyId(Integer surveyId) {
		Integer locationId = null;
		
		Location locationDoc = locationsRepository.findBySurveyid(surveyId);
		if (locationDoc != null) {
			locationId = locationDoc.getId();
		}
		return locationId;
	}

}
