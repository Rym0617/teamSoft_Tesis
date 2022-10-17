/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.POJOS;

import org.teamsoft.entity.Worker;

/**
 *
 * @author G1lb3rt
 */
public class PersonAssignedTemplate {
    
    private Worker person;
    private float evaluation;

    public Worker getPerson() {
        return person;
    }

    public void setPerson(Worker person) {
        this.person = person;
    }

    public float getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(float evaluation) {
        this.evaluation = evaluation;
    }
    
}
