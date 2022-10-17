package org.teamsoft.controller.importar.tableContent;


/**
 * clase que maneja los objetos que pertenecen a los
 * atributos del fichero que se seleccionaron como
 * competencias y son texto para asignarle un peso
 * a cada uno de sus valores dentro del fichero
 */
public class AssignedAtributeTextValueTable {

    private String valorDentroFichero;
    private float peso;

    public AssignedAtributeTextValueTable(String valorDentroFichero, float peso) {
        this.valorDentroFichero = valorDentroFichero;
        this.peso = peso;
    }

    public String getValorDentroFichero() {
        return valorDentroFichero;
    }

    public void setValorDentroFichero(String valorDentroFichero) {
        this.valorDentroFichero = valorDentroFichero;
    }

    public float getPeso() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }
}
