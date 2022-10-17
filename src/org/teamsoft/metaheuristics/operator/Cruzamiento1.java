/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.metaheuristics.operator;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author Zucelys
 */
public class Cruzamiento1 extends TeamFormationOperator {

    public static ArrayList<ArrayList<Object>> OperadorCruzamiento1(ArrayList<Object> code1, ArrayList<Object> code2) {
        ArrayList<Object> newCode1 = new ArrayList<>();
        ArrayList<Object> newCode2 = new ArrayList<>();
        ArrayList<ArrayList<Object>> codes = new ArrayList<>();
        ArrayList<Integer> vector = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < code1.size(); i++) {
            vector.add(random.nextInt(2));

        }
        for (int i = 0; i < code1.size(); i++) {
            if (vector.get(i) == 0) {
                newCode1.add(code1.get(i));
                newCode2.add(code2.get(i));
            } else {
                newCode1.add(code2.get(i));
                newCode2.add(code1.get(i));
            }
        }
        codes.add(newCode1);
        codes.add(newCode2);
        return codes;
    }

}
