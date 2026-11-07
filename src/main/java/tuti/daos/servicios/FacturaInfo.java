package tuti.daos.servicios;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FacturaInfo(Long id, BigDecimal importe, LocalDate fechaEmision, String estado) {
	
}
