package org.teamsoft.metaheuristics.restrictions;

import org.teamsoft.entity.Worker;
import org.teamsoft.metaheuristics.util.ProjectRole;
import org.teamsoft.metaheuristics.util.RoleWorker;
import problem.definition.State;

import java.util.ArrayList;
import java.util.List;

/**
 * chequear que un trabajador no esta repetido en el mismo rol, en un proyecto.
 *
 * @author G1lb3rt
 */
public class WorkerNotRepeatedInSameRole extends Constrain {

    public WorkerNotRepeatedInSameRole() {
    }

    @Override
    public Boolean ValidateState(State state) {

        List<Object> projects = state.getCode(); // obtener lista de proyectos

        int i = 0;
        boolean meet = true;
        while (i < projects.size() && meet) { //para cada projecto-rol
            ProjectRole projectRole = (ProjectRole) projects.get(i);
            List<RoleWorker> roleWorkers = projectRole.getRoleWorkers();

            int j = 0;
            while (j < roleWorkers.size() && meet) { //para cada rol-persona
                RoleWorker roleWorker = roleWorkers.get(j);

                List<Worker> aux = new ArrayList<>(); // concatenar listas de personas y personas fijadas por el usuario
                aux.addAll(roleWorker.getWorkers());
                aux.addAll(roleWorker.getFixedWorkers());

                int k = 0;
                while (k < aux.size() && meet) {  //para cada persona
                    Worker worker = aux.get(k);

                    meet = validatePerson(aux, worker);
                    k++;
                }
                j++;
            }
            i++;
        }
        return meet;
    }


    public boolean validatePerson(List<Worker> aux, Worker worker) {

        //aux podria ser la lista de personas del rol a analizar
        boolean meet = true;

        if (getWorkerOcurrences(aux, worker) > 1) { // si esta repetida para el rol actual
            meet = false;  // fallo de la restriccion
        }
        return meet;
    }

    /**
     * Contar ocurrencias de una persona en una lista.
     *
     * @param workerList
     * @param worker
     * @return
     */
    public static int getWorkerOcurrences(List<Worker> workerList, Worker worker) {
        int countWorkers = 0;

        for (Worker item : workerList) {
            if (worker.getId().equals(item.getId())) {
                countWorkers++;
            }
        }
        return countWorkers;
    }
}
