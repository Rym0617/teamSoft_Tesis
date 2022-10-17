/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.metaheuristics.objetives;

import org.teamsoft.POJOS.TeamFormationParameters;
import org.teamsoft.entity.CostDistance;
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
public class BalanceMinimizeCostDistance extends ObjetiveFunction {

    private TeamFormationParameters parameters;

    public TeamFormationParameters getParameters() {
        return parameters;
    }

    public void setParameters(TeamFormationParameters parameters) {
        this.parameters = parameters;
    }

    public BalanceMinimizeCostDistance(TeamFormationParameters parameters) {
        super();
        this.parameters = parameters;
    }

    public static String className = "BalanceCostoDistancia";


    @Override
    public Double Evaluation(State state) {
        List<Object> projects = state.getCode();
        List<Double> indexList = new ArrayList<>();
        double project_distanceIndex;
        double project_distanceMax;
        double costDistance;
        double roleImpact;
        double costDistanceIndex;
        double maxCostDistance = parameters.getMaxCostDistance().getCostDistance();
        double balance;
        List<Double> projectSumMax = new ArrayList<>();

        int amount_projects_max;
        if (projects.size() % 2 == 0) { //Si la cantidad de proyectos es par
            amount_projects_max = projects.size() / 2;
        } else {
            amount_projects_max = (projects.size() + 1) / 2;
        }

        for (Object item : projects) {//Para cada ProjectRole
            project_distanceIndex = 0;
            project_distanceMax = 0;
            ProjectRole projectRole = (ProjectRole) item;
            List<RoleWorker> roleWorkers = projectRole.getRoleWorkers();

            for (int i = 0; i < roleWorkers.size(); i++) {//Por cada RoleWorker del ProjectRole                  
                RoleWorker rwItem = roleWorkers.get(i);
                Role role = rwItem.getRole();
                roleImpact = role.getImpact();

                for (int j = 0; j < rwItem.getWorkers().size(); j++) { //Para cada trabajador de la lista de trabajadores del RoleWorker
                    Worker worker = rwItem.getWorkers().get(j);
                    List<CostDistance> worker_CountyCostDistance = worker.getCountyFk().getCostDistanceList1();
                    costDistance = 0;
                    Long project_CountyId = projectRole.getProject().getProvinceFk().getId();

                    int k = 0;
                    boolean found = false;
                    while (k < worker_CountyCostDistance.size() && !found) {

                        if (worker_CountyCostDistance.get(k).getCountyBFk().getId().equals(project_CountyId)
                                || worker_CountyCostDistance.get(k).getCountyAFk().getId().equals(project_CountyId)) {
                            costDistance = worker_CountyCostDistance.get(k).getCostDistance();
                            found = true;
                        }
                        k++;
                    }
                    costDistanceIndex = costDistance * roleImpact;
                    project_distanceIndex += costDistanceIndex;
                    project_distanceMax += maxCostDistance * roleImpact;
                }

                for (int j = 0; j < rwItem.getFixedWorkers().size(); j++) { //Para cada trabajador de la lista de trabajadores del RoleWorker
                    Worker worker = rwItem.getFixedWorkers().get(j);
                    List<CostDistance> worker_CountyCostDistance = worker.getCountyFk().getCostDistanceList1();
                    costDistance = 0;
                    Long project_CountyId = projectRole.getProject().getProvinceFk().getId();

                    int k = 0;
                    boolean found = false;
                    while (k < worker_CountyCostDistance.size() && !found) {

                        if (worker_CountyCostDistance.get(k).getCountyBFk().getId().equals(project_CountyId)
                                || worker_CountyCostDistance.get(k).getCountyAFk().getId().equals(project_CountyId)) {
                            costDistance = worker_CountyCostDistance.get(k).getCostDistance();
                            found = true;
                        }
                        k++;
                    }
                    costDistanceIndex = costDistance * roleImpact;
                    project_distanceIndex += costDistanceIndex;
                    project_distanceMax += maxCostDistance * roleImpact;
                }
            }
            indexList.add(project_distanceIndex / project_distanceMax);

            if (amount_projects_max > 0) {
                projectSumMax.add(1d); //La razón de máximo costo es 1
                amount_projects_max--;
            } else {
                projectSumMax.add(0d); //La razón de mínimo costo es 0
            }
        }
        balance = ObjetiveFunctionUtil.balance(indexList);

        //Calcular el máximo valor de balance
        maxCostDistance = ObjetiveFunctionUtil.balance(projectSumMax);

        balance = balance / maxCostDistance; // min = 0
        return balance;
    }
}
