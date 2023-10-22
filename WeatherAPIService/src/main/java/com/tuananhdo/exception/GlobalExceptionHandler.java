package com.tuananhdo.exception;

import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import payload.ErrorDTO;

import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorDTO handleGenericException(Exception exception, WebRequest request) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setTimestamp(LocalDateTime.now());
        errorDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorDTO.setPath(request.getDescription(false));
        errorDTO.addError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        LOGGER.error(exception.getMessage(), exception);
        return errorDTO;
    }

    @ExceptionHandler(LocationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorDTO handleLocationNotFoundException(LocationNotFoundException exception, WebRequest request) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setTimestamp(LocalDateTime.now());
        errorDTO.setStatus(HttpStatus.NOT_FOUND.value());
        errorDTO.setPath(request.getDescription(false));
        errorDTO.addError(exception.getMessage());
        LOGGER.error(exception.getMessage(), exception);
        return errorDTO;
    }

    @ExceptionHandler({BadRequestException.class, GeolocationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorDTO handleGeolocationOrNumberFormatException(Exception exception, WebRequest request) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setTimestamp(LocalDateTime.now());
        errorDTO.setStatus(HttpStatus.BAD_REQUEST.value());
        errorDTO.setPath(request.getDescription(false));
        errorDTO.addError(exception.getMessage());
        return errorDTO;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NotNull MethodArgumentNotValidException exception,
                                                                  @NotNull HttpHeaders headers,
                                                                  @NotNull HttpStatusCode status,
                                                                  @NotNull WebRequest request) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setTimestamp(LocalDateTime.now());
        errorDTO.setStatus(HttpStatus.BAD_REQUEST.value());
        errorDTO.setPath(request.getDescription(false));
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        fieldErrors.stream()
                .map(FieldError::getDefaultMessage)
                .forEach(errorDTO::addError);
        return new ResponseEntity<>(errorDTO, headers, status);
    }
}
