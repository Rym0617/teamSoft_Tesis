/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.POJOS;

import org.teamsoft.entity.Project;

/**
 * Plantilla para seleccionar la cantidad de personas necesarias para desarrollar cada proyecto
 *
 * @author Alejandro Durán
 */
public class PersonPerProjectAmount {

    private Project proj;
    private int cant;

    public Project getProj() {
        return proj;
    }

    public void setProj(Project proj) {
        this.proj = proj;
    }

    public int getCant() {
        return cant;
    }

    public void setCant(int cant) {
        this.cant = cant;
    }

    public PersonPerProjectAmount(Project proj, int cant) {
        this.proj = proj;
        this.cant = cant;
    }
}
