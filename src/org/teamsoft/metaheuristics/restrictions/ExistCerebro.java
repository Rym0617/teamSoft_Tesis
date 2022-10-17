package org.teamsoft.metaheuristics.restrictions;

import metaheurictics.strategy.Strategy;
import org.teamsoft.POJOS.TeamFormationParameters;
import org.teamsoft.entity.Worker;
import org.teamsoft.entity.WorkerTest;
import org.teamsoft.metaheuristics.util.ProjectRole;
import org.teamsoft.metaheuristics.util.RoleWorker;
import org.teamsoft.metaheuristics.util.TeamFormationCodification;
import problem.definition.State;

import java.util.ArrayList;
import java.util.List;

/**
 * Que en el equipo existan la cantidad de personas con el rol cerebro definidas
 * en la GUI, 1 por defecto.
 */
public class ExistCerebro extends Constrain {

    private TeamFormationParameters parameters;

    public ExistCerebro(TeamFormationParameters parameters) {
        this.parameters = parameters;
    }

    public TeamFormationParameters getParameters() {
        return parameters;
    }

    public void setParameters(TeamFormationParameters parameters) {
        this.parameters = parameters;
    }

    public ExistCerebro() {
    }

    @Override
    public Boolean ValidateState(State state) {

        int min = parameters.getCountBrains();

        int personCount = 0; //contador de personas con rol cerebro
        List<Object> projects = state.getCode();  //lista de projectos - roles

        int i = 0;
        boolean meet = false;
        while (i < projects.size() && !meet) {  // para cada projecto-rol
            ProjectRole projectRole = (ProjectRole) projects.get(i);
            List<RoleWorker> roleWorkers = projectRole.getRoleWorkers();

            int j = 0;
            while (j < roleWorkers.size() && !meet) { //para cada rol-persona
                RoleWorker roleWorker = roleWorkers.get(j);

                List<Worker> aux = new ArrayList<>(); // concatenar listas de personas y personas fijadas por el usuario
                aux.addAll(roleWorker.getWorkers());
                aux.addAll(roleWorker.getFixedWorkers());

                int k = 0;
                while (k < aux.size() && !meet) { // para cada persona
                    Worker worker = aux.get(k);

                    if (worker.getWorkerTest() != null) {
                        if (worker.getWorkerTest().getCE() != 'I' && worker.getWorkerTest().getCE() != 'E') { // si tiene rol cerebro
                            personCount++; //cuento uno
                        }
                        if (personCount >= min) { //si esta cubierta la cantidad definida por el usuario
                            meet = true;
                        }
                    }
                    k++;
                }
                j++;
            }
            i++;
        }
        return meet;
    }


    public void repareState(State state, int cantFaltan) {

        TeamFormationCodification codification = (TeamFormationCodification) Strategy.getStrategy().getProblem().getCodification();
        List<Object> projects = state.getCode();  //lista de projectos - roles

        ArrayList<Worker> candidatos = new ArrayList<>();

        int i = 0;
        while (i < codification.getSearchArea().size() && (candidatos.size() < cantFaltan)) {

            Worker worker = codification.getSearchArea().get(i);
            WorkerTest workerTest = worker.getWorkerTest(); //obtener caracteristicas psicologicas

            if (workerTest.getCE() != 'I' && workerTest.getCE() != 'E') {
                candidatos.add(worker);
            }

        }

        int c = 0;
        int x = 0;
        while (x < projects.size() && !candidatos.isEmpty()) {
            ProjectRole projectRole = (ProjectRole) projects.get(x);
            List<RoleWorker> roleWorkers = projectRole.getRoleWorkers();
            int j = 0;
            while (j < roleWorkers.size() && !candidatos.isEmpty()) {
                RoleWorker rw = roleWorkers.get(j);
                int k = 0;
                while (k < rw.getWorkers().size()) {
                    Worker worker = rw.getWorkers().get(k);
                    if (!(worker.getWorkerTest().getCE() != 'I' && worker.getWorkerTest().getCE() != 'E')) { // si tiene rol cerebro
                        rw.getWorkers().set(k, candidatos.remove(c));
                        c++;
                    }
                    k++;
                }
                j++;
            }
            x++;
        }
    }
}
