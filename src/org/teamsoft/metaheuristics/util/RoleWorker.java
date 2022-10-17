package org.teamsoft.metaheuristics.util;

import org.teamsoft.entity.Role;
import org.teamsoft.entity.Worker;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RoleWorker {

    private Role role;
    private Long neededWorkers;
    private List<Worker> workers;
    private ArrayList<Object> workersEvaluation;
    private List<Worker> fixedWorkers; //trabajadores que fueron fijados en la pantalla

    public RoleWorker() {
        super();
    }

    /**
     * @param role
     * @param workers
     * @param neededWorkers
     * @param fixedWorkers
     */
    public RoleWorker(Role role, List<Worker> workers, Long neededWorkers, List<Worker> fixedWorkers) {
        super();
        this.role = role;
        this.workers = workers;
        this.neededWorkers = neededWorkers;
        this.fixedWorkers = fixedWorkers;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Long getNeededWorkers() {
        return neededWorkers;
    }

    public void setNeededWorkers(Long neededWorkers) {
        this.neededWorkers = neededWorkers;
    }

    public List<Worker> getWorkers() {
        return workers;
    }

    public void setWorkers(List<Worker> workers) {
        this.workers = workers;
    }

    public List<Worker> getFixedWorkers() {        
        return fixedWorkers;
    }

    public void setFixedWorkers(List<Worker> fixedWorkers) {
        this.fixedWorkers = fixedWorkers;
    }
    
    public ArrayList<Object> getWorkersEvaluation() {
        return workersEvaluation;
    }
    
    public void SetWorkersEvaluation(ArrayList<Object> workersEvaluation) {
        this.workersEvaluation = workersEvaluation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleWorker that = (RoleWorker) o;
        return Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(role);
    }
}
