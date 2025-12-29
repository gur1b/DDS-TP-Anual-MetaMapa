package servicioEstadisticas.DTO;

import com.fasterxml.jackson.annotation.JsonAlias;

public class HechoDTO {
        private String hash;
        private String provincia;
        private String categoria;


        @JsonAlias({"dia", "fecha_suceso", "fechaSuceso"})
        private String fecha_suceso;

        public HechoDTO() {}

        public HechoDTO(String hash, String provincia, String categoria, String fecha_suceso) {
                this.hash = hash;
                this.provincia = provincia;
                this.categoria = categoria;
                this.fecha_suceso = fecha_suceso;
        }

        public String getHash() { return hash; }
        public void setHash(String id_hecho) { this.hash = id_hecho; }

        public String getProvincia() { return provincia; }
        public void setProvincia(String provincia) { this.provincia = provincia; }

        public String getCategoria() { return categoria; }
        public void setCategoria(String categoria) { this.categoria = categoria; }

        public String getFecha_suceso() { return fecha_suceso; }
        public void setFecha_suceso(String fechaSuceso) { this.fecha_suceso = fechaSuceso; }

        public String toString(){
                return "HechoDTO{" +
                        "hash='" + hash + '\'' +
                        ", provincia='" + provincia + '\'' +
                        ", categoria='" + categoria + '\'' +
                        ", fecha_suceso='" + fecha_suceso + '\'' +
                        '}';
        }
}
