package org.teamsoft.model;

import org.teamsoft.entity.PersonalInterests;
import org.teamsoft.entity.Role;
import org.teamsoft.entity.Worker;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.Optional;

/**
 * @author jpinas
 */
@Stateless
public class PersonalInterestsFacade extends AbstractFacade<PersonalInterests> implements Serializable {

    @PersistenceContext(unitName = "tesisPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PersonalInterestsFacade() {
        super(PersonalInterests.class);
    }

    public Optional<PersonalInterests> findByRoleAndWorker(Role rol, Worker worker) {
        Query query = getEntityManager().createNamedQuery("PersonalInterests.findByRoleAndWorker");
        query.setParameter("worker", worker);
        query.setParameter("role", rol);
        PersonalInterests toReturn;
        try {
            toReturn = (PersonalInterests) query.getSingleResult();
        } catch (NoResultException e) {
            toReturn = null;
        }
        return Optional.ofNullable(toReturn);
    }

    public void deleteByWorker(Worker worker) {
        Query query = getEntityManager().createNamedQuery("PersonalInterests.deleteByWorker");
        query.setParameter("workerId", worker);
        query.executeUpdate();
    }
}
