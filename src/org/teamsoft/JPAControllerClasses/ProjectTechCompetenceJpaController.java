/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.JPAControllerClasses;

import org.teamsoft.entity.*;
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
public class ProjectTechCompetenceJpaController implements Serializable {

    public ProjectTechCompetenceJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ProjectTechCompetence projectTechCompetence) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Competence competenceFk = projectTechCompetence.getCompetenceFk();
            if (competenceFk != null) {
                competenceFk = em.getReference(competenceFk.getClass(), competenceFk.getId());
                projectTechCompetence.setCompetenceFk(competenceFk);
            }
            CompetenceImportance competenceImportanceFk = projectTechCompetence.getCompetenceImportanceFk();
            if (competenceImportanceFk != null) {
                competenceImportanceFk = em.getReference(competenceImportanceFk.getClass(), competenceImportanceFk.getId());
                projectTechCompetence.setCompetenceImportanceFk(competenceImportanceFk);
            }
            Levels levelFk = projectTechCompetence.getLevelFk();
            if (levelFk != null) {
                levelFk = em.getReference(levelFk.getClass(), levelFk.getId());
                projectTechCompetence.setLevelFk(levelFk);
            }
            ProjectRoles projectRoles = projectTechCompetence.getProjectRoles();
            if (projectRoles != null) {
                projectRoles = em.getReference(projectRoles.getClass(), projectRoles.getId());
                projectTechCompetence.setProjectRoles(projectRoles);
            }
            em.persist(projectTechCompetence);
            if (competenceFk != null) {
                competenceFk.getProjectTechCompetenceList().add(projectTechCompetence);
                competenceFk = em.merge(competenceFk);
            }
            if (competenceImportanceFk != null) {
                competenceImportanceFk.getProjectTechCompetenceList().add(projectTechCompetence);
                competenceImportanceFk = em.merge(competenceImportanceFk);
            }
            if (levelFk != null) {
                levelFk.getProjectTechCompetenceList().add(projectTechCompetence);
                levelFk = em.merge(levelFk);
            }
            if (projectRoles != null) {
                projectRoles.getProjectTechCompetenceList().add(projectTechCompetence);
                projectRoles = em.merge(projectRoles);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findProjectTechCompetence(projectTechCompetence.getId()) != null) {
                throw new PreexistingEntityException("ProjectTechCompetence " + projectTechCompetence + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ProjectTechCompetence projectTechCompetence) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ProjectTechCompetence persistentProjectTechCompetence = em.find(ProjectTechCompetence.class, projectTechCompetence.getId());
            Competence competenceFkOld = persistentProjectTechCompetence.getCompetenceFk();
            Competence competenceFkNew = projectTechCompetence.getCompetenceFk();
            CompetenceImportance competenceImportanceFkOld = persistentProjectTechCompetence.getCompetenceImportanceFk();
            CompetenceImportance competenceImportanceFkNew = projectTechCompetence.getCompetenceImportanceFk();
            Levels levelFkOld = persistentProjectTechCompetence.getLevelFk();
            Levels levelFkNew = projectTechCompetence.getLevelFk();
            ProjectRoles projectRolesOld = persistentProjectTechCompetence.getProjectRoles();
            ProjectRoles projectRolesNew = projectTechCompetence.getProjectRoles();
            if (competenceFkNew != null) {
                competenceFkNew = em.getReference(competenceFkNew.getClass(), competenceFkNew.getId());
                projectTechCompetence.setCompetenceFk(competenceFkNew);
            }
            if (competenceImportanceFkNew != null) {
                competenceImportanceFkNew = em.getReference(competenceImportanceFkNew.getClass(), competenceImportanceFkNew.getId());
                projectTechCompetence.setCompetenceImportanceFk(competenceImportanceFkNew);
            }
            if (levelFkNew != null) {
                levelFkNew = em.getReference(levelFkNew.getClass(), levelFkNew.getId());
                projectTechCompetence.setLevelFk(levelFkNew);
            }
            if (projectRolesNew != null) {
                projectRolesNew = em.getReference(projectRolesNew.getClass(), projectRolesNew.getId());
                projectTechCompetence.setProjectRoles(projectRolesNew);
            }
            projectTechCompetence = em.merge(projectTechCompetence);
            if (competenceFkOld != null && !competenceFkOld.equals(competenceFkNew)) {
                competenceFkOld.getProjectTechCompetenceList().remove(projectTechCompetence);
                competenceFkOld = em.merge(competenceFkOld);
            }
            if (competenceFkNew != null && !competenceFkNew.equals(competenceFkOld)) {
                competenceFkNew.getProjectTechCompetenceList().add(projectTechCompetence);
                competenceFkNew = em.merge(competenceFkNew);
            }
            if (competenceImportanceFkOld != null && !competenceImportanceFkOld.equals(competenceImportanceFkNew)) {
                competenceImportanceFkOld.getProjectTechCompetenceList().remove(projectTechCompetence);
                competenceImportanceFkOld = em.merge(competenceImportanceFkOld);
            }
            if (competenceImportanceFkNew != null && !competenceImportanceFkNew.equals(competenceImportanceFkOld)) {
                competenceImportanceFkNew.getProjectTechCompetenceList().add(projectTechCompetence);
                competenceImportanceFkNew = em.merge(competenceImportanceFkNew);
            }
            if (levelFkOld != null && !levelFkOld.equals(levelFkNew)) {
                levelFkOld.getProjectTechCompetenceList().remove(projectTechCompetence);
                levelFkOld = em.merge(levelFkOld);
            }
            if (levelFkNew != null && !levelFkNew.equals(levelFkOld)) {
                levelFkNew.getProjectTechCompetenceList().add(projectTechCompetence);
                levelFkNew = em.merge(levelFkNew);
            }
            if (projectRolesOld != null && !projectRolesOld.equals(projectRolesNew)) {
                projectRolesOld.getProjectTechCompetenceList().remove(projectTechCompetence);
                projectRolesOld = em.merge(projectRolesOld);
            }
            if (projectRolesNew != null && !projectRolesNew.equals(projectRolesOld)) {
                projectRolesNew.getProjectTechCompetenceList().add(projectTechCompetence);
                projectRolesNew = em.merge(projectRolesNew);
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
                Long id = projectTechCompetence.getId();
                if (findProjectTechCompetence(id) == null) {
                    throw new NonexistentEntityException("The projectTechCompetence with id " + id + " no longer exists.");
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
            ProjectTechCompetence projectTechCompetence;
            try {
                projectTechCompetence = em.getReference(ProjectTechCompetence.class, id);
                projectTechCompetence.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The projectTechCompetence with id " + id + " no longer exists.", enfe);
            }
            Competence competenceFk = projectTechCompetence.getCompetenceFk();
            if (competenceFk != null) {
                competenceFk.getProjectTechCompetenceList().remove(projectTechCompetence);
                competenceFk = em.merge(competenceFk);
            }
            CompetenceImportance competenceImportanceFk = projectTechCompetence.getCompetenceImportanceFk();
            if (competenceImportanceFk != null) {
                competenceImportanceFk.getProjectTechCompetenceList().remove(projectTechCompetence);
                competenceImportanceFk = em.merge(competenceImportanceFk);
            }
            Levels levelFk = projectTechCompetence.getLevelFk();
            if (levelFk != null) {
                levelFk.getProjectTechCompetenceList().remove(projectTechCompetence);
                levelFk = em.merge(levelFk);
            }
            ProjectRoles projectRoles = projectTechCompetence.getProjectRoles();
            if (projectRoles != null) {
                projectRoles.getProjectTechCompetenceList().remove(projectTechCompetence);
                projectRoles = em.merge(projectRoles);
            }
            em.remove(projectTechCompetence);
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

    public List<ProjectTechCompetence> findProjectTechCompetenceEntities() {
        return findProjectTechCompetenceEntities(true, -1, -1);
    }

    public List<ProjectTechCompetence> findProjectTechCompetenceEntities(int maxResults, int firstResult) {
        return findProjectTechCompetenceEntities(false, maxResults, firstResult);
    }

    private List<ProjectTechCompetence> findProjectTechCompetenceEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ProjectTechCompetence.class));
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

    public ProjectTechCompetence findProjectTechCompetence(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ProjectTechCompetence.class, id);
        } finally {
            em.close();
        }
    }

    public int getProjectTechCompetenceCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ProjectTechCompetence> rt = cq.from(ProjectTechCompetence.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
