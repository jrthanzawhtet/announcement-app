package com.jdc.spring.model.input;

import com.jdc.spring.model.entity.State;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StateDto {

	private int id;

	private String name;
	
	public static StateDto from(State entity) {
		return new StateDto(entity.getId(), entity.getName());
	}

}