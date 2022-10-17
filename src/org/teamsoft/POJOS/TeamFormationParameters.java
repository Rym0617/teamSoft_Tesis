/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.POJOS;

import org.teamsoft.entity.*;
import problem.extension.TypeSolutionMethod;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author G1lb3rt
 */
public class TeamFormationParameters implements Serializable {


    /////////////////////////////////////////////////////////////////
    //////////////////////PASO 1/////////////////////////////////////
    /////////////////////////////////////////////////////////////////
    private ArrayList<PersonGroup> groupList = new ArrayList<>(); //area de busqueda
    private boolean confRole = false;
    public int maximunRoles = 0; //cantidad maxima de roles que puede asumir una persona
    private boolean confAllGroup = false;
    private boolean onlyOneProject = false; //cada persona se asigna a un solo proyecto
    private int confFormMode = 0;
    private ArrayList<PersonPerProjectAmount> ppg = new ArrayList();

    public boolean isMinimunRoles = false; //cantidad minima de roles a asumir una persona
    public int minimumRole = 1; //cantidad minima de roles a asumir una persona
    public boolean bossNeedToBeAssignedToAnotherRoles = false;
    //////////////////////////////////////////////////////////////////////////
    ///APLICAR ESTAS FUNCIONES OBJETIVO///////////////
    private boolean maxCompetences = false;
    private boolean maxInterests = false;
    private boolean maxProjectInterests = false;
    private boolean maxMbtiTypes = false;
    private boolean maxBelbinRoles = false;
    private boolean minIncomp = false;
    private boolean minCostDistance = false;
    private boolean takeWorkLoad = false;
    private boolean maxMultiroleTeamMembers = false;

    private boolean balanceCompetences = false;
    private boolean balanceInterests = false;
    private boolean balanceProjectInterests = false;
    private boolean balanceMbtiTypes = false;
    private boolean balanceBelbinRoles = false;
    private boolean balanceSynergy = false;
    private boolean balanceCostDistance = false;
    private boolean balancePersonWorkload = false;
    private boolean balanceMultiroleTeamMembers = false;

    //para la funcion maxCompetences: considerar los objetivos...
    private boolean maxCompetencesByProject = false; //maximizar las competencias por proyecto
    private float maxCompetencesByProjectWeight = 0; // peso asignado a las competencias por cada proyecto
    //    private boolean maxRoleExperience = false; //si se tiene en cuenta la experiencia en el rol
//    private float maxRoleExperienceWeight = 0; //peso dado a la experiencia en el desempeño del rol
    //para la experiencia en el desempe;o del rol
    private boolean noTimesInRol = false; //si se tiene en cuenta el numero de proyectos en que ha desempeñado el rol
    private boolean evaluationInRol = false; //si se tiene en cuenta la evaluacion en el desempeño del rol

    /////PESO PARA LAS FUNCIONES OBJETIVO///////////////
    private float maxCompetencesWeight = 0;
    private float maxInterestsWeight = 0;
    private float maxProjectInterestsWeight = 0;
    private float maxMbtiTypesWeight = 0;
    private float maxBelbinWeight = 0;
    private float minIncompWeight = 0;
    private float minCostDistanceWeight = 0;
    private float workLoadWeight = 0;
    private float maxMultiroleTeamMembersWeight = 0;

    private float balanceCompetenceWeight = 0;
    private float balanceWorkLoadWeight = 0;
    private float balanceSynergyWeight = 0;
    private float balanceCostDistanceWeight = 0;
    private float balanceInterestWeight = 0;
    private float balanceProjectInterestWeight = 0;
    private float balanceMbtiTypesWeight = 0;
    private float balanceBelbinWeight = 0;
    private float balanceMultiroleTeamMembersWeight = 0;

    private ArrayList<Worker> searchArea = new ArrayList<>();
    private ArrayList<FixedWorker> fixedWorkers = new ArrayList<>();
    /////////////////////////////////////////////////////////////////
    //////////////////////PASO 2 en adelante/////////////////////////
    ///////////////////////////////////////////////////////////////
    private List<ProjectRoleCompetenceTemplate> projects = new ArrayList<>(); //lista de proyectos seleccionados en paso 1 del wizard

    private Levels maxLevel = new Levels(); //maximo nivel de competencia declarado por la entidad
    private Levels minLevel = new Levels(); //minimo nivel de competencia declarado por la entidad

    private ConflictIndex maxConflictIndex = new ConflictIndex(); //maximo indice de conflicto
    private CostDistance maxCostDistance = new CostDistance(); // maximo costo de distancia

    //    private RoleEval maxEvaluation = new RoleEval(); //minima evaluacion definida para un rol
//    private RoleEval minEvaluation = new RoleEval(); //maxima evaluacion definida para un rol
    /////VARIABLES PARA LA PESTAÑA CARGA DE TRABAJO DEL ROL///////////////
    private boolean canBeProjectBoss = false; //Permitir o no que ya este asignado como jefe de proyecto
    private boolean takeCustomPersonWorkLoad = false; //definir la carga maxima que puede tener el trabajador
    private RoleLoad maxRoleLoad = new RoleLoad(); //Carga maxima que puede tener el trabajador

    ////////VARIABLES PARA LA PESTAÑA CARACTERÍSTICAS PSICOLÓGICAS//////////////////////////
    private boolean preferBelbin = false; // si prefiere los roles de belbin
    private boolean preferMyersBrigs = false; // si prefiere el subtipo E??J de Myers-Brigs
    private int cantCerebro = 0; //cantidad de min de personas con rol cerebro en el equipo

    ////////VARIABLES PARA LA PESTAÑA INTERÉS POR EL EQUIPO//////////////////////////
    private boolean bossTeamInterest = false;

    // para la solucion considerar 
    private final TypeSolutionMethod solutionMethodOptionBoss = TypeSolutionMethod.FactoresPonderados; // Factores pnderados siempre para jefe de proyecto

    //////////////////////////////////////////////////////////////////////////////
    ///////////////////////PASO 3//////////FORMACION DE EQUIPOS////////////////
    /////////////////////////////////////////////////////////////////////////////
    // para la solucion considerar 
    private TypeSolutionMethod solutionMethodOptionTeam = TypeSolutionMethod.FactoresPonderados; // Metoodo de solucion a usar "PONDERAR";PRIORIZAR;IGUALAR. Ponderar por defecto
    private int solutionAlgorithm; //Opcion de algoritmo de solucion a emplear (case 1: StochasticMutiObjetiveHillClimbing si factores ponderados y HillClimbing si Priorizar)

    //Sinergia
    private boolean anyIncompatibility = false; //no permitir ninguna incompatibilidad
    private boolean anySelectedIncompatibility = false; //no permitir ninguna incompatibilidad seleccionada
    private List<SelectedWorkerIncompatibility> sWI = new ArrayList<>(); //Incompatibilidades entre los Trabajadores
    private List<SelectedRoleIncompatibility> sRI = new ArrayList<>(); //Incompatibilidades entre los Roles

    ////////////////////////////TAB CARACTERISTICAS PSYCOLOGICAS////////////////////////////
    private boolean allBelbinRoles = false; ///exigir la precencia de todos los roles de Belbin
    private boolean demandNBrains = false;//al menis nBrains personas con el rol cerebro
    private int countBrains = 0;//cant nBrains personas con el rol cerebro
    private boolean balanceBelbinCategories = false; //balancear categorias de roles de belbin
    private boolean allBelbinCategories = false; //precensia de todas las categorias de Belbin
    private String actionMentalOper = "&gt;"; // más menos o igual numero de roles de accion que mentales (>) por defecto....("&gt;" equals to ">" )
    private String mentalSocialOper = "&gt;"; // más menos o igual numero de roles de mentales que sociales (>) por defecto....("&gt;" equals to ">" )

    /////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////
    public TeamFormationParameters() {
    }

    public float getMaxMbtiTypesWeight() {
        return maxMbtiTypesWeight;
    }

    public void setMaxMbtiTypesWeight(float maxMbtiTypesWeight) {
        this.maxMbtiTypesWeight = maxMbtiTypesWeight;
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

    public List<ProjectRoleCompetenceTemplate> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectRoleCompetenceTemplate> projects) {
        this.projects = projects;
    }

    public String getActionMentalOper() {
        return actionMentalOper;
    }

    public ArrayList<FixedWorker> getFixedWorkers() {
        return fixedWorkers;
    }

    public void setFixedWorkers(ArrayList<FixedWorker> fixedWorkers) {
        this.fixedWorkers = fixedWorkers;
    }

    public ArrayList<Worker> getSearchArea() {
        return searchArea;
    }

    public void setSearchArea(ArrayList<Worker> searchArea) {
        this.searchArea = searchArea;
    }

    public void setActionMentalOper(String actionMentalOper) {
        this.actionMentalOper = actionMentalOper;
    }

    public String getMentalSocialOper() {
        return mentalSocialOper;
    }

    public void setMentalSocialOper(String mentalSocialOper) {
        this.mentalSocialOper = mentalSocialOper;
    }

    public boolean isCanBeProjectBoss() {
        return canBeProjectBoss;
    }

    public void setCanBeProjectBoss(boolean canBeProjectBoss) {
        this.canBeProjectBoss = canBeProjectBoss;
    }

    public RoleLoad getMaxRoleLoad() {
        return maxRoleLoad;
    }

    public void setMaxRoleLoad(RoleLoad maxRoleLoad) {
        this.maxRoleLoad = maxRoleLoad;
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

    public int getCantCerebro() {
        return cantCerebro;
    }

    public void setCantCerebro(int cantCerebro) {
        this.cantCerebro = cantCerebro;
    }

    public ArrayList<PersonGroup> getGroupList() {
        return groupList;
    }

    public void setGroupList(ArrayList<PersonGroup> groupList) {
        this.groupList = groupList;
    }

    public boolean isConfRole() {
        return confRole;
    }

    public void setConfRole(boolean confRole) {
        this.confRole = confRole;
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

    public ArrayList<PersonPerProjectAmount> getPpg() {
        return ppg;
    }

    public void setPpg(ArrayList<PersonPerProjectAmount> ppg) {
        this.ppg = ppg;
    }

    public boolean isAllBelbinRoles() {
        return allBelbinRoles;
    }

    public void setAllBelbinRoles(boolean allBelbinRoles) {
        this.allBelbinRoles = allBelbinRoles;
    }

    public int getCountBrains() {
        return countBrains;
    }

    public boolean isDemandNBrains() {
        return demandNBrains;
    }

    public void setDemandNBrains(boolean demandNBrains) {
        this.demandNBrains = demandNBrains;
    }

    public void setCountBrains(int countBrains) {
        this.countBrains = countBrains;
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

    public int getConfFormMode() {
        return confFormMode;
    }

    public void setConfFormMode(int confFormMode) {
        this.confFormMode = confFormMode;
    }

    public int getSolutionAlgorithm() {
        return solutionAlgorithm;
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

    public void setSolutionAlgorithm(int solutionAlgorithm) {
        this.solutionAlgorithm = solutionAlgorithm;
    }

    public int getMaximunRoles() {
        return maximunRoles;
    }

    public void setMaximunRoles(int isEnable) {
        this.maximunRoles = isEnable;
    }

    public boolean isMinimunRoles() {
        return isMinimunRoles;
    }

    public void setMinimunRoles(boolean minuminRoles) {
        isMinimunRoles = minuminRoles;
    }

    public int getMinimumRole() {
        return minimumRole;
    }

    public void setMinimumRole(int minimumRole) {
        this.minimumRole = minimumRole;
    }

    public List<SelectedWorkerIncompatibility> getsWI() {
        return sWI;
    }

    public void setsWI(List<SelectedWorkerIncompatibility> sWI) {
        this.sWI = sWI;
    }

    public List<SelectedRoleIncompatibility> getsRI() {
        return sRI;
    }

    public void setsRI(List<SelectedRoleIncompatibility> sRI) {
        this.sRI = sRI;
    }

    public Levels getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(Levels maxLevel) {
        this.maxLevel = maxLevel;
    }

    public Levels getMinLevel() {
        return minLevel;
    }

    public void setMinLevel(Levels minLevel) {
        this.minLevel = minLevel;
    }

    public TypeSolutionMethod getSolutionMethodOptionBoss() {
        return solutionMethodOptionBoss;
    }

    public TypeSolutionMethod getSolutionMethodOptionTeam() {
        return solutionMethodOptionTeam;
    }

    public void setSolutionMethodOptionTeam(TypeSolutionMethod solutionMethodOptionTeam) {
        this.solutionMethodOptionTeam = solutionMethodOptionTeam;
    }

    public boolean isMaxCompetences() {
        return maxCompetences;
    }

    public void setMaxCompetences(boolean maxCompetences) {
        this.maxCompetences = maxCompetences;
    }

    public boolean isMaxInterests() {
        return maxInterests;
    }

    public void setMaxInterests(boolean maxInterests) {
        this.maxInterests = maxInterests;
    }

    public boolean isMaxProjectInterests() {
        return maxProjectInterests;
    }

    public void setMaxProjectInterests(boolean maxProjectInterests) {
        this.maxProjectInterests = maxProjectInterests;
    }

    public boolean isMaxBelbinRoles() {
        return maxBelbinRoles;
    }

    public void setMaxBelbinRoles(boolean maxBelbinRoles) {
        this.maxBelbinRoles = maxBelbinRoles;
    }

    public boolean isMaxMbtiTypes() {
        return maxMbtiTypes;
    }

    public void setMaxMbtiTypes(boolean maxMbtiTypes) {
        this.maxMbtiTypes = maxMbtiTypes;
    }

    public boolean isBalanceCompetences() {
        return balanceCompetences;
    }

    public void setBalanceCompetences(boolean balanceCompetences) {
        this.balanceCompetences = balanceCompetences;
    }

    public boolean isBalanceInterests() {
        return balanceInterests;
    }

    public void setBalanceInterests(boolean balanceInterests) {
        this.balanceInterests = balanceInterests;
    }

    public boolean isBalanceBelbinRoles() {
        return balanceBelbinRoles;
    }

    public void setBalanceBelbinRoles(boolean balanceBelbinRoles) {
        this.balanceBelbinRoles = balanceBelbinRoles;
    }

    public boolean isMaxCompetencesByProject() {
        return maxCompetencesByProject;
    }

    public void setMaxCompetencesByProject(boolean maxCompetencesByProject) {
        this.maxCompetencesByProject = maxCompetencesByProject;
    }

    public float getMaxCompetencesByProjectWeight() {
        return maxCompetencesByProjectWeight;
    }

    public void setMaxCompetencesByProjectWeight(float maxCompetencesByProjectWeight) {
        this.maxCompetencesByProjectWeight = maxCompetencesByProjectWeight;
    }

    //    public boolean isMaxRoleExperience() {
//        return maxRoleExperience;
//    }
    public boolean isTakeCustomPersonWorkLoad() {
        return takeCustomPersonWorkLoad;
    }

    public void setTakeCustomPersonWorkLoad(boolean takeCustomPersonWorkLoad) {
        this.takeCustomPersonWorkLoad = takeCustomPersonWorkLoad;
    }

    //    public void setMaxRoleExperience(boolean maxRoleExperience) {
//        this.maxRoleExperience = maxRoleExperience;
//    }
//    public float getMaxRoleExperienceWeight() {
//        return maxRoleExperienceWeight;
//    }
//
//    public void setMaxRoleExperienceWeight(float maxRoleExperienceWeight) {
//        this.maxRoleExperienceWeight = maxRoleExperienceWeight;
//    }
    public boolean isNoTimesInRol() {
        return noTimesInRol;
    }

    public void setNoTimesInRol(boolean noTimesInRol) {
        this.noTimesInRol = noTimesInRol;
    }

    public boolean isEvaluationInRol() {
        return evaluationInRol;
    }

    public void setEvaluationInRol(boolean evaluationInRol) {
        this.evaluationInRol = evaluationInRol;
    }

    public float getMaxCompetencesWeight() {
        return maxCompetencesWeight;
    }

    public void setMaxCompetencesWeight(float maxCompetencesWeight) {
        this.maxCompetencesWeight = maxCompetencesWeight;
    }

    public float getMaxInterestsWeight() {
        return maxInterestsWeight;
    }

    public void setMaxInterestsWeight(float maxInterestsWeight) {
        this.maxInterestsWeight = maxInterestsWeight;
    }

    public float getMaxProjectInterestsWeight() {
        return maxProjectInterestsWeight;
    }

    public void setMaxProjectInterestsWeight(float maxProjectInterestsWeight) {
        this.maxProjectInterestsWeight = maxProjectInterestsWeight;
    }

    public float getMaxBelbinWeight() {
        return maxBelbinWeight;
    }

    public void setMaxBelbinWeight(float maxBelbinWeight) {
        this.maxBelbinWeight = maxBelbinWeight;
    }

    public boolean isMinIncomp() {
        return minIncomp;
    }

    public void setMinIncomp(boolean minIncomp) {
        this.minIncomp = minIncomp;
    }

    public ConflictIndex getMaxConflictIndex() {
        return maxConflictIndex;
    }

    public void setMaxConflictIndex(ConflictIndex maxConflictIndex) {
        this.maxConflictIndex = maxConflictIndex;
    }

    public CostDistance getMaxCostDistance() {
        return maxCostDistance;
    }

    public void setMaxCostDistance(CostDistance maxCostDistance) {
        this.maxCostDistance = maxCostDistance;
    }

    public boolean isMinCostDistance() {
        return minCostDistance;
    }

    public void setMinCostDistance(boolean minCostDistance) {
        this.minCostDistance = minCostDistance;
    }

    public boolean isBalancePersonWorkload() {
        return balancePersonWorkload;
    }

    public void setBalancePersonWorkload(boolean balancePersonWorkload) {
        this.balancePersonWorkload = balancePersonWorkload;
    }

    public float getMinIncompWeight() {
        return minIncompWeight;
    }

    public void setMinIncompWeight(float minIncompWeight) {
        this.minIncompWeight = minIncompWeight;
    }

    public float getMinCostDistanceWeight() {
        return minCostDistanceWeight;
    }

    public void setMinCostDistanceWeight(float minCostDistanceWeight) {
        this.minCostDistanceWeight = minCostDistanceWeight;
    }

    public boolean isBalanceSynergy() {
        return balanceSynergy;
    }

    public void setBalanceSynergy(boolean balanceSynergy) {
        this.balanceSynergy = balanceSynergy;
    }

    public boolean isBalanceCostDistance() {
        return balanceCostDistance;
    }

    public void setBalanceCostDistance(boolean balanceCostDistance) {
        this.balanceCostDistance = balanceCostDistance;
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

    public boolean isTakeWorkLoad() {
        return takeWorkLoad;
    }

    public void setTakeWorkLoad(boolean takeWorkLoad) {
        this.takeWorkLoad = takeWorkLoad;
    }

    public float getWorkLoadWeight() {
        return workLoadWeight;
    }

    public void setWorkLoadWeight(float workLoadWeight) {
        this.workLoadWeight = workLoadWeight;
    }

    public boolean isBossNeedToBeAssignedToAnotherRoles() {
        return bossNeedToBeAssignedToAnotherRoles;
    }

    public void setBossNeedToBeAssignedToAnotherRoles(boolean bossNeedToBeAssignedToAnotherRoles) {
        this.bossNeedToBeAssignedToAnotherRoles = bossNeedToBeAssignedToAnotherRoles;
    }

    public boolean isMaxMultiroleTeamMembers() {
        return maxMultiroleTeamMembers;
    }

    public void setMaxMultiroleTeamMembers(boolean maxMultiroleTeamMembers) {
        this.maxMultiroleTeamMembers = maxMultiroleTeamMembers;
    }

    public boolean isBalanceMultiroleTeamMembers() {
        return balanceMultiroleTeamMembers;
    }

    public void setBalanceMultiroleTeamMembers(boolean balanceMultiroleTeamMembers) {
        this.balanceMultiroleTeamMembers = balanceMultiroleTeamMembers;
    }

    public float getMaxMultiroleTeamMembersWeight() {
        return maxMultiroleTeamMembersWeight;
    }

    public void setMaxMultiroleTeamMembersWeight(float maxMultiroleTeamMembersWeight) {
        this.maxMultiroleTeamMembersWeight = maxMultiroleTeamMembersWeight;
    }

    public float getBalanceMultiroleTeamMembersWeight() {
        return balanceMultiroleTeamMembersWeight;
    }

    public void setBalanceMultiroleTeamMembersWeight(float balanceMultiroleTeamMembersWeight) {
        this.balanceMultiroleTeamMembersWeight = balanceMultiroleTeamMembersWeight;
    }
    
    

    public int getPpgByProject(Project project) {
        return ppg.
                stream().
                filter(e -> e.getProj().equals(project)).
                findAny().
                get().
                getCant();
    }

    public boolean isBossTeamInterest() {
        return bossTeamInterest;
    }

    public void setBossTeamInterest(boolean bossTeamInterest) {
        this.bossTeamInterest = bossTeamInterest;
    }
}
