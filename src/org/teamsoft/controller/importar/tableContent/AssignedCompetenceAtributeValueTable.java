package org.teamsoft.controller.importar.tableContent;

import java.util.Objects;

/**
 * clase que se encarga de almacenar los valores que contiene la tabla
 * del step de Seleccionar Competencia cuando existen competencias
 * que se le asignaron mas de un atributo y el objetivo es conocer
 * qué peso tienen
 *
 * @author jpinas
 */
public class AssignedCompetenceAtributeValueTable {

    private String atributo;
    private float peso;

    public AssignedCompetenceAtributeValueTable(String atributo, float peso) {
        this.atributo = atributo;
        this.peso = peso;
    }

    public String getAtributo() {
        return atributo;
    }

    public void setAtributo(String atributo) {
        this.atributo = atributo;
    }

    public float getPeso() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssignedCompetenceAtributeValueTable that = (AssignedCompetenceAtributeValueTable) o;
        return Objects.equals(atributo, that.atributo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(atributo);
    }
}
