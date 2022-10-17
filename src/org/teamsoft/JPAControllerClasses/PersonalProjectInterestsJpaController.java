/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.JPAControllerClasses;

import org.teamsoft.entity.PersonalProjectInterests;
import org.teamsoft.entity.Project;
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
public class PersonalProjectInterestsJpaController implements Serializable {

    public PersonalProjectInterestsJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PersonalProjectInterests personalProjectInterests) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Project projectFk = personalProjectInterests.getProjectFk();
            if (projectFk != null) {
                projectFk = em.getReference(projectFk.getClass(), projectFk.getId());
                personalProjectInterests.setProjectFk(projectFk);
            }
            Worker workersFk = personalProjectInterests.getWorkersFk();
            if (workersFk != null) {
                workersFk = em.getReference(workersFk.getClass(), workersFk.getId());
                personalProjectInterests.setWorkersFk(workersFk);
            }
            em.persist(personalProjectInterests);
            if (projectFk != null) {
                projectFk.getPersonalProjectInterestsList().add(personalProjectInterests);
                projectFk = em.merge(projectFk);
            }
            if (workersFk != null) {
                workersFk.getPersonalProjectInterestsList().add(personalProjectInterests);
                workersFk = em.merge(workersFk);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPersonalProjectInterests(personalProjectInterests.getId()) != null) {
                throw new PreexistingEntityException("PersonalProjectInterests " + personalProjectInterests + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PersonalProjectInterests personalProjectInterests) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            PersonalProjectInterests persistentPersonalProjectInterests = em.find(PersonalProjectInterests.class, personalProjectInterests.getId());
            Project projectFkOld = persistentPersonalProjectInterests.getProjectFk();
            Project projectFkNew = personalProjectInterests.getProjectFk();
            Worker workersFkOld = persistentPersonalProjectInterests.getWorkersFk();
            Worker workersFkNew = personalProjectInterests.getWorkersFk();
            if (projectFkNew != null) {
                projectFkNew = em.getReference(projectFkNew.getClass(), projectFkNew.getId());
                personalProjectInterests.setProjectFk(projectFkNew);
            }
            if (workersFkNew != null) {
                workersFkNew = em.getReference(workersFkNew.getClass(), workersFkNew.getId());
                personalProjectInterests.setWorkersFk(workersFkNew);
            }
            personalProjectInterests = em.merge(personalProjectInterests);
            if (projectFkOld != null && !projectFkOld.equals(projectFkNew)) {
                projectFkOld.getPersonalProjectInterestsList().remove(personalProjectInterests);
                projectFkOld = em.merge(projectFkOld);
            }
            if (projectFkNew != null && !projectFkNew.equals(projectFkOld)) {
                projectFkNew.getPersonalProjectInterestsList().add(personalProjectInterests);
                projectFkNew = em.merge(projectFkNew);
            }
            if (workersFkOld != null && !workersFkOld.equals(workersFkNew)) {
                workersFkOld.getPersonalInterestsList().remove(personalProjectInterests);
                workersFkOld = em.merge(workersFkOld);
            }
            if (workersFkNew != null && !workersFkNew.equals(workersFkOld)) {
                workersFkNew.getPersonalProjectInterestsList().add(personalProjectInterests);
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
                Long id = personalProjectInterests.getId();
                if (findPersonalProjectInterests(id) == null) {
                    throw new NonexistentEntityException("The personalProjectInterests with id " + id + " no longer exists.");
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
            PersonalProjectInterests personalProjectInterests;
            try {
                personalProjectInterests = em.getReference(PersonalProjectInterests.class, id);
                personalProjectInterests.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The personalProjectInterests with id " + id + " no longer exists.", enfe);
            }
            Project projectFk = personalProjectInterests.getProjectFk()
;            if (projectFk != null) {
                projectFk.getPersonalProjectInterestsList().remove(personalProjectInterests);
                projectFk = em.merge(projectFk);
            }
            Worker workersFk = personalProjectInterests.getWorkersFk();
            if (workersFk != null) {
                workersFk.getPersonalInterestsList().remove(personalProjectInterests);
                workersFk = em.merge(workersFk);
            }
            em.remove(personalProjectInterests);
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

    public List<PersonalProjectInterests> findPersonalProjectInterestsEntities() {
        return findPersonalProjectInterestsEntities(true, -1, -1);
    }

    public List<PersonalProjectInterests> findPersonalProjectInterestsEntities(int maxResults, int firstResult) {
        return findPersonalProjectInterestsEntities(false, maxResults, firstResult);
    }

    private List<PersonalProjectInterests> findPersonalProjectInterestsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PersonalProjectInterests.class));
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

    public PersonalProjectInterests findPersonalProjectInterests(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PersonalProjectInterests.class, id);
        } finally {
            em.close();
        }
    }

    public int getPersonalProjectInterestsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PersonalProjectInterests> rt = cq.from(PersonalProjectInterests.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}