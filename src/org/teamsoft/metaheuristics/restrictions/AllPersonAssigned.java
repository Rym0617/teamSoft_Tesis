/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.metaheuristics.restrictions;

import org.teamsoft.POJOS.TeamFormationParameters;
import org.teamsoft.entity.Worker;
import org.teamsoft.metaheuristics.util.ProjectRole;
import org.teamsoft.metaheuristics.util.RoleWorker;
import problem.definition.State;

import java.util.ArrayList;
import java.util.List;

/**
 * @author G1lb3rt
 */
public class AllPersonAssigned extends Constrain {

    private TeamFormationParameters parameters;

    public TeamFormationParameters getParameters() {
        return parameters;
    }

    public void setParameters(TeamFormationParameters parameters) {
        this.parameters = parameters;
    }

    public AllPersonAssigned(TeamFormationParameters parameters) {
        this.parameters = parameters;
    }

    public AllPersonAssigned() {
    }

    @Override
    public Boolean ValidateState(State state) {

        List<Worker> toAssign = parameters.getSearchArea(); //todas las personas que deben ser asignadas

        List<Object> projects = state.getCode();  //lista de projectos - roles

        int p = 0;
        boolean meet = true;
        while (p < toAssign.size() && meet) {  //para cada persona que debe ser asignada
            int i = 0;
            boolean next = false;
            while (i < projects.size() && !next) {  // para cada projecto-rol
                ProjectRole projectRole = (ProjectRole) projects.get(i);
                List<RoleWorker> roleWorkers = projectRole.getRoleWorkers();

                int j = 0;
                while (j < roleWorkers.size() && !next) { //para cada rol-persona
                    RoleWorker roleWorker = roleWorkers.get(j);

                    List<Worker> aux = new ArrayList<>(); // concatenar listas de personas y personas fijadas por el usuario
                    aux.addAll(roleWorker.getWorkers());
                    aux.addAll(roleWorker.getFixedWorkers());

                    int k = 0;
                    while (k < aux.size() && !next) { // para cada persona en el rol
                        Worker worker = aux.get(k);

                        if (worker.getId().equals(toAssign.get(p).getId())) { //si esta asignada la persona que se verifica pasar a la siguiente
                            next = true;
                        }
                        k++;
                    }
                    j++;
                }
                i++;
            }

            if (next == false) { //si la persona verificada no se asignó a ningún proyecto falla la restricción
                meet = false;
            }
            p++;
        }
        return meet;
    }
}
