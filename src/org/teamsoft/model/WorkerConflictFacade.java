/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.model;

import java.util.List;
import org.teamsoft.entity.WorkerConflict;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author G1lb3rt
 */
@Stateless
public class WorkerConflictFacade extends AbstractFacade<WorkerConflict> {

    @PersistenceContext(unitName = "tesisPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public WorkerConflictFacade() {
        super(WorkerConflict.class);
    }
    
      public WorkerConflict findworkerConflict(long worker1, long worker2){
        
        List<WorkerConflict> wc;
        
        Query query = em.createQuery(" SELECT wc FROM WorkerConflict wc WHERE wc.workerFk.id = ?1 AND wc.workerConflictFk.id = ?2 ");
        query.setParameter(1, worker1);
        query.setParameter(2, worker2);
        wc = query.getResultList();
        
        return !wc.isEmpty()? wc.get(0) : null;
    }
    
}
