package tuti.daos.presentacion;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.*;
import tuti.daos.enums.MediosPagos;


public class PagoRequest {
	
	
	@NotNull private Long facturaId;
	private LocalDate fechaPago;
	
	@NotNull private MediosPagos medioPago;
	
	@NotNull @Positive private BigDecimal importePagado;
	
	@NotNull @PositiveOrZero private BigDecimal Interes;
	
}