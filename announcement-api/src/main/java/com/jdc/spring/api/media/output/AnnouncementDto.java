package com.jdc.spring.api.media.output;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.jdc.spring.model.constants.MediaType;
import com.jdc.spring.model.entity.Account;
import com.jdc.spring.model.entity.Announcement;
import com.jdc.spring.model.entity.Announcement_;

import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementDto {

	private Long announcementId;
	private String title;
	private String content;
    private List<String> images;
	private String tags;
	private String link;
	private String fileName;
	private LocalDate postDate;
	private LocalDateTime postTime;
	private MediaType type;
	private Account postedBy;
	
	public static void select(CriteriaQuery<AnnouncementDto> cq, Root<Announcement> root) {
		var announcement = root.join(Announcement_.ANNOUNCEMENT_ID);
		cq.multiselect(
				root.get(Announcement_.announcementId),
				root.get(Announcement_.title),
				root.get(Announcement_.content),
				root.get(Announcement_.IMAGES),	
				root.get(Announcement_.tags),
				root.get(Announcement_.link),
				root.get(Announcement_.fileName),
				root.get(Announcement_.postDate),
				root.get(Announcement_.postTime),
				root.get(Announcement_.type),
				root.get(Announcement_.postedBy)
				);
	}


}
