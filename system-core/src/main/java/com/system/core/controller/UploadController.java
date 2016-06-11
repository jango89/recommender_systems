package com.system.core.controller;

import javax.servlet.http.HttpServletRequest;

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

@Controller
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/psapi")
public class UploadController {

	@RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE, produces = MediaType.ALL_VALUE)
	public Object upload(@RequestBody MultipartFile file, HttpServletRequest httpreq) {
		file = ((StandardMultipartHttpServletRequest) httpreq).getFile("post[file]");
		return new ResponseEntity<>(null, HttpStatus.ACCEPTED);
	}
}
