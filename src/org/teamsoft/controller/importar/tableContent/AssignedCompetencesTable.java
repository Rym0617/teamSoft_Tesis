package org.teamsoft.controller.importar.tableContent;

import java.util.LinkedList;

/**
 * contiene el contenido de la tabla step3 de los asignados.
 * Me da el valor de la competenceType seleccionada junto con el atributo
 * del fichero que se relaciona
 *
 * @author jpinas
 */
public class AssignedCompetencesTable {

    private LinkedList<String> competences;
    private String fileAtribute;

    /**
     * @param competences  listado de competencias asociadas al atributo
     * @param fileAtribute atributo del fichero
     */
    public AssignedCompetencesTable(LinkedList<String> competences, String fileAtribute) {
        this.competences = competences;
        this.fileAtribute = fileAtribute;
    }

    public LinkedList<String> getCompetences() {
        return competences;
    }

    public void setCompetences(LinkedList<String> competence) {
        this.competences = competence;
    }

    public String getFileAtribute() {
        return fileAtribute;
    }

    public void setFileAtribute(String fileAtribute) {
        this.fileAtribute = fileAtribute;
    }

    /**
     * devuelve la lista de competencias separadas por coma para mostrarlo
     * en la tabla
     */
    public Object getCompetencesAsString() {
        StringBuilder stb = new StringBuilder();

        int cont = 0;
        for (String compName : competences) {
            if (cont == 2) { // si hay dos se le ponen los 3 pticos para no cargar la tabla
                stb.append("...");
                break;
            } else {
                stb.append(compName).append(", ");
                cont++;
            }
        }
        String result = stb.toString();
        if (result.endsWith(", "))
            result = result.substring(0, result.length() - 2);
        return result;
    }
}
