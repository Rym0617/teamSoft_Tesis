/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.model;

import org.teamsoft.entity.CompetenceDimension;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author G1lb3rt
 */
@Stateless
public class CompetenceDimensionFacade extends AbstractFacade<CompetenceDimension> {

    @PersistenceContext(unitName = "tesisPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CompetenceDimensionFacade() {
        super(CompetenceDimension.class);
    }
    
}
