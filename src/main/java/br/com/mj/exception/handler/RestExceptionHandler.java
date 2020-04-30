package br.com.mj.exception.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	private MessageSource messages;

	private Locale locale;

	public RestExceptionHandler() {
		super();
		locale = LocaleContextHolder.getLocale();
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status,
			WebRequest request) {
		List<HandlerError> erros = Arrays
				.asList(new HandlerError(messages.getMessage("requisicao.invalida", null, locale), ex.getCause().getMessage()));

		return handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status,
			WebRequest request) {
		return handleExceptionInternal(ex, criarErros(ex.getBindingResult()), headers, HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler(EmptyResultDataAccessException.class)
	protected ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex, WebRequest request) {
		List<HandlerError> erros = Arrays.asList(new HandlerError(messages.getMessage("requisicao.sem-conteudo", null, locale), ex.toString()));

		return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	protected ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
		List<HandlerError> errors = Arrays.asList(
				new HandlerError(messages.getMessage("requisicao.operacao-nao-permitida", null, locale), ExceptionUtils.getRootCauseMessage(ex)));
		return handleExceptionInternal(ex, errors, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
	
	private List<HandlerError> criarErros(BindingResult binding) {

		List<HandlerError> erros = new ArrayList<>();

		for (FieldError fError : binding.getFieldErrors()) {
			String mensagemUsuario = messages.getMessage(fError, locale);
			String mensagemDesenvolvedor = fError.toString();

			erros.add(new HandlerError(mensagemUsuario, mensagemDesenvolvedor));
		}

		return erros;
	}
}
