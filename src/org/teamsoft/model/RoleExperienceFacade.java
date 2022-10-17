/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.model;

import org.teamsoft.entity.Role;
import org.teamsoft.entity.RoleExperience;
import org.teamsoft.entity.Worker;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.Optional;

/**
 *
 * @author G1lb3rt
 */
@Stateless
public class RoleExperienceFacade extends AbstractFacade<RoleExperience> implements Serializable {

    @PersistenceContext(unitName = "tesisPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RoleExperienceFacade() {
        super(RoleExperience.class);
    }

    public void deleteByWorker(Worker worker){
        Query query = getEntityManager().createNamedQuery("RoleExperience.deleteByWorker");
        query.setParameter("workerId", worker);
        query.executeUpdate();
    }

    public Optional<RoleExperience> findByRoleAndWorker(Role role, Worker worker) {
        Query query = getEntityManager().createNamedQuery("RoleExperience.findByRoleAndWorker");
        query.setParameter("worker", worker);
        query.setParameter("role", role);
        RoleExperience toReturn;
        try {
            toReturn = (RoleExperience) query.getSingleResult();
        } catch (NoResultException e) {
            toReturn = null;
        }
        return Optional.ofNullable(toReturn);
    }
    
}
