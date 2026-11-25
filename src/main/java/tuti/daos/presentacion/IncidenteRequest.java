package tuti.daos.presentacion;


 public class IncidenteRequest {

        private Long contratoId;
        private String titulo;
        private String descripcion;
        private String categoria;
        private String prioridad;
        private String estado;

        public IncidenteRequest() {}

        public Long getContratoId() { return contratoId; }
        public void setContratoId(Long contratoId) { this.contratoId = contratoId; }

        public String getTitulo() { return titulo; }
        public void setTitulo(String titulo) { this.titulo = titulo; }

        public String getDescripcion() { return descripcion; }
        public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

        public String getCategoria() { return categoria; }
        public void setCategoria(String categoria) { this.categoria = categoria; }

        public String getPrioridad() { return prioridad; }
        public void setPrioridad(String prioridad) { this.prioridad = prioridad; }

        public String getEstado() { return estado; }
        public void setEstado(String estado) { this.estado = estado; }
    }