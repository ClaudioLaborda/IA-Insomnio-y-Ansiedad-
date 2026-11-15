package tuti.daos.servicios;



import java.util.List;

import tuti.daos.dto.FacturaRequestDto;
import tuti.daos.dto.FacturaResponseDto;
import tuti.daos.entidades.EstadoFactura;

public interface FacturaService {

    FacturaResponseDto emitirFactura(FacturaRequestDto request);

    FacturaResponseDto obtenerPorId(Long id);

    List<FacturaResponseDto> listarFiltradas(Long contratoId, String periodo, EstadoFactura estado);

    FacturaResponseDto anularFactura(Long id);
    
}