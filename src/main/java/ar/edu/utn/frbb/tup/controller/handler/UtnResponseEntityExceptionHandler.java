package ar.edu.utn.frbb.tup.controller.handler;

import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.ALumnoNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoBadRequestException;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaBadRequestException;
import ar.edu.utn.frbb.tup.persistence.exception.CarreraBadRequestException;
import ar.edu.utn.frbb.tup.persistence.exception.CarreraNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaBadRequestException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class UtnResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    // Manejo de NOT FOUND (404)
    @ExceptionHandler({ MateriaNotFoundException.class })
    protected ResponseEntity<Object> handleMateriaNotFound(MateriaNotFoundException ex, WebRequest request) {
        CustomApiError error = new CustomApiError();
        error.setErrorMessage(ex.getMessage());
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({ ALumnoNotFoundException.class })
    protected ResponseEntity<Object> handleAlumnoNotFound(ALumnoNotFoundException ex, WebRequest request) {
        CustomApiError error = new CustomApiError();
        error.setErrorMessage(ex.getMessage());
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({ CarreraNotFoundException.class })
    protected ResponseEntity<Object> handleCarreraNotFound(CarreraNotFoundException ex, WebRequest request) {
        CustomApiError error = new CustomApiError();
        error.setErrorMessage(ex.getMessage());
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    // Manejo de CONFLICT (409)
    @ExceptionHandler({ IllegalArgumentException.class, IllegalStateException.class })
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        CustomApiError error = new CustomApiError();
        error.setErrorCode(1234);
        error.setErrorMessage(ex.getMessage());
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    // Manejo de BAD REQUEST (400)
    @ExceptionHandler({ AlumnoBadRequestException.class, AsignaturaBadRequestException.class, MateriaBadRequestException.class })
    protected ResponseEntity<Object> handleBadRequestExceptions(Exception ex, WebRequest request) {
        CustomApiError error = new CustomApiError();
        error.setErrorMessage(ex.getMessage());
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({ CarreraBadRequestException.class })
    protected ResponseEntity<Object> handleCarreraBadRequest(CarreraBadRequestException ex, WebRequest request) {
        CustomApiError error = new CustomApiError();
        error.setErrorMessage(ex.getMessage());
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body,
                                                             HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (body == null) {
            CustomApiError error = new CustomApiError();
            error.setErrorMessage(ex.getMessage());
            body = error;
        }
        return new ResponseEntity<>(body, headers, status);
    }
}
