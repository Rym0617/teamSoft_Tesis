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
public class RolAssignedTemplate {

    private Role role;
    private List<PersonAssignedTemplate> peopleAssigned;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<PersonAssignedTemplate> getPeopleAssigned() {
        return peopleAssigned;
    }

    public void setPeopleAssigned(List<PersonAssignedTemplate> peopleAssigned) {
        this.peopleAssigned = peopleAssigned;
    }
    
    
    
}
