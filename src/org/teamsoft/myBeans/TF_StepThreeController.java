/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.myBeans;

import evolutionary_algorithms.complement.CrossoverType;
import evolutionary_algorithms.complement.MutationType;
import evolutionary_algorithms.complement.ReplaceType;
import evolutionary_algorithms.complement.SelectionType;
import local_search.complement.StopExecute;
import local_search.complement.TabuSolutions;
import local_search.complement.UpdateParameter;
import metaheurictics.strategy.Strategy;
import metaheuristics.generators.*;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.teamsoft.POJOS.*;
import org.teamsoft.controller.AssignedRoleController;
import org.teamsoft.entity.*;
import org.teamsoft.locale.LocaleConfig;
import org.teamsoft.metaheuristics.operator.TeamBuilder;
import org.teamsoft.metaheuristics.operator.TeamFormationOperator;
import org.teamsoft.metaheuristics.restrictions.*;
import org.teamsoft.metaheuristics.util.*;
import org.teamsoft.model.*;
import problem.definition.ObjetiveFunction;
import problem.definition.Problem;
import problem.definition.State;
import problem.extension.TypeSolutionMethod;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author G1lb3rt
 */
@Named("tF_StepThreeController")
@SessionScoped
public class TF_StepThreeController implements Serializable {

    @Inject
    AssignedRoleController assignedRoleController;
    @Inject
    LevelsFacade levelsFacade;
    @Inject
    RoleFacade roleFacade;
    @Inject
    RoleEvalFacade roleEvalFacade;
    @Inject
    ConflictIndexFacade conflictIndexFacade;
    @Inject
    CostDistanceFacade costDistanceFacade;
    @Inject
    LocaleConfig localeConfig;

    private String solutionMethod = "PONDERAR"; // Metodo a usar seleccionado: (PONDERAR,PRIORIZAR,IGUALAR) los factores
    private int solutionWay = 1; //Algoritmo de solucion a emplear

    TeamFormationParameters parametersTeam;
    ///TOMAR EN CUENTA O NO ESTAS FUNCIONES OBJETIVO///////////////
    private boolean takeCompetences;
    private boolean takeWorkLoad = false;
    private boolean takeCostDistance = false;
    private boolean takeInterests = false;
    private boolean takeProjectInterests = false;
    private boolean takeSynergy = false;
    private boolean takeBelbin = false;
    private boolean takeMbtiTypes = false;
    private boolean takeMultiroleTeamMembers = false;

    ///BALANCEAR ESTAS  FUNCIONES OBJETIVO///////////////
    private boolean balanceCompetences = false;
    private boolean balanceWorkLoad = false;
    private boolean balanceCostDistance = false;
    private boolean balanceInterests = false;
    private boolean balanceProjectInterests = false;
    private boolean balanceMbtiTypes = false;
    private boolean balanceSynergy = false;
    private boolean balanceBelbin = false;
    private boolean balanceMultiroleTeamMembers = false;
    
    /////PESO PARA LAS  FUNCIONES OBJETIVO///////////////
    private float competenceWeight = 0;
    private float workLoadWeight = 0;
    private float costDistanceWeight = 0;
    private float interestWeight = 0;
    private float projectInterestWeight = 0;
    private float synergyWeight = 0;
    private float belbinWeight = 0;
    private float mbtiWeight = 0;
    private float multiRoleTeamMembersWeight = 0;

    private float balanceCompetenceWeight = 0;
    private float balanceWorkLoadWeight = 0;
    private float balanceSynergyWeight = 0;
    private float balanceCostDistanceWeight = 0;
    private float balanceInterestWeight = 0;
    private float balanceProjectInterestWeight = 0;
    private float balanceMbtiTypesWeight = 0;
    private float balanceBelbinWeight = 0;
    private float balanceMultiRoleTeamMembersWeight = 0;

    /////VARIABLES PARA LA PESTAÑA COMPETENCIAS DEL ROL///////////////
    private List<ProjectRoleCompetenceTemplate> projects; //lista de proyectos seleccionados en paso 1 del wizard
    private ProjectRoleCompetenceTemplate selectedProject; //proyecto seleccionado en el combo box para ajustar competencias
    private List<Role> selectableRoles = new ArrayList<>(); //roles del proyecto seleccionado en el combo box para ajustar competencias
    private Role selectedRol; //rol de proyecto seleccionado para ajustar competencias
    private List<CompetencesTemplate> techCompetences;
    private List<CompetencesTemplate> genCompetences;

    private float projectCompetenceWeight = 1; // peso asignado a las competencias por cada proyecto
    private boolean takeProjectRequirements = false; //si se tienen en cuenta los requisitos del proyecto
    private List<Competence> techCompetencesOptions; //para mostrar en la tabla
    private List<Competence> genCompetencesOptions;    //para mostrar en la tabla

    private float roleExperienceWeight = 0; //peso dado a la experiencia en el desempeño del rol
    private boolean roleExperience = false; //si se tiene en cuenta la experiencia en el rol
    private boolean noProject = false; //si se tiene en cuenta el numero de proyectos en que ha desempeñado el rol
    private boolean evaluation = false; //si se tiene en cuenta la evaluacion en el desempeño del rol

    /////VARIABLES PARA LA PESTAÑA CARGA DE TRABAJO DEL ROL///////////////
    private boolean canBeAssigned = false; //Permitir o no que ya este asignado como jefe de proyecto

    private boolean takeCustomPersonWorkLoad = true; //definir la carga maxima que puede tener el trabajador
    private RoleLoad roleLoad;// Carga maxima que puede tener el trabajador

    ////////////////////////////////TAB SINERGY///////////////////////////////////////////
    private String aboutIncompatibilityes = "none"; //no permitir ninguna incompatibilidad;
    private boolean anyIncompatibility = false; //no permitir ninguna incompatibilidad;
    private boolean anySelectedIncompatibility = false; //no permitir ninguna incompatibilidad seleccionada;

    private Worker personA;
    private Worker personB; //incompatibilidades de la personaA
    List<SelectedWorkerIncompatibility> incompList; //lista incompatibilidades
    List<SelectedWorkerIncompatibility> incompDelete; //lista incompatibilidades seleccionadas en el componente
    private Role roleA;
    private Role roleB; //roles para incompatibilidades
    List<SelectedRoleIncompatibility> incompRoleList; //lista incompatibilidades
    List<SelectedRoleIncompatibility> incompRoleDelete; //lista incompatibilidades seleccionadas en el componente

    ////////////////////////////TAB CARACTERISTICAS PSYCOLOGICAS////////////////////////////
    private boolean allBelbin = false; ///exigir la precencia de todos los roles de Belbin
    private boolean demandNBrains = false;//al menis nBrains personas con el rol cerebro
    private int countBrains = 0;//cant nBrains personas con el rol cerebro
    private boolean balanceBelbinCategories = false; //balancear categorias de roles de belbin
    private boolean allBelbinCategories = false; //precensia de todas las categorias de Belbin
    private String actionMental = ">";
    private String mentalSocial = ">";

    private TreeNode teamProposal;
    private TreeNode selectedNode; //jefe de proyecto seleccionado
    ///////////Manejar elementos de UI//////////////////////////

    private boolean mostrarPesos = true;
    private int activeIndex = 0; //tab activo
    
    private List<FixedWorker> fixedWorkersList;
    

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
            case "workersTab": {
                activeIndex = 1;
            }
            break;
            case "synergyTab": {
                activeIndex = 2;
            }
            break;
            case "cdistanceTab": {
                activeIndex = 3;
            }
            break;
            case "interestTab": {
                activeIndex = 4;
            }
            break;
            case "psychoTab": {
                activeIndex = 5;
            }
            break;
            case "projectInterestTab": {
                activeIndex = 6;
            }
            break;
            case "mbtiTab": {
                activeIndex = 7;
            }
            break;
            case "multiRoleTeamMembersTab":{
                activeIndex = 8;
            }
        }
    }

    /**
     * Habilitar el check de balancear solo si se selecciona mas de un equipo.
     */
    public boolean haveAnySenseToBalanceObjetiveFunction() {
        boolean haveSense = false;

        if (parametersTeam.getProjects() != null) {
            if (parametersTeam.getProjects().size() > 1) {
                haveSense = true;
            }
        }
        return haveSense;
    }

    /**
     * Asegurar que la sumatoria del peso de las funciones objetivo sea 1.
     *
     * @return
     */
    public boolean ensureGeneralWeightSummatory() {
        boolean isOk = true;

        float weight = 0;
        if (balanceCompetences) {
            weight += balanceCompetenceWeight;
        }
        if (balanceWorkLoad) {
            weight += balanceWorkLoadWeight;
        }
        if (balanceCostDistance) {
            weight += balanceCostDistanceWeight;
        }
        if (balanceInterests) {
            weight += balanceInterestWeight;
        }
        if (balanceProjectInterests) {
            weight += balanceProjectInterestWeight;
        }
        if (balanceMbtiTypes) {
            weight += balanceMbtiTypesWeight;
        }
        if (balanceSynergy) {
            weight += balanceSynergyWeight;
        }
        if (balanceBelbin) {
            weight += balanceBelbinWeight;
        }
        if(balanceMultiroleTeamMembers){
            weight += balanceMultiRoleTeamMembersWeight;
        }

        if (weight != 1) {
            isOk = false;
        }
        return isOk;
    }

    /**
     * Mostrar/Ocultar pesos de las FO.
     */
    public void hideWeights() {
        if (!solutionMethod.equalsIgnoreCase("PONDERAR")) {
            mostrarPesos = false;
        } else {
            mostrarPesos = true;
        }
    }

    /**
     * activar/desactivar maximizar evaluacion en el rol y numero de veces que
     * lo ha desarrollado
     */
    public void roleExperience_Listener() {
        if (roleExperience) {
            noProject = true;
            evaluation = true;
        } else {
            noProject = false;
            evaluation = false;
        }
    }

    /**
     * Obtener los roles del proyecto seleccionado para editar competencias.
     */
    public void selectableRolesForProyect() {
        selectableRoles = new ArrayList<>();
        techCompetences = new ArrayList<>();
        genCompetences = new ArrayList<>();
        selectedRol = null;
        if (selectedProject != null) {
            if (!selectedProject.getRoleCompetences().isEmpty()) {
                for (ProjectCompetenceTemplate roleCompetence : selectedProject.getRoleCompetences()) {
                    selectableRoles.add(roleCompetence.getRole());
                }
            }
        }
    }

    /**
     * Editar los datos de las competencias para un rol en un proyeccto
     * determinado
     */
    public void prepareProjectInCellEdit() {

        int i = 0;
        boolean find = false;
        if (selectedProject != null) {
            while (i < selectedProject.getRoleCompetences().size() && !find) {
                if (selectedProject.getRoleCompetences().get(i).getRole().getId().equals(selectedRol.getId())) {
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
     * activar/desactivar maximizar competencias por proyecto y experiencia en
     * el rol
     */
    public void takeCompetences_Listener() {
        if (!takeCompetences) {
            balanceCompetences = false;
        }
    }

    public void takeWorkLoad_Listener() {
        if (!takeWorkLoad) {
            balanceWorkLoad = false;
        }
    }

    public void takeSynergy_Listener() {
        if (!takeSynergy) {
            balanceSynergy = false;
        }
    }

    public void takeCostdostance_Listener() {
        if (!takeCostDistance) {
            balanceCostDistance = false;
        }
    }

    public void takeInterest_Listener() {
        if (!takeInterests) {
            balanceInterests = false;
        }
    }

    public void takeProjectInterest_Listener() {
        if (!takeProjectInterests) {
            balanceProjectInterests = false;
        }
    }

    public void takeMbtiTypes_Listener() {
        if (!takeMbtiTypes) {
            balanceMbtiTypes = false;
        }
    }

    public void takeBelbin_Listener() {
        if (!takeBelbin) {
            balanceBelbin = false;
        }
    }
    
    public void takeMultiroleTeamMembers_Listener(){
        if(!takeMultiroleTeamMembers){
            balanceMultiroleTeamMembers = false;
        }
    }

    /**
     * activar/desactivar maximizar competencias si se desmarcan su dos
     * objetivos
     */
//    public void deactiveExperienceOrCompetence() {
//        takeCompetences = !(takeProjectRequirements == false && roleExperience == false);
//    }
    //////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////

    /**
     * Inicializa los parámetros del prblema segun la selección del usuario en
     * la GUI
     *
     * @return
     */
    public TeamFormationParameters initializeParameters() {
        if (parametersTeam == null) {
            parametersTeam = new TeamFormationParameters();
        }

        //metodo de solucion
        switch (solutionMethod) {
            case "PONDERAR": {
                parametersTeam.setSolutionMethodOptionTeam(TypeSolutionMethod.FactoresPonderados);
                parametersTeam.setSolutionAlgorithm(solutionWay);
            }
            break;
            case "PRIORIZAR": {
                parametersTeam.setSolutionMethodOptionTeam(TypeSolutionMethod.FactoresPonderados);
                parametersTeam.setSolutionAlgorithm(solutionWay);
            }
            break;
            case "IGUALAR": {
                parametersTeam.setSolutionMethodOptionTeam(TypeSolutionMethod.MultiObjetivoPuro);
                parametersTeam.setSolutionAlgorithm(solutionWay);
            }
            break;
            default: {
                parametersTeam.setSolutionMethodOptionTeam(TypeSolutionMethod.FactoresPonderados);
                parametersTeam.setSolutionAlgorithm(solutionWay);
            }
            break;
        }

        //Funciones Objetivo
        parametersTeam.setMaxCompetences(takeCompetences); //max competences
        parametersTeam.setMaxCompetencesWeight(competenceWeight); //peso
        parametersTeam.setBalanceCompetences(balanceCompetences); //balancear competencias
        parametersTeam.setBalanceCompetenceWeight(balanceCompetenceWeight); //peso

        parametersTeam.setMaxCompetencesByProject(takeProjectRequirements); //max competences por proyecto
        parametersTeam.setMaxCompetencesByProjectWeight(projectCompetenceWeight); //peso

        parametersTeam.setMaxInterests(takeInterests); // maximizar intereces
        parametersTeam.setMaxInterestsWeight(interestWeight);//peso
        parametersTeam.setBalanceInterests(balanceInterests); //balancear intereces
        parametersTeam.setBalanceInterestWeight(balanceInterestWeight);//peso

        parametersTeam.setMaxProjectInterests(takeProjectInterests); // maximizar intereses rafiki
        parametersTeam.setMaxProjectInterestsWeight(projectInterestWeight);//peso rafiki
        parametersTeam.setBalanceProjectInterests(balanceProjectInterests); //balancear intereses por el rol
        parametersTeam.setBalanceProjectInterestWeight(balanceProjectInterestWeight);//balancear intereses por el equipo

        parametersTeam.setMaxMbtiTypes(takeMbtiTypes);
        parametersTeam.setMaxMbtiTypesWeight(mbtiWeight);
        parametersTeam.setBalanceMbtiTypes(balanceMbtiTypes);//balancear tipos mbti
        parametersTeam.setBalanceMbtiTypesWeight(balanceMbtiTypesWeight);//balancear tipos mbti

        parametersTeam.setMaxBelbinRoles(takeBelbin);// maximizar roles de belbin
        parametersTeam.setMaxBelbinWeight(belbinWeight);
        parametersTeam.setBalanceBelbinRoles(balanceBelbin); //balancear roles de belbin 
        parametersTeam.setBalanceBelbinWeight(balanceBelbinWeight);

        parametersTeam.setMinIncomp(takeSynergy);// minimizar incompatibilidades
        parametersTeam.setMinIncompWeight(synergyWeight);//peso
        parametersTeam.setBalanceSynergy(balanceSynergy);//balancear incompatibilidades
        parametersTeam.setBalanceSynergyWeight(balanceSynergyWeight);//peso

        parametersTeam.setMinCostDistance(takeCostDistance);// minimizar costo por distancia
        parametersTeam.setMinCostDistanceWeight(costDistanceWeight);//peso
        parametersTeam.setBalanceCostDistance(balanceCostDistance);//peso
        parametersTeam.setBalanceCostDistanceWeight(balanceCostDistanceWeight);//peso

        parametersTeam.setTakeWorkLoad(takeWorkLoad);//minimizar carga de trabajo personal
        parametersTeam.setWorkLoadWeight(workLoadWeight);//peso
        parametersTeam.setBalancePersonWorkload(balanceWorkLoad);//minimizar carga de trabajo personal
        parametersTeam.setBalanceWorkLoadWeight(balanceWorkLoadWeight);//peso
        
        parametersTeam.setMaxMultiroleTeamMembers(takeMultiroleTeamMembers); // maximizar equipos con personas multirol
        parametersTeam.setMaxMultiroleTeamMembersWeight(multiRoleTeamMembersWeight); // peso
        parametersTeam.setBalanceMultiroleTeamMembers(balanceMultiroleTeamMembers); //balancear equipos con personas multirol
        parametersTeam.setBalanceMultiroleTeamMembersWeight(balanceMultiRoleTeamMembersWeight); // peso

        //valores requeridos por la funcion objetivo max competence
        Levels maxCompetenceLevel = levelsFacade.getMaxLevel(); //maximo nivel de competencia declarado por la entidad
        Levels minCompetenceLevel = levelsFacade.getMinLevel(); //minimo nivel de competencia declarado por la entidad
        RoleEval maxEvaluation = roleEvalFacade.getMaxLevel(); //maxima evaluacion definida para un rol
        ConflictIndex maxConflict = conflictIndexFacade.getMaxConflict();
        CostDistance maxCostDistance = costDistanceFacade.getMaxCostDistance();

        parametersTeam.setMaxLevel(maxCompetenceLevel);
        parametersTeam.setMinLevel(minCompetenceLevel);
        parametersTeam.setMaxConflictIndex(maxConflict);
        parametersTeam.setMaxCostDistance(maxCostDistance);

        //RESTRICCIONES
        //sobre la carga de trabajo considerar
        parametersTeam.setCanBeProjectBoss(canBeAssigned); //una persona solo puede ser jefe de un projecto
        parametersTeam.setTakeCustomPersonWorkLoad(takeCustomPersonWorkLoad); //personalizar carga maxima de trabajo
        parametersTeam.setMaxRoleLoad(roleLoad);//carga maxima personalizada

        //sobre la sinergia
        parametersTeam.setAnyIncompatibility(anyIncompatibility);//no permitir incompatibilidades.
        parametersTeam.setAnySelectedIncompatibility(anySelectedIncompatibility);//no permitir incompatibilidades seleccionadas.

        //sobre las caracteristicas psicologicas considerar
        parametersTeam.setAllBelbinRoles(allBelbin); //exigir precensia de todos los roles de belbin
        parametersTeam.setDemandNBrains(demandNBrains); //demndar una cantidad de persona con rol cerebro por equipo
        parametersTeam.setCountBrains(countBrains); // cantidad de personas con rol cerebro demandada
        parametersTeam.setBalanceBelbinCategories(balanceBelbinCategories); //balancear categorias de belbin
        parametersTeam.setActionMentalOper(actionMental); //balancear categorias de belbin
        parametersTeam.setMentalSocialOper(mentalSocial); //balancear categorias de belbin

        parametersTeam.setAllBelbinCategories(allBelbinCategories); //exigir precensia de todas las categorias de roles de belbin

        return parametersTeam;

    }

    /**
     * Construir equipos (mejorando solución con la aplicacón metaheurísticas)
     */
    public void getTeam() throws Exception {

        if (true) { //ensureGeneralWeightSummatory()
            TeamFormationParameters parameters = initializeParameters(); //obtener parametros a considerar

            ProjectRoleState initialVoidSolution = TeamBuilder.getInitialVoidSolution(parameters); // construir una solucion inicial vacía (contendrá las personas que ya fueron asignadas a cada rol)

            //Funciones objetivo
            ArrayList<ObjetiveFunction> objectiveFunctions = new ArrayList<>();
            objectiveFunctions.addAll(ObjetiveFunctionUtil.getObjectiveFunctions(parameters));
            //Restricciones
            ArrayList<Constrain> restrictions = new ArrayList<>();
            restrictions.addAll(getRestrictions(parameters));
            //Operador
            TeamFormationOperator operator = new TeamFormationOperator();
            //Problema
            TeamFormationProblem problem = new TeamFormationProblem();

            problem.setOperator(operator);
            problem.setState(initialVoidSolution);
            problem.setTypeSolutionMethod(parameters.getSolutionMethodOptionTeam());
            problem.setTypeProblem(Problem.ProblemType.Maximizar); //Cambiado de Maximizar a Minimizar
            problem.setFunction(objectiveFunctions);
            problem.setParameters(parameters);

            ArrayList<Worker> searchArea = parameters.getSearchArea();
            TeamFormationCodification codification = new TeamFormationCodification(restrictions, problem, searchArea); //Inicializar parametros de la codificacion

            problem.setState(initialVoidSolution);
            problem.setCodification(codification);

            switch (parameters.getSolutionMethodOptionTeam()) { //Seleccion de algoritmo

                case FactoresPonderados: {

                    switch (parameters.getSolutionAlgorithm()) {
                        case 1:
                            teamProposal = buildTeamProposalTree(applyHillClimbing(initialVoidSolution, problem));
                            if (teamProposal.getChildCount() < 1) {
                                MyJSF_Util.addWarningMessage(localeConfig.getBundleValue("impossible_get_proposal"));
                            }
                            break;
                        case 2:
                            teamProposal = buildTeamProposalTree(applyHillClimbingRestart(initialVoidSolution, problem));
                            if (teamProposal.getChildCount() < 1) {
                                MyJSF_Util.addWarningMessage(localeConfig.getBundleValue("impossible_get_proposal"));
                            }
                            break;
                        case 3:
                            teamProposal = buildTeamProposalTree(applyRandomSearch(initialVoidSolution, problem));
                            if (teamProposal.getChildCount() < 1) {
                                MyJSF_Util.addWarningMessage(localeConfig.getBundleValue("impossible_get_proposal"));
                            }
                            break;
                        case 4:
                            teamProposal = buildTeamProposalTree(applyTabuSearch(initialVoidSolution, problem));
                            if (teamProposal.getChildCount() < 1) {
                                MyJSF_Util.addWarningMessage(localeConfig.getBundleValue("impossible_get_proposal"));
                            }
                            break;
                        case 5:
                            teamProposal = buildTeamProposalTree(applyAlgorithmsBriefcase(initialVoidSolution, problem));
                            if (teamProposal.getChildCount() < 1) {
                                MyJSF_Util.addWarningMessage(localeConfig.getBundleValue("impossible_get_proposal"));
                            }
                            break;
                        case 6:
                            teamProposal = buildTeamProposalTree(GA(initialVoidSolution, problem));
                            if (teamProposal.getChildCount() < 1) {
                                MyJSF_Util.addWarningMessage(localeConfig.getBundleValue("impossible_get_proposal"));
                            }
                            break;
                        case 7:
                            teamProposal = buildTeamProposalTree(applySimulatedAnnealing(initialVoidSolution, problem));
                            if (teamProposal.getChildCount() < 1) {
                                MyJSF_Util.addWarningMessage(localeConfig.getBundleValue("impossible_get_proposal"));
                            }
                            break;
                    }
                }
                break;

                case MultiObjetivoPuro: {

                    switch (parameters.getSolutionAlgorithm()) {

                        case 1:
                            teamProposal = buildTeamProposalTree(applyMultiobjectiveStochasticHC(initialVoidSolution, problem));
                            if (teamProposal.getChildCount() < 1) {
                                MyJSF_Util.addWarningMessage(localeConfig.getBundleValue("impossible_get_proposal"));
                            }
                            break;

                        case 2:
                            teamProposal = buildTeamProposalTree(applyMultiobjectiveHCRestart(initialVoidSolution, problem));
                            if (teamProposal.getChildCount() < 1) {
                                MyJSF_Util.addWarningMessage(localeConfig.getBundleValue("impossible_get_proposal"));
                            }
                            break;

                        case 3:
                            teamProposal = buildTeamProposalTree(applyMultiobjectiveHCDistance(initialVoidSolution, problem));
                            if (teamProposal.getChildCount() < 1) {
                                MyJSF_Util.addWarningMessage(localeConfig.getBundleValue("impossible_get_proposal"));
                            }
                            break;

                        case 4:
                            teamProposal = buildTeamProposalTree(applyMultiobjectiveTabuSearch(initialVoidSolution, problem));
                            if (teamProposal.getChildCount() < 1) {
                                MyJSF_Util.addWarningMessage(localeConfig.getBundleValue("impossible_get_proposal"));
                            }
                            break;

                        case 5:
                            teamProposal = buildTeamProposalTree(applyMultiobjectiveAlgorithmsBriefcase(initialVoidSolution, problem));
                            if (teamProposal.getChildCount() < 1) {
                                MyJSF_Util.addWarningMessage(localeConfig.getBundleValue("impossible_get_proposal"));
                            }
                            break;
                        case 6:
                            teamProposal = buildTeamProposalTree(applyMOGA(initialVoidSolution, problem));
                            if (teamProposal.getChildCount() < 1) {
                                MyJSF_Util.addWarningMessage(localeConfig.getBundleValue("impossible_get_proposal"));
                            }
                            break;
                        case 7:
                            teamProposal = buildTeamProposalTree(applyUMOSA(initialVoidSolution, problem));
                            if (teamProposal.getChildCount() < 1) {
                                MyJSF_Util.addWarningMessage(localeConfig.getBundleValue("impossible_get_proposal"));
                            }
                            break;
                        case 8:
                            teamProposal = buildTeamProposalTree(applyMCMOSA(initialVoidSolution, problem));
                            if (teamProposal.getChildCount() < 1) {
                                MyJSF_Util.addWarningMessage(localeConfig.getBundleValue("impossible_get_proposal"));
                            }
                            break;
                        case 9:
                            teamProposal = buildTeamProposalTree(applyNSGAII(initialVoidSolution, problem));
                            if (teamProposal.getChildCount() < 1) {
                                MyJSF_Util.addWarningMessage(localeConfig.getBundleValue("impossible_get_proposal"));
                            }
                            break;
                    }
                }
                break;
            }
        } else {
            MyJSF_Util.addWarningMessage(localeConfig.getBundleValue("fo_weights_most_sum_one"));
        }
    }

    public void configStrategy(TeamFormationProblem problem) {
        // Configurando elementos comunes de la estrategia
        Strategy.getStrategy().setStopexecute(new StopExecute());
        Strategy.getStrategy().setUpdateparameter(new UpdateParameter());
        Strategy.getStrategy().setProblem(problem);
        Strategy.getStrategy().calculateTime = Boolean.parseBoolean(ResourceBundle.getBundle("/algorithmConf").getString("calculateTime"));
    }

    public State prepareSolution() {
        List<State> states = Strategy.getStrategy().listStates;
        State bestState = null;
        int i = 0;
        boolean found = false;

        while (i < states.size() && !found) {
            if (Strategy.getStrategy().getProblem().getCodification().validState(states.get(i))) {
                found = true;
                bestState = states.get(i);
            }
            i++;
        }

        while (i < states.size()) {
            if (Strategy.getStrategy().getProblem().getCodification().validState(states.get(i))) {
                if (states.get(i).getEvaluation().get(0) > bestState.getEvaluation().get(0)) {
                    bestState = states.get(i);
                }
            }
            i++;
        }
        return bestState;
    }

    /**
     * Mejorar solucion aplicando el metodo HillClimbing
     *
     * @param initialSolution
     * @param problem
     * @return
     * @throws java.io.IOException
     */
    public List<State> applyHillClimbing(ProjectRoleState initialSolution, TeamFormationProblem problem) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {

        List<State> sol = null;
        if (initialSolution != null) {
            sol = new ArrayList<>();

            configStrategy(problem);
            Strategy.getStrategy().saveListBestStates = true;
            Strategy.getStrategy().saveListStates = true;
            Strategy.getStrategy().saveFreneParetoMonoObjetivo = true;
            int iterations = Integer.parseInt(ResourceBundle.getBundle("/algorithmConf").getString("iterations"));

            Strategy.getStrategy().executeStrategy(iterations, 1, GeneratorType.HillClimbing);
            State bestState = Strategy.getStrategy().getBestState();

            if (Strategy.getStrategy().getProblem().getCodification().validState(bestState)) {
                sol.add(bestState);
                SaveTxt.writeStatesMono();
            } else {
                bestState = prepareSolution();
                if (bestState != null) {
                    sol.add(bestState);
                    SaveTxt.writeStatesMono();
                }
            }
            Strategy.destroyExecute();
        }
        return sol;
    }

    /**
     * Mejorar solucion aplicando el metodo HillClimbing
     *
     * @param initialSolution
     * @param problem
     * @return
     * @throws java.io.IOException
     */
    public List<State> applyHillClimbingRestart(ProjectRoleState initialSolution, TeamFormationProblem problem) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {

        List<State> sol = null;
        if (initialSolution != null) {
            sol = new ArrayList<>();

            configStrategy(problem);
            Strategy.getStrategy().saveListBestStates = true;
            Strategy.getStrategy().saveListStates = true;
            HillClimbingRestart.count = Integer.parseInt(ResourceBundle.getBundle("/algorithmConf").getString("HillClimbingRestartCount"));
            int iterations = Integer.parseInt(ResourceBundle.getBundle("/algorithmConf").getString("iterations"));

            Strategy.getStrategy().executeStrategy(iterations, 1, GeneratorType.HillClimbingRestart);
            State bestState = Strategy.getStrategy().getBestState();

            if (Strategy.getStrategy().getProblem().getCodification().validState(bestState)) {
                sol.add(bestState);
                SaveTxt.writeStatesMono();
            } else {
                bestState = prepareSolution();
                if (bestState != null) {
                    sol.add(bestState);
                    SaveTxt.writeStatesMono();
                }
            }
            Strategy.destroyExecute();
        }
        return sol;
    }

    /**
     * Mejorar solucion aplicando el metodo RandomSearch
     *
     * @param initialSolution
     * @param problem
     * @return
     * @throws java.io.IOException
     */
    public List<State> applyRandomSearch(ProjectRoleState initialSolution, TeamFormationProblem problem) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {

        List<State> sol = null;
        if (initialSolution != null) {
            sol = new ArrayList<>();

            configStrategy(problem);
            Strategy.getStrategy().saveListBestStates = true;
            Strategy.getStrategy().saveListStates = true;
            int iterations = Integer.parseInt(ResourceBundle.getBundle("/algorithmConf").getString("iterations"));

            Strategy.getStrategy().executeStrategy(iterations, 1, GeneratorType.RandomSearch);
            State bestState = Strategy.getStrategy().getBestState();

            if (Strategy.getStrategy().getProblem().getCodification().validState(bestState)) {
                sol.add(bestState);
                SaveTxt.writeStatesMono();
            } else {
                bestState = prepareSolution();
                if (bestState != null) {
                    sol.add(bestState);
                    SaveTxt.writeStatesMono();
                }
            }
            Strategy.destroyExecute();
        }
        return sol;
    }

    /**
     * Mejorar solucion aplicando el metodo TabuSearch
     *
     * @param initialSolution
     * @param problem
     * @return
     * @throws java.io.IOException
     */
    public List<State> applyTabuSearch(ProjectRoleState initialSolution, TeamFormationProblem problem) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {

        List<State> sol = null;
        if (initialSolution != null) {
            sol = new ArrayList<>();

            configStrategy(problem);
            Strategy.getStrategy().saveListBestStates = true;
            Strategy.getStrategy().saveListStates = true;
            TabuSolutions.maxelements = Integer.parseInt(ResourceBundle.getBundle("/algorithmConf").getString("TabuSolutionsMaxelements"));
            int iterations = Integer.parseInt(ResourceBundle.getBundle("/algorithmConf").getString("iterations"));

            Strategy.getStrategy().executeStrategy(iterations, 1, GeneratorType.TabuSearch);
            State bestState = Strategy.getStrategy().getBestState();

            if (Strategy.getStrategy().getProblem().getCodification().validState(bestState)) {
                sol.add(bestState);
                SaveTxt.writeStatesMono();
            } else {
                bestState = prepareSolution();
                if (bestState != null) {
                    sol.add(bestState);
                    SaveTxt.writeStatesMono();
                }
            }
            Strategy.destroyExecute();
        }
        return sol;
    }

    /**
     * Mejorar solucion aplicando el portafolio de algoritmos
     *
     * @param initialSolution
     * @param problem
     * @return
     * @throws java.io.IOException
     */
    public List<State> applyAlgorithmsBriefcase(ProjectRoleState initialSolution, TeamFormationProblem problem) throws IOException, IllegalArgumentException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, SecurityException, InstantiationException, InvocationTargetException {

        List<State> sol = null;
        if (initialSolution != null) {

            int iterations = Integer.parseInt(ResourceBundle.getBundle("/algorithmConf").getString("iterations"));
            int executions = Integer.parseInt(ResourceBundle.getBundle("/algorithmConf").getString("executions"));
            for (int i = 0; i < executions; i++) {
                //RS,RE,RL,EE,GA,BT,BA,ECR,EDA,EC,LU
                configurePA(iterations, false, false, false, false, false, true, true, true, false, true, false);
                Strategy.getStrategy().setStopexecute(new StopExecute());
                Strategy.getStrategy().setUpdateparameter(new UpdateParameter());
                Strategy.getStrategy().setProblem(problem);
                Strategy.getStrategy().saveListBestStates = true;
                Strategy.getStrategy().calculateTime = true;
                Strategy.getStrategy().saveListStates = true;
                try {
                    Strategy.getStrategy().executeStrategy(iterations, 1, GeneratorType.MultiGenerator);
                } catch (IllegalArgumentException | SecurityException | ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                    Logger.getLogger(TF_StepThreeController.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
            Strategy.destroyExecute();
        }
        return sol;
    }

    private List<State> applyNSGAII(ProjectRoleState initialSolution, TeamFormationProblem problem) {
        int ITERATIONS = Integer.parseInt(ResourceBundle.getBundle("/algorithmConf").getString("iterations"));
        int EXECUTIONS = Integer.parseInt(ResourceBundle.getBundle("/algorithmConf").getString("executions"));
        List<State> sol = null;
        if (initialSolution != null) {
            sol = new ArrayList<>();
            for (int i = 0; i < EXECUTIONS; i++) {
                configStrategy(problem);
                //Configurar el algoritmo dentro del BiCIAM
                Strategy.getStrategy().setStopexecute(new StopExecute());
                Strategy.getStrategy().setUpdateparameter(new UpdateParameter());
                NSGAII.countRef = Integer.parseInt(ResourceBundle.getBundle("/algorithmConf").getString("CountRef")); //Cantidad de individuos
                NSGAII.selectionType = SelectionType.TournamentSelection;

                NSGAII.crossoverType = CrossoverType.GenericCrossover;
                NSGAII.mutationType = MutationType.GenericMutation;
                Strategy.getStrategy().calculateTime = true;
                NSGAII.PM = Float.parseFloat(ResourceBundle.getBundle("/algorithmConf").getString("PM"));
                NSGAII.PC = Float.parseFloat(ResourceBundle.getBundle("/algorithmConf").getString("PC"));

                try {
                    Strategy.getStrategy().executeStrategy(ITERATIONS, 1, GeneratorType.RandomSearch);
                } catch (IllegalArgumentException | SecurityException | ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                    Logger.getLogger(TF_StepThreeController.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
                List<State> noDominadas = Strategy.getStrategy().listRefPoblacFinal;
                State bestState = Strategy.getStrategy().getBestState();

                //Mostrando las soluciones no dominadas y guardando soluciones en el fichero de texto
                for (State noDominada : noDominadas) {
                    if (Strategy.getStrategy().getProblem().getCodification().validState(noDominada)) {
                        sol.add(noDominada);
                    }
                }

                if (sol.isEmpty()) {
                    if (Strategy.getStrategy().getProblem().getCodification().validState(bestState)) {
                        sol.add(bestState);
                    }
                }

                if (!sol.isEmpty()) {
                    long time = System.currentTimeMillis() / 1000;
                    String txt = "/multi NSGAII teams " + time + ".txt";
                    ServletContext relativePath = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
                    String mainPath = relativePath.getRealPath("/reports/");
                    SaveTxt.writeFileTxtMulti(mainPath, txt, noDominadas, 0);
                    String txtTime = "/time multi NSGAII teams " + time + ".txt";
                    long executionTime = Strategy.timeExecute;
                    if (Strategy.getStrategy().calculateTime) {
                        SaveTxt.writeFileTxtTime(mainPath, txtTime, 0, executionTime);
                    }
                }
                Strategy.destroyExecute();
            }
        }
        return sol;
    }

    public List<State> applyMCMOSA(ProjectRoleState initialSolution, TeamFormationProblem problem) {

        List<State> sol = null;
        if (initialSolution != null) {
            sol = new ArrayList<>();

            configStrategy(problem);
            Strategy.getStrategy().saveListBestStates = true;
            Strategy.getStrategy().saveListStates = true;
            Strategy.getStrategy().saveFreneParetoMonoObjetivo = true;
            MultiCaseSimulatedAnnealing.countIterationsT = 100;
            MultiCaseSimulatedAnnealing.alpha = 0.9;
            MultiCaseSimulatedAnnealing.tfinal = 0.0;
            MultiCaseSimulatedAnnealing.tinitial = 20.0;
            String format = ResourceBundle.getBundle("/algorithmConf").getString("decimalFormat");
            DecimalFormat df = new DecimalFormat(format);
            int iterations = Integer.parseInt(ResourceBundle.getBundle("/algorithmConf").getString("iterations"));

            try {
                Strategy.getStrategy().executeStrategy(iterations, 1, GeneratorType.MultiCaseSimulatedAnnealing);
            } catch (IllegalArgumentException | SecurityException | ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                Logger.getLogger(TF_StepThreeController.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
            List<State> noDominadas = Strategy.getStrategy().listRefPoblacFinal;
            State bestState = Strategy.getStrategy().getBestState();

            //Mostrando las soluciones no dominadas y guardando soluciones en el fichero de texto
            for (State noDominada : noDominadas) {
                if (Strategy.getStrategy().getProblem().getCodification().validState(noDominada)) {
                    sol.add(noDominada);
                }
            }

            if (sol.isEmpty()) {
                if (Strategy.getStrategy().getProblem().getCodification().validState(bestState)) {
                    sol.add(bestState);
                }
            }

            if (!sol.isEmpty()) {
                //SaveTxt.writeStatesMulti();
                long time = System.currentTimeMillis() / 1000;
                String txt = "/multi MCMOSA teams " + time + ".txt";
                ServletContext relativePath = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
                String mainPath = (String) relativePath.getRealPath("/reports/");
                SaveTxt.writeFileTxtMulti(mainPath, txt, noDominadas, 0);
                String txtTime = "/time mutli MCMOSA teams " + time + ".txt";
                long executionTime = Strategy.timeExecute;
                if (Strategy.getStrategy().calculateTime) {
                    SaveTxt.writeFileTxtTime(mainPath, txtTime, 0, executionTime);
                }
            }
            Strategy.destroyExecute();
        }
        return sol;
    }

    public List<State> applyUMOSA(ProjectRoleState initialSolution, TeamFormationProblem problem) {
        List<State> sol = null;
        if (initialSolution != null) {
            sol = new ArrayList<>();
            configStrategy(problem);
            Strategy.getStrategy().saveListBestStates = true;
            Strategy.getStrategy().saveListStates = true;
            Strategy.getStrategy().saveFreneParetoMonoObjetivo = true;
            UMOSA.countIterationsT = 100;
            UMOSA.alpha = 0.9;
            UMOSA.tfinal = 0.0;
            UMOSA.tinitial = 20.0;
            String format = ResourceBundle.getBundle("/algorithmConf").getString("decimalFormat");
            DecimalFormat df = new DecimalFormat(format);
            int iterations = Integer.parseInt(ResourceBundle.getBundle("/algorithmConf").getString("iterations"));

            try {
                Strategy.getStrategy().executeStrategy(iterations, 1, GeneratorType.UMOSA);
            } catch (IllegalArgumentException | SecurityException | ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                Logger.getLogger(TF_StepThreeController.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
            List<State> noDominadas = Strategy.getStrategy().listRefPoblacFinal;
            State bestState = Strategy.getStrategy().getBestState();

            //Mostrando las soluciones no dominadas y guardando soluciones en el fichero de texto
            for (State noDominada : noDominadas) {
                if (Strategy.getStrategy().getProblem().getCodification().validState(noDominada)) {
                    sol.add(noDominada);
                }
            }
            if (sol.isEmpty()) {
                if (Strategy.getStrategy().getProblem().getCodification().validState(bestState)) {
                    sol.add(bestState);
                }
            }
            if (!sol.isEmpty()) {
                long time = System.currentTimeMillis() / 1000;
                String txt = "/multi UMOSA teams " + time + ".txt";
                ServletContext relativePath = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
                String mainPath = relativePath.getRealPath("/reports/");
                SaveTxt.writeFileTxtMulti(mainPath, txt, noDominadas, 0);
                String txtTime = "/time multi UMOSA teams " + time + ".txt";
                long executionTime = Strategy.timeExecute;
                if (Strategy.getStrategy().calculateTime) {
                    SaveTxt.writeFileTxtTime(mainPath, txtTime, 0, executionTime);
                }
            }
            Strategy.destroyExecute();
        }
        return sol;
    }

    public List<State> applyMOGA(ProjectRoleState initialSolution, TeamFormationProblem problem) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException, NoSuchFieldException {
        int ITERATIONS = Integer.parseInt(ResourceBundle.getBundle("/algorithmConf").getString("iterations"));
        int EXECUTIONS = Integer.parseInt(ResourceBundle.getBundle("/algorithmConf").getString("executions"));
        List<State> sol = null;
        if (initialSolution != null) {
            sol = new ArrayList<>();
            for (int i = 0; i < EXECUTIONS; i++) {
                configStrategy(problem);
                //Configurar el algoritmo dentro del BiCIAM
                Strategy.getStrategy().setStopexecute(new StopExecute());
                Strategy.getStrategy().setUpdateparameter(new UpdateParameter());
                MOGA.countRef = 100;//Integer.valueOf(ResourceBundle.getBundle("/algorithmConf").getString("CountRef")); //Cantidad de individuos
                MOGA.selectionType = SelectionType.TournamentSelection;
                MOGA.crossoverType = CrossoverType.GenericCrossover;
                MOGA.mutationType = MutationType.GenericMutation;
                MOGA.replaceType = ReplaceType.GenerationalReplace;
                Strategy.getStrategy().calculateTime = true;
                MOGA.PM = Float.parseFloat(ResourceBundle.getBundle("/algorithmConf").getString("PM"));
                MOGA.PC = Float.parseFloat(ResourceBundle.getBundle("/algorithmConf").getString("PC"));
                try {
                    Strategy.getStrategy().executeStrategy(ITERATIONS, 1, GeneratorType.RandomSearch);
                } catch (IllegalArgumentException | SecurityException | ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                    Logger.getLogger(TF_StepThreeController.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
                List<State> noDominadas = Strategy.getStrategy().listRefPoblacFinal;
                State bestState = Strategy.getStrategy().getBestState();

                //Mostrando las soluciones no dominadas y guardando soluciones en el fichero de texto
                for (State noDominada : noDominadas) {
                    if (Strategy.getStrategy().getProblem().getCodification().validState(noDominada)) {
                        sol.add(noDominada);
                    }
                }
                if (sol.isEmpty()) {
                    if (Strategy.getStrategy().getProblem().getCodification().validState(bestState)) {
                        sol.add(bestState);
                    }
                }
                Strategy.destroyExecute();
            }
        }
        return sol;

    }

    public List<State> applySimulatedAnnealing(ProjectRoleState initialSolution, TeamFormationProblem problem) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException, NoSuchFieldException {
        List<State> sol = null;
        if (initialSolution != null) {
            sol = new ArrayList<>();
            configStrategy(problem);
            Strategy.getStrategy().saveListBestStates = true;
            Strategy.getStrategy().saveListStates = true;
            Strategy.getStrategy().saveFreneParetoMonoObjetivo = true;
            int iterations = Integer.parseInt(ResourceBundle.getBundle("/algorithmConf").getString("iterations"));
            SimulatedAnnealing.alpha = 0.9;
            SimulatedAnnealing.tinitial = 20.0;
            SimulatedAnnealing.tfinal = 0.0;
            SimulatedAnnealing.countIterationsT = 100;
            SimulatedAnnealing.saveRestart = true;
            Strategy.getStrategy().executeStrategy(iterations, 1, GeneratorType.SimulatedAnnealing);

            State bestState = Strategy.getStrategy().getBestState();

            if (Strategy.getStrategy().getProblem().getCodification().validState(bestState)) {
                sol.add(bestState);
                //SaveTxt.writeStatesMono();
                //SaveCSV.mono(bestState);
                Long time = System.currentTimeMillis() / 1000;
                String txt = "/mono simulated annealing teams " + time + ".txt";
                ServletContext relativePath = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
                String mainPath = (String) relativePath.getRealPath("/reports/");
                SaveTxt.writeFileTxtMono(mainPath, txt, sol, 0);
                String txtTime = "/time mono simulated annealing teams " + time + ".txt";
                long executionTime = Strategy.timeExecute;
                if (Strategy.getStrategy().calculateTime) {
                    SaveTxt.writeFileTxtTime(mainPath, txtTime, 0, executionTime);
                }

            } else {
                bestState = prepareSolution();
                if (bestState != null) {
                    sol.add(bestState);
                    long time = System.currentTimeMillis() / 1000;
                    String txt = "/mono simulated annealing teams " + time + ".txt";
                    ServletContext relativePath = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
                    String mainPath = (String) relativePath.getRealPath("/reports/");
                    SaveTxt.writeFileTxtMono(mainPath, txt, sol, 0);
                    String txtTime = "/time mono simulated annealing teams " + time + ".txt";
                    long executionTime = Strategy.timeExecute;
                    if (Strategy.getStrategy().calculateTime) {
                        SaveTxt.writeFileTxtTime(mainPath, txtTime, 0, executionTime);
                    }
                }
            }
            Strategy.destroyExecute();
        }
        return sol;
    }


    public void configurePA(int countMaxIterations, boolean RS, boolean RE, boolean RL, boolean EE, boolean GA, boolean BT, boolean BA, boolean ECR, boolean EDA, boolean EC, boolean LU) throws IllegalArgumentException, NoSuchMethodException, ClassNotFoundException, IllegalAccessException, SecurityException, InstantiationException, InvocationTargetException {
        Strategy.getStrategy().initializeGenerators();
        List<Generator> gene = new ArrayList<>();
        if (RS) {
            SimulatedAnnealing sa = new SimulatedAnnealing();
            SimulatedAnnealing.alpha = 0.9;
            SimulatedAnnealing.tinitial = 20.0;
            SimulatedAnnealing.tfinal = 0.0;
            SimulatedAnnealing.countIterationsT = 100;
            SimulatedAnnealing.saveRestart = true;
            gene.add(sa);
        }
        if (RE) {
            double chi = 0.75;
            double omega1 = -2462.822;
            double tempInitial1 = omega1 / Math.log(chi);
            SimulatedAnnealingExponential1 rs = new SimulatedAnnealingExponential1();
            SimulatedAnnealingExponential1.tinitial = tempInitial1;
            SimulatedAnnealingExponential1.tfinal = 0.0;
            double A1 = (tempInitial1 - rs.tfinal) * (10000 + 1) / 10000;
            double B1 = tempInitial1 - A1;
            SimulatedAnnealingExponential1.valueA = A1;
            SimulatedAnnealingExponential1.valueB = B1;
            SimulatedAnnealingExponential1.countIterationsT = 50;
            gene.add(rs);
        }
        if (RL) {
            double chi = 0.75;
            double omega1 = -2462.822;
            double tempInitial1 = omega1 / Math.log(chi);
            SimulatedAnnealingLinear rl = new SimulatedAnnealingLinear();
            SimulatedAnnealingLinear.t0 = tempInitial1;
            SimulatedAnnealingLinear.tinitial = tempInitial1;
            SimulatedAnnealingLinear.countIterationsT = 50;
            gene.add(rl);
        }
        if (EE) {
            EvolutionStrategies ee = new EvolutionStrategies();
            EvolutionStrategies.countRef = 10;
            EvolutionStrategies.PM = 0.9;
            EvolutionStrategies.selectionType = SelectionType.TruncationSelection;
            EvolutionStrategies.mutationType = MutationType.GenericMutation;
            EvolutionStrategies.replaceType = ReplaceType.GenerationalReplace;
            EvolutionStrategies.truncation = 5;
            gene.add(ee);
        }
        if (GA) {
            GeneticAlgorithm ag = new GeneticAlgorithm();
            GeneticAlgorithm.countRef = 10;
            GeneticAlgorithm.PC = 0.5;
            GeneticAlgorithm.PM = 0.9;
            GeneticAlgorithm.selectionType = SelectionType.TournamentSelection;
            GeneticAlgorithm.crossoverType = CrossoverType.GenericCrossover;
            GeneticAlgorithm.mutationType = MutationType.GenericMutation;
            GeneticAlgorithm.replaceType = ReplaceType.SteadyStateReplace;
            GeneticAlgorithm.truncation = 5;
            gene.add(ag);
        }
        if (BT) {
            TabuSearch ts = new TabuSearch();
            TabuSolutions.maxelements = Integer.parseInt(ResourceBundle.getBundle("/algorithmConf").getString("TabuSolutionsMaxelements"));
            gene.add(ts);
        }
        if (EC) {
            HillClimbing hc = new HillClimbing();
            gene.add(hc);
        }
        if (BA) {
            RandomSearch rs = new RandomSearch();
            gene.add(rs);
        }
        if (ECR) {
            HillClimbingRestart hcr = new HillClimbingRestart();
            hcr.count = Integer.parseInt(ResourceBundle.getBundle("/algorithmConf").getString("HillClimbingRestartCount"));
            gene.add(hcr);
        }
        MultiGenerator.setListGenerators(gene);
    }

    public List<State> applyMultiobjectiveAlgorithmsBriefcase(ProjectRoleState initialSolution, TeamFormationProblem problem) throws IOException, IllegalArgumentException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, SecurityException, InstantiationException, InvocationTargetException, Exception {
        List<State> noDominadas = new ArrayList<State>();
        int iterations = Integer.parseInt(ResourceBundle.getBundle("/algorithmConf").getString("iterations"));
        int executions = Integer.parseInt(ResourceBundle.getBundle("/algorithmConf").getString("executions"));
        String fichero = "multiAlgorithmsBriefcase.txt";
        for (int i = 0; i < executions; i++) {
            //Configurar el algoritmo, dentro de BiCIAM            
            Strategy.getStrategy().setStopexecute(new StopExecute());
            Strategy.getStrategy().setUpdateparameter(new UpdateParameter());
            Strategy.getStrategy().setProblem(problem);
            Strategy.getStrategy().saveListBestStates = true;
            Strategy.getStrategy().calculateTime = true;
            Strategy.getStrategy().saveListStates = true;

            //Aqui va la configuracion espeficia del PA multiobjetivo
            //ECMO, ECMOR, ECMOD, BTMO, RSMO, RSMOU, NSGAII, MOGA, MOEADDE
            configurePAMO(iterations, true, true, false, true, false, false, false, false, false);
            createPlans();
            Strategy.getStrategy().executeStrategy(iterations, 1, GeneratorType.MultiobjectiveMultiGenerator); //cambia aqui el generator type por el multigenerator.
            noDominadas = Strategy.getStrategy().listRefPoblacFinal;
            Strategy.destroyExecute();
            MultiobjectiveMultiGenerator.destroyMultiobjectiveMultiGenerator();
            SaveTxt.writeFileTxt(fichero, noDominadas, i);
        }
        return noDominadas;
    }

    public void configurePAMO(int countIterations, boolean ECMO, boolean ECMOR, boolean ECMOD, boolean BTMO, boolean RSMO, boolean RSMOU, boolean NSGAII, boolean MOGA, boolean MOEADDE)
            throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        //ECMO, ECMOR, ECMOD, BTMO, RSMO, RSMOU, NSGAII, MOGA, MOEADDE
        Strategy.getStrategy().initializeGenerators();
        List<Generator> gene = new ArrayList<Generator>();
        NSGAII nsga = new NSGAII();
        MOGA moga = new MOGA();
        MOEADDE moeadde = new MOEADDE();
        if (ECMO) {
            MultiobjectiveStochasticHillClimbing ecmo = new MultiobjectiveStochasticHillClimbing();
            gene.add(ecmo);
        }
        if (ECMOR) {
            MultiobjectiveHillClimbingRestart ecmor = new MultiobjectiveHillClimbingRestart();
            ecmor.sizeNeighbors = 2;
            gene.add(ecmor);
        }
        if (ECMOD) {
            MultiobjectiveHillClimbingDistance ecmod = new MultiobjectiveHillClimbingDistance();
            gene.add(ecmod);
        }
        if (BTMO) {
            MultiobjectiveTabuSearch btmo = new MultiobjectiveTabuSearch();
            gene.add(btmo);
        }
        if (RSMO) {
            MultiCaseSimulatedAnnealing rsmo = new MultiCaseSimulatedAnnealing();
            rsmo.countIterationsT = 30;
            rsmo.alpha = 0.9;
            rsmo.tfinal = 0.0;
            rsmo.tinitial = 20.0;
            gene.add(rsmo);
        }
        if (RSMOU) {
            UMOSA rsmou = new UMOSA();
            rsmou.countIterationsT = 30;
            rsmou.alpha = 0.9;
            rsmou.tfinal = 0.0;
            rsmou.tinitial = 20.0;
            gene.add(rsmou);
        }
        if (NSGAII) {
            nsga.countRef = 20; //cantidad de individuos 20
            nsga.selectionType = SelectionType.TournamentSelection;
            nsga.crossoverType = CrossoverType.GenericCrossover;
            nsga.mutationType = MutationType.GenericMutation;
            nsga.PM = 0.5;
            nsga.PC = 0.9;
            gene.add(nsga);
        }
        if (MOGA) {
            moga.countRef = 50; //cantidad de individuos 100
            moga.selectionType = SelectionType.TournamentSelection;
            moga.crossoverType = CrossoverType.GenericCrossover;
            moga.mutationType = MutationType.GenericMutation;

            //moga.replaceType = ReplaceType.GenerationalReplace;
            moga.replaceType = ReplaceType.SteadyStateReplace;
            moga.PM = 0.9;
            moga.PC = 0.5;
            gene.add(moga);
        }
        if (MOEADDE) {
            moeadde.countRef = 100; //cantidad de individuos 100
            moeadde.crossoverType = CrossoverType.OnePointCrossover;
            moeadde.mutationType = MutationType.TowPointsMutation;
            moeadde.PS = 0.7;
            moeadde.PC = 0.6;
            moeadde.PM = 0.9;
            moeadde.T = (int) (0.1 * moeadde.countRef);
            moeadde.Nr = (int) (0.1 * moeadde.countRef);
            gene.add(moeadde);
        }
        MultiobjectiveMultiGenerator.setListGenerators(gene);

    }

    public List<State> GA(ProjectRoleState initialSolution, TeamFormationProblem problem) throws ClassNotFoundException, InvocationTargetException, InstantiationException, NoSuchMethodException, IllegalAccessException {

        int ITERATIONS = Integer.parseInt(ResourceBundle.getBundle("/algorithmConf").getString("iterations"));
        int EXECUTIONS = Integer.parseInt(ResourceBundle.getBundle("/algorithmConf").getString("executions"));

        List<State> sol = null;
        if (initialSolution != null) {
            sol = new ArrayList<>();

            for (int i = 0; i < EXECUTIONS; i++) {
                configStrategy(problem);
                State bestState = null;
                Strategy.getStrategy().setStopexecute(new StopExecute());
                Strategy.getStrategy().setUpdateparameter(new UpdateParameter());
                Strategy.getStrategy().saveListBestStates = true;
                Strategy.getStrategy().saveListStates = true;

                //Configuracion del algoritmo genetico
                //cantidad de individuos
                GeneticAlgorithm.countRef = Integer.parseInt(ResourceBundle.getBundle("/algorithmConf").getString("CountRef"));
                //Probabilidad de cruzamiento o combinacion
                GeneticAlgorithm.PC = Float.parseFloat(ResourceBundle.getBundle("/algorithmConf").getString("PC"));
                //Probabilidad de mutacion
                GeneticAlgorithm.PM = Float.parseFloat(ResourceBundle.getBundle("/algorithmConf").getString("PM"));

                GeneticAlgorithm.selectionType = SelectionType.TournamentSelection;
                GeneticAlgorithm.truncation = Integer.parseInt(ResourceBundle.getBundle("/algorithmConf").getString("GeneticAlgorithmTruncation"));
                GeneticAlgorithm.crossoverType = CrossoverType.GenericCrossover;
                GeneticAlgorithm.mutationType = MutationType.GenericMutation;
                GeneticAlgorithm.replaceType = ReplaceType.SteadyStateReplace;

                Strategy.getStrategy().executeStrategy(ITERATIONS, 1, GeneratorType.RandomSearch);

                bestState = Strategy.getStrategy().getBestState();

                if (Strategy.getStrategy().getProblem().getCodification().validState(bestState)) {
                    sol.add(bestState);
                    long time = System.currentTimeMillis() / 1000;
                    String txt = "/mono genetic algorithm teams " + time + ".txt";
                    ServletContext relativePath = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
                    String mainPath = relativePath.getRealPath("/reports/");
                    SaveTxt.writeFileTxtMono(mainPath, txt, sol, 0);
                    String txtTime = "/time mono genetic algorithm teams " + time + ".txt";
                    long executionTime = Strategy.timeExecute;
                    if (Strategy.getStrategy().calculateTime) {
                        SaveTxt.writeFileTxtTime(mainPath, txtTime, 0, executionTime);
                    }
                } else {
                    bestState = prepareSolution();
                    if (bestState != null) {
                        sol.add(bestState);
                        long time = System.currentTimeMillis() / 1000;
                        String txt = "/mono genetic algorithm teams " + time + ".txt";
                        ServletContext relativePath = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
                        String mainPath = (String) relativePath.getRealPath("/reports/");
                        SaveTxt.writeFileTxtMono(mainPath, txt, sol, 0);
                        String txtTime = "/time mono genetic algorithm teams " + time + ".txt";
                        long executionTime = Strategy.timeExecute;
                        if (Strategy.getStrategy().calculateTime) {
                            SaveTxt.writeFileTxtTime(mainPath, txtTime, 0, executionTime);
                        }
                    }
                }
                Strategy.destroyExecute();
            }
        }
        return sol;
    }

    public void createPlans() throws Exception {
        /*List<Plan> plans = new ArrayList<Plan>();

        ArrayList<Float> weights = new ArrayList<>();
        ArrayList<Float> taxs = new ArrayList<>();
        ArrayList<Float> weights2 = new ArrayList<>();

        weights.add(50f);

        weights.add(50f);

        weights.add(50f);

        weights2.add(50f);
        weights2.add(0.0f);
        weights2.add(0.0f);
        weights2.add(0.0f);
        weights2.add(0.0f);
        weights2.add(10f);
        weights2.add(0.0f);
        weights2.add(30f);
        weights2.add(0.0f);

        for (int i = 0; i < 9; i++) {

            taxs.add(0.1f);
        }

        Plan plan1 = new Plan(1, 1000, taxs, 10, weights, SelectionTypePortfolio.Roulette);
        //Plan plan2 = new Plan(5001, 10000, taxs, 10, 0.2f, weights2);
        plans.add(plan1);
        //plans.add(plan2);

        if (MultiobjectiveMultiGenerator.validateIterations(plans, 1000)) {
            MultiobjectiveMultiGenerator.setPlans(plans);
        } else {
            throw new Exception("No coinciden las iteraciones");
        }*/
    }

    /**
     * Mejorar solucion aplicando el metodo MultiobjectiveStochasticHillClimbing
     *
     * @param initialSolution
     * @param problem
     * @return
     * @throws java.io.IOException
     */
    public List<State> applyMultiobjectiveStochasticHC(ProjectRoleState initialSolution, TeamFormationProblem problem) throws IOException {

        List<State> sol = null;
        if (initialSolution != null) {
            sol = new ArrayList<>();

            configStrategy(problem);
            Strategy.getStrategy().saveListBestStates = true;
            Strategy.getStrategy().saveListStates = true;
            Strategy.getStrategy().saveFreneParetoMonoObjetivo = true;
            int iterations = Integer.parseInt(ResourceBundle.getBundle("/algorithmConf").getString("iterations"));

            try {
                Strategy.getStrategy().executeStrategy(iterations, 1, GeneratorType.MultiobjectiveStochasticHillClimbing);
            } catch (IllegalArgumentException | SecurityException | ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                Logger.getLogger(TF_StepThreeController.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
            List<State> noDominadas = Strategy.getStrategy().listRefPoblacFinal;
            State bestState = Strategy.getStrategy().getBestState();

            //Mostrando las soluciones no dominadas y guardando soluciones en el fichero de texto
            for (State noDominada : noDominadas) {
                if (Strategy.getStrategy().getProblem().getCodification().validState(noDominada)) {
                    sol.add(noDominada);
                }
            }

            if (sol.isEmpty()) {
                if (Strategy.getStrategy().getProblem().getCodification().validState(bestState)) {
                    sol.add(bestState);
                }
            }

            if (!sol.isEmpty()) {
                SaveTxt.writeStatesMulti();
            }
            Strategy.destroyExecute();
        }
        return sol;
    }

    /**
     * Mejorar solucion aplicando el metodo MultiobjectiveHCRestart
     *
     * @param initialSolution
     * @param problem
     * @return
     * @throws java.io.IOException
     */
    public List<State> applyMultiobjectiveHCRestart(ProjectRoleState initialSolution, TeamFormationProblem problem) throws IOException {

        List<State> sol = null;
        if (initialSolution != null) {
            sol = new ArrayList<>();

            configStrategy(problem);
            Strategy.getStrategy().saveListBestStates = true;
            Strategy.getStrategy().saveListStates = true;
            Strategy.getStrategy().saveFreneParetoMonoObjetivo = true;
            MultiobjectiveHillClimbingRestart.sizeNeighbors = Integer.parseInt(ResourceBundle.getBundle("/algorithmConf").getString("MultiobjectiveHCRestartSizeNeighbors"));
            int iterations = Integer.parseInt(ResourceBundle.getBundle("/algorithmConf").getString("iterations"));

            try {
                Strategy.getStrategy().executeStrategy(iterations, 1, GeneratorType.MultiobjectiveHillClimbingRestart);
            } catch (IllegalArgumentException | SecurityException | ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                Logger.getLogger(TF_StepThreeController.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
            List<State> noDominadas = Strategy.getStrategy().listRefPoblacFinal;
            State bestState = Strategy.getStrategy().getBestState();

            //Mostrando las soluciones no dominadas y guardando soluciones en el fichero de texto
            for (State noDominada : noDominadas) {
                if (Strategy.getStrategy().getProblem().getCodification().validState(noDominada)) {
                    sol.add(noDominada);
                }
            }

            if (sol.isEmpty()) {
                if (Strategy.getStrategy().getProblem().getCodification().validState(bestState)) {
                    sol.add(bestState);
                }
            }

            if (!sol.isEmpty()) {
                SaveTxt.writeStatesMulti();
            }
            Strategy.destroyExecute();
        }
        return sol;
    }

    /**
     * Mejorar solucion aplicando el metodo MultiobjectiveHCDistance
     *
     * @param initialSolution
     * @param problem
     * @return
     * @throws java.io.IOException
     */
    public List<State> applyMultiobjectiveHCDistance(ProjectRoleState initialSolution, TeamFormationProblem problem) throws IOException {

        List<State> sol = null;
        if (initialSolution != null) {
            sol = new ArrayList<>();

            configStrategy(problem);
            Strategy.getStrategy().saveListBestStates = true;
            Strategy.getStrategy().saveListStates = true;
            Strategy.getStrategy().saveFreneParetoMonoObjetivo = true;
            MultiobjectiveHillClimbingDistance.sizeNeighbors = Integer.parseInt(ResourceBundle.getBundle("/algorithmConf").getString("MultiobjectiveHCDistanceSizeNeighbors"));
            int iterations = Integer.parseInt(ResourceBundle.getBundle("/algorithmConf").getString("iterations"));

            try {
                Strategy.getStrategy().executeStrategy(iterations, 1, GeneratorType.MultiobjectiveHillClimbingDistance);
            } catch (IllegalArgumentException | SecurityException | ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                Logger.getLogger(TF_StepThreeController.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
            List<State> noDominadas = Strategy.getStrategy().listRefPoblacFinal;
            State bestState = Strategy.getStrategy().getBestState();

            //Mostrando las soluciones no dominadas y guardando soluciones en el fichero de texto
            for (State noDominada : noDominadas) {
                if (Strategy.getStrategy().getProblem().getCodification().validState(noDominada)) {
                    sol.add(noDominada);
                }
            }

            if (sol.isEmpty()) {
                if (Strategy.getStrategy().getProblem().getCodification().validState(bestState)) {
                    sol.add(bestState);
                }
            }

            if (!sol.isEmpty()) {
                SaveTxt.writeStatesMulti();
            }
            Strategy.destroyExecute();
        }
        return sol;
    }

    public List<State> applyMultiobjectiveTabuSearch(ProjectRoleState initialSolution, TeamFormationProblem problem) throws IOException {

        List<State> sol = null;
        if (initialSolution != null) {
            sol = new ArrayList<>();

            configStrategy(problem);
            Strategy.getStrategy().saveListBestStates = true;
            Strategy.getStrategy().saveListStates = true;
            Strategy.getStrategy().saveFreneParetoMonoObjetivo = true;
            TabuSolutions.maxelements = Integer.parseInt(ResourceBundle.getBundle("/algorithmConf").getString("MultiobjectiveTabuSolutionsMaxelements"));
            String format = ResourceBundle.getBundle("/algorithmConf").getString("decimalFormat");
            DecimalFormat df = new DecimalFormat(format);
            int iterations = Integer.parseInt(ResourceBundle.getBundle("/algorithmConf").getString("iterations"));

            try {
                Strategy.getStrategy().executeStrategy(iterations, 1, GeneratorType.MultiobjectiveTabuSearch);
            } catch (IllegalArgumentException | SecurityException | ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                Logger.getLogger(TF_StepThreeController.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
            List<State> noDominadas = Strategy.getStrategy().listRefPoblacFinal;
            State bestState = Strategy.getStrategy().getBestState();

            //Mostrando las soluciones no dominadas y guardando soluciones en el fichero de texto
            for (State noDominada : noDominadas) {
                if (Strategy.getStrategy().getProblem().getCodification().validState(noDominada)) {
                    sol.add(noDominada);
                }
            }

            if (sol.isEmpty()) {
                if (Strategy.getStrategy().getProblem().getCodification().validState(bestState)) {
                    sol.add(bestState);
                }
            }

            if (!sol.isEmpty()) {
                SaveTxt.writeStatesMulti();
            }
            Strategy.destroyExecute();
        }
        return sol;
    }

    /**
     * Registrar incompatibilidad de roles (personalizada)
     */
    public void addRolesIncompatibility_Listener() {
        if (incompRoleList == null) {
            incompRoleList = new ArrayList<>();
        }
        incompRoleList.add(new SelectedRoleIncompatibility(roleA, roleB));
    }

    public void deleteRolesIncompatibility_Listener(List<SelectedRoleIncompatibility> incompRoleDelete) {
        incompRoleList.removeAll(incompRoleDelete);
    }

    /**
     * Registrar incompatibilidad personalizada
     */
    public void addIncompatibility_Listener() {
        if (incompList == null) {
            incompList = new ArrayList<>();
        }
        incompList.add(new SelectedWorkerIncompatibility(personA, personB));
    }

    public void deleteIncompatibility_Listener(List<SelectedWorkerIncompatibility> incompDelete) {
        incompList.removeAll(incompDelete);
    }

    /**
     * para mostrar incompatibilidades de una persona en el combo dependiente
     *
     * @return
     */
    public List<Worker> updateIncompatibilities_Listener() {

        List<Worker> toReturn = new ArrayList<>();

        if (personA != null) {
            List<WorkerConflict> personAWorkerConflicts = personA.getWorkerConflictList1();
            for (WorkerConflict workerIncompatibleConflict : personAWorkerConflicts) {
                if (workerIncompatibleConflict.getIndexFk().getWeight() == 1) {
                    if (workerIncompatibleConflict.getWorkerFk().getId().equals(personA.getId())) {
                        toReturn.add(workerIncompatibleConflict.getWorkerConflictFk());
                    } else {
                        toReturn.add(workerIncompatibleConflict.getWorkerFk());
                    }
                }
            }
        }
        return toReturn;
    }

    /**
     * para mostrar incompatibilidades de un rol en el combo dependiente
     *
     * @return
     */
    public List<Role> updateRoleIncompatibilities_Listener() {
        List<Role> toReturn = new ArrayList<>();

        if (roleA != null) {
            toReturn.addAll(roleFacade.findAll());
            toReturn.remove(roleA);
        }
        return toReturn;
    }

    /**
     * Construir el arbol con las propuestas de jefes de proyecto
     *
     * @param teamProposal
     * @return
     */
    public TreeNode buildTeamProposalTree(List<State> teamProposal) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        TreeNode root = new DefaultTreeNode();

        if (!teamProposal.isEmpty()) {
            String format = ResourceBundle.getBundle("/algorithmConf").getString("decimalFormat");
            DecimalFormat df = new DecimalFormat(format);
            root.setExpanded(true);
            TreeNode prop;
            TreeNode projectNode;
            TreeNode projectEvaluation;
            TreeNode roleNode;
            TreeNode personNode;

            TeamFormationParameters parameters = initializeParameters(); //obtener parametros a considerar
            //Funciones objetivo
            ArrayList<ObjetiveFunction> objectiveFunctions = new ArrayList<>(ObjetiveFunctionUtil.getObjectiveFunctions(parameters));

            for (State state : teamProposal) {
                if (state != null) {
                    StringBuilder eval = new StringBuilder();
                    if (state.getEvaluation().size() == 1) {
                        eval.append(" ").append(objectiveFunctions.get(0).getClass().getField("className").get(null));
                        for (int i = 1; i < objectiveFunctions.size(); i++) {
                            eval.append(" | ").append(objectiveFunctions.get(i).getClass().getField("className").get(null));
                        }
                        eval.append(": ").append(df.format(state.getEvaluation().get(0)));
                    } else {
                        for (int i = 0; i < objectiveFunctions.size(); i++) {
                            eval.append(" ").append(objectiveFunctions.get(i).getClass().getField("className").get(null)).append(": ").append(df.format(state.getEvaluation().get(i)));
                        }
                    }
                    String formattedEval = "Propuesta: " + eval.toString();

                    prop = new DefaultTreeNode(formattedEval);
                    prop.setType("Prop");
                    prop.setExpanded(false);
                    root.getChildren().add(prop);

                    List<Object> projects = state.getCode();

                    if (projects != null) {
                        for (Object proj : projects) {
                            ProjectRole projectRole = (ProjectRole) proj;

                            projectNode = new DefaultTreeNode(projectRole.getProject());
                            projectNode.setType("P");
                            projectNode.setSelectable(false);
                            projectNode.setExpanded(false);
                            prop.getChildren().add(projectNode);

                            for (int j = 0; j < projectRole.getProjectEvaluation().length - 1; j++) {
                                if (projectRole.getProjectEvaluation()[j] != null) {
                                    Double projectEval = projectRole.getProjectEvaluation()[j];
                                    String className = "";
                                    switch (j) {
                                        case 0:
                                            className = "Competencias";
                                            break;
                                        case 1:
                                            className = "Balance competencias";
                                            break;
                                        case 2:
                                            className = "Carga de trabajo";
                                            break;
                                        case 3:
                                            className = "Balance carga de trabajo";
                                            break;
                                        case 4:
                                            className = "Incompatibilidades";
                                            break;
                                        case 5:
                                            className = "Balance incompatibilidades";
                                            break;
                                        case 6:
                                            className = "Costo a distancia";
                                            break;
                                        case 7:
                                            className = "Balance costo a distancia";
                                            break;
                                        case 8:
                                            className = "Interés";
                                            break;
                                        case 9:
                                            className = "Balance interés";
                                            break;
                                        case 10:
                                            className = "Roles de Belbin";
                                            break;
                                        case 11:
                                            className = "Balance roles de Belbin";
                                            break;
                                        case 12:
                                            className = "Interés por proyecto";
                                            break;
                                        case 13:
                                            className = "Balance interés por proyecto";
                                            break;
                                        case 14:
                                            className = "Tipos MBTI";
                                            break;
                                        case 15:
                                            className = "Balance tipos MBTI";
                                            break;
                                        case 16:
                                            className = "Multirol";
                                            break;
                                    }
                                    eval = new StringBuilder(className + ": " + df.format(projectEval));
                                    projectEvaluation = new DefaultTreeNode(eval.toString());
                                    projectEvaluation.setType("E");
                                    projectEvaluation.setSelectable(false);
                                    projectEvaluation.setExpanded(true);
                                    projectNode.getChildren().add(projectEvaluation);
                                }
                            }
                            for (RoleWorker pr : projectRole.getRoleWorkers()) {
                                if (pr != null) {
                                    roleNode = new DefaultTreeNode(pr.getRole());
                                    roleNode.setType("R");
                                    roleNode.setSelectable(false);
                                    roleNode.setExpanded(true);
                                    projectNode.getChildren().add(roleNode);

                                    pr.getWorkers().addAll(pr.getFixedWorkers());
                                    for (Worker p : pr.getWorkers()) {
                                        if (p != null) {
                                            personNode = new DefaultTreeNode(p);
                                            personNode.setType("W");
                                            personNode.setSelectable(true);
                                            personNode.setExpanded(true);
                                            roleNode.getChildren().add(personNode);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return root;
    }

    public String evalToString(LinkedList<Double> eval) {
        StringBuilder res = new StringBuilder();
        eval.forEach(ev -> res.append(ev).append(" "));
        return res.toString();
    }

    /**
     * Obtener restricciones para equipo
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
            if (parameters.isCanBeProjectBoss()) { //una persona solo puede ser asignada a un proyecto
                restrictions.add(new IsWorkerAssigned());
            }
            if (parameters.isCanBeProjectBoss()) { //una persona solo puede ser jefe de un projecto
                restrictions.add(new IsProjectBoss());
            }
            if (parameters.isConfAllGroup()) { //una persona solo puede ser jefe de un projecto
                restrictions.add(new PersonPerGroupAssigned());
            }
            if (parameters.isTakeCustomPersonWorkLoad()) { //la persona no puede tener una carga de trabajo superior a la definida
                if (parameters.getMaxRoleLoad() != null) {
                    restrictions.add(new MaxWorkload(parameters));
                }
            }
            if (parameters.isMaxCompetencesByProject()) { // cada persona debe tener los niveles de competencias seleccionados
                restrictions.add(new AllCompetitionLevels(parameters));
            }
            if (parameters.isAnyIncompatibility()) {
                restrictions.add(new IncompatibleWorkers());
            }
            if (parameters.isAnySelectedIncompatibility()) {
                parameters.setsWI(incompList);
                parameters.setsRI(incompRoleList);
                if (parameters.getsWI() != null && !parameters.getsWI().isEmpty()) {
                    restrictions.add(new IncompatibleSelectedWorkers(parameters));
                }
                // incompatibilidades de la visual ede conf equipo
                if (parameters.getsRI() != null && !parameters.getsRI().isEmpty()) {
                    restrictions.add(new IncompatibleRoles(parameters));
                }
            }
            //sobre las caracteristicas psicologicas considerar
            if (parameters.isAllBelbinRoles()) { //exigir presencia de todos los roles de belbin
                restrictions.add(new AllBelbinRoles());
            }
            if (parameters.isDemandNBrains()) { //demndar una cantidad de personas con rol cerebro por equipo
                restrictions.add(new ExistCerebro(parameters));
            }
            if (parameters.isBalanceBelbinCategories()) { //balancear categorias de belbin
                restrictions.add(new isBalanced(parameters));
            }
            if (parameters.isAllBelbinCategories()) { //exigir precensia de todas las categorias de roles de belbin
                restrictions.add(new AllBelbinCategories());
            }
            if (parameters.isMinimunRoles()) {
                restrictions.add(new MinimumRoles());
            }
            if (parameters.isBossNeedToBeAssignedToAnotherRoles()) {
                restrictions.add(new BossNeedToBeAssignedToAnotherRole());
            }
        }
        return restrictions;
    }

    public List<ProjectRole> getProjectRolesForSaveTeam() {
        List<TreeNode> projectNodes = selectedNode.getChildren();
        List<ProjectRole> projectRoles = new ArrayList<>();
        for (TreeNode projectNode : projectNodes) {
            ProjectRole projectRole = new ProjectRole();
            List<RoleWorker> roleWorkers = new ArrayList<>();
            Project project = (Project) projectNode.getData();
            projectRole.setProject(project);
            List<TreeNode> rolesNodes = projectNode.getChildren();
            for (TreeNode roleNode : rolesNodes) {
                if (roleNode.getType().equalsIgnoreCase("R")) {
                    Role role = (Role) roleNode.getData();
                    RoleWorker roleWorker = new RoleWorker();
                    roleWorker.setRole(role);
                    List<TreeNode> workersNodes = roleNode.getChildren();
                    List<Worker> workers = new ArrayList<>();
                    for (TreeNode workerNode : workersNodes) {
                        Worker worker = (Worker) workerNode.getData();
                        workers.add(worker);
                    }
                    roleWorker.setWorkers(workers);
                    roleWorkers.add(roleWorker);
                }
            }
            projectRole.setRoleWorkers(roleWorkers);
            projectRoles.add(projectRole);
        }
        return projectRoles;
    }

    public void saveTeam() {
        Date date = new Date();
        List<ProjectRole> projectRoles = getProjectRolesForSaveTeam();

        for (ProjectRole projectRole : projectRoles) {
            Project project = projectRole.getProject();
            List<RoleWorker> roleWorkers = projectRole.getRoleWorkers();
            for (RoleWorker roleWorker : roleWorkers) {
                Role role = roleWorker.getRole();
                List<Worker> workers = roleWorker.getWorkers();
                for (Worker worker : workers) {
                    AssignedRole assignedRole = new AssignedRole();
                    assignedRole.setStatus("status");
                    assignedRole.setObservation("observation");
                    assignedRole.setBeginDate(date);
                    assignedRole.setCyclesFk(projectRole.lastProjectCycle());
                    assignedRole.setRolesFk(role);
                    assignedRole.setWorkersFk(worker);
                    assignedRoleController.setSelected(assignedRole);
                    assignedRoleController.create();
                }
            }
        }
    }
    
    /*
       Para fijar personas en roles despues de obtener una propuesta de equipo
    */

        /**
     * Para fijar una persona en un rol
     */
    public void fixMember() {
        System.out.println("entrooooooooooooooooooooooooooooooooooooo");
        FixedWorker fixed = new FixedWorker();

        Project project;  
        Role role;
        Worker selectedMember;
        TreeNode roleNode;
       
        
        if (selectedNode.getData() != null) {
            System.out.println("el selected node no esta vacio--------------------");
            //System.out.println("rol distinto de null---------------------");
             //System.out.println(role.getRoleName()+" es el rol q entroooooo");
            if (selectedNode.getType().equals("W")) {
               // selectedMemberNode = (BossProposal) selectedNode.getData();
                selectedMember = (Worker) selectedNode.getData();
                System.out.println("m cogio el worker---------------------");
                System.out.println(selectedMember.getPersonName() + "  fue seleccionado");
                    //System.out.println("reconocio al nodoooooooooo");
                if(selectedNode.getParent().getType().equals("R")) {
                    roleNode = selectedNode.getParent();
                    role = (Role) roleNode.getData();
                    System.out.println(role.getRoleName() + " rol padre");
                    System.out.println("me cogio el rol---------------------");
                    System.out.println(roleNode.getParent().getType()  + " tipo del padre del rol");
                    
                    if (roleNode.getParent().getType().equals("P")){
                      project = (Project) roleNode.getParent().getData();
                      System.out.println("el proyecto es   " + project.getProjectName());
                      fixed.setBoss(selectedMember);
                      fixed.setRole(role);
                      fixed.setProject(project);

                        if (parametersTeam.getFixedWorkers() == null) {
                            parametersTeam.setFixedWorkers(new ArrayList<>());                           
                        }

                        if(parametersTeam.getFixedWorkers().isEmpty()){
                            parametersTeam.getFixedWorkers().add(fixed);
                        }
                        else{                            
                            int i = 0; 
                            boolean foundFixed = false;
                            while(i < parametersTeam.getFixedWorkers().size() && !foundFixed){
                                if(parametersTeam.getFixedWorkers().get(i).getBoss().getId().compareTo(fixed.getBoss().getId()) == 0){
                                    foundFixed = true;
                                }  
                                i++;
                            }
                            
                            if(!foundFixed){
                                parametersTeam.getFixedWorkers().add(fixed);
                                System.out.println("entro  a fijar miembroooooooooo");
                                System.out.println(parametersTeam.getFixedWorkers().size() + " miembros fijados");
                                System.out.println(selectedMember.getPersonName());
                            }
                        }                   
                    }                    
                }
            }
        }
    }

    /**
     * Desasignar una persona del rol
     */
    public void removeMemberFromFixedList_Listener() {
        for (FixedWorker fixedWorker : fixedWorkersList) {
            for (int i = 0; i < parametersTeam.getFixedWorkers().size(); i++) {
                if (fixedWorker.getBoss().getId().equals(parametersTeam.getFixedWorkers().get(i).getBoss().getId())) {
                    parametersTeam.getFixedWorkers().remove(i);
                }
            }
        }
        fixedWorkersList.clear();   
        
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

    public float getProjectCompetenceWeight() {
        return projectCompetenceWeight;
    }

    public void setProjectCompetenceWeight(float projectCompetenceWeight) {
        this.projectCompetenceWeight = projectCompetenceWeight;
    }

    public boolean isTakeProjectRequirements() {
        return takeProjectRequirements;
    }

    public void setTakeProjectRequirements(boolean takeProjectRequirements) {
        this.takeProjectRequirements = takeProjectRequirements;
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

    public float getRoleExperienceWeight() {
        return roleExperienceWeight;
    }

    public boolean isDemandNBrains() {
        return demandNBrains;
    }

    public void setDemandNBrains(boolean demandNBrains) {
        this.demandNBrains = demandNBrains;
    }

    public void setRoleExperienceWeight(float roleExperienceWeight) {
        this.roleExperienceWeight = roleExperienceWeight;
    }

    public boolean isRoleExperience() {
        return roleExperience;
    }

    public void setRoleExperience(boolean roleExperience) {
        this.roleExperience = roleExperience;
    }

    public boolean isNoProject() {
        return noProject;
    }

    public void setNoProject(boolean noProject) {
        this.noProject = noProject;
    }

    public boolean isEvaluation() {
        return evaluation;
    }

    public void setEvaluation(boolean evaluation) {
        this.evaluation = evaluation;
    }

    public String getSolutionMethod() {
        return solutionMethod;
    }

    public void setSolutionMethod(String solutionMethod) {
        this.solutionMethod = solutionMethod;
    }

    public int getSolutionWay() {
        return solutionWay;
    }

    public void setSolutionWay(int solutionWay) {
        this.solutionWay = solutionWay;
    }

    public boolean isTakeCompetences() {
        return takeCompetences;
    }

    public boolean isAllBelbin() {
        return allBelbin;
    }

    public String getActionMental() {
        return actionMental;
    }

    public void setActionMental(String actionMental) {
        this.actionMental = actionMental;
    }

    public TeamFormationParameters getParametersTeam() {
        return parametersTeam;
    }

    public void setParametersTeam(TeamFormationParameters parametersTeam) {
        this.parametersTeam = parametersTeam;
    }

    public String getMentalSocial() {
        return mentalSocial;
    }

    public void setMentalSocial(String mentalSocial) {
        this.mentalSocial = mentalSocial;
    }

    public void setAllBelbin(boolean allBelbin) {
        this.allBelbin = allBelbin;
    }

    public int getCountBrains() {
        return countBrains;
    }

    public void setCountBrains(int countBrains) {
        this.countBrains = countBrains;
    }

    public boolean isCanBeAssigned() {
        return canBeAssigned;
    }

    public void setCanBeAssigned(boolean canBeAssigned) {
        this.canBeAssigned = canBeAssigned;
    }

    public boolean isTakeCustomPersonWorkLoad() {
        return takeCustomPersonWorkLoad;
    }

    public void setTakeCustomPersonWorkLoad(boolean takeCustomPersonWorkLoad) {
        this.takeCustomPersonWorkLoad = takeCustomPersonWorkLoad;
    }

    public RoleLoad getRoleLoad() {
        return roleLoad;
    }

    public void setRoleLoad(RoleLoad roleLoad) {
        this.roleLoad = roleLoad;
    }

    public boolean isBalanceBelbinCategories() {
        return balanceBelbinCategories;
    }

    public void setBalanceBelbinCategories(boolean balanceBelbinCategories) {
        this.balanceBelbinCategories = balanceBelbinCategories;
    }

    public boolean isAllBelbinCategories() {
        return allBelbinCategories;
    }

    public void setAllBelbinCategories(boolean allBelbinCategories) {
        this.allBelbinCategories = allBelbinCategories;
    }

    public void setTakeCompetences(boolean takeCompetences) {
        this.takeCompetences = takeCompetences;
    }

    public List<SelectedWorkerIncompatibility> getIncompDelete() {
        return incompDelete;
    }

    public void setIncompDelete(List<SelectedWorkerIncompatibility> incompDelete) {
        this.incompDelete = incompDelete;
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

    public boolean isTakeMbtiTypes() {
        return takeMbtiTypes;
    }

    public void setTakeMbtiTypes(boolean takeMbtiTypes) {
        this.takeMbtiTypes = takeMbtiTypes;
    }

    public TreeNode getTeamProposal() {
        return teamProposal;
    }

    public void setTeamProposal(TreeNode teamProposal) {
        this.teamProposal = teamProposal;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public boolean isBalanceCompetences() {
        return balanceCompetences;
    }

    public void setBalanceCompetences(boolean balanceCompetences) {
        this.balanceCompetences = balanceCompetences;
    }

    public boolean isBalanceWorkLoad() {
        return balanceWorkLoad;
    }

    public void setBalanceWorkLoad(boolean balanceWorkLoad) {
        this.balanceWorkLoad = balanceWorkLoad;
    }

    public boolean isBalanceCostDistance() {
        return balanceCostDistance;
    }

    public void setBalanceCostDistance(boolean balanceCostDistance) {
        this.balanceCostDistance = balanceCostDistance;
    }

    public boolean isBalanceInterests() {
        return balanceInterests;
    }

    public void setBalanceInterests(boolean balanceInterests) {
        this.balanceInterests = balanceInterests;
    }

    public float getCompetenceWeight() {
        return competenceWeight;
    }

    public void setCompetenceWeight(float competenceWeight) {
        this.competenceWeight = competenceWeight;
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

    public boolean isTakeSynergy() {
        return takeSynergy;
    }

    public String getAboutIncompatibilityes() {
        return aboutIncompatibilityes;
    }

    public boolean isAnyIncompatibility() {
        return anyIncompatibility;
    }

    public void setAnyIncompatibility(boolean anyIncompatibility) {
        this.anyIncompatibility = anyIncompatibility;
    }

    public boolean isAnySelectedIncompatibility() {
        return anySelectedIncompatibility;
    }

    public void setAnySelectedIncompatibility(boolean anySelectedIncompatibility) {
        this.anySelectedIncompatibility = anySelectedIncompatibility;
    }

    public void setAboutIncompatibilityes(String aboutIncompatibilityes) {
        this.aboutIncompatibilityes = aboutIncompatibilityes;
    }

    public void setTakeSynergy(boolean takeSynergy) {
        this.takeSynergy = takeSynergy;
    }

    public boolean isBalanceSynergy() {
        return balanceSynergy;
    }

    public void setBalanceSynergy(boolean balanceSynergy) {
        this.balanceSynergy = balanceSynergy;
    }

    public float getSynergyWeight() {
        return synergyWeight;
    }

    public boolean isTakeBelbin() {
        return takeBelbin;
    }

    public void setTakeBelbin(boolean takeBelbin) {
        this.takeBelbin = takeBelbin;
    }

    public boolean isBalanceBelbin() {
        return balanceBelbin;
    }

    public void setBalanceBelbin(boolean balanceBelbin) {
        this.balanceBelbin = balanceBelbin;
    }

    public float getBelbinWeight() {
        return belbinWeight;
    }

    public void setBelbinWeight(float belbinWeight) {
        this.belbinWeight = belbinWeight;
    }

    public float getMbtiWeight() {
        return mbtiWeight;
    }

    public void setMbtiWeight(float mbtiWeight) {
        this.mbtiWeight = mbtiWeight;
    }

    public void setSynergyWeight(float synergyWeight) {
        this.synergyWeight = synergyWeight;
    }

    public boolean isTakeMultiroleTeamMembers() {
        return takeMultiroleTeamMembers;
    }

    public void setTakeMultiroleTeamMembers(boolean takeMultiroleTeamMembers) {
        this.takeMultiroleTeamMembers = takeMultiroleTeamMembers;
    }

    public boolean isBalanceMultiroleTeamMembers() {
        return balanceMultiroleTeamMembers;
    }

    public void setBalanceMultiroleTeamMembers(boolean balanceMultiroleTeamMembers) {
        this.balanceMultiroleTeamMembers = balanceMultiroleTeamMembers;
    }

    public float getMultiRoleTeamMembersWeight() {
        return multiRoleTeamMembersWeight;
    }

    public void setMultiRoleTeamMembersWeight(float multiRoleTeamMembersWeight) {
        this.multiRoleTeamMembersWeight = multiRoleTeamMembersWeight;
    }

    public float getBalanceMultiRoleTeamMembersWeight() {
        return balanceMultiRoleTeamMembersWeight;
    }

    public void setBalanceMultiRoleTeamMembersWeight(float balanceMultiRoleTeamMembersWeight) {
        this.balanceMultiRoleTeamMembersWeight = balanceMultiRoleTeamMembersWeight;
    }

    
    public Worker getPersonA() {
        return personA;
    }

    public void setPersonA(Worker personA) {
        this.personA = personA;
    }

    public Worker getPersonB() {
        return personB;
    }

    public void setPersonB(Worker personB) {
        this.personB = personB;
    }

    public List<SelectedWorkerIncompatibility> getIncompList() {
        return incompList;
    }

    public void setIncompList(List<SelectedWorkerIncompatibility> incompList) {
        this.incompList = incompList;
    }

    public Role getRoleA() {
        return roleA;
    }

    public void setRoleA(Role roleA) {
        this.roleA = roleA;
    }

    public Role getRoleB() {
        return roleB;
    }

    public void setRoleB(Role roleB) {
        this.roleB = roleB;
    }

    public List<SelectedRoleIncompatibility> getIncompRoleList() {
        return incompRoleList;
    }

    public void setIncompRoleList(List<SelectedRoleIncompatibility> incompRoleList) {
        this.incompRoleList = incompRoleList;
    }

    public List<SelectedRoleIncompatibility> getIncompRoleDelete() {
        return incompRoleDelete;
    }

    public void setIncompRoleDelete(List<SelectedRoleIncompatibility> incompRoleDelete) {
        this.incompRoleDelete = incompRoleDelete;
    }

    public Role getSelectedRol() {
        return selectedRol;
    }

    public void setSelectedRol(Role selectedRol) {
        this.selectedRol = selectedRol;
    }

    public boolean isMostrarPesos() {
        return mostrarPesos;
    }

    public void setMostrarPesos(boolean mostrarPesos) {
        this.mostrarPesos = mostrarPesos;
    }

    public List<Role> getSelectableRoles() {
        return selectableRoles;
    }

    public void setSelectableRoles(List<Role> selectableRoles) {
        this.selectableRoles = selectableRoles;
    }

    public float getBalanceCompetenceWeight() {
        return balanceCompetenceWeight;
    }

    public void setBalanceCompetenceWeight(float balanceCompetenceWeight) {
        this.balanceCompetenceWeight = balanceCompetenceWeight;
    }

    public float getBalanceWorkLoadWeight() {
        return balanceWorkLoadWeight;
    }

    public void setBalanceWorkLoadWeight(float balanceWorkLoadWeight) {
        this.balanceWorkLoadWeight = balanceWorkLoadWeight;
    }

    public float getBalanceSynergyWeight() {
        return balanceSynergyWeight;
    }

    public void setBalanceSynergyWeight(float balanceSynergyWeight) {
        this.balanceSynergyWeight = balanceSynergyWeight;
    }

    public float getBalanceCostDistanceWeight() {
        return balanceCostDistanceWeight;
    }

    public void setBalanceCostDistanceWeight(float balanceCostDistanceWeight) {
        this.balanceCostDistanceWeight = balanceCostDistanceWeight;
    }

    public float getBalanceInterestWeight() {
        return balanceInterestWeight;
    }

    public void setBalanceInterestWeight(float balanceInterestWeight) {
        this.balanceInterestWeight = balanceInterestWeight;
    }

    public float getBalanceBelbinWeight() {
        return balanceBelbinWeight;
    }

    public void setBalanceBelbinWeight(float balanceBelbinWeight) {
        this.balanceBelbinWeight = balanceBelbinWeight;
    }

    
    public int getActiveIndex() {
        return activeIndex;
    }

    public void setActiveIndex(int activeIndex) {
        this.activeIndex = activeIndex;
    }

    public boolean isBalanceProjectInterests() {
        return balanceProjectInterests;
    }

    public void setBalanceProjectInterests(boolean balanceProjectInterests) {
        this.balanceProjectInterests = balanceProjectInterests;
    }

    public boolean isBalanceMbtiTypes() {
        return balanceMbtiTypes;
    }

    public void setBalanceMbtiTypes(boolean balanceMbtiTypes) {
        this.balanceMbtiTypes = balanceMbtiTypes;
    }

    public float getBalanceProjectInterestWeight() {
        return balanceProjectInterestWeight;
    }

    public void setBalanceProjectInterestWeight(float balanceProjectInterestWeight) {
        this.balanceProjectInterestWeight = balanceProjectInterestWeight;
    }

    public float getBalanceMbtiTypesWeight() {
        return balanceMbtiTypesWeight;
    }

    public void setBalanceMbtiTypesWeight(float balanceMbtiTypesWeight) {
        this.balanceMbtiTypesWeight = balanceMbtiTypesWeight;
    }

    public List<FixedWorker> getFixedWorkersList() {
        return fixedWorkersList;
    }

    public void setFixedWorkersList(List<FixedWorker> fixedWorkersList) {
        this.fixedWorkersList = fixedWorkersList;
    }

    
    /**
     * se encarga de establecer todas las incompatibilidades establecidas
     * cuando se crean los roles. Esto se hace así porque no se
     * tiene en cuenta cuando se va a dar la solucion
     */
    public void initializeIncomp() {
        if (incompRoleList == null) {
            incompRoleList = new ArrayList<>();
        }
        List<Role> roles = roleFacade.findAll();
        roles.forEach(e -> e.getRoleList().forEach(r -> {
            incompRoleList.add(new SelectedRoleIncompatibility(e, r));
        }));
    }
}
