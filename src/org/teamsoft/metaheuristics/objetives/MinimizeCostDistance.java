package org.teamsoft.metaheuristics.objetives;

import org.teamsoft.POJOS.TeamFormationParameters;
import org.teamsoft.entity.CostDistance;
import org.teamsoft.entity.Role;
import org.teamsoft.entity.Worker;
import org.teamsoft.metaheuristics.util.ProjectRole;
import org.teamsoft.metaheuristics.util.RoleWorker;
import problem.definition.ObjetiveFunction;
import problem.definition.State;

import java.util.List;

public class MinimizeCostDistance extends ObjetiveFunction {

    private TeamFormationParameters parameters;

    public TeamFormationParameters getParameters() {
        return parameters;
    }

    public void setParameters(TeamFormationParameters parameters) {
        this.parameters = parameters;
    }
    
    public static String className = "CostoDistancia";
    
     public MinimizeCostDistance(TeamFormationParameters parameters) {
        super();
        this.parameters = parameters;
    }
    
     public MinimizeCostDistance() {
        super();
    }
    
    @Override
    public Double Evaluation(State state) {
        List<Object> projects = state.getCode();
        double total_distanceCost = 0f;
        float project_distanceIndex;
        double costDistance;
        double roleImpact;
        double costDistanceIndex;
        float maxCostDistance = maxCostDistance(projects);
        float bigger = parameters.getMaxCostDistance().getCostDistance();
        float maxProjectCostDistance;
        
        for (Object item : projects) { // Para cada ProjectRole
            project_distanceIndex = 0;
            ProjectRole projectRole = (ProjectRole) item;
            List<RoleWorker> roleWorkers = projectRole.getRoleWorkers();
            maxProjectCostDistance = 0;

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
                    maxProjectCostDistance += bigger * roleImpact;
                    project_distanceIndex += costDistanceIndex;
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
                    maxProjectCostDistance += bigger * roleImpact;
                    project_distanceIndex += costDistanceIndex;
                }
            }
            total_distanceCost += project_distanceIndex;
            projectRole.getProjectEvaluation()[3] = Double.valueOf(project_distanceIndex/maxProjectCostDistance);
        }
        total_distanceCost = total_distanceCost / maxCostDistance; // min = 0
        return total_distanceCost;
    }

    public float maxCostDistance(List<Object> projects) {
        float maxCostDistance = 0;
        float roleImpact;
        float bigger = parameters.getMaxCostDistance().getCostDistance();
        
        for (Object item : projects) { // Para cada ProjectRole
            ProjectRole projectRole = (ProjectRole) item;
                       
            List<RoleWorker> roleWorkers = projectRole.getRoleWorkers();

            for (int j = 0; j < roleWorkers.size(); j++) {//Por cada RoleWorker del ProjectRole     
                RoleWorker rwItem = roleWorkers.get(j);
                Role role = rwItem.getRole();
                roleImpact = role.getImpact();
                for (Worker worker : rwItem.getWorkers()) {
                    //Para cada trabajador de la lista de trabajadores del RoleWorker
                    maxCostDistance += bigger * roleImpact;
                }
                for (Worker fixedWorker : rwItem.getFixedWorkers()) {
                    //Para cada trabajador de la lista de trabajadores del RoleWorker
                    maxCostDistance += bigger * roleImpact;
                }
            }
        }
        return maxCostDistance;
    }
}
