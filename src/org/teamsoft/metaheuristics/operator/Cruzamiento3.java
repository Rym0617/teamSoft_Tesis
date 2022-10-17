/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.metaheuristics.operator;

import org.teamsoft.entity.Worker;
import org.teamsoft.metaheuristics.util.RoleWorker;

import java.util.ArrayList;

/**
 * @author Zucelys
 */
public class Cruzamiento3 extends TeamFormationOperator {

    public static ArrayList<Worker> OperadorCruzamiento3(ArrayList<RoleWorker> proyRoles1, ArrayList<RoleWorker> proyRoles2, int j, int k, int numVec) {

        ArrayList<Worker> PersonasTemporales = new ArrayList<>();
        Worker temPersona, temPersona2;
        if (numVec == 0) {
            temPersona = proyRoles1.get(j).getWorkers().get(k);
            temPersona2 = proyRoles2.get(j).getWorkers().get(k);
        } else {
            temPersona2 = proyRoles1.get(j).getWorkers().get(k);
            temPersona = proyRoles2.get(j).getWorkers().get(k);
        }
        PersonasTemporales.add(temPersona);
        PersonasTemporales.add(temPersona2);

        return PersonasTemporales;
    }

}
