package com.jdc.spring.api.media.output;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementDetailsDto {
	
	 private Long announcementId;
	    private String title;
	    private String content;
	    private String tags;
	    private String link;
	    private String fileName;
	    private LocalDate postDate;
	    private LocalDateTime postTime;
	    private String postedBy;
	 

}
