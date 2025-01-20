package com.jdc.spring.api.media.input;

import java.time.LocalDate;
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
	private LocalDate createFrom;
	private LocalDate createTo;

	public Predicate where(CriteriaBuilder cb, Root<Announcement> root) {
	    var list = new ArrayList<Predicate>();

	    if (null != announcementId && announcementId > 0) {
	        list.add(cb.equal(root.get(Announcement_.announcementId), announcementId));
	    }

	    if (StringUtils.hasLength(title)) {
	        list.add(cb.like(cb.lower(root.get(Announcement_.title)), title.toLowerCase().concat("%")));
	    }

	    if (StringUtils.hasLength(tags)) {
	        list.add(cb.like(cb.lower(root.join(Announcement_.tags).get(Tag_.name)), tags.toLowerCase().concat("%")));
	    }

	    if (null != createFrom) {
	        list.add(cb.greaterThanOrEqualTo(root.get(Announcement_.createAt), createFrom.atStartOfDay()));
	    }

	    if (null != createTo) {
	        list.add(cb.lessThanOrEqualTo(root.get(Announcement_.createAt), createTo.atStartOfDay().plusDays(1)));
	    }

	    return cb.and(list.toArray(new Predicate[0]));
	}


}
