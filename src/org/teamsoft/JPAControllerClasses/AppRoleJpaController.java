/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.JPAControllerClasses;

import org.teamsoft.entity.AppRole;
import org.teamsoft.entity.UserRole;
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
public class AppRoleJpaController implements Serializable {

    public AppRoleJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AppRole appRole) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (appRole.getUserRoleList() == null) {
            appRole.setUserRoleList(new ArrayList<UserRole>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<UserRole> attachedUserRoleList = new ArrayList<UserRole>();
            for (UserRole userRoleListUserRoleToAttach : appRole.getUserRoleList()) {
                userRoleListUserRoleToAttach = em.getReference(userRoleListUserRoleToAttach.getClass(), userRoleListUserRoleToAttach.getId());
                attachedUserRoleList.add(userRoleListUserRoleToAttach);
            }
            appRole.setUserRoleList(attachedUserRoleList);
            em.persist(appRole);
            for (UserRole userRoleListUserRole : appRole.getUserRoleList()) {
                AppRole oldRoleIdOfUserRoleListUserRole = userRoleListUserRole.getRoleId();
                userRoleListUserRole.setRoleId(appRole);
                userRoleListUserRole = em.merge(userRoleListUserRole);
                if (oldRoleIdOfUserRoleListUserRole != null) {
                    oldRoleIdOfUserRoleListUserRole.getUserRoleList().remove(userRoleListUserRole);
                    oldRoleIdOfUserRoleListUserRole = em.merge(oldRoleIdOfUserRoleListUserRole);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findAppRole(appRole.getRoleId()) != null) {
                throw new PreexistingEntityException("AppRole " + appRole + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AppRole appRole) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            AppRole persistentAppRole = em.find(AppRole.class, appRole.getRoleId());
            List<UserRole> userRoleListOld = persistentAppRole.getUserRoleList();
            List<UserRole> userRoleListNew = appRole.getUserRoleList();
            List<String> illegalOrphanMessages = null;
            for (UserRole userRoleListOldUserRole : userRoleListOld) {
                if (!userRoleListNew.contains(userRoleListOldUserRole)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain UserRole " + userRoleListOldUserRole + " since its roleId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<UserRole> attachedUserRoleListNew = new ArrayList<UserRole>();
            for (UserRole userRoleListNewUserRoleToAttach : userRoleListNew) {
                userRoleListNewUserRoleToAttach = em.getReference(userRoleListNewUserRoleToAttach.getClass(), userRoleListNewUserRoleToAttach.getId());
                attachedUserRoleListNew.add(userRoleListNewUserRoleToAttach);
            }
            userRoleListNew = attachedUserRoleListNew;
            appRole.setUserRoleList(userRoleListNew);
            appRole = em.merge(appRole);
            for (UserRole userRoleListNewUserRole : userRoleListNew) {
                if (!userRoleListOld.contains(userRoleListNewUserRole)) {
                    AppRole oldRoleIdOfUserRoleListNewUserRole = userRoleListNewUserRole.getRoleId();
                    userRoleListNewUserRole.setRoleId(appRole);
                    userRoleListNewUserRole = em.merge(userRoleListNewUserRole);
                    if (oldRoleIdOfUserRoleListNewUserRole != null && !oldRoleIdOfUserRoleListNewUserRole.equals(appRole)) {
                        oldRoleIdOfUserRoleListNewUserRole.getUserRoleList().remove(userRoleListNewUserRole);
                        oldRoleIdOfUserRoleListNewUserRole = em.merge(oldRoleIdOfUserRoleListNewUserRole);
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
                Long id = appRole.getRoleId();
                if (findAppRole(id) == null) {
                    throw new NonexistentEntityException("The appRole with id " + id + " no longer exists.");
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
            AppRole appRole;
            try {
                appRole = em.getReference(AppRole.class, id);
                appRole.getRoleId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The appRole with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<UserRole> userRoleListOrphanCheck = appRole.getUserRoleList();
            for (UserRole userRoleListOrphanCheckUserRole : userRoleListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AppRole (" + appRole + ") cannot be destroyed since the UserRole " + userRoleListOrphanCheckUserRole + " in its userRoleList field has a non-nullable roleId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(appRole);
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

    public List<AppRole> findAppRoleEntities() {
        return findAppRoleEntities(true, -1, -1);
    }

    public List<AppRole> findAppRoleEntities(int maxResults, int firstResult) {
        return findAppRoleEntities(false, maxResults, firstResult);
    }

    private List<AppRole> findAppRoleEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AppRole.class));
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

    public AppRole findAppRole(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AppRole.class, id);
        } finally {
            em.close();
        }
    }

    public int getAppRoleCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AppRole> rt = cq.from(AppRole.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
