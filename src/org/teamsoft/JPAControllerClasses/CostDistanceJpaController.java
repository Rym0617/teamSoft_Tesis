/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.JPAControllerClasses;

import org.teamsoft.entity.CostDistance;
import org.teamsoft.entity.County;
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
public class CostDistanceJpaController implements Serializable {

    public CostDistanceJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CostDistance costDistance) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            County countyAFk = costDistance.getCountyAFk();
            if (countyAFk != null) {
                countyAFk = em.getReference(countyAFk.getClass(), countyAFk.getId());
                costDistance.setCountyAFk(countyAFk);
            }
            County countyBFk = costDistance.getCountyBFk();
            if (countyBFk != null) {
                countyBFk = em.getReference(countyBFk.getClass(), countyBFk.getId());
                costDistance.setCountyBFk(countyBFk);
            }
            em.persist(costDistance);
            if (countyAFk != null) {
                countyAFk.getCostDistanceList().add(costDistance);
                countyAFk = em.merge(countyAFk);
            }
            if (countyBFk != null) {
                countyBFk.getCostDistanceList().add(costDistance);
                countyBFk = em.merge(countyBFk);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findCostDistance(costDistance.getId()) != null) {
                throw new PreexistingEntityException("CostDistance " + costDistance + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CostDistance costDistance) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            CostDistance persistentCostDistance = em.find(CostDistance.class, costDistance.getId());
            County countyAFkOld = persistentCostDistance.getCountyAFk();
            County countyAFkNew = costDistance.getCountyAFk();
            County countyBFkOld = persistentCostDistance.getCountyBFk();
            County countyBFkNew = costDistance.getCountyBFk();
            if (countyAFkNew != null) {
                countyAFkNew = em.getReference(countyAFkNew.getClass(), countyAFkNew.getId());
                costDistance.setCountyAFk(countyAFkNew);
            }
            if (countyBFkNew != null) {
                countyBFkNew = em.getReference(countyBFkNew.getClass(), countyBFkNew.getId());
                costDistance.setCountyBFk(countyBFkNew);
            }
            costDistance = em.merge(costDistance);
            if (countyAFkOld != null && !countyAFkOld.equals(countyAFkNew)) {
                countyAFkOld.getCostDistanceList().remove(costDistance);
                countyAFkOld = em.merge(countyAFkOld);
            }
            if (countyAFkNew != null && !countyAFkNew.equals(countyAFkOld)) {
                countyAFkNew.getCostDistanceList().add(costDistance);
                countyAFkNew = em.merge(countyAFkNew);
            }
            if (countyBFkOld != null && !countyBFkOld.equals(countyBFkNew)) {
                countyBFkOld.getCostDistanceList().remove(costDistance);
                countyBFkOld = em.merge(countyBFkOld);
            }
            if (countyBFkNew != null && !countyBFkNew.equals(countyBFkOld)) {
                countyBFkNew.getCostDistanceList().add(costDistance);
                countyBFkNew = em.merge(countyBFkNew);
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
                Long id = costDistance.getId();
                if (findCostDistance(id) == null) {
                    throw new NonexistentEntityException("The costDistance with id " + id + " no longer exists.");
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
            CostDistance costDistance;
            try {
                costDistance = em.getReference(CostDistance.class, id);
                costDistance.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The costDistance with id " + id + " no longer exists.", enfe);
            }
            County countyAFk = costDistance.getCountyAFk();
            if (countyAFk != null) {
                countyAFk.getCostDistanceList().remove(costDistance);
                countyAFk = em.merge(countyAFk);
            }
            County countyBFk = costDistance.getCountyBFk();
            if (countyBFk != null) {
                countyBFk.getCostDistanceList().remove(costDistance);
                countyBFk = em.merge(countyBFk);
            }
            em.remove(costDistance);
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

    public List<CostDistance> findCostDistanceEntities() {
        return findCostDistanceEntities(true, -1, -1);
    }

    public List<CostDistance> findCostDistanceEntities(int maxResults, int firstResult) {
        return findCostDistanceEntities(false, maxResults, firstResult);
    }

    private List<CostDistance> findCostDistanceEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CostDistance.class));
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

    public CostDistance findCostDistance(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CostDistance.class, id);
        } finally {
            em.close();
        }
    }

    public int getCostDistanceCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CostDistance> rt = cq.from(CostDistance.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
