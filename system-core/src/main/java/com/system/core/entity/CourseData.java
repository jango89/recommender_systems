package com.system.core.entity;

public class CourseData {

	private long id;
	private String type = "recommend";
	private CourseDataAttribute attributes;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public CourseDataAttribute getAttributes() {
		return attributes;
	}
	public void setAttributes(CourseDataAttribute attributes) {
		this.attributes = attributes;
	}
	
	
}
