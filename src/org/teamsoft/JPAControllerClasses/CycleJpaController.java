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
public class CycleJpaController implements Serializable {

    public CycleJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cycle cycle) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (cycle.getAssignedRoleList() == null) {
            cycle.setAssignedRoleList(new ArrayList<AssignedRole>());
        }
        if (cycle.getProjectRoleIncompList() == null) {
            cycle.setProjectRoleIncompList(new ArrayList<ProjectRoleIncomp>());
        }
        if (cycle.getRoleEvaluationList() == null) {
            cycle.setRoleEvaluationList(new ArrayList<RoleEvaluation>());
        }
        if (cycle.getRoleEvalProjectList() == null) {
            cycle.setRoleEvalProjectList(new ArrayList<RoleEvalProject>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Project projectFk = cycle.getProjectFk();
            if (projectFk != null) {
                projectFk = em.getReference(projectFk.getClass(), projectFk.getId());
                cycle.setProjectFk(projectFk);
            }
            ProjectStructure structureFk = cycle.getStructureFk();
            if (structureFk != null) {
                structureFk = em.getReference(structureFk.getClass(), structureFk.getId());
                cycle.setStructureFk(structureFk);
            }
            List<AssignedRole> attachedAssignedRoleList = new ArrayList<AssignedRole>();
            for (AssignedRole assignedRoleListAssignedRoleToAttach : cycle.getAssignedRoleList()) {
                assignedRoleListAssignedRoleToAttach = em.getReference(assignedRoleListAssignedRoleToAttach.getClass(), assignedRoleListAssignedRoleToAttach.getId());
                attachedAssignedRoleList.add(assignedRoleListAssignedRoleToAttach);
            }
            cycle.setAssignedRoleList(attachedAssignedRoleList);
            List<ProjectRoleIncomp> attachedProjectRoleIncompList = new ArrayList<ProjectRoleIncomp>();
            for (ProjectRoleIncomp projectRoleIncompListProjectRoleIncompToAttach : cycle.getProjectRoleIncompList()) {
                projectRoleIncompListProjectRoleIncompToAttach = em.getReference(projectRoleIncompListProjectRoleIncompToAttach.getClass(), projectRoleIncompListProjectRoleIncompToAttach.getId());
                attachedProjectRoleIncompList.add(projectRoleIncompListProjectRoleIncompToAttach);
            }
            cycle.setProjectRoleIncompList(attachedProjectRoleIncompList);
            List<RoleEvaluation> attachedRoleEvaluationList = new ArrayList<RoleEvaluation>();
            for (RoleEvaluation roleEvaluationListRoleEvaluationToAttach : cycle.getRoleEvaluationList()) {
                roleEvaluationListRoleEvaluationToAttach = em.getReference(roleEvaluationListRoleEvaluationToAttach.getClass(), roleEvaluationListRoleEvaluationToAttach.getId());
                attachedRoleEvaluationList.add(roleEvaluationListRoleEvaluationToAttach);
            }
            cycle.setRoleEvaluationList(attachedRoleEvaluationList);
            List<RoleEvalProject> attachedRoleEvalProjectList = new ArrayList<RoleEvalProject>();
            for (RoleEvalProject roleEvalProjectListRoleEvalProjectToAttach : cycle.getRoleEvalProjectList()) {
                roleEvalProjectListRoleEvalProjectToAttach = em.getReference(roleEvalProjectListRoleEvalProjectToAttach.getClass(), roleEvalProjectListRoleEvalProjectToAttach.getId());
                attachedRoleEvalProjectList.add(roleEvalProjectListRoleEvalProjectToAttach);
            }
            cycle.setRoleEvalProjectList(attachedRoleEvalProjectList);
            em.persist(cycle);
            if (projectFk != null) {
                projectFk.getCycleList().add(cycle);
                projectFk = em.merge(projectFk);
            }
            if (structureFk != null) {
                structureFk.getCycleList().add(cycle);
                structureFk = em.merge(structureFk);
            }
            for (AssignedRole assignedRoleListAssignedRole : cycle.getAssignedRoleList()) {
                Cycle oldCyclesFkOfAssignedRoleListAssignedRole = assignedRoleListAssignedRole.getCyclesFk();
                assignedRoleListAssignedRole.setCyclesFk(cycle);
                assignedRoleListAssignedRole = em.merge(assignedRoleListAssignedRole);
                if (oldCyclesFkOfAssignedRoleListAssignedRole != null) {
                    oldCyclesFkOfAssignedRoleListAssignedRole.getAssignedRoleList().remove(assignedRoleListAssignedRole);
                    oldCyclesFkOfAssignedRoleListAssignedRole = em.merge(oldCyclesFkOfAssignedRoleListAssignedRole);
                }
            }
            for (ProjectRoleIncomp projectRoleIncompListProjectRoleIncomp : cycle.getProjectRoleIncompList()) {
                Cycle oldCycleFkOfProjectRoleIncompListProjectRoleIncomp = projectRoleIncompListProjectRoleIncomp.getCycleFk();
                projectRoleIncompListProjectRoleIncomp.setCycleFk(cycle);
                projectRoleIncompListProjectRoleIncomp = em.merge(projectRoleIncompListProjectRoleIncomp);
                if (oldCycleFkOfProjectRoleIncompListProjectRoleIncomp != null) {
                    oldCycleFkOfProjectRoleIncompListProjectRoleIncomp.getProjectRoleIncompList().remove(projectRoleIncompListProjectRoleIncomp);
                    oldCycleFkOfProjectRoleIncompListProjectRoleIncomp = em.merge(oldCycleFkOfProjectRoleIncompListProjectRoleIncomp);
                }
            }
            for (RoleEvaluation roleEvaluationListRoleEvaluation : cycle.getRoleEvaluationList()) {
                Cycle oldCycleFkOfRoleEvaluationListRoleEvaluation = roleEvaluationListRoleEvaluation.getCycleFk();
                roleEvaluationListRoleEvaluation.setCycleFk(cycle);
                roleEvaluationListRoleEvaluation = em.merge(roleEvaluationListRoleEvaluation);
                if (oldCycleFkOfRoleEvaluationListRoleEvaluation != null) {
                    oldCycleFkOfRoleEvaluationListRoleEvaluation.getRoleEvaluationList().remove(roleEvaluationListRoleEvaluation);
                    oldCycleFkOfRoleEvaluationListRoleEvaluation = em.merge(oldCycleFkOfRoleEvaluationListRoleEvaluation);
                }
            }
            for (RoleEvalProject roleEvalProjectListRoleEvalProject : cycle.getRoleEvalProjectList()) {
                Cycle oldCycleFkOfRoleEvalProjectListRoleEvalProject = roleEvalProjectListRoleEvalProject.getCycleFk();
                roleEvalProjectListRoleEvalProject.setCycleFk(cycle);
                roleEvalProjectListRoleEvalProject = em.merge(roleEvalProjectListRoleEvalProject);
                if (oldCycleFkOfRoleEvalProjectListRoleEvalProject != null) {
                    oldCycleFkOfRoleEvalProjectListRoleEvalProject.getRoleEvalProjectList().remove(roleEvalProjectListRoleEvalProject);
                    oldCycleFkOfRoleEvalProjectListRoleEvalProject = em.merge(oldCycleFkOfRoleEvalProjectListRoleEvalProject);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findCycle(cycle.getId()) != null) {
                throw new PreexistingEntityException("Cycle " + cycle + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cycle cycle) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Cycle persistentCycle = em.find(Cycle.class, cycle.getId());
            Project projectFkOld = persistentCycle.getProjectFk();
            Project projectFkNew = cycle.getProjectFk();
            ProjectStructure structureFkOld = persistentCycle.getStructureFk();
            ProjectStructure structureFkNew = cycle.getStructureFk();
            List<AssignedRole> assignedRoleListOld = persistentCycle.getAssignedRoleList();
            List<AssignedRole> assignedRoleListNew = cycle.getAssignedRoleList();
            List<ProjectRoleIncomp> projectRoleIncompListOld = persistentCycle.getProjectRoleIncompList();
            List<ProjectRoleIncomp> projectRoleIncompListNew = cycle.getProjectRoleIncompList();
            List<RoleEvaluation> roleEvaluationListOld = persistentCycle.getRoleEvaluationList();
            List<RoleEvaluation> roleEvaluationListNew = cycle.getRoleEvaluationList();
            List<RoleEvalProject> roleEvalProjectListOld = persistentCycle.getRoleEvalProjectList();
            List<RoleEvalProject> roleEvalProjectListNew = cycle.getRoleEvalProjectList();
            List<String> illegalOrphanMessages = null;
            for (AssignedRole assignedRoleListOldAssignedRole : assignedRoleListOld) {
                if (!assignedRoleListNew.contains(assignedRoleListOldAssignedRole)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AssignedRole " + assignedRoleListOldAssignedRole + " since its cyclesFk field is not nullable.");
                }
            }
            for (ProjectRoleIncomp projectRoleIncompListOldProjectRoleIncomp : projectRoleIncompListOld) {
                if (!projectRoleIncompListNew.contains(projectRoleIncompListOldProjectRoleIncomp)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ProjectRoleIncomp " + projectRoleIncompListOldProjectRoleIncomp + " since its cycleFk field is not nullable.");
                }
            }
            for (RoleEvaluation roleEvaluationListOldRoleEvaluation : roleEvaluationListOld) {
                if (!roleEvaluationListNew.contains(roleEvaluationListOldRoleEvaluation)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RoleEvaluation " + roleEvaluationListOldRoleEvaluation + " since its cycleFk field is not nullable.");
                }
            }
            for (RoleEvalProject roleEvalProjectListOldRoleEvalProject : roleEvalProjectListOld) {
                if (!roleEvalProjectListNew.contains(roleEvalProjectListOldRoleEvalProject)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RoleEvalProject " + roleEvalProjectListOldRoleEvalProject + " since its cycleFk field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (projectFkNew != null) {
                projectFkNew = em.getReference(projectFkNew.getClass(), projectFkNew.getId());
                cycle.setProjectFk(projectFkNew);
            }
            if (structureFkNew != null) {
                structureFkNew = em.getReference(structureFkNew.getClass(), structureFkNew.getId());
                cycle.setStructureFk(structureFkNew);
            }
            List<AssignedRole> attachedAssignedRoleListNew = new ArrayList<AssignedRole>();
            for (AssignedRole assignedRoleListNewAssignedRoleToAttach : assignedRoleListNew) {
                assignedRoleListNewAssignedRoleToAttach = em.getReference(assignedRoleListNewAssignedRoleToAttach.getClass(), assignedRoleListNewAssignedRoleToAttach.getId());
                attachedAssignedRoleListNew.add(assignedRoleListNewAssignedRoleToAttach);
            }
            assignedRoleListNew = attachedAssignedRoleListNew;
            cycle.setAssignedRoleList(assignedRoleListNew);
            List<ProjectRoleIncomp> attachedProjectRoleIncompListNew = new ArrayList<ProjectRoleIncomp>();
            for (ProjectRoleIncomp projectRoleIncompListNewProjectRoleIncompToAttach : projectRoleIncompListNew) {
                projectRoleIncompListNewProjectRoleIncompToAttach = em.getReference(projectRoleIncompListNewProjectRoleIncompToAttach.getClass(), projectRoleIncompListNewProjectRoleIncompToAttach.getId());
                attachedProjectRoleIncompListNew.add(projectRoleIncompListNewProjectRoleIncompToAttach);
            }
            projectRoleIncompListNew = attachedProjectRoleIncompListNew;
            cycle.setProjectRoleIncompList(projectRoleIncompListNew);
            List<RoleEvaluation> attachedRoleEvaluationListNew = new ArrayList<RoleEvaluation>();
            for (RoleEvaluation roleEvaluationListNewRoleEvaluationToAttach : roleEvaluationListNew) {
                roleEvaluationListNewRoleEvaluationToAttach = em.getReference(roleEvaluationListNewRoleEvaluationToAttach.getClass(), roleEvaluationListNewRoleEvaluationToAttach.getId());
                attachedRoleEvaluationListNew.add(roleEvaluationListNewRoleEvaluationToAttach);
            }
            roleEvaluationListNew = attachedRoleEvaluationListNew;
            cycle.setRoleEvaluationList(roleEvaluationListNew);
            List<RoleEvalProject> attachedRoleEvalProjectListNew = new ArrayList<RoleEvalProject>();
            for (RoleEvalProject roleEvalProjectListNewRoleEvalProjectToAttach : roleEvalProjectListNew) {
                roleEvalProjectListNewRoleEvalProjectToAttach = em.getReference(roleEvalProjectListNewRoleEvalProjectToAttach.getClass(), roleEvalProjectListNewRoleEvalProjectToAttach.getId());
                attachedRoleEvalProjectListNew.add(roleEvalProjectListNewRoleEvalProjectToAttach);
            }
            roleEvalProjectListNew = attachedRoleEvalProjectListNew;
            cycle.setRoleEvalProjectList(roleEvalProjectListNew);
            cycle = em.merge(cycle);
            if (projectFkOld != null && !projectFkOld.equals(projectFkNew)) {
                projectFkOld.getCycleList().remove(cycle);
                projectFkOld = em.merge(projectFkOld);
            }
            if (projectFkNew != null && !projectFkNew.equals(projectFkOld)) {
                projectFkNew.getCycleList().add(cycle);
                projectFkNew = em.merge(projectFkNew);
            }
            if (structureFkOld != null && !structureFkOld.equals(structureFkNew)) {
                structureFkOld.getCycleList().remove(cycle);
                structureFkOld = em.merge(structureFkOld);
            }
            if (structureFkNew != null && !structureFkNew.equals(structureFkOld)) {
                structureFkNew.getCycleList().add(cycle);
                structureFkNew = em.merge(structureFkNew);
            }
            for (AssignedRole assignedRoleListNewAssignedRole : assignedRoleListNew) {
                if (!assignedRoleListOld.contains(assignedRoleListNewAssignedRole)) {
                    Cycle oldCyclesFkOfAssignedRoleListNewAssignedRole = assignedRoleListNewAssignedRole.getCyclesFk();
                    assignedRoleListNewAssignedRole.setCyclesFk(cycle);
                    assignedRoleListNewAssignedRole = em.merge(assignedRoleListNewAssignedRole);
                    if (oldCyclesFkOfAssignedRoleListNewAssignedRole != null && !oldCyclesFkOfAssignedRoleListNewAssignedRole.equals(cycle)) {
                        oldCyclesFkOfAssignedRoleListNewAssignedRole.getAssignedRoleList().remove(assignedRoleListNewAssignedRole);
                        oldCyclesFkOfAssignedRoleListNewAssignedRole = em.merge(oldCyclesFkOfAssignedRoleListNewAssignedRole);
                    }
                }
            }
            for (ProjectRoleIncomp projectRoleIncompListNewProjectRoleIncomp : projectRoleIncompListNew) {
                if (!projectRoleIncompListOld.contains(projectRoleIncompListNewProjectRoleIncomp)) {
                    Cycle oldCycleFkOfProjectRoleIncompListNewProjectRoleIncomp = projectRoleIncompListNewProjectRoleIncomp.getCycleFk();
                    projectRoleIncompListNewProjectRoleIncomp.setCycleFk(cycle);
                    projectRoleIncompListNewProjectRoleIncomp = em.merge(projectRoleIncompListNewProjectRoleIncomp);
                    if (oldCycleFkOfProjectRoleIncompListNewProjectRoleIncomp != null && !oldCycleFkOfProjectRoleIncompListNewProjectRoleIncomp.equals(cycle)) {
                        oldCycleFkOfProjectRoleIncompListNewProjectRoleIncomp.getProjectRoleIncompList().remove(projectRoleIncompListNewProjectRoleIncomp);
                        oldCycleFkOfProjectRoleIncompListNewProjectRoleIncomp = em.merge(oldCycleFkOfProjectRoleIncompListNewProjectRoleIncomp);
                    }
                }
            }
            for (RoleEvaluation roleEvaluationListNewRoleEvaluation : roleEvaluationListNew) {
                if (!roleEvaluationListOld.contains(roleEvaluationListNewRoleEvaluation)) {
                    Cycle oldCycleFkOfRoleEvaluationListNewRoleEvaluation = roleEvaluationListNewRoleEvaluation.getCycleFk();
                    roleEvaluationListNewRoleEvaluation.setCycleFk(cycle);
                    roleEvaluationListNewRoleEvaluation = em.merge(roleEvaluationListNewRoleEvaluation);
                    if (oldCycleFkOfRoleEvaluationListNewRoleEvaluation != null && !oldCycleFkOfRoleEvaluationListNewRoleEvaluation.equals(cycle)) {
                        oldCycleFkOfRoleEvaluationListNewRoleEvaluation.getRoleEvaluationList().remove(roleEvaluationListNewRoleEvaluation);
                        oldCycleFkOfRoleEvaluationListNewRoleEvaluation = em.merge(oldCycleFkOfRoleEvaluationListNewRoleEvaluation);
                    }
                }
            }
            for (RoleEvalProject roleEvalProjectListNewRoleEvalProject : roleEvalProjectListNew) {
                if (!roleEvalProjectListOld.contains(roleEvalProjectListNewRoleEvalProject)) {
                    Cycle oldCycleFkOfRoleEvalProjectListNewRoleEvalProject = roleEvalProjectListNewRoleEvalProject.getCycleFk();
                    roleEvalProjectListNewRoleEvalProject.setCycleFk(cycle);
                    roleEvalProjectListNewRoleEvalProject = em.merge(roleEvalProjectListNewRoleEvalProject);
                    if (oldCycleFkOfRoleEvalProjectListNewRoleEvalProject != null && !oldCycleFkOfRoleEvalProjectListNewRoleEvalProject.equals(cycle)) {
                        oldCycleFkOfRoleEvalProjectListNewRoleEvalProject.getRoleEvalProjectList().remove(roleEvalProjectListNewRoleEvalProject);
                        oldCycleFkOfRoleEvalProjectListNewRoleEvalProject = em.merge(oldCycleFkOfRoleEvalProjectListNewRoleEvalProject);
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
                Long id = cycle.getId();
                if (findCycle(id) == null) {
                    throw new NonexistentEntityException("The cycle with id " + id + " no longer exists.");
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
            Cycle cycle;
            try {
                cycle = em.getReference(Cycle.class, id);
                cycle.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cycle with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<AssignedRole> assignedRoleListOrphanCheck = cycle.getAssignedRoleList();
            for (AssignedRole assignedRoleListOrphanCheckAssignedRole : assignedRoleListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cycle (" + cycle + ") cannot be destroyed since the AssignedRole " + assignedRoleListOrphanCheckAssignedRole + " in its assignedRoleList field has a non-nullable cyclesFk field.");
            }
            List<ProjectRoleIncomp> projectRoleIncompListOrphanCheck = cycle.getProjectRoleIncompList();
            for (ProjectRoleIncomp projectRoleIncompListOrphanCheckProjectRoleIncomp : projectRoleIncompListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cycle (" + cycle + ") cannot be destroyed since the ProjectRoleIncomp " + projectRoleIncompListOrphanCheckProjectRoleIncomp + " in its projectRoleIncompList field has a non-nullable cycleFk field.");
            }
            List<RoleEvaluation> roleEvaluationListOrphanCheck = cycle.getRoleEvaluationList();
            for (RoleEvaluation roleEvaluationListOrphanCheckRoleEvaluation : roleEvaluationListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cycle (" + cycle + ") cannot be destroyed since the RoleEvaluation " + roleEvaluationListOrphanCheckRoleEvaluation + " in its roleEvaluationList field has a non-nullable cycleFk field.");
            }
            List<RoleEvalProject> roleEvalProjectListOrphanCheck = cycle.getRoleEvalProjectList();
            for (RoleEvalProject roleEvalProjectListOrphanCheckRoleEvalProject : roleEvalProjectListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cycle (" + cycle + ") cannot be destroyed since the RoleEvalProject " + roleEvalProjectListOrphanCheckRoleEvalProject + " in its roleEvalProjectList field has a non-nullable cycleFk field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Project projectFk = cycle.getProjectFk();
            if (projectFk != null) {
                projectFk.getCycleList().remove(cycle);
                projectFk = em.merge(projectFk);
            }
            ProjectStructure structureFk = cycle.getStructureFk();
            if (structureFk != null) {
                structureFk.getCycleList().remove(cycle);
                structureFk = em.merge(structureFk);
            }
            em.remove(cycle);
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

    public List<Cycle> findCycleEntities() {
        return findCycleEntities(true, -1, -1);
    }

    public List<Cycle> findCycleEntities(int maxResults, int firstResult) {
        return findCycleEntities(false, maxResults, firstResult);
    }

    private List<Cycle> findCycleEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cycle.class));
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

    public Cycle findCycle(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cycle.class, id);
        } finally {
            em.close();
        }
    }

    public int getCycleCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cycle> rt = cq.from(Cycle.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
