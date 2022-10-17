package org.teamsoft.metaheuristics.restrictions;

import org.teamsoft.POJOS.TeamFormationParameters;
import org.teamsoft.entity.Worker;
import org.teamsoft.metaheuristics.util.ProjectRole;
import org.teamsoft.metaheuristics.util.RoleWorker;
import problem.definition.State;

import java.util.LinkedList;
import java.util.List;

/**
 * El boss necesita formar parte del equipo. Lo que es lo mismo, la persona que juega el
 * rol de jefe tiene que estar asignado a otro rol.
 *
 * @author jpinas
 */
public class BossNeedToBeAssignedToAnotherRole extends Constrain {

    private TeamFormationParameters parameters;

    public TeamFormationParameters getParameters() {
        return parameters;
    }

    public void setParameters(TeamFormationParameters parameters) {
        this.parameters = parameters;
    }

    public BossNeedToBeAssignedToAnotherRole(TeamFormationParameters parameters) {
        this.parameters = parameters;
    }

    public BossNeedToBeAssignedToAnotherRole() {
    }

    @Override
    public Boolean ValidateState(State state) {
        List<Object> projects = state.getCode();  //lista de proyectos - roles

        boolean isBossAssignedMoreThanOnce = false;
        for (Object pr : projects) { // por cada proyecto
            isBossAssignedMoreThanOnce = false;
            ProjectRole projectRole = (ProjectRole) pr;
            RoleWorker boss = projectRole.getProjectBoss(); // lider del proyecto
            List<RoleWorker> allProjectRoles = new LinkedList<>(projectRole.getRoleWorkers());
            allProjectRoles.remove(boss); // quita el jefe de los roles a buscar

            for (RoleWorker roleWorker : allProjectRoles) {
                List<Worker> workers = roleWorker.getWorkers(); // dame los workers del rol
                for (Worker worker : workers) {
                    if (worker.equals(boss.getWorkers().get(0))) { // pido en 0 xq lider es uno solo
                        isBossAssignedMoreThanOnce = true; // si esta salgo del ciclo
                        break;
                    }
                }
                if (isBossAssignedMoreThanOnce) break; //si esta salgo del ciclo
            }
            if (!isBossAssignedMoreThanOnce) break; // si no esta salgo del ciclo
        }
        return isBossAssignedMoreThanOnce;
    }

    public void invalidateState(State state) {
    }
}
