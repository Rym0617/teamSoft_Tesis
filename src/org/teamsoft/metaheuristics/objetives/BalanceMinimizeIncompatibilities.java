package org.teamsoft.metaheuristics.objetives;

import org.teamsoft.POJOS.TeamFormationParameters;
import org.teamsoft.entity.Worker;
import org.teamsoft.entity.WorkerConflict;
import org.teamsoft.metaheuristics.util.ObjetiveFunctionUtil;
import org.teamsoft.metaheuristics.util.ProjectRole;
import problem.definition.ObjetiveFunction;
import problem.definition.State;

import java.util.ArrayList;
import java.util.List;

public class BalanceMinimizeIncompatibilities extends ObjetiveFunction {

    private TeamFormationParameters parameters;

    public TeamFormationParameters getParameters() {
        return parameters;
    }

    public void setParameters(TeamFormationParameters parameters) {
        this.parameters = parameters;
    }
    
     public BalanceMinimizeIncompatibilities(TeamFormationParameters parameters) {
        super();
        this.parameters = parameters;
    }
    
    public static String className = "BalanceIncompatibilidades";
    
    @Override
    public Double Evaluation(State state) {
        List<Object> projects = state.getCode();        
        List<Double> indexList = new ArrayList<>();
        double maxBalanceIncomp;
        double maxIncompatibilities = parameters.getMaxConflictIndex().getWeight();
        
        double projectIncomp = 0;
        double max_project_incomp;
        List<Double> projectSumMax = new ArrayList<>();

        int amount_projects_max;
        if(projects.size()%2 == 0){ //Si la cantidad de proyectos es par
            amount_projects_max = projects.size()/2;
        }
        else{
            amount_projects_max = (projects.size()+1)/2;
        }
        
        double project_incomp;
        for (Object item : projects) { // Para cada ProjectRole
            project_incomp = 0;
            max_project_incomp = 0;
            ProjectRole projectRole = (ProjectRole) item;
            List<Worker> projectWorkers = ObjetiveFunctionUtil.ProjectWorkers(projectRole);

            for (int j = 0; j < projectWorkers.size(); j++) {
                Worker worker = projectWorkers.get(j);
                for (int i = j + 1; i < projectWorkers.size(); i++) {
                    Worker projectWorker = projectWorkers.get(i);
                    Double projWorkerIndex = getIncompIndex(projectWorker, worker);
                    Double workerIndex = getIncompIndex(worker, projectWorker);
                    if (projWorkerIndex > workerIndex) {
                        project_incomp += projWorkerIndex;
                    } else {
                        project_incomp += workerIndex;
                    }
                    max_project_incomp += maxIncompatibilities;
                }
            }
            indexList.add(project_incomp/max_project_incomp);
            ArrayList pRole = new ArrayList();
            pRole.add(projectRole);
            projectIncomp = maxBalanceIncomp(pRole);
            
             if(amount_projects_max > 0){
                    projectSumMax.add(1d); //Indica que todos lo miembros del equipo son incompatibles
                    amount_projects_max--;
                }
                else{
                    projectSumMax.add(0d); //La razón de mínima incomp es 0, indicando que son compatibles todos los miembros del equipo
                } 
            
        }
        double balance = ObjetiveFunctionUtil.balance(indexList);
        
        //Calcular el máximo valor de balance
        maxBalanceIncomp = ObjetiveFunctionUtil.balance(projectSumMax);
        
        balance = balance / maxBalanceIncomp; // Min = 0
        return balance;
    }

    public Double getIncompIndex(Worker worker, Worker worker2) {
        double incompIndex = 0.5;
        List<WorkerConflict> workerConflicts = worker.getWorkerConflictList();
        int l = 0;
        boolean foundConflict = false;
        while (l < workerConflicts.size() && !foundConflict) {
            WorkerConflict workerConflict = workerConflicts.get(l);
            if (worker2.getId().equals(workerConflict.getWorkerConflictFk().getId())
                    || worker2.getId().equals(workerConflict.getWorkerFk().getId())) {
                incompIndex = workerConflict.getIndexFk().getWeight();
                foundConflict = true;
            }
            l++;
        }
        return incompIndex;
    }

    public double maxBalanceIncomp(List<Object> projects) {
        double maxBalanceIncomp = 0;
        double maxIncomp = parameters.getMaxConflictIndex().getWeight();
        int projectAmount = Math.floorDiv(projects.size(), 2);

        for (int k = 0; k < projectAmount; k++) {
            ProjectRole projectRole = (ProjectRole) projects.get(k);
            for (int j = 0; j < projectRole.getRoleWorkers().size(); j++) {
                for (int i = j + 1; i < projectRole.getRoleWorkers().size(); i++) {
                    maxBalanceIncomp += maxIncomp;
                }
            }
        }
        double averageIncomp = maxBalanceIncomp / projects.size();
        maxBalanceIncomp = maxBalanceIncomp - (averageIncomp * Math.floorDiv(projects.size(), 2));

        return maxBalanceIncomp;
    }
    
   
}
