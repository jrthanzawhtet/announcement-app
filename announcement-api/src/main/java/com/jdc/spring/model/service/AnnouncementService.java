package com.jdc.spring.model.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jdc.spring.model.entity.Account;
import com.jdc.spring.model.entity.Announcement;
import com.jdc.spring.model.entity.AnnouncementHistory;
import com.jdc.spring.model.repo.AccountRepo;
import com.jdc.spring.model.repo.AnnouncementHistoryRepo;
import com.jdc.spring.model.repo.AnnouncementRepo;

@Service
public class AnnouncementService {

	 @Autowired
	    private AnnouncementRepo announcementRepo;

	    @Autowired
	    private AccountRepo accountRepo;  
	    
	    @Autowired
	    private AnnouncementHistoryRepo historyRepo;

	    public Announcement saveAnnouncement(String title, String content, String photoPath, String videoPath,
	            String loginId) { 

	        Account account = accountRepo.findById(loginId).orElseThrow(() -> new RuntimeException("User not found"));

	        Announcement announcement = new Announcement();
	        announcement.setTitle(title);
	        announcement.setContent(content);
	        announcement.setPhotoPath(photoPath);
	        announcement.setVideoPath(videoPath);
	        announcement.setPostDate(LocalDate.now());
	        announcement.setPostTime(LocalTime.now());
	        announcement.setPostedBy(account); 

	        return announcementRepo.save(announcement);
	    }
	    
	    public List<Announcement> getAllAnnouncements() {
	        return announcementRepo.findAll();
	    }

		/*
		 * public List<Announcement> getNewAnnouncementsForUser(Account user) { return
		 * announcementRepo.findByNotInHistory(user.getLoginId()); }
		 */

	    public void saveViewedAnnouncement(Account user, Announcement announcement) {
	        AnnouncementHistory history = new AnnouncementHistory();
	        history.setUser(user);
	        history.setAnnouncement(announcement);
	        history.setViewedAt(LocalDateTime.now());
	        historyRepo.save(history);
	    }

		public Optional<Announcement> findById(Long id) {
			return announcementRepo.findById(id);
		}

}
