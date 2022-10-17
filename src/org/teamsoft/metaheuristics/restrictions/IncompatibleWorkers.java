package org.teamsoft.metaheuristics.restrictions;

import metaheurictics.strategy.Strategy;
import org.teamsoft.entity.Worker;
import org.teamsoft.entity.WorkerConflict;
import org.teamsoft.metaheuristics.util.ObjetiveFunctionUtil;
import org.teamsoft.metaheuristics.util.ProjectRole;
import org.teamsoft.metaheuristics.util.RoleWorker;
import org.teamsoft.metaheuristics.util.TeamFormationCodification;
import problem.definition.State;

import java.util.List;
import java.util.Random;

/**
 * No permitir que en un mismo equipo se encuentren personas incompatibles
 * (incompatibilidad = 0)
 */
public class IncompatibleWorkers extends Constrain {

    @Override
    public Boolean ValidateState(State state) {

        List<Object> projects = state.getCode();

        int i = 0;
        boolean meet = true;

        while (i < projects.size() && meet) {
            ProjectRole projectRole = (ProjectRole) projects.get(i);
            List<Worker> projectWorkers = ObjetiveFunctionUtil.ProjectWorkers(projectRole);
            meet = validateProyect(state, i);
            i++;
        }
        return meet;
    }

    public boolean validateProyect(State state, int posProy) {

        List<Object> projects = state.getCode();
        ProjectRole projectRole = (ProjectRole) projects.get(posProy);
        List<RoleWorker> roleWorkers = projectRole.getRoleWorkers();
        List<Worker> projectWorkers = ObjetiveFunctionUtil.ProjectWorkers(projectRole);

        boolean meet = true;

        int j = 0;
        while (j < roleWorkers.size() && meet) {

            RoleWorker roleWorker = roleWorkers.get(j);
            List<Worker> workers = roleWorker.getWorkers();

            int k = 0;
            while (k < workers.size() && meet) {
                Worker worker = workers.get(k);
                List<WorkerConflict> workerConflicts = worker.getWorkerConflictList1();

                int l = 0;
                while (l < projectWorkers.size() && meet) {
                    Worker workerC = projectWorkers.get(l);

                    if (!worker.getId().equals(workerC.getId())) {
                        int m = 0;
                        while (m < workerConflicts.size() && meet) {
                            if (workerC.getId().equals(workerConflicts.get(m).getWorkerFk().getId())
                                    || workerC.getId().equals(workerConflicts.get(m).getWorkerConflictFk().getId())) { //workerFk
                                if (workerConflicts.get(m).getIndexFk().getWeight() == 1) {
                                    meet = false;
                                }
                            }
                            m++;
                        }
                    }
                    l++;
                }
                k++;
            }
            j++;
        }
        return meet;
    }


    public void repareState(State state, int posProy) {

        List<Object> projects = state.getCode();
        TeamFormationCodification codification = (TeamFormationCodification) Strategy.getStrategy().getProblem().getCodification();
        Random generator = new Random();
        int i = 0;
        boolean meet = true;

        ProjectRole projectRole = (ProjectRole) projects.get(posProy);
        List<RoleWorker> roleWorkers = projectRole.getRoleWorkers();
        List<Worker> projectWorkers = ObjetiveFunctionUtil.ProjectWorkers(projectRole);

        int j = 0;
        while (j < roleWorkers.size() && meet) {

            RoleWorker roleWorker = roleWorkers.get(j);
            List<Worker> workers = roleWorker.getWorkers();

            int k = 0;
            while (k < workers.size()) {
                Worker worker = workers.get(k);
                List<WorkerConflict> workerConflicts = worker.getWorkerConflictList1();

                int l = 0;
                while (l < projectWorkers.size()) {
                    Worker workerC = projectWorkers.get(l);

                    if (!worker.getId().equals(workerC.getId())) {
                        int m = 0;
                        while (m < workerConflicts.size()) {
                            if (workerC.getId().equals(workerConflicts.get(m).getWorkerFk().getId())
                                    || workerC.getId().equals(workerConflicts.get(m).getWorkerConflictFk().getId())) { //workerFk
                                if (workerConflicts.get(m).getIndexFk().getWeight() == 1) {
                                    int personIndex = generator.nextInt(codification.getSearchArea().size());
                                    Worker newWorker = codification.getSearchArea().get(personIndex);
                                    ((ProjectRole) state.getCode().get(posProy)).getRoleWorkers().get(j).getWorkers().set(k, newWorker);
                                }
                            }
                            m++;
                        }
                    }
                    l++;
                }
                k++;
            }
            j++;
        }
    }
}
