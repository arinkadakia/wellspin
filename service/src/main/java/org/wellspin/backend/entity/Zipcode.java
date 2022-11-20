package org.wellspin.backend.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Zipcode {

	@Id
	private Integer id;
	private Integer zip;
	private Double lat;
	private Double lng;
	private String city;
	private String state_id;
	private String state_name;
	private String zcta;
	private String parent_zcta;
	private String population;
	private String density;
	private String county_fips;
	private String county_name;
	private String county_weights;
	private String county_names_all;
	private String county_fips_all;
	private String imprecise;
	private String military;
	private String timezone;
	
	public Integer getId() throws Exception {
		return this.id;
	}
	
	public void setId(Integer id) {
		Integer idInt = Integer.valueOf(-1);
		if (id != null) {
			idInt = id;			
		}
		this.id = idInt;
	}
	
	public Integer getZip() {
		return this.zip;
	}
	
	public void setZip(Integer zipcode) {
		Integer zipcodeInt = Integer.valueOf(-1);
		if (zipcode != null) {
			zipcodeInt = zipcode;			
		}
		this.zip = zipcodeInt;
	}
	
	public Double getLat() {
		return this.lat;
	}
	
	public void setLat(Double latd) {
		Double latD = Double.valueOf(-1);
		if (latd != null) {
			latD = latd;			
		}
		this.lat = latD;
	}
	
	public Double getLng() {
		return lng;
	}
	
	public void setLng(Double lngd) {
		Double lngD = Double.valueOf(-1);
		if (lngd != null) {
			lngD = lngd;			
		}
		this.lng = lngD;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getState_id() {
		return state_id;
	}
	
	public void setState_id(String state_id) {
		this.state_id = state_id;
	}
	
	public String getState_name() {
		return state_name;
	}
	
	public void setState_name(String state_name) {
		this.state_name = state_name;
	}
	
	public String getZcta() {
		return zcta;
	}
	
	public void setZcta(String zcta) {
		this.zcta = zcta;
	}
	
	public String getParent_zcta() {
		return parent_zcta;
	}
	
	public void setParent_zcta(String parent_zcta) {
		this.parent_zcta = parent_zcta;
	}
	
	public String getPopulation() {
		return population;
	}
	
	public void setPopulation(String population) {
		this.population = population;
	}
	
	public String getDensity() {
		return density;
	}
	
	public void setDensity(String density) {
		this.density = density;
	}
	
	public String getCounty_fips() {
		return county_fips;
	}
	
	public void setCounty_fips(String county_fips) {
		this.county_fips = county_fips;
	}
	
	public String getCounty_name() {
		return county_name;
	}
	
	public void setCounty_name(String county_name) {
		this.county_name = county_name;
	}
	
	public String getCounty_weights() {
		return county_weights;
	}
	
	public void setCounty_weights(String county_weights) {
		this.county_weights = county_weights;
	}
	
	public String getCounty_names_all() {
		return county_names_all;
	}
	
	public void setCounty_names_all(String county_names_all) {
		this.county_names_all = county_names_all;
	}
	
	public String getCounty_fips_all() {
		return county_fips_all;
	}
	
	public void setCounty_fips_all(String county_fips_all) {
		this.county_fips_all = county_fips_all;
	}
	
	public String getImprecise() {
		return imprecise;
	}
	
	public void setImprecise(String imprecise) {
		this.imprecise = imprecise;
	}
	
	public String getMilitary() {
		return military;
	}
	
	public void setMilitary(String military) {
		this.military = military;
	}
	
	public String getTimezone() {
		return timezone;
	}
	
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
	
}
