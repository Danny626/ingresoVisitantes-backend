package com.albo.exception;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.albo.exception.list.AlreadyExistException;
import com.albo.exception.list.ModelNotFoundException;
import com.albo.exception.list.ModelUnSupportedFieldPatchException;
import com.albo.exception.list.PersistException;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

	// Let Spring BasicErrorController handle the exception, we just override the
	// status code
	@ExceptionHandler(ModelNotFoundException.class)
	public void springHandleNotFound(HttpServletResponse response) throws IOException {
		response.sendError(HttpStatus.NOT_FOUND.value());
	}

	/*
	 * @ExceptionHandler(BookNotFoundException.class) public
	 * ResponseEntity<CustomErrorResponse> customHandleNotFound(Exception ex,
	 * WebRequest request) {
	 * 
	 * CustomErrorResponse errors = new CustomErrorResponse();
	 * errors.setTimestamp(LocalDateTime.now()); errors.setError(ex.getMessage());
	 * errors.setStatus(HttpStatus.NOT_FOUND.value());
	 * 
	 * return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
	 * 
	 * }
	 */

//	@ExceptionHandler(AlreadyExistsException.class)
//	public ResponseEntity<CustomErrorResponse> customHandleNotFound(Exception ex, WebRequest request) {
//
//		CustomErrorResponse errors = new CustomErrorResponse();
//		errors.setTimestamp(LocalDateTime.now());
//		errors.setError(ex.getMessage());
//		errors.setStatus(HttpStatus.CONFLICT.value());
//
//		return new ResponseEntity<>(errors, HttpStatus.CONFLICT);
//
//	}

	/* maneja los internalError 500 */
	@ExceptionHandler
	@ResponseBody
	public CustomErrorResponse handleException(Exception ex, HttpServletRequest request) {

		StringWriter sw = new StringWriter();
		ex.printStackTrace(new PrintWriter(sw));

		return new CustomErrorResponse(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
				ex.getClass().getName(), ex.getMessage(), request.getRequestURI(), sw.toString());
	}

	/* maneja los errores conflict 409 */
	@ExceptionHandler(AlreadyExistException.class)
	public ResponseEntity<CustomErrorResponse> alreadyExistException(Exception ex, HttpServletRequest request) {

		StringWriter sw = new StringWriter();
		ex.printStackTrace(new PrintWriter(sw));

		return new ResponseEntity<>(new CustomErrorResponse(LocalDateTime.now(), HttpStatus.CONFLICT.value(),
				ex.getClass().getName(), ex.getMessage(), request.getRequestURI(), sw.toString()), HttpStatus.CONFLICT);
	}

	/* maneja los errores al persistir un objeto */
	@ExceptionHandler(PersistException.class)
	public ResponseEntity<CustomErrorResponse> persistException(Exception ex, HttpServletRequest request) {

		StringWriter sw = new StringWriter();
		ex.printStackTrace(new PrintWriter(sw));

		return new ResponseEntity<>(
				new CustomErrorResponse(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
						ex.getClass().getName(), ex.getMessage(), request.getRequestURI(), sw.toString()),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/* maneja los errores Bad Request 400 */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		StringWriter sw = new StringWriter();
		ex.printStackTrace(new PrintWriter(sw));

		return new ResponseEntity<>(
				new CustomErrorResponse(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
						ex.getClass().getName(), ex.getMessage(), request.getContextPath(), sw.toString()),
				HttpStatus.INTERNAL_SERVER_ERROR);

	}

	/* maneja los errores del rest */
	@ExceptionHandler(RestClientException.class)
	public ResponseEntity<CustomErrorResponse> restClientException(Exception ex, HttpServletRequest request) {

		StringWriter sw = new StringWriter();
		ex.printStackTrace(new PrintWriter(sw));

		return new ResponseEntity<>(new CustomErrorResponse(LocalDateTime.now(), HttpStatus.CONFLICT.value(),
				ex.getClass().getName(), ex.getMessage(), request.getRequestURI(), sw.toString()), HttpStatus.CONFLICT);
	}

	@ExceptionHandler(ModelUnSupportedFieldPatchException.class)
	public void springUnSupportedFieldPatch(HttpServletResponse response) throws IOException {
		response.sendError(HttpStatus.METHOD_NOT_ALLOWED.value());
	}

	// @Validate For Validating Path Variables and Request Parameters
	@ExceptionHandler(ConstraintViolationException.class)
	public void constraintViolationException(HttpServletResponse response) throws IOException {
		response.sendError(HttpStatus.BAD_REQUEST.value());
	}

	// error handle for @Valid
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", new Date());
		body.put("status", status.value());

		// Get all errors
		List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(x -> x.getDefaultMessage())
				.collect(Collectors.toList());

		body.put("errors", errors);

		return new ResponseEntity<>(body, headers, status);

		// Map<String, String> fieldErrors =
		// ex.getBindingResult().getFieldErrors().stream().collect(
		// Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

	}

}
