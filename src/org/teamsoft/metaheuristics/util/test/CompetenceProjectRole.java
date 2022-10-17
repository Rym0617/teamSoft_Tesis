package org.teamsoft.metaheuristics.util.test;

import org.teamsoft.entity.Cycle;
import org.teamsoft.entity.Project;

import java.util.List;

public class CompetenceProjectRole {

    private Project project;
    private List<CompetenceRoleWorker> roleWorkers;

    public CompetenceProjectRole(Project project, List<CompetenceRoleWorker> roleWorkers) {
        super();
        this.project = project;
        this.roleWorkers = roleWorkers;
    }

    public CompetenceProjectRole() {
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

    public List<CompetenceRoleWorker> getRoleWorkers() {
        return roleWorkers;
    }

    public void setRoleWorkers(List<CompetenceRoleWorker> roleWorkers) {
        this.roleWorkers = roleWorkers;
    }

}
