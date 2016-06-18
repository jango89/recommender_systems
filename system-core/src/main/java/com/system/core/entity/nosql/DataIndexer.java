package com.system.core.entity.nosql;

import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "data_index", type = "crawled")
public class DataIndexer {

	@Id
	private String id;
	@Field(type = FieldType.String, searchAnalyzer = "analyzer_keyword_org", analyzer = "analyzer_keyword_org")
	private Set<String> courseKeywords;
	private String courseName;
	private String programeName;

	public DataIndexer() {
		super();
	}

	public DataIndexer(Set<String> courseKeywords, String courseName, String programeName) {
		super();
		this.courseKeywords = courseKeywords;
		this.courseName = courseName;
		this.programeName = programeName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Set<String> getCourseKeywords() {
		return courseKeywords;
	}

	public void setCourseKeywords(Set<String> courseKeywords) {
		this.courseKeywords = courseKeywords;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getProgrameName() {
		return programeName;
	}

	public void setProgrameName(String programeName) {
		this.programeName = programeName;
	}

}
