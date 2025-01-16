package com.jdc.spring.api.media;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jdc.spring.api.media.input.AnnouncementForm;
import com.jdc.spring.api.media.output.AnnouncementDto;
import com.jdc.spring.model.service.AnnouncementService;
import com.jdc.spring.utils.io.ApiResponse;

@RestController
@RequestMapping("public/announcement")
public class AnnouncementApi {

	@Autowired
	private AnnouncementService service;

	/*
	 * @GetMapping("{id}") public ApiResponse<AnnouncementForm>
	 * showDetails(@PathVariable int id) { return
	 * ApiResponse.success(service.findById(id)); }
	 * 
	 * @GetMapping("announcements") public ApiResponse<List<AnnouncementDto>>
	 * findAllAnnouncement() { return
	 * ApiResponse.success(service.getAllAnnouncements()); }
	 */

}
