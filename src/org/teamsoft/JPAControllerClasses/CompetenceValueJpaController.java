/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.JPAControllerClasses;

import org.teamsoft.entity.Competence;
import org.teamsoft.entity.CompetenceValue;
import org.teamsoft.entity.Levels;
import org.teamsoft.entity.Worker;
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
public class CompetenceValueJpaController implements Serializable {

    public CompetenceValueJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CompetenceValue competenceValue) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Competence competenceFk = competenceValue.getCompetenceFk();
            if (competenceFk != null) {
                competenceFk = em.getReference(competenceFk.getClass(), competenceFk.getId());
                competenceValue.setCompetenceFk(competenceFk);
            }
            Levels levelFk = competenceValue.getLevelFk();
            if (levelFk != null) {
                levelFk = em.getReference(levelFk.getClass(), levelFk.getId());
                competenceValue.setLevelFk(levelFk);
            }
            Worker workersFk = competenceValue.getWorkersFk();
            if (workersFk != null) {
                workersFk = em.getReference(workersFk.getClass(), workersFk.getId());
                competenceValue.setWorkersFk(workersFk);
            }
            em.persist(competenceValue);
            if (competenceFk != null) {
                competenceFk.getCompetenceValueList().add(competenceValue);
                competenceFk = em.merge(competenceFk);
            }
            if (levelFk != null) {
                levelFk.getCompetenceValueList().add(competenceValue);
                levelFk = em.merge(levelFk);
            }
            if (workersFk != null) {
                workersFk.getCompetenceValueList().add(competenceValue);
                workersFk = em.merge(workersFk);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findCompetenceValue(competenceValue.getId()) != null) {
                throw new PreexistingEntityException("CompetenceValue " + competenceValue + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CompetenceValue competenceValue) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            CompetenceValue persistentCompetenceValue = em.find(CompetenceValue.class, competenceValue.getId());
            Competence competenceFkOld = persistentCompetenceValue.getCompetenceFk();
            Competence competenceFkNew = competenceValue.getCompetenceFk();
            Levels levelFkOld = persistentCompetenceValue.getLevelFk();
            Levels levelFkNew = competenceValue.getLevelFk();
            Worker workersFkOld = persistentCompetenceValue.getWorkersFk();
            Worker workersFkNew = competenceValue.getWorkersFk();
            if (competenceFkNew != null) {
                competenceFkNew = em.getReference(competenceFkNew.getClass(), competenceFkNew.getId());
                competenceValue.setCompetenceFk(competenceFkNew);
            }
            if (levelFkNew != null) {
                levelFkNew = em.getReference(levelFkNew.getClass(), levelFkNew.getId());
                competenceValue.setLevelFk(levelFkNew);
            }
            if (workersFkNew != null) {
                workersFkNew = em.getReference(workersFkNew.getClass(), workersFkNew.getId());
                competenceValue.setWorkersFk(workersFkNew);
            }
            competenceValue = em.merge(competenceValue);
            if (competenceFkOld != null && !competenceFkOld.equals(competenceFkNew)) {
                competenceFkOld.getCompetenceValueList().remove(competenceValue);
                competenceFkOld = em.merge(competenceFkOld);
            }
            if (competenceFkNew != null && !competenceFkNew.equals(competenceFkOld)) {
                competenceFkNew.getCompetenceValueList().add(competenceValue);
                competenceFkNew = em.merge(competenceFkNew);
            }
            if (levelFkOld != null && !levelFkOld.equals(levelFkNew)) {
                levelFkOld.getCompetenceValueList().remove(competenceValue);
                levelFkOld = em.merge(levelFkOld);
            }
            if (levelFkNew != null && !levelFkNew.equals(levelFkOld)) {
                levelFkNew.getCompetenceValueList().add(competenceValue);
                levelFkNew = em.merge(levelFkNew);
            }
            if (workersFkOld != null && !workersFkOld.equals(workersFkNew)) {
                workersFkOld.getCompetenceValueList().remove(competenceValue);
                workersFkOld = em.merge(workersFkOld);
            }
            if (workersFkNew != null && !workersFkNew.equals(workersFkOld)) {
                workersFkNew.getCompetenceValueList().add(competenceValue);
                workersFkNew = em.merge(workersFkNew);
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
                Long id = competenceValue.getId();
                if (findCompetenceValue(id) == null) {
                    throw new NonexistentEntityException("The competenceValue with id " + id + " no longer exists.");
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
            CompetenceValue competenceValue;
            try {
                competenceValue = em.getReference(CompetenceValue.class, id);
                competenceValue.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The competenceValue with id " + id + " no longer exists.", enfe);
            }
            Competence competenceFk = competenceValue.getCompetenceFk();
            if (competenceFk != null) {
                competenceFk.getCompetenceValueList().remove(competenceValue);
                competenceFk = em.merge(competenceFk);
            }
            Levels levelFk = competenceValue.getLevelFk();
            if (levelFk != null) {
                levelFk.getCompetenceValueList().remove(competenceValue);
                levelFk = em.merge(levelFk);
            }
            Worker workersFk = competenceValue.getWorkersFk();
            if (workersFk != null) {
                workersFk.getCompetenceValueList().remove(competenceValue);
                workersFk = em.merge(workersFk);
            }
            em.remove(competenceValue);
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

    public List<CompetenceValue> findCompetenceValueEntities() {
        return findCompetenceValueEntities(true, -1, -1);
    }

    public List<CompetenceValue> findCompetenceValueEntities(int maxResults, int firstResult) {
        return findCompetenceValueEntities(false, maxResults, firstResult);
    }

    private List<CompetenceValue> findCompetenceValueEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CompetenceValue.class));
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

    public CompetenceValue findCompetenceValue(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CompetenceValue.class, id);
        } finally {
            em.close();
        }
    }

    public int getCompetenceValueCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CompetenceValue> rt = cq.from(CompetenceValue.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
