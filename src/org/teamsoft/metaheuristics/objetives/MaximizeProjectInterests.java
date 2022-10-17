/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.metaheuristics.objetives;

import org.teamsoft.entity.PersonalProjectInterests;
import org.teamsoft.entity.Project;
import org.teamsoft.entity.Worker;
import org.teamsoft.metaheuristics.util.ProjectRole;
import org.teamsoft.metaheuristics.util.RoleWorker;
import problem.definition.ObjetiveFunction;
import problem.definition.State;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lesmes
 */
public class MaximizeProjectInterests extends ObjetiveFunction {

    public static String className = "ProjectIntereses";

    @Override
    public Double Evaluation(State state) {
        double total_interest = 0;
        double projectInterest = 0;
        double normalization = 0;
        double maxPerson = 0;
        double projectPersons = 0;

        ArrayList<Worker> projectWorkerList = new ArrayList<>();
        List<Object> projects = state.getCode();

        for (Object item : projects) {
            ProjectRole projectRole = (ProjectRole) item;
            Project project = projectRole.getProject();

            List<RoleWorker> roleWorkers = projectRole.getRoleWorkers();
            projectInterest = 0;

            for (RoleWorker roleWorker : roleWorkers) {
                List<Worker> workers = roleWorker.getWorkers();
                for (Worker worker : workers) {
                    if (addWorker(projectWorkerList, worker)) {
                        List<PersonalProjectInterests> personalProjectInterests = worker.getPersonalProjectInterestsList();
                        for (PersonalProjectInterests ppi : personalProjectInterests) {
                            if (ppi.getProjectFk().getProjectName().equalsIgnoreCase(project.getProjectName())) {
                                if (ppi.getPreference()) {
                                    projectInterest = projectInterest + 1;
                                }
                            }
                        }
                    }
                }
            }
            projectPersons = projectWorkerList.size();
            projectWorkerList.clear();
            total_interest = total_interest + projectInterest;
            maxPerson = maxPerson + projectPersons;
        }

        System.out.println("objective.function.MaximizeProjectInterests.Evaluation()");
        System.out.println("Total Propuesta = " + total_interest + "\n"); //Eliminar
        System.out.println("Maximo Posible = " + maxPerson + "\n"); //Eliminar
        double aux = maxPerson - total_interest;
        System.out.println("Aux = " + aux + "\n"); //Eliminar
        double pepe = aux / maxPerson;
        System.out.println("pepe = " + pepe + "\n"); //Eliminar
        normalization = pepe;
        System.out.println("Indice Propuesta = " + normalization + "\n"); //Eliminar
        return normalization;
    }

    private boolean addWorker(List<Worker> workersList, Worker worker) {
        boolean added = false;
        boolean found = false;
        for (Worker w : workersList) {
            if (w.getId().equals(worker.getId())) {
                found = true;
            }
        }
        if (!found) {
            workersList.add(worker);
            added = true;
        }
        return added;
    }

}
