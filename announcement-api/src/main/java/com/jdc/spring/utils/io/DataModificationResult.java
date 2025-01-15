package com.jdc.spring.utils.io;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataModificationResult<T> {

	private T id;

	private String message;

}