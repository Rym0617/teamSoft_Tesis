/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.JPAControllerClasses;

import org.teamsoft.entity.*;
import org.teamsoft.exceptions.IllegalOrphanException;
import org.teamsoft.exceptions.NonexistentEntityException;
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
public class LevelsJpaController implements Serializable {

    public LevelsJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Levels levels) throws RollbackFailureException, Exception {
        if (levels.getRoleCompetitionList() == null) {
            levels.setRoleCompetitionList(new ArrayList<RoleCompetition>());
        }
        if (levels.getProjectTechCompetenceList() == null) {
            levels.setProjectTechCompetenceList(new ArrayList<ProjectTechCompetence>());
        }
        if (levels.getCompetenceValueList() == null) {
            levels.setCompetenceValueList(new ArrayList<CompetenceValue>());
        }
        if (levels.getCompetenceDimensionList() == null) {
            levels.setCompetenceDimensionList(new ArrayList<CompetenceDimension>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<RoleCompetition> attachedRoleCompetitionList = new ArrayList<RoleCompetition>();
            for (RoleCompetition roleCompetitionListRoleCompetitionToAttach : levels.getRoleCompetitionList()) {
                roleCompetitionListRoleCompetitionToAttach = em.getReference(roleCompetitionListRoleCompetitionToAttach.getClass(), roleCompetitionListRoleCompetitionToAttach.getId());
                attachedRoleCompetitionList.add(roleCompetitionListRoleCompetitionToAttach);
            }
            levels.setRoleCompetitionList(attachedRoleCompetitionList);
            List<ProjectTechCompetence> attachedProjectTechCompetenceList = new ArrayList<ProjectTechCompetence>();
            for (ProjectTechCompetence projectTechCompetenceListProjectTechCompetenceToAttach : levels.getProjectTechCompetenceList()) {
                projectTechCompetenceListProjectTechCompetenceToAttach = em.getReference(projectTechCompetenceListProjectTechCompetenceToAttach.getClass(), projectTechCompetenceListProjectTechCompetenceToAttach.getId());
                attachedProjectTechCompetenceList.add(projectTechCompetenceListProjectTechCompetenceToAttach);
            }
            levels.setProjectTechCompetenceList(attachedProjectTechCompetenceList);
            List<CompetenceValue> attachedCompetenceValueList = new ArrayList<CompetenceValue>();
            for (CompetenceValue competenceValueListCompetenceValueToAttach : levels.getCompetenceValueList()) {
                competenceValueListCompetenceValueToAttach = em.getReference(competenceValueListCompetenceValueToAttach.getClass(), competenceValueListCompetenceValueToAttach.getId());
                attachedCompetenceValueList.add(competenceValueListCompetenceValueToAttach);
            }
            levels.setCompetenceValueList(attachedCompetenceValueList);
            List<CompetenceDimension> attachedCompetenceDimensionList = new ArrayList<CompetenceDimension>();
            for (CompetenceDimension competenceDimensionListCompetenceDimensionToAttach : levels.getCompetenceDimensionList()) {
                competenceDimensionListCompetenceDimensionToAttach = em.getReference(competenceDimensionListCompetenceDimensionToAttach.getClass(), competenceDimensionListCompetenceDimensionToAttach.getId());
                attachedCompetenceDimensionList.add(competenceDimensionListCompetenceDimensionToAttach);
            }
            levels.setCompetenceDimensionList(attachedCompetenceDimensionList);
            em.persist(levels);
            for (RoleCompetition roleCompetitionListRoleCompetition : levels.getRoleCompetitionList()) {
                Levels oldLevelsFkOfRoleCompetitionListRoleCompetition = roleCompetitionListRoleCompetition.getLevelsFk();
                roleCompetitionListRoleCompetition.setLevelsFk(levels);
                roleCompetitionListRoleCompetition = em.merge(roleCompetitionListRoleCompetition);
                if (oldLevelsFkOfRoleCompetitionListRoleCompetition != null) {
                    oldLevelsFkOfRoleCompetitionListRoleCompetition.getRoleCompetitionList().remove(roleCompetitionListRoleCompetition);
                    oldLevelsFkOfRoleCompetitionListRoleCompetition = em.merge(oldLevelsFkOfRoleCompetitionListRoleCompetition);
                }
            }
            for (ProjectTechCompetence projectTechCompetenceListProjectTechCompetence : levels.getProjectTechCompetenceList()) {
                Levels oldLevelFkOfProjectTechCompetenceListProjectTechCompetence = projectTechCompetenceListProjectTechCompetence.getLevelFk();
                projectTechCompetenceListProjectTechCompetence.setLevelFk(levels);
                projectTechCompetenceListProjectTechCompetence = em.merge(projectTechCompetenceListProjectTechCompetence);
                if (oldLevelFkOfProjectTechCompetenceListProjectTechCompetence != null) {
                    oldLevelFkOfProjectTechCompetenceListProjectTechCompetence.getProjectTechCompetenceList().remove(projectTechCompetenceListProjectTechCompetence);
                    oldLevelFkOfProjectTechCompetenceListProjectTechCompetence = em.merge(oldLevelFkOfProjectTechCompetenceListProjectTechCompetence);
                }
            }
            for (CompetenceValue competenceValueListCompetenceValue : levels.getCompetenceValueList()) {
                Levels oldLevelFkOfCompetenceValueListCompetenceValue = competenceValueListCompetenceValue.getLevelFk();
                competenceValueListCompetenceValue.setLevelFk(levels);
                competenceValueListCompetenceValue = em.merge(competenceValueListCompetenceValue);
                if (oldLevelFkOfCompetenceValueListCompetenceValue != null) {
                    oldLevelFkOfCompetenceValueListCompetenceValue.getCompetenceValueList().remove(competenceValueListCompetenceValue);
                    oldLevelFkOfCompetenceValueListCompetenceValue = em.merge(oldLevelFkOfCompetenceValueListCompetenceValue);
                }
            }
            for (CompetenceDimension competenceDimensionListCompetenceDimension : levels.getCompetenceDimensionList()) {
                Levels oldLevelFkOfCompetenceDimensionListCompetenceDimension = competenceDimensionListCompetenceDimension.getLevelFk();
                competenceDimensionListCompetenceDimension.setLevelFk(levels);
                competenceDimensionListCompetenceDimension = em.merge(competenceDimensionListCompetenceDimension);
                if (oldLevelFkOfCompetenceDimensionListCompetenceDimension != null) {
                    oldLevelFkOfCompetenceDimensionListCompetenceDimension.getCompetenceDimensionList().remove(competenceDimensionListCompetenceDimension);
                    oldLevelFkOfCompetenceDimensionListCompetenceDimension = em.merge(oldLevelFkOfCompetenceDimensionListCompetenceDimension);
                }
            }
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

    public void edit(Levels levels) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Levels persistentLevels = em.find(Levels.class, levels.getId());
            List<RoleCompetition> roleCompetitionListOld = persistentLevels.getRoleCompetitionList();
            List<RoleCompetition> roleCompetitionListNew = levels.getRoleCompetitionList();
            List<ProjectTechCompetence> projectTechCompetenceListOld = persistentLevels.getProjectTechCompetenceList();
            List<ProjectTechCompetence> projectTechCompetenceListNew = levels.getProjectTechCompetenceList();
            List<CompetenceValue> competenceValueListOld = persistentLevels.getCompetenceValueList();
            List<CompetenceValue> competenceValueListNew = levels.getCompetenceValueList();
            List<CompetenceDimension> competenceDimensionListOld = persistentLevels.getCompetenceDimensionList();
            List<CompetenceDimension> competenceDimensionListNew = levels.getCompetenceDimensionList();
            List<String> illegalOrphanMessages = null;
            for (RoleCompetition roleCompetitionListOldRoleCompetition : roleCompetitionListOld) {
                if (!roleCompetitionListNew.contains(roleCompetitionListOldRoleCompetition)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RoleCompetition " + roleCompetitionListOldRoleCompetition + " since its levelsFk field is not nullable.");
                }
            }
            for (ProjectTechCompetence projectTechCompetenceListOldProjectTechCompetence : projectTechCompetenceListOld) {
                if (!projectTechCompetenceListNew.contains(projectTechCompetenceListOldProjectTechCompetence)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ProjectTechCompetence " + projectTechCompetenceListOldProjectTechCompetence + " since its levelFk field is not nullable.");
                }
            }
            for (CompetenceValue competenceValueListOldCompetenceValue : competenceValueListOld) {
                if (!competenceValueListNew.contains(competenceValueListOldCompetenceValue)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain CompetenceValue " + competenceValueListOldCompetenceValue + " since its levelFk field is not nullable.");
                }
            }
            for (CompetenceDimension competenceDimensionListOldCompetenceDimension : competenceDimensionListOld) {
                if (!competenceDimensionListNew.contains(competenceDimensionListOldCompetenceDimension)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain CompetenceDimension " + competenceDimensionListOldCompetenceDimension + " since its levelFk field is not nullable.");
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
            levels.setRoleCompetitionList(roleCompetitionListNew);
            List<ProjectTechCompetence> attachedProjectTechCompetenceListNew = new ArrayList<ProjectTechCompetence>();
            for (ProjectTechCompetence projectTechCompetenceListNewProjectTechCompetenceToAttach : projectTechCompetenceListNew) {
                projectTechCompetenceListNewProjectTechCompetenceToAttach = em.getReference(projectTechCompetenceListNewProjectTechCompetenceToAttach.getClass(), projectTechCompetenceListNewProjectTechCompetenceToAttach.getId());
                attachedProjectTechCompetenceListNew.add(projectTechCompetenceListNewProjectTechCompetenceToAttach);
            }
            projectTechCompetenceListNew = attachedProjectTechCompetenceListNew;
            levels.setProjectTechCompetenceList(projectTechCompetenceListNew);
            List<CompetenceValue> attachedCompetenceValueListNew = new ArrayList<CompetenceValue>();
            for (CompetenceValue competenceValueListNewCompetenceValueToAttach : competenceValueListNew) {
                competenceValueListNewCompetenceValueToAttach = em.getReference(competenceValueListNewCompetenceValueToAttach.getClass(), competenceValueListNewCompetenceValueToAttach.getId());
                attachedCompetenceValueListNew.add(competenceValueListNewCompetenceValueToAttach);
            }
            competenceValueListNew = attachedCompetenceValueListNew;
            levels.setCompetenceValueList(competenceValueListNew);
            List<CompetenceDimension> attachedCompetenceDimensionListNew = new ArrayList<CompetenceDimension>();
            for (CompetenceDimension competenceDimensionListNewCompetenceDimensionToAttach : competenceDimensionListNew) {
                competenceDimensionListNewCompetenceDimensionToAttach = em.getReference(competenceDimensionListNewCompetenceDimensionToAttach.getClass(), competenceDimensionListNewCompetenceDimensionToAttach.getId());
                attachedCompetenceDimensionListNew.add(competenceDimensionListNewCompetenceDimensionToAttach);
            }
            competenceDimensionListNew = attachedCompetenceDimensionListNew;
            levels.setCompetenceDimensionList(competenceDimensionListNew);
            levels = em.merge(levels);
            for (RoleCompetition roleCompetitionListNewRoleCompetition : roleCompetitionListNew) {
                if (!roleCompetitionListOld.contains(roleCompetitionListNewRoleCompetition)) {
                    Levels oldLevelsFkOfRoleCompetitionListNewRoleCompetition = roleCompetitionListNewRoleCompetition.getLevelsFk();
                    roleCompetitionListNewRoleCompetition.setLevelsFk(levels);
                    roleCompetitionListNewRoleCompetition = em.merge(roleCompetitionListNewRoleCompetition);
                    if (oldLevelsFkOfRoleCompetitionListNewRoleCompetition != null && !oldLevelsFkOfRoleCompetitionListNewRoleCompetition.equals(levels)) {
                        oldLevelsFkOfRoleCompetitionListNewRoleCompetition.getRoleCompetitionList().remove(roleCompetitionListNewRoleCompetition);
                        oldLevelsFkOfRoleCompetitionListNewRoleCompetition = em.merge(oldLevelsFkOfRoleCompetitionListNewRoleCompetition);
                    }
                }
            }
            for (ProjectTechCompetence projectTechCompetenceListNewProjectTechCompetence : projectTechCompetenceListNew) {
                if (!projectTechCompetenceListOld.contains(projectTechCompetenceListNewProjectTechCompetence)) {
                    Levels oldLevelFkOfProjectTechCompetenceListNewProjectTechCompetence = projectTechCompetenceListNewProjectTechCompetence.getLevelFk();
                    projectTechCompetenceListNewProjectTechCompetence.setLevelFk(levels);
                    projectTechCompetenceListNewProjectTechCompetence = em.merge(projectTechCompetenceListNewProjectTechCompetence);
                    if (oldLevelFkOfProjectTechCompetenceListNewProjectTechCompetence != null && !oldLevelFkOfProjectTechCompetenceListNewProjectTechCompetence.equals(levels)) {
                        oldLevelFkOfProjectTechCompetenceListNewProjectTechCompetence.getProjectTechCompetenceList().remove(projectTechCompetenceListNewProjectTechCompetence);
                        oldLevelFkOfProjectTechCompetenceListNewProjectTechCompetence = em.merge(oldLevelFkOfProjectTechCompetenceListNewProjectTechCompetence);
                    }
                }
            }
            for (CompetenceValue competenceValueListNewCompetenceValue : competenceValueListNew) {
                if (!competenceValueListOld.contains(competenceValueListNewCompetenceValue)) {
                    Levels oldLevelFkOfCompetenceValueListNewCompetenceValue = competenceValueListNewCompetenceValue.getLevelFk();
                    competenceValueListNewCompetenceValue.setLevelFk(levels);
                    competenceValueListNewCompetenceValue = em.merge(competenceValueListNewCompetenceValue);
                    if (oldLevelFkOfCompetenceValueListNewCompetenceValue != null && !oldLevelFkOfCompetenceValueListNewCompetenceValue.equals(levels)) {
                        oldLevelFkOfCompetenceValueListNewCompetenceValue.getCompetenceValueList().remove(competenceValueListNewCompetenceValue);
                        oldLevelFkOfCompetenceValueListNewCompetenceValue = em.merge(oldLevelFkOfCompetenceValueListNewCompetenceValue);
                    }
                }
            }
            for (CompetenceDimension competenceDimensionListNewCompetenceDimension : competenceDimensionListNew) {
                if (!competenceDimensionListOld.contains(competenceDimensionListNewCompetenceDimension)) {
                    Levels oldLevelFkOfCompetenceDimensionListNewCompetenceDimension = competenceDimensionListNewCompetenceDimension.getLevelFk();
                    competenceDimensionListNewCompetenceDimension.setLevelFk(levels);
                    competenceDimensionListNewCompetenceDimension = em.merge(competenceDimensionListNewCompetenceDimension);
                    if (oldLevelFkOfCompetenceDimensionListNewCompetenceDimension != null && !oldLevelFkOfCompetenceDimensionListNewCompetenceDimension.equals(levels)) {
                        oldLevelFkOfCompetenceDimensionListNewCompetenceDimension.getCompetenceDimensionList().remove(competenceDimensionListNewCompetenceDimension);
                        oldLevelFkOfCompetenceDimensionListNewCompetenceDimension = em.merge(oldLevelFkOfCompetenceDimensionListNewCompetenceDimension);
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
                Long id = levels.getId();
                if (findLevels(id) == null) {
                    throw new NonexistentEntityException("The levels with id " + id + " no longer exists.");
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
            Levels levels;
            try {
                levels = em.getReference(Levels.class, id);
                levels.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The levels with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<RoleCompetition> roleCompetitionListOrphanCheck = levels.getRoleCompetitionList();
            for (RoleCompetition roleCompetitionListOrphanCheckRoleCompetition : roleCompetitionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Levels (" + levels + ") cannot be destroyed since the RoleCompetition " + roleCompetitionListOrphanCheckRoleCompetition + " in its roleCompetitionList field has a non-nullable levelsFk field.");
            }
            List<ProjectTechCompetence> projectTechCompetenceListOrphanCheck = levels.getProjectTechCompetenceList();
            for (ProjectTechCompetence projectTechCompetenceListOrphanCheckProjectTechCompetence : projectTechCompetenceListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Levels (" + levels + ") cannot be destroyed since the ProjectTechCompetence " + projectTechCompetenceListOrphanCheckProjectTechCompetence + " in its projectTechCompetenceList field has a non-nullable levelFk field.");
            }
            List<CompetenceValue> competenceValueListOrphanCheck = levels.getCompetenceValueList();
            for (CompetenceValue competenceValueListOrphanCheckCompetenceValue : competenceValueListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Levels (" + levels + ") cannot be destroyed since the CompetenceValue " + competenceValueListOrphanCheckCompetenceValue + " in its competenceValueList field has a non-nullable levelFk field.");
            }
            List<CompetenceDimension> competenceDimensionListOrphanCheck = levels.getCompetenceDimensionList();
            for (CompetenceDimension competenceDimensionListOrphanCheckCompetenceDimension : competenceDimensionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Levels (" + levels + ") cannot be destroyed since the CompetenceDimension " + competenceDimensionListOrphanCheckCompetenceDimension + " in its competenceDimensionList field has a non-nullable levelFk field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(levels);
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

    public List<Levels> findLevelsEntities() {
        return findLevelsEntities(true, -1, -1);
    }

    public List<Levels> findLevelsEntities(int maxResults, int firstResult) {
        return findLevelsEntities(false, maxResults, firstResult);
    }

    private List<Levels> findLevelsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Levels.class));
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

    public Levels findLevels(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Levels.class, id);
        } finally {
            em.close();
        }
    }

    public int getLevelsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Levels> rt = cq.from(Levels.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
