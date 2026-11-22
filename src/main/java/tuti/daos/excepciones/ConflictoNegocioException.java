package tuti.daos.excepciones;



public class ConflictoNegocioException extends RuntimeException {
    public ConflictoNegocioException(String msg) { super(msg); }
}