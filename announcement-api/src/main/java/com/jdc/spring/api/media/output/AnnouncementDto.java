package com.jdc.spring.api.media.output;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


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
	    private List<String> base64Images;
	    private String tags;
	    private String link;
	    private String fileName;
	    private LocalDate postDate;
	    private LocalDateTime postTime;
	    private MediaType type;
	    private Account postedBy;
	    
	public static AnnouncementDto toDto(Announcement entity) {
	    List<String> base64Images = entity.getMediaFiles().stream()
	        .map(media -> {
	            try {
	                Path imagePath = Paths.get(media.getFilePathName()); 
	                if (Files.exists(imagePath)) {
	                    byte[] imageBytes = Files.readAllBytes(imagePath);
	                    return Base64.getEncoder().encodeToString(imageBytes);
	                }
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	            return null; 
	        })
	        .filter(Objects::nonNull)
	        .collect(Collectors.toList());

	    return new AnnouncementDto(
	        entity.getAnnouncementId(),
	        entity.getTitle(),
	        entity.getContent(),
	        base64Images,
	        entity.getTags(),
	        entity.getLink(),
	        entity.getFileName(),
	        entity.getPostDate(),
	        entity.getPostTime(),
	        entity.getType(),
	        entity.getPostedBy()
	    );
	}

	
	public static void select(CriteriaQuery<AnnouncementDto> cq, Root<Announcement> root) {
		cq.multiselect(
				root.get(Announcement_.announcementId),
				root.get(Announcement_.title),
				root.get(Announcement_.content),
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
