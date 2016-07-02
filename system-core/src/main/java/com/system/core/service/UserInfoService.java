package com.system.core.service;

import java.time.LocalDate;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.system.core.entity.nosql.UserInfo;
import com.system.core.repository.nosql.UserInfoRepository;

@Service
public class UserInfoService {

	private final UserInfoRepository userInfoRepository;

	@Autowired
	public UserInfoService(UserInfoRepository userInfoRepository) {
		this.userInfoRepository = userInfoRepository;
	}

	public void saveUserInfo(HttpServletRequest request, String courseName, Float score, float secondsTaken,
			String searchVia) {
		String ipAddress = request.getHeader("X-FORWARDED-FOR");
		if (StringUtils.isBlank(ipAddress)) {
			ipAddress = request.getRemoteAddr();
		}
		UserInfo userInfo = new UserInfo();
		userInfo.setIpAddress(ipAddress);
		LocalDate localDate = LocalDate.now();
		userInfo.setMonth(localDate.getMonth().toString());
		userInfo.setYear(String.valueOf(localDate.getYear()));
		userInfo.setCurrentTime(System.currentTimeMillis());
		userInfo.setCourseName(courseName);
		userInfo.setDay(String.valueOf(localDate.getDayOfMonth()));
		userInfo.setScore(score.doubleValue());
		userInfo.setTimeInSeconds(secondsTaken);
		userInfo.setSearchVia(searchVia);
		userInfoRepository.save(userInfo);
	}

}
