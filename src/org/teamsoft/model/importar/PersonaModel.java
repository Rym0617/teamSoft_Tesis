package org.teamsoft.model.importar;

import java.util.Objects;

public class PersonaModel {
    String nombre;
    String exp;

    public PersonaModel(String nombre, String exp) {
        this.nombre = nombre;
        this.exp = exp;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    @Override
    public String toString() {
        return nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonaModel that = (PersonaModel) o;
        return Objects.equals(nombre, that.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, exp);
    }
}
