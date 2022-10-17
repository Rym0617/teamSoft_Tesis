package org.teamsoft.metaheuristics.restrictions;

import metaheurictics.strategy.Strategy;
import org.teamsoft.entity.Worker;
import org.teamsoft.entity.WorkerTest;
import org.teamsoft.metaheuristics.util.ProjectRole;
import org.teamsoft.metaheuristics.util.ProjectRoleState;
import org.teamsoft.metaheuristics.util.RoleWorker;
import org.teamsoft.metaheuristics.util.TeamFormationCodification;
import problem.definition.State;

import java.util.ArrayList;
import java.util.List;

/**
 * Al menos una persona con rol mental, una con rol de accion, y una con rol
 * social (en el equipo).
 */
public class AllBelbinCategories extends Constrain {

    public AllBelbinCategories() {
    }

    @Override
    public Boolean ValidateState(State state) {

        List<Object> projects = state.getCode(); //obtener lista de proyectos -roles

        int i = 0;
        boolean meet = true;
        while (i < projects.size() && meet) { //para cada projecto-rol
            ProjectRole projectRole = (ProjectRole) projects.get(i);
            List<RoleWorker> roleWorkers = projectRole.getRoleWorkers();

            List<Worker> team = new ArrayList<>(); //listado de personas del projecto

            int j = 0;
            while (j < roleWorkers.size() && meet) { //para cada rol-persona
                RoleWorker roleWorker = roleWorkers.get(j);
                List<Worker> aux = new ArrayList<>(); // concatenar listas de personas y personas fijadas por el usuario
                aux.addAll(roleWorker.getWorkers());
                aux.addAll(roleWorker.getFixedWorkers());

                team.addAll(aux); //añadir personas que juegan el rol actual a la lista de personas del proyecto

                j++;
            }
            meet = validateProject(team);
            i++;
        }
        return meet;

    }


    public void invalidateState(State state) {

        ProjectRoleState prState1 = new ProjectRoleState(state.getCode(), null, state.getTypeGenerator());
        ArrayList<Object> code = prState1.getCode();

        ProjectRole rp = ((ProjectRole) code.get(0));
        for (int j = 0; j < rp.getRoleWorkers().size(); j++) {
            RoleWorker rw = rp.getRoleWorkers().get(j);
            for (int k = 0; k < rw.getWorkers().size(); k++) {

                Worker worker = rw.getWorkers().get(k);
                WorkerTest workerTest = worker.getWorkerTest(); //obtener caracteristicas psicologicas

                if (workerTest != null) {
                    //De este modo garantizo que en el primer proyecto no hayan roles de acción

                    if (workerTest.getID() != 'I' && workerTest.getID() != 'E') {
                        ((ProjectRole) code.get(0)).getRoleWorkers().get(j).getWorkers().get(k).getWorkerTest().setID('I');
                    }
                    if (workerTest.getIS() != 'I' && workerTest.getIS() != 'E') {
                        ((ProjectRole) code.get(0)).getRoleWorkers().get(j).getWorkers().get(k).getWorkerTest().setIS('I');
                    }
                    if (workerTest.getIF() != 'I' && workerTest.getIF() != 'E') {
                        ((ProjectRole) code.get(0)).getRoleWorkers().get(j).getWorkers().get(k).getWorkerTest().setIF('I');
                    }
                }
            }
        }

    }


    public boolean validateProject(List<Worker> team) {

        boolean mentalRoles = false;
        boolean actionRoles = false;
        boolean socialRoles = false;

        int k = 0;
        boolean meet = false;
        while (k < team.size() && !meet) {  //para cada persona del equipo de proyecto actual
            Worker worker = team.get(k);
            WorkerTest workerTest = worker.getWorkerTest(); //obtener caracteristicas psicologicas

            if (workerTest != null) {

                if ((workerTest.getID() != 'I' && workerTest.getID() != 'E') || (workerTest.getIS() != 'I' && workerTest.getIS() != 'E') || (workerTest.getIF() != 'I' && workerTest.getIF() != 'E')) {
                    actionRoles = true;
                }
                if ((workerTest.getCE() != 'I' && workerTest.getCE() != 'E') || (workerTest.getME() != 'I' && workerTest.getME() != 'E') || (workerTest.getES() != 'I' && workerTest.getES() != 'E')) {
                    mentalRoles = true;
                }
                if ((workerTest.getCO() != 'I' && workerTest.getCO() != 'E') || (workerTest.getCH() != 'I' && workerTest.getCH() != 'E') || (workerTest.getIR() != 'I' && workerTest.getIR() != 'E')) {
                    socialRoles = true;
                }

                if (mentalRoles && actionRoles && socialRoles) {
                    meet = true;
                }
            }
            k++;
        }
        return meet;
    }

    public ArrayList<String> rolesQueHay(List<Worker> team) {
        ArrayList<String> rolesQHay = new ArrayList<>();

        for (Worker worker : team) {  //para cada persona del equipo de proyecto actual
            WorkerTest workerTest = worker.getWorkerTest(); //obtener caracteristicas psicologicas

            if (workerTest != null) {

                if ((workerTest.getID() != 'I' && workerTest.getID() != 'E') || (workerTest.getIS() != 'I' && workerTest.getIS() != 'E') || (workerTest.getIF() != 'I' && workerTest.getIF() != 'E')) {
                    rolesQHay.add("actionRoles");
                }
                if ((workerTest.getCE() != 'I' && workerTest.getCE() != 'E') || (workerTest.getME() != 'I' && workerTest.getME() != 'E') || (workerTest.getES() != 'I' && workerTest.getES() != 'E')) {
                    rolesQHay.add("mentalRoles");

                }
                if ((workerTest.getCO() != 'I' && workerTest.getCO() != 'E') || (workerTest.getCH() != 'I' && workerTest.getCH() != 'E') || (workerTest.getIR() != 'I' && workerTest.getIR() != 'E')) {
                    rolesQHay.add("socialRoles");
                }
            }
        }
        return rolesQHay;
    }

    public void RepareState(State state, List<Worker> team, int posProy) {

        ArrayList<String> rolesQHay = rolesQueHay(team);

        ProjectRoleState prState1 = new ProjectRoleState(state.getCode(), null, state.getTypeGenerator());
        ArrayList<Object> code = prState1.getCode();


        ArrayList<Worker> candidatos = new ArrayList<>();

        ArrayList<String> debenHaber = new ArrayList<String>() {{
            add("actionRoles");
            add("mentalRoles");
            add("socialRoles");
        }};

        ArrayList<String> rolesFaltan = new ArrayList<>();
        ArrayList<String> rolesRepetidos = new ArrayList<>();

        for (int i = 0; i < debenHaber.size(); i++) {
            int c = 0;
            for (String s : rolesQHay) {
                if (debenHaber.get(i).equals(s)) {
                    c++;
                }
                if (c > 1)
                    rolesRepetidos.add(rolesQHay.get(i));
            }
            if (c == 0) {
                rolesFaltan.add(rolesQHay.get(i));
            }
        }

        if (!rolesFaltan.isEmpty()) {
            candidatos = candidatos(rolesFaltan);

        }
        //tienen que haber candidatos suficientes para reparar la solucion y roles repetidos suficientes para sustituir
        if (candidatos.size() >= rolesFaltan.size() && rolesRepetidos.size() >= rolesFaltan.size()) {
            repareProjectSolution(state, posProy, rolesRepetidos, candidatos);

        }

    }

    public ArrayList<Worker> candidatos(ArrayList<String> rolesFaltan) {
        TeamFormationCodification codification = (TeamFormationCodification) Strategy.getStrategy().getProblem().getCodification();
        ArrayList<Worker> candidatos = new ArrayList<>();

        int i = 0;
        while (i < codification.getSearchArea().size() && (candidatos.size() < rolesFaltan.size())) {

            Worker worker = codification.getSearchArea().get(i);
            WorkerTest workerTest = worker.getWorkerTest(); //obtener caracteristicas psicologicas

            for (String s : rolesFaltan) {
                switch (s) {
                    case "actionRoles":
                        if ((workerTest.getID() != 'I' && workerTest.getID() != 'E') || (workerTest.getIS() != 'I' && workerTest.getIS() != 'E') || (workerTest.getIF() != 'I' && workerTest.getIF() != 'E')) {
                            candidatos.add(worker);
                        }
                        break;
                    case "mentalRoles":
                        if ((workerTest.getCE() != 'I' && workerTest.getCE() != 'E') || (workerTest.getME() != 'I' && workerTest.getME() != 'E') || (workerTest.getES() != 'I' && workerTest.getES() != 'E')) {
                            candidatos.add(worker);
                        }
                        break;
                    case "socialRoles":
                        if ((workerTest.getCO() != 'I' && workerTest.getCO() != 'E') || (workerTest.getCH() != 'I' && workerTest.getCH() != 'E') || (workerTest.getIR() != 'I' && workerTest.getIR() != 'E')) {
                            candidatos.add(worker);
                        }
                        break;
                }
            }
            i++;
        }
        return candidatos;
    }

    public void repareProjectSolution(State state, int posProy, ArrayList<String> rolesRepetidos, ArrayList<Worker> candidatos) {

        ProjectRole projectRole = (ProjectRole) state.getCode().get(posProy);
        List<RoleWorker> roleWorkers = projectRole.getRoleWorkers();
        int j = 0;
        while (j < roleWorkers.size() && !candidatos.isEmpty()) {
            RoleWorker rw = roleWorkers.get(j);
            for (int k = 0; k < rw.getWorkers().size(); k++) {
                Worker worker = rw.getWorkers().get(k);
                WorkerTest workerTest = worker.getWorkerTest(); //obtener caracteristicas psicologicas
                int a = 0;
                while (a < rolesRepetidos.size() && !candidatos.isEmpty()) {
                    switch (rolesRepetidos.get(a)) {
                        case "actionRoles":
                            if ((workerTest.getID() != 'I' && workerTest.getID() != 'E') || (workerTest.getIS() != 'I' && workerTest.getIS() != 'E') || (workerTest.getIF() != 'I' && workerTest.getIF() != 'E')) {
                                ((ProjectRole) state.getCode().get(posProy)).getRoleWorkers().get(j).getWorkers().set(k, candidatos.remove(0));
                            }
                            break;
                        case "mentalRoles":
                            if ((workerTest.getCE() != 'I' && workerTest.getCE() != 'E') || (workerTest.getME() != 'I' && workerTest.getME() != 'E') || (workerTest.getES() != 'I' && workerTest.getES() != 'E')) {
                                ((ProjectRole) state.getCode().get(posProy)).getRoleWorkers().get(j).getWorkers().set(k, candidatos.remove(0));
                            }
                            break;
                        case "socialRoles":
                            if ((workerTest.getCO() != 'I' && workerTest.getCO() != 'E') || (workerTest.getCH() != 'I' && workerTest.getCH() != 'E') || (workerTest.getIR() != 'I' && workerTest.getIR() != 'E')) {
                                ((ProjectRole) state.getCode().get(posProy)).getRoleWorkers().get(j).getWorkers().set(k, candidatos.remove(0));
                            }
                            break;
                    }
                    a++;
                }
            }
            j++;
        }

    }
}
