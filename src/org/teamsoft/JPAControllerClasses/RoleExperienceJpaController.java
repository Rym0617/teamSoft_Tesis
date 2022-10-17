/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.JPAControllerClasses;

import org.teamsoft.entity.Role;
import org.teamsoft.entity.RoleExperience;
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
public class RoleExperienceJpaController implements Serializable {

    public RoleExperienceJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(RoleExperience roleExperience) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Role roleFk = roleExperience.getRoleFk();
            if (roleFk != null) {
                roleFk = em.getReference(roleFk.getClass(), roleFk.getId());
                roleExperience.setRoleFk(roleFk);
            }
            Worker workerFk = roleExperience.getWorkerFk();
            if (workerFk != null) {
                workerFk = em.getReference(workerFk.getClass(), workerFk.getId());
                roleExperience.setWorkerFk(workerFk);
            }
            em.persist(roleExperience);
            if (roleFk != null) {
                roleFk.getRoleExperienceList().add(roleExperience);
                roleFk = em.merge(roleFk);
            }
            if (workerFk != null) {
                workerFk.getRoleExperienceList().add(roleExperience);
                workerFk = em.merge(workerFk);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findRoleExperience(roleExperience.getId()) != null) {
                throw new PreexistingEntityException("RoleExperience " + roleExperience + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(RoleExperience roleExperience) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            RoleExperience persistentRoleExperience = em.find(RoleExperience.class, roleExperience.getId());
            Role roleFkOld = persistentRoleExperience.getRoleFk();
            Role roleFkNew = roleExperience.getRoleFk();
            Worker workerFkOld = persistentRoleExperience.getWorkerFk();
            Worker workerFkNew = roleExperience.getWorkerFk();
            if (roleFkNew != null) {
                roleFkNew = em.getReference(roleFkNew.getClass(), roleFkNew.getId());
                roleExperience.setRoleFk(roleFkNew);
            }
            if (workerFkNew != null) {
                workerFkNew = em.getReference(workerFkNew.getClass(), workerFkNew.getId());
                roleExperience.setWorkerFk(workerFkNew);
            }
            roleExperience = em.merge(roleExperience);
            if (roleFkOld != null && !roleFkOld.equals(roleFkNew)) {
                roleFkOld.getRoleExperienceList().remove(roleExperience);
                roleFkOld = em.merge(roleFkOld);
            }
            if (roleFkNew != null && !roleFkNew.equals(roleFkOld)) {
                roleFkNew.getRoleExperienceList().add(roleExperience);
                roleFkNew = em.merge(roleFkNew);
            }
            if (workerFkOld != null && !workerFkOld.equals(workerFkNew)) {
                workerFkOld.getRoleExperienceList().remove(roleExperience);
                workerFkOld = em.merge(workerFkOld);
            }
            if (workerFkNew != null && !workerFkNew.equals(workerFkOld)) {
                workerFkNew.getRoleExperienceList().add(roleExperience);
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
                Long id = roleExperience.getId();
                if (findRoleExperience(id) == null) {
                    throw new NonexistentEntityException("The roleExperience with id " + id + " no longer exists.");
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
            RoleExperience roleExperience;
            try {
                roleExperience = em.getReference(RoleExperience.class, id);
                roleExperience.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The roleExperience with id " + id + " no longer exists.", enfe);
            }
            Role roleFk = roleExperience.getRoleFk();
            if (roleFk != null) {
                roleFk.getRoleExperienceList().remove(roleExperience);
                roleFk = em.merge(roleFk);
            }
            Worker workerFk = roleExperience.getWorkerFk();
            if (workerFk != null) {
                workerFk.getRoleExperienceList().remove(roleExperience);
                workerFk = em.merge(workerFk);
            }
            em.remove(roleExperience);
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

    public List<RoleExperience> findRoleExperienceEntities() {
        return findRoleExperienceEntities(true, -1, -1);
    }

    public List<RoleExperience> findRoleExperienceEntities(int maxResults, int firstResult) {
        return findRoleExperienceEntities(false, maxResults, firstResult);
    }

    private List<RoleExperience> findRoleExperienceEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(RoleExperience.class));
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

    public RoleExperience findRoleExperience(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RoleExperience.class, id);
        } finally {
            em.close();
        }
    }

    public int getRoleExperienceCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<RoleExperience> rt = cq.from(RoleExperience.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
