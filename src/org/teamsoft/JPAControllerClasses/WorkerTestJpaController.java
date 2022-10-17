/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.JPAControllerClasses;

import org.teamsoft.entity.Worker;
import org.teamsoft.entity.WorkerTest;
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
public class WorkerTestJpaController implements Serializable {

    public WorkerTestJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(WorkerTest workerTest) throws IllegalOrphanException, PreexistingEntityException, RollbackFailureException, Exception {
        List<String> illegalOrphanMessages = null;
        Worker workerFkOrphanCheck = workerTest.getWorkerFk();
        if (workerFkOrphanCheck != null) {
            WorkerTest oldWorkerTestOfWorkerFk = workerFkOrphanCheck.getWorkerTest();
            if (oldWorkerTestOfWorkerFk != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Worker " + workerFkOrphanCheck + " already has an item of type WorkerTest whose workerFk column cannot be null. Please make another selection for the workerFk field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Worker workerFk = workerTest.getWorkerFk();
            if (workerFk != null) {
                workerFk = em.getReference(workerFk.getClass(), workerFk.getId());
                workerTest.setWorkerFk(workerFk);
            }
            em.persist(workerTest);
            if (workerFk != null) {
                workerFk.setWorkerTest(workerTest);
                workerFk = em.merge(workerFk);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findWorkerTest(workerTest.getId()) != null) {
                throw new PreexistingEntityException("WorkerTest " + workerTest + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(WorkerTest workerTest) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            WorkerTest persistentWorkerTest = em.find(WorkerTest.class, workerTest.getId());
            Worker workerFkOld = persistentWorkerTest.getWorkerFk();
            Worker workerFkNew = workerTest.getWorkerFk();
            List<String> illegalOrphanMessages = null;
            if (workerFkNew != null && !workerFkNew.equals(workerFkOld)) {
                WorkerTest oldWorkerTestOfWorkerFk = workerFkNew.getWorkerTest();
                if (oldWorkerTestOfWorkerFk != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Worker " + workerFkNew + " already has an item of type WorkerTest whose workerFk column cannot be null. Please make another selection for the workerFk field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (workerFkNew != null) {
                workerFkNew = em.getReference(workerFkNew.getClass(), workerFkNew.getId());
                workerTest.setWorkerFk(workerFkNew);
            }
            workerTest = em.merge(workerTest);
            if (workerFkOld != null && !workerFkOld.equals(workerFkNew)) {
                workerFkOld.setWorkerTest(null);
                workerFkOld = em.merge(workerFkOld);
            }
            if (workerFkNew != null && !workerFkNew.equals(workerFkOld)) {
                workerFkNew.setWorkerTest(workerTest);
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
                Long id = workerTest.getId();
                if (findWorkerTest(id) == null) {
                    throw new NonexistentEntityException("The workerTest with id " + id + " no longer exists.");
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
            WorkerTest workerTest;
            try {
                workerTest = em.getReference(WorkerTest.class, id);
                workerTest.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The workerTest with id " + id + " no longer exists.", enfe);
            }
            Worker workerFk = workerTest.getWorkerFk();
            if (workerFk != null) {
                workerFk.setWorkerTest(null);
                workerFk = em.merge(workerFk);
            }
            em.remove(workerTest);
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

    public List<WorkerTest> findWorkerTestEntities() {
        return findWorkerTestEntities(true, -1, -1);
    }

    public List<WorkerTest> findWorkerTestEntities(int maxResults, int firstResult) {
        return findWorkerTestEntities(false, maxResults, firstResult);
    }

    private List<WorkerTest> findWorkerTestEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(WorkerTest.class));
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

    public WorkerTest findWorkerTest(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(WorkerTest.class, id);
        } finally {
            em.close();
        }
    }

    public int getWorkerTestCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<WorkerTest> rt = cq.from(WorkerTest.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
