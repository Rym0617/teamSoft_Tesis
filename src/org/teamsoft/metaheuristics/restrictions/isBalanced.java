package org.teamsoft.metaheuristics.restrictions;

import metaheurictics.strategy.Strategy;
import org.teamsoft.POJOS.TeamFormationParameters;
import org.teamsoft.entity.Worker;
import org.teamsoft.entity.WorkerTest;
import org.teamsoft.metaheuristics.util.ProjectRole;
import org.teamsoft.metaheuristics.util.RoleWorker;
import org.teamsoft.metaheuristics.util.TeamFormationCodification;
import problem.definition.State;

import java.util.ArrayList;
import java.util.List;

import static org.teamsoft.metaheuristics.util.TeamFormationCodification.actualiceTeam;

/**
 * Que en el equipo estén presentes más roles de acción que mentales y más
 * mentales que sociales... o lo que se seleccione en la GUI ( mayor o menor
 * estricto )
 */
public class isBalanced extends Constrain {

    private TeamFormationParameters parameters;

    public TeamFormationParameters getParameters() {
        return parameters;
    }

    public void setParameters(TeamFormationParameters parameters) {
        this.parameters = parameters;
    }

    public isBalanced(TeamFormationParameters parameters) {
        this.parameters = parameters;
    }

    public isBalanced() {
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

            meet = validateProyect(team);
            i++;
        }
        return meet;
    }

    public boolean validateProyect(List<Worker> team) {
        boolean meet = false;
        String actionMentalOper = parameters.getActionMentalOper(); // operador binario para roles de accion/mentales
        String mentalSocialOper = parameters.getMentalSocialOper(); // operador binario para roles de mentales/sociales

        int countMentalRoles = 0;
        int countActionRoles = 0;
        int countSocialRoles = 0;
        int k = 0;
        while (k < team.size() && !meet) {  //para cada persona del equipo de proyecto actual
            Worker worker = team.get(k);
            WorkerTest workerTest = worker.getWorkerTest(); //obtener caracteristicas psicologicas

            if (workerTest != null) {

                if ((workerTest.getID() != 'I' && workerTest.getID() != 'E') || (workerTest.getIS() != 'I' && workerTest.getIS() != 'E') || (workerTest.getIF() != 'I' && workerTest.getIF() != 'E')) {
                    countActionRoles++;
                }
                if ((workerTest.getCE() != 'I' && workerTest.getCE() != 'E') || (workerTest.getME() != 'I' && workerTest.getME() != 'E') || (workerTest.getES() != 'I' && workerTest.getES() != 'E')) {
                    countMentalRoles++;
                }
                if ((workerTest.getCO() != 'I' && workerTest.getCO() != 'E') || (workerTest.getCH() != 'I' && workerTest.getCH() != 'E') || (workerTest.getIR() != 'I' && workerTest.getIR() != 'E')) {
                    countSocialRoles++;
                }

                boolean actionMental = false;
                boolean mentalSocial = false;

                switch (actionMentalOper) {
                    case "==": {
                        actionMental = countActionRoles == countMentalRoles;
                        break;
                    }
                    case ">":
                        actionMental = countActionRoles > countMentalRoles;
                        break;
                    case "<":
                        actionMental = countActionRoles < countMentalRoles;
                        break;
                    default:
                        break;
                }

                switch (mentalSocialOper) {
                    case "==":
                        mentalSocial = countMentalRoles == countSocialRoles;
                        break;
                    case ">":
                        mentalSocial = countMentalRoles > countSocialRoles;
                        break;
                    case "<":
                        mentalSocial = countMentalRoles < countSocialRoles;
                        break;
                    default:
                        break;
                }

                if (actionMental && mentalSocial) {
                    meet = true;
                }
            }
            k++;
        }
        return meet;
    }

    public void repareProjectSolution(State state, int posProy, ArrayList<Worker> candidatos, String seaRol, String noSeaRol) {
        ProjectRole projectRole = (ProjectRole) state.getCode().get(posProy);
        List<RoleWorker> roleWorkers = projectRole.getRoleWorkers();
        int j = 0;
        boolean meet = false;
        while (j < roleWorkers.size() && !candidatos.isEmpty()) {
            RoleWorker rw = roleWorkers.get(j);
            int k = 0;
            while (k < rw.getWorkers().size() && !candidatos.isEmpty()) {
                Worker worker = rw.getWorkers().get(k);
                WorkerTest workerTest = worker.getWorkerTest(); //obtener caracteristicas psicologicas
                switch (seaRol) {
                    case "MentalRoles": {
                        boolean mr = ((workerTest.getCE() != 'I' && workerTest.getCE() != 'E') || (workerTest.getME() != 'I' && workerTest.getME() != 'E') || (workerTest.getES() != 'I' && workerTest.getES() != 'E'));

                        switch (noSeaRol) {
                            case "": {
                                if (mr) {
                                    Worker wor = candidatos.remove(0);
                                    ((ProjectRole) state.getCode().get(posProy)).getRoleWorkers().get(j).getWorkers().set(k, wor);
                                }
                                break;
                            }
                            case "ActionRoles":
                                boolean ar = ((workerTest.getID() != 'I' && workerTest.getID() != 'E') || (workerTest.getIS() != 'I' && workerTest.getIS() != 'E') || (workerTest.getIF() != 'I' && workerTest.getIF() != 'E'));
                                if (mr && !ar) {
                                    Worker wor = candidatos.remove(0);
                                    ((ProjectRole) state.getCode().get(posProy)).getRoleWorkers().get(j).getWorkers().set(k, wor);
                                }

                                break;
                            case "SocialRoles":
                                boolean sr = ((workerTest.getCO() != 'I' && workerTest.getCO() != 'E') || (workerTest.getCH() != 'I' && workerTest.getCH() != 'E') || (workerTest.getIR() != 'I' && workerTest.getIR() != 'E'));
                                if (!sr && mr) {
                                    Worker wor = candidatos.remove(0);
                                    ((ProjectRole) state.getCode().get(posProy)).getRoleWorkers().get(j).getWorkers().set(k, wor);
                                }

                                break;
                            default:
                                break;
                        }
                        break;
                    }
                    case "ActionRoles": {
                        boolean ar = ((workerTest.getID() != 'I' && workerTest.getID() != 'E') || (workerTest.getIS() != 'I' && workerTest.getIS() != 'E') || (workerTest.getIF() != 'I' && workerTest.getIF() != 'E'));

                        switch (noSeaRol) {
                            case "": {
                                if (ar) {
                                    Worker wor = candidatos.remove(0);
                                    ((ProjectRole) state.getCode().get(posProy)).getRoleWorkers().get(j).getWorkers().set(k, wor);
                                }
                                break;
                            }
                            case "MentalRoles":
                                boolean mr = ((workerTest.getCE() != 'I' && workerTest.getCE() != 'E') || (workerTest.getME() != 'I' && workerTest.getME() != 'E') || (workerTest.getES() != 'I' && workerTest.getES() != 'E'));
                                if (!mr && ar) {
                                    Worker wor = candidatos.remove(0);
                                    ((ProjectRole) state.getCode().get(posProy)).getRoleWorkers().get(j).getWorkers().set(k, wor);
                                }

                                break;
                            case "SocialRoles":
                                boolean sr = ((workerTest.getCO() != 'I' && workerTest.getCO() != 'E') || (workerTest.getCH() != 'I' && workerTest.getCH() != 'E') || (workerTest.getIR() != 'I' && workerTest.getIR() != 'E'));
                                if (!sr && ar) {
                                    Worker wor = candidatos.remove(0);
                                    ((ProjectRole) state.getCode().get(posProy)).getRoleWorkers().get(j).getWorkers().set(k, wor);
                                }

                                break;
                            default:
                                break;
                        }

                        break;
                    }
                    case "SocialRoles": {

                        boolean sr = ((workerTest.getCO() != 'I' && workerTest.getCO() != 'E') || (workerTest.getCH() != 'I' && workerTest.getCH() != 'E') || (workerTest.getIR() != 'I' && workerTest.getIR() != 'E'));

                        switch (noSeaRol) {
                            case "": {
                                if (sr) {
                                    Worker wor = candidatos.remove(0);
                                    ((ProjectRole) state.getCode().get(posProy)).getRoleWorkers().get(j).getWorkers().set(k, wor);
                                }
                                break;
                            }
                            case "MentalRoles":
                                boolean mr = ((workerTest.getCE() != 'I' && workerTest.getCE() != 'E') || (workerTest.getME() != 'I' && workerTest.getME() != 'E') || (workerTest.getES() != 'I' && workerTest.getES() != 'E'));
                                if (!mr && sr) {
                                    Worker wor = candidatos.remove(0);
                                    ((ProjectRole) state.getCode().get(posProy)).getRoleWorkers().get(j).getWorkers().set(k, wor);
                                }

                                break;
                            case "ActionRoles":
                                boolean ar = ((workerTest.getID() != 'I' && workerTest.getID() != 'E') || (workerTest.getIS() != 'I' && workerTest.getIS() != 'E') || (workerTest.getIF() != 'I' && workerTest.getIF() != 'E'));
                                if (sr && !ar) {
                                    Worker wor = candidatos.remove(0);
                                    ((ProjectRole) state.getCode().get(posProy)).getRoleWorkers().get(j).getWorkers().set(k, wor);
                                }

                                break;
                            default:
                                break;
                        }

                        break;
                    }
                    case "": {
                        switch (noSeaRol) {

                            case "SocialRoles": {
                                boolean sr = ((workerTest.getCO() != 'I' && workerTest.getCO() != 'E') || (workerTest.getCH() != 'I' && workerTest.getCH() != 'E') || (workerTest.getIR() != 'I' && workerTest.getIR() != 'E'));

                                if (!sr) {
                                    Worker wor = candidatos.remove(0);
                                    ((ProjectRole) state.getCode().get(posProy)).getRoleWorkers().get(j).getWorkers().set(k, wor);
                                }
                                break;
                            }
                            case "MentalRoles":
                                boolean mr = ((workerTest.getCE() != 'I' && workerTest.getCE() != 'E') || (workerTest.getME() != 'I' && workerTest.getME() != 'E') || (workerTest.getES() != 'I' && workerTest.getES() != 'E'));
                                if (!mr) {
                                    Worker wor = candidatos.remove(0);
                                    ((ProjectRole) state.getCode().get(posProy)).getRoleWorkers().get(j).getWorkers().set(k, wor);
                                }

                                break;
                            case "ActionRoles":
                                boolean ar = ((workerTest.getID() != 'I' && workerTest.getID() != 'E') || (workerTest.getIS() != 'I' && workerTest.getIS() != 'E') || (workerTest.getIF() != 'I' && workerTest.getIF() != 'E'));
                                if (!ar) {
                                    Worker wor = candidatos.remove(0);
                                    ((ProjectRole) state.getCode().get(posProy)).getRoleWorkers().get(j).getWorkers().set(k, wor);
                                }

                                break;
                            default:
                                break;
                        }

                        break;
                    }

                    default:
                        break;

                }
                k++;
            }
            j++;
        }

    }

    public void repareState(State state, List<Worker> team, int posProy) {

        String actionMentalOper = parameters.getActionMentalOper(); // operador binario para roles de accion/mentales
        String mentalSocialOper = parameters.getMentalSocialOper(); // operador binario para roles de mentales/sociales

        int countMentalRoles = contarCategoriaRoles(team).get(0);
        int countActionRoles = contarCategoriaRoles(team).get(1);
        int countSocialRoles = contarCategoriaRoles(team).get(2);
        ArrayList<Worker> candidatos;

        int faltanAM = Math.abs(countActionRoles - countMentalRoles);

        switch (actionMentalOper) {
            case "==": {
                if (countActionRoles > countMentalRoles) {
                    candidatos = buscarCandidatos(faltanAM, "MentalRoles", "ActionRoles", 2);
                    repareProjectSolution(state, posProy, candidatos, "ActionRoles", "MentalRoles");
                } else if (countActionRoles < countMentalRoles) {
                    candidatos = buscarCandidatos(faltanAM, "ActionRoles", "MentalRoles", 2);
                    repareProjectSolution(state, posProy, candidatos, "MentalRoles", "ActionRoles");
                }

                break;
            }
            case ">":
                if (countActionRoles == countMentalRoles) {
                    candidatos = buscarCandidatos(faltanAM, "ActionRoles", "MentalRoles", 1);
                    repareProjectSolution(state, posProy, candidatos, "MentalRoles", "");
                } else if (countActionRoles < countMentalRoles) {
                    candidatos = buscarCandidatos(1, "ActionRoles", "MentalRoles", 1);
                    repareProjectSolution(state, posProy, candidatos, "MentalRoles", "ActionRoles");
                    candidatos = buscarCandidatos(faltanAM - 1, "ActionRoles", "", 1);
                    repareProjectSolution(state, posProy, candidatos, "", "ActionRoles");

                }
                break;
            case "<":
                if (countActionRoles == countMentalRoles) {
                    candidatos = buscarCandidatos(faltanAM, "MentalRoles", "ActionRoles", 1);
                    repareProjectSolution(state, posProy, candidatos, "ActionRoles", "");
                } else if (countActionRoles < countMentalRoles) {
                    candidatos = buscarCandidatos(1, "MentalRoles", "ActionRoles", 1);
                    repareProjectSolution(state, posProy, candidatos, "ActionRoles", "MentalRoles");
                    candidatos = buscarCandidatos(faltanAM - 1, "MentalRoles", "", 1);
                    repareProjectSolution(state, posProy, candidatos, "", "MentalRoles");

                }
                break;
            default:
                break;
        }

        team = actualiceTeam(state, posProy);
        countMentalRoles = contarCategoriaRoles(team).get(0);
        countActionRoles = contarCategoriaRoles(team).get(1);
        countSocialRoles = contarCategoriaRoles(team).get(2);


        int faltanMS = Math.abs(countMentalRoles - countSocialRoles);
        switch (mentalSocialOper) {
            case "==":
                if (countSocialRoles > countMentalRoles) {
                    candidatos = buscarCandidatos(faltanMS, "MentalRoles", "SocialRoles", 4);
                    repareProjectSolution(state, posProy, candidatos, "SocialRoles", "MentalRoles");
                } else if (countSocialRoles < countMentalRoles) {

                    candidatos = buscarCandidatos(faltanMS, "MentalRoles", "SocialRoles", 4);
                    repareProjectSolution(state, posProy, candidatos, "MentalRoles", "SocialRoles");
                }
                break;
            case ">":
                if (countSocialRoles == countMentalRoles) {
                    candidatos = buscarCandidatos(1, "MentalRoles", "SocialRoles", 3);
                    repareProjectSolution(state, posProy, candidatos, "", "MentalRoles");


                } else if (countMentalRoles < countSocialRoles) {
                    candidatos = buscarCandidatos(faltanMS, "MentalRoles", "SocialRoles", 3);
                    repareProjectSolution(state, posProy, candidatos, "SocialRoles", "MentalRoles");
                }
                break;
            case "<":
                if (countSocialRoles == countMentalRoles) {
                    candidatos = buscarCandidatos(1, "SocialRoles", "MentalRoles", 3);
                    repareProjectSolution(state, posProy, candidatos, "", "SocialRoles");

                } else if (countMentalRoles > countSocialRoles) {
                    candidatos = buscarCandidatos(faltanMS, "SocialRoles", "MentalRoles", 3);
                    repareProjectSolution(state, posProy, candidatos, "MentalRoles", "SocialRoles");
                }
                break;
            default:
                break;
        }
        team = actualiceTeam(state, posProy);
        countMentalRoles = contarCategoriaRoles(team).get(0);
        countActionRoles = contarCategoriaRoles(team).get(1);
        countSocialRoles = contarCategoriaRoles(team).get(2);

        if (countActionRoles == countMentalRoles) {
            if (actionMentalOper.equals(">")) {
                candidatos = buscarCandidatos(faltanMS, "MentalRoles", "SocialRoles", 5);
                repareProjectSolution(state, posProy, candidatos, "MentalRoles", "SocialRoles");

            } else if (actionMentalOper.equals("<")) {
                candidatos = buscarCandidatos(faltanMS, "ActionRoles", "SocialRoles", 6);
                repareProjectSolution(state, posProy, candidatos, "ActionRoles", "SocialRoles");
            }
        }

    }

    public ArrayList<Worker> buscarCandidatos(int faltan, String seaRol, String noSeaRol, int caso) {
        TeamFormationCodification codification = (TeamFormationCodification) Strategy.getStrategy().getProblem().getCodification();
        ArrayList<Worker> candidatos = new ArrayList<>();

        int i = 0;
        while (i < codification.getSearchArea().size() && (candidatos.size() < faltan)) {

            Worker worker = codification.getSearchArea().get(i);
            WorkerTest workerTest = worker.getWorkerTest(); //obtener caracteristicas psicologicas

            boolean isRoleAction = (workerTest.getID() != 'I' && workerTest.getID() != 'E') || (workerTest.getIS() != 'I' && workerTest.getIS() != 'E') || (workerTest.getIF() != 'I' && workerTest.getIF() != 'E');
            boolean isRoleMental = (workerTest.getCE() != 'I' && workerTest.getCE() != 'E') || (workerTest.getME() != 'I' && workerTest.getME() != 'E') || (workerTest.getES() != 'I' && workerTest.getES() != 'E');

            switch (seaRol) {
                case "MentalRoles": {
                    boolean mr = (workerTest.getCE() != 'I' && workerTest.getCE() != 'E') || (workerTest.getME() != 'I' && workerTest.getME() != 'E') || (workerTest.getES() != 'I' && workerTest.getES() != 'E');
                    switch (noSeaRol) {
                        case "ActionRoles":
                            boolean ar = (workerTest.getID() != 'I' && workerTest.getID() != 'E') || (workerTest.getIS() != 'I' && workerTest.getIS() != 'E') || (workerTest.getIF() != 'I' && workerTest.getIF() != 'E');
                            if ((mr && !ar && caso == 1) ||
                                    (mr && ar && caso == 2) ||
                                    (mr && !ar && isRoleAction && caso == 3) ||
                                    (mr && ar && isRoleAction && caso == 4)) {
                                candidatos.add(worker);
                            }
                            break;
                        case "SocialRoles":
                            boolean sr = (workerTest.getCO() != 'I' && workerTest.getCO() != 'E') || (workerTest.getCH() != 'I' && workerTest.getCH() != 'E') || (workerTest.getIR() != 'I' && workerTest.getIR() != 'E');
                            if ((mr && !sr && caso == 1) ||
                                    (mr && sr && caso == 2) ||
                                    (mr && !sr && isRoleAction && caso == 3) ||
                                    (mr && sr && isRoleAction && caso == 4) ||
                                    (!mr && !sr && isRoleAction && caso == 5)) {
                                candidatos.add(worker);
                            }
                            break;
                        case "":
                            if ((mr && caso == 1) || (mr && isRoleAction && caso == 3)) {
                                candidatos.add(worker);
                            }
                            break;
                        default:
                            break;
                    }

                    break;
                }
                case "ActionRoles": {
                    boolean ar = (workerTest.getID() != 'I' && workerTest.getID() != 'E') || (workerTest.getIS() != 'I' && workerTest.getIS() != 'E') || (workerTest.getIF() != 'I' && workerTest.getIF() != 'E');

                    switch (noSeaRol) {
                        case "MentalRoles":
                            boolean mr = (workerTest.getCE() != 'I' && workerTest.getCE() != 'E') || (workerTest.getME() != 'I' && workerTest.getME() != 'E') || (workerTest.getES() != 'I' && workerTest.getES() != 'E');
                            if ((!mr && ar && caso == 1) ||
                                    (mr && ar && caso == 2) ||
                                    (!mr && ar && isRoleAction && caso == 3) ||
                                    (mr && ar && isRoleAction && caso == 4)) {
                                candidatos.add(worker);
                            }
                            break;
                        case "SocialRoles":
                            boolean sr = (workerTest.getCO() != 'I' && workerTest.getCO() != 'E') || (workerTest.getCH() != 'I' && workerTest.getCH() != 'E') || (workerTest.getIR() != 'I' && workerTest.getIR() != 'E');
                            if ((ar && !sr && caso == 1) ||
                                    (ar && sr && caso == 2) ||
                                    (ar && !sr && isRoleAction && caso == 3) ||
                                    (ar && sr && isRoleAction && caso == 4) ||
                                    (!ar && !sr && isRoleMental && caso == 6)) {
                                candidatos.add(worker);
                            }
                            break;
                        case "":
                            if ((ar && caso == 1) || (ar && isRoleAction && caso == 3)) {
                                candidatos.add(worker);
                            }
                            break;
                        default:
                            break;
                    }
                    break;
                }
                case "SocialRoles": {
                    boolean sr = (workerTest.getCO() != 'I' && workerTest.getCO() != 'E') || (workerTest.getCH() != 'I' && workerTest.getCH() != 'E') || (workerTest.getIR() != 'I' && workerTest.getIR() != 'E');
                    switch (noSeaRol) {
                        case "MentalRoles":
                            boolean mr = (workerTest.getCE() != 'I' && workerTest.getCE() != 'E') || (workerTest.getME() != 'I' && workerTest.getME() != 'E') || (workerTest.getES() != 'I' && workerTest.getES() != 'E');
                            if ((!mr && sr && caso == 1) ||
                                    (mr && sr && caso == 2) ||
                                    (!mr && sr && isRoleAction && caso == 3) ||
                                    (mr && sr && isRoleAction && caso == 4) ||
                                    (!mr && !sr && isRoleAction && caso == 5)) {
                                candidatos.add(worker);
                            }
                            break;
                        case "ActionRoles":
                            boolean ar = (workerTest.getID() != 'I' && workerTest.getID() != 'E') || (workerTest.getIS() != 'I' && workerTest.getIS() != 'E') || (workerTest.getIF() != 'I' && workerTest.getIF() != 'E');
                            if ((sr && !ar && caso == 1) ||
                                    (sr && ar && caso == 2) ||
                                    (sr && !ar && isRoleAction && caso == 3) ||
                                    (sr && ar && isRoleAction && caso == 4) ||
                                    (!sr && !ar && isRoleAction && caso == 5)) {
                                candidatos.add(worker);
                            }
                            break;
                        case "":
                            if ((sr && caso == 1) || (sr && isRoleAction && caso == 3)) {
                                candidatos.add(worker);
                            }
                            break;
                        default:
                            break;

                    }
                }
                default:
                    break;
            }
            i++;
        }
        return candidatos;
    }

    public List<Integer> contarCategoriaRoles(List<Worker> team) {

        List<Integer> result = new ArrayList<>();
        int countMentalRoles = 0;
        int countActionRoles = 0;
        int countSocialRoles = 0;
        int k = 0;
        while (k < team.size()) {  //para cada persona del equipo de proyecto actual
            Worker worker = team.get(k);
            WorkerTest workerTest = worker.getWorkerTest(); //obtener caracteristicas psicologicas

            if (workerTest != null) {
                if ((workerTest.getID() != 'I' && workerTest.getID() != 'E') || (workerTest.getIS() != 'I' && workerTest.getIS() != 'E') || (workerTest.getIF() != 'I' && workerTest.getIF() != 'E')) {
                    countActionRoles++;
                }
                if ((workerTest.getCE() != 'I' && workerTest.getCE() != 'E') || (workerTest.getME() != 'I' && workerTest.getME() != 'E') || (workerTest.getES() != 'I' && workerTest.getES() != 'E')) {
                    countMentalRoles++;
                }
                if ((workerTest.getCO() != 'I' && workerTest.getCO() != 'E') || (workerTest.getCH() != 'I' && workerTest.getCH() != 'E') || (workerTest.getIR() != 'I' && workerTest.getIR() != 'E')) {
                    countSocialRoles++;
                }
            }
            k++;
        }
        result.add(countMentalRoles);
        result.add(countActionRoles);
        result.add(countSocialRoles);
        return result;
    }
}
