package com.jdc.spring.model.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import com.jdc.spring.model.constants.MediaType;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "announcements")
@EqualsAndHashCode(callSuper = false)
public class Announcement extends AbstractEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long announcementId;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String title;

	@Column(columnDefinition = "TEXT")
	private String content;

	private String tags;

	private String link;
	
    private String fileName;

	@Column(nullable = false)
	private LocalDate postDate;

	@Column(nullable = false)
	private LocalDateTime postTime;
	
	@Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MediaType type;

	@ManyToOne
	@JoinColumn(name = "posted_by")
	private Account postedBy;

	@CreatedDate
	private LocalDateTime createAt;

	@LastModifiedDate
	private LocalDateTime modifiedAt;

	@CreatedBy
	private String createBy;

	@LastModifiedBy
	private String modifyBy;
	
	@ElementCollection
	@CollectionTable(name = "ANNOUNCHEMENT_IMAGES")
	private List<String> images = new ArrayList<String>();

}
