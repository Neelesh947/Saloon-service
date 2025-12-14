package com.exception.handling;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.exception.handling.constants.ExceptionCodes;
import com.exception.handling.models.BusinessException;
import com.exception.handling.models.ForbiddenException;
import com.exception.handling.models.NotFoundException;
import com.exception.handling.models.UnauthorizedException;
import com.exception.handling.models.ValidationException;
import com.exception.handling.utils.ApiErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ApiErrorResponse> handleNotFound(NotFoundException ex, HttpServletRequest request) {
		return buildResponse(ex.getMessage(), ExceptionCodes.RESOURCE_NOT_FOUND_MSG, HttpStatus.NOT_FOUND, request);
	}

	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<ApiErrorResponse> handleValidation(ValidationException ex, HttpServletRequest req) {
		return buildResponse(ex.getMessage(), ExceptionCodes.VALIDATION_ERROR_MSG, HttpStatus.BAD_REQUEST, req);
	}

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ApiErrorResponse> handleBusiness(BusinessException ex, HttpServletRequest req) {
		return buildResponse(ex.getMessage(), ExceptionCodes.BUSINESS_ERROR_MSG, HttpStatus.BAD_REQUEST, req);
	}

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<ApiErrorResponse> handleUnauthorized(UnauthorizedException ex, HttpServletRequest req) {
		return buildResponse(ex.getMessage(), ExceptionCodes.UNAUTHORIZED_MSG, HttpStatus.UNAUTHORIZED, req);
	}

	@ExceptionHandler(ForbiddenException.class)
	public ResponseEntity<ApiErrorResponse> handleForbidden(ForbiddenException ex, HttpServletRequest req) {
		return buildResponse(ex.getMessage(), ExceptionCodes.FORBIDDEN_MSG, HttpStatus.FORBIDDEN, req);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiErrorResponse> handleGeneral(Exception ex, HttpServletRequest req) {
		return buildResponse("Something went wrong", ExceptionCodes.INTERNAL_SERVER_ERROR_MSG,
				HttpStatus.INTERNAL_SERVER_ERROR, req);
	}

	private ResponseEntity<ApiErrorResponse> buildResponse(String messsage, String code, HttpStatus status,
			HttpServletRequest request) {
		ApiErrorResponse response = ApiErrorResponse.builder().message(messsage).errorCode(code).status(status.value())
				.localDateTime(LocalDateTime.now()).path(request.getRequestURI()).build();
		return new ResponseEntity<>(response, status);
	}
}
