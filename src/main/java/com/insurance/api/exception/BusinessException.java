package com.insurance.api.exception;

/**
 * Excepción personalizada para errores de negocio en el sistema de seguros.
 * <p>
 * Se lanza cuando ocurre una violación de las reglas de negocio,
 * como intentar eliminar un cliente con pólizas activas o añadir
 * más beneficiarios de los permitidos.
 * </p>
 *
 * @author Insurance API Team
 * @version 1.0.0
 */
public class BusinessException extends RuntimeException {

    /**
     * Crea una nueva excepción de negocio con el mensaje especificado.
     *
     * @param message mensaje descriptivo del error de negocio
     */
    public BusinessException(String message) {
        super(message);
    }
}