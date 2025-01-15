package com.jdc.spring.api.media;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jdc.spring.model.constants.MediaType;
import com.jdc.spring.model.entity.Announcement;
import com.jdc.spring.model.entity.Media;
import com.jdc.spring.model.service.AnnouncementService;
import com.jdc.spring.model.service.MediaService;
import com.jdc.spring.utils.io.ApiResponse;

import io.jsonwebtoken.io.IOException;

@RestController
@RequestMapping("/media")
public class MediaApi {
	
	@Autowired
    private MediaService mediaService;

    @Autowired
    private AnnouncementService announcementService;

    @PostMapping("/upload")
    public ApiResponse<?> uploadMedia(@RequestParam("annoId") Long annoId,
                                      @RequestParam("file") MultipartFile file,
                                      @RequestParam("type") MediaType type) throws IOException, java.io.IOException {
        try {
            Announcement announcement = announcementService.findById(annoId)
                    .orElseThrow(() -> new RuntimeException("Announcement not found"));

            Media media = mediaService.saveMedia(announcement, file.getBytes(), file.getOriginalFilename(), type);

            return ApiResponse.success(media);
        } catch (RuntimeException ex) {
            return ApiResponse.businessError("Error uploading media: " + ex.getMessage());
        }
    }

}
