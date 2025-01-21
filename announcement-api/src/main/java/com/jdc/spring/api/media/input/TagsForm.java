package com.jdc.spring.api.media.input;

import com.jdc.spring.model.entity.Tag;

import lombok.Data;

@Data
public class TagsForm {
	
	private Long id;

	private String name;
	
	public Tag entity() {
		var entity = new Tag();
		entity.setId(id);
		entity.setName(name);
		return entity;
	}
}
