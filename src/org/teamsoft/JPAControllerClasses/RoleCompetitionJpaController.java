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
public class RoleCompetitionJpaController implements Serializable {

    public RoleCompetitionJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(RoleCompetition roleCompetition) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Competence competenceFk = roleCompetition.getCompetenceFk();
            if (competenceFk != null) {
                competenceFk = em.getReference(competenceFk.getClass(), competenceFk.getId());
                roleCompetition.setCompetenceFk(competenceFk);
            }
            CompetenceImportance compImportanceFk = roleCompetition.getCompImportanceFk();
            if (compImportanceFk != null) {
                compImportanceFk = em.getReference(compImportanceFk.getClass(), compImportanceFk.getId());
                roleCompetition.setCompImportanceFk(compImportanceFk);
            }
            Levels levelsFk = roleCompetition.getLevelsFk();
            if (levelsFk != null) {
                levelsFk = em.getReference(levelsFk.getClass(), levelsFk.getId());
                roleCompetition.setLevelsFk(levelsFk);
            }
            Role rolesFk = roleCompetition.getRolesFk();
            if (rolesFk != null) {
                rolesFk = em.getReference(rolesFk.getClass(), rolesFk.getId());
                roleCompetition.setRolesFk(rolesFk);
            }
            em.persist(roleCompetition);
            if (competenceFk != null) {
                competenceFk.getRoleCompetitionList().add(roleCompetition);
                competenceFk = em.merge(competenceFk);
            }
            if (compImportanceFk != null) {
                compImportanceFk.getRoleCompetitionList().add(roleCompetition);
                compImportanceFk = em.merge(compImportanceFk);
            }
            if (levelsFk != null) {
                levelsFk.getRoleCompetitionList().add(roleCompetition);
                levelsFk = em.merge(levelsFk);
            }
            if (rolesFk != null) {
                rolesFk.getRoleCompetitionList().add(roleCompetition);
                rolesFk = em.merge(rolesFk);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findRoleCompetition(roleCompetition.getId()) != null) {
                throw new PreexistingEntityException("RoleCompetition " + roleCompetition + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(RoleCompetition roleCompetition) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            RoleCompetition persistentRoleCompetition = em.find(RoleCompetition.class, roleCompetition.getId());
            Competence competenceFkOld = persistentRoleCompetition.getCompetenceFk();
            Competence competenceFkNew = roleCompetition.getCompetenceFk();
            CompetenceImportance compImportanceFkOld = persistentRoleCompetition.getCompImportanceFk();
            CompetenceImportance compImportanceFkNew = roleCompetition.getCompImportanceFk();
            Levels levelsFkOld = persistentRoleCompetition.getLevelsFk();
            Levels levelsFkNew = roleCompetition.getLevelsFk();
            Role rolesFkOld = persistentRoleCompetition.getRolesFk();
            Role rolesFkNew = roleCompetition.getRolesFk();
            if (competenceFkNew != null) {
                competenceFkNew = em.getReference(competenceFkNew.getClass(), competenceFkNew.getId());
                roleCompetition.setCompetenceFk(competenceFkNew);
            }
            if (compImportanceFkNew != null) {
                compImportanceFkNew = em.getReference(compImportanceFkNew.getClass(), compImportanceFkNew.getId());
                roleCompetition.setCompImportanceFk(compImportanceFkNew);
            }
            if (levelsFkNew != null) {
                levelsFkNew = em.getReference(levelsFkNew.getClass(), levelsFkNew.getId());
                roleCompetition.setLevelsFk(levelsFkNew);
            }
            if (rolesFkNew != null) {
                rolesFkNew = em.getReference(rolesFkNew.getClass(), rolesFkNew.getId());
                roleCompetition.setRolesFk(rolesFkNew);
            }
            roleCompetition = em.merge(roleCompetition);
            if (competenceFkOld != null && !competenceFkOld.equals(competenceFkNew)) {
                competenceFkOld.getRoleCompetitionList().remove(roleCompetition);
                competenceFkOld = em.merge(competenceFkOld);
            }
            if (competenceFkNew != null && !competenceFkNew.equals(competenceFkOld)) {
                competenceFkNew.getRoleCompetitionList().add(roleCompetition);
                competenceFkNew = em.merge(competenceFkNew);
            }
            if (compImportanceFkOld != null && !compImportanceFkOld.equals(compImportanceFkNew)) {
                compImportanceFkOld.getRoleCompetitionList().remove(roleCompetition);
                compImportanceFkOld = em.merge(compImportanceFkOld);
            }
            if (compImportanceFkNew != null && !compImportanceFkNew.equals(compImportanceFkOld)) {
                compImportanceFkNew.getRoleCompetitionList().add(roleCompetition);
                compImportanceFkNew = em.merge(compImportanceFkNew);
            }
            if (levelsFkOld != null && !levelsFkOld.equals(levelsFkNew)) {
                levelsFkOld.getRoleCompetitionList().remove(roleCompetition);
                levelsFkOld = em.merge(levelsFkOld);
            }
            if (levelsFkNew != null && !levelsFkNew.equals(levelsFkOld)) {
                levelsFkNew.getRoleCompetitionList().add(roleCompetition);
                levelsFkNew = em.merge(levelsFkNew);
            }
            if (rolesFkOld != null && !rolesFkOld.equals(rolesFkNew)) {
                rolesFkOld.getRoleCompetitionList().remove(roleCompetition);
                rolesFkOld = em.merge(rolesFkOld);
            }
            if (rolesFkNew != null && !rolesFkNew.equals(rolesFkOld)) {
                rolesFkNew.getRoleCompetitionList().add(roleCompetition);
                rolesFkNew = em.merge(rolesFkNew);
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
                Long id = roleCompetition.getId();
                if (findRoleCompetition(id) == null) {
                    throw new NonexistentEntityException("The roleCompetition with id " + id + " no longer exists.");
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
            RoleCompetition roleCompetition;
            try {
                roleCompetition = em.getReference(RoleCompetition.class, id);
                roleCompetition.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The roleCompetition with id " + id + " no longer exists.", enfe);
            }
            Competence competenceFk = roleCompetition.getCompetenceFk();
            if (competenceFk != null) {
                competenceFk.getRoleCompetitionList().remove(roleCompetition);
                competenceFk = em.merge(competenceFk);
            }
            CompetenceImportance compImportanceFk = roleCompetition.getCompImportanceFk();
            if (compImportanceFk != null) {
                compImportanceFk.getRoleCompetitionList().remove(roleCompetition);
                compImportanceFk = em.merge(compImportanceFk);
            }
            Levels levelsFk = roleCompetition.getLevelsFk();
            if (levelsFk != null) {
                levelsFk.getRoleCompetitionList().remove(roleCompetition);
                levelsFk = em.merge(levelsFk);
            }
            Role rolesFk = roleCompetition.getRolesFk();
            if (rolesFk != null) {
                rolesFk.getRoleCompetitionList().remove(roleCompetition);
                rolesFk = em.merge(rolesFk);
            }
            em.remove(roleCompetition);
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

    public List<RoleCompetition> findRoleCompetitionEntities() {
        return findRoleCompetitionEntities(true, -1, -1);
    }

    public List<RoleCompetition> findRoleCompetitionEntities(int maxResults, int firstResult) {
        return findRoleCompetitionEntities(false, maxResults, firstResult);
    }

    private List<RoleCompetition> findRoleCompetitionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(RoleCompetition.class));
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

    public RoleCompetition findRoleCompetition(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RoleCompetition.class, id);
        } finally {
            em.close();
        }
    }

    public int getRoleCompetitionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<RoleCompetition> rt = cq.from(RoleCompetition.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
