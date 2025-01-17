package com.jdc.spring.model.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jdc.spring.model.entity.Announcement;
import com.jdc.spring.model.entity.Media;
import com.jdc.spring.model.repo.AnnouncementRepo;
import com.jdc.spring.model.repo.MediaRepo;
import com.jdc.spring.utils.exceptions.ApiBusinessException;

@Service
public class PhotoUploadService {

	@Value("${app.image.folder}")
	private String imageFolder;

	@Autowired
	private AnnouncementRepo announcementRepo;

	@Autowired
	private MediaRepo mediaRepo;

	private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

	public String saveProfileImage(int id, MultipartFile file) {

		try {
			var extension = getFileExtension(file);
			var dateTime = LocalDateTime.now().format(DF);
			var imageName = "profile_%s_%s.%s".formatted(dateTime, id, extension);

			var imageFolderPath = Path.of(imageFolder);
			if (!Files.exists(imageFolderPath)) {
				Files.createDirectories(imageFolderPath);
			}

			Files.copy(file.getInputStream(), imageFolderPath.resolve(imageName), StandardCopyOption.REPLACE_EXISTING);

			return imageName;
		} catch (IOException e) {
			throw new ApiBusinessException(e.getMessage());
		}
	}

	public List<String> saveAnnouncementImages(Long announcementId, List<MultipartFile> files) {
		var list = new ArrayList<String>();

		for (int i = 0; i < files.size(); i++) {
			list.add(saveAnnouncementImage(announcementId, files.get(i), i));
		}

		return list;
	}

	public String saveAnnouncementImage(Long id, MultipartFile file, int index) {

		try {
			var extension = getFileExtension(file);
			var dateTime = LocalDateTime.now().format(DF);
			var imageName = "announcement_%s_%s_%s.%s".formatted(dateTime, id, index + 1, extension);

			var imageFolderPath = Path.of(imageFolder);
			if (!Files.exists(imageFolderPath)) {
				Files.createDirectories(imageFolderPath);
			}

			Files.copy(file.getInputStream(), imageFolderPath.resolve(imageName), StandardCopyOption.REPLACE_EXISTING);

			Media media = new Media();
	        media.setFilePathName(imageFolder + "/" + imageName);

	        Announcement announcement = announcementRepo.findById(id)
	            .orElseThrow(() -> new ApiBusinessException("Announcement not found with id: " + id));
	        media.setAnnouncement(announcement);

	        mediaRepo.save(media); 
			return imageName;
		} catch (IOException e) {
			throw new ApiBusinessException(e.getMessage());
		}
	}

	private String getFileExtension(MultipartFile file) {
		var fileName = file.getOriginalFilename();
		var array = fileName.split("\\.");
		return array[array.length - 1];
	}
}
