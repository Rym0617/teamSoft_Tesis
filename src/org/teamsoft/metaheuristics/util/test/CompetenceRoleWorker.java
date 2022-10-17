package org.teamsoft.metaheuristics.util.test;

import org.teamsoft.entity.Role;

import java.util.List;

public class CompetenceRoleWorker {

    private Role role;
    private List<CompetentWorker> workers;

    public CompetenceRoleWorker() {
        super();
    }

    /**
     * @param role
     * @param workers
     */
    public CompetenceRoleWorker(Role role, List<CompetentWorker> workers) {
        super();
        this.role = role;
        this.workers = workers;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<CompetentWorker> getWorkers() {
        return workers;
    }

    public void setWorkers(List<CompetentWorker> workers) {
        this.workers = workers;
    }
}
