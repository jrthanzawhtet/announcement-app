package com.jdc.spring.model.input;



import com.jdc.spring.model.entity.District;

import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.Data;

@Data
public class DistrictDto {

	private int id;
	private String name;
	private StateDto state;

	public DistrictDto(int id, String name, int stateId, String stateName) {
		super();
		this.id = id;
		this.name = name;
		this.state = new StateDto(stateId, stateName);
	}

	/*
	 * public static void select(CriteriaQuery<DistrictDto> cq, Root<District> root)
	 * { cq.multiselect( root.get(District_.id), root.get(District_.name),
	 * root.get(District_.state).get(State_.id),
	 * root.get(District_.state).get(State_.name) ); }
	 */
}