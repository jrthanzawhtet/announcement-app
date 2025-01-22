package com.jdc.spring.model.service;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.jdc.spring.api.media.input.AnnouncementForm;
import com.jdc.spring.api.media.input.AnnouncementSearch;
import com.jdc.spring.api.media.input.TagsForm;
import com.jdc.spring.api.media.output.AnnouncementDto;
import com.jdc.spring.api.media.output.AnnouncementSearchDto;
import com.jdc.spring.api.media.output.AnnouncementSearchDtoRowMapper;
import com.jdc.spring.api.media.output.AnnouncementShowLessDto;
import com.jdc.spring.model.entity.Announcement;
import com.jdc.spring.model.entity.Announcement_;
import com.jdc.spring.model.entity.Media_;
import com.jdc.spring.model.entity.Tag;
import com.jdc.spring.model.entity.Tag_;
import com.jdc.spring.model.repo.AnnouncementRepo;
import com.jdc.spring.model.repo.TagRepo;
import com.jdc.spring.utils.exceptions.ApiBusinessException;
import com.jdc.spring.utils.io.DataModificationResult;

import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnnouncementService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");


	@Value("${app.image.folder}")
	
	private String imageFolder;
	
	private final PhotoUploadService photoUploadService;

	private final AnnouncementRepo announcementRepo;

	private final TagRepo tagRepo;
	
	private final JdbcTemplate jdbcTemplate;

	public DataModificationResult<Long> create(AnnouncementForm form) {
	    var entity = new Announcement();
	    entity.setAnnouncementId(form.getAnnouncementId());
	    entity.setTitle(form.getTitle());
	    entity.setContent(form.getContent());
	    entity.setLink(form.getLink());
	    entity.setFileName(form.getFileName());
	    entity.setPostDate(LocalDate.now());
	    entity.setPostTime(LocalDateTime.now());
	    entity.setType(form.getType());
	    entity.setPostedBy(form.getPostedBy());
	    entity.setCreateBy(form.getPostedBy().toString());
	    
	    if (form.getTags() != null && !form.getTags().isBlank()) {
	        Set<String> newTagNames = Arrays.stream(form.getTags().split(","))
	                .map(String::trim)
	                .filter(tag -> !tag.isBlank())
	                .collect(Collectors.toSet());

	        Set<Tag> tagsToPersist = new HashSet<>();
	        for (String tagName : newTagNames) {
	            Tag tag = tagRepo.findByName(tagName).orElseGet(() -> {
	                Tag newTag = new Tag();
	                newTag.setName(tagName);
	                return tagRepo.save(newTag);
	            });
	            tagsToPersist.add(tag);
	        }
	        entity.setTags(tagsToPersist);
	    } else {
	        entity.setTags(new HashSet<>());
	    }

	    entity = announcementRepo.save(entity);
	    return new DataModificationResult<>(entity.getAnnouncementId(), "Announcement has been created.");
	}
	
	public DataModificationResult<Long> update(Long id, AnnouncementForm form) {
		var entity = announcementRepo.findById(id)
				.orElseThrow(() -> new ApiBusinessException("Invalid Announcement id."));
		entity.setAnnouncementId(id);
		entity.setTitle(form.getTitle());
		entity.setContent(form.getContent());
    	entity.setLink(form.getLink());
    	entity.setFileName(form.getFileName());
    	entity.setPostDate(LocalDate.now());
    	entity.setPostTime(LocalDateTime.now());
    	entity.setModifiedAt(LocalDateTime.now());
    	entity.setType(form.getType());
    	entity.setPostedBy(form.getPostedBy());
    	entity.setModifyBy(form.getPostedBy().toString());
		entity = announcementRepo.saveAndFlush(entity);
		if (form.getTags() != null && !form.getTags().isBlank()) {
	        Set<String> newTagNames = Arrays.stream(form.getTags().split(","))
	                .map(String::trim)
	                .filter(tag -> !tag.isBlank())
	                .collect(Collectors.toSet());

	        Set<Tag> existingTags = newTagNames.stream()
	                .map(tagName -> tagRepo.findByName(tagName).orElseGet(() -> {
	                    Tag newTag = new Tag();
	                    newTag.setName(tagName);
	                    return newTag;
	                }))
	                .collect(Collectors.toSet());

	        entity.setTags(existingTags);
	    } else {
	        entity.setTags(new HashSet<>());
	    }
	    entity = announcementRepo.saveAndFlush(entity);
		return new DataModificationResult<Long>(entity.getAnnouncementId(), "Announcement has been updated.");
	}

	public DataModificationResult<Long> uploadPhoto(Long id, List<MultipartFile> files) {
		var entity = announcementRepo.findById(id)
				.orElseThrow(() -> new ApiBusinessException("Invalid Announcement id."));
		
         photoUploadService.saveAnnouncementImages(id, files);
		
		return new DataModificationResult<Long>(entity.getAnnouncementId(), "Announcement has been updated.");
	}

	public List<AnnouncementDto> findAll() {
	    List<AnnouncementDto> result = Optional.ofNullable(announcementRepo.findAll())
	        .orElse(Collections.emptyList())
	        .stream()
	        .map(AnnouncementDto::toDto)
	        .collect(Collectors.toList());

	    if (result.isEmpty()) {
	        throw new ApiBusinessException("No announcements found.");
	    }

	    return result;
	}
	
	public List<AnnouncementShowLessDto> showLess() {
	    List<AnnouncementShowLessDto> result = Optional.ofNullable(announcementRepo.findAll())
	        .orElse(Collections.emptyList())
	        .stream()
	        .map(AnnouncementShowLessDto::toShowLessDto)
	        .collect(Collectors.toList());

	    if (result.isEmpty()) {
	        throw new ApiBusinessException("No announcements found.");
	    }

	    return result;
	}

	@Transactional(readOnly = true)
	public AnnouncementDto findById(Long id) {
		return announcementRepo.findById(id)
				.map(AnnouncementDto::toDto)
				.orElseThrow(() -> new ApiBusinessException("Invalid Announcement id."));
	}

	@Transactional(readOnly = true)
	@SuppressWarnings("deprecation")
	public List<AnnouncementSearchDto> search(AnnouncementSearch form) {
	    StringBuilder sql = new StringBuilder("SELECT " +
	            "a1.announcement_id, " +
	            "a1.title, " +
	            "a1.content, " +
	            "t2.file_path_name, " +
	            "t2.title_file_path_name, " +
	            "(SELECT GROUP_CONCAT(MT.name SEPARATOR ', ') " +
	            "FROM media_tags MT " +
	            "INNER JOIN announcement_tags AT " +
	            "ON AT.tag_id = MT.id " +
	            "WHERE AT.announcement_id = a1.announcement_id) AS tagName, " +
	            "a1.link, " +
	            "a1.file_name, " +
	            "a1.post_date, " +
	            "a1.post_time " +
	            "FROM announcements a1 " +
	            "LEFT JOIN media t2 ON a1.announcement_id = t2.announcement_id " +
	            "LEFT JOIN announcement_tags AT ON a1.announcement_id = AT.announcement_id " +
	            "LEFT JOIN media_tags MT ON AT.tag_id = MT.id " +
	            "WHERE 1=1");

	    List<Object> params = new ArrayList<>();
	    
	    if (form.getAnnouncementId() != null) {
	        sql.append(" AND a1.announcement_id = ?");
	        params.add(form.getAnnouncementId());
	    }
	    
	    if (form.getTitle() != null && !form.getTitle().isEmpty()) {
	        sql.append(" AND LOWER(a1.title) LIKE LOWER(CONCAT('%', ?, '%'))");
	        params.add(form.getTitle());
	    }
	    
	    if (form.getTags() != null && !form.getTags().isEmpty()) {
	        sql.append(" AND LOWER(MT.name) LIKE LOWER(CONCAT('%', ?, '%'))");
	        params.add(form.getTags());
	    }

	    return jdbcTemplate.query(
	            sql.toString(),
	            params.toArray(),
	            new AnnouncementSearchDtoRowMapper());
	}





	public DataModificationResult<Long> createTags(TagsForm form) {
	    var name = form.getName();

	    var existingTag = tagRepo.findByName(name);
	    if (existingTag.isPresent()) {
	        return new DataModificationResult<>(existingTag.get().getId(), "Tag already exists.");
	    }

	    var entity = form.entity();
	    entity = tagRepo.save(entity);

	    return new DataModificationResult<>(entity.getId(), "Tag has been created.");
	}




}
