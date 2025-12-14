package com.exception.handling.utils;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiErrorResponse {

	private String errorCode;
	private String message;
	private int status;
	private LocalDateTime localDateTime;
	private String path;
	private List<ErrorDetails> details;
}
