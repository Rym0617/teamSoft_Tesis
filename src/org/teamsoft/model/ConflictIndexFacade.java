/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.model;

import org.teamsoft.entity.ConflictIndex;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

/**
 * @author G1lb3rt
 */
@Stateless
public class ConflictIndexFacade extends AbstractFacade<ConflictIndex> implements Serializable {

    @PersistenceContext(unitName = "tesisPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ConflictIndexFacade() {
        super(ConflictIndex.class);
    }

    public ConflictIndex getMaxConflict() {
        List<ConflictIndex> conflicts = findAll();
        ConflictIndex maxConflict = new ConflictIndex();

        if (!conflicts.isEmpty()) {
            maxConflict = findAll().get(0);

            for (ConflictIndex conflict : conflicts) {
                if (maxConflict.getWeight() < conflict.getWeight()) {
                    maxConflict = conflict;
                }
            }
        }
        return maxConflict;
    }
}
