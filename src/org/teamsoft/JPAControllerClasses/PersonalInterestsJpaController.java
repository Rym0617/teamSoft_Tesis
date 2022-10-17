/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.JPAControllerClasses;

import org.teamsoft.entity.PersonalInterests;
import org.teamsoft.entity.Role;
import org.teamsoft.entity.Worker;
import org.teamsoft.exceptions.NonexistentEntityException;
import org.teamsoft.exceptions.PreexistingEntityException;
import org.teamsoft.exceptions.RollbackFailureException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author yoyo
 */
public class PersonalInterestsJpaController implements Serializable {

    public PersonalInterestsJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PersonalInterests personalInterests) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Role rolesFk = personalInterests.getRolesFk();
            if (rolesFk != null) {
                rolesFk = em.getReference(rolesFk.getClass(), rolesFk.getId());
                personalInterests.setRolesFk(rolesFk);
            }
            Worker workersFk = personalInterests.getWorkersFk();
            if (workersFk != null) {
                workersFk = em.getReference(workersFk.getClass(), workersFk.getId());
                personalInterests.setWorkersFk(workersFk);
            }
            em.persist(personalInterests);
            if (rolesFk != null) {
                rolesFk.getPersonalInterestsList().add(personalInterests);
                rolesFk = em.merge(rolesFk);
            }
            if (workersFk != null) {
                workersFk.getPersonalInterestsList().add(personalInterests);
                workersFk = em.merge(workersFk);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPersonalInterests(personalInterests.getId()) != null) {
                throw new PreexistingEntityException("PersonalInterests " + personalInterests + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PersonalInterests personalInterests) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            PersonalInterests persistentPersonalInterests = em.find(PersonalInterests.class, personalInterests.getId());
            Role rolesFkOld = persistentPersonalInterests.getRolesFk();
            Role rolesFkNew = personalInterests.getRolesFk();
            Worker workersFkOld = persistentPersonalInterests.getWorkersFk();
            Worker workersFkNew = personalInterests.getWorkersFk();
            if (rolesFkNew != null) {
                rolesFkNew = em.getReference(rolesFkNew.getClass(), rolesFkNew.getId());
                personalInterests.setRolesFk(rolesFkNew);
            }
            if (workersFkNew != null) {
                workersFkNew = em.getReference(workersFkNew.getClass(), workersFkNew.getId());
                personalInterests.setWorkersFk(workersFkNew);
            }
            personalInterests = em.merge(personalInterests);
            if (rolesFkOld != null && !rolesFkOld.equals(rolesFkNew)) {
                rolesFkOld.getPersonalInterestsList().remove(personalInterests);
                rolesFkOld = em.merge(rolesFkOld);
            }
            if (rolesFkNew != null && !rolesFkNew.equals(rolesFkOld)) {
                rolesFkNew.getPersonalInterestsList().add(personalInterests);
                rolesFkNew = em.merge(rolesFkNew);
            }
            if (workersFkOld != null && !workersFkOld.equals(workersFkNew)) {
                workersFkOld.getPersonalInterestsList().remove(personalInterests);
                workersFkOld = em.merge(workersFkOld);
            }
            if (workersFkNew != null && !workersFkNew.equals(workersFkOld)) {
                workersFkNew.getPersonalInterestsList().add(personalInterests);
                workersFkNew = em.merge(workersFkNew);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = personalInterests.getId();
                if (findPersonalInterests(id) == null) {
                    throw new NonexistentEntityException("The personalInterests with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            PersonalInterests personalInterests;
            try {
                personalInterests = em.getReference(PersonalInterests.class, id);
                personalInterests.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The personalInterests with id " + id + " no longer exists.", enfe);
            }
            Role rolesFk = personalInterests.getRolesFk();
            if (rolesFk != null) {
                rolesFk.getPersonalInterestsList().remove(personalInterests);
                rolesFk = em.merge(rolesFk);
            }
            Worker workersFk = personalInterests.getWorkersFk();
            if (workersFk != null) {
                workersFk.getPersonalInterestsList().remove(personalInterests);
                workersFk = em.merge(workersFk);
            }
            em.remove(personalInterests);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PersonalInterests> findPersonalInterestsEntities() {
        return findPersonalInterestsEntities(true, -1, -1);
    }

    public List<PersonalInterests> findPersonalInterestsEntities(int maxResults, int firstResult) {
        return findPersonalInterestsEntities(false, maxResults, firstResult);
    }

    private List<PersonalInterests> findPersonalInterestsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PersonalInterests.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public PersonalInterests findPersonalInterests(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PersonalInterests.class, id);
        } finally {
            em.close();
        }
    }

    public int getPersonalInterestsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PersonalInterests> rt = cq.from(PersonalInterests.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
