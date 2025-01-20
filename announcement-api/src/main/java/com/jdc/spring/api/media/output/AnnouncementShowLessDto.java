package com.jdc.spring.api.media.output;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import com.jdc.spring.model.constants.Role;
import com.jdc.spring.model.entity.Announcement;
import com.jdc.spring.model.entity.Tag;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementShowLessDto {

    private Long announcementId;
    private String title;
    private String content;
    private String base64TitleImage;
    private String tags;
    private LocalDate postDate;
    private Role role;

    public static AnnouncementShowLessDto toShowLessDto(Announcement entity) {
        String base64TitleImage = Optional.ofNullable(
                entity.getMediaFiles()
                    .stream()
                    .filter(media -> media.getTitleFilePathName() != null)
                    .findFirst()
                    .orElse(null)
            )
            .map(titleMedia -> encodeToBase64(titleMedia.getTitleFilePathName()))
            .orElse(null);

        return new AnnouncementShowLessDto(
            entity.getAnnouncementId(),
            entity.getTitle(),
            entity.getContent(),
            base64TitleImage,
            entity.getTags(),
            entity.getPostDate(),
            entity.getPostedBy() != null ? entity.getPostedBy().getRole() : Role.Admin
        );
    }

    private static String encodeToBase64(String filePath) {
        if (filePath == null || filePath.isBlank()) {
            return null;
        }
        try {
            Path path = Paths.get(filePath);
            if (Files.exists(path)) {
                byte[] fileBytes = Files.readAllBytes(path);
                return java.util.Base64.getEncoder().encodeToString(fileBytes);
            }
        } catch (Exception e) {
            System.err.println("Error encoding file to Base64: " + filePath + " - " + e.getMessage());
        }
        return null;
    }

	public AnnouncementShowLessDto(Long announcementId2, String title2, String content2, String base64TitleImage2,
			Set<Tag> tags2, LocalDate postDate2, Role role2) {
	}
}
