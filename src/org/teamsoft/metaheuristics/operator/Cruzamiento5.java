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
public class Cruzamiento5 extends TeamFormationOperator {

    public static Worker OperadorCruzamiento5(ArrayList<Worker> pers1, ArrayList<Worker> pers2, Long idRol, ArrayList<Worker> persProy, int numVect)
            throws IOException, ClassNotFoundException {
        Worker temPersona;
        ArrayList<Worker> personas = pers1;
        if (numVect == 1)
            personas = pers2;
        if (persProy.isEmpty())
            temPersona = personaConMayorExperienciaParaRolSolucion(idRol, personas);
        else
            temPersona = personaMayorIdoneidadParaSolucion(persProy, idRol, personas);

        return temPersona;
    }

}
