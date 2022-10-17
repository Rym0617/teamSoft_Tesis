/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.metaheuristics.objetives;

import org.teamsoft.entity.Worker;
import org.teamsoft.entity.WorkerTest;
import org.teamsoft.metaheuristics.util.ProjectRole;
import org.teamsoft.metaheuristics.util.RoleWorker;
import problem.definition.ObjetiveFunction;
import problem.definition.State;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Link
 */
public class BalanceMaximizeMbtiTypes extends ObjetiveFunction {

    public static String className = "BalanceTiposMBTI";

    @Override
    public Double Evaluation(State state) {

        List<Object> projects = state.getCode(); // obtener lista de proyectos -roles
        double sum = 0;
        double mbtiProject = 0;
        ArrayList<Double> mbtiTeamReason = new ArrayList<>(); //guarda la razón de la cantidad de tipos MBTI en cada equipo

        int i = 0;
        int totalPersons = 0;
        while (i < projects.size()) { //para cada projecto-rol
            ProjectRole projectRole = (ProjectRole) projects.get(i);
            List<RoleWorker> roleWorkers = projectRole.getRoleWorkers();
            mbtiProject = 0;
            List<Worker> verifiedWorkers = new LinkedList<>();//lista para verificar que no se repita ningun trabajador entre roles del mismo proyecto

            List<Worker> team = new ArrayList<>(); //listado de personas del projecto

            int j = 0;
            while (j < roleWorkers.size()) { //para cada rol-persona
                RoleWorker roleWorker = roleWorkers.get(j);
                roleWorker.getWorkers().addAll(roleWorker.getFixedWorkers());  // concatenar listas de personas y personas fijadas por el usuario
                team.addAll(roleWorker.getWorkers()); //añadir personas que juegan el rol actual a la lista de personas del proyecto

                j++;
            }

            int ISTJ = 0;
            int ISFJ = 0;
            int INTJ = 0;
            int INFJ = 0;
            int ISTP = 0;
            int ISFP = 0;
            int INTP = 0;
            int INFP = 0;
            int ESTJ = 0;
            int ESFJ = 0;
            int ENTJ = 0;
            int ENFJ = 0;
            int ESTP = 0;
            int ESFP = 0;
            int ENTP = 0;
            int ENFP = 0;

            boolean found;
            int k = 0;
            while (k < team.size()) {  //para cada persona del equipo de proyecto actual
                found = false;
                Worker worker = team.get(k);
                for (int l = 0; l < verifiedWorkers.size(); l++) {
                    if (worker == verifiedWorkers.get(l)) {
                        found = true;
                    }
                }
                if (!found) {
                    verifiedWorkers.add(worker);
                    WorkerTest workerTest = worker.getWorkerTest(); //obtener caracteristicas psicologicas

                    if (workerTest != null) {
                        if (workerTest.getTipoMB().equalsIgnoreCase("ISTJ") && ISTJ == 0) {
                            ISTJ = 1;
                        }
                        if (workerTest.getTipoMB().equalsIgnoreCase("ISFJ") && ISFJ == 0) {
                            ISFJ = 1;
                        }
                        if (workerTest.getTipoMB().equalsIgnoreCase("INTJ") && INTJ == 0) {
                            INTJ = 1;
                        }
                        if (workerTest.getTipoMB().equalsIgnoreCase("INFJ") && INFJ == 0) {
                            INFJ = 1;
                        }
                        if (workerTest.getTipoMB().equalsIgnoreCase("ISTP") && ISTP == 0) {
                            ISTP = 1;
                        }
                        if (workerTest.getTipoMB().equalsIgnoreCase("ISFP") && ISFP == 0) {
                            ISFP = 1;
                        }
                        if (workerTest.getTipoMB().equalsIgnoreCase("INTP") && INTP == 0) {
                            INTP = 1;
                        }
                        if (workerTest.getTipoMB().equalsIgnoreCase("INFP") && INFP == 0) {
                            INFP = 1;
                        }
                        if (workerTest.getTipoMB().equalsIgnoreCase("ESTJ") && ESTJ == 0) {
                            ESTJ = 1;
                        }
                        if (workerTest.getTipoMB().equalsIgnoreCase("ESFJ") && ESFJ == 0) {
                            ESFJ = 1;
                        }
                        if (workerTest.getTipoMB().equalsIgnoreCase("ENTJ") && ENTJ == 0) {
                            ENTJ = 1;
                        }
                        if (workerTest.getTipoMB().equalsIgnoreCase("ENFJ") && ENFJ == 0) {
                            ENFJ = 1;
                        }
                        if (workerTest.getTipoMB().equalsIgnoreCase("ESTP") && ESTP == 0) {
                            ESTP = 1;
                        }
                        if (workerTest.getTipoMB().equalsIgnoreCase("ESFP") && ESFP == 0) {
                            ESFP = 1;
                        }
                        if (workerTest.getTipoMB().equalsIgnoreCase("ENTP") && ENTP == 0) {
                            ENTP = 1;
                        }
                        if (workerTest.getTipoMB().equalsIgnoreCase("ENFP") && ENFP == 0) {
                            ENFP = 1;
                        }
                    }
                }
                k++;
            }
            mbtiProject = ISTJ + ISFJ + INTJ + INFJ + ISTP + ISFP + INTP + INFP + ESTJ + ESFJ + ENTJ + ENFJ + ESTP + ESFP + ENTP + ENFP;
            //.out.println("MBTI PROJECT: " + mbtiProject);
            sum += mbtiProject;
            totalPersons += verifiedWorkers.size();
            //if (!projectRole.getProject().getProjectName().contains("MBTI_Types")) {
            //    projectRole.getProject().setProjectName(projectRole.getProject().getProjectName() + " MBTI_Types: " + mbtiProject / verifiedWorkers.size());
            //}
            mbtiTeamReason.add(mbtiProject / verifiedWorkers.size());
            //System.out.println("MBTI_Types:" + mbtiProject / verifiedWorkers.size());

            i++;
        }
        double mediaTotal = 0; //media de las razones de tipos MBTI entre todos los equipos
        for (i = 0; i < mbtiTeamReason.size(); i++) {
            mediaTotal += mbtiTeamReason.get(i);
        }
        mediaTotal /= mbtiTeamReason.size();

        double balance = 0;
        //calcular el valor modular de la diferencia entre la razón de cantidad de tipos MBTI en cada equipo con respecto a la media total de dicha razón
        //luego sumar todas las diferencias y así obtener el balance
        for (i = 0; i < mbtiTeamReason.size(); i++) {
            balance += Math.abs(mbtiTeamReason.get(i) - mediaTotal);
        }

        //System.out.println("Total Propuesta = " + sum + "\n"); //Eliminar
        //System.out.println("Maximo Posible = " + totalPersons + "\n"); //Eliminar
        sum = (sum) / (totalPersons);
        //sum = ((totalPersons - sum) / (totalPersons - 1));
        //System.out.println("Indice Propuesta = " + sum + "\n"); //Eliminar
        return balance;
    }
}
