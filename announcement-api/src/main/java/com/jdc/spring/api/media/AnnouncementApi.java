package com.jdc.spring.api.media;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jdc.spring.model.entity.Announcement;
import com.jdc.spring.model.service.AnnouncementService;
import com.jdc.spring.utils.io.ApiResponse;

@RestController
@RequestMapping("/publish")
public class AnnouncementApi {
	
	@Autowired
	private AnnouncementService service;
	
	
	/*
	 * @GetMapping public ApiResponse<Page<Announcement>> search(MemberSearch form,
	 * 
	 * @RequestParam(required = false, defaultValue = "0") int page,
	 * 
	 * @RequestParam(required = false, defaultValue = "10") int size) { return
	 * ApiResponse.success(service.search(form, page, size)); }
	 */

}
