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
public class CompetenceJpaController implements Serializable {

    public CompetenceJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Competence competence) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (competence.getRoleCompetitionList() == null) {
            competence.setRoleCompetitionList(new ArrayList<RoleCompetition>());
        }
        if (competence.getProjectTechCompetenceList() == null) {
            competence.setProjectTechCompetenceList(new ArrayList<ProjectTechCompetence>());
        }
        if (competence.getCompetenceValueList() == null) {
            competence.setCompetenceValueList(new ArrayList<CompetenceValue>());
        }
        if (competence.getCompetenceDimensionList() == null) {
            competence.setCompetenceDimensionList(new ArrayList<CompetenceDimension>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<RoleCompetition> attachedRoleCompetitionList = new ArrayList<RoleCompetition>();
            for (RoleCompetition roleCompetitionListRoleCompetitionToAttach : competence.getRoleCompetitionList()) {
                roleCompetitionListRoleCompetitionToAttach = em.getReference(roleCompetitionListRoleCompetitionToAttach.getClass(), roleCompetitionListRoleCompetitionToAttach.getId());
                attachedRoleCompetitionList.add(roleCompetitionListRoleCompetitionToAttach);
            }
            competence.setRoleCompetitionList(attachedRoleCompetitionList);
            List<ProjectTechCompetence> attachedProjectTechCompetenceList = new ArrayList<ProjectTechCompetence>();
            for (ProjectTechCompetence projectTechCompetenceListProjectTechCompetenceToAttach : competence.getProjectTechCompetenceList()) {
                projectTechCompetenceListProjectTechCompetenceToAttach = em.getReference(projectTechCompetenceListProjectTechCompetenceToAttach.getClass(), projectTechCompetenceListProjectTechCompetenceToAttach.getId());
                attachedProjectTechCompetenceList.add(projectTechCompetenceListProjectTechCompetenceToAttach);
            }
            competence.setProjectTechCompetenceList(attachedProjectTechCompetenceList);
            List<CompetenceValue> attachedCompetenceValueList = new ArrayList<CompetenceValue>();
            for (CompetenceValue competenceValueListCompetenceValueToAttach : competence.getCompetenceValueList()) {
                competenceValueListCompetenceValueToAttach = em.getReference(competenceValueListCompetenceValueToAttach.getClass(), competenceValueListCompetenceValueToAttach.getId());
                attachedCompetenceValueList.add(competenceValueListCompetenceValueToAttach);
            }
            competence.setCompetenceValueList(attachedCompetenceValueList);
            List<CompetenceDimension> attachedCompetenceDimensionList = new ArrayList<CompetenceDimension>();
            for (CompetenceDimension competenceDimensionListCompetenceDimensionToAttach : competence.getCompetenceDimensionList()) {
                competenceDimensionListCompetenceDimensionToAttach = em.getReference(competenceDimensionListCompetenceDimensionToAttach.getClass(), competenceDimensionListCompetenceDimensionToAttach.getId());
                attachedCompetenceDimensionList.add(competenceDimensionListCompetenceDimensionToAttach);
            }
            competence.setCompetenceDimensionList(attachedCompetenceDimensionList);
            em.persist(competence);
            for (RoleCompetition roleCompetitionListRoleCompetition : competence.getRoleCompetitionList()) {
                Competence oldCompetenceFkOfRoleCompetitionListRoleCompetition = roleCompetitionListRoleCompetition.getCompetenceFk();
                roleCompetitionListRoleCompetition.setCompetenceFk(competence);
                roleCompetitionListRoleCompetition = em.merge(roleCompetitionListRoleCompetition);
                if (oldCompetenceFkOfRoleCompetitionListRoleCompetition != null) {
                    oldCompetenceFkOfRoleCompetitionListRoleCompetition.getRoleCompetitionList().remove(roleCompetitionListRoleCompetition);
                    oldCompetenceFkOfRoleCompetitionListRoleCompetition = em.merge(oldCompetenceFkOfRoleCompetitionListRoleCompetition);
                }
            }
            for (ProjectTechCompetence projectTechCompetenceListProjectTechCompetence : competence.getProjectTechCompetenceList()) {
                Competence oldCompetenceFkOfProjectTechCompetenceListProjectTechCompetence = projectTechCompetenceListProjectTechCompetence.getCompetenceFk();
                projectTechCompetenceListProjectTechCompetence.setCompetenceFk(competence);
                projectTechCompetenceListProjectTechCompetence = em.merge(projectTechCompetenceListProjectTechCompetence);
                if (oldCompetenceFkOfProjectTechCompetenceListProjectTechCompetence != null) {
                    oldCompetenceFkOfProjectTechCompetenceListProjectTechCompetence.getProjectTechCompetenceList().remove(projectTechCompetenceListProjectTechCompetence);
                    oldCompetenceFkOfProjectTechCompetenceListProjectTechCompetence = em.merge(oldCompetenceFkOfProjectTechCompetenceListProjectTechCompetence);
                }
            }
            for (CompetenceValue competenceValueListCompetenceValue : competence.getCompetenceValueList()) {
                Competence oldCompetenceFkOfCompetenceValueListCompetenceValue = competenceValueListCompetenceValue.getCompetenceFk();
                competenceValueListCompetenceValue.setCompetenceFk(competence);
                competenceValueListCompetenceValue = em.merge(competenceValueListCompetenceValue);
                if (oldCompetenceFkOfCompetenceValueListCompetenceValue != null) {
                    oldCompetenceFkOfCompetenceValueListCompetenceValue.getCompetenceValueList().remove(competenceValueListCompetenceValue);
                    oldCompetenceFkOfCompetenceValueListCompetenceValue = em.merge(oldCompetenceFkOfCompetenceValueListCompetenceValue);
                }
            }
            for (CompetenceDimension competenceDimensionListCompetenceDimension : competence.getCompetenceDimensionList()) {
                Competence oldCompetenceFkOfCompetenceDimensionListCompetenceDimension = competenceDimensionListCompetenceDimension.getCompetenceFk();
                competenceDimensionListCompetenceDimension.setCompetenceFk(competence);
                competenceDimensionListCompetenceDimension = em.merge(competenceDimensionListCompetenceDimension);
                if (oldCompetenceFkOfCompetenceDimensionListCompetenceDimension != null) {
                    oldCompetenceFkOfCompetenceDimensionListCompetenceDimension.getCompetenceDimensionList().remove(competenceDimensionListCompetenceDimension);
                    oldCompetenceFkOfCompetenceDimensionListCompetenceDimension = em.merge(oldCompetenceFkOfCompetenceDimensionListCompetenceDimension);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findCompetence(competence.getId()) != null) {
                throw new PreexistingEntityException("Competence " + competence + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Competence competence) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Competence persistentCompetence = em.find(Competence.class, competence.getId());
            List<RoleCompetition> roleCompetitionListOld = persistentCompetence.getRoleCompetitionList();
            List<RoleCompetition> roleCompetitionListNew = competence.getRoleCompetitionList();
            List<ProjectTechCompetence> projectTechCompetenceListOld = persistentCompetence.getProjectTechCompetenceList();
            List<ProjectTechCompetence> projectTechCompetenceListNew = competence.getProjectTechCompetenceList();
            List<CompetenceValue> competenceValueListOld = persistentCompetence.getCompetenceValueList();
            List<CompetenceValue> competenceValueListNew = competence.getCompetenceValueList();
            List<CompetenceDimension> competenceDimensionListOld = persistentCompetence.getCompetenceDimensionList();
            List<CompetenceDimension> competenceDimensionListNew = competence.getCompetenceDimensionList();
            List<String> illegalOrphanMessages = null;
            for (RoleCompetition roleCompetitionListOldRoleCompetition : roleCompetitionListOld) {
                if (!roleCompetitionListNew.contains(roleCompetitionListOldRoleCompetition)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RoleCompetition " + roleCompetitionListOldRoleCompetition + " since its competenceFk field is not nullable.");
                }
            }
            for (ProjectTechCompetence projectTechCompetenceListOldProjectTechCompetence : projectTechCompetenceListOld) {
                if (!projectTechCompetenceListNew.contains(projectTechCompetenceListOldProjectTechCompetence)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ProjectTechCompetence " + projectTechCompetenceListOldProjectTechCompetence + " since its competenceFk field is not nullable.");
                }
            }
            for (CompetenceValue competenceValueListOldCompetenceValue : competenceValueListOld) {
                if (!competenceValueListNew.contains(competenceValueListOldCompetenceValue)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain CompetenceValue " + competenceValueListOldCompetenceValue + " since its competenceFk field is not nullable.");
                }
            }
            for (CompetenceDimension competenceDimensionListOldCompetenceDimension : competenceDimensionListOld) {
                if (!competenceDimensionListNew.contains(competenceDimensionListOldCompetenceDimension)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain CompetenceDimension " + competenceDimensionListOldCompetenceDimension + " since its competenceFk field is not nullable.");
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
            competence.setRoleCompetitionList(roleCompetitionListNew);
            List<ProjectTechCompetence> attachedProjectTechCompetenceListNew = new ArrayList<ProjectTechCompetence>();
            for (ProjectTechCompetence projectTechCompetenceListNewProjectTechCompetenceToAttach : projectTechCompetenceListNew) {
                projectTechCompetenceListNewProjectTechCompetenceToAttach = em.getReference(projectTechCompetenceListNewProjectTechCompetenceToAttach.getClass(), projectTechCompetenceListNewProjectTechCompetenceToAttach.getId());
                attachedProjectTechCompetenceListNew.add(projectTechCompetenceListNewProjectTechCompetenceToAttach);
            }
            projectTechCompetenceListNew = attachedProjectTechCompetenceListNew;
            competence.setProjectTechCompetenceList(projectTechCompetenceListNew);
            List<CompetenceValue> attachedCompetenceValueListNew = new ArrayList<CompetenceValue>();
            for (CompetenceValue competenceValueListNewCompetenceValueToAttach : competenceValueListNew) {
                competenceValueListNewCompetenceValueToAttach = em.getReference(competenceValueListNewCompetenceValueToAttach.getClass(), competenceValueListNewCompetenceValueToAttach.getId());
                attachedCompetenceValueListNew.add(competenceValueListNewCompetenceValueToAttach);
            }
            competenceValueListNew = attachedCompetenceValueListNew;
            competence.setCompetenceValueList(competenceValueListNew);
            List<CompetenceDimension> attachedCompetenceDimensionListNew = new ArrayList<CompetenceDimension>();
            for (CompetenceDimension competenceDimensionListNewCompetenceDimensionToAttach : competenceDimensionListNew) {
                competenceDimensionListNewCompetenceDimensionToAttach = em.getReference(competenceDimensionListNewCompetenceDimensionToAttach.getClass(), competenceDimensionListNewCompetenceDimensionToAttach.getId());
                attachedCompetenceDimensionListNew.add(competenceDimensionListNewCompetenceDimensionToAttach);
            }
            competenceDimensionListNew = attachedCompetenceDimensionListNew;
            competence.setCompetenceDimensionList(competenceDimensionListNew);
            competence = em.merge(competence);
            for (RoleCompetition roleCompetitionListNewRoleCompetition : roleCompetitionListNew) {
                if (!roleCompetitionListOld.contains(roleCompetitionListNewRoleCompetition)) {
                    Competence oldCompetenceFkOfRoleCompetitionListNewRoleCompetition = roleCompetitionListNewRoleCompetition.getCompetenceFk();
                    roleCompetitionListNewRoleCompetition.setCompetenceFk(competence);
                    roleCompetitionListNewRoleCompetition = em.merge(roleCompetitionListNewRoleCompetition);
                    if (oldCompetenceFkOfRoleCompetitionListNewRoleCompetition != null && !oldCompetenceFkOfRoleCompetitionListNewRoleCompetition.equals(competence)) {
                        oldCompetenceFkOfRoleCompetitionListNewRoleCompetition.getRoleCompetitionList().remove(roleCompetitionListNewRoleCompetition);
                        oldCompetenceFkOfRoleCompetitionListNewRoleCompetition = em.merge(oldCompetenceFkOfRoleCompetitionListNewRoleCompetition);
                    }
                }
            }
            for (ProjectTechCompetence projectTechCompetenceListNewProjectTechCompetence : projectTechCompetenceListNew) {
                if (!projectTechCompetenceListOld.contains(projectTechCompetenceListNewProjectTechCompetence)) {
                    Competence oldCompetenceFkOfProjectTechCompetenceListNewProjectTechCompetence = projectTechCompetenceListNewProjectTechCompetence.getCompetenceFk();
                    projectTechCompetenceListNewProjectTechCompetence.setCompetenceFk(competence);
                    projectTechCompetenceListNewProjectTechCompetence = em.merge(projectTechCompetenceListNewProjectTechCompetence);
                    if (oldCompetenceFkOfProjectTechCompetenceListNewProjectTechCompetence != null && !oldCompetenceFkOfProjectTechCompetenceListNewProjectTechCompetence.equals(competence)) {
                        oldCompetenceFkOfProjectTechCompetenceListNewProjectTechCompetence.getProjectTechCompetenceList().remove(projectTechCompetenceListNewProjectTechCompetence);
                        oldCompetenceFkOfProjectTechCompetenceListNewProjectTechCompetence = em.merge(oldCompetenceFkOfProjectTechCompetenceListNewProjectTechCompetence);
                    }
                }
            }
            for (CompetenceValue competenceValueListNewCompetenceValue : competenceValueListNew) {
                if (!competenceValueListOld.contains(competenceValueListNewCompetenceValue)) {
                    Competence oldCompetenceFkOfCompetenceValueListNewCompetenceValue = competenceValueListNewCompetenceValue.getCompetenceFk();
                    competenceValueListNewCompetenceValue.setCompetenceFk(competence);
                    competenceValueListNewCompetenceValue = em.merge(competenceValueListNewCompetenceValue);
                    if (oldCompetenceFkOfCompetenceValueListNewCompetenceValue != null && !oldCompetenceFkOfCompetenceValueListNewCompetenceValue.equals(competence)) {
                        oldCompetenceFkOfCompetenceValueListNewCompetenceValue.getCompetenceValueList().remove(competenceValueListNewCompetenceValue);
                        oldCompetenceFkOfCompetenceValueListNewCompetenceValue = em.merge(oldCompetenceFkOfCompetenceValueListNewCompetenceValue);
                    }
                }
            }
            for (CompetenceDimension competenceDimensionListNewCompetenceDimension : competenceDimensionListNew) {
                if (!competenceDimensionListOld.contains(competenceDimensionListNewCompetenceDimension)) {
                    Competence oldCompetenceFkOfCompetenceDimensionListNewCompetenceDimension = competenceDimensionListNewCompetenceDimension.getCompetenceFk();
                    competenceDimensionListNewCompetenceDimension.setCompetenceFk(competence);
                    competenceDimensionListNewCompetenceDimension = em.merge(competenceDimensionListNewCompetenceDimension);
                    if (oldCompetenceFkOfCompetenceDimensionListNewCompetenceDimension != null && !oldCompetenceFkOfCompetenceDimensionListNewCompetenceDimension.equals(competence)) {
                        oldCompetenceFkOfCompetenceDimensionListNewCompetenceDimension.getCompetenceDimensionList().remove(competenceDimensionListNewCompetenceDimension);
                        oldCompetenceFkOfCompetenceDimensionListNewCompetenceDimension = em.merge(oldCompetenceFkOfCompetenceDimensionListNewCompetenceDimension);
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
                Long id = competence.getId();
                if (findCompetence(id) == null) {
                    throw new NonexistentEntityException("The competence with id " + id + " no longer exists.");
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
            Competence competence;
            try {
                competence = em.getReference(Competence.class, id);
                competence.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The competence with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<RoleCompetition> roleCompetitionListOrphanCheck = competence.getRoleCompetitionList();
            for (RoleCompetition roleCompetitionListOrphanCheckRoleCompetition : roleCompetitionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Competence (" + competence + ") cannot be destroyed since the RoleCompetition " + roleCompetitionListOrphanCheckRoleCompetition + " in its roleCompetitionList field has a non-nullable competenceFk field.");
            }
            List<ProjectTechCompetence> projectTechCompetenceListOrphanCheck = competence.getProjectTechCompetenceList();
            for (ProjectTechCompetence projectTechCompetenceListOrphanCheckProjectTechCompetence : projectTechCompetenceListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Competence (" + competence + ") cannot be destroyed since the ProjectTechCompetence " + projectTechCompetenceListOrphanCheckProjectTechCompetence + " in its projectTechCompetenceList field has a non-nullable competenceFk field.");
            }
            List<CompetenceValue> competenceValueListOrphanCheck = competence.getCompetenceValueList();
            for (CompetenceValue competenceValueListOrphanCheckCompetenceValue : competenceValueListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Competence (" + competence + ") cannot be destroyed since the CompetenceValue " + competenceValueListOrphanCheckCompetenceValue + " in its competenceValueList field has a non-nullable competenceFk field.");
            }
            List<CompetenceDimension> competenceDimensionListOrphanCheck = competence.getCompetenceDimensionList();
            for (CompetenceDimension competenceDimensionListOrphanCheckCompetenceDimension : competenceDimensionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Competence (" + competence + ") cannot be destroyed since the CompetenceDimension " + competenceDimensionListOrphanCheckCompetenceDimension + " in its competenceDimensionList field has a non-nullable competenceFk field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(competence);
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

    public List<Competence> findCompetenceEntities() {
        return findCompetenceEntities(true, -1, -1);
    }

    public List<Competence> findCompetenceEntities(int maxResults, int firstResult) {
        return findCompetenceEntities(false, maxResults, firstResult);
    }

    private List<Competence> findCompetenceEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Competence.class));
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

    public Competence findCompetence(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Competence.class, id);
        } finally {
            em.close();
        }
    }

    public int getCompetenceCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Competence> rt = cq.from(Competence.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
