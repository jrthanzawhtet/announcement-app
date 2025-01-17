package com.jdc.spring.api.anonymous;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jdc.spring.api.media.output.AnnouncementDto;
import com.jdc.spring.model.service.AnnouncementService;
import com.jdc.spring.utils.io.ApiResponse;

@RestController
@RequestMapping("public/announcement")
public class PublicAnnouncementApi {

	@Autowired
	private AnnouncementService announcementService;
	
	@GetMapping("announcements")
	public ApiResponse<List<AnnouncementDto>> findAllAnnouncements() {
		return ApiResponse.success(announcementService.findAll());
	}
	
	@GetMapping("{id}")
	public ApiResponse<AnnouncementDto> showDetails(@PathVariable Long id){
		return ApiResponse.success(announcementService.findById(id));
	}

}
