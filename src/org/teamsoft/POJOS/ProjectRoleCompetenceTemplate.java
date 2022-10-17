/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.POJOS;

import org.teamsoft.entity.Project;

import java.io.Serializable;
import java.util.List;

/**
 * Plantilla para variar las competencias asociadas a un proyecto en la pantalla
 * de asignacion del jefe de proyecto
 *
 * @author G1lb3rt
 */
public class ProjectRoleCompetenceTemplate implements Serializable{

    private Project project;
    private List<ProjectCompetenceTemplate> roleCompetences;

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<ProjectCompetenceTemplate> getRoleCompetences() {
        return roleCompetences;
    }

    public void setRoleCompetences(List<ProjectCompetenceTemplate> roleCompetences) {
        this.roleCompetences = roleCompetences;
    }

    /**
     * Busca un proyecto por su id en una lista de
     * ProjectRoleCompompetenceTemplate
     *
     * @param list
     * @param project
     * @return
     */
    public static ProjectRoleCompetenceTemplate findProjectById(List<ProjectRoleCompetenceTemplate> list, Project project) {
        ProjectRoleCompetenceTemplate searchResult = null;
        int i = 0;
        boolean found = false;
        while (i < list.size() && !found) {
            if (project.getId().equals(list.get(i).getProject().getId())) {
                searchResult = list.get(i);
            }

            i++;
        }
        return searchResult;
    }

}
