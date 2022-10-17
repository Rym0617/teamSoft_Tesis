/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.JPAControllerClasses;

import org.teamsoft.entity.AppRole;
import org.teamsoft.entity.AppUser;
import org.teamsoft.entity.UserRole;
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
public class UserRoleJpaController implements Serializable {

    public UserRoleJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(UserRole userRole) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            AppRole roleId = userRole.getRoleId();
            if (roleId != null) {
                roleId = em.getReference(roleId.getClass(), roleId.getRoleId());
                userRole.setRoleId(roleId);
            }
            AppUser userId = userRole.getUserId();
            if (userId != null) {
                userId = em.getReference(userId.getClass(), userId.getUserId());
                userRole.setUserId(userId);
            }
            em.persist(userRole);
            if (roleId != null) {
                roleId.getUserRoleList().add(userRole);
                roleId = em.merge(roleId);
            }
            if (userId != null) {
                userId.getUserRoleList().add(userRole);
                userId = em.merge(userId);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findUserRole(userRole.getId()) != null) {
                throw new PreexistingEntityException("UserRole " + userRole + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(UserRole userRole) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            UserRole persistentUserRole = em.find(UserRole.class, userRole.getId());
            AppRole roleIdOld = persistentUserRole.getRoleId();
            AppRole roleIdNew = userRole.getRoleId();
            AppUser userIdOld = persistentUserRole.getUserId();
            AppUser userIdNew = userRole.getUserId();
            if (roleIdNew != null) {
                roleIdNew = em.getReference(roleIdNew.getClass(), roleIdNew.getRoleId());
                userRole.setRoleId(roleIdNew);
            }
            if (userIdNew != null) {
                userIdNew = em.getReference(userIdNew.getClass(), userIdNew.getUserId());
                userRole.setUserId(userIdNew);
            }
            userRole = em.merge(userRole);
            if (roleIdOld != null && !roleIdOld.equals(roleIdNew)) {
                roleIdOld.getUserRoleList().remove(userRole);
                roleIdOld = em.merge(roleIdOld);
            }
            if (roleIdNew != null && !roleIdNew.equals(roleIdOld)) {
                roleIdNew.getUserRoleList().add(userRole);
                roleIdNew = em.merge(roleIdNew);
            }
            if (userIdOld != null && !userIdOld.equals(userIdNew)) {
                userIdOld.getUserRoleList().remove(userRole);
                userIdOld = em.merge(userIdOld);
            }
            if (userIdNew != null && !userIdNew.equals(userIdOld)) {
                userIdNew.getUserRoleList().add(userRole);
                userIdNew = em.merge(userIdNew);
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
                Long id = userRole.getId();
                if (findUserRole(id) == null) {
                    throw new NonexistentEntityException("The userRole with id " + id + " no longer exists.");
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
            UserRole userRole;
            try {
                userRole = em.getReference(UserRole.class, id);
                userRole.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The userRole with id " + id + " no longer exists.", enfe);
            }
            AppRole roleId = userRole.getRoleId();
            if (roleId != null) {
                roleId.getUserRoleList().remove(userRole);
                roleId = em.merge(roleId);
            }
            AppUser userId = userRole.getUserId();
            if (userId != null) {
                userId.getUserRoleList().remove(userRole);
                userId = em.merge(userId);
            }
            em.remove(userRole);
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

    public List<UserRole> findUserRoleEntities() {
        return findUserRoleEntities(true, -1, -1);
    }

    public List<UserRole> findUserRoleEntities(int maxResults, int firstResult) {
        return findUserRoleEntities(false, maxResults, firstResult);
    }

    private List<UserRole> findUserRoleEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UserRole.class));
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

    public UserRole findUserRole(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UserRole.class, id);
        } finally {
            em.close();
        }
    }

    public int getUserRoleCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UserRole> rt = cq.from(UserRole.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
