package org.teamsoft.model;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

/**
 * @author jpinas
 */
@Stateless
public class IncompatibleRolesFacade extends AbstractFacade<IncompatibleRolesFacade> implements Serializable {

    @PersistenceContext(unitName = "tesisPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public IncompatibleRolesFacade() {
        super(IncompatibleRolesFacade.class);
    }
}
