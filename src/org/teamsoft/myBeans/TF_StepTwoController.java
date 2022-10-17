/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.myBeans;

import metaheurictics.strategy.Strategy;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.teamsoft.POJOS.*;
import org.teamsoft.entity.*;
import org.teamsoft.locale.LocaleConfig;
import org.teamsoft.metaheuristics.operator.TeamBuilder;
import org.teamsoft.metaheuristics.restrictions.*;
import org.teamsoft.metaheuristics.util.*;
import org.teamsoft.model.CostDistanceFacade;
import org.teamsoft.model.LevelsFacade;
import org.teamsoft.model.RoleFacade;
import problem.definition.ObjetiveFunction;
import problem.definition.Problem;
import problem.extension.FactoresPonderados;
import problem.extension.TypeSolutionMethod;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author G1lb3rt
 */
@Named("tF_StepTwoController")
@SessionScoped
public class TF_StepTwoController implements Serializable {

    @EJB
    RoleFacade roleFacade;
    @EJB
    LevelsFacade levelsFacade;
    @EJB
    CostDistanceFacade costDistanceFacade;
    @Inject
    LocaleConfig localeConfig;

    TeamFormationParameters parametersBoss;
    //////////////////////////////////////////////////////////////////////////////////
    //////////////////VARIABLES PARA LA SELECCIÓN DEL JEFE DE PROYECTO////////////////
    //////////////////////////////////////////////////////////////////////////////////
    private TypeSolutionMethod solutionMethodOptions;
    ///TOMAR EN CUENTA O NO ESTAS RESTRICCIONES///////////////
    private boolean takeCompetences = false;
    private boolean takeWorkLoad = false;
    private boolean takeCostDistance = false;
    private boolean takeInterests = false;
    private boolean takeProjectInterests = false;
    private boolean takeSynergy = false;
    private boolean takeMbti = false;

    /////PESO PARA LAS RESTRICCIONES///////////////
    private float competenceWeight = 0f;
    private float workLoadWeight = 0f;
    private float costDistanceWeight = 0f;
    private float interestWeight = 0f;
    private float projectInterestWeight = 0f;
    private float synergyWeight = 0f;
    private float mbtiWeight = 0f;

    /////VARIABLES PARA LA PESTAÑA COMPETENCIAS DEL ROL///////////////
    private List<ProjectRoleCompetenceTemplate> projects; //lista de proyectos seleccionados en paso 1 del wizard
    private ProjectRoleCompetenceTemplate selectedProject; //proyecto seleccionado en el combo box para ajustar competencias
    private List<CompetencesTemplate> techCompetences;
    private List<CompetencesTemplate> genCompetences;

    private float projectCompetenceWeight = 1; // peso asignado a las competencias por cada proyecto
    private boolean takeProjectRequirements = false; //si se tienen en cuenta los requisitos del proyecto
    private List<Competence> techCompetencesOptions; //para mostrar en la tabla
    private List<Competence> genCompetencesOptions;    //para mostrar en la tabla

    //    private float roleExperienceWeight = 0; //peso dado a la experiencia en el desempeño del rol
//    private boolean roleExperience = false; //si se tiene en cuenta la experiencia en el rol
//    private boolean noProject = false; //si se tiene en cuenta el numero de proyectos en que ha desempeñado el rol
//    private boolean evaluation = false; //si se tiene en cuenta la evaluacion en el desempeño del rol
    /////VARIABLES PARA LA PESTAÑA CARGA DE TRABAJO DEL ROL///////////////
    private boolean canBeAssigned = false; //Permitir o no que ya este asignado como jefe de proyecto

    private boolean takeCustomPersonWorkLoad = true; //definir la carga maxima que puede tener el trabajador
    private RoleLoad roleLoad; // Carga maxima que puede tener el trabajador

    ////////VARIABLES PARA LA PESTAÑA CARACTERÍSTICAS PSICOLÓGICAS//////////////////////////
    private boolean preferBelbin = false; // si prefiere los roles de belbin
    private boolean preferMyersBrigs = false; // si prefiere el subtipo E??J de Myers-Brigs

    ////////VARIABLES PARA LA PESTAÑA INTERES POR EL EQUIPO//////////////////////////
    private boolean bossTeamInterest = false; // el jefe de equipo este interesado en el equipo

    //////////////////////////////////////////////////////////////////////////////////
    private TreeNode bossProposal;
    private TreeNode selectedNode; //jefe de proyecto seleccionado

    private List<FixedWorker> fixedChiefsRemoveList;

    ///////////Manejar elementos de UI//////////////////////////
    private int activeIndex = 0; //tab activo

    /**
     * Manejar que pestaña debe estar activa luego de recargar todo el
     * formulario.
     *
     * @param event
     */
    public void onChange(TabChangeEvent event) {

        switch (event.getTab().getId()) {
            case "tabCompetences": {
                activeIndex = 0;
            }
            break;
            case "tabWorkLoad": {
                activeIndex = 1;
            }
            break;
            case "tabInterest": {
                activeIndex = 2;
            }
            break;
            case "tabCharacteristics": {
                activeIndex = 3;
            }
            break;
            case "tabCostdistance": {
                activeIndex = 4;
            }
            break;
            case "tabProjectInterest": {
                activeIndex = 5;
            }
            break;
            case "mbtiTab": {
                activeIndex = 6;
            }
            break;
        }
    }

    /**
     * activar/desactivar maximizar evaluacion en el rol y numero de veces que
     * lo ha desarrollado
     */
//    public void roleExperience_Listener() {
//        if (roleExperience == true) {
//            noProject = true;
//            evaluation = true;
//        } else {
//            noProject = false;
//            evaluation = false;
//        }
//    }
    public float higherCompetenceWeight() {
        float weight = 1;
        if (takeCompetences) {
            if (takeWorkLoad) {
                weight -= workLoadWeight;
            }
            if (takeCostDistance) {
                weight -= costDistanceWeight;
            }
            if (takeInterests) {
                weight -= interestWeight;
            }
        }
        return weight;
    }

    public float higherWorkLoadWeight() {
        float weight = 1;
        if (takeWorkLoad) {
            if (takeCompetences) {
                weight -= competenceWeight;
            }
            if (takeCostDistance) {
                weight -= costDistanceWeight;
            }
            if (takeInterests) {
                weight -= interestWeight;
            }
        }
        return weight;
    }

    public float higherInterestWeight() {
        float weight = 1;
        if (takeInterests) {
            if (takeCompetences) {
                weight -= competenceWeight;
            }
            if (takeCostDistance) {
                weight -= costDistanceWeight;
            }
            if (takeWorkLoad) {
                weight -= workLoadWeight;
            }

        }
        return weight;
    }

    public float higherProjectInterestWeight() {
        float weight = 1;
        if (takeProjectInterests) {
            if (takeMbti) {
                weight -= mbtiWeight;
            }
            if (takeCompetences) {
                weight -= competenceWeight;
            }
            if (takeCostDistance) {
                weight -= costDistanceWeight;
            }
            if (takeWorkLoad) {
                weight -= workLoadWeight;
            }
            if (takeInterests) {
                weight -= interestWeight;
            }
        }
        return weight;
    }

    public float higherMbtiWeight() {
        float weight = 1;
        if (takeMbti) {
            if (takeProjectInterests) {
                weight -= projectInterestWeight;
            }
            if (takeCompetences) {
                weight -= competenceWeight;
            }
            if (takeCostDistance) {
                weight -= costDistanceWeight;
            }
            if (takeWorkLoad) {
                weight -= workLoadWeight;
            }
            if (takeInterests) {
                weight -= interestWeight;
            }
        }
        return weight;
    }
//    public float higherPsycoCharacteristicstWeight() {
//        float weight = 0;
//        if (takeInterests) {
//            if (takeCompetences) {
//                weight = 1 - competenceWeight;
//            }
//            if (takeCostDistance) {
//                weight = 1 - costDistanceWeight;
//            }
//            if (takeWorkLoad) {
//                weight = 1 - workLoadWeight;
//            }
//        }
//        return weight;
//    }

    public float higherCostDistanceWeight() {
        float weight = 1;
        if (takeCostDistance) {
            if (takeCompetences) {
                weight -= competenceWeight;
            }
            if (takeInterests) {
                weight -= interestWeight;
            }
            if (takeWorkLoad) {
                weight -= workLoadWeight;
            }
        }
        return weight;
    }

    //    public float higherCompetenceProjectWeight() {
//        float weight = 1;
//        if (takeProjectRequirements) {
//            if (roleExperience) {
//                weight -= roleExperienceWeight;
//            }
////            projectCompetenceWeight = weight;
//        }
//        return weight;
//    }
//    public float higherCompetenceExperienceWeight() {
//        float weight = 1;
//        if (roleExperience) {
//            if (takeProjectRequirements) {
//                weight -= projectCompetenceWeight;
//            }
////            roleExperienceWeight = weight;
//        }
//        return weight;
//    }
    public boolean ensureGeneralWeightSummatory() {
        boolean isOk = true;

        float weight = 0;
        if (takeCostDistance) {
            weight += costDistanceWeight;
        }
        if (takeCompetences) {
            weight += competenceWeight;
        }
        if (takeInterests) {
            weight += interestWeight;
        }
        if (takeProjectInterests) {
            weight += projectInterestWeight;
        }
        if (takeWorkLoad) {
            weight += workLoadWeight;
        }
        if (takeMbti) {
            weight += mbtiWeight;
        }

        if (weight != 1 && !preferBelbin && !preferMyersBrigs) {
            isOk = false;
        }
        return isOk;
    }

    public boolean ensureCompetencesWeightSummatory() {
        boolean isOk = true;

        float weight = 0;
//        if (roleExperience) {
//            weight += roleExperienceWeight;
//        }
        if (takeProjectRequirements) {
            weight += projectCompetenceWeight;
        }

        if (weight != 1 && takeCompetences) {
            isOk = false;
        }
        return isOk;
    }

    /**
     * activar/desactivar maximizar competencias por proyecto y experiencia en
     * el rol
     */
//    public void takeCompetences_Listener() {
//        if (takeCompetences) {
//            takeProjectRequirements = true;
////            roleExperience = true;
////            noProject = true;
////            evaluation = true;
//        } else {
//            takeProjectRequirements = false;
////            roleExperience = false;
////            noProject = false;
////            evaluation = false;
//        }
//    }
    /**
     * activar/desactivar maximizar competencias si se desmarcan su dos
     * objetivos
     */
//    public void deactiveExperienceOrCompetence() {
//        takeCompetences = !(takeProjectRequirements == false ); //&& roleExperience == false
//    }
    //////////////////////////////////////////////////////////////////////////////////

    /**
     * Editar los datos de las competencias para un rol en un proyeccto
     * determinado
     */
    public void prepareProjectInCellEdit() {
        int i = 0;
        boolean find = false;
        if (selectedProject != null) {
            while (i < selectedProject.getRoleCompetences().size() && !find) {
                if (selectedProject.getRoleCompetences().get(i).getRole().isBoss()) {
                    find = true;
                    genCompetences = (selectedProject.getRoleCompetences().get(i).getGenCompetences());
                    techCompetences = (selectedProject.getRoleCompetences().get(i).getTechCompetences());
                } else {
                    i++;
                }
            }
        }
    }

    /**
     * Para obtener la propuesta de Jefes de proyecto que cumplen las
     * restricciones dadas
     */
    public void getBoss() {
        if (ensureGeneralWeightSummatory()) { //assureCompetencesWeightSummatory() && 
            TeamFormationParameters parameters = initializeParameters(); // capturar parametros de las GUI
            Role role = roleFacade.findBoss();
            List<ProjectBossProposal> proposal = obtainProposal(role, parameters);

            if (proposal.isEmpty()) {
                MyJSF_Util.addWarningMessage(localeConfig.getBundleValue("impossible_get_proposal"));
            } else {
                int i = 0;
                boolean found = false;
                while (i < proposal.size() && !found) {
                    if (proposal.get(i).getBossProposalList().isEmpty()) {
                        MyJSF_Util.addWarningMessage(localeConfig.getBundleValue("error_get_solution_foreach_project"));
                        found = true;
                    }
                    i++;
                }
                if (!found) {
                    MyJSF_Util.addSuccessMessage(localeConfig.getBundleValue("new_proposal_generated"));
                }
            }
            this.bossProposal = buildBossProposalTree(proposal);
        } else {
            if (!ensureGeneralWeightSummatory()) {
                MyJSF_Util.addWarningMessage(localeConfig.getBundleValue("factors_weights_most_be_one"));
            }
        }
    }

    /**
     * Para fijar una persona como jefe de proyecto
     */
    public void fixBoss() {
        FixedWorker fixed = new FixedWorker();

        Project project;
        Role role = roleFacade.findBoss();
        Worker selectedBoss;

        BossProposal selectedBossNode;
        if (role != null) {
            if (selectedNode.getData() instanceof BossProposal) {
                selectedBossNode = (BossProposal) selectedNode.getData();
                selectedBoss = selectedBossNode.getPerson();

                if (selectedNode.getParent().getData() instanceof Project) {
                    project = (Project) selectedNode.getParent().getData();

                    fixed.setBoss(selectedBoss);
                    fixed.setRole(role);
                    fixed.setProject(project);

                    if (parametersBoss.getFixedWorkers() == null) {
                        parametersBoss.setFixedWorkers(new ArrayList<>());
                    }
                    parametersBoss.getFixedWorkers().add(fixed);
                    getBoss(); //generar nueva propuesta para actualizar arbol

                }
            }
        }
    }

    /**
     * Desasignar un jefe de proyecto
     */
    public void removeBossFromTable_Listener() {
        for (FixedWorker fixedWorker : fixedChiefsRemoveList) {
            for (int i = 0; i < parametersBoss.getFixedWorkers().size(); i++) {
                if (fixedWorker.getBoss().getId().equals(parametersBoss.getFixedWorkers().get(i).getBoss().getId())) {
                    parametersBoss.getFixedWorkers().remove(i);
                }
            }
        }
        fixedChiefsRemoveList.clear();
        getBoss(); //generar nueva propuesta
    }

    /**
     * Para obtener la propuesta de personas que cumplen con las restricciones
     * impuestas para un rol dado
     *
     * @param role
     * @param parameters
     * @return
     */
    public List<ProjectBossProposal> obtainProposal(Role role, TeamFormationParameters parameters) {
        DecimalFormat formato = new DecimalFormat("#.00");

        ProjectRoleState initialSolution = TeamBuilder.getInitialVoidSolution(parameters); // construir una solucion inicial vacía (contendrá las personas que ya fueron asignadas a cada rol)

        List<ProjectBossProposal> projectBossProposal = new ArrayList<>();
        ProjectBossProposal proposal;

        List<Worker> groupList = parameters.getSearchArea();//personas disponibles para asignar.
        ArrayList<ObjetiveFunction> objectiveFunctions = new ArrayList<>();
        objectiveFunctions.addAll(ObjetiveFunctionUtil.getObjectiveFunctions(parameters)); //funciones objetivo seleccionadas en la UI
        List<Constrain> restrictions = new ArrayList<>();

        TeamFormationProblem problem = new TeamFormationProblem();
        //Inicialización y Asignación de las clases del problema
        problem.setParameters(parameters);
        problem.setState(initialSolution);
        problem.setMethod(new FactoresPonderados()); // factores ponderados para Jefe de Proyecto
        problem.setTypeSolutionMethod(TypeSolutionMethod.FactoresPonderados); // factores ponderados siempre para Jefe de Proyecto
        problem.setTypeProblem(Problem.ProblemType.Maximizar);//CAmbiado de Minimizar a Maximizar!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        problem.setFunction(objectiveFunctions); //lista definida anteriormente        
        restrictions.addAll(getRestrictions(parameters)); //lista de restricciones seleccionadas en la UI
        TeamFormationCodification codification = new TeamFormationCodification(restrictions, problem, groupList);
        problem.setCodification(codification);
        Strategy.getStrategy().setProblem(problem);
        List<Object> solutionProjects = initialSolution.getCode(); //obtener proyectos- roles de la solución (solución inicial vacía)

        //TRATAR DE ASIGNAR PERSONAS AL ROL (UN JEFE DE PROYECTO)
        boolean checkedRestrictions;
        for (Object solutionProject : solutionProjects) { //para cada proyecto-rol de la solución
            ProjectRole projectRole = (ProjectRole) solutionProject;
            List<RoleWorker> roleWorkers = projectRole.getRoleWorkers();  //obtener lista de roles-personas

            proposal = new ProjectBossProposal();
            proposal.setProject(projectRole.getProject());
            proposal.setBossProposalList(new ArrayList<>());

            for (RoleWorker roleWorker : roleWorkers) { //para cada rol-persona
                List<Worker> people = roleWorker.getWorkers(); //obtener lista de personas
                if (roleWorker.getRole().getId().equals(role.getId())) {

                    if (roleWorker.getNeededWorkers() > 0) {
                        for (Worker person : groupList) { //para cada persona del searchArea

                            people.clear();
                            people.add(person);

                            checkedRestrictions = codification.validState(initialSolution); //CHEQUEAR RESTRICIONES

                            if (checkedRestrictions) {
                                try {
                                    problem.Evaluate(initialSolution);
                                    float functionEvaluation = problem.getState().getEvaluation().get(0).floatValue();
                                    //proposal.getBossProposalList().add(new BossProposal(person, Float.valueOf(formatedEvaluation)));
                                    proposal.getBossProposalList().add(new BossProposal(person, functionEvaluation));

                                } catch (IllegalArgumentException | SecurityException | ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                                    Logger.getLogger(TF_StepTwoController.class
                                            .getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            people.clear();
                        }
                    }
                }
            }
            if (!proposal.getBossProposalList().isEmpty()) {
                projectBossProposal.add(proposal);
            }
        }
        return projectBossProposal;
    }

    /**
     * Construir el arbol con las propuestas de jefes de proyecto
     *
     * @param bossProposal
     * @return
     */
    public TreeNode buildBossProposalTree(List<ProjectBossProposal> bossProposal) {

        TreeNode root = new DefaultTreeNode();
        root.setExpanded(true);

        TreeNode project;
        TreeNode boss;

        if (bossProposal != null) {
            for (int i = 0; i < bossProposal.size(); i++) {
                project = new DefaultTreeNode(bossProposal.get(i).getProject());
                project.setType("P");
                project.setSelectable(false);
                project.setExpanded(true);
                project.setRowKey(String.valueOf(i));
                root.getChildren().add(project);

                for (BossProposal chief : bossProposal.get(i).getBossProposalList()) {
                    boss = new DefaultTreeNode(chief);
                    boss.setType("B");
                    boss.setExpanded(true);
                    boss.setRowKey(String.valueOf(i + 1));
                    project.getChildren().add(boss);
                }
            }
        }
        return root;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public float getCompetenceWeight() {
        return competenceWeight;
    }

    public void setCompetenceWeight(float competenceWeight) {
        this.competenceWeight = competenceWeight;
    }

    public List<FixedWorker> getFixedChiefsRemoveList() {
        return fixedChiefsRemoveList;
    }

    public void setFixedChiefsRemoveList(List<FixedWorker> fixedChiefsRemoveList) {
        this.fixedChiefsRemoveList = fixedChiefsRemoveList;
    }

    public float getWorkLoadWeight() {
        return workLoadWeight;
    }

    public void setWorkLoadWeight(float workLoadWeight) {
        this.workLoadWeight = workLoadWeight;
    }

    public float getCostDistanceWeight() {
        return costDistanceWeight;
    }

    public void setCostDistanceWeight(float costDistanceWeight) {
        this.costDistanceWeight = costDistanceWeight;
    }

    public float getInterestWeight() {
        return interestWeight;
    }

    public void setInterestWeight(float interestWeight) {
        this.interestWeight = interestWeight;
    }

    public float getProjectInterestWeight() {
        return projectInterestWeight;
    }

    public void setProjectInterestWeight(float projectInterestWeight) {
        this.projectInterestWeight = projectInterestWeight;
    }

    public boolean isTakeCompetences() {
        return takeCompetences;
    }

    public void setTakeCompetences(boolean takeCompetences) {
        this.takeCompetences = takeCompetences;
    }

    public boolean isTakeSynergy() {
        return takeSynergy;
    }

    public void setTakeSynergy(boolean takeSynergy) {
        this.takeSynergy = takeSynergy;
    }

    public float getSynergyWeight() {
        return synergyWeight;
    }

    public void setSynergyWeight(float synergyWeight) {
        this.synergyWeight = synergyWeight;
    }

    public TypeSolutionMethod getSolutionMethodOptions() {
        return solutionMethodOptions;
    }

    public void setSolutionMethodOptions(TypeSolutionMethod solutionMethodOptions) {
        this.solutionMethodOptions = solutionMethodOptions;
    }

    public boolean isTakeWorkLoad() {
        return takeWorkLoad;
    }

    public void setTakeWorkLoad(boolean takeWorkLoad) {
        this.takeWorkLoad = takeWorkLoad;
    }

    public boolean isTakeCostDistance() {
        return takeCostDistance;
    }

    public void setTakeCostDistance(boolean takeCostDistance) {
        this.takeCostDistance = takeCostDistance;
    }

    public boolean isTakeInterests() {
        return takeInterests;
    }

    public void setTakeInterests(boolean takeInterests) {
        this.takeInterests = takeInterests;
    }

    public boolean isTakeProjectInterests() {
        return takeProjectInterests;
    }

    public void setTakeProjectInterests(boolean takeProjectInterests) {
        this.takeProjectInterests = takeProjectInterests;
    }

    public List<Competence> getTechCompetencesOptions() {
        return techCompetencesOptions;
    }

    public void setTechCompetencesOptions(List<Competence> techCompetencesOptions) {
        this.techCompetencesOptions = techCompetencesOptions;
    }

    public List<Competence> getGenCompetencesOptions() {
        return genCompetencesOptions;
    }

    public void setGenCompetencesOptions(List<Competence> genCompetencesOptions) {
        this.genCompetencesOptions = genCompetencesOptions;
    }

    public boolean isCanBeAssigned() {
        return canBeAssigned;
    }

    public void setCanBeAssigned(boolean canBeAssigned) {
        this.canBeAssigned = canBeAssigned;
    }

    public RoleLoad getRoleLoad() {
        return roleLoad;
    }

    public void setRoleLoad(RoleLoad roleLoad) {
        this.roleLoad = roleLoad;
    }

    public boolean isPreferBelbin() {
        return preferBelbin;
    }

    public void setPreferBelbin(boolean preferBelbin) {
        this.preferBelbin = preferBelbin;
    }

    public boolean isPreferMyersBrigs() {
        return preferMyersBrigs;
    }

    public void setPreferMyersBrigs(boolean preferMyersBrigs) {
        this.preferMyersBrigs = preferMyersBrigs;
    }

    public float getProjectCompetenceWeight() {
        return projectCompetenceWeight;
    }

    public void setProjectCompetenceWeight(float projectCompetenceWeight) {
        this.projectCompetenceWeight = projectCompetenceWeight;
    }

    public boolean isTakeMbti() {
        return takeMbti;
    }

    public void setTakeMbti(boolean takeMbti) {
        this.takeMbti = takeMbti;
    }

    public float getMbtiWeight() {
        return mbtiWeight;
    }

    public void setMbtiWeight(float mbtiWeight) {
        this.mbtiWeight = mbtiWeight;
    }

    //    public float getRoleExperienceWeight() {
//        return roleExperienceWeight;
//    }
//
//    public void setRoleExperienceWeight(float roleExperienceWeight) {
//        this.roleExperienceWeight = roleExperienceWeight;
//    }
//
//    public boolean isRoleExperience() {
//        return roleExperience;
//    }
//
//    public void setRoleExperience(boolean roleExperience) {
//        this.roleExperience = roleExperience;
//    }
    public boolean isTakeCustomPersonWorkLoad() {
        return takeCustomPersonWorkLoad;
    }

    public void setTakeCustomPersonWorkLoad(boolean takeCustomPersonWorkLoad) {
        this.takeCustomPersonWorkLoad = takeCustomPersonWorkLoad;
    }

    //    public boolean isNoProject() {
//        return noProject;
//    }
//
//    public void setNoProject(boolean noProject) {
//        this.noProject = noProject;
//    }
//    public boolean isEvaluation() {
//        return evaluation;
//    }
//
//    public void setEvaluation(boolean evaluation) {
//        this.evaluation = evaluation;
//    }
    public boolean isTakeProjectRequirements() {
        return takeProjectRequirements;
    }

    public void setTakeProjectRequirements(boolean takeProjectRequirements) {
        this.takeProjectRequirements = takeProjectRequirements;
    }

    public List<ProjectRoleCompetenceTemplate> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectRoleCompetenceTemplate> projects) {
        this.projects = projects;
    }

    public ProjectRoleCompetenceTemplate getSelectedProject() {
        return selectedProject;
    }

    public void setSelectedProject(ProjectRoleCompetenceTemplate selectedProject) {
        this.selectedProject = selectedProject;
    }

    public List<CompetencesTemplate> getTechCompetences() {
        return techCompetences;
    }

    public void setTechCompetences(List<CompetencesTemplate> techCompetences) {
        this.techCompetences = techCompetences;
    }

    public List<CompetencesTemplate> getGenCompetences() {
        return genCompetences;
    }

    public void setGenCompetences(List<CompetencesTemplate> genCompetences) {
        this.genCompetences = genCompetences;
    }

    public TreeNode getBossProposal() {
        return bossProposal;
    }

    public void setBossProposal(TreeNode bossProposal) {
        this.bossProposal = bossProposal;
    }

    public TeamFormationParameters getParametersBoss() {
        return parametersBoss;
    }

    public void setParametersBoss(TeamFormationParameters parametersBoss) {
        this.parametersBoss = parametersBoss;
    }

    public int getActiveIndex() {
        return activeIndex;
    }

    public void setActiveIndex(int activeIndex) {
        this.activeIndex = activeIndex;
    }

    public boolean isBossTeamInterest() {
        return bossTeamInterest;
    }

    public void setBossTeamInterest(boolean bossTeamInterest) {
        this.bossTeamInterest = bossTeamInterest;
    }

    ///////////////////////////////////////////////////////////////////////////
    ////////////////FUNCIONES PATRA OBTENCION DE JEFES DE EQUIPOS//////////////
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Inicializa los parámetros del problema segun la seleccion del usuario en
     * la GUI
     *
     * @return
     */
    public TeamFormationParameters initializeParameters() {

        if (parametersBoss == null) {
            parametersBoss = new TeamFormationParameters();
        }

        //Funciones Objetivo
        parametersBoss.setMaxCompetences(takeCompetences); //max competences
        parametersBoss.setMaxCompetencesWeight(competenceWeight); //peso

        parametersBoss.setMaxCompetencesByProject(takeProjectRequirements); //max competences por proyecto
        parametersBoss.setMaxCompetencesByProjectWeight(projectCompetenceWeight); //peso

        parametersBoss.setMaxInterests(takeInterests); // maximizar intereces
        parametersBoss.setMaxInterestsWeight(interestWeight);//peso

        parametersBoss.setMinCostDistance(takeCostDistance); //max costo distancia
        parametersBoss.setMinCostDistanceWeight(costDistanceWeight);

        parametersBoss.setTakeWorkLoad(takeWorkLoad);//minimizar carga de trabajo personal
        parametersBoss.setWorkLoadWeight(workLoadWeight);//peso

        parametersBoss.setMinIncomp(takeSynergy);// minimizar incompatibilidades
        parametersBoss.setMinIncompWeight(synergyWeight);//peso

        parametersBoss.setMaxProjectInterests(takeProjectInterests); // maximizar intereces
        parametersBoss.setMaxProjectInterestsWeight(projectInterestWeight);//peso

        parametersBoss.setMaxMbtiTypes(takeMbti);
        parametersBoss.setMaxMbtiTypesWeight(mbtiWeight);

        //valores requeridos por la funcion objetivo max competence
        Levels maxCompetenceLevel = levelsFacade.getMaxLevel(); //maximo nivel de competencia declarado por la entidad
        Levels minCompetenceLevel = levelsFacade.getMinLevel(); //minimo nivel de competencia declarado por la entidad
//        RoleEval maxEvaluation = roleEvalFacade.getMaxLevel(); //maxima evaluacion definida para un rol
//      parametersBoss.setMaxEvaluation(maxEvaluation);
        parametersBoss.setMaxLevel(maxCompetenceLevel);
        parametersBoss.setMinLevel(minCompetenceLevel);
        CostDistance maxCostDistance = costDistanceFacade.getMaxCostDistance();
        parametersBoss.setMaxCostDistance(maxCostDistance);
        //RESTRICCIONES
        //sobre la carga de trabajo considerar
        parametersBoss.setCanBeProjectBoss(canBeAssigned); //una persona solo puede ser jefe de un projecto
        parametersBoss.setTakeCustomPersonWorkLoad(takeCustomPersonWorkLoad); //personalizar carga maxima de trabajo
        parametersBoss.setMaxRoleLoad(roleLoad);//carga maxima personalizada

        //sobre las caracteristicas psicologicas considerar
        parametersBoss.setPreferBelbin(preferBelbin);
        parametersBoss.setPreferMyersBrigs(preferMyersBrigs);

        //sobre los intereses considerar
        parametersBoss.setBossTeamInterest(bossTeamInterest);

        return parametersBoss;
    }

    /**
     * Obtener restricciones para jefe de proyecto
     *
     * @param parameters
     * @return
     */
    public List getRestrictions(TeamFormationParameters parameters) {
        List restrictions = new ArrayList();

        if (parameters != null) {

            restrictions.add(new WorkerNotRepeatedInSameRole());//obligatoria, evitar que se repita la misma persona en un rol
            restrictions.add(new WorkerRemovedFromRole()); //obligatoria, que la persona no este asignada a un ciclo anterior activo del proyecto

            if (parameters.isConfRole()) {
                restrictions.add(new MaximumRoles()); //Que una persona no pueda desempeÃ±ar mÃ¡s roles que los definidos en la interfaz
            }

            if (parameters.isOnlyOneProject()) { //una persona solo puede ser asignada a un proyecto
                restrictions.add(new DiferentPersonByProject());
            }

            if (parameters.isCanBeProjectBoss()) { //una persona solo puede ser jefe de un projecto
                restrictions.add(new IsProjectBoss());
            }

            if (parameters.isTakeCustomPersonWorkLoad()) { //la persona no puede tener una carga de trabajo superior a la definida
                if (parameters.getMaxRoleLoad() != null) {
                    restrictions.add(new MaxWorkload(parameters));
                }
            }
            if (parameters.isPreferBelbin()) { //debe tener preferencia por los roles de belbin impulsor o cordinador
                restrictions.add(new IsISCO());
            }
            if (parameters.isPreferMyersBrigs()) { //debe tener el subtipo ej de Myer-Briggs
                restrictions.add(new IsEJ());
            }

            if (parameters.isMaxCompetencesByProject()) {
                restrictions.add(new AllCompetitionLevels(parameters));
            }

            if (parameters.isBossTeamInterest()) {
                restrictions.add(new BossTeamInterest());
            }
        }
        return restrictions;
    }
}