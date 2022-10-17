/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.metaheuristics.operator;

import org.teamsoft.entity.Worker;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Zucelys
 */
public class Cruzamiento6 extends TeamFormationOperator {
    public static Worker OperadorCruzamiento6(Worker pers1, Worker pers2, Long idRol, ArrayList<Worker> persProy)
            throws IOException, ClassNotFoundException {
        ArrayList<Worker> personas = new ArrayList<>();
        personas.add(pers1);
        personas.add(pers2);
        Worker temPersona;
        if (persProy.isEmpty())
            temPersona = personaConMayorExperienciaParaRolSolucion(idRol, personas);
        else
            temPersona = personaMayorIdoneidadParaSolucion(persProy, idRol, personas);

        return temPersona;
    }

}
