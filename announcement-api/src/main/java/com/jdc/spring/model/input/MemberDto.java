package com.jdc.spring.model.input;


import com.jdc.spring.member.output.MemberProfile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {

	private MemberProfile profile;


}