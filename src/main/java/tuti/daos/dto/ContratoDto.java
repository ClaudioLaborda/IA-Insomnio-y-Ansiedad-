
package tuti.daos.dto;


import java.math.BigDecimal;
import java.time.LocalDate;

public class ContratoDto {

    private Long id;
    private LocalDate fechaInicio;
    private int duracionMeses;
    private BigDecimal importeMensual;
    private int diaVencimientoMensual;
    private boolean activo;

    // Constructor vacío
    public ContratoDto() {
    }

    // Constructor con parámetros principales
    public ContratoDto(Long id, LocalDate fechaInicio, int duracionMeses, BigDecimal importeMensual, int diaVencimientoMensual, boolean activo) {
        this.id = id;
        this.fechaInicio = fechaInicio;
        this.duracionMeses = duracionMeses;
        this.importeMensual = importeMensual;
        this.diaVencimientoMensual = diaVencimientoMensual;
        this.activo = activo;
        
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public int getDuracionMeses() {
        return duracionMeses;
    }

    public void setDuracionMeses(int duracionMeses) {
        this.duracionMeses = duracionMeses;
    }

    public BigDecimal getImporteMensual() {
        return importeMensual;
    }

    public void setImporteMensual(BigDecimal importeMensual) {
        this.importeMensual = importeMensual;
    }

    public int getDiaVencimientoMensual() {
        return diaVencimientoMensual;
    }

    public void setDiaVencimientoMensual(int diaVencimientoMensual) {
        this.diaVencimientoMensual = diaVencimientoMensual;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

   
}