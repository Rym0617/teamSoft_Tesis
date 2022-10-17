/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.JPAControllerClasses;

import org.teamsoft.entity.Cycle;
import org.teamsoft.entity.ProjectRoles;
import org.teamsoft.entity.ProjectStructure;
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
public class ProjectStructureJpaController implements Serializable {

    public ProjectStructureJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ProjectStructure projectStructure) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (projectStructure.getCycleList() == null) {
            projectStructure.setCycleList(new ArrayList<Cycle>());
        }
        if (projectStructure.getProjectRolesList() == null) {
            projectStructure.setProjectRolesList(new ArrayList<ProjectRoles>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Cycle> attachedCycleList = new ArrayList<Cycle>();
            for (Cycle cycleListCycleToAttach : projectStructure.getCycleList()) {
                cycleListCycleToAttach = em.getReference(cycleListCycleToAttach.getClass(), cycleListCycleToAttach.getId());
                attachedCycleList.add(cycleListCycleToAttach);
            }
            projectStructure.setCycleList(attachedCycleList);
            List<ProjectRoles> attachedProjectRolesList = new ArrayList<ProjectRoles>();
            for (ProjectRoles projectRolesListProjectRolesToAttach : projectStructure.getProjectRolesList()) {
                projectRolesListProjectRolesToAttach = em.getReference(projectRolesListProjectRolesToAttach.getClass(), projectRolesListProjectRolesToAttach.getId());
                attachedProjectRolesList.add(projectRolesListProjectRolesToAttach);
            }
            projectStructure.setProjectRolesList(attachedProjectRolesList);
            em.persist(projectStructure);
            for (Cycle cycleListCycle : projectStructure.getCycleList()) {
                ProjectStructure oldStructureFkOfCycleListCycle = cycleListCycle.getStructureFk();
                cycleListCycle.setStructureFk(projectStructure);
                cycleListCycle = em.merge(cycleListCycle);
                if (oldStructureFkOfCycleListCycle != null) {
                    oldStructureFkOfCycleListCycle.getCycleList().remove(cycleListCycle);
                    oldStructureFkOfCycleListCycle = em.merge(oldStructureFkOfCycleListCycle);
                }
            }
            for (ProjectRoles projectRolesListProjectRoles : projectStructure.getProjectRolesList()) {
                ProjectStructure oldProjectStructureFkOfProjectRolesListProjectRoles = projectRolesListProjectRoles.getProjectStructureFk();
                projectRolesListProjectRoles.setProjectStructureFk(projectStructure);
                projectRolesListProjectRoles = em.merge(projectRolesListProjectRoles);
                if (oldProjectStructureFkOfProjectRolesListProjectRoles != null) {
                    oldProjectStructureFkOfProjectRolesListProjectRoles.getProjectRolesList().remove(projectRolesListProjectRoles);
                    oldProjectStructureFkOfProjectRolesListProjectRoles = em.merge(oldProjectStructureFkOfProjectRolesListProjectRoles);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findProjectStructure(projectStructure.getId()) != null) {
                throw new PreexistingEntityException("ProjectStructure " + projectStructure + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ProjectStructure projectStructure) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ProjectStructure persistentProjectStructure = em.find(ProjectStructure.class, projectStructure.getId());
            List<Cycle> cycleListOld = persistentProjectStructure.getCycleList();
            List<Cycle> cycleListNew = projectStructure.getCycleList();
            List<ProjectRoles> projectRolesListOld = persistentProjectStructure.getProjectRolesList();
            List<ProjectRoles> projectRolesListNew = projectStructure.getProjectRolesList();
            List<String> illegalOrphanMessages = null;
            for (ProjectRoles projectRolesListOldProjectRoles : projectRolesListOld) {
                if (!projectRolesListNew.contains(projectRolesListOldProjectRoles)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ProjectRoles " + projectRolesListOldProjectRoles + " since its projectStructureFk field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Cycle> attachedCycleListNew = new ArrayList<Cycle>();
            for (Cycle cycleListNewCycleToAttach : cycleListNew) {
                cycleListNewCycleToAttach = em.getReference(cycleListNewCycleToAttach.getClass(), cycleListNewCycleToAttach.getId());
                attachedCycleListNew.add(cycleListNewCycleToAttach);
            }
            cycleListNew = attachedCycleListNew;
            projectStructure.setCycleList(cycleListNew);
            List<ProjectRoles> attachedProjectRolesListNew = new ArrayList<ProjectRoles>();
            for (ProjectRoles projectRolesListNewProjectRolesToAttach : projectRolesListNew) {
                projectRolesListNewProjectRolesToAttach = em.getReference(projectRolesListNewProjectRolesToAttach.getClass(), projectRolesListNewProjectRolesToAttach.getId());
                attachedProjectRolesListNew.add(projectRolesListNewProjectRolesToAttach);
            }
            projectRolesListNew = attachedProjectRolesListNew;
            projectStructure.setProjectRolesList(projectRolesListNew);
            projectStructure = em.merge(projectStructure);
            for (Cycle cycleListOldCycle : cycleListOld) {
                if (!cycleListNew.contains(cycleListOldCycle)) {
                    cycleListOldCycle.setStructureFk(null);
                    cycleListOldCycle = em.merge(cycleListOldCycle);
                }
            }
            for (Cycle cycleListNewCycle : cycleListNew) {
                if (!cycleListOld.contains(cycleListNewCycle)) {
                    ProjectStructure oldStructureFkOfCycleListNewCycle = cycleListNewCycle.getStructureFk();
                    cycleListNewCycle.setStructureFk(projectStructure);
                    cycleListNewCycle = em.merge(cycleListNewCycle);
                    if (oldStructureFkOfCycleListNewCycle != null && !oldStructureFkOfCycleListNewCycle.equals(projectStructure)) {
                        oldStructureFkOfCycleListNewCycle.getCycleList().remove(cycleListNewCycle);
                        oldStructureFkOfCycleListNewCycle = em.merge(oldStructureFkOfCycleListNewCycle);
                    }
                }
            }
            for (ProjectRoles projectRolesListNewProjectRoles : projectRolesListNew) {
                if (!projectRolesListOld.contains(projectRolesListNewProjectRoles)) {
                    ProjectStructure oldProjectStructureFkOfProjectRolesListNewProjectRoles = projectRolesListNewProjectRoles.getProjectStructureFk();
                    projectRolesListNewProjectRoles.setProjectStructureFk(projectStructure);
                    projectRolesListNewProjectRoles = em.merge(projectRolesListNewProjectRoles);
                    if (oldProjectStructureFkOfProjectRolesListNewProjectRoles != null && !oldProjectStructureFkOfProjectRolesListNewProjectRoles.equals(projectStructure)) {
                        oldProjectStructureFkOfProjectRolesListNewProjectRoles.getProjectRolesList().remove(projectRolesListNewProjectRoles);
                        oldProjectStructureFkOfProjectRolesListNewProjectRoles = em.merge(oldProjectStructureFkOfProjectRolesListNewProjectRoles);
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
                Long id = projectStructure.getId();
                if (findProjectStructure(id) == null) {
                    throw new NonexistentEntityException("The projectStructure with id " + id + " no longer exists.");
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
            ProjectStructure projectStructure;
            try {
                projectStructure = em.getReference(ProjectStructure.class, id);
                projectStructure.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The projectStructure with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<ProjectRoles> projectRolesListOrphanCheck = projectStructure.getProjectRolesList();
            for (ProjectRoles projectRolesListOrphanCheckProjectRoles : projectRolesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ProjectStructure (" + projectStructure + ") cannot be destroyed since the ProjectRoles " + projectRolesListOrphanCheckProjectRoles + " in its projectRolesList field has a non-nullable projectStructureFk field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Cycle> cycleList = projectStructure.getCycleList();
            for (Cycle cycleListCycle : cycleList) {
                cycleListCycle.setStructureFk(null);
                cycleListCycle = em.merge(cycleListCycle);
            }
            em.remove(projectStructure);
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

    public List<ProjectStructure> findProjectStructureEntities() {
        return findProjectStructureEntities(true, -1, -1);
    }

    public List<ProjectStructure> findProjectStructureEntities(int maxResults, int firstResult) {
        return findProjectStructureEntities(false, maxResults, firstResult);
    }

    private List<ProjectStructure> findProjectStructureEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ProjectStructure.class));
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

    public ProjectStructure findProjectStructure(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ProjectStructure.class, id);
        } finally {
            em.close();
        }
    }

    public int getProjectStructureCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ProjectStructure> rt = cq.from(ProjectStructure.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
