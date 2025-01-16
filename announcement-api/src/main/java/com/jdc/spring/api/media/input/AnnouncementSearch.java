package com.jdc.spring.api.media.input;

import java.time.LocalDate;

import com.jdc.spring.model.entity.Announcement;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
import lombok.Data;

@Data
public class AnnouncementSearch {
	
	private String title;
	private String tags;
	private LocalDate createFrom;
	private LocalDate createTo;
	
	public Expression<Boolean> where(CriteriaBuilder cb, Root<Announcement> root) {
		return null;
	}

}
