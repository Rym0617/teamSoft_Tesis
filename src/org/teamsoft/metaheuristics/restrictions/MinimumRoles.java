package org.teamsoft.metaheuristics.restrictions;

import org.teamsoft.POJOS.TeamFormationParameters;
import org.teamsoft.entity.Worker;
import org.teamsoft.metaheuristics.util.ProjectRole;
import org.teamsoft.metaheuristics.util.RoleWorker;
import problem.definition.State;

import java.util.List;

/**
 * Que una persona no pueda desempeñar menos roles que los definidos en la
 * interfaz EN TODOS LOS PROYECTOS
 *
 * @author jpinas
 */
public class MinimumRoles extends Constrain {

    private TeamFormationParameters parameters;

    public TeamFormationParameters getParameters() {
        return parameters;
    }

    public void setParameters(TeamFormationParameters parameters) {
        this.parameters = parameters;
    }

    public MinimumRoles(TeamFormationParameters parameters) {
        this.parameters = parameters;
    }

    public MinimumRoles() {
    }

    @Override
    public Boolean ValidateState(State state) {
        int minRoles = parameters.getMinimumRole(); //cant. min. de roles definida por el usuario
        List<Object> projects = state.getCode();  //lista de proyectos - roles
        boolean cumple = true;

        // por cada proyecto
        for (Object pr : projects) {
            ProjectRole projectRole = (ProjectRole) pr;
            for (RoleWorker roleWorker : projectRole.getRoleWorkers()) { // por cada rol
                for (Worker worker : roleWorker.getWorkers()) { // por cada trabajador
                    /*si la cant de roles asignadas en el proyecto es menor que
                     * la cantidad de roles minima establecida por el usuario entonces
                     * no es válida la solución*/
                    if (projectRole.getCantOccurenceWorker(worker) < minRoles) {
                        cumple = false;
                        break;
                    }
                }
                if (!cumple) break;
            }
            if (!cumple) break;
        }
        return cumple;
    }

    /**
     * se encarga de arreglar el estado para que cumpla con la restricción
     */
    public void invalidateState(State state) {
//        int minRoles = parameters.getMinimumRole(); //cant. min. de roles definida por el usuario
//        List<Object> projects = state.getCode();  //lista de proyectos - roles
//        for (Object pr : projects) {
//            ProjectRole projectRole = (ProjectRole) pr;
////            busca los workers con menos roles que los permitidos
//            List<Worker> unreliableWorkers = projectRole.findUnreliableWorkers(minRoles);
//            if (!unreliableWorkers.isEmpty()){ // si la existen workers con menos roles que los permitidos
////                dame aquellos q tengan más que el minimo permitido
//                List<Worker> overAssignedWorkers = projectRole.findWorkersAssignedToMoreRolesThanMinimum(minRoles);
//
//            }
//        }
    }

    /**
     * busca los trabajadores que tienen menos roles asignados que los que se necesitan
     *
     * @param projects
     */
    private void findUnreliableWorkers(List<Object> projects) {

    }
}
