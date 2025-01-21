package com.jdc.spring.model.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import com.jdc.spring.model.constants.MediaType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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

	@OneToMany(mappedBy = "announcement", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Media> mediaFiles = new ArrayList<>();

	@ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	@JoinTable(name = "announcement_tags", joinColumns = @JoinColumn(name = "announcement_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
	private Set<Tag> tags = new HashSet<>();

	@CreatedDate
	@Column(name = "create_at")
	private LocalDateTime createAt;

	@LastModifiedDate
	private LocalDateTime modifiedAt;

	@CreatedBy
	private String createBy;

	@LastModifiedBy
	private String modifyBy;

}
