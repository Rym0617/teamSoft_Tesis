/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.model;

import org.teamsoft.entity.Competence;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author G1lb3rt
 */
@Stateless
public class CompetenceFacade extends AbstractFacade<Competence> implements Serializable {

    @PersistenceContext(unitName = "tesisPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<Competence> getGenericCompetences() {
        List<Competence> list = new ArrayList<>();
        for (Competence item : findAll()) {
            if (!item.getTechnical()) {
                list.add(item);
            }
        }
        return list;
    }

    public List<Competence> getTechnicalCompetences() {
        List<Competence> list = new ArrayList<>();
        for (Competence item : findAll()) {
            if (item.getTechnical()) {
                list.add(item);
            }
        }
        return list;
    }

    public Competence findByName(String name) {
        Competence result = null;
        Query query = getEntityManager().createNamedQuery("Competence.findByCompetitionName");
        query.setParameter("competitionName", name);
        try {
            result = (Competence) query.getSingleResult();
        } catch (Exception ignore) {
        }
        return result;
    }

    public CompetenceFacade() {
        super(Competence.class);
    }

}
