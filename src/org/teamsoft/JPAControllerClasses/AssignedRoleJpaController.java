/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.JPAControllerClasses;

import org.teamsoft.entity.AssignedRole;
import org.teamsoft.entity.Cycle;
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
public class AssignedRoleJpaController implements Serializable {

    public AssignedRoleJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AssignedRole assignedRole) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Cycle cyclesFk = assignedRole.getCyclesFk();
            if (cyclesFk != null) {
                cyclesFk = em.getReference(cyclesFk.getClass(), cyclesFk.getId());
                assignedRole.setCyclesFk(cyclesFk);
            }
            Role rolesFk = assignedRole.getRolesFk();
            if (rolesFk != null) {
                rolesFk = em.getReference(rolesFk.getClass(), rolesFk.getId());
                assignedRole.setRolesFk(rolesFk);
            }
            Worker workersFk = assignedRole.getWorkersFk();
            if (workersFk != null) {
                workersFk = em.getReference(workersFk.getClass(), workersFk.getId());
                assignedRole.setWorkersFk(workersFk);
            }
            em.persist(assignedRole);
            if (cyclesFk != null) {
                cyclesFk.getAssignedRoleList().add(assignedRole);
                cyclesFk = em.merge(cyclesFk);
            }
            if (rolesFk != null) {
                rolesFk.getAssignedRoleList().add(assignedRole);
                rolesFk = em.merge(rolesFk);
            }
            if (workersFk != null) {
                workersFk.getAssignedRoleList().add(assignedRole);
                workersFk = em.merge(workersFk);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findAssignedRole(assignedRole.getId()) != null) {
                throw new PreexistingEntityException("AssignedRole " + assignedRole + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AssignedRole assignedRole) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            AssignedRole persistentAssignedRole = em.find(AssignedRole.class, assignedRole.getId());
            Cycle cyclesFkOld = persistentAssignedRole.getCyclesFk();
            Cycle cyclesFkNew = assignedRole.getCyclesFk();
            Role rolesFkOld = persistentAssignedRole.getRolesFk();
            Role rolesFkNew = assignedRole.getRolesFk();
            Worker workersFkOld = persistentAssignedRole.getWorkersFk();
            Worker workersFkNew = assignedRole.getWorkersFk();
            if (cyclesFkNew != null) {
                cyclesFkNew = em.getReference(cyclesFkNew.getClass(), cyclesFkNew.getId());
                assignedRole.setCyclesFk(cyclesFkNew);
            }
            if (rolesFkNew != null) {
                rolesFkNew = em.getReference(rolesFkNew.getClass(), rolesFkNew.getId());
                assignedRole.setRolesFk(rolesFkNew);
            }
            if (workersFkNew != null) {
                workersFkNew = em.getReference(workersFkNew.getClass(), workersFkNew.getId());
                assignedRole.setWorkersFk(workersFkNew);
            }
            assignedRole = em.merge(assignedRole);
            if (cyclesFkOld != null && !cyclesFkOld.equals(cyclesFkNew)) {
                cyclesFkOld.getAssignedRoleList().remove(assignedRole);
                cyclesFkOld = em.merge(cyclesFkOld);
            }
            if (cyclesFkNew != null && !cyclesFkNew.equals(cyclesFkOld)) {
                cyclesFkNew.getAssignedRoleList().add(assignedRole);
                cyclesFkNew = em.merge(cyclesFkNew);
            }
            if (rolesFkOld != null && !rolesFkOld.equals(rolesFkNew)) {
                rolesFkOld.getAssignedRoleList().remove(assignedRole);
                rolesFkOld = em.merge(rolesFkOld);
            }
            if (rolesFkNew != null && !rolesFkNew.equals(rolesFkOld)) {
                rolesFkNew.getAssignedRoleList().add(assignedRole);
                rolesFkNew = em.merge(rolesFkNew);
            }
            if (workersFkOld != null && !workersFkOld.equals(workersFkNew)) {
                workersFkOld.getAssignedRoleList().remove(assignedRole);
                workersFkOld = em.merge(workersFkOld);
            }
            if (workersFkNew != null && !workersFkNew.equals(workersFkOld)) {
                workersFkNew.getAssignedRoleList().add(assignedRole);
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
                Long id = assignedRole.getId();
                if (findAssignedRole(id) == null) {
                    throw new NonexistentEntityException("The assignedRole with id " + id + " no longer exists.");
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
            AssignedRole assignedRole;
            try {
                assignedRole = em.getReference(AssignedRole.class, id);
                assignedRole.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The assignedRole with id " + id + " no longer exists.", enfe);
            }
            Cycle cyclesFk = assignedRole.getCyclesFk();
            if (cyclesFk != null) {
                cyclesFk.getAssignedRoleList().remove(assignedRole);
                cyclesFk = em.merge(cyclesFk);
            }
            Role rolesFk = assignedRole.getRolesFk();
            if (rolesFk != null) {
                rolesFk.getAssignedRoleList().remove(assignedRole);
                rolesFk = em.merge(rolesFk);
            }
            Worker workersFk = assignedRole.getWorkersFk();
            if (workersFk != null) {
                workersFk.getAssignedRoleList().remove(assignedRole);
                workersFk = em.merge(workersFk);
            }
            em.remove(assignedRole);
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

    public List<AssignedRole> findAssignedRoleEntities() {
        return findAssignedRoleEntities(true, -1, -1);
    }

    public List<AssignedRole> findAssignedRoleEntities(int maxResults, int firstResult) {
        return findAssignedRoleEntities(false, maxResults, firstResult);
    }

    private List<AssignedRole> findAssignedRoleEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AssignedRole.class));
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

    public AssignedRole findAssignedRole(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AssignedRole.class, id);
        } finally {
            em.close();
        }
    }

    public int getAssignedRoleCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AssignedRole> rt = cq.from(AssignedRole.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
