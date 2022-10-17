/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.metaheuristics.restrictions;

import org.teamsoft.entity.PersonalProjectInterests;
import org.teamsoft.entity.Project;
import org.teamsoft.entity.Worker;
import org.teamsoft.metaheuristics.util.ProjectRole;
import org.teamsoft.metaheuristics.util.RoleWorker;
import problem.definition.State;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lesmes
 */
public class BossTeamInterest extends Constrain {

    @Override
    public Boolean ValidateState(State state) {

        List<Object> projects = state.getCode(); //lista de proyectos

        int i = 0;
        boolean meet = true;
        while (i < projects.size() && meet) { //para cada proyecto -rol

            ProjectRole projectRole = (ProjectRole) projects.get(i);
            Project project = projectRole.getProject();

            List<RoleWorker> roleWorkers = projectRole.getRoleWorkers();

            int j = 0;
            while (j < roleWorkers.size() && meet) { //para cada rol trabajador
                RoleWorker roleWorker = roleWorkers.get(j);

                if (roleWorker.getRole().isBoss()) { // si el rol actual es jefe de proyecto
                    List<Worker> aux = new ArrayList<>();
                    aux.addAll(roleWorker.getWorkers());
                    aux.addAll(roleWorker.getFixedWorkers());
                    int k = 0;
                    while (k < aux.size() && meet) { //para cada persona

                        Worker worker = aux.get(k);
                        meet = validatePerson(worker, project);
                        k++;
                    }
                }
                j++;
            }
            i++;
        }
        return meet;
    }

    public boolean validatePerson(Worker worker, Project project) {
        boolean meet = true;

        List<PersonalProjectInterests> personalProjectInterestses = worker.getPersonalProjectInterestsList();
        int i = 0;
        while (i < personalProjectInterestses.size() && meet) {
            PersonalProjectInterests personalProjectInterests = personalProjectInterestses.get(i);
            if (personalProjectInterests.getProjectFk().getId().equals(project.getId())) { // si es el equipo
                if (personalProjectInterests.getPreference()) { // si esta interesado
                    return true;
                } else {
                    meet = false;
                }
            }
            i++;
        }
        return meet;
    }
}
