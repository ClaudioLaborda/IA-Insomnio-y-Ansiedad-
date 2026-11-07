package tuti.daos.servicios;

public interface FacturaService {
    FacturaInfo obtener(Long id);        // lanza RecursoNoEncontradoException si no existe
    void marcarComoPagada(Long id);
}

