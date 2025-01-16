package com.jdc.spring.model.repo;

import java.util.List;

import com.jdc.spring.model.BaseRepository;
import com.jdc.spring.model.entity.Announcement;
import com.jdc.spring.model.entity.AnnouncementAttribute;

public interface AnnouncementAttributeRepo extends BaseRepository<Announcement, Long>{

	List<AnnouncementAttribute> findByAnnouncementId(Long announcementId);

}
