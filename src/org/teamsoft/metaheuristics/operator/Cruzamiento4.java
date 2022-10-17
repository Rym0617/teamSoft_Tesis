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
public class Cruzamiento4 extends TeamFormationOperator {

    public static Worker OperadorCruzamiento4(ArrayList<Worker> pers1, ArrayList<Worker> pers2, Long idRol, int numVect)
            throws IOException, ClassNotFoundException {
        Worker temPersona;
        ArrayList<Worker> personas = pers1;
        if (numVect == 1)
            personas = pers2;
        temPersona = personaConMayorExperienciaParaRolSolucion(idRol, personas);
        return temPersona;
    }

}
