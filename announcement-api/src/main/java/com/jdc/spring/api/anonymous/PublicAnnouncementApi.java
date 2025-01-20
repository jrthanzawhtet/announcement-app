package com.jdc.spring.api.anonymous;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jdc.spring.api.media.input.AnnouncementSearch;
import com.jdc.spring.api.media.output.AnnouncementDto;
import com.jdc.spring.api.media.output.AnnouncementShowLessDto;
import com.jdc.spring.model.service.AnnouncementService;
import com.jdc.spring.utils.io.ApiResponse;
import org.springframework.data.domain.Page;


@RestController
@RequestMapping("public/announcement")
public class PublicAnnouncementApi {

	@Autowired
	private AnnouncementService announcementService;
	
	@GetMapping
	public ApiResponse<Page<AnnouncementDto>> search(AnnouncementSearch form,
			@RequestParam(required = false, defaultValue = "0") int page, 
			@RequestParam(required = false, defaultValue = "10") int size){
		return ApiResponse.success(announcementService.search(form,page,size));
	}
	
	@GetMapping("announcements")
	public ApiResponse<List<AnnouncementDto>> findAllAnnouncements() {
		return ApiResponse.success(announcementService.findAll());
	}
	
	@GetMapping("showLess")
	public ApiResponse<List<AnnouncementShowLessDto>> showLess() {
		return ApiResponse.success(announcementService.showLess());
	}
	
	@GetMapping("{id}")
	public ApiResponse<AnnouncementDto> showDetails(@PathVariable Long id){
		return ApiResponse.success(announcementService.findById(id));
	}

}
