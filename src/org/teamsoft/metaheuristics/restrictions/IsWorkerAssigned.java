package org.teamsoft.metaheuristics.restrictions;

import org.teamsoft.entity.AssignedRole;
import org.teamsoft.entity.Worker;
import org.teamsoft.metaheuristics.util.ProjectRole;
import org.teamsoft.metaheuristics.util.RoleWorker;
import problem.definition.State;

import java.util.ArrayList;
import java.util.List;

import static org.teamsoft.metaheuristics.restrictions.WorkerNotRepeatedInSameRole.getWorkerOcurrences;

/**
 * Que si ya es jefe de proyecto no pueda jugar ningun otro rol en la solucion
 * (OJO REVISAR XQ NO COINSIDE LA DESCRIPCION Y POR CONSIGUIENTE MI
 * IMPLEMENTACION CON LA SOLUCION ANTERIOR)
 *
 */
public class IsWorkerAssigned extends Constrain {

    public IsWorkerAssigned() {
    }

    @Override
    public Boolean ValidateState(State state) {
        List<Object> projects = state.getCode(); // obtener lista de proyectos
        List<Worker> people = new ArrayList<>(); // lista para añadir a todas las personas de la solucion que no son jefes de proyecto
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
                if (!roleWorker.getRole().isBoss()) { // si el rol actual no es jefe de proyecto
                    people.addAll(aux); //añadir personas que juegan el rol a la lista de personas del equipo actual
                } else {
                    int k = 0;
                    while (k < aux.size() && meet) {  //para cada jefe del proyecto actual
                        Worker worker = aux.get(k);
                        meet = validatePerson(people, worker);
                        k++;
                    }
                }
                j++;
            }
            i++;
        }

        int l = 0;
        while (l < people.size() && meet) {  //para cada persona en un rol DISTINTO del rol Jefe de Proyecto
            Worker worker = people.get(l);
            List<AssignedRole> roles = worker.getAssignedRoleList(); // obtener roles asignados anteriormente
            int m = 0;
            while (m < roles.size() && meet) { // para cada rol asignado
                if (roles.get(m).getRolesFk().isBoss() && roles.get(m).getStatus().equalsIgnoreCase("ACTIVE")) { // si es Jefe de P. y aun está activo
                    meet = false; // falla la retriccion porque un Jefe de Proyecto esta desarrollando (propuesto para desarrollar) otro rol
                }
                m++;
            }
            l++;
        }
        return meet;
    }


    public boolean validatePerson(List<Worker> people, Worker worker) {
        //peopel lista de todas las personas de la solucion que  son jefes de proyecto
        boolean meet = true;
        if (getWorkerOcurrences(people, worker) >= 1) { // verificar si desempeña un rol diferente de Jefe de P. (Da error si llamo al metodo estatico sin pedirlcelo a la clase?)
            meet = false;  // fallo de la restricción
        }
        return meet;
    }
}
