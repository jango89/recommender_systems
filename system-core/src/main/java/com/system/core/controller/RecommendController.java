package com.system.core.controller;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.system.core.entity.CourseData;
import com.system.core.service.DashboardService;

@Controller
@CrossOrigin(origins = "http://localhost:4200", exposedHeaders = MediaType.ALL_VALUE, allowedHeaders = MediaType.ALL_VALUE)
@RequestMapping("/psapi")
public class RecommendController {

	@Autowired
	private DashboardService dashboardService;

	@RequestMapping(value = "/recommends", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.ALL_VALUE)
	public Object recommend() {
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("data", Lists.newArrayList(new CourseData()));
		return new ResponseEntity<>(resultMap, HttpStatus.ACCEPTED);
	}

	@RequestMapping(value = "/dashboards", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.ALL_VALUE)
	@ResponseBody
	public Object dashboard() {
		JSONObject obj = dashboardService.getDashboardDetails();
		String toReturn = new String("window.jsonPResponse=");
		toReturn = toReturn.concat(obj.toJSONString());
		return toReturn;
	}
}
