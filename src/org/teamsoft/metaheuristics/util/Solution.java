/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.metaheuristics.util;

import java.util.List;

/**
 *
 * @author G1lb3rt
 */
public class Solution {

    private List<ProjectRole> solutions;

    private float maxComp;
    private float minCost;
    private float minIncomp;
    private float minWorkLoad;
    private float maxBelbinRoles; //caracteristicas psicologicas
    private float maxInterests;

    private float cost;// si se usa factores ponderados

    public List<ProjectRole> getSolutions() {
        return solutions;
    }

    public void setSolutions(List<ProjectRole> solutions) {
        this.solutions = solutions;
    }

    public float getMaxComp() {
        return maxComp;
    }

    public void setMaxComp(float maxComp) {
        this.maxComp = maxComp;
    }

    public float getMinCost() {
        return minCost;
    }

    public void setMinCost(float minCost) {
        this.minCost = minCost;
    }

    public float getMinIncomp() {
        return minIncomp;
    }

    public void setMinIncomp(float minIncomp) {
        this.minIncomp = minIncomp;
    }

    public float getMinWorkLoad() {
        return minWorkLoad;
    }

    public void setMinWorkLoad(float minWorkLoad) {
        this.minWorkLoad = minWorkLoad;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public float getMaxBelbinRoles() {
        return maxBelbinRoles;
    }

    public void setMaxBelbinRoles(float maxBelbinRoles) {
        this.maxBelbinRoles = maxBelbinRoles;
    }

    public float getMaxInterests() {
        return maxInterests;
    }

    public void setMaxInterests(float maxInterests) {
        this.maxInterests = maxInterests;
    }

}
