package tuti.daos.exception;

public class FacturaExcepcion extends RuntimeException{

	
	/*
	 * Atributo al que está asociado el error
	 */
	private  String atributo;
	
	private String mensaje;
	
	private int statusCode;

	public FacturaExcepcion() {
		super();
		
	}

	public FacturaExcepcion(String atributo,String mensaje,int statusCode) {
		super(mensaje);
		this.mensaje = mensaje;
		this.atributo = atributo;
		this.statusCode = statusCode;
	}

	
	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}


	public String getAtributo() {
		return atributo;
	}

	public void setAtributo(String atributo) {
		this.atributo = atributo;
	}
	
	@Override
	public String getMessage() {
		return mensaje;
	}
	
}
