package org.teamsoft.metaheuristics.restrictions;

import org.teamsoft.entity.Worker;
import org.teamsoft.metaheuristics.util.ProjectRole;
import org.teamsoft.metaheuristics.util.RoleWorker;
import problem.definition.State;

import java.util.ArrayList;
import java.util.List;

//es IS o CO  ; para jefe de proyecto
public class IsISCO extends Constrain {

    public IsISCO() {
    }

    @Override
    public Boolean ValidateState(State state) {
        List<Object> projects = state.getCode(); //lista de proyectos  roles
        int i = 0;
        boolean meet = true;
        while (i < projects.size() && meet) { // para cada proyecto
            ProjectRole projectRole = (ProjectRole) projects.get(i);
            List<RoleWorker> roleWorkers = projectRole.getRoleWorkers();
            int j = 0;
            while (j < roleWorkers.size() && meet) { //para cada rol
                RoleWorker roleWorker = roleWorkers.get(j);
                List<Worker> aux = new ArrayList<>(); // concatenar listas de personas y personas fijadas por el usuario
                aux.addAll(roleWorker.getWorkers());
                aux.addAll(roleWorker.getFixedWorkers());
                if (roleWorker.getRole().isBoss()) { //si el rol actual es Jefe de proyecto
                    int k = 0;
                    while (k < aux.size() && meet) { // para cada persona
                        Worker worker = aux.get(k);
                        meet = validatePerson(worker);
                        k++;
                    }
                }
                j++;
            }
            i++;
        }
        return meet;
    }


    public boolean validatePerson(Worker worker) {
        boolean meet = true;

        if (worker.getWorkerTest() != null) { //si se registraron sus caracteristicas psicologicas
            if ((worker.getWorkerTest().getCO() == 'I' || worker.getWorkerTest().getCO() == 'E') && (worker.getWorkerTest().getIS() == 'I' || worker.getWorkerTest().getIS() == 'E')) {
                meet = false;
            }
        } else {  //si no se cuenta con los datos psicologicos de la persona
            meet = false;
        }
        return meet;
    }

}
