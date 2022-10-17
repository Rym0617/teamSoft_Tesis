/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.metaheuristics.objetives;

import java.util.ArrayList;
import java.util.List;
import org.teamsoft.POJOS.CompetencesTemplate;
import org.teamsoft.POJOS.ProjectRoleCompetenceTemplate;
import org.teamsoft.POJOS.TeamFormationParameters;
import org.teamsoft.entity.CompetenceValue;
import org.teamsoft.entity.Worker;
import org.teamsoft.metaheuristics.util.ProjectRole;
import problem.definition.ObjetiveFunction;
import problem.definition.Problem;
import problem.definition.State;
import org.teamsoft.metaheuristics.util.ObjetiveFunctionUtil;
import problem.definition.Problem.ProblemType;

/**
 *
 * @author Derly
 */
public class MaximizeMultiroleTeamMembers extends ObjetiveFunction{
    
      private TeamFormationParameters parameters;

    public static String className = "Multirol";
    
    public MaximizeMultiroleTeamMembers(TeamFormationParameters parameters){
        super();
        this.parameters = parameters;        
        setTypeProblem(ProblemType.Maximizar);
    }
    
       public MaximizeMultiroleTeamMembers() {
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
        
       
        double roleCompetences = 0L;
        
        double maxCompetence = 0L;
        double minCompetence = 0L;
        double normalizeTotalProjectsCompetences = 0L;
        double importance;
        
        List<ProjectRoleCompetenceTemplate> requirements = parameters.getProjects(); //Lista de proyectos configurados por el usuario
        List<Object> projects = state.getCode();  //lista de proyectos - roles
        
        int p = 0;
        
        List<Worker> allprojectWorkers = new ArrayList<>();
        List<CompetenceValue> personCompetences;
        List<CompetencesTemplate> competences;
        boolean roleFound;
        
        while(p < projects.size()){  // recorrer todos los proyectos
             ProjectRole projectRole = (ProjectRole) projects.get(p); // obtener proyecto actual
             
             ProjectRoleCompetenceTemplate projectRequirements = ProjectRoleCompetenceTemplate.findProjectById(requirements, projectRole.getProject()); //requerimentos del proyecto actual
             allprojectWorkers = ObjetiveFunctionUtil.ProjectWorkers(projectRole); //obtener todos los trabajadores del proyecto actual(p)
             
             int w = 0;
             
             while(w < allprojectWorkers.size()){ // recorrer todos los trabajadores del proyecto p
                 
                 personCompetences = allprojectWorkers.get(w).getCompetenceValueList(); // obtener competencias de la persona
                 
                 int r = 0;
                 
                 
                 while(r <  projectRole.getRoleWorkers().size()){ // recorrer todos los roles que requiere el proyecto p
                     
                     competences = new ArrayList<>();
                     
                     int n = 0; //
                     roleFound = false;
                    
                     
                     while(n < projectRequirements.getRoleCompetences().size() && !roleFound){ // recorrer todos los roles requeridos del proyecto para buscar sus competencias
                         
                         
                         if(projectRequirements.getRoleCompetences().get(n).getRole().getRoleName().equals(projectRole.getRoleWorkers().get(r).getRole().getRoleName())){ // si el rol requerido n coincide con el rol asignado r entonces llenar la lista de competencias requeridas del rol encontrado
                           
                           
                           importance = 0L;
                           competences.clear(); // My Changes
                           competences.addAll(projectRequirements.getRoleCompetences().get(n).getGenCompetences());
                           competences.addAll(projectRequirements.getRoleCompetences().get(n).getTechCompetences()); //concatenar competencias tecnicas y genericas
                           
                           roleFound = true;
                           
                           int c = 0;
                           
                           while(c < competences.size()){ // recorrer las competencias del rol encontrado
                               roleCompetences += ObjetiveFunctionUtil.personCompetenceLevel(personCompetences, competences.get(c).getCompetence()) * competences.get(c).getCompetenceImportance().getLevels(); //nivel de la persona en la competencia actual
                               minCompetence += parameters.getMinLevel().getLevels() * competences.get(c).getCompetenceImportance().getLevels();
                               maxCompetence += parameters.getMaxLevel().getLevels() * competences.get(c).getCompetenceImportance().getLevels();
                               importance = competences.get(c).getCompetenceImportance().getLevels();
                               c++;
                           }                           
                         }
                          n++;
                     } 
                   
                     r++;
                 }
                
                 w++;
             }
            
          p++;  
        }
        
        normalizeTotalProjectsCompetences = (roleCompetences - minCompetence)/(maxCompetence - minCompetence);
        return normalizeTotalProjectsCompetences;
    }
    
}
