package com.jdc.spring.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jdc.spring.model.constants.MediaType;
import com.jdc.spring.model.entity.Announcement;
import com.jdc.spring.model.entity.Media;
import com.jdc.spring.model.repo.MediaRepo;

@Service
public class MediaService {

	@Autowired
	private MediaRepo mediaRepo;

	public Media saveMedia(Announcement announcement, byte[] data, String fileName, MediaType type) {
		Media media = new Media();
		media.setAnnouncement(announcement);
		media.setData(data);
		media.setFileName(fileName);
		media.setType(type);

		return mediaRepo.save(media);
	}

}
