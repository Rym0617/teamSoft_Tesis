package org.teamsoft.metaheuristics.restrictions;

import metaheurictics.strategy.Strategy;
import org.teamsoft.POJOS.SelectedWorkerIncompatibility;
import org.teamsoft.POJOS.TeamFormationParameters;
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
 * No peritir las incompatibilidades seleccionadas en la GUI, en un equipo
 * determinada
 */
public class IncompatibleSelectedWorkers extends Constrain {

    private TeamFormationParameters parameters;

    public IncompatibleSelectedWorkers(TeamFormationParameters parameters) {
        this.parameters = parameters;
    }

    public TeamFormationParameters getParameters() {
        return parameters;
    }

    public void setParameters(TeamFormationParameters parameters) {
        this.parameters = parameters;
    }

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

            for (Worker projectWorker : projectWorkers) {
                System.out.println(projectWorker.getPersonName() + "\n");
            }
        }
        return meet;
    }

    public boolean validateProyect(State state, int posProy) {
        List<SelectedWorkerIncompatibility> wiL = parameters.getsWI();
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

                int m = 0;
                while (m < wiL.size() && meet) {
                    SelectedWorkerIncompatibility swI = wiL.get(m);
                    Worker workerA = swI.getWorkerAFk();
                    Worker workerB = swI.getWorkerBFk();

                    if (workerA.getId().equals(worker.getId())) {
                        int l = 0;
                        while (l < projectWorkers.size() && meet) {
                            Worker workerC = projectWorkers.get(l);
                            if (workerC.getId().equals(workerB.getId())) {
                                if (workerConflicts.get(m).getIndexFk().getWeight() == 1) {
                                    meet = false;
                                }
                            }
                            l++;
                        }
                    }

                    //Segunda Variante
                    if (workerB.getId().equals(worker.getId())) {
                        int l = 0;
                        while (l < projectWorkers.size() && meet) {
                            Worker workerC = projectWorkers.get(l);
                            if (workerC.getId().equals(workerA.getId())) {
                                if (workerConflicts.get(m).getIndexFk().getWeight() == 1) {
                                    meet = false;
                                }
                            }
                            l++;
                        }
                    }
                    m++;
                }
                k++;
            }
            j++;
        }
        return meet;
    }


    public void invalidateState(State state) {

        List<SelectedWorkerIncompatibility> wiL = parameters.getsWI();
        SelectedWorkerIncompatibility swI = wiL.get(0);
        Worker workerA = swI.getWorkerAFk();
        Worker workerB = swI.getWorkerBFk();

        ((ProjectRole) state.getCode().get(0)).getRoleWorkers().get(0).getWorkers().set(0, workerA);
        ((ProjectRole) state.getCode().get(0)).getRoleWorkers().get(1).getWorkers().set(0, workerB);

    }


    public void repareState(State state, int posProy) {
        List<SelectedWorkerIncompatibility> wiL = parameters.getsWI();
        TeamFormationCodification codification = (TeamFormationCodification) Strategy.getStrategy().getProblem().getCodification();
        Random generator = new Random();

        List<Object> projects = state.getCode();

        ProjectRole projectRole = (ProjectRole) projects.get(posProy);
        List<RoleWorker> roleWorkers = projectRole.getRoleWorkers();
        List<Worker> projectWorkers = ObjetiveFunctionUtil.ProjectWorkers(projectRole);

        int j = 0;
        while (j < roleWorkers.size()) {

            RoleWorker roleWorker = roleWorkers.get(j);
            List<Worker> workers = roleWorker.getWorkers();

            int k = 0;
            while (k < workers.size()) {
                Worker worker = workers.get(k);
                List<WorkerConflict> workerConflicts = worker.getWorkerConflictList1();

                int m = 0;
                while (m < wiL.size()) {
                    SelectedWorkerIncompatibility swI = wiL.get(m);
                    Worker workerA = swI.getWorkerAFk();
                    Worker workerB = swI.getWorkerBFk();

                    if (workerA.getId().equals(worker.getId())) {
                        int l = 0;
                        while (l < projectWorkers.size()) {
                            Worker workerC = projectWorkers.get(l);
                            if (workerC.getId().equals(workerB.getId())) {
                                if (workerConflicts.get(m).getIndexFk().getWeight() == 1) {
                                    int personIndex = generator.nextInt(codification.getSearchArea().size());
                                    Worker newWorker = codification.getSearchArea().get(personIndex);
                                    ((ProjectRole) state.getCode().get(posProy)).getRoleWorkers().get(j).getWorkers().set(k, newWorker);
                                    //workers.set(k,newWorker);
                                }
                            }
                            l++;
                        }
                    }
                    m++;
                }
                k++;
            }
            j++;
        }
    }
}
