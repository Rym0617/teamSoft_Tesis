/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.JPAControllerClasses;

import org.teamsoft.entity.Cycle;
import org.teamsoft.entity.ProjectRoleIncomp;
import org.teamsoft.entity.Role;
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
public class ProjectRoleIncompJpaController implements Serializable {

    public ProjectRoleIncompJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ProjectRoleIncomp projectRoleIncomp) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Cycle cycleFk = projectRoleIncomp.getCycleFk();
            if (cycleFk != null) {
                cycleFk = em.getReference(cycleFk.getClass(), cycleFk.getId());
                projectRoleIncomp.setCycleFk(cycleFk);
            }
            Role roleAFk = projectRoleIncomp.getRoleAFk();
            if (roleAFk != null) {
                roleAFk = em.getReference(roleAFk.getClass(), roleAFk.getId());
                projectRoleIncomp.setRoleAFk(roleAFk);
            }
            Role roleBFk = projectRoleIncomp.getRoleBFk();
            if (roleBFk != null) {
                roleBFk = em.getReference(roleBFk.getClass(), roleBFk.getId());
                projectRoleIncomp.setRoleBFk(roleBFk);
            }
            em.persist(projectRoleIncomp);
            if (cycleFk != null) {
                cycleFk.getProjectRoleIncompList().add(projectRoleIncomp);
                cycleFk = em.merge(cycleFk);
            }
            if (roleAFk != null) {
                roleAFk.getProjectRoleIncompList().add(projectRoleIncomp);
                roleAFk = em.merge(roleAFk);
            }
            if (roleBFk != null) {
                roleBFk.getProjectRoleIncompList().add(projectRoleIncomp);
                roleBFk = em.merge(roleBFk);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findProjectRoleIncomp(projectRoleIncomp.getId()) != null) {
                throw new PreexistingEntityException("ProjectRoleIncomp " + projectRoleIncomp + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ProjectRoleIncomp projectRoleIncomp) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ProjectRoleIncomp persistentProjectRoleIncomp = em.find(ProjectRoleIncomp.class, projectRoleIncomp.getId());
            Cycle cycleFkOld = persistentProjectRoleIncomp.getCycleFk();
            Cycle cycleFkNew = projectRoleIncomp.getCycleFk();
            Role roleAFkOld = persistentProjectRoleIncomp.getRoleAFk();
            Role roleAFkNew = projectRoleIncomp.getRoleAFk();
            Role roleBFkOld = persistentProjectRoleIncomp.getRoleBFk();
            Role roleBFkNew = projectRoleIncomp.getRoleBFk();
            if (cycleFkNew != null) {
                cycleFkNew = em.getReference(cycleFkNew.getClass(), cycleFkNew.getId());
                projectRoleIncomp.setCycleFk(cycleFkNew);
            }
            if (roleAFkNew != null) {
                roleAFkNew = em.getReference(roleAFkNew.getClass(), roleAFkNew.getId());
                projectRoleIncomp.setRoleAFk(roleAFkNew);
            }
            if (roleBFkNew != null) {
                roleBFkNew = em.getReference(roleBFkNew.getClass(), roleBFkNew.getId());
                projectRoleIncomp.setRoleBFk(roleBFkNew);
            }
            projectRoleIncomp = em.merge(projectRoleIncomp);
            if (cycleFkOld != null && !cycleFkOld.equals(cycleFkNew)) {
                cycleFkOld.getProjectRoleIncompList().remove(projectRoleIncomp);
                cycleFkOld = em.merge(cycleFkOld);
            }
            if (cycleFkNew != null && !cycleFkNew.equals(cycleFkOld)) {
                cycleFkNew.getProjectRoleIncompList().add(projectRoleIncomp);
                cycleFkNew = em.merge(cycleFkNew);
            }
            if (roleAFkOld != null && !roleAFkOld.equals(roleAFkNew)) {
                roleAFkOld.getProjectRoleIncompList().remove(projectRoleIncomp);
                roleAFkOld = em.merge(roleAFkOld);
            }
            if (roleAFkNew != null && !roleAFkNew.equals(roleAFkOld)) {
                roleAFkNew.getProjectRoleIncompList().add(projectRoleIncomp);
                roleAFkNew = em.merge(roleAFkNew);
            }
            if (roleBFkOld != null && !roleBFkOld.equals(roleBFkNew)) {
                roleBFkOld.getProjectRoleIncompList().remove(projectRoleIncomp);
                roleBFkOld = em.merge(roleBFkOld);
            }
            if (roleBFkNew != null && !roleBFkNew.equals(roleBFkOld)) {
                roleBFkNew.getProjectRoleIncompList().add(projectRoleIncomp);
                roleBFkNew = em.merge(roleBFkNew);
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
                Long id = projectRoleIncomp.getId();
                if (findProjectRoleIncomp(id) == null) {
                    throw new NonexistentEntityException("The projectRoleIncomp with id " + id + " no longer exists.");
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
            ProjectRoleIncomp projectRoleIncomp;
            try {
                projectRoleIncomp = em.getReference(ProjectRoleIncomp.class, id);
                projectRoleIncomp.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The projectRoleIncomp with id " + id + " no longer exists.", enfe);
            }
            Cycle cycleFk = projectRoleIncomp.getCycleFk();
            if (cycleFk != null) {
                cycleFk.getProjectRoleIncompList().remove(projectRoleIncomp);
                cycleFk = em.merge(cycleFk);
            }
            Role roleAFk = projectRoleIncomp.getRoleAFk();
            if (roleAFk != null) {
                roleAFk.getProjectRoleIncompList().remove(projectRoleIncomp);
                roleAFk = em.merge(roleAFk);
            }
            Role roleBFk = projectRoleIncomp.getRoleBFk();
            if (roleBFk != null) {
                roleBFk.getProjectRoleIncompList().remove(projectRoleIncomp);
                roleBFk = em.merge(roleBFk);
            }
            em.remove(projectRoleIncomp);
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

    public List<ProjectRoleIncomp> findProjectRoleIncompEntities() {
        return findProjectRoleIncompEntities(true, -1, -1);
    }

    public List<ProjectRoleIncomp> findProjectRoleIncompEntities(int maxResults, int firstResult) {
        return findProjectRoleIncompEntities(false, maxResults, firstResult);
    }

    private List<ProjectRoleIncomp> findProjectRoleIncompEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ProjectRoleIncomp.class));
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

    public ProjectRoleIncomp findProjectRoleIncomp(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ProjectRoleIncomp.class, id);
        } finally {
            em.close();
        }
    }

    public int getProjectRoleIncompCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ProjectRoleIncomp> rt = cq.from(ProjectRoleIncomp.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
