/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.JPAControllerClasses;

import org.teamsoft.entity.Cycle;
import org.teamsoft.entity.RoleEval;
import org.teamsoft.entity.RoleEvalProject;
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
public class RoleEvalProjectJpaController implements Serializable {

    public RoleEvalProjectJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(RoleEvalProject roleEvalProject) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Cycle cycleFk = roleEvalProject.getCycleFk();
            if (cycleFk != null) {
                cycleFk = em.getReference(cycleFk.getClass(), cycleFk.getId());
                roleEvalProject.setCycleFk(cycleFk);
            }
            RoleEval roleEvalFk = roleEvalProject.getRoleEvalFk();
            if (roleEvalFk != null) {
                roleEvalFk = em.getReference(roleEvalFk.getClass(), roleEvalFk.getId());
                roleEvalProject.setRoleEvalFk(roleEvalFk);
            }
            em.persist(roleEvalProject);
            if (cycleFk != null) {
                cycleFk.getRoleEvalProjectList().add(roleEvalProject);
                cycleFk = em.merge(cycleFk);
            }
            if (roleEvalFk != null) {
                roleEvalFk.getRoleEvalProjectList().add(roleEvalProject);
                roleEvalFk = em.merge(roleEvalFk);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findRoleEvalProject(roleEvalProject.getId()) != null) {
                throw new PreexistingEntityException("RoleEvalProject " + roleEvalProject + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(RoleEvalProject roleEvalProject) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            RoleEvalProject persistentRoleEvalProject = em.find(RoleEvalProject.class, roleEvalProject.getId());
            Cycle cycleFkOld = persistentRoleEvalProject.getCycleFk();
            Cycle cycleFkNew = roleEvalProject.getCycleFk();
            RoleEval roleEvalFkOld = persistentRoleEvalProject.getRoleEvalFk();
            RoleEval roleEvalFkNew = roleEvalProject.getRoleEvalFk();
            if (cycleFkNew != null) {
                cycleFkNew = em.getReference(cycleFkNew.getClass(), cycleFkNew.getId());
                roleEvalProject.setCycleFk(cycleFkNew);
            }
            if (roleEvalFkNew != null) {
                roleEvalFkNew = em.getReference(roleEvalFkNew.getClass(), roleEvalFkNew.getId());
                roleEvalProject.setRoleEvalFk(roleEvalFkNew);
            }
            roleEvalProject = em.merge(roleEvalProject);
            if (cycleFkOld != null && !cycleFkOld.equals(cycleFkNew)) {
                cycleFkOld.getRoleEvalProjectList().remove(roleEvalProject);
                cycleFkOld = em.merge(cycleFkOld);
            }
            if (cycleFkNew != null && !cycleFkNew.equals(cycleFkOld)) {
                cycleFkNew.getRoleEvalProjectList().add(roleEvalProject);
                cycleFkNew = em.merge(cycleFkNew);
            }
            if (roleEvalFkOld != null && !roleEvalFkOld.equals(roleEvalFkNew)) {
                roleEvalFkOld.getRoleEvalProjectList().remove(roleEvalProject);
                roleEvalFkOld = em.merge(roleEvalFkOld);
            }
            if (roleEvalFkNew != null && !roleEvalFkNew.equals(roleEvalFkOld)) {
                roleEvalFkNew.getRoleEvalProjectList().add(roleEvalProject);
                roleEvalFkNew = em.merge(roleEvalFkNew);
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
                Long id = roleEvalProject.getId();
                if (findRoleEvalProject(id) == null) {
                    throw new NonexistentEntityException("The roleEvalProject with id " + id + " no longer exists.");
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
            RoleEvalProject roleEvalProject;
            try {
                roleEvalProject = em.getReference(RoleEvalProject.class, id);
                roleEvalProject.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The roleEvalProject with id " + id + " no longer exists.", enfe);
            }
            Cycle cycleFk = roleEvalProject.getCycleFk();
            if (cycleFk != null) {
                cycleFk.getRoleEvalProjectList().remove(roleEvalProject);
                cycleFk = em.merge(cycleFk);
            }
            RoleEval roleEvalFk = roleEvalProject.getRoleEvalFk();
            if (roleEvalFk != null) {
                roleEvalFk.getRoleEvalProjectList().remove(roleEvalProject);
                roleEvalFk = em.merge(roleEvalFk);
            }
            em.remove(roleEvalProject);
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

    public List<RoleEvalProject> findRoleEvalProjectEntities() {
        return findRoleEvalProjectEntities(true, -1, -1);
    }

    public List<RoleEvalProject> findRoleEvalProjectEntities(int maxResults, int firstResult) {
        return findRoleEvalProjectEntities(false, maxResults, firstResult);
    }

    private List<RoleEvalProject> findRoleEvalProjectEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(RoleEvalProject.class));
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

    public RoleEvalProject findRoleEvalProject(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RoleEvalProject.class, id);
        } finally {
            em.close();
        }
    }

    public int getRoleEvalProjectCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<RoleEvalProject> rt = cq.from(RoleEvalProject.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
