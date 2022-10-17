/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.model;

import org.teamsoft.entity.Competence;
import org.teamsoft.entity.CompetenceValue;
import org.teamsoft.entity.Worker;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * @author G1lb3rt
 */
@Stateless
public class CompetenceValueFacade extends AbstractFacade<CompetenceValue> implements Serializable {

    @PersistenceContext(unitName = "tesisPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CompetenceValueFacade() {
        super(CompetenceValue.class);
    }

    public Optional<CompetenceValue> findByCompetenceAndWorker(Competence competence, Worker worker) {
        Query query = getEntityManager().createNamedQuery("CompetenceValue.findByCompAndWorker");
        query.setParameter("competenceId", competence);
        query.setParameter("workerId", worker);
        CompetenceValue toReturn;
        try {
            toReturn = (CompetenceValue) query.getSingleResult();
        } catch (NoResultException e) {
            toReturn = null;
        }
        return Optional.ofNullable(toReturn);
    }

    /**
     * borra todas las competencias asociadas a una persona
     */
    public void deleteCompetencesByWorker(Worker worker) {
        Query query = getEntityManager().createNamedQuery("CompetenceValue.deleteCompetencesByWorker");
        query.setParameter("workerId", worker);
        query.executeUpdate();
    }

    public CompetenceValue findCompetenceValue(long workerId, long competenceId){
        
        List<CompetenceValue> competenceValue;
        
        Query query = em.createQuery(" SELECT cv FROM CompetenceValue cv WHERE cv.workersFk.id = ?1 AND cv.competenceFk.id = ?2 ");
        query.setParameter(1, workerId);
        query.setParameter(2, competenceId);
        competenceValue = query.getResultList();
        
        return !competenceValue.isEmpty() ? competenceValue.get(0):null;
    }


}
