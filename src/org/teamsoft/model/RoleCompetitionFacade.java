/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.model;

import org.teamsoft.entity.RoleCompetition;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author G1lb3rt
 */
@Stateless
public class RoleCompetitionFacade extends AbstractFacade<RoleCompetition> {

    @PersistenceContext(unitName = "tesisPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * Obtener lista de RoleCompetition paraun rol determinado
     *
     * @param rol
     * @return
     */
    public List<RoleCompetition> findByRole(String rol) {
        List<RoleCompetition> competitions = new ArrayList<>();

        for (RoleCompetition item : findAll()) {
            if (item.getRolesFk().getRoleName().equalsIgnoreCase(rol)) {
                competitions.add(item);
            }
        }

        return competitions;
    }

    public RoleCompetitionFacade() {
        super(RoleCompetition.class);
    }

}
