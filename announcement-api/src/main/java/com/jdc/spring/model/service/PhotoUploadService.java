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
import org.springframework.transaction.annotation.Transactional;
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

	@Transactional
	public List<String> saveAnnouncementImages(Long id, List<MultipartFile> files) {
		if (files == null || files.isEmpty()) {
			throw new ApiBusinessException("No files provided to save.");
		}

		List<String> savedImageNames = new ArrayList<>();
		List<String> filePaths = new ArrayList<>();

		Announcement announcement = announcementRepo.findById(id)
				.orElseThrow(() -> new ApiBusinessException("Announcement not found with id: " + id));

		int size = files.size();
		for (int i = 0; i < size; i++) {
			MultipartFile file = files.get(i);

			try {
				String extension = getFileExtension(file);
				String dateTime = LocalDateTime.now().format(DF);
				String imageName;

				if (i == 0) {
					imageName = "title_anno_%s_%s_%s.%s".formatted(dateTime, id, i + 1, extension);
				} else {
					imageName = "anno_%s_%s_%s.%s".formatted(dateTime, id, i + 1, extension);
				}

				Path imageFolderPath = Path.of(imageFolder);
				if (!Files.exists(imageFolderPath)) {
					Files.createDirectories(imageFolderPath);
				}

				Files.copy(file.getInputStream(), imageFolderPath.resolve(imageName),
						StandardCopyOption.REPLACE_EXISTING);

				String filePath = imageFolder + "/" + imageName;
				filePaths.add(filePath);

				savedImageNames.add(imageName);

			} catch (IOException e) {
				throw new ApiBusinessException(
						"Error saving file: " + file.getOriginalFilename() + ". " + e.getMessage());
			}
		}

		Media media = new Media();
		media.setTitleFilePathName(filePaths.get(0));
		media.setFilePathName(String.join(",", filePaths));
		media.setAnnouncement(announcement);

		mediaRepo.save(media);

		return savedImageNames;
	}

	private String getFileExtension(MultipartFile file) {
		var fileName = file.getOriginalFilename();
		var array = fileName.split("\\.");
		return array[array.length - 1];
	}
}
