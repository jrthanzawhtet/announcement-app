package com.jdc.spring.api.media.input;

import com.jdc.spring.model.constants.MediaType;
import com.jdc.spring.model.entity.Account;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AnnouncementForm {

	private Long announcementId;

	@NotBlank(message = "Please enter title.")
	private String title;
	private String content;
	private String tags;
	private String link;
	private String fileName;
	private MediaType type;
	private Account postedBy;
	
}
