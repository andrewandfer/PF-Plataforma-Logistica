package PFPlataformaLogistica.dto;

import PFPlataformaLogistica.model.Direccion;

import java.io.Serializable;

public class DireccionDTO implements Serializable {
        private String idDireccion;
        private String alias;
        private String calle;
        private String ciudad;

        public String getIdDireccion() {
            return idDireccion;
        }

        public String getAlias() {
            return alias;
        }

        public String getCalle() {
            return calle;
        }

        public String getCiudad() {
            return ciudad;
        }

        public void setIdDireccion(String idDireccion) {
            this.idDireccion = idDireccion;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }

        public void setCalle(String calle) {
            this.calle = calle;
        }

        public void setCiudad(String ciudad) {
            this.ciudad = ciudad;
        }

}
