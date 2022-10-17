/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.POJOS;

import org.teamsoft.entity.Worker;

/**
 *
 * @author Alejandro Durán
 */
public class SelectedWorkerIncompatibility {

    private Worker workerAFk;
    private Worker workerBFk;

    public SelectedWorkerIncompatibility(Worker workerAFk, Worker workerBFk) {
        this.workerAFk = workerAFk;
        this.workerBFk = workerBFk;
    }

    public Worker getWorkerAFk() {
        return workerAFk;
    }

    public void setWorkerAFk(Worker workerAFk) {
        this.workerAFk = workerAFk;
    }

    public Worker getWorkerBFk() {
        return workerBFk;
    }

    public void setWorkerBFk(Worker workerBFk) {
        this.workerBFk = workerBFk;
    }
}
