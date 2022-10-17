/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.model;

import org.teamsoft.entity.Role;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

/**
 * @author G1lb3rt & jpinas
 */
@Stateless
public class RoleFacade extends AbstractFacade<Role> implements Serializable {

    @PersistenceContext(unitName = "tesisPU")
    private EntityManager em;


    /**
     * Buscar un rol dado su nombre
     */
    public Role findByName(String name) {
        Role role = null;

        List<Role> roles = findAll();
        int i = 0;
        boolean found = false;
        while (i < roles.size() && !found) {
            if (roles.get(i).getRoleName().equalsIgnoreCase(name)) {
                role = roles.get(i);
                found = true;
            }
            i++;
        }
        return role;
    }

    public Role findBoss(){
        Role boss = null;
        Query query = getEntityManager().createNamedQuery("Role.findBoss");
        try {
            boss = (Role) query.getSingleResult();
        } catch (Exception ignore) {
        }
        return boss;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RoleFacade() {
        super(Role.class);
    }

}
