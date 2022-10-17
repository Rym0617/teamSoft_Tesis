package org.teamsoft.metaheuristics.util;

import java.util.Iterator;
import org.teamsoft.entity.Cycle;
import org.teamsoft.entity.Project;
import org.teamsoft.entity.Role;
import org.teamsoft.entity.Worker;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProjectRole {

    private Project project;
    private Double[] projectEvaluation = new Double[12];
    private List<RoleWorker> roleWorkers;

    public ProjectRole(Project project, Double[] projectEvaluation, List<RoleWorker> roleWorkers) {
        super();
        this.project = project;
        this.projectEvaluation = projectEvaluation;
        this.roleWorkers = roleWorkers;
    }

    public ProjectRole() {
        // TODO Auto-generated constructor stub
    }

    /**
     * Retorna el ultimo ciclo del proyecto (retorna el que no tiene fecha de
     * fin)
     *
     * @return
     */
    public Cycle lastProjectCycle() {
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

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<RoleWorker> getRoleWorkers() {
        return roleWorkers;
    }

    public void setRoleWorkers(List<RoleWorker> roleWorkers) {
        this.roleWorkers = roleWorkers;
    }

    public Double[] getProjectEvaluation() {
        return projectEvaluation;
    }

    public void setProjectEvaluation(Double[] projectEvaluation) {
        this.projectEvaluation = projectEvaluation;
    }


    /**
     * @param workerToFind persona a buscar
     * @return número de veces que está asignada una persona en un proyecto
     */
    public int getCantOccurenceWorker(Worker workerToFind) {
        return (int) roleWorkers.
                stream().
                map(roleWorker -> roleWorker.getWorkers().stream().filter(workerToFind::equals).findAny()).
                filter(Optional::isPresent).
                count();
    }

    /**
     * busca el rol que está puesto como líder
     */
    public RoleWorker getProjectBoss() {
        for (Iterator<RoleWorker> iterator = roleWorkers.iterator(); iterator.hasNext();) {
            RoleWorker roleWorker = iterator.next();
            if(roleWorker.getRole().isBoss()){
                return roleWorker;
            }
        }
        return null;
        
        //return roleWorkers.stream().filter(roleWorker -> roleWorker.getRole().isBoss()).findAny().orElse(null);
    }

    /**
     * obtiene la lista de roles asociados al proyecto
     */
    public List<Role> getRoles() {
        List<Role> result = new LinkedList<>();
        roleWorkers.forEach(e -> result.add(e.getRole()));
        return result;
    }

    /**
     * busca los workes con menos roles asignados que los definidos por parámetro
     */
    public List<Worker> findUnreliableWorkers(int cantRoles) {
        List<Worker> result = new LinkedList<>();
        roleWorkers.forEach(roleWorker -> result.
                addAll(roleWorker.
                        getWorkers().
                        stream().
                        filter(worker -> getCantOccurenceWorker(worker) < cantRoles).
                        collect(Collectors.toCollection(LinkedList::new)))
        );
        return result;
    }

    /**
     * busca los workes con más roles asignados que los definidos por parámetro
     */
    public List<Worker> findWorkersAssignedToMoreRolesThanMinimum(int cantRoles) {
        List<Worker> result = new LinkedList<>();
        roleWorkers.forEach(roleWorker -> result.
                addAll(roleWorker.
                        getWorkers().
                        stream().
                        filter(worker -> getCantOccurenceWorker(worker) > cantRoles).
                        collect(Collectors.toCollection(LinkedList::new)))
        );
        return result;
    }

    /**
     * busca los roles asignados de un trabajador
     */
    public List<RoleWorker> findAssignedRoleWorkerByWorker(Worker worker) {
        List<RoleWorker> result = new LinkedList<>();
        roleWorkers.forEach(roleWorker -> {
            if (roleWorker.getWorkers().contains(worker)) {
                result.add(roleWorker);
            }
        });
        return result;
    }
}
