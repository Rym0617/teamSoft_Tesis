/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.JPAControllerClasses;

import org.teamsoft.entity.CompetenceImportance;
import org.teamsoft.entity.ProjectTechCompetence;
import org.teamsoft.entity.RoleCompetition;
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
public class CompetenceImportanceJpaController implements Serializable {

    public CompetenceImportanceJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CompetenceImportance competenceImportance) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (competenceImportance.getRoleCompetitionList() == null) {
            competenceImportance.setRoleCompetitionList(new ArrayList<RoleCompetition>());
        }
        if (competenceImportance.getProjectTechCompetenceList() == null) {
            competenceImportance.setProjectTechCompetenceList(new ArrayList<ProjectTechCompetence>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<RoleCompetition> attachedRoleCompetitionList = new ArrayList<RoleCompetition>();
            for (RoleCompetition roleCompetitionListRoleCompetitionToAttach : competenceImportance.getRoleCompetitionList()) {
                roleCompetitionListRoleCompetitionToAttach = em.getReference(roleCompetitionListRoleCompetitionToAttach.getClass(), roleCompetitionListRoleCompetitionToAttach.getId());
                attachedRoleCompetitionList.add(roleCompetitionListRoleCompetitionToAttach);
            }
            competenceImportance.setRoleCompetitionList(attachedRoleCompetitionList);
            List<ProjectTechCompetence> attachedProjectTechCompetenceList = new ArrayList<ProjectTechCompetence>();
            for (ProjectTechCompetence projectTechCompetenceListProjectTechCompetenceToAttach : competenceImportance.getProjectTechCompetenceList()) {
                projectTechCompetenceListProjectTechCompetenceToAttach = em.getReference(projectTechCompetenceListProjectTechCompetenceToAttach.getClass(), projectTechCompetenceListProjectTechCompetenceToAttach.getId());
                attachedProjectTechCompetenceList.add(projectTechCompetenceListProjectTechCompetenceToAttach);
            }
            competenceImportance.setProjectTechCompetenceList(attachedProjectTechCompetenceList);
            em.persist(competenceImportance);
            for (RoleCompetition roleCompetitionListRoleCompetition : competenceImportance.getRoleCompetitionList()) {
                CompetenceImportance oldCompImportanceFkOfRoleCompetitionListRoleCompetition = roleCompetitionListRoleCompetition.getCompImportanceFk();
                roleCompetitionListRoleCompetition.setCompImportanceFk(competenceImportance);
                roleCompetitionListRoleCompetition = em.merge(roleCompetitionListRoleCompetition);
                if (oldCompImportanceFkOfRoleCompetitionListRoleCompetition != null) {
                    oldCompImportanceFkOfRoleCompetitionListRoleCompetition.getRoleCompetitionList().remove(roleCompetitionListRoleCompetition);
                    oldCompImportanceFkOfRoleCompetitionListRoleCompetition = em.merge(oldCompImportanceFkOfRoleCompetitionListRoleCompetition);
                }
            }
            for (ProjectTechCompetence projectTechCompetenceListProjectTechCompetence : competenceImportance.getProjectTechCompetenceList()) {
                CompetenceImportance oldCompetenceImportanceFkOfProjectTechCompetenceListProjectTechCompetence = projectTechCompetenceListProjectTechCompetence.getCompetenceImportanceFk();
                projectTechCompetenceListProjectTechCompetence.setCompetenceImportanceFk(competenceImportance);
                projectTechCompetenceListProjectTechCompetence = em.merge(projectTechCompetenceListProjectTechCompetence);
                if (oldCompetenceImportanceFkOfProjectTechCompetenceListProjectTechCompetence != null) {
                    oldCompetenceImportanceFkOfProjectTechCompetenceListProjectTechCompetence.getProjectTechCompetenceList().remove(projectTechCompetenceListProjectTechCompetence);
                    oldCompetenceImportanceFkOfProjectTechCompetenceListProjectTechCompetence = em.merge(oldCompetenceImportanceFkOfProjectTechCompetenceListProjectTechCompetence);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findCompetenceImportance(competenceImportance.getId()) != null) {
                throw new PreexistingEntityException("CompetenceImportance " + competenceImportance + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CompetenceImportance competenceImportance) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            CompetenceImportance persistentCompetenceImportance = em.find(CompetenceImportance.class, competenceImportance.getId());
            List<RoleCompetition> roleCompetitionListOld = persistentCompetenceImportance.getRoleCompetitionList();
            List<RoleCompetition> roleCompetitionListNew = competenceImportance.getRoleCompetitionList();
            List<ProjectTechCompetence> projectTechCompetenceListOld = persistentCompetenceImportance.getProjectTechCompetenceList();
            List<ProjectTechCompetence> projectTechCompetenceListNew = competenceImportance.getProjectTechCompetenceList();
            List<String> illegalOrphanMessages = null;
            for (RoleCompetition roleCompetitionListOldRoleCompetition : roleCompetitionListOld) {
                if (!roleCompetitionListNew.contains(roleCompetitionListOldRoleCompetition)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RoleCompetition " + roleCompetitionListOldRoleCompetition + " since its compImportanceFk field is not nullable.");
                }
            }
            for (ProjectTechCompetence projectTechCompetenceListOldProjectTechCompetence : projectTechCompetenceListOld) {
                if (!projectTechCompetenceListNew.contains(projectTechCompetenceListOldProjectTechCompetence)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ProjectTechCompetence " + projectTechCompetenceListOldProjectTechCompetence + " since its competenceImportanceFk field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<RoleCompetition> attachedRoleCompetitionListNew = new ArrayList<RoleCompetition>();
            for (RoleCompetition roleCompetitionListNewRoleCompetitionToAttach : roleCompetitionListNew) {
                roleCompetitionListNewRoleCompetitionToAttach = em.getReference(roleCompetitionListNewRoleCompetitionToAttach.getClass(), roleCompetitionListNewRoleCompetitionToAttach.getId());
                attachedRoleCompetitionListNew.add(roleCompetitionListNewRoleCompetitionToAttach);
            }
            roleCompetitionListNew = attachedRoleCompetitionListNew;
            competenceImportance.setRoleCompetitionList(roleCompetitionListNew);
            List<ProjectTechCompetence> attachedProjectTechCompetenceListNew = new ArrayList<ProjectTechCompetence>();
            for (ProjectTechCompetence projectTechCompetenceListNewProjectTechCompetenceToAttach : projectTechCompetenceListNew) {
                projectTechCompetenceListNewProjectTechCompetenceToAttach = em.getReference(projectTechCompetenceListNewProjectTechCompetenceToAttach.getClass(), projectTechCompetenceListNewProjectTechCompetenceToAttach.getId());
                attachedProjectTechCompetenceListNew.add(projectTechCompetenceListNewProjectTechCompetenceToAttach);
            }
            projectTechCompetenceListNew = attachedProjectTechCompetenceListNew;
            competenceImportance.setProjectTechCompetenceList(projectTechCompetenceListNew);
            competenceImportance = em.merge(competenceImportance);
            for (RoleCompetition roleCompetitionListNewRoleCompetition : roleCompetitionListNew) {
                if (!roleCompetitionListOld.contains(roleCompetitionListNewRoleCompetition)) {
                    CompetenceImportance oldCompImportanceFkOfRoleCompetitionListNewRoleCompetition = roleCompetitionListNewRoleCompetition.getCompImportanceFk();
                    roleCompetitionListNewRoleCompetition.setCompImportanceFk(competenceImportance);
                    roleCompetitionListNewRoleCompetition = em.merge(roleCompetitionListNewRoleCompetition);
                    if (oldCompImportanceFkOfRoleCompetitionListNewRoleCompetition != null && !oldCompImportanceFkOfRoleCompetitionListNewRoleCompetition.equals(competenceImportance)) {
                        oldCompImportanceFkOfRoleCompetitionListNewRoleCompetition.getRoleCompetitionList().remove(roleCompetitionListNewRoleCompetition);
                        oldCompImportanceFkOfRoleCompetitionListNewRoleCompetition = em.merge(oldCompImportanceFkOfRoleCompetitionListNewRoleCompetition);
                    }
                }
            }
            for (ProjectTechCompetence projectTechCompetenceListNewProjectTechCompetence : projectTechCompetenceListNew) {
                if (!projectTechCompetenceListOld.contains(projectTechCompetenceListNewProjectTechCompetence)) {
                    CompetenceImportance oldCompetenceImportanceFkOfProjectTechCompetenceListNewProjectTechCompetence = projectTechCompetenceListNewProjectTechCompetence.getCompetenceImportanceFk();
                    projectTechCompetenceListNewProjectTechCompetence.setCompetenceImportanceFk(competenceImportance);
                    projectTechCompetenceListNewProjectTechCompetence = em.merge(projectTechCompetenceListNewProjectTechCompetence);
                    if (oldCompetenceImportanceFkOfProjectTechCompetenceListNewProjectTechCompetence != null && !oldCompetenceImportanceFkOfProjectTechCompetenceListNewProjectTechCompetence.equals(competenceImportance)) {
                        oldCompetenceImportanceFkOfProjectTechCompetenceListNewProjectTechCompetence.getProjectTechCompetenceList().remove(projectTechCompetenceListNewProjectTechCompetence);
                        oldCompetenceImportanceFkOfProjectTechCompetenceListNewProjectTechCompetence = em.merge(oldCompetenceImportanceFkOfProjectTechCompetenceListNewProjectTechCompetence);
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
                Long id = competenceImportance.getId();
                if (findCompetenceImportance(id) == null) {
                    throw new NonexistentEntityException("The competenceImportance with id " + id + " no longer exists.");
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
            CompetenceImportance competenceImportance;
            try {
                competenceImportance = em.getReference(CompetenceImportance.class, id);
                competenceImportance.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The competenceImportance with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<RoleCompetition> roleCompetitionListOrphanCheck = competenceImportance.getRoleCompetitionList();
            for (RoleCompetition roleCompetitionListOrphanCheckRoleCompetition : roleCompetitionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This CompetenceImportance (" + competenceImportance + ") cannot be destroyed since the RoleCompetition " + roleCompetitionListOrphanCheckRoleCompetition + " in its roleCompetitionList field has a non-nullable compImportanceFk field.");
            }
            List<ProjectTechCompetence> projectTechCompetenceListOrphanCheck = competenceImportance.getProjectTechCompetenceList();
            for (ProjectTechCompetence projectTechCompetenceListOrphanCheckProjectTechCompetence : projectTechCompetenceListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This CompetenceImportance (" + competenceImportance + ") cannot be destroyed since the ProjectTechCompetence " + projectTechCompetenceListOrphanCheckProjectTechCompetence + " in its projectTechCompetenceList field has a non-nullable competenceImportanceFk field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(competenceImportance);
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

    public List<CompetenceImportance> findCompetenceImportanceEntities() {
        return findCompetenceImportanceEntities(true, -1, -1);
    }

    public List<CompetenceImportance> findCompetenceImportanceEntities(int maxResults, int firstResult) {
        return findCompetenceImportanceEntities(false, maxResults, firstResult);
    }

    private List<CompetenceImportance> findCompetenceImportanceEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CompetenceImportance.class));
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

    public CompetenceImportance findCompetenceImportance(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CompetenceImportance.class, id);
        } finally {
            em.close();
        }
    }

    public int getCompetenceImportanceCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CompetenceImportance> rt = cq.from(CompetenceImportance.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
