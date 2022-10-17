/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.model;

import org.teamsoft.entity.RoleEval;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

/**
 * @author G1lb3rt
 */
@Stateless
public class RoleEvalFacade extends AbstractFacade<RoleEval> implements Serializable {

    @PersistenceContext(unitName = "tesisPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * Obtener evaluacion máxima definida para el desempeño de un rol
     *
     * @return
     */
    public RoleEval getMaxLevel() {

        List<RoleEval> evaluations = findAll();
        RoleEval maxEvaluation = new RoleEval();

        if (!evaluations.isEmpty()) {
            maxEvaluation = evaluations.get(0);

            for (RoleEval lvl : evaluations) {
                if (maxEvaluation.getLevels() < lvl.getLevels()) {
                    maxEvaluation = lvl;
                }
            }
        }

        return maxEvaluation;
    }

    /**
     * Obtener evaluacion mínima definida para el desempeño de un rol
     *
     * @return
     */
    public RoleEval getMinLevel() {

        List<RoleEval> evaluations = findAll();
        RoleEval minEvaluation = new RoleEval();

        if (!evaluations.isEmpty()) {
            minEvaluation = evaluations.get(0);

            for (RoleEval lvl : evaluations) {
                if (minEvaluation.getLevels() > lvl.getLevels()) {
                    minEvaluation = lvl;
                }
            }
        }

        return minEvaluation;
    }

    public RoleEvalFacade() {
        super(RoleEval.class);
    }

}
