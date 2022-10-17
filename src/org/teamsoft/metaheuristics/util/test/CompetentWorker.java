/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.metaheuristics.util.test;

import org.teamsoft.entity.Worker;

/**
 *
 * @author Alejandro Durán
 */
public class CompetentWorker { //REVIEW

    private Worker worker;
    private long evaluation;

    public CompetentWorker() {
    }

    public CompetentWorker(Worker worker, long evaluation) {
        this.worker = worker;
        this.evaluation = evaluation;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public long getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(long evaluation) {
        this.evaluation = evaluation;
    }
}
