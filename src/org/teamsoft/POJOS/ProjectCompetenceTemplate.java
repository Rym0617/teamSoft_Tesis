/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.POJOS;

import org.teamsoft.entity.Role;

import java.util.List;

/**
 * @author G1lb3rt
 */
public class ProjectCompetenceTemplate {

    private Role role;
    private List<CompetencesTemplate> techCompetences;
    private List<CompetencesTemplate> genCompetences;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<CompetencesTemplate> getTechCompetences() {
        return techCompetences;
    }

    public void setTechCompetences(List<CompetencesTemplate> techCompetences) {
        this.techCompetences = techCompetences;
    }

    public List<CompetencesTemplate> getGenCompetences() {
        return genCompetences;
    }

    public void setGenCompetences(List<CompetencesTemplate> genCompetences) {
        this.genCompetences = genCompetences;
    }
    
    
    
    
}
