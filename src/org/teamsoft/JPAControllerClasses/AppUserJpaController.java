/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.JPAControllerClasses;

import org.teamsoft.entity.AppUser;
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
public class AppUserJpaController implements Serializable {

    public AppUserJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AppUser appUser) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (appUser.getUserRoleList() == null) {
            appUser.setUserRoleList(new ArrayList<UserRole>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<UserRole> attachedUserRoleList = new ArrayList<UserRole>();
            for (UserRole userRoleListUserRoleToAttach : appUser.getUserRoleList()) {
                userRoleListUserRoleToAttach = em.getReference(userRoleListUserRoleToAttach.getClass(), userRoleListUserRoleToAttach.getId());
                attachedUserRoleList.add(userRoleListUserRoleToAttach);
            }
            appUser.setUserRoleList(attachedUserRoleList);
            em.persist(appUser);
            for (UserRole userRoleListUserRole : appUser.getUserRoleList()) {
                AppUser oldUserIdOfUserRoleListUserRole = userRoleListUserRole.getUserId();
                userRoleListUserRole.setUserId(appUser);
                userRoleListUserRole = em.merge(userRoleListUserRole);
                if (oldUserIdOfUserRoleListUserRole != null) {
                    oldUserIdOfUserRoleListUserRole.getUserRoleList().remove(userRoleListUserRole);
                    oldUserIdOfUserRoleListUserRole = em.merge(oldUserIdOfUserRoleListUserRole);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findAppUser(appUser.getUserId()) != null) {
                throw new PreexistingEntityException("AppUser " + appUser + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AppUser appUser) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            AppUser persistentAppUser = em.find(AppUser.class, appUser.getUserId());
            List<UserRole> userRoleListOld = persistentAppUser.getUserRoleList();
            List<UserRole> userRoleListNew = appUser.getUserRoleList();
            List<String> illegalOrphanMessages = null;
            for (UserRole userRoleListOldUserRole : userRoleListOld) {
                if (!userRoleListNew.contains(userRoleListOldUserRole)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain UserRole " + userRoleListOldUserRole + " since its userId field is not nullable.");
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
            appUser.setUserRoleList(userRoleListNew);
            appUser = em.merge(appUser);
            for (UserRole userRoleListNewUserRole : userRoleListNew) {
                if (!userRoleListOld.contains(userRoleListNewUserRole)) {
                    AppUser oldUserIdOfUserRoleListNewUserRole = userRoleListNewUserRole.getUserId();
                    userRoleListNewUserRole.setUserId(appUser);
                    userRoleListNewUserRole = em.merge(userRoleListNewUserRole);
                    if (oldUserIdOfUserRoleListNewUserRole != null && !oldUserIdOfUserRoleListNewUserRole.equals(appUser)) {
                        oldUserIdOfUserRoleListNewUserRole.getUserRoleList().remove(userRoleListNewUserRole);
                        oldUserIdOfUserRoleListNewUserRole = em.merge(oldUserIdOfUserRoleListNewUserRole);
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
                Long id = appUser.getUserId();
                if (findAppUser(id) == null) {
                    throw new NonexistentEntityException("The appUser with id " + id + " no longer exists.");
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
            AppUser appUser;
            try {
                appUser = em.getReference(AppUser.class, id);
                appUser.getUserId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The appUser with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<UserRole> userRoleListOrphanCheck = appUser.getUserRoleList();
            for (UserRole userRoleListOrphanCheckUserRole : userRoleListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AppUser (" + appUser + ") cannot be destroyed since the UserRole " + userRoleListOrphanCheckUserRole + " in its userRoleList field has a non-nullable userId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(appUser);
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

    public List<AppUser> findAppUserEntities() {
        return findAppUserEntities(true, -1, -1);
    }

    public List<AppUser> findAppUserEntities(int maxResults, int firstResult) {
        return findAppUserEntities(false, maxResults, firstResult);
    }

    private List<AppUser> findAppUserEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AppUser.class));
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

    public AppUser findAppUser(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AppUser.class, id);
        } finally {
            em.close();
        }
    }

    public int getAppUserCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AppUser> rt = cq.from(AppUser.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
