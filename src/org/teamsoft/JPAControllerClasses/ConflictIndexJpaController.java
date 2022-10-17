/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.JPAControllerClasses;

import org.teamsoft.entity.ConflictIndex;
import org.teamsoft.entity.WorkerConflict;
import org.teamsoft.exceptions.IllegalOrphanException;
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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author yoyo
 */
public class ConflictIndexJpaController implements Serializable {

    public ConflictIndexJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ConflictIndex conflictIndex) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (conflictIndex.getWorkerConflictList() == null) {
            conflictIndex.setWorkerConflictList(new ArrayList<WorkerConflict>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<WorkerConflict> attachedWorkerConflictList = new ArrayList<WorkerConflict>();
            for (WorkerConflict workerConflictListWorkerConflictToAttach : conflictIndex.getWorkerConflictList()) {
                workerConflictListWorkerConflictToAttach = em.getReference(workerConflictListWorkerConflictToAttach.getClass(), workerConflictListWorkerConflictToAttach.getId());
                attachedWorkerConflictList.add(workerConflictListWorkerConflictToAttach);
            }
            conflictIndex.setWorkerConflictList(attachedWorkerConflictList);
            em.persist(conflictIndex);
            for (WorkerConflict workerConflictListWorkerConflict : conflictIndex.getWorkerConflictList()) {
                ConflictIndex oldIndexFkOfWorkerConflictListWorkerConflict = workerConflictListWorkerConflict.getIndexFk();
                workerConflictListWorkerConflict.setIndexFk(conflictIndex);
                workerConflictListWorkerConflict = em.merge(workerConflictListWorkerConflict);
                if (oldIndexFkOfWorkerConflictListWorkerConflict != null) {
                    oldIndexFkOfWorkerConflictListWorkerConflict.getWorkerConflictList().remove(workerConflictListWorkerConflict);
                    oldIndexFkOfWorkerConflictListWorkerConflict = em.merge(oldIndexFkOfWorkerConflictListWorkerConflict);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findConflictIndex(conflictIndex.getId()) != null) {
                throw new PreexistingEntityException("ConflictIndex " + conflictIndex + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ConflictIndex conflictIndex) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ConflictIndex persistentConflictIndex = em.find(ConflictIndex.class, conflictIndex.getId());
            List<WorkerConflict> workerConflictListOld = persistentConflictIndex.getWorkerConflictList();
            List<WorkerConflict> workerConflictListNew = conflictIndex.getWorkerConflictList();
            List<String> illegalOrphanMessages = null;
            for (WorkerConflict workerConflictListOldWorkerConflict : workerConflictListOld) {
                if (!workerConflictListNew.contains(workerConflictListOldWorkerConflict)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain WorkerConflict " + workerConflictListOldWorkerConflict + " since its indexFk field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<WorkerConflict> attachedWorkerConflictListNew = new ArrayList<WorkerConflict>();
            for (WorkerConflict workerConflictListNewWorkerConflictToAttach : workerConflictListNew) {
                workerConflictListNewWorkerConflictToAttach = em.getReference(workerConflictListNewWorkerConflictToAttach.getClass(), workerConflictListNewWorkerConflictToAttach.getId());
                attachedWorkerConflictListNew.add(workerConflictListNewWorkerConflictToAttach);
            }
            workerConflictListNew = attachedWorkerConflictListNew;
            conflictIndex.setWorkerConflictList(workerConflictListNew);
            conflictIndex = em.merge(conflictIndex);
            for (WorkerConflict workerConflictListNewWorkerConflict : workerConflictListNew) {
                if (!workerConflictListOld.contains(workerConflictListNewWorkerConflict)) {
                    ConflictIndex oldIndexFkOfWorkerConflictListNewWorkerConflict = workerConflictListNewWorkerConflict.getIndexFk();
                    workerConflictListNewWorkerConflict.setIndexFk(conflictIndex);
                    workerConflictListNewWorkerConflict = em.merge(workerConflictListNewWorkerConflict);
                    if (oldIndexFkOfWorkerConflictListNewWorkerConflict != null && !oldIndexFkOfWorkerConflictListNewWorkerConflict.equals(conflictIndex)) {
                        oldIndexFkOfWorkerConflictListNewWorkerConflict.getWorkerConflictList().remove(workerConflictListNewWorkerConflict);
                        oldIndexFkOfWorkerConflictListNewWorkerConflict = em.merge(oldIndexFkOfWorkerConflictListNewWorkerConflict);
                    }
                }
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
                Long id = conflictIndex.getId();
                if (findConflictIndex(id) == null) {
                    throw new NonexistentEntityException("The conflictIndex with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ConflictIndex conflictIndex;
            try {
                conflictIndex = em.getReference(ConflictIndex.class, id);
                conflictIndex.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The conflictIndex with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<WorkerConflict> workerConflictListOrphanCheck = conflictIndex.getWorkerConflictList();
            for (WorkerConflict workerConflictListOrphanCheckWorkerConflict : workerConflictListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ConflictIndex (" + conflictIndex + ") cannot be destroyed since the WorkerConflict " + workerConflictListOrphanCheckWorkerConflict + " in its workerConflictList field has a non-nullable indexFk field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(conflictIndex);
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

    public List<ConflictIndex> findConflictIndexEntities() {
        return findConflictIndexEntities(true, -1, -1);
    }

    public List<ConflictIndex> findConflictIndexEntities(int maxResults, int firstResult) {
        return findConflictIndexEntities(false, maxResults, firstResult);
    }

    private List<ConflictIndex> findConflictIndexEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ConflictIndex.class));
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

    public ConflictIndex findConflictIndex(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ConflictIndex.class, id);
        } finally {
            em.close();
        }
    }

    public int getConflictIndexCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ConflictIndex> rt = cq.from(ConflictIndex.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
