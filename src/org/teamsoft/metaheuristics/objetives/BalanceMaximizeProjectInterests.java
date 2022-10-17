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
public class BalanceMaximizeProjectInterests extends ObjetiveFunction {

    public static String className = "BalanceProjectInterests";

    @Override
    public Double Evaluation(State state) {
        ArrayList<Worker> projectWorkerList = new ArrayList<>();
        List<Double> reasons = new ArrayList<>();
        List<Object> projects = state.getCode();
        double projectInterest = 0;
        double normalization = 0;
        double projectPersons = 0;
        double projectReason = 0;
        double reasonSum = 0;
        double finalReasonSum = 0;
        double reasonAvg = 0;
        double max = 0;
        int teamsAmount = projects.size();

        for (Object item : projects) {
            projectPersons = 0;
            projectInterest = 0;
            ProjectRole projectRole = (ProjectRole) item;
            Project project = projectRole.getProject();
            List<RoleWorker> roleWorkers = projectRole.getRoleWorkers();

            for (RoleWorker roleWorker : roleWorkers) {
                List<Worker> workers = roleWorker.getWorkers();
                for (Worker worker : workers) {
                    if (addWorker(projectWorkerList, worker)) {
                        projectPersons += 1;
                        List<PersonalProjectInterests> personalProjectInterests = worker.getPersonalProjectInterestsList();
                        for (PersonalProjectInterests ppi : personalProjectInterests) {
                            //if (ppi.getProjectFk().getProjectName().equalsIgnoreCase(project.getProjectName())) {
                            if (ppi.getProjectFk().getId().equals(project.getId())) {
                                if (ppi.getPreference()) {
                                    projectInterest += 1;
                                }
                            }
                        }
                    }
                }
            }
            projectReason = projectInterest / projectPersons;
            reasons.add(projectReason);
            projectWorkerList.clear();
        }
        for (int i = 0; i < reasons.size(); i++) {
            reasonSum += reasons.get(i);
        }
        reasonAvg = reasonSum / teamsAmount;
        for (int j = 0; j < reasons.size(); j++) {
            finalReasonSum += Math.abs(reasons.get(j) - reasonAvg);
        }
        max = getMax(teamsAmount);

        normalization = (finalReasonSum - 0) / (max - 0);

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

    private double getMax(int size) { //2
        double average;
        double max = 0;
        int teamsAmountWithFullPreference;
        int teamsAmountWithVoidPreference;

        if (size % 2 == 0) {
            teamsAmountWithFullPreference = size / 2;  //1
            teamsAmountWithVoidPreference = size / 2; //1
        } else {
            teamsAmountWithFullPreference = (size + 1) / 2;
            teamsAmountWithVoidPreference = (size - 1) / 2;
        }
        average = teamsAmountWithFullPreference / size; //0.5

        for (int i = 0; i < teamsAmountWithFullPreference; i++) {
            max += Math.abs(1 - average);                       // 0.5
        }
        for (int j = 0; j < teamsAmountWithVoidPreference; j++) {
            max += Math.abs(0 - average);   //0.5
        }

        return max; //1
    }

}
