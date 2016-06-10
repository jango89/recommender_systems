package com.system.core.controller;

import java.util.Map;

import org.elasticsearch.common.collect.Maps;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.common.collect.Lists;
import com.system.core.entity.CourseData;

@Controller
@CrossOrigin(origins = "http://localhost:4200", exposedHeaders=MediaType.ALL_VALUE, allowedHeaders=MediaType.ALL_VALUE)
@RequestMapping("/psapi")
public class RecommendController {

	@RequestMapping(value="/recommends", method=RequestMethod.GET, consumes=MediaType.ALL_VALUE, produces=MediaType.ALL_VALUE)
	public Object recommend(){
		Map<String, Object> resultMap = Maps.newHashMap();
		resultMap.put("data", Lists.newArrayList(new CourseData()));
		return new ResponseEntity<>(resultMap, HttpStatus.ACCEPTED);
	}
}
