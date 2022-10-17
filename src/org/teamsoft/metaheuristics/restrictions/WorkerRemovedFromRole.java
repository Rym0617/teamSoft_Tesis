package org.teamsoft.metaheuristics.restrictions;

import org.teamsoft.POJOS.TeamFormationParameters;
import org.teamsoft.entity.AssignedRole;
import org.teamsoft.entity.Cycle;
import org.teamsoft.entity.Worker;
import org.teamsoft.metaheuristics.util.ProjectRole;
import org.teamsoft.metaheuristics.util.RoleWorker;
import problem.definition.State;

import java.util.ArrayList;
import java.util.List;

/**
 * Verificar que el trabajador no este activo en un ciclo anterior del proyecto
 * actual
 *
 * @author G1lb3rt
 */
public class WorkerRemovedFromRole extends Constrain {

    private TeamFormationParameters parameters;

    public TeamFormationParameters getParameters() {
        return parameters;
    }

    public void setParameters(TeamFormationParameters parameters) {
        this.parameters = parameters;
    }

    public WorkerRemovedFromRole(TeamFormationParameters parameters) {
        this.parameters = parameters;
    }

    public WorkerRemovedFromRole() {
    }

    @Override
    public Boolean ValidateState(State state) {

        List<Object> projects = state.getCode();  //lista de proyectos - roles

        int i = 0;
        boolean meet = true;
        while (i < projects.size() && meet) {  // para cada projecto-rol
            ProjectRole projectRole = (ProjectRole) projects.get(i);
            List<RoleWorker> roleWorkers = projectRole.getRoleWorkers();

            List<Cycle> cycleList = projectRole.getProject().getCycleList();   //obtener ciclos del proyeccto actual

            int j = 0;
            while (j < roleWorkers.size() && meet) { //para cada rol-persona
                RoleWorker roleWorker = roleWorkers.get(j);

                List<Worker> aux = new ArrayList<>(); // concatenar listas de personas y personas fijadas por el usuario
                aux.addAll(roleWorker.getWorkers());
                aux.addAll(roleWorker.getFixedWorkers());

                int k = 0;
                while (k < aux.size() && meet) { // para cada persona
                    Worker worker = aux.get(k);

                    meet = validatePerson(worker, roleWorker, cycleList);
                    k++;
                }
                j++;
            }
            i++;
        }
        return meet;
    }

    public boolean validatePerson(Worker worker, RoleWorker roleWorker, List<Cycle> cycleList) {
        boolean meet = true;

        int l = 0;
        while (l < cycleList.size() && meet) { // para cada ciclo

            List<AssignedRole> assignedRoleList = cycleList.get(l).getAssignedRoleList(); //obtener lista de roles asignados al ciclo

            int m = 0;
            while (m < assignedRoleList.size() && meet) { // para cada rol asignado al ciclo
                if (assignedRoleList.get(m).getStatus().equals("ACTIVE") && assignedRoleList.get(m).getWorkersFk().getId().equals(worker.getId()) && assignedRoleList.get(m).getRolesFk().getId().equals(roleWorker.getRole().getId())) {
                    meet = false;
                }
                m++;
            }
            l++;
        }

        return meet;
    }

}
