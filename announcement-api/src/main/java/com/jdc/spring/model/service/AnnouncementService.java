package com.jdc.spring.model.service;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.jdc.spring.api.media.input.AnnouncementForm;
import com.jdc.spring.api.media.input.AnnouncementSearch;
import com.jdc.spring.api.media.output.AnnouncementDto;
import com.jdc.spring.model.entity.Announcement;
import com.jdc.spring.model.entity.Announcement_;
import com.jdc.spring.model.repo.AccountRepo;
import com.jdc.spring.model.repo.AnnouncementRepo;
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

	private final AccountRepo accountRepo;

	public DataModificationResult<Long> create(AnnouncementForm form) {		
		var entity = form.entity();
		entity = announcementRepo.save(entity);
		return new DataModificationResult<Long>(entity.getAnnouncementId(), "Announcement has been created.");
	}

	public DataModificationResult<Long> update(Long id, AnnouncementForm form) {
		var entity = announcementRepo.findById(id)
				.orElseThrow(() -> new ApiBusinessException("Invalid Announcement id."));
		entity.setAnnouncementId(id);
		entity.setTitle(form.getTitle());
		entity.setContent(form.getContent());
    	entity.setTags(form.getTags());
    	entity.setLink(form.getLink());
    	entity.setFileName(form.getFileName());
    	entity.setPostDate(LocalDate.now());
    	entity.setPostTime(LocalDateTime.now());
    	entity.setType(form.getType());
    	entity.setPostedBy(form.getPostedBy());
		entity = announcementRepo.saveAndFlush(entity);

		return new DataModificationResult<Long>(entity.getAnnouncementId(), "Announcement has been updated.");
	}

	public DataModificationResult<Long> uploadPhoto(Long id, List<MultipartFile> files) {
		var entity = announcementRepo.findById(id)
				.orElseThrow(() -> new ApiBusinessException("Invalid Announcement id."));
		
		var images = photoUploadService.saveAnnouncementImages(id, files);
		entity.getImages().addAll(images);
		
		return new DataModificationResult<Long>(entity.getAnnouncementId(), "Announcement has been updated.");
	}


	/*
	 * @Transactional(readOnly = true) public Page<AnnouncementDto>
	 * search(AnnouncementSearch form, int page, int size) {
	 * 
	 * Function<CriteriaBuilder, CriteriaQuery<Long>> countFunc = cb -> { var cq =
	 * cb.createQuery(Long.class); var root = cq.from(Announcement.class);
	 * cq.select(cb.count(root.get(Announcement_.ANNOUNCEMENT_ID)));
	 * 
	 * cq.where(form.where(cb, root)); return cq; };
	 * 
	 * Function<CriteriaBuilder, CriteriaQuery<AnnouncementDto>> queryFunc = cb -> {
	 * var cq = cb.createQuery(Announcement.class); var root =
	 * cq.from(Announcement.class); AnnouncementDto.select(cq, root);
	 * cq.where(form.where(cb, root));
	 * 
	 * cq.orderBy(cb.asc(root.get(Catalog_.name)));
	 * 
	 * return cq; };
	 * 
	 * return catalogRepo.search(queryFunc, countFunc, page, size); }
	 */

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
}
