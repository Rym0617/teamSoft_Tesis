/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.model;

import org.teamsoft.entity.Levels;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author G1lb3rt
 */
@Stateless
public class LevelsFacade extends AbstractFacade<Levels> implements Serializable {

    @PersistenceContext(unitName = "tesisPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * Obtener el nivel maximo definido para las competencias
     *
     * @return
     */
    public Levels getMaxLevel() {

        List<Levels> levels = findAll();
        Levels maxLevel = new Levels();

        if (!levels.isEmpty()) {
            maxLevel = findAll().get(0);

            for (Levels lvl : levels) {
                if (maxLevel.getLevels() < lvl.getLevels()) {
                    maxLevel = lvl;
                }
            }

        }

        return maxLevel;
    }

    /**
     * Obtener el nivel Minimo definido para las competencias
     *
     * @return
     */
    public Levels getMinLevel() {

        List<Levels> levels = findAll();
        Levels minLevel = new Levels();

        if (!levels.isEmpty()) {
            minLevel = findAll().get(0);

            for (Levels lvl : levels) {
                if (minLevel.getLevels() > lvl.getLevels()) {
                    minLevel = lvl;
                }
            }
        }
        return minLevel;
    }

    public LevelsFacade() {
        super(Levels.class);
    }

}
