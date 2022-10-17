/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.POJOS;

import org.teamsoft.entity.Project;

import java.util.List;

/**
 * @author G1lb3rt
 */
public class ProjectAssignmentTemplate {

    private Project project;
    List<RolAssignedTemplate> assignedRoles;

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<RolAssignedTemplate> getAssignedRoles() {
        return assignedRoles;
    }

    public void setAssignedRoles(List<RolAssignedTemplate> assignedRoles) {
        this.assignedRoles = assignedRoles;
    }
    
    
}
