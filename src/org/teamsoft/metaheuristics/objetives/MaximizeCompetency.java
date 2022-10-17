package org.teamsoft.metaheuristics.objetives;

import org.teamsoft.POJOS.CompetencesTemplate;
import org.teamsoft.POJOS.ProjectRoleCompetenceTemplate;
import org.teamsoft.POJOS.TeamFormationParameters;
import org.teamsoft.entity.CompetenceValue;
import org.teamsoft.entity.Worker;
import org.teamsoft.metaheuristics.util.ObjetiveFunctionUtil;
import org.teamsoft.metaheuristics.util.ProjectRole;
import org.teamsoft.metaheuristics.util.RoleWorker;
import problem.definition.ObjetiveFunction;
import problem.definition.Problem.ProblemType;
import problem.definition.State;

import java.util.ArrayList;
import java.util.List;

public class MaximizeCompetency
        extends ObjetiveFunction {

    private TeamFormationParameters parameters;

    public static String className = "Competencias";


    public MaximizeCompetency(TeamFormationParameters parameters) {
        super();
        this.parameters = parameters;
        setTypeProblem(ProblemType.Maximizar);
    }

    public MaximizeCompetency() {
        super();
        setTypeProblem(ProblemType.Maximizar);
    }

    public TeamFormationParameters getParameters() {
        return parameters;
    }

    public void setParameters(TeamFormationParameters parameters) {
        this.parameters = parameters;
    }

    @Override
    public Double Evaluation(State state) {

        double maxCompetence = 0;

        double competenceLevel;
        double importance;
        double sumMaxCompetence;

        double sumCompetenceMax = 0L;
        double sumCompetenceMin = 0L;
        double projectCompetence;
        double maxProjectCompetence;
        Float competenceWeight = parameters.getMaxCompetencesByProjectWeight(); //peso asignado a las competencias de cada projecto

        List<ProjectRoleCompetenceTemplate> requirements = parameters.getProjects(); //Lista de proyectos configurados por el usuario
        List<Object> projects = state.getCode();  //lista de proyectos - roles

        int i = 0;
        boolean meet = true;
        Worker worker;
        List<CompetenceValue> personCompetences;
        List<CompetencesTemplate> competences;
        int l;
        boolean found;
        while (i < projects.size() && meet) {  // para cada proyecto-rol            
            ProjectRole projectRole = (ProjectRole) projects.get(i);
            projectCompetence = 0;
            maxProjectCompetence = 0;
            ProjectRoleCompetenceTemplate projectRequirements = ProjectRoleCompetenceTemplate.findProjectById(requirements, projectRole.getProject()); //requerimentos del proyecto actual

            int j = 0;

            while (j < projectRole.getRoleWorkers().size() && meet) { //para cada rol-persona
                RoleWorker rolePerson = projectRole.getRoleWorkers().get(j);

                int k = 0;
                while (k < rolePerson.getWorkers().size() && meet) { // para cada persona
                    worker = rolePerson.getWorkers().get(k);
                    personCompetences = worker.getCompetenceValueList(); // obtener competencias de la persona
                    //List<CompetencesTemplate> competences;
                    competences = new ArrayList<>();
                    l = 0;
                    found = false;

                    while (l < projectRequirements.getRoleCompetences().size() && !found) { // para cada rol requerido
                        if (rolePerson.getRole().getRoleName().equalsIgnoreCase(projectRequirements.getRoleCompetences().get(l).getRole().getRoleName())) { //si la persona juega el rol
                            competenceLevel = 0L;
                            importance = 0L;
                            sumMaxCompetence = 0L;

                            //sumCompetenceMax += (rolePerson.getWorkers().size()) * (projectRequirements.getRoleCompetences().get(l).getGenCompetences().size() + projectRequirements.getRoleCompetences().get(l).getTechCompetences().size()) * parameters.getMaxLevel().getLevels();

                            competences.clear(); // My Changes
                            competences.addAll(projectRequirements.getRoleCompetences().get(l).getGenCompetences());
                            competences.addAll(projectRequirements.getRoleCompetences().get(l).getTechCompetences()); //concatenar competencias tecnicas y genericas
                            found = true;

                            int m = 0;
                            while (m < competences.size() && meet) { //para cada competencia
                                competenceLevel += ObjetiveFunctionUtil.personCompetenceLevel(personCompetences, competences.get(m).getCompetence()) * competences.get(m).getCompetenceImportance().getLevels(); //nivel de la persona en la competencia actual
                                sumMaxCompetence += parameters.getMaxLevel().getLevels() * competences.get(m).getCompetenceImportance().getLevels();
                                importance += competences.get(m).getCompetenceImportance().getLevels();

                                m++;
                            }
                            if (importance != 0){ // si no hay competencia asociadas a al rol NaN dividas
                                projectCompetence += competenceLevel / importance;
                                maxProjectCompetence += sumMaxCompetence / importance;
                                maxCompetence += competenceLevel / importance;
                                sumCompetenceMax += sumMaxCompetence / importance;
                            }else {
                                break; // mejor sal del ciclo :)
                            }
                        }
                        l++;
                    }
                    k++;
                }
                k = 0;
                while (k < rolePerson.getFixedWorkers().size() && meet) { // para cada persona
                    worker = rolePerson.getFixedWorkers().get(k);
                    personCompetences = worker.getCompetenceValueList(); // obtener competencias de la persona
                    //List<CompetencesTemplate> competences;
                    competences = new ArrayList<>();
                    l = 0;
                    found = false;

                    while (l < projectRequirements.getRoleCompetences().size() && !found) { // para cada rol requerido
                        if (rolePerson.getRole().getRoleName().equalsIgnoreCase(projectRequirements.getRoleCompetences().get(l).getRole().getRoleName())) { //si la persona juega el rol
                            competenceLevel = 0L;
                            importance = 0L;
                            sumMaxCompetence = 0L;

                            //sumCompetenceMax += (rolePerson.getWorkers().size()) * (projectRequirements.getRoleCompetences().get(l).getGenCompetences().size() + projectRequirements.getRoleCompetences().get(l).getTechCompetences().size()) * parameters.getMaxLevel().getLevels();

                            competences.clear(); // My Changes
                            competences.addAll(projectRequirements.getRoleCompetences().get(l).getGenCompetences());
                            competences.addAll(projectRequirements.getRoleCompetences().get(l).getTechCompetences()); //concatenar competencias tecnicas y genericas
                            found = true;

                            int m = 0;
                            while (m < competences.size() && meet) { //para cada competencia
                                competenceLevel += ObjetiveFunctionUtil.personCompetenceLevel(personCompetences, competences.get(m).getCompetence()) * competences.get(m).getCompetenceImportance().getLevels(); //nivel de la persona en la competencia actual
                                sumMaxCompetence += parameters.getMaxLevel().getLevels() * competences.get(m).getCompetenceImportance().getLevels();
                                importance += competences.get(m).getCompetenceImportance().getLevels();

                                m++;
                            }
                            projectCompetence += competenceLevel / importance;
                            maxProjectCompetence += sumMaxCompetence / importance;
                            maxCompetence += competenceLevel / importance;
                            sumCompetenceMax += sumMaxCompetence / importance;
                        }
                        l++;
                    }
                    k++;
                }
                j++;
            }
            //projectCompetence = (projectCompetence - parameters.getMinLevel().getLevels()) / (parameters.getMaxLevel().getLevels() - parameters.getMinLevel().getLevels());
            projectRole.getProjectEvaluation()[0] = (projectCompetence / maxProjectCompetence);
            i++;
        }
        maxCompetence = (maxCompetence - sumCompetenceMin) / (sumCompetenceMax - sumCompetenceMin);
        return maxCompetence;
    }

}
