package com.jdc.spring.api.media.output;

import java.time.LocalDate;
import java.util.List;

import com.jdc.spring.model.entity.Announcement;
import com.jdc.spring.model.entity.Announcement_;
import com.jdc.spring.model.entity.Media_;
import com.jdc.spring.model.entity.Tag_;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AnnouncementSearchDto {

	private Long announcementId;
	private String title;
	private String content;
	private String base64TitleImage;
	private List<String> base64Images;
	private List<String> tags;
	private String link;
	private String fileName;
	private LocalDate postDate;

	public static void select(CriteriaBuilder cb, CriteriaQuery<AnnouncementSearchDto> query, Root<Announcement> root) {
		var mediaJoin = root.join(Announcement_.mediaFiles, JoinType.LEFT);
		var tagJoin = root.join(Announcement_.tags, JoinType.LEFT);

		query.select(cb.construct(AnnouncementSearchDto.class, root.get(Announcement_.announcementId),
				root.get(Announcement_.title), root.get(Announcement_.content), mediaJoin.get(Media_.titleFilePathName),
				mediaJoin.get(Media_.filePathName), tagJoin.get(Tag_.name), root.get(Announcement_.link),
				root.get(Announcement_.fileName), root.get(Announcement_.postDate)));
	}

	public AnnouncementSearchDto(Long announcementId, String title, String content, String base64TitleImage,
			List<String> base64Images, List<String> tags, String link, String fileName, LocalDate postDate) {
		this.announcementId = announcementId;
		this.title = title;
		this.content = content;
		this.base64TitleImage = base64TitleImage;
		this.base64Images = base64Images;
		this.tags = tags;
		this.link = link;
		this.fileName = fileName;
		this.postDate = postDate;
	}
}