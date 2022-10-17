/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.POJOS;

import org.teamsoft.entity.Project;
import org.teamsoft.entity.ProjectRoles;
import org.teamsoft.entity.ProjectStructure;
import org.teamsoft.entity.ProjectTechCompetence;

import java.util.List;

/**
 * @author G1lb3rt
 */
@Deprecated
public class ProjectTreeTemplate {

    private String displayName;

    private ProjectStructure structure;
    private Project project;
    private ProjectRoles roles;
    private ProjectTechCompetence projectCompetences;

    private String structureName = "-";
    private String projectName = "-";
    private String rolesName = "-";
    private String projectCompetencesName = "-";

    //crei que haria falta pero aun no le encuentro utilidad
    private List<ProjectStructure> structureList;
    private List<Project> projectList;
    private List<ProjectRoles> rolesList;
    private List<ProjectTechCompetence> projectCompetencesList;
    
    
    public String getStructureName() {
        return structureName;
    }

    
    public void setStructureName(String structureName) {
        this.structureName = structureName;
        this.displayName = "Structure/Estructura";
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        
        this.projectName = projectName;
        this.displayName = "Project/Proyecto";
    }

    public String getRolesName() {
        return rolesName;
    }

    public void setRolesName(String rolesName) {
        this.rolesName = rolesName;
        this.displayName = "Role/Rol";
    }

    public String getProjectCompetencesName() {
        return projectCompetencesName;
    }

    public void setProjectCompetencesName(String projectCompetencesName) {
        this.projectCompetencesName = projectCompetencesName;
        this.displayName = "Competence/Competencia";
    }

    public ProjectStructure getStructure() {
        return structure;
    }

    public void setStructure(ProjectStructure structure) {
        this.structure = structure;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public ProjectRoles getRoles() {
        return roles;
    }

    public void setRoles(ProjectRoles roles) {
        this.roles = roles;
    }

    public ProjectTechCompetence getProjectCompetences() {
        return projectCompetences;
    }

    public void setProjectCompetences(ProjectTechCompetence projectCompetences) {
        this.projectCompetences = projectCompetences;
    }

    public List<ProjectStructure> getStructureList() {
        return structureList;
    }

    public void setStructureList(List<ProjectStructure> structureList) {
        this.structureList = structureList;
    }

    public List<Project> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<Project> projectList) {
        this.projectList = projectList;
    }

    public List<ProjectRoles> getRolesList() {
        return rolesList;
    }

    public void setRolesList(List<ProjectRoles> rolesList) {
        this.rolesList = rolesList;
    }

    public List<ProjectTechCompetence> getProjectCompetencesList() {
        return projectCompetencesList;
    }

    public void setProjectCompetencesList(List<ProjectTechCompetence> projectCompetencesList) {
        this.projectCompetencesList = projectCompetencesList;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    
    

}
