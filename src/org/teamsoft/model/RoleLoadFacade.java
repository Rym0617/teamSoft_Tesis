/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.model;

import org.teamsoft.entity.RoleLoad;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author G1lb3rt
 */
@Stateless
public class RoleLoadFacade extends AbstractFacade<RoleLoad> {

    @PersistenceContext(unitName = "tesisPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RoleLoadFacade() {
        super(RoleLoad.class);
    }
    
}
