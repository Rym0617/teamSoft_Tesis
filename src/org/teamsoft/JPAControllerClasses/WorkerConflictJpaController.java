/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.JPAControllerClasses;

import org.teamsoft.entity.ConflictIndex;
import org.teamsoft.entity.Worker;
import org.teamsoft.entity.WorkerConflict;
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
public class WorkerConflictJpaController implements Serializable {

    public WorkerConflictJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(WorkerConflict workerConflict) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ConflictIndex indexFk = workerConflict.getIndexFk();
            if (indexFk != null) {
                indexFk = em.getReference(indexFk.getClass(), indexFk.getId());
                workerConflict.setIndexFk(indexFk);
            }
            Worker workerConflictFk = workerConflict.getWorkerConflictFk();
            if (workerConflictFk != null) {
                workerConflictFk = em.getReference(workerConflictFk.getClass(), workerConflictFk.getId());
                workerConflict.setWorkerConflictFk(workerConflictFk);
            }
            Worker workerFk = workerConflict.getWorkerFk();
            if (workerFk != null) {
                workerFk = em.getReference(workerFk.getClass(), workerFk.getId());
                workerConflict.setWorkerFk(workerFk);
            }
            em.persist(workerConflict);
            if (indexFk != null) {
                indexFk.getWorkerConflictList().add(workerConflict);
                indexFk = em.merge(indexFk);
            }
            if (workerConflictFk != null) {
                workerConflictFk.getWorkerConflictList().add(workerConflict);
                workerConflictFk = em.merge(workerConflictFk);
            }
            if (workerFk != null) {
                workerFk.getWorkerConflictList().add(workerConflict);
                workerFk = em.merge(workerFk);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findWorkerConflict(workerConflict.getId()) != null) {
                throw new PreexistingEntityException("WorkerConflict " + workerConflict + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(WorkerConflict workerConflict) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            WorkerConflict persistentWorkerConflict = em.find(WorkerConflict.class, workerConflict.getId());
            ConflictIndex indexFkOld = persistentWorkerConflict.getIndexFk();
            ConflictIndex indexFkNew = workerConflict.getIndexFk();
            Worker workerConflictFkOld = persistentWorkerConflict.getWorkerConflictFk();
            Worker workerConflictFkNew = workerConflict.getWorkerConflictFk();
            Worker workerFkOld = persistentWorkerConflict.getWorkerFk();
            Worker workerFkNew = workerConflict.getWorkerFk();
            if (indexFkNew != null) {
                indexFkNew = em.getReference(indexFkNew.getClass(), indexFkNew.getId());
                workerConflict.setIndexFk(indexFkNew);
            }
            if (workerConflictFkNew != null) {
                workerConflictFkNew = em.getReference(workerConflictFkNew.getClass(), workerConflictFkNew.getId());
                workerConflict.setWorkerConflictFk(workerConflictFkNew);
            }
            if (workerFkNew != null) {
                workerFkNew = em.getReference(workerFkNew.getClass(), workerFkNew.getId());
                workerConflict.setWorkerFk(workerFkNew);
            }
            workerConflict = em.merge(workerConflict);
            if (indexFkOld != null && !indexFkOld.equals(indexFkNew)) {
                indexFkOld.getWorkerConflictList().remove(workerConflict);
                indexFkOld = em.merge(indexFkOld);
            }
            if (indexFkNew != null && !indexFkNew.equals(indexFkOld)) {
                indexFkNew.getWorkerConflictList().add(workerConflict);
                indexFkNew = em.merge(indexFkNew);
            }
            if (workerConflictFkOld != null && !workerConflictFkOld.equals(workerConflictFkNew)) {
                workerConflictFkOld.getWorkerConflictList().remove(workerConflict);
                workerConflictFkOld = em.merge(workerConflictFkOld);
            }
            if (workerConflictFkNew != null && !workerConflictFkNew.equals(workerConflictFkOld)) {
                workerConflictFkNew.getWorkerConflictList().add(workerConflict);
                workerConflictFkNew = em.merge(workerConflictFkNew);
            }
            if (workerFkOld != null && !workerFkOld.equals(workerFkNew)) {
                workerFkOld.getWorkerConflictList().remove(workerConflict);
                workerFkOld = em.merge(workerFkOld);
            }
            if (workerFkNew != null && !workerFkNew.equals(workerFkOld)) {
                workerFkNew.getWorkerConflictList().add(workerConflict);
                workerFkNew = em.merge(workerFkNew);
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
                Long id = workerConflict.getId();
                if (findWorkerConflict(id) == null) {
                    throw new NonexistentEntityException("The workerConflict with id " + id + " no longer exists.");
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
            WorkerConflict workerConflict;
            try {
                workerConflict = em.getReference(WorkerConflict.class, id);
                workerConflict.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The workerConflict with id " + id + " no longer exists.", enfe);
            }
            ConflictIndex indexFk = workerConflict.getIndexFk();
            if (indexFk != null) {
                indexFk.getWorkerConflictList().remove(workerConflict);
                indexFk = em.merge(indexFk);
            }
            Worker workerConflictFk = workerConflict.getWorkerConflictFk();
            if (workerConflictFk != null) {
                workerConflictFk.getWorkerConflictList().remove(workerConflict);
                workerConflictFk = em.merge(workerConflictFk);
            }
            Worker workerFk = workerConflict.getWorkerFk();
            if (workerFk != null) {
                workerFk.getWorkerConflictList().remove(workerConflict);
                workerFk = em.merge(workerFk);
            }
            em.remove(workerConflict);
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

    public List<WorkerConflict> findWorkerConflictEntities() {
        return findWorkerConflictEntities(true, -1, -1);
    }

    public List<WorkerConflict> findWorkerConflictEntities(int maxResults, int firstResult) {
        return findWorkerConflictEntities(false, maxResults, firstResult);
    }

    private List<WorkerConflict> findWorkerConflictEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(WorkerConflict.class));
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

    public WorkerConflict findWorkerConflict(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(WorkerConflict.class, id);
        } finally {
            em.close();
        }
    }

    public int getWorkerConflictCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<WorkerConflict> rt = cq.from(WorkerConflict.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
