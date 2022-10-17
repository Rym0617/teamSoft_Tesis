/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.model;

import java.util.List;
import org.teamsoft.entity.RoleEvaluation;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author G1lb3rt
 */
@Stateless
public class RoleEvaluationFacade extends AbstractFacade<RoleEvaluation> {

    @PersistenceContext(unitName = "tesisPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RoleEvaluationFacade() {
        super(RoleEvaluation.class);
    }
    
      public RoleEvaluation findRolEvaluation(long workerID, long cycleID, long rolID){
        
        List<RoleEvaluation> rolEvaluation;
        
        Query query = em.createQuery(" SELECT r FROM RoleEvaluation r WHERE r.workerFk.id = ?1 AND r.cycleFk.id = ?2 AND r.roleFk.id = ?3 ");
        query.setParameter(1, workerID);
        query.setParameter(2, cycleID);
        query.setParameter(3, rolID);
        rolEvaluation = query.getResultList();
        return !rolEvaluation.isEmpty()? rolEvaluation.get(0) : null;
    }
    
}
