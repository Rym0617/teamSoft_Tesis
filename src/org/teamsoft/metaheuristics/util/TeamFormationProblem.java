package org.teamsoft.metaheuristics.util;

import org.teamsoft.POJOS.TeamFormationParameters;
import problem.definition.Problem;

import java.util.List;

public class TeamFormationProblem extends Problem {

    private List<ProjectRole> projects;
    private TeamFormationParameters parameters;

    public TeamFormationProblem() {
        super();
    }

    public TeamFormationProblem(List<ProjectRole> projects, TeamFormationParameters parameters) {
        this.projects = projects;
        this.parameters = parameters;
    }

    public TeamFormationParameters getParameters() {
        return parameters;
    }

    public void setParameters(TeamFormationParameters parameters) {
        this.parameters = parameters;
    }

    public List<ProjectRole> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectRole> projects) {
        this.projects = projects;
    }

}
