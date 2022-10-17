/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.metaheuristics.util;

import org.teamsoft.POJOS.TeamFormationParameters;
import org.teamsoft.entity.*;
import org.teamsoft.metaheuristics.objetives.*;
import problem.definition.ObjetiveFunction;
import problem.definition.Problem;
import problem.extension.TypeSolutionMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * @author G1lb3rt
 */
public class ObjetiveFunctionUtil {

    /**
     * Balancear la funcion objetivo
     *
     * @param totalSum
     * @return
     */
    public static double balance(List<Double> totalSum) {
        double balance = 0;
        double diference;
        double average = average(totalSum);

        for (Double item : totalSum) {
            diference = average - item;
            balance += diference > 0 ? diference : -diference;
        }
        return balance;
    }

    /**
     * Calcular el promedio de los valores de la funcion objetivo para cada
     * proyecto
     *
     * @param projectSum
     * @return
     */
    public static double average(List<Double> projectSum) {
        double average = -1;
        double total = 0;
        int count = 0;

        if (projectSum != null) {
            for (Double item : projectSum) {
                total += item;
                count++;
            }

            if (!projectSum.isEmpty()) {
                average = total / count;
            }
        }

        return average;
    }

    /**
     * Chequear si una persona posee un nivel minimo en una competencia dada una
     * lista de competences value
     *
     * @param personCompetences
     * @param comp
     * @param minValue
     * @return
     */
    public static boolean validatePersonCompetences(List<CompetenceValue> personCompetences, Competence comp, Long minValue) {

        boolean meet = false;

        int i = 0;
        boolean found = false;
        while (i < personCompetences.size() && !found) { //comprobar que la persona posee la competencia
            if (personCompetences.get(i).getCompetenceFk().getId().equals(comp.getId())) { //si es la competencia que se mide
                found = true;
                if (personCompetences.get(i).getLevelFk().getLevels() >= minValue) { // si posee el nivel minimo
                    meet = true;
                }
            }
            i++;
        }

        return meet;
    }

    /**
     * Obtener el nivel de una persona en una competencia dada
     *
     * @param personCompetences
     * @param comp
     * @return
     */
    public static long personCompetenceLevel(List<CompetenceValue> personCompetences, Competence comp) {

        long level = 0;

        int i = 0;
        boolean found = false;
        while (i < personCompetences.size() && !found) { //buscar la competencia
            if (personCompetences.get(i).getCompetenceFk().getId().equals(comp.getId())) { //si es la competencia deseada
                found = true;
                level = personCompetences.get(i).getLevelFk().getLevels();
            }
            i++;
        }

        return level;
    }

    /**
     * Calcular experiencia en el desempeño del rol.
     *
     * @param roleEvaluation
     * @param desireRole
     * @return
     */
    public static Long evaluationByRole(List<RoleEvaluation> roleEvaluation, Role desireRole) {
        long sum = 0;

        for (RoleEvaluation item : roleEvaluation) {
            if (desireRole.getId().equals(item.getRoleFk().getId())) {
                sum += item.getRolEvalFk().getLevels();
            }
        }
        return 1 - (1 / (1 + sum));
    }

    /**
     * Cantidad de veces que se ha desempeñado un rol.
     *
     * @param roleEvaluation
     * @param desireRole
     * @return
     */
    public static Long roleEvaluationCount(List<RoleEvaluation> roleEvaluation, Role desireRole) {
        long sum = 0;

        for (RoleEvaluation item : roleEvaluation) {
            if (desireRole.getId().equals(item.getRoleFk().getId())) {
                sum++;
            }
        }
        return sum;
    }

    /**
     * Obtener funciones objetivo
     *
     * @param parameters
     * @return
     */
    public static List<ObjetiveFunction> getObjectiveFunctions(TeamFormationParameters parameters) {

        List<ObjetiveFunction> objectiveFunctions = new ArrayList<>();

        if (parameters != null) {
            if (parameters.isMaxCompetences()) { //maximizar competencias
                MaximizeCompetency maxCompetence = new MaximizeCompetency();
                maxCompetence.setParameters(parameters);
                if (parameters.getSolutionMethodOptionBoss().equals(TypeSolutionMethod.FactoresPonderados)) {
                    maxCompetence.setWeight(parameters.getMaxCompetencesWeight());
                    maxCompetence.setTypeProblem(Problem.ProblemType.Maximizar);
                }
                objectiveFunctions.add(maxCompetence);
            }
            if (parameters.isMaxInterests()) {  //maximizar intereces por rol
                MaximizeInterests maxInterest = new MaximizeInterests();
                if (parameters.getSolutionMethodOptionBoss().equals(TypeSolutionMethod.FactoresPonderados)) {
                    maxInterest.setWeight(parameters.getMaxInterestsWeight());
                    maxInterest.setTypeProblem(Problem.ProblemType.Maximizar);
                }
                objectiveFunctions.add(maxInterest);
            }
            if (parameters.isMaxProjectInterests()) {  //maximizar intereces por proyecto rafiki
                MaximizeProjectInterests maxProjectInterest = new MaximizeProjectInterests();
                if (parameters.getSolutionMethodOptionBoss().equals(TypeSolutionMethod.FactoresPonderados)) {
                    maxProjectInterest.setWeight(parameters.getMaxProjectInterestsWeight());
                    maxProjectInterest.setTypeProblem(Problem.ProblemType.Maximizar);
                }
                objectiveFunctions.add(maxProjectInterest);
            }
            if (parameters.isMaxBelbinRoles()) {  //maximizar roles de belbin
                MaximizeBelbinRoles maxBelbinRoles = new MaximizeBelbinRoles();
                if (parameters.getSolutionMethodOptionBoss().equals(TypeSolutionMethod.FactoresPonderados)) {
                    maxBelbinRoles.setWeight(parameters.getMaxBelbinWeight());
                    maxBelbinRoles.setTypeProblem(Problem.ProblemType.Maximizar);
                }
                objectiveFunctions.add(maxBelbinRoles);
            }
           if (parameters.isMaxMbtiTypes()) {  //maximizar tipos MBTI
                MaximizeMbtiTypes maximizeMbtiTypes = new MaximizeMbtiTypes();
                if (parameters.getSolutionMethodOptionBoss().equals(TypeSolutionMethod.FactoresPonderados)) {
                    maximizeMbtiTypes.setWeight(parameters.getMaxMbtiTypesWeight());
                    maximizeMbtiTypes.setTypeProblem(Problem.ProblemType.Maximizar);
                }
                objectiveFunctions.add(maximizeMbtiTypes);
            }

            if (parameters.isMinIncomp()) {  //minimizar incompatibilidades
                MinimizeIncompatibilities minIncompatibilities = new MinimizeIncompatibilities(parameters);
                if (parameters.getSolutionMethodOptionBoss().equals(TypeSolutionMethod.FactoresPonderados)) {
                    minIncompatibilities.setWeight(parameters.getMinIncompWeight());
                    minIncompatibilities.setTypeProblem(Problem.ProblemType.Minimizar);
                }
                objectiveFunctions.add(minIncompatibilities);
            }

            if (parameters.isMinCostDistance()) {  //minimizar costo por distancia
                MinimizeCostDistance minCostDistance = new MinimizeCostDistance(parameters);
                if (parameters.getSolutionMethodOptionBoss().equals(TypeSolutionMethod.FactoresPonderados)) {
                    minCostDistance.setWeight(parameters.getMinCostDistanceWeight());
                    minCostDistance.setTypeProblem(Problem.ProblemType.Minimizar);
                }
                objectiveFunctions.add(minCostDistance);
            }

            if (parameters.isTakeWorkLoad()) {  //balancear carga de trabajo personal
                MinimizeWorkload minimizeWorkload = new MinimizeWorkload();
                minimizeWorkload.setParameters(parameters);
                if (parameters.getSolutionMethodOptionBoss().equals(TypeSolutionMethod.FactoresPonderados)) {
                    minimizeWorkload.setWeight(parameters.getWorkLoadWeight());
                    minimizeWorkload.setTypeProblem(Problem.ProblemType.Minimizar);
                }
                objectiveFunctions.add(minimizeWorkload);
            }
            if (parameters.isMaxMultiroleTeamMembers()) { //maximizar competencias
                MaximizeMultiroleTeamMembers maxMultiroleTeamMembers = new MaximizeMultiroleTeamMembers();
                maxMultiroleTeamMembers.setParameters(parameters);
                if (parameters.getSolutionMethodOptionBoss().equals(TypeSolutionMethod.FactoresPonderados)) {
                    maxMultiroleTeamMembers.setWeight(parameters.getMaxMultiroleTeamMembersWeight());
                    maxMultiroleTeamMembers.setTypeProblem(Problem.ProblemType.Maximizar);
                }
                objectiveFunctions.add(maxMultiroleTeamMembers);
            }

            //PARA BALANCEAR ENTRE EQUIPOS
            if (parameters.isBalanceCompetences()) { //balancear las competencias
                BalanceMaximizeCompetency maxCompetency = new BalanceMaximizeCompetency();
                maxCompetency.setParameters(parameters);
                if (parameters.getSolutionMethodOptionBoss().equals(TypeSolutionMethod.FactoresPonderados)) {
                    maxCompetency.setWeight(parameters.getBalanceCompetenceWeight());
                    maxCompetency.setTypeProblem(Problem.ProblemType.Minimizar);
                }
                objectiveFunctions.add(maxCompetency);
            }
            if (parameters.isBalanceInterests()) { //balancear los intereces por rol
                BalanceMaximizeInterests maxInterest = new BalanceMaximizeInterests();
                if (parameters.getSolutionMethodOptionBoss().equals(TypeSolutionMethod.FactoresPonderados)) {
                    maxInterest.setWeight(parameters.getBalanceInterestWeight());
                    maxInterest.setTypeProblem(Problem.ProblemType.Minimizar);
                }
                objectiveFunctions.add(maxInterest);
            }
             if (parameters.isBalanceProjectInterests()) { //balancear los intereses por proyecto
                BalanceMaximizeProjectInterests maxProjectInterest = new BalanceMaximizeProjectInterests();
                if (parameters.getSolutionMethodOptionBoss().equals(TypeSolutionMethod.FactoresPonderados)) {
                    maxProjectInterest.setWeight(parameters.getBalanceProjectInterestWeight());
                    maxProjectInterest.setTypeProblem(Problem.ProblemType.Maximizar);
                }
                objectiveFunctions.add(maxProjectInterest);
            }
            if (parameters.isBalanceMbtiTypes()) { //balancear los tipos mbti
                BalanceMaximizeMbtiTypes maxMbtiTypes = new BalanceMaximizeMbtiTypes();
                if (parameters.getSolutionMethodOptionBoss().equals(TypeSolutionMethod.FactoresPonderados)) {
                    maxMbtiTypes.setWeight(parameters.getBalanceMbtiTypesWeight());
                    maxMbtiTypes.setTypeProblem(Problem.ProblemType.Maximizar);
                }
                objectiveFunctions.add(maxMbtiTypes);
            }
            if (parameters.isBalanceBelbinRoles()) { //balancear los roles de belbin
                BalanceMaximizeBelbinRoles maxBelbinRoles = new BalanceMaximizeBelbinRoles();
                if (parameters.getSolutionMethodOptionBoss().equals(TypeSolutionMethod.FactoresPonderados)) {
                    maxBelbinRoles.setWeight(parameters.getBalanceBelbinWeight());
                    maxBelbinRoles.setTypeProblem(Problem.ProblemType.Minimizar);
                }
                objectiveFunctions.add(maxBelbinRoles);
            }
            if (parameters.isBalanceCostDistance()) { //balancear costo por distancia
                BalanceMinimizeCostDistance minCostDistance = new BalanceMinimizeCostDistance(parameters);
                if (parameters.getSolutionMethodOptionBoss().equals(TypeSolutionMethod.FactoresPonderados)) {
                    minCostDistance.setWeight(parameters.getBalanceCostDistanceWeight());
                    minCostDistance.setTypeProblem(Problem.ProblemType.Minimizar);
                }
                objectiveFunctions.add(minCostDistance);
            }
            if (parameters.isBalanceSynergy()) { //balancear costo por distancia
                BalanceMinimizeIncompatibilities minIncompatibilities = new BalanceMinimizeIncompatibilities(parameters);
                if (parameters.getSolutionMethodOptionBoss().equals(TypeSolutionMethod.FactoresPonderados)) {
                    minIncompatibilities.setWeight(parameters.getBalanceSynergyWeight());
                    minIncompatibilities.setTypeProblem(Problem.ProblemType.Minimizar);
                }
                objectiveFunctions.add(minIncompatibilities);
            }

            if (parameters.isBalancePersonWorkload()) {  //balancear carga de trabajo personal
                BalanceMinimizeWorkload minWorkload = new BalanceMinimizeWorkload();
                minWorkload.setParameters(parameters);
                if (parameters.getSolutionMethodOptionBoss().equals(TypeSolutionMethod.FactoresPonderados)) {
                    minWorkload.setWeight(parameters.getBalanceWorkLoadWeight());
                    minWorkload.setTypeProblem(Problem.ProblemType.Minimizar);
                }
                objectiveFunctions.add(minWorkload);
            }
        }
        //COMMENT

        return objectiveFunctions;
    }

    public static List<Worker> ProjectWorkers(ProjectRole projectRole) {
        List<Worker> projectWorkers = new ArrayList<>(0);
        List<RoleWorker> roleWorkers = projectRole.getRoleWorkers();

        int i = 0;
        while (i < roleWorkers.size()) {
            RoleWorker roleWorker = roleWorkers.get(i);
            List<Worker> workers = new ArrayList<>(0);
            workers.addAll(roleWorker.getWorkers());
            workers.addAll(roleWorker.getFixedWorkers());

            int j = 0;
            while (j < workers.size()) {
                Worker worker = workers.get(j);

                int k = 0;
                boolean found = false;
                while (k < projectWorkers.size() && !found) {
                    if (projectWorkers.get(k).getId().equals(worker.getId())) {
                        found = true;
                    }
                    k++;
                }
                if (found == false) {
                    projectWorkers.add(worker);
                }
                j++;
            }
            i++;
        }
        return projectWorkers;
    }

    public static int getIdentificator(String className) {
        int id = 0;
        switch (className) {
            case "Competencias": //Maximizar las competencias
                id = 1;
                break;
            case "BalanceCompetencias": //Balancear el índice de competencias
                id = 2;
                break;
            case "Incompatibilidades": //Maximizar la sinergia entre los miembros de los equipos
                id = 3;
                break;
            case "BalanceIncompatibilidades": //Balancear el índice de incompatibilidades
                id = 4;
                break;
            case "CargaTrabajo": //Minimizar la carga de trabajo
                id = 5;
                break;
            case "BalanceCargaTrabajo": //Balancear la carga de trabajo
                id = 6;
                break;
            case "CostoDistancia": //Minimizar el costo de trabajar a distancia
                id = 7;
                break;
            case "BalanceCostoDistancia": //Balancear el costo de trabajar a distancia
                id = 8;
                break;
            case "RolesBelbin": //Maximizar la presencia de los diferentes roles de Belbin
                id = 9;
                break;
            case "BalanceRolesBelbin": //Balancear la presencia de los diferentes roles de Belbin
                id = 10;
                break;
            case "Intereses": //Maximizar los intereses de las personas por desempeñar el rol
                id = 11;
                break;
            case "BalanceIntereses": //Balancear los intereses de las personas por desempeñar el rol
                id = 12;
                break;
            case "ProjectIntereses": //Maximizar el interés de las personas por trabajar en el proyecto
                id = 13;
                break;
            case "BalanceProjectInterests": //Balancear el interés de las personas en trabajar en el (los) proyecto(s)
                id = 14;
                break;
            case "TiposMBTI": //Maximizar la presencia de personas con tipos psicológicos, según el test MBTI
                id = 15;
                break;
            case "BalanceTiposMBTI":
                id = 16; //Balancear la presencia de personas con tipos psicológicos, según test MBTI
                break;
            case "Multirol":
                id = 17; // Maximizar las personas capaces de ocupar mas roles en el equipo
                break;

        }
        return id;
    }


}
