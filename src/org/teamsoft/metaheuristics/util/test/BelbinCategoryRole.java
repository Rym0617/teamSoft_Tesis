/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.metaheuristics.util.test;

import org.teamsoft.entity.Worker;

import java.util.List;

/**
 * @author Alejandro Durán
 */
public class BelbinCategoryRole {
    private List<Worker> categoryWorkers;

    public BelbinCategoryRole(List<Worker> categoryWorkers) {
        this.categoryWorkers = categoryWorkers;
    }

    public List<Worker> getCategoryWorkers() {
        return categoryWorkers;
    }

    public void setCategoryWorkers(List<Worker> categoryWorkers) {
        this.categoryWorkers = categoryWorkers;
    }
    
    
    
    
    
}
