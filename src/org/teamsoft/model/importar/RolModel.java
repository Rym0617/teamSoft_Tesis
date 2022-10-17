package org.teamsoft.model.importar;

import com.opencsv.bean.CsvBindByName;

public class RolModel {
    @CsvBindByName(column = "nombre", required = true)
    String nombre;
    @CsvBindByName(column = "descripcion", required = true)
    String descripcion;
    @CsvBindByName(column = "impacto", required = true)
    String impacto;
    @CsvBindByName(column = "incompatibilidades")
    String incompatibleRoles;

    public String getNombre() {
        return nombre.trim();
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public float getImpacto() {
        return Float.parseFloat(impacto);
    }

    public void setImpacto(String impacto) {
        this.impacto = impacto;
    }

    public String[] getIncompatibleRoles() {
        return incompatibleRoles.split(",");
    }

    public void setIncompatibleRoles(String incompatibleRoles) {
        this.incompatibleRoles = incompatibleRoles;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
