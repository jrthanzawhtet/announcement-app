package com.jdc.spring.model.service;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jdc.spring.model.constants.MediaType;
import com.jdc.spring.model.entity.Account;
import com.jdc.spring.model.entity.Announcement;
import com.jdc.spring.model.entity.AnnouncementAttribute;
import com.jdc.spring.model.entity.Media;
import com.jdc.spring.model.repo.AccountRepo;
import com.jdc.spring.model.repo.AnnouncementAttributeRepo;
import com.jdc.spring.model.repo.AnnouncementRepo;
import com.jdc.spring.model.repo.MediaRepo;
import com.jdc.spring.utils.exceptions.ApiBusinessException;

import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnnouncementService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Value("${app.image.folder}")
	private String imageFolder;

	private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

	private final AnnouncementRepo announcementRepo;

	private final AccountRepo accountRepo;

	private final AnnouncementAttributeRepo announcementAttributeRepo;

	private final MediaRepo mediaRepo;

	public Announcement saveAnnouncement(String title, String content, String photoPath, MultipartFile file,
			String loginId) {

		Account account = accountRepo.findById(loginId).orElseThrow(() -> new RuntimeException("User not found"));

		Announcement announcement = new Announcement();
		announcement.setTitle(title);
		announcement.setContent(content);
		announcement.setPostDate(LocalDate.now());
		announcement.setPostTime(LocalTime.now());
		announcement.setPostedBy(account);
		announcement = announcementRepo.save(announcement);

		if (file != null) {
			Media photoMedia = new Media();
			photoMedia.setAnnouncement(announcement);
			photoMedia.setType(MediaType.PHOTO);
			photoMedia.setFileName(photoPath);
			mediaRepo.save(photoMedia);
		}

		return announcement;
	}

	public List<String> saveAnnouncementImages(int AnnouncementId, List<MultipartFile> files)
			throws java.io.IOException {
		var list = new ArrayList<String>();

		for (int i = 0; i < files.size(); i++) {
			list.add(saveAnnouncementImage(AnnouncementId, files.get(i), i));
		}

		return list;
	}

	public String saveAnnouncementImage(int id, MultipartFile file, int index) throws java.io.IOException {

		try {
			var extension = getFileExtension(file);
			var dateTime = LocalDateTime.now().format(DF);
			var imageName = "catalog_%s_%s_%s.%s".formatted(dateTime, id, index + 1, extension);

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

	private String getFileExtension(MultipartFile file) {
		var fileName = file.getOriginalFilename();
		var array = fileName.split("\\.");
		return array[array.length - 1];
	}

	public List<Announcement> getAllAnnouncements() {
		return announcementRepo.findAll();
	}

	public List<AnnouncementAttribute> getAttributesForAnnouncement(Long announcementId) {
		return announcementAttributeRepo.findByAnnouncementId(announcementId);
	}

	public Optional<Announcement> findById(Long id) {
		return announcementRepo.findById(id);
	}

}
