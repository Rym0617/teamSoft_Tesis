package org.teamsoft.metaheuristics.objetives;

import org.teamsoft.POJOS.TeamFormationParameters;
import org.teamsoft.entity.Worker;
import org.teamsoft.entity.WorkerConflict;
import org.teamsoft.metaheuristics.util.ObjetiveFunctionUtil;
import org.teamsoft.metaheuristics.util.ProjectRole;
import org.teamsoft.metaheuristics.util.RoleWorker;
import problem.definition.ObjetiveFunction;
import problem.definition.State;

import java.util.ArrayList;
import java.util.List;

public class MinimizeIncompatibilities extends ObjetiveFunction {

    private TeamFormationParameters parameters;

    public TeamFormationParameters getParameters() {
        return parameters;
    }

    public void setParameters(TeamFormationParameters parameters) {
        this.parameters = parameters;
    }
    
    public static String className = "Incompatibilidades";
    
    public MinimizeIncompatibilities(TeamFormationParameters parameters) {
        super();
        this.parameters = parameters;
    }
    
    public MinimizeIncompatibilities() {
        super();
    }
    
    @Override
    public Double Evaluation(State state) {
        List<Object> projects = state.getCode();
        double total_incomp = 0;
        double maxIncompatibilities = parameters.getMaxConflictIndex().getWeight();
        double maxIncomp = 0;
        double projectIncomp;
        double max_project_incomp;
        ArrayList pRole = new ArrayList();  
        for (Object item : projects) { // Para cada ProjectRole
            double project_incomp = 0;
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
                    maxIncomp += maxIncompatibilities;
                }
            }
            total_incomp += project_incomp;            
            projectRole.getProjectEvaluation()[4] = project_incomp/max_project_incomp;
        }
       total_incomp = total_incomp / maxIncomp; // Min = 0
        return total_incomp;
    }

    public double maxIncomp(List<Object> projects) {
        double maxValue = 0;
        double maxIncomp = parameters.getMaxConflictIndex().getWeight();
        //Conformando lista de trabajadores
        List<Worker> projectWorkers;
        for (Object project : projects) {
            projectWorkers = new ArrayList();
            ProjectRole projectRole = (ProjectRole) project;
            for (int r = 0; r < projectRole.getRoleWorkers().size(); r++) {
                RoleWorker roleWorker = projectRole.getRoleWorkers().get(r);
                for (int k = 0; k < roleWorker.getWorkers().size(); k++) {
                    Worker worker = roleWorker.getWorkers().get(k);
                    if(!projectWorkers.contains(worker))
                        projectWorkers.add(worker);
                }
            }
            for (int h = 0; h < projectWorkers.size(); h++) {
                for (int l = h+1; l < projectWorkers.size(); l++) {
                    maxValue += maxIncomp;
                }
        }
        }
        return maxValue;
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

}
