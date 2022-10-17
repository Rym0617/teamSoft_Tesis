/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.POJOS;

import org.teamsoft.entity.Competence;
import org.teamsoft.entity.CompetenceImportance;
import org.teamsoft.entity.Levels;

/**
 * Esta Clase sirve de plantilla para los datos que se le pasan a la tabla de
 * competencias tecnicaas y genericas en la vista Seleccionar Jefe de Proyecto
 *
 * @author G1lb3rt
 */
public class CompetencesTemplate {

    private Competence competence;
    private Levels minLevel;
    private CompetenceImportance competenceImportance;

    public Competence getCompetence() {
        return competence;
    }

    public void setCompetence(Competence competence) {
        this.competence = competence;
    }

    public Levels getMinLevel() {
        return minLevel;
    }

    public void setMinLevel(Levels minLevel) {
        this.minLevel = minLevel;
    }

    public CompetenceImportance getCompetenceImportance() {
        return competenceImportance;
    }

    public void setCompetenceImportance(CompetenceImportance competenceImportance) {
        this.competenceImportance = competenceImportance;
    }

}
