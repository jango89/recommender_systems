package com.system.core.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.system.core.response.GraphResponse;

public interface SearchInterface {

	List<GraphResponse> searchByUploadedPdf(MultipartFile multiPartFile);
}
