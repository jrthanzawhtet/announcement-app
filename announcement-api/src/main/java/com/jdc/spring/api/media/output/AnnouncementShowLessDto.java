package com.jdc.spring.api.media.output;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.jdc.spring.model.constants.Role;
import com.jdc.spring.model.entity.Announcement;
import com.jdc.spring.model.entity.Tag;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AnnouncementShowLessDto {

    private Long announcementId;
    private String title;
    private String content;
    private String base64TitleImage;
    private List<String> tags;
    private LocalDate postDate;
    private Role role;

    public static AnnouncementShowLessDto toShowLessDto(Announcement entity) {
        String base64TitleImage = entity.getMediaFiles()
            .stream()
            .filter(media -> media.getTitleFilePathName() != null)
            .findFirst()
            .map(media -> encodeToBase64(media.getTitleFilePathName()))
            .orElse(null);

        List<String> tagNames = Optional.ofNullable(entity.getTags())
                .orElse(Set.of())
                .stream()
                .map(Tag::getName)
                .collect(Collectors.toList());

        return new AnnouncementShowLessDto(
            entity.getAnnouncementId(),
            entity.getTitle(),
            entity.getContent(),
            base64TitleImage,
            tagNames, 
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

    public AnnouncementShowLessDto(Long announcementId, String title, String content, String base64TitleImage,
                                   List<String> tags, LocalDate postDate, Role role) {
        this.announcementId = announcementId;
        this.title = title;
        this.content = content;
        this.base64TitleImage = base64TitleImage;
        this.tags = tags; 
        this.postDate = postDate;
        this.role = role;
    }
}
