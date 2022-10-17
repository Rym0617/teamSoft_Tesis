/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.model;

import org.teamsoft.entity.CostDistance;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

/**
 * @author G1lb3rt
 */
@Stateless
public class CostDistanceFacade extends AbstractFacade<CostDistance> implements Serializable {

    @PersistenceContext(unitName = "tesisPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CostDistanceFacade() {
        super(CostDistance.class);
    }

    public CostDistance getMaxCostDistance() {
        List<CostDistance> costsDistance = findAll();
        CostDistance maxCostDistance = new CostDistance();

        if (!costsDistance.isEmpty()) {
            maxCostDistance = findAll().get(0);

            for (CostDistance cost : costsDistance) {
                if (maxCostDistance.getCostDistance() < cost.getCostDistance()) {
                    maxCostDistance = cost;
                }
            }

        }
        return maxCostDistance;
    }
}
