package com.system.core.entity.nosql;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "user_info", type = "info")
public class UserInfo {

	@Id
	private String id;
	@Field(type = FieldType.String, searchAnalyzer = "analyzer_keyword_org", analyzer = "analyzer_keyword_org")
	private String ipAddress;
	private String month;
	private String year;
	private String day;
	private Long currentTime;
	@Field(type = FieldType.String, searchAnalyzer = "analyzer_keyword_org", analyzer = "analyzer_keyword_org")
	private String courseName;
	private Double score;
	private Float timeInSeconds;
	private String searchVia;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public Long getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(Long currentTime) {
		this.currentTime = currentTime;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public Float getTimeInSeconds() {
		return timeInSeconds;
	}

	public void setTimeInSeconds(Float timeInSeconds) {
		this.timeInSeconds = timeInSeconds;
	}

	public String getSearchVia() {
		return searchVia;
	}

	public void setSearchVia(String searchVia) {
		this.searchVia = searchVia;
	}

}
