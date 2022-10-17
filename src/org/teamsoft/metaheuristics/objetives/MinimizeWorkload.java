package org.teamsoft.metaheuristics.objetives;

import metaheurictics.strategy.Strategy;
import org.teamsoft.POJOS.TeamFormationParameters;
import org.teamsoft.entity.ProjectRoles;
import org.teamsoft.entity.Role;
import org.teamsoft.entity.Worker;
import org.teamsoft.metaheuristics.util.ProjectRole;
import org.teamsoft.metaheuristics.util.RoleWorker;
import org.teamsoft.metaheuristics.util.TeamFormationCodification;
import problem.definition.ObjetiveFunction;
import problem.definition.State;

import java.util.ArrayList;
import java.util.List;

public class MinimizeWorkload extends ObjetiveFunction {

    private TeamFormationParameters parameters;

    public static String className = "CargaTrabajo";

    public TeamFormationParameters getParameters() {
        return parameters;
    }

    public void setParameters(TeamFormationParameters parameters) {
        this.parameters = parameters;
    }

    @Override
    public Double Evaluation(State state) {
        List<Object> projects = state.getCode();
        List<Double> indexList = new ArrayList<>();
        float newLoad = 0;
        double balancedLoad = 0;
        int total_projects = state.getCode().size();
        double workerLoad;
        List<Worker> projectWorkers = AllProjectWorkers(projects);
        double maxWorkload = maxWorkload();

        for (int i = 0; i < projectWorkers.size(); i++) {
            Worker worker = projectWorkers.get(i);
            workerLoad = worker.getWorkload();

            for (int k = 0; k < total_projects; k++) {
                ProjectRole projectRole = (ProjectRole) projects.get(k);
                List<RoleWorker> roleWorkers = projectRole.getRoleWorkers();

                for (int j = 0; j < roleWorkers.size(); j++) {
                    RoleWorker rwItem = roleWorkers.get(j);
                    Role role = rwItem.getRole();

                    if (rwItem.getWorkers().contains(worker)) { 
                        for (int l = 0; l < rwItem.getWorkers().size(); l++) {
                            workerLoad += getRoleLoad(role, projectRole);
                            newLoad += getRoleLoad(role, projectRole);
                        }
                    }

                    if (rwItem.getFixedWorkers().contains(worker)) { 
                        for (int l = 0; l < rwItem.getFixedWorkers().size(); l++) {
                            workerLoad += getRoleLoad(role, projectRole);
                            newLoad += getRoleLoad(role, projectRole);
                        }
                    }
                }
                projectRole.getProjectEvaluation()[2] = workerLoad;
            }
            indexList.add(workerLoad);
        }
        double averageWorkload = averageWorkload(newLoad);

        for (int j = 0; j < indexList.size(); j++) {
            double diference = indexList.get(j) - averageWorkload;
            double coeficient = Math.pow(diference, 2);
            balancedLoad += coeficient;
        }

        balancedLoad = (balancedLoad) / (maxWorkload);
        return balancedLoad;
    }

    public float getRoleLoad(Role role, ProjectRole pr) {
        int cycleSize = pr.getProject().getCycleList().size();
        List<ProjectRoles> pRolesList = pr.getProject().getCycleList().get(cycleSize - 1).getStructureFk().getProjectRolesList();
        float roleLoad = -1;

        boolean found = false;
        int i = 0;
        while (!found && i < pRolesList.size()) {
            if (pRolesList.get(i).getRoleFk().getId().equals(role.getId())) {
                roleLoad = pRolesList.get(i).getRoleLoadFk().getValue();
                found = true;
            } else {
                i++;
            }
        }
        return roleLoad;
    }

    public double averageWorkload(float newLoad) {
        List<Worker> workers = new ArrayList<>();
        workers.addAll(((TeamFormationCodification) Strategy.getStrategy().getProblem().getCodification()).getSearchArea());
        float actualLoad = 0;
        float totalLoad;
        float averageWorkload;

        for (int l = 0; l < workers.size(); l++) {
            Worker worker = workers.get(l);
            actualLoad += worker.getWorkload();
        }
        totalLoad = actualLoad + newLoad;
        averageWorkload = totalLoad / workers.size();
        return Double.valueOf(averageWorkload);
    }

    public double maxWorkload() {               
        List<Worker> workers = new ArrayList<>();
        workers.addAll(((TeamFormationCodification) Strategy.getStrategy().getProblem().getCodification()).getSearchArea());
        float totalLoad = parameters.getMaxRoleLoad().getValue();
        double averageLoad;
        double actualLoad = 0;
        double maxWorkload = 0;
        List<Float> workerSumMax = new ArrayList<>();
        
        int amount_workers_max;
        if(workers.size()%2 == 0){ //Si la cantidad de proyectos es par
            amount_workers_max = workers.size()/2;
        }
        else{
            amount_workers_max = (workers.size()+1)/2;
        }
        for (int l = 0; l < workers.size(); l++) {
            Worker worker = workers.get(l);
            if(amount_workers_max > 0){
                    workerSumMax.add(totalLoad); 
                    actualLoad += totalLoad;
                    amount_workers_max--;
                }
                else{
                    workerSumMax.add(0f); 
                }
        }
        averageLoad = actualLoad/workers.size();
        for (int i = 0; i < workerSumMax.size(); i++) {
            maxWorkload += Math.pow(workerSumMax.get(i) - averageLoad, 2);
        }
        return maxWorkload;
    }

    public List<Worker> AllProjectWorkers(List<Object> projects) {
        List<Worker> projectWorkers = new ArrayList<>(0);
        for (Object project : projects) {
            ProjectRole projectRole = (ProjectRole) project;
            List<RoleWorker> roleWorkers = projectRole.getRoleWorkers();

            int i = 0;
            while (i < roleWorkers.size()) {
                RoleWorker roleWorker = roleWorkers.get(i);
                List<Worker> workers = new ArrayList<>(0);
                workers.addAll(roleWorker.getWorkers());
                workers.addAll(roleWorker.getFixedWorkers());

                int j = 0;
                while (j < workers.size()) {
                    Worker worker = workers.get(j);

                    int k = 0;
                    boolean found = false;
                    while (k < projectWorkers.size() && !found) {
                        if (projectWorkers.get(k).getId().equals(worker.getId())) {
                            found = true;
                        }
                        k++;
                    }
                    if (found == false) {
                        projectWorkers.add(worker);
                    }
                    j++;
                }
                i++;
            }
        }
        return projectWorkers;
    }

    public int getTotalRoles(List<Object> projects) {
        int totalRoles = 0;
        for (Object project : projects) {
            ProjectRole projectRole = (ProjectRole) project;
            List<RoleWorker> roleWorkers = projectRole.getRoleWorkers();
            for (RoleWorker roleWorker : roleWorkers) {
                if (!roleWorker.getWorkers().isEmpty()) {
                    totalRoles += roleWorker.getWorkers().size();
                }
                if (!roleWorker.getFixedWorkers().isEmpty()) {
                    totalRoles += roleWorker.getFixedWorkers().size();
                }
            }
        }
        return totalRoles;
    }
}
