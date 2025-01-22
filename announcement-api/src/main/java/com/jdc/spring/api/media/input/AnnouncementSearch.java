package com.jdc.spring.api.media.input;

import java.util.ArrayList;

import org.springframework.util.StringUtils;

import com.jdc.spring.model.entity.Announcement;
import com.jdc.spring.model.entity.Announcement_;
import com.jdc.spring.model.entity.Tag_;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;

@Data
public class AnnouncementSearch {

	private Long announcementId;
	private String title;
	private String tags;
	

	public Predicate where(CriteriaBuilder cb, Root<Announcement> root) {
	    var list = new ArrayList<Predicate>();

	    if (Announcement_.announcementId != null) {
	        list.add(cb.equal(root.get(Announcement_.announcementId), announcementId));
	    }

	    if (StringUtils.hasLength(title)) {
	        list.add(cb.like(cb.lower(root.get(Announcement_.title)), title.toLowerCase() + "%"));
	    }

	    if (StringUtils.hasLength(tags)) {
	        var tagJoin = root.join(Announcement_.tags);
	        list.add(cb.equal(cb.lower(tagJoin.get(Tag_.name)), tags.toLowerCase()));
	    }
	    
	    return cb.and(list.toArray(new Predicate[0]));
	}
	
	
}
