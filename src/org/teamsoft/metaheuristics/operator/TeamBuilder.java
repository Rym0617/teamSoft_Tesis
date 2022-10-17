/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.metaheuristics.operator;

import org.teamsoft.POJOS.FixedWorker;
import org.teamsoft.POJOS.TeamFormationParameters;
import org.teamsoft.entity.*;
import org.teamsoft.metaheuristics.util.ProjectRole;
import org.teamsoft.metaheuristics.util.ProjectRoleState;
import org.teamsoft.metaheuristics.util.RoleWorker;

import java.util.ArrayList;
import java.util.List;

/**
 * @author G1lb3rt
 */
public class TeamBuilder {

    /**
     * Para construir una solucion inicial vacia (contendra solo las personas
     * que ya fueron asignadas al proyecto con anterioridad)
     *
     * @param parameters
     * @return
     */
    public static ProjectRoleState getInitialVoidSolution(TeamFormationParameters parameters) {

        ProjectRoleState initialSolution = new ProjectRoleState(); //estado inicial

        ArrayList<ProjectRole> projectRoles = new ArrayList<>(); //codificacion del problema
        ProjectRole projectRole;
        List<RoleWorker> roleWorkers; // roles-personas  para la codificación
        RoleWorker roleWorker;
        Project project;
        int i = 0;
        boolean found = false;

        if (parameters != null) {
            if (parameters.getProjects() != null) {
                while (i < parameters.getProjects().size() && !found) { //para cada proyecto
                    project = new Project(); //para evitar actualizar referencias
                    project.setClientEntityFk(parameters.getProjects().get(i).getProject().getClientEntityFk());
                    project.setClose(parameters.getProjects().get(i).getProject().getClose());
                    project.setCycleList(parameters.getProjects().get(i).getProject().getCycleList());
                    project.setEndDate(parameters.getProjects().get(i).getProject().getEndDate());
                    project.setFinalize(parameters.getProjects().get(i).getProject().getFinalize());
                    project.setId(parameters.getProjects().get(i).getProject().getId());
                    project.setInitialDate(parameters.getProjects().get(i).getProject().getInitialDate());
                    project.setProjectName(parameters.getProjects().get(i).getProject().getProjectName());
                    project.setProvinceFk(parameters.getProjects().get(i).getProject().getProvinceFk());

                    projectRole = new ProjectRole();
                    projectRole.setProject(project); //fijar proyecto en la codificación

                    Cycle lastCycle = lastProjectCycle(project); // obtener último ciclo del proyecto (basado en que este no tendrá fecha de fin)
                    ProjectStructure structure = lastCycle.getStructureFk(); //del  ultimo ciclo del proyecto obtener su estructura
                    List<ProjectRoles> neededRoles = structure.getProjectRolesList(); // de la estructura obtener roles necesarios para desarrollar el proyecto

                    roleWorkers = new ArrayList<>(); //crear nueva lista de roles-personas para cada proyecto
                    for (ProjectRoles item : neededRoles) { //para cada rol requerido por el proyecto, definir su estructura
                        roleWorker = new RoleWorker();

                        roleWorker.setRole(item.getRoleFk()); //establecer rol
                        roleWorker.setWorkers(new ArrayList<>());
                        roleWorker.setFixedWorkers(new ArrayList<>());
                        roleWorker.setNeededWorkers(item.getAmountWorkersRole()); //cantidad de personas necesarias para este rol en el projecto

                        List<AssignedRole> as = lastCycle.getAssignedRoleList(); //obtener lista de roles que ya fueron asignados al ciclo actual
                        for (AssignedRole ar : as) { //para cada rol asignado al ciclo actual
                            if (ar.getStatus().equalsIgnoreCase("ACTIVE")) { //si se encuentra activo el proyecto
                                roleWorker.getFixedWorkers().add(ar.getWorkersFk()); //agregar la persona a la lista 
                                roleWorker.setNeededWorkers(roleWorker.getNeededWorkers() - 1);//decrementar las personas requeridas
                            }
                        }

                        for (FixedWorker fixedWorker : parameters.getFixedWorkers()) {
                            if (fixedWorker.getProject().getId().equals(project.getId())) { //es el projecto al que se fijo?
                                if (fixedWorker.getRole().getId().equals(item.getRoleFk().getId())) { //es el rol ?
                                    roleWorker.getFixedWorkers().add(fixedWorker.getBoss()); //agregar la persona a la lista 
                                    roleWorker.setNeededWorkers(roleWorker.getNeededWorkers() - 1); //decrementar las personas requeridas
                                }
                            }
                        }

                        roleWorkers.add(roleWorker); // agregar el rol con sus datos a la configuracion del proyecto actual
                    }

                    projectRole.setRoleWorkers(roleWorkers); //establecer roles-personas de los proyectos-roles
                    projectRoles.add(projectRole); //agregar proyecto-rol a la lista de proyectos-roles
                    i++;
                }
                initialSolution.getCode().addAll(projectRoles); //codificar el problema
            }
        }
        return initialSolution;
    }

    /**
     * Retorna el ultimo ciclo dado un proyecto (retorna el que no tiene fecha
     * de fin)
     *
     * @param project
     * @return
     */
    public static Cycle lastProjectCycle(Project project) {
        Cycle cycle = null;
        List<Cycle> cycleList = project.getCycleList();

        int i = 0;
        boolean found = false;
        while (i < cycleList.size() && !found) {
            if (cycleList.get(i).getEndDate() == null) {
                cycle = cycleList.get(i);
                found = true;
            }
            i++;
        }
        return cycle;
    }

    public static ProjectRoleState getInitialMinimunRolesSolution(TeamFormationParameters parameters) {
        return new ProjectRoleState();
    }
}
