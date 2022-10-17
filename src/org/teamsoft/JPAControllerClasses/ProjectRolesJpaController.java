/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.JPAControllerClasses;

import org.teamsoft.entity.*;
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
public class ProjectRolesJpaController implements Serializable {

    public ProjectRolesJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ProjectRoles projectRoles) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (projectRoles.getProjectTechCompetenceList() == null) {
            projectRoles.setProjectTechCompetenceList(new ArrayList<ProjectTechCompetence>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ProjectStructure projectStructureFk = projectRoles.getProjectStructureFk();
            if (projectStructureFk != null) {
                projectStructureFk = em.getReference(projectStructureFk.getClass(), projectStructureFk.getId());
                projectRoles.setProjectStructureFk(projectStructureFk);
            }
            Role roleFk = projectRoles.getRoleFk();
            if (roleFk != null) {
                roleFk = em.getReference(roleFk.getClass(), roleFk.getId());
                projectRoles.setRoleFk(roleFk);
            }
            RoleLoad roleLoadFk = projectRoles.getRoleLoadFk();
            if (roleLoadFk != null) {
                roleLoadFk = em.getReference(roleLoadFk.getClass(), roleLoadFk.getId());
                projectRoles.setRoleLoadFk(roleLoadFk);
            }
            List<ProjectTechCompetence> attachedProjectTechCompetenceList = new ArrayList<ProjectTechCompetence>();
            for (ProjectTechCompetence projectTechCompetenceListProjectTechCompetenceToAttach : projectRoles.getProjectTechCompetenceList()) {
                projectTechCompetenceListProjectTechCompetenceToAttach = em.getReference(projectTechCompetenceListProjectTechCompetenceToAttach.getClass(), projectTechCompetenceListProjectTechCompetenceToAttach.getId());
                attachedProjectTechCompetenceList.add(projectTechCompetenceListProjectTechCompetenceToAttach);
            }
            projectRoles.setProjectTechCompetenceList(attachedProjectTechCompetenceList);
            em.persist(projectRoles);
            if (projectStructureFk != null) {
                projectStructureFk.getProjectRolesList().add(projectRoles);
                projectStructureFk = em.merge(projectStructureFk);
            }
            if (roleFk != null) {
                roleFk.getProjectRolesList().add(projectRoles);
                roleFk = em.merge(roleFk);
            }
            if (roleLoadFk != null) {
                roleLoadFk.getProjectRolesList().add(projectRoles);
                roleLoadFk = em.merge(roleLoadFk);
            }
            for (ProjectTechCompetence projectTechCompetenceListProjectTechCompetence : projectRoles.getProjectTechCompetenceList()) {
                ProjectRoles oldProjectRolesOfProjectTechCompetenceListProjectTechCompetence = projectTechCompetenceListProjectTechCompetence.getProjectRoles();
                projectTechCompetenceListProjectTechCompetence.setProjectRoles(projectRoles);
                projectTechCompetenceListProjectTechCompetence = em.merge(projectTechCompetenceListProjectTechCompetence);
                if (oldProjectRolesOfProjectTechCompetenceListProjectTechCompetence != null) {
                    oldProjectRolesOfProjectTechCompetenceListProjectTechCompetence.getProjectTechCompetenceList().remove(projectTechCompetenceListProjectTechCompetence);
                    oldProjectRolesOfProjectTechCompetenceListProjectTechCompetence = em.merge(oldProjectRolesOfProjectTechCompetenceListProjectTechCompetence);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findProjectRoles(projectRoles.getId()) != null) {
                throw new PreexistingEntityException("ProjectRoles " + projectRoles + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ProjectRoles projectRoles) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ProjectRoles persistentProjectRoles = em.find(ProjectRoles.class, projectRoles.getId());
            ProjectStructure projectStructureFkOld = persistentProjectRoles.getProjectStructureFk();
            ProjectStructure projectStructureFkNew = projectRoles.getProjectStructureFk();
            Role roleFkOld = persistentProjectRoles.getRoleFk();
            Role roleFkNew = projectRoles.getRoleFk();
            RoleLoad roleLoadFkOld = persistentProjectRoles.getRoleLoadFk();
            RoleLoad roleLoadFkNew = projectRoles.getRoleLoadFk();
            List<ProjectTechCompetence> projectTechCompetenceListOld = persistentProjectRoles.getProjectTechCompetenceList();
            List<ProjectTechCompetence> projectTechCompetenceListNew = projectRoles.getProjectTechCompetenceList();
            List<String> illegalOrphanMessages = null;
            for (ProjectTechCompetence projectTechCompetenceListOldProjectTechCompetence : projectTechCompetenceListOld) {
                if (!projectTechCompetenceListNew.contains(projectTechCompetenceListOldProjectTechCompetence)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ProjectTechCompetence " + projectTechCompetenceListOldProjectTechCompetence + " since its projectRoles field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (projectStructureFkNew != null) {
                projectStructureFkNew = em.getReference(projectStructureFkNew.getClass(), projectStructureFkNew.getId());
                projectRoles.setProjectStructureFk(projectStructureFkNew);
            }
            if (roleFkNew != null) {
                roleFkNew = em.getReference(roleFkNew.getClass(), roleFkNew.getId());
                projectRoles.setRoleFk(roleFkNew);
            }
            if (roleLoadFkNew != null) {
                roleLoadFkNew = em.getReference(roleLoadFkNew.getClass(), roleLoadFkNew.getId());
                projectRoles.setRoleLoadFk(roleLoadFkNew);
            }
            List<ProjectTechCompetence> attachedProjectTechCompetenceListNew = new ArrayList<ProjectTechCompetence>();
            for (ProjectTechCompetence projectTechCompetenceListNewProjectTechCompetenceToAttach : projectTechCompetenceListNew) {
                projectTechCompetenceListNewProjectTechCompetenceToAttach = em.getReference(projectTechCompetenceListNewProjectTechCompetenceToAttach.getClass(), projectTechCompetenceListNewProjectTechCompetenceToAttach.getId());
                attachedProjectTechCompetenceListNew.add(projectTechCompetenceListNewProjectTechCompetenceToAttach);
            }
            projectTechCompetenceListNew = attachedProjectTechCompetenceListNew;
            projectRoles.setProjectTechCompetenceList(projectTechCompetenceListNew);
            projectRoles = em.merge(projectRoles);
            if (projectStructureFkOld != null && !projectStructureFkOld.equals(projectStructureFkNew)) {
                projectStructureFkOld.getProjectRolesList().remove(projectRoles);
                projectStructureFkOld = em.merge(projectStructureFkOld);
            }
            if (projectStructureFkNew != null && !projectStructureFkNew.equals(projectStructureFkOld)) {
                projectStructureFkNew.getProjectRolesList().add(projectRoles);
                projectStructureFkNew = em.merge(projectStructureFkNew);
            }
            if (roleFkOld != null && !roleFkOld.equals(roleFkNew)) {
                roleFkOld.getProjectRolesList().remove(projectRoles);
                roleFkOld = em.merge(roleFkOld);
            }
            if (roleFkNew != null && !roleFkNew.equals(roleFkOld)) {
                roleFkNew.getProjectRolesList().add(projectRoles);
                roleFkNew = em.merge(roleFkNew);
            }
            if (roleLoadFkOld != null && !roleLoadFkOld.equals(roleLoadFkNew)) {
                roleLoadFkOld.getProjectRolesList().remove(projectRoles);
                roleLoadFkOld = em.merge(roleLoadFkOld);
            }
            if (roleLoadFkNew != null && !roleLoadFkNew.equals(roleLoadFkOld)) {
                roleLoadFkNew.getProjectRolesList().add(projectRoles);
                roleLoadFkNew = em.merge(roleLoadFkNew);
            }
            for (ProjectTechCompetence projectTechCompetenceListNewProjectTechCompetence : projectTechCompetenceListNew) {
                if (!projectTechCompetenceListOld.contains(projectTechCompetenceListNewProjectTechCompetence)) {
                    ProjectRoles oldProjectRolesOfProjectTechCompetenceListNewProjectTechCompetence = projectTechCompetenceListNewProjectTechCompetence.getProjectRoles();
                    projectTechCompetenceListNewProjectTechCompetence.setProjectRoles(projectRoles);
                    projectTechCompetenceListNewProjectTechCompetence = em.merge(projectTechCompetenceListNewProjectTechCompetence);
                    if (oldProjectRolesOfProjectTechCompetenceListNewProjectTechCompetence != null && !oldProjectRolesOfProjectTechCompetenceListNewProjectTechCompetence.equals(projectRoles)) {
                        oldProjectRolesOfProjectTechCompetenceListNewProjectTechCompetence.getProjectTechCompetenceList().remove(projectTechCompetenceListNewProjectTechCompetence);
                        oldProjectRolesOfProjectTechCompetenceListNewProjectTechCompetence = em.merge(oldProjectRolesOfProjectTechCompetenceListNewProjectTechCompetence);
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
                Long id = projectRoles.getId();
                if (findProjectRoles(id) == null) {
                    throw new NonexistentEntityException("The projectRoles with id " + id + " no longer exists.");
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
            ProjectRoles projectRoles;
            try {
                projectRoles = em.getReference(ProjectRoles.class, id);
                projectRoles.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The projectRoles with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<ProjectTechCompetence> projectTechCompetenceListOrphanCheck = projectRoles.getProjectTechCompetenceList();
            for (ProjectTechCompetence projectTechCompetenceListOrphanCheckProjectTechCompetence : projectTechCompetenceListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ProjectRoles (" + projectRoles + ") cannot be destroyed since the ProjectTechCompetence " + projectTechCompetenceListOrphanCheckProjectTechCompetence + " in its projectTechCompetenceList field has a non-nullable projectRoles field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            ProjectStructure projectStructureFk = projectRoles.getProjectStructureFk();
            if (projectStructureFk != null) {
                projectStructureFk.getProjectRolesList().remove(projectRoles);
                projectStructureFk = em.merge(projectStructureFk);
            }
            Role roleFk = projectRoles.getRoleFk();
            if (roleFk != null) {
                roleFk.getProjectRolesList().remove(projectRoles);
                roleFk = em.merge(roleFk);
            }
            RoleLoad roleLoadFk = projectRoles.getRoleLoadFk();
            if (roleLoadFk != null) {
                roleLoadFk.getProjectRolesList().remove(projectRoles);
                roleLoadFk = em.merge(roleLoadFk);
            }
            em.remove(projectRoles);
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

    public List<ProjectRoles> findProjectRolesEntities() {
        return findProjectRolesEntities(true, -1, -1);
    }

    public List<ProjectRoles> findProjectRolesEntities(int maxResults, int firstResult) {
        return findProjectRolesEntities(false, maxResults, firstResult);
    }

    private List<ProjectRoles> findProjectRolesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ProjectRoles.class));
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

    public ProjectRoles findProjectRoles(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ProjectRoles.class, id);
        } finally {
            em.close();
        }
    }

    public int getProjectRolesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ProjectRoles> rt = cq.from(ProjectRoles.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
