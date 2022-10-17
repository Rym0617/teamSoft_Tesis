/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.myBeans;

import org.teamsoft.POJOS.*;
import org.teamsoft.entity.*;
import org.teamsoft.metaheuristics.operator.TeamBuilder;
import org.teamsoft.model.PersonGroupFacade;
import org.teamsoft.model.ProjectFacade;
import org.teamsoft.model.WorkerFacade;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alejandro Durán & jpinas
 */
@Named("tF_StepOneController")
@SessionScoped
public class TF_StepOneController implements Serializable {

    @Inject
    PersonGroupFacade personGFacade;
    @Inject
    ProjectFacade projFacade;
    @Inject
    WorkerFacade workerFacade;

    private List<String> projectIdList = null; //manejar listado de proyectos
    private ArrayList<Project> projectList = new ArrayList<>();
    private List<String> groupIdList = null;
    private ArrayList<PersonGroup> groupList = new ArrayList<>();
    private String confRole = "all";        //limitar cantidad max de roles "all" (sin limite) "custom" (limitada)
    private int roleMax;                    //cantidad max de roles
    private boolean confAllGroup = false;   //asignar a todas las personas del grupo
    private boolean onlyOneProject = false; //cada persona se asigna a un solo proyecto
    private int confFormMode = 1;           //modo de conformación del equipo 1-Jefe primero/Secuencial

    private boolean bossNeedToBeAssigned = false;

    private boolean enableCantMin = false;
    private int roleMin = 1;                    //cantidad max de roles

    private ArrayList<PersonPerProjectAmount> ppg = new ArrayList<>();
    private boolean clear;


    public void changeClear() {
        clear = true;
    }

    public ProjectFacade getProjFacade() {
        return projFacade;
    }

    public void setProjFacade(ProjectFacade projFacade) {
        this.projFacade = projFacade;
    }

    public PersonGroupFacade getPersonGFacade() {
        return personGFacade;
    }

    public void setPersonGFacade(PersonGroupFacade personGFacade) {
        this.personGFacade = personGFacade;
    }

    public Project getProject(String id) {
        return getProjFacade().find(Long.parseLong(id));
    }

    public PersonGroup getGroup(String id) {
        return getPersonGFacade().find(Long.parseLong(id));
    }

    public List<String> getProjectIdList() {
        return projectIdList;
    }

    public void setProjectIdList(List<String> projectIdList) {
        this.projectIdList = projectIdList;
    }

    public ArrayList<Project> getProjectList() {

        if (projectIdList != null) {
            projectList.clear();
            for (int j = 0; j < projectIdList.size(); j++) {
                projectList.add(j, getProject(projectIdList.get(j)));
            }
        }

        return projectList;
    }

    public void setProjectList(ArrayList<Project> projectList) {
        this.projectList = projectList;
    }

    public List<String> getGroupIdList() {
        return groupIdList;
    }

    public void setGroupIdList(List<String> groupIdList) {
        this.groupIdList = groupIdList;
    }

    public ArrayList<PersonGroup> getGroupList() {
        if (groupIdList != null) {
            groupList.clear();
            for (int j = 0; j < groupIdList.size(); j++) {
                groupList.add(j, getGroup(groupIdList.get(j)));
            }
        }
        return groupList;
    }

    public int getGroupListAmount() {
        int amount = 0;
        getGroupList();

        for (PersonGroup group : groupList) {
            amount += group.getWorkerList().size();
        }
        return amount;
    }

    public void setGroupList(ArrayList<PersonGroup> groupList) {
        this.groupList = groupList;
    }

    public String getConfRole() {
        return confRole;
    }

    public void setConfRole(String confRole) {
        this.confRole = confRole;
    }

    public int getRoleMax() {
        return roleMax;
    }

    public void setRoleMax(int roleMax) {
        this.roleMax = roleMax;
    }

    public boolean isConfAllGroup() {
        return confAllGroup;
    }

    public void setConfAllGroup(boolean confAllGroup) {
        this.confAllGroup = confAllGroup;
    }

    public boolean isOnlyOneProject() {
        return onlyOneProject;
    }

    public void setOnlyOneProject(boolean onlyOneProject) {
        this.onlyOneProject = onlyOneProject;
    }

    public int getConfFormMode() {
        return confFormMode;
    }

    public void setConfFormMode(int confFormMode) {
        this.confFormMode = confFormMode;
    }

    public boolean isBossNeedToBeAssigned() {
        return bossNeedToBeAssigned;
    }

    public void setBossNeedToBeAssigned(boolean bossNeedToBeAssigned) {
        this.bossNeedToBeAssigned = bossNeedToBeAssigned;
    }

    public int getRoleMin() {
        return roleMin;
    }

    public void setRoleMin(int roleMin) {
        this.roleMin = roleMin;
    }

    public boolean isEnableCantMin() {
        return enableCantMin;
    }

    public void setEnableCantMin(boolean enableCantMin) {
        this.enableCantMin = enableCantMin;
    }

    public ArrayList<PersonPerProjectAmount> getPpg() {
        if (clear) {
            ppg.clear();
            clear = false;

            if (projectIdList != null) {
                for (int j = 0; j < projectIdList.size(); j++) {
                    PersonPerProjectAmount ppGroup = new PersonPerProjectAmount(getProject(projectIdList.get(j)), 0);
                    ppg.add(j, ppGroup);
                }
            }
        }
        return ppg;
    }

    public void setPpg(ArrayList<PersonPerProjectAmount> ppg) {
        this.ppg = ppg;
    }

    /////////////////////////////////////////////////////////
    public TeamFormationParameters initializeParameters() {

        TeamFormationParameters parameters = new TeamFormationParameters();

        parameters.setProjects(formatProjects(getProjectList())); //formatear lista de proyectos
        parameters.setSearchArea(getSearchArea(getGroupList()));  //personas disponibles para la conformacion

        parameters.setConfFormMode(confFormMode);

        if (enableCantMin) {
            parameters.setMinimunRoles(true);
            parameters.setMinimumRole(roleMin);
        }

        if (bossNeedToBeAssigned) {
            parameters.setBossNeedToBeAssignedToAnotherRoles(true);
        }

        if (confRole.equalsIgnoreCase("custom")) { //cantidad maxima de roles que puede jugar una persona
            parameters.setConfRole(true);
            parameters.setMaximunRoles(roleMax);
        } else {
            parameters.setConfRole(false);
        }
        parameters.setOnlyOneProject(onlyOneProject); ///cada persona se asigna a un solo proyecto

        parameters.setConfAllGroup(confAllGroup);
        if (confAllGroup) {
            parameters.setPpg(ppg);
        }

        return parameters;
    }

    /**
     * Dar formato a lista de proyectos (convertitr a la estructura:
     * ProjectRoleCompetenceTemplate)
     *
     * @param unformatedProjects
     * @return
     */
    public List<ProjectRoleCompetenceTemplate> formatProjects(List<Project> unformatedProjects) {

        List<ProjectRoleCompetenceTemplate> formatedProjectList = new ArrayList<>();

        if (!unformatedProjects.isEmpty()) {
            ProjectRoleCompetenceTemplate prct;

            for (Project item : unformatedProjects) { //para cada proyecto
                Cycle lastC = TeamBuilder.lastProjectCycle(item); // obtener ultimo ciclo
                ProjectStructure structure = lastC.getStructureFk(); //obtener estructura del ultimo ciclo
                List<ProjectRoles> projectRoles = structure.getProjectRolesList(); //obtener roles requeridos para desarrollar el proyecto
                List<ProjectTechCompetence> techComp = projectTechnicalRequirements(projectRoles); //obtener competencias tecnicas requeridas por el proyecto

                prct = new ProjectRoleCompetenceTemplate();
                prct.setProject(item);
                prct.setRoleCompetences(doPCTFormat(techComp, projectRoles));//dar formato a las competencias del proyecto
                formatedProjectList.add(prct); //agregar proyecto con formato prct a la lista
            }
        }
        return formatedProjectList;
    }

    /**
     * Obtener las competencias tecnicas requeridas por un projecto dados sus
     * roles
     *
     * @param projectRoles
     * @return
     */
    public List<ProjectTechCompetence> projectTechnicalRequirements(List<ProjectRoles> projectRoles) {
        List<ProjectTechCompetence> out = new ArrayList<>();

        for (ProjectRoles projectRole : projectRoles) {
            out.addAll(projectRole.getProjectTechCompetenceList());
        }

        return out;
    }

    /**
     * Convertir competencias de un proyecto alformato PCT
     * (ProjectCompetenceTemplate)
     *
     * @param projectTechComp
     * @param projectRoles
     * @return
     */
    public List<ProjectCompetenceTemplate> doPCTFormat(List<ProjectTechCompetence> projectTechComp, List<ProjectRoles> projectRoles) {
        List<ProjectCompetenceTemplate> pctList = new ArrayList<>();

        ProjectCompetenceTemplate projectCT;
        for (ProjectRoles prole : projectRoles) {  //para cada rol de proyectos
            projectCT = new ProjectCompetenceTemplate();
            projectCT.setRole(prole.getRoleFk()); //declarar rol del ProjectCompetenceTemplate

            List<RoleCompetition> roleCompetitionList = prole.getRoleFk().getRoleCompetitionList(); //obtener competencias genericas para el rol
            List<ProjectTechCompetence> roleTechCompetenceList = prole.getProjectTechCompetenceList(); //obtener competencias tecnicas para el rol en el proyecto

            List<CompetencesTemplate> genComp = doCTFormat(roleCompetitionList); //dar formato a las competencias genericas
            List<CompetencesTemplate> techComp = doCTFormat(roleTechCompetenceList);//dar formato a las competencias tecnicas

            projectCT.setGenCompetences(genComp); // asignar competencias con nuevo formato
            projectCT.setTechCompetences(techComp);

            pctList.add(projectCT);
        }

        return pctList;
    }

    /**
     * Dar formato CompetenceTemplate a las competencias de un proyecto
     *
     * @param competences
     * @return
     */
    public List<CompetencesTemplate> doCTFormat(List competences) {
        List<CompetencesTemplate> compTemplateList = new ArrayList<>();

        CompetencesTemplate cTemplate;

        for (Object item : competences) { //para cada competencia
            cTemplate = new CompetencesTemplate();

            if (item instanceof RoleCompetition) { //se obtienen competencias genericas
                cTemplate.setCompetence(((RoleCompetition) item).getCompetenceFk());
                cTemplate.setCompetenceImportance(((RoleCompetition) item).getCompImportanceFk());
                cTemplate.setMinLevel(((RoleCompetition) item).getLevelsFk());
            }

            if (item instanceof ProjectTechCompetence) {//se obtienen competencias tecnicas
                cTemplate.setCompetence(((ProjectTechCompetence) item).getCompetenceFk());
                cTemplate.setCompetenceImportance(((ProjectTechCompetence) item).getCompetenceImportanceFk());
                cTemplate.setMinLevel(((ProjectTechCompetence) item).getLevelFk());
            }
            compTemplateList.add(cTemplate);
        }

        return compTemplateList;
    }

    /**
     * Obtener ProjectTechCompetence para un rol determinado
     *
     * @param competencesList
     * @param role
     * @return
     */
//    public List<ProjectTechCompetence> getRolTechComp(List<ProjectTechCompetence> competencesList, Role role) {
//        List<ProjectTechCompetence> competences = new ArrayList<>();
//
//        for (ProjectTechCompetence item : competencesList) {
//            if (item.getgetRoleFk().getId().equals(role.getId())) {
//                competences.add(item);
//            }
//        }
//        return competences;
//    }

    /**
     * ARREGLAR CONSECUENTEMENTE CON LA FORMA DE SELECCIONAR A LA PERSONA DEL
     * ARBOL EN PASO 1 Retorna dada una lista de personas y un listado de
     * grupos, las personas que pertenecen a cualquiera de los grupos
     *
     * @param groups
     * @return
     */
    public ArrayList<Worker> getSearchArea(List<PersonGroup> groups) {
        ArrayList<Worker> searchArea = new ArrayList<>();
        List<Worker> source = workerFacade.findAll();

        for (Worker item : source) { //para cada persona
            if (item.getStatus() != null) {
                if (item.getStatus().equalsIgnoreCase("ACTIVE")) { //si esta activa
                    int i = 0;
                    boolean belong = false;
                    while (i < groups.size()) // para cada grupo
                    {
                        if (item.getGroupFk().getName().equalsIgnoreCase(groups.get(i).getName())) //si la persona pertenece
                        {
                            searchArea.add(item);
                            belong = true;
                        }
                        i++;
                    }
                }
            }
        }
        return searchArea;
    }

    public boolean isMaximunRolesSet() {
        return confRole != null;
    }
}
