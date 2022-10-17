/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.JPAControllerClasses;

import org.teamsoft.entity.*;
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
public class RoleEvaluationJpaController implements Serializable {

    public RoleEvaluationJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(RoleEvaluation roleEvaluation) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Cycle cycleFk = roleEvaluation.getCycleFk();
            if (cycleFk != null) {
                cycleFk = em.getReference(cycleFk.getClass(), cycleFk.getId());
                roleEvaluation.setCycleFk(cycleFk);
            }
            Role roleFk = roleEvaluation.getRoleFk();
            if (roleFk != null) {
                roleFk = em.getReference(roleFk.getClass(), roleFk.getId());
                roleEvaluation.setRoleFk(roleFk);
            }
            RoleEval rolEvalFk = roleEvaluation.getRolEvalFk();
            if (rolEvalFk != null) {
                rolEvalFk = em.getReference(rolEvalFk.getClass(), rolEvalFk.getId());
                roleEvaluation.setRolEvalFk(rolEvalFk);
            }
            Worker workerFk = roleEvaluation.getWorkerFk();
            if (workerFk != null) {
                workerFk = em.getReference(workerFk.getClass(), workerFk.getId());
                roleEvaluation.setWorkerFk(workerFk);
            }
            em.persist(roleEvaluation);
            if (cycleFk != null) {
                cycleFk.getRoleEvaluationList().add(roleEvaluation);
                cycleFk = em.merge(cycleFk);
            }
            if (roleFk != null) {
                roleFk.getRoleEvaluationList().add(roleEvaluation);
                roleFk = em.merge(roleFk);
            }
            if (rolEvalFk != null) {
                rolEvalFk.getRoleEvaluationList().add(roleEvaluation);
                rolEvalFk = em.merge(rolEvalFk);
            }
            if (workerFk != null) {
                workerFk.getRoleEvaluationList().add(roleEvaluation);
                workerFk = em.merge(workerFk);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findRoleEvaluation(roleEvaluation.getId()) != null) {
                throw new PreexistingEntityException("RoleEvaluation " + roleEvaluation + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(RoleEvaluation roleEvaluation) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            RoleEvaluation persistentRoleEvaluation = em.find(RoleEvaluation.class, roleEvaluation.getId());
            Cycle cycleFkOld = persistentRoleEvaluation.getCycleFk();
            Cycle cycleFkNew = roleEvaluation.getCycleFk();
            Role roleFkOld = persistentRoleEvaluation.getRoleFk();
            Role roleFkNew = roleEvaluation.getRoleFk();
            RoleEval rolEvalFkOld = persistentRoleEvaluation.getRolEvalFk();
            RoleEval rolEvalFkNew = roleEvaluation.getRolEvalFk();
            Worker workerFkOld = persistentRoleEvaluation.getWorkerFk();
            Worker workerFkNew = roleEvaluation.getWorkerFk();
            if (cycleFkNew != null) {
                cycleFkNew = em.getReference(cycleFkNew.getClass(), cycleFkNew.getId());
                roleEvaluation.setCycleFk(cycleFkNew);
            }
            if (roleFkNew != null) {
                roleFkNew = em.getReference(roleFkNew.getClass(), roleFkNew.getId());
                roleEvaluation.setRoleFk(roleFkNew);
            }
            if (rolEvalFkNew != null) {
                rolEvalFkNew = em.getReference(rolEvalFkNew.getClass(), rolEvalFkNew.getId());
                roleEvaluation.setRolEvalFk(rolEvalFkNew);
            }
            if (workerFkNew != null) {
                workerFkNew = em.getReference(workerFkNew.getClass(), workerFkNew.getId());
                roleEvaluation.setWorkerFk(workerFkNew);
            }
            roleEvaluation = em.merge(roleEvaluation);
            if (cycleFkOld != null && !cycleFkOld.equals(cycleFkNew)) {
                cycleFkOld.getRoleEvaluationList().remove(roleEvaluation);
                cycleFkOld = em.merge(cycleFkOld);
            }
            if (cycleFkNew != null && !cycleFkNew.equals(cycleFkOld)) {
                cycleFkNew.getRoleEvaluationList().add(roleEvaluation);
                cycleFkNew = em.merge(cycleFkNew);
            }
            if (roleFkOld != null && !roleFkOld.equals(roleFkNew)) {
                roleFkOld.getRoleEvaluationList().remove(roleEvaluation);
                roleFkOld = em.merge(roleFkOld);
            }
            if (roleFkNew != null && !roleFkNew.equals(roleFkOld)) {
                roleFkNew.getRoleEvaluationList().add(roleEvaluation);
                roleFkNew = em.merge(roleFkNew);
            }
            if (rolEvalFkOld != null && !rolEvalFkOld.equals(rolEvalFkNew)) {
                rolEvalFkOld.getRoleEvaluationList().remove(roleEvaluation);
                rolEvalFkOld = em.merge(rolEvalFkOld);
            }
            if (rolEvalFkNew != null && !rolEvalFkNew.equals(rolEvalFkOld)) {
                rolEvalFkNew.getRoleEvaluationList().add(roleEvaluation);
                rolEvalFkNew = em.merge(rolEvalFkNew);
            }
            if (workerFkOld != null && !workerFkOld.equals(workerFkNew)) {
                workerFkOld.getRoleEvaluationList().remove(roleEvaluation);
                workerFkOld = em.merge(workerFkOld);
            }
            if (workerFkNew != null && !workerFkNew.equals(workerFkOld)) {
                workerFkNew.getRoleEvaluationList().add(roleEvaluation);
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
                Long id = roleEvaluation.getId();
                if (findRoleEvaluation(id) == null) {
                    throw new NonexistentEntityException("The roleEvaluation with id " + id + " no longer exists.");
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
            RoleEvaluation roleEvaluation;
            try {
                roleEvaluation = em.getReference(RoleEvaluation.class, id);
                roleEvaluation.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The roleEvaluation with id " + id + " no longer exists.", enfe);
            }
            Cycle cycleFk = roleEvaluation.getCycleFk();
            if (cycleFk != null) {
                cycleFk.getRoleEvaluationList().remove(roleEvaluation);
                cycleFk = em.merge(cycleFk);
            }
            Role roleFk = roleEvaluation.getRoleFk();
            if (roleFk != null) {
                roleFk.getRoleEvaluationList().remove(roleEvaluation);
                roleFk = em.merge(roleFk);
            }
            RoleEval rolEvalFk = roleEvaluation.getRolEvalFk();
            if (rolEvalFk != null) {
                rolEvalFk.getRoleEvaluationList().remove(roleEvaluation);
                rolEvalFk = em.merge(rolEvalFk);
            }
            Worker workerFk = roleEvaluation.getWorkerFk();
            if (workerFk != null) {
                workerFk.getRoleEvaluationList().remove(roleEvaluation);
                workerFk = em.merge(workerFk);
            }
            em.remove(roleEvaluation);
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

    public List<RoleEvaluation> findRoleEvaluationEntities() {
        return findRoleEvaluationEntities(true, -1, -1);
    }

    public List<RoleEvaluation> findRoleEvaluationEntities(int maxResults, int firstResult) {
        return findRoleEvaluationEntities(false, maxResults, firstResult);
    }

    private List<RoleEvaluation> findRoleEvaluationEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(RoleEvaluation.class));
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

    public RoleEvaluation findRoleEvaluation(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RoleEvaluation.class, id);
        } finally {
            em.close();
        }
    }

    public int getRoleEvaluationCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<RoleEvaluation> rt = cq.from(RoleEvaluation.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
