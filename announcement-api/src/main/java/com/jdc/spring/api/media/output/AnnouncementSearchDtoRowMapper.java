package com.jdc.spring.api.media.output;

import org.springframework.jdbc.core.RowMapper;

import java.io.File;
import java.nio.file.Files;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnnouncementSearchDtoRowMapper implements RowMapper<AnnouncementSearchDto> {

    @Override
    public AnnouncementSearchDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        AnnouncementSearchDto dto = new AnnouncementSearchDto();
        
        dto.setAnnouncementId(rs.getLong("announcement_id"));
        dto.setTitle(rs.getString("title"));
        dto.setContent(rs.getString("content"));
        
        String filePathName = rs.getString("file_path_name");
        if (filePathName != null) {
            dto.setBase64Images(encodeFilesToBase64(filePathName));
        }
        
        String titleFilePathName = rs.getString("title_file_path_name");
        if (titleFilePathName != null) {
            dto.setBase64TitleImage(encodeToBase64(titleFilePathName));
        }
        
        String tagName = rs.getString("tagName");
        if (tagName != null) {
            dto.setTags(List.of(tagName.split(","))); 
        }
        
        dto.setLink(rs.getString("link"));
        dto.setFileName(rs.getString("file_name"));
        dto.setPostDate(rs.getDate("post_date").toLocalDate());
        
        return dto;
    }

    private String encodeToBase64(String filePath) {
        try {
            File file = new File(filePath); 
            if (!file.exists()) {
                return null; 
            }
            byte[] fileContent = Files.readAllBytes(file.toPath());
            return Base64.getEncoder().encodeToString(fileContent);
        } catch (Exception e) {
            e.printStackTrace();
            return null; 
        }
    }

    private List<String> encodeFilesToBase64(String filePaths) {
        return Optional.ofNullable(filePaths)
                .map(path -> Stream.of(path.split(","))
                        .map(this::encodeToBase64)
                        .filter(base64 -> base64 != null)
                        .collect(Collectors.toList()))
                .orElse(List.of());
    }
}
