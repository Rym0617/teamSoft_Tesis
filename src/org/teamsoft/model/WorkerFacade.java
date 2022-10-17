/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.model;

import org.teamsoft.entity.Worker;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;

/**
 * @author G1lb3rt
 */
@Stateless
public class WorkerFacade extends AbstractFacade<Worker> implements Serializable {

    @PersistenceContext(unitName = "tesisPU")
    private EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public WorkerFacade() {
        super(Worker.class);
    }

    public Worker getByName(String name) {
        Query cq = getEntityManager().createNamedQuery("Worker.findByName");
        cq.setParameter("personName", name.trim());
        return (Worker) cq.getSingleResult();
    }

}
