package com.jdc.spring.model.service;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.jdc.spring.api.media.input.AnnouncementForm;
import com.jdc.spring.api.media.input.AnnouncementSearch;
import com.jdc.spring.api.media.input.TagsForm;
import com.jdc.spring.api.media.output.AnnouncementDto;
import com.jdc.spring.api.media.output.AnnouncementShowLessDto;
import com.jdc.spring.model.entity.Announcement;
import com.jdc.spring.model.entity.Announcement_;
import com.jdc.spring.model.entity.Tag;
import com.jdc.spring.model.repo.AccountRepo;
import com.jdc.spring.model.repo.AnnouncementRepo;
import com.jdc.spring.model.repo.MediaRepo;
import com.jdc.spring.model.repo.TagRepo;
import com.jdc.spring.utils.exceptions.ApiBusinessException;
import com.jdc.spring.utils.io.DataModificationResult;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
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
	public Page<AnnouncementDto> search(AnnouncementSearch form, int page, int size) {
		
		Function<CriteriaBuilder, CriteriaQuery<Long>> countFunc = cb -> {
			var cq = cb.createQuery(Long.class);
			var root = cq.from(Announcement.class);
			cq.select(cb.count(root.get(Announcement_.announcementId)));
			
			cq.where(form.where(cb, root));
			return cq;
		};
		Function<CriteriaBuilder, CriteriaQuery<AnnouncementDto>> queryFunc = cb -> {
			var cq = cb.createQuery(AnnouncementDto.class);
			var root = cq.from(Announcement.class);
			AnnouncementDto.select(cb, cq, root);
			cq.where(form.where(cb, root));
			cq.orderBy(cb.asc(root.get(Announcement_.title)));
			return cq;
		};

		return announcementRepo.search(queryFunc, countFunc, page, size);
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
