/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.metaheuristics.objetives;

import org.teamsoft.POJOS.TeamFormationParameters;
import org.teamsoft.entity.ProjectRoles;
import org.teamsoft.entity.Role;
import org.teamsoft.entity.Worker;
import org.teamsoft.metaheuristics.util.ObjetiveFunctionUtil;
import org.teamsoft.metaheuristics.util.ProjectRole;
import org.teamsoft.metaheuristics.util.RoleWorker;
import problem.definition.ObjetiveFunction;
import problem.definition.State;

import java.util.ArrayList;
import java.util.List;

/**
 * @author G1lb3rt
 */
public class BalanceMinimizeWorkload extends ObjetiveFunction {

    public static String className = "BalanceCargaTrabajo";

    private TeamFormationParameters parameters;
    
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
        double totalLoad;
        float actualLoad;
        float newLoad;
        double maxWorkload;
        double balancedLoad;
        int total_projects = projects.size();
        double maxLoad = parameters.getMaxRoleLoad().getValue();
        List<Double> projectSumMax = new ArrayList<>();

        int amount_projects_max;
        if(total_projects%2 == 0){ //Si la cantidad de proyectos es par
            amount_projects_max = projects.size()/2;
        }
        else{
            amount_projects_max = (projects.size()+1)/2;
        }
        
        for (int k = 0; k < total_projects; k++) {
            actualLoad = 0;
            newLoad = 0;
            ProjectRole projectRole = (ProjectRole) projects.get(k);
            List<RoleWorker> roleWorkers = projectRole.getRoleWorkers();

            List<Worker> projectWorkers = ObjetiveFunctionUtil.ProjectWorkers(projectRole);
            for (Worker projectWorker : projectWorkers) {
                actualLoad += projectWorker.getWorkload();
            }

            for (int j = 0; j < roleWorkers.size(); j++) {
                RoleWorker rwItem = roleWorkers.get(j);
                Role role = rwItem.getRole();

                if (rwItem.getWorkers().size() > 0) { 
                    for (int l = 0; l < rwItem.getWorkers().size(); l++) {
                        newLoad += getRoleLoad(role, projectRole);
                    }
                }

                if (rwItem.getFixedWorkers().size() > 0) { 
                    for (int l = 0; l < rwItem.getFixedWorkers().size(); l++) {
                        newLoad += getRoleLoad(role, projectRole);
                    }
                }
            }
            totalLoad = actualLoad + newLoad;
            indexList.add(totalLoad/(maxLoad*projectWorkers.size()));
            
             if(amount_projects_max > 0){
                    projectSumMax.add(1d); //La razón de máximo interes es 1, indicando que todos tienen preferencia por los roles asignados
                    amount_projects_max--;
                }
                else{
                    projectSumMax.add(0d); //La razón de mínimo interés es 0, indicando que nadie tiene preferencia por los roles asignados
                }  
        }
        balancedLoad = ObjetiveFunctionUtil.balance(indexList);
        
        //Calcular el máximo valor de balance
        maxWorkload = ObjetiveFunctionUtil.balance(projectSumMax);
        balancedLoad = balancedLoad / maxWorkload;
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
}
