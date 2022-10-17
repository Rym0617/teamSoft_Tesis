package org.teamsoft.model.importar;

import com.opencsv.bean.CsvBindByName;

public class CompetenceModel {
    @CsvBindByName(column = "nombre", required = true)
    public String nombre;
    @CsvBindByName(column = "descripcion", required = true)
    public String descripcion;
    @CsvBindByName(column = "isTecnica", required = true)
    public String isTechnical;

    public String getNombre() {
        return nombre;
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

    public boolean isTechnical() {
        return Boolean.parseBoolean(isTechnical);
    }

    public void setIsTechnical(String isTechnical) {
        this.isTechnical = isTechnical;
    }
}
