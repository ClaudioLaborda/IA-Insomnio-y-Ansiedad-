package tuti.daos.error;

import java.util.List;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import tuti.daos.excepciones.Excepcion;


@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(Excepcion.class)
    public ResponseEntity<ErrorAtributo> methodArgumentNotValidException(HttpServletRequest request, Excepcion e) {
        return ResponseEntity.status(e.getStatusCode())
                .body(new ErrorAtributo(e.getAtributo(), e.getMensaje()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorAtributo>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ErrorAtributo> errores = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new ErrorAtributo(error.getField(), error.getDefaultMessage()))
                .toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errores);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorAtributo> handleJsonMalFormado(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorAtributo("json", "El formato del JSON enviado es inválido o contiene tipos de datos incorrectos."));
    }

    //<---- ---->
     @org.springframework.web.bind.annotation.ExceptionHandler(FacturaExcepcion.class)
    public ResponseEntity<ErrorAtributo> manejarExcepcion(HttpServletRequest request, FacturaExcepcion e) {
        return ResponseEntity
                .status(e.getStatusCode())
                .body(new ErrorAtributo(e.getAtributo(), e.getMensaje()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorAtributo> manejarNoEncontrado(HttpServletRequest request, EntityNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorAtributo("id", e.getMessage()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorAtributo> manejarConflicto(HttpServletRequest request, IllegalStateException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorAtributo("estado", e.getMessage()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorAtributo> manejarArgumentoInvalido(HttpServletRequest request, IllegalArgumentException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorAtributo("request", e.getMessage()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorAtributo> manejarValidacionDto(HttpServletRequest request, MethodArgumentNotValidException e) {
        FieldError error = e.getBindingResult().getFieldError();
        String atributo = error != null ? error.getField() : "body";
        String mensaje = error != null ? error.getDefaultMessage() : "Datos inválidos";
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorAtributo(atributo, mensaje));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorAtributo> manejarJsonInvalido(HttpServletRequest request, HttpMessageNotReadableException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorAtributo("body", "JSON inválido o tipo de dato incorrecto (revisá comas y formatos de fecha/número)."));
    }


    
}
