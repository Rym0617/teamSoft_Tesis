/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.JPAControllerClasses;

import org.teamsoft.entity.Competence;
import org.teamsoft.entity.CompetenceDimension;
import org.teamsoft.entity.Levels;
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
public class CompetenceDimensionJpaController implements Serializable {

    public CompetenceDimensionJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CompetenceDimension competenceDimension) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Competence competenceFk = competenceDimension.getCompetenceFk();
            if (competenceFk != null) {
                competenceFk = em.getReference(competenceFk.getClass(), competenceFk.getId());
                competenceDimension.setCompetenceFk(competenceFk);
            }
            Levels levelFk = competenceDimension.getLevelFk();
            if (levelFk != null) {
                levelFk = em.getReference(levelFk.getClass(), levelFk.getId());
                competenceDimension.setLevelFk(levelFk);
            }
            em.persist(competenceDimension);
            if (competenceFk != null) {
                competenceFk.getCompetenceDimensionList().add(competenceDimension);
                competenceFk = em.merge(competenceFk);
            }
            if (levelFk != null) {
                levelFk.getCompetenceDimensionList().add(competenceDimension);
                levelFk = em.merge(levelFk);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findCompetenceDimension(competenceDimension.getId()) != null) {
                throw new PreexistingEntityException("CompetenceDimension " + competenceDimension + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CompetenceDimension competenceDimension) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            CompetenceDimension persistentCompetenceDimension = em.find(CompetenceDimension.class, competenceDimension.getId());
            Competence competenceFkOld = persistentCompetenceDimension.getCompetenceFk();
            Competence competenceFkNew = competenceDimension.getCompetenceFk();
            Levels levelFkOld = persistentCompetenceDimension.getLevelFk();
            Levels levelFkNew = competenceDimension.getLevelFk();
            if (competenceFkNew != null) {
                competenceFkNew = em.getReference(competenceFkNew.getClass(), competenceFkNew.getId());
                competenceDimension.setCompetenceFk(competenceFkNew);
            }
            if (levelFkNew != null) {
                levelFkNew = em.getReference(levelFkNew.getClass(), levelFkNew.getId());
                competenceDimension.setLevelFk(levelFkNew);
            }
            competenceDimension = em.merge(competenceDimension);
            if (competenceFkOld != null && !competenceFkOld.equals(competenceFkNew)) {
                competenceFkOld.getCompetenceDimensionList().remove(competenceDimension);
                competenceFkOld = em.merge(competenceFkOld);
            }
            if (competenceFkNew != null && !competenceFkNew.equals(competenceFkOld)) {
                competenceFkNew.getCompetenceDimensionList().add(competenceDimension);
                competenceFkNew = em.merge(competenceFkNew);
            }
            if (levelFkOld != null && !levelFkOld.equals(levelFkNew)) {
                levelFkOld.getCompetenceDimensionList().remove(competenceDimension);
                levelFkOld = em.merge(levelFkOld);
            }
            if (levelFkNew != null && !levelFkNew.equals(levelFkOld)) {
                levelFkNew.getCompetenceDimensionList().add(competenceDimension);
                levelFkNew = em.merge(levelFkNew);
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
                Long id = competenceDimension.getId();
                if (findCompetenceDimension(id) == null) {
                    throw new NonexistentEntityException("The competenceDimension with id " + id + " no longer exists.");
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
            CompetenceDimension competenceDimension;
            try {
                competenceDimension = em.getReference(CompetenceDimension.class, id);
                competenceDimension.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The competenceDimension with id " + id + " no longer exists.", enfe);
            }
            Competence competenceFk = competenceDimension.getCompetenceFk();
            if (competenceFk != null) {
                competenceFk.getCompetenceDimensionList().remove(competenceDimension);
                competenceFk = em.merge(competenceFk);
            }
            Levels levelFk = competenceDimension.getLevelFk();
            if (levelFk != null) {
                levelFk.getCompetenceDimensionList().remove(competenceDimension);
                levelFk = em.merge(levelFk);
            }
            em.remove(competenceDimension);
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

    public List<CompetenceDimension> findCompetenceDimensionEntities() {
        return findCompetenceDimensionEntities(true, -1, -1);
    }

    public List<CompetenceDimension> findCompetenceDimensionEntities(int maxResults, int firstResult) {
        return findCompetenceDimensionEntities(false, maxResults, firstResult);
    }

    private List<CompetenceDimension> findCompetenceDimensionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CompetenceDimension.class));
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

    public CompetenceDimension findCompetenceDimension(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CompetenceDimension.class, id);
        } finally {
            em.close();
        }
    }

    public int getCompetenceDimensionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CompetenceDimension> rt = cq.from(CompetenceDimension.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
