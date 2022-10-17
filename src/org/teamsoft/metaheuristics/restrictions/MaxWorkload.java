package org.teamsoft.metaheuristics.restrictions;

import org.teamsoft.POJOS.TeamFormationParameters;
import org.teamsoft.entity.Cycle;
import org.teamsoft.entity.ProjectRoles;
import org.teamsoft.entity.Role;
import org.teamsoft.entity.Worker;
import org.teamsoft.metaheuristics.util.ProjectRole;
import org.teamsoft.metaheuristics.util.RoleWorker;
import problem.definition.State;

import java.util.ArrayList;
import java.util.List;


/**
 * La carga de trabajo de un trabajador no puede ser mayor que la definida en la
 * UI ( worload del trabajador mas carga de cada rol asignado en la solucion
 * debe ser menor que la definida)
 *
 * @author G1lb3rt
 */
public class MaxWorkload
        extends Constrain {

    private TeamFormationParameters parameters;

    public MaxWorkload(TeamFormationParameters parameters) {
        this.parameters = parameters;
    }


    public TeamFormationParameters getParameters() {
        return parameters;
    }


    public void setParameters(TeamFormationParameters parameters) {
        this.parameters = parameters;
    }


    public MaxWorkload() {
    }


    @Override
    public Boolean ValidateState(State state) {
        float maxWorkload = parameters.getMaxRoleLoad().getValue(); //obtener restricción para carga de trabajo
        List<Object> projects = state.getCode(); //lista de proyectos -roles
        List<Worker> people = new ArrayList<>(); // personas de la solución
        int i = 0;
        boolean meet = true;
        while (i < projects.size() && meet) { //para cada proyecto-rol
            ProjectRole projectRole = (ProjectRole) projects.get(i);
            List<RoleWorker> roleWorkers = projectRole.getRoleWorkers();
            int j = 0;
            while (j < roleWorkers.size() && meet) { //para cada rol-persona
                RoleWorker roleWorker = roleWorkers.get(j);
                List<Worker> aux = new ArrayList<>(); // concatenar listas de personas y personas fijadas por el usuario
                aux.addAll(roleWorker.getWorkers());
                aux.addAll(roleWorker.getFixedWorkers());
                float rolLoad = findWorkLoad(roleWorker.getRole(), projectRole.lastProjectCycle()); //carga de trabajo que implica desempeñar el rol en el proyecto actual
                int k = 0;
                while (k < aux.size() && meet) { //por cada persona en el rol
                    Worker worker = new Worker();
                    worker.setWorkload(aux.get(k).getWorkload());
                    worker.setId(aux.get(k).getId());
                    meet = validatePerson(worker, people, rolLoad, maxWorkload);
                    k++;
                }
                j++;
            }
            i++;
        }
        return meet;
    }

    public boolean validatePerson(Worker worker, List<Worker> people, float rolLoad, float maxWorkload) {
        int l = 0;
        boolean meet = true;
        if (!people.isEmpty()) {
            boolean found = false;
            while (l < people.size() && !found) { // recorrer lista de personas
                if (people.get(l).getId().equals(worker.getId())) {  //si ya se encuentra desempeñando otro rol (es decir si esta en la lista)
                    people.get(l).setWorkload(people.get(l).getWorkload() + rolLoad); //aumentar carga de trabajo
                    if (people.get(l).getWorkload() > maxWorkload) { //si sobrepasa la carga definida
                        meet = false;
                    }
                    found = true;
                }
                l++;
            }

            if (!found) { //si la persona no se encuentra desempeñando otro rol (no esta en la lista)
                if (worker.getWorkload() == null) {
                    worker.setWorkload(0f);
                }
                worker.setWorkload(worker.getWorkload() + rolLoad); //actualizar carga de trabajo de la persona
                if (worker.getWorkload() > maxWorkload) { //si sobrepasa la carga definida
                    meet = false;
                } else {
                    people.add(worker);
                }
            }
        }
        return meet;
    }

    /**
     * Carga de trabajo que implica desempñar un rol en un projecto dado
     *
     * @param role
     * @param lastProjectCycle
     * @return
     */
    public float findWorkLoad(Role role, Cycle lastProjectCycle) {
        float rolLoad = 0;
        List<ProjectRoles> roles = lastProjectCycle.getStructureFk().getProjectRolesList();
        int i = 0;
        boolean found = false;
        while (i < roles.size() && !found) {
            if (roles.get(i).getRoleFk().getId().equals(role.getId())) {
                rolLoad = roles.get(i).getRoleLoadFk().getValue();
                found = true;
            }
            i++;
        }
        return rolLoad;
    }
}
