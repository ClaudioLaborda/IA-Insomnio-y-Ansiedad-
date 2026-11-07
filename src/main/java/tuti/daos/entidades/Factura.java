package tuti.daos.entidades;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class Factura{
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long ID;
	
	
	public void setId(Long ID) {
		
		this.ID = ID;
		
	}
	
}