package com.jdc.spring.api.media.output;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.jdc.spring.model.constants.MediaType;
import com.jdc.spring.model.constants.Role;
import com.jdc.spring.model.entity.Announcement;
import com.jdc.spring.model.entity.Announcement_;
import com.jdc.spring.model.entity.Tag;

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
	private String base64TitleImage;
	private List<String> base64Images;
	private String tags;
	private String link;
	private String fileName;
	private LocalDate postDate;
	private LocalDateTime postTime;
	private MediaType type;
	private Role role;

	public static AnnouncementDto toDto(Announcement entity) {
	    List<String> base64Images = Optional.ofNullable(entity.getMediaFiles())
	        .orElse(List.of()) 
	        .stream()
	        .flatMap(media -> {
	            if (media.getFilePathName() != null) {
	                return Arrays.stream(media.getFilePathName().split(","));
	            }
	            return Stream.empty();
	        })
	        .map(filePath -> encodeToBase64(filePath))
	        .filter(Objects::nonNull)
	        .collect(Collectors.toList());

	    String base64TitleImage = Optional.ofNullable(
	            entity.getMediaFiles()
	                  .stream()
	                  .filter(media -> media.getTitleFilePathName() != null)
	                  .findFirst()
	                  .orElse(null)
	        )
	        .map(titleMedia -> encodeToBase64(titleMedia.getTitleFilePathName()))
	        .orElse(null);

	    return new AnnouncementDto(
	        entity.getAnnouncementId(),
	        entity.getTitle(),
	        entity.getContent(),
	        base64TitleImage,
	        base64Images,
	        entity.getTags(),
	        entity.getLink(),
	        entity.getFileName(),
	        entity.getPostDate(),
	        entity.getPostTime(),
	        entity.getType(),
	        entity.getPostedBy() != null ? entity.getPostedBy().getRole() : Role.Admin 
	    );
	}
	
	private static String encodeToBase64(String filePath) {
	    try {
	        Path path = Paths.get(filePath);
	        if (Files.exists(path)) {
	            byte[] fileBytes = Files.readAllBytes(path);
	            return Base64.getEncoder().encodeToString(fileBytes);
	        }
	    } catch (Exception e) {
	        System.err.println("Error encoding file to Base64: " + filePath + " - " + e.getMessage());
	    }
	    return null;
	}


	public static void select(CriteriaQuery<AnnouncementDto> cq, Root<Announcement> root) {
		cq.multiselect(root.get(Announcement_.announcementId), root.get(Announcement_.title),
				root.get(Announcement_.content), root.get(Announcement_.tags), root.get(Announcement_.link),
				root.get(Announcement_.fileName), root.get(Announcement_.postDate), root.get(Announcement_.postTime),
				root.get(Announcement_.type), root.get(Announcement_.postedBy));
	}

	public AnnouncementDto(Long announcementId2, String title2, String content2, String base64TitleImage2,
			List<String> base64Images2, Set<Tag> tags2, String link2, String fileName2, LocalDate postDate2,
			LocalDateTime postTime2, MediaType type2, Role role2) {
	}

	public AnnouncementDto(Long announcementId2, String title2, String content2, String base64TitleImage2,
			Set<Tag> tags2, Object object) {
	}


}
