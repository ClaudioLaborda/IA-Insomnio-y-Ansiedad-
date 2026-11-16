package tuti.daos.presentacion;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import tuti.daos.excepciones.*;

@RestControllerAdvice
public class ManejadorErrores {

    public record ErrorApi(int status, String error, String mensaje) {}

    private ResponseEntity<ErrorApi> responder(HttpStatus status, String mensaje) {
        return ResponseEntity.status(status)
                .body(new ErrorApi(status.value(), status.getReasonPhrase(), mensaje));
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorApi> noEncontrado(RecursoNoEncontradoException e) {
        return responder(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(ConflictoNegocioException.class)
    public ResponseEntity<ErrorApi> conflicto(ConflictoNegocioException e) {
        return responder(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler({ DatosInvalidosException.class,
                        HttpMessageNotReadableException.class,
                        MethodArgumentTypeMismatchException.class })
    public ResponseEntity<ErrorApi> datosInvalidos(Exception e) {
        return responder(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorApi> validacion(MethodArgumentNotValidException e) {
        String detalle = e.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return responder(HttpStatus.BAD_REQUEST, detalle);
    }
}