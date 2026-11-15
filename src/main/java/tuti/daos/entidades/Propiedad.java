
package tuti.daos.entidades;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "propiedades")
public class Propiedad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String direccion;

    @Column(nullable = false)
    private String tipo; // Ej: Casa, Departamento, Local Comercial, etc.

    @Column(name = "precio_referencia", precision = 12, scale = 2)
    private BigDecimal precioReferencia;

    @Column(nullable = false)
    private boolean disponible = true;

  

    // Constructores
    public Propiedad() {
        super();
    }

    public Propiedad(Long id, String direccion, String tipo, BigDecimal precioReferencia, boolean disponible) {
        super();
        this.id = id;
        this.direccion = direccion;
        this.tipo = tipo;
        this.precioReferencia = precioReferencia;
        this.disponible = disponible;
      
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getPrecioReferencia() {
        return precioReferencia;
    }

    public void setPrecioReferencia(BigDecimal precioReferencia) {
        this.precioReferencia = precioReferencia;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

}