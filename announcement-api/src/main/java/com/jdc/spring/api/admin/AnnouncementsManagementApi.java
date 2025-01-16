package com.jdc.spring.api.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jdc.spring.api.media.input.AnnouncementForm;
import com.jdc.spring.model.service.AnnouncementService;
import com.jdc.spring.utils.io.ApiResponse;
import com.jdc.spring.utils.io.DataModificationResult;


@RestController
@RequestMapping("admin/announcements")
public class AnnouncementsManagementApi {

	@Autowired
	private AnnouncementService service;

	@PostMapping
	public ApiResponse<DataModificationResult<Long>> create(@RequestBody AnnouncementForm form,
			BindingResult result) {
		return ApiResponse.success(service.create(form));
	}
	
	@PutMapping("{id}")
	public ApiResponse<DataModificationResult<Long>> update(
			@PathVariable Long id, 
			@RequestBody AnnouncementForm form, BindingResult result) {
		return ApiResponse.success(service.update(id, form));
	}
	
	@PutMapping("{id}/photos")
	public ApiResponse<DataModificationResult<Long>> uploadPhoto(
			@PathVariable Long id, 
			@RequestParam List<MultipartFile> files) {
		return ApiResponse.success(service.uploadPhoto(id, files));
	}

}
