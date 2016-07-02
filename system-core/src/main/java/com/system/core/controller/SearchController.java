package com.system.core.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.system.core.response.GraphResponse;
import com.system.core.service.SearchInterface;
import com.system.core.service.UserInfoService;

@Controller
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/psapi")
public class SearchController {

	@Autowired
	private SearchInterface searchInterface;

	@Autowired
	private UserInfoService userInfoService;

	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object recommend(@RequestBody JSONObject jsonObject, HttpServletRequest httpreq) {
		long startTime = System.currentTimeMillis();
		List<GraphResponse> result = searchInterface.searchByUploadedText(jsonObject.get("skills").toString());
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		if (!result.isEmpty()) {
			userInfoService.saveUserInfo(httpreq, result.get(0).getCourseName(), result.get(0).getScore(),
					totalTime / 1000, "normal");
		}
		return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
	}

}
