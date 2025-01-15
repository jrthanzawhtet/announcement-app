package com.jdc.spring.model.entity;

import com.jdc.spring.model.constants.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "account")
public class Account {

	@Id
	private String loginId;

	private String name;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private Role role;

	public Account(String loginId) {
		this.loginId = loginId;
	}

	public Account() {
	}

}