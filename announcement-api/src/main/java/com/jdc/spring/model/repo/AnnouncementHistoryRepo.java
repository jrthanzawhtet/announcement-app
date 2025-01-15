package com.jdc.spring.model.repo;

import java.util.List;

import com.jdc.spring.model.BaseRepository;
import com.jdc.spring.model.entity.AnnouncementHistory;

public interface AnnouncementHistoryRepo extends BaseRepository<AnnouncementHistory, Long> {
	
    List<AnnouncementHistory> findByUser_LoginId(String loginId);

}
