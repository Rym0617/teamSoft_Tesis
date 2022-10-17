/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.JPAControllerClasses;

import org.teamsoft.entity.ProjectRoles;
import org.teamsoft.entity.RoleLoad;
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
public class RoleLoadJpaController implements Serializable {

    public RoleLoadJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(RoleLoad roleLoad) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (roleLoad.getProjectRolesList() == null) {
            roleLoad.setProjectRolesList(new ArrayList<ProjectRoles>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<ProjectRoles> attachedProjectRolesList = new ArrayList<ProjectRoles>();
            for (ProjectRoles projectRolesListProjectRolesToAttach : roleLoad.getProjectRolesList()) {
                projectRolesListProjectRolesToAttach = em.getReference(projectRolesListProjectRolesToAttach.getClass(), projectRolesListProjectRolesToAttach.getId());
                attachedProjectRolesList.add(projectRolesListProjectRolesToAttach);
            }
            roleLoad.setProjectRolesList(attachedProjectRolesList);
            em.persist(roleLoad);
            for (ProjectRoles projectRolesListProjectRoles : roleLoad.getProjectRolesList()) {
                RoleLoad oldRoleLoadFkOfProjectRolesListProjectRoles = projectRolesListProjectRoles.getRoleLoadFk();
                projectRolesListProjectRoles.setRoleLoadFk(roleLoad);
                projectRolesListProjectRoles = em.merge(projectRolesListProjectRoles);
                if (oldRoleLoadFkOfProjectRolesListProjectRoles != null) {
                    oldRoleLoadFkOfProjectRolesListProjectRoles.getProjectRolesList().remove(projectRolesListProjectRoles);
                    oldRoleLoadFkOfProjectRolesListProjectRoles = em.merge(oldRoleLoadFkOfProjectRolesListProjectRoles);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findRoleLoad(roleLoad.getId()) != null) {
                throw new PreexistingEntityException("RoleLoad " + roleLoad + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(RoleLoad roleLoad) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            RoleLoad persistentRoleLoad = em.find(RoleLoad.class, roleLoad.getId());
            List<ProjectRoles> projectRolesListOld = persistentRoleLoad.getProjectRolesList();
            List<ProjectRoles> projectRolesListNew = roleLoad.getProjectRolesList();
            List<String> illegalOrphanMessages = null;
            for (ProjectRoles projectRolesListOldProjectRoles : projectRolesListOld) {
                if (!projectRolesListNew.contains(projectRolesListOldProjectRoles)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ProjectRoles " + projectRolesListOldProjectRoles + " since its roleLoadFk field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<ProjectRoles> attachedProjectRolesListNew = new ArrayList<ProjectRoles>();
            for (ProjectRoles projectRolesListNewProjectRolesToAttach : projectRolesListNew) {
                projectRolesListNewProjectRolesToAttach = em.getReference(projectRolesListNewProjectRolesToAttach.getClass(), projectRolesListNewProjectRolesToAttach.getId());
                attachedProjectRolesListNew.add(projectRolesListNewProjectRolesToAttach);
            }
            projectRolesListNew = attachedProjectRolesListNew;
            roleLoad.setProjectRolesList(projectRolesListNew);
            roleLoad = em.merge(roleLoad);
            for (ProjectRoles projectRolesListNewProjectRoles : projectRolesListNew) {
                if (!projectRolesListOld.contains(projectRolesListNewProjectRoles)) {
                    RoleLoad oldRoleLoadFkOfProjectRolesListNewProjectRoles = projectRolesListNewProjectRoles.getRoleLoadFk();
                    projectRolesListNewProjectRoles.setRoleLoadFk(roleLoad);
                    projectRolesListNewProjectRoles = em.merge(projectRolesListNewProjectRoles);
                    if (oldRoleLoadFkOfProjectRolesListNewProjectRoles != null && !oldRoleLoadFkOfProjectRolesListNewProjectRoles.equals(roleLoad)) {
                        oldRoleLoadFkOfProjectRolesListNewProjectRoles.getProjectRolesList().remove(projectRolesListNewProjectRoles);
                        oldRoleLoadFkOfProjectRolesListNewProjectRoles = em.merge(oldRoleLoadFkOfProjectRolesListNewProjectRoles);
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
                Long id = roleLoad.getId();
                if (findRoleLoad(id) == null) {
                    throw new NonexistentEntityException("The roleLoad with id " + id + " no longer exists.");
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
            RoleLoad roleLoad;
            try {
                roleLoad = em.getReference(RoleLoad.class, id);
                roleLoad.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The roleLoad with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<ProjectRoles> projectRolesListOrphanCheck = roleLoad.getProjectRolesList();
            for (ProjectRoles projectRolesListOrphanCheckProjectRoles : projectRolesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This RoleLoad (" + roleLoad + ") cannot be destroyed since the ProjectRoles " + projectRolesListOrphanCheckProjectRoles + " in its projectRolesList field has a non-nullable roleLoadFk field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(roleLoad);
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

    public List<RoleLoad> findRoleLoadEntities() {
        return findRoleLoadEntities(true, -1, -1);
    }

    public List<RoleLoad> findRoleLoadEntities(int maxResults, int firstResult) {
        return findRoleLoadEntities(false, maxResults, firstResult);
    }

    private List<RoleLoad> findRoleLoadEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(RoleLoad.class));
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

    public RoleLoad findRoleLoad(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RoleLoad.class, id);
        } finally {
            em.close();
        }
    }

    public int getRoleLoadCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<RoleLoad> rt = cq.from(RoleLoad.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
