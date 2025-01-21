package com.jdc.spring.api.media.output;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.jdc.spring.model.constants.MediaType;
import com.jdc.spring.model.constants.Role;
import com.jdc.spring.model.entity.Announcement;
import com.jdc.spring.model.entity.Announcement_;
import com.jdc.spring.model.entity.Media_;
import com.jdc.spring.model.entity.Tag;
import com.jdc.spring.model.entity.Tag_;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
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
	private List<String> tags;
	private String link;
	private String fileName;
	private LocalDate postDate;
	private LocalDateTime postTime;
	private MediaType type;
	private Role role;

	public static AnnouncementDto toDto(Announcement entity) {
		List<String> base64Images = Optional.ofNullable(entity.getMediaFiles()).orElse(List.of()).stream()
				.flatMap(media -> {
					if (media.getFilePathName() != null) {
						return Arrays.stream(media.getFilePathName().split(","));
					}
					return Stream.empty();
				}).map(AnnouncementDto::encodeToBase64).filter(Objects::nonNull).collect(Collectors.toList());

		String base64TitleImage = Optional
				.ofNullable(entity.getMediaFiles().stream().filter(media -> media.getTitleFilePathName() != null)
						.findFirst().orElse(null))
				.map(titleMedia -> encodeToBase64(titleMedia.getTitleFilePathName())).orElse(null);

		List<String> tagNames = Optional.ofNullable(entity.getTags()).orElse(Set.of()).stream().map(Tag::getName)
				.collect(Collectors.toList());

		return new AnnouncementDto(entity.getAnnouncementId(), entity.getTitle(), entity.getContent(), base64TitleImage,
				base64Images, tagNames, entity.getLink(), entity.getFileName(), entity.getPostDate(),
				entity.getPostTime(), entity.getType(),
				entity.getPostedBy() != null ? entity.getPostedBy().getRole() : Role.Admin);
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

	public static void select(CriteriaBuilder cb, CriteriaQuery<AnnouncementDto> query, Root<Announcement> root) {
		var mediaJoin = root.join(Announcement_.mediaFiles, JoinType.LEFT);

		var tagJoin = root.join(Announcement_.tags, JoinType.LEFT);

		query.select(cb.construct(AnnouncementDto.class, root.get(Announcement_.announcementId),
				root.get(Announcement_.title), root.get(Announcement_.content), mediaJoin.get(Media_.titleFilePathName),
				mediaJoin.get(Media_.filePathName), tagJoin.get(Tag_.name), root.get(Announcement_.link),
				root.get(Announcement_.fileName), root.get(Announcement_.postDate), root.get(Announcement_.postTime),
				root.get(Announcement_.type), root.get(Announcement_.postedBy)));
	}

}
