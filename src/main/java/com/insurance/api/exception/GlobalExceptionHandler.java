package com.insurance.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Manejador global de excepciones para la API REST.
 * <p>
 * Intercepta y maneja las excepciones lanzadas por los controladores,
 * proporcionando respuestas de error estandarizadas en formato JSON.
 * </p>
 * <ul>
 *   <li>{@link BusinessException} - Retorna estado HTTP 400 (Bad Request)</li>
 *   <li>Otras excepciones - Retorna estado HTTP 500 (Internal Server Error)</li>
 * </ul>
 *
 * @author Insurance API Team
 * @version 1.0.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja las excepciones de tipo {@link BusinessException}.
     * <p>
     * Retorna una respuesta HTTP 400 con un cuerpo JSON que incluye
     * la marca de tiempo y el mensaje de error.
     * </p>
     *
     * @param ex la excepción de negocio capturada
     * @return respuesta HTTP con estado 400 y detalles del error
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> handleBusiness(BusinessException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "message", ex.getMessage()
                ));
    }

    /**
     * Maneja las excepciones generales no capturadas específicamente.
     * <p>
     * Retorna una respuesta HTTP 500 con un cuerpo JSON que incluye
     * la marca de tiempo y el mensaje de error. También imprime
     * el stack trace para fines de depuración.
     * </p>
     *
     * @param ex la excepción capturada
     * @return respuesta HTTP con estado 500 y detalles del error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneral(Exception ex) {

        ex.printStackTrace();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "message", ex.getMessage()
                ));
    }
}