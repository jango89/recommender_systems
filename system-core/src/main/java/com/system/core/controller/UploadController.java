package com.system.core.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import com.system.core.response.GraphResponse;
import com.system.core.service.SearchInterface;

@Controller
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/psapi")
public class UploadController {

	@Autowired
	private SearchInterface searchInterface;

	@RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Object upload(@RequestBody MultipartFile file, HttpServletRequest httpreq) {
		file = ((StandardMultipartHttpServletRequest) httpreq).getFile("post[file]");
		List<GraphResponse> result = searchInterface.searchByUploadedPdf(file);
		return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
	}
}
