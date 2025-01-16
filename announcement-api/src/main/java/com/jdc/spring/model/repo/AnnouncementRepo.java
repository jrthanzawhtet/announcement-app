package com.jdc.spring.model.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jdc.spring.model.BaseRepository;
import com.jdc.spring.model.entity.Announcement;

public interface AnnouncementRepo extends BaseRepository<Announcement, Long> {
	
	@Query("SELECT a FROM Announcement a LEFT JOIN FETCH a.images WHERE a.announcementId = :id")
	Optional<Announcement> findWithImagesById(@Param("id") Long id);

}
