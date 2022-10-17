/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.controller.util;

import java.util.ArrayList;
import java.util.List;
import org.teamsoft.entity.RoleEvaluation;
import org.teamsoft.entity.Worker;
import org.teamsoft.entity.WorkerConflict;

/**
 *
 * @author Derly
 */
public class MemberToEvaluate {
    
      
    private Worker member;
    private boolean evaluate;      
    private WorkerConflict compatibilityLevel;    
    private List<RoleEvaluation> roleEvaluationsMember;
    


    public Worker getMember() {
        return member;
    }

    public void setMember(Worker member) {
        this.member = member;
    }

    public boolean isEvaluate() {
        return evaluate;
    }
   
    public WorkerConflict getCompatibilityLevel() {
        return compatibilityLevel;
    }

    public void setCompatibilityLevel(WorkerConflict compatibilityLevel) {
        this.compatibilityLevel = compatibilityLevel;
    }

    public void setEvaluate(boolean evaluate) {
        this.evaluate = evaluate;
    }

    public List<RoleEvaluation> getRoleEvaluationsMember() {
        return roleEvaluationsMember;
    }

    public void setRoleEvaluationsMember(List<RoleEvaluation> roleEvaluationsMember) {
        this.roleEvaluationsMember = roleEvaluationsMember;
    }

    
    public MemberToEvaluate(Worker member) {
        this.member = member;
        this.evaluate = false;         
        this.compatibilityLevel = null;
        this.roleEvaluationsMember = new ArrayList<>();        
    }       
    
    
}
