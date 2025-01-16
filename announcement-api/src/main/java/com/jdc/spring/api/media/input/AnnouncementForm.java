package com.jdc.spring.api.media.input;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.jdc.spring.model.constants.MediaType;
import com.jdc.spring.model.entity.Account;
import com.jdc.spring.model.entity.Announcement;

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
	
    public Announcement entity() {
    	var entity = new Announcement();
    	entity.setAnnouncementId(announcementId);
    	entity.setTitle(title);
    	entity.setContent(content);
    	entity.setTags(tags);
    	entity.setLink(link);
    	entity.setFileName(fileName);
    	entity.setPostDate(LocalDate.now());
    	entity.setPostTime(LocalDateTime.now());
    	entity.setType(type);
    	entity.setPostedBy(postedBy);
    	return entity;
    }


}
