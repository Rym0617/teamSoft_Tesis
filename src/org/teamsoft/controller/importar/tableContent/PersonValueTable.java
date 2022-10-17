package org.teamsoft.controller.importar.tableContent;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author jpinas
 */
public class PersonValueTable implements Serializable {

    private String atributoFichero;
    private String atributoPersona;

    public PersonValueTable(String atributoFichero, String atributoPersona) {
        this.atributoFichero = atributoFichero;
        this.atributoPersona = atributoPersona;
    }

    public String getAtributoFichero() {
        return atributoFichero;
    }

    public void setAtributoFichero(String atributoFichero) {
        this.atributoFichero = atributoFichero;
    }

    public String getAtributoPersona() {
        return atributoPersona;
    }

    public void setAtributoPersona(String atributoPersona) {
        this.atributoPersona = atributoPersona;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonValueTable that = (PersonValueTable) o;
        return Objects.equals(atributoFichero, that.atributoFichero) &&
                Objects.equals(atributoPersona, that.atributoPersona);
    }

    @Override
    public int hashCode() {
        return Objects.hash(atributoFichero, atributoPersona);
    }
}