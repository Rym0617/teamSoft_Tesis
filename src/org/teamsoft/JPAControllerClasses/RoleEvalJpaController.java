/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.JPAControllerClasses;

import org.teamsoft.entity.RoleEval;
import org.teamsoft.entity.RoleEvalProject;
import org.teamsoft.entity.RoleEvaluation;
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
public class RoleEvalJpaController implements Serializable {

    public RoleEvalJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(RoleEval roleEval) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (roleEval.getRoleEvaluationList() == null) {
            roleEval.setRoleEvaluationList(new ArrayList<RoleEvaluation>());
        }
        if (roleEval.getRoleEvalProjectList() == null) {
            roleEval.setRoleEvalProjectList(new ArrayList<RoleEvalProject>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<RoleEvaluation> attachedRoleEvaluationList = new ArrayList<RoleEvaluation>();
            for (RoleEvaluation roleEvaluationListRoleEvaluationToAttach : roleEval.getRoleEvaluationList()) {
                roleEvaluationListRoleEvaluationToAttach = em.getReference(roleEvaluationListRoleEvaluationToAttach.getClass(), roleEvaluationListRoleEvaluationToAttach.getId());
                attachedRoleEvaluationList.add(roleEvaluationListRoleEvaluationToAttach);
            }
            roleEval.setRoleEvaluationList(attachedRoleEvaluationList);
            List<RoleEvalProject> attachedRoleEvalProjectList = new ArrayList<RoleEvalProject>();
            for (RoleEvalProject roleEvalProjectListRoleEvalProjectToAttach : roleEval.getRoleEvalProjectList()) {
                roleEvalProjectListRoleEvalProjectToAttach = em.getReference(roleEvalProjectListRoleEvalProjectToAttach.getClass(), roleEvalProjectListRoleEvalProjectToAttach.getId());
                attachedRoleEvalProjectList.add(roleEvalProjectListRoleEvalProjectToAttach);
            }
            roleEval.setRoleEvalProjectList(attachedRoleEvalProjectList);
            em.persist(roleEval);
            for (RoleEvaluation roleEvaluationListRoleEvaluation : roleEval.getRoleEvaluationList()) {
                RoleEval oldRolEvalFkOfRoleEvaluationListRoleEvaluation = roleEvaluationListRoleEvaluation.getRolEvalFk();
                roleEvaluationListRoleEvaluation.setRolEvalFk(roleEval);
                roleEvaluationListRoleEvaluation = em.merge(roleEvaluationListRoleEvaluation);
                if (oldRolEvalFkOfRoleEvaluationListRoleEvaluation != null) {
                    oldRolEvalFkOfRoleEvaluationListRoleEvaluation.getRoleEvaluationList().remove(roleEvaluationListRoleEvaluation);
                    oldRolEvalFkOfRoleEvaluationListRoleEvaluation = em.merge(oldRolEvalFkOfRoleEvaluationListRoleEvaluation);
                }
            }
            for (RoleEvalProject roleEvalProjectListRoleEvalProject : roleEval.getRoleEvalProjectList()) {
                RoleEval oldRoleEvalFkOfRoleEvalProjectListRoleEvalProject = roleEvalProjectListRoleEvalProject.getRoleEvalFk();
                roleEvalProjectListRoleEvalProject.setRoleEvalFk(roleEval);
                roleEvalProjectListRoleEvalProject = em.merge(roleEvalProjectListRoleEvalProject);
                if (oldRoleEvalFkOfRoleEvalProjectListRoleEvalProject != null) {
                    oldRoleEvalFkOfRoleEvalProjectListRoleEvalProject.getRoleEvalProjectList().remove(roleEvalProjectListRoleEvalProject);
                    oldRoleEvalFkOfRoleEvalProjectListRoleEvalProject = em.merge(oldRoleEvalFkOfRoleEvalProjectListRoleEvalProject);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findRoleEval(roleEval.getId()) != null) {
                throw new PreexistingEntityException("RoleEval " + roleEval + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(RoleEval roleEval) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            RoleEval persistentRoleEval = em.find(RoleEval.class, roleEval.getId());
            List<RoleEvaluation> roleEvaluationListOld = persistentRoleEval.getRoleEvaluationList();
            List<RoleEvaluation> roleEvaluationListNew = roleEval.getRoleEvaluationList();
            List<RoleEvalProject> roleEvalProjectListOld = persistentRoleEval.getRoleEvalProjectList();
            List<RoleEvalProject> roleEvalProjectListNew = roleEval.getRoleEvalProjectList();
            List<String> illegalOrphanMessages = null;
            for (RoleEvaluation roleEvaluationListOldRoleEvaluation : roleEvaluationListOld) {
                if (!roleEvaluationListNew.contains(roleEvaluationListOldRoleEvaluation)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RoleEvaluation " + roleEvaluationListOldRoleEvaluation + " since its rolEvalFk field is not nullable.");
                }
            }
            for (RoleEvalProject roleEvalProjectListOldRoleEvalProject : roleEvalProjectListOld) {
                if (!roleEvalProjectListNew.contains(roleEvalProjectListOldRoleEvalProject)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RoleEvalProject " + roleEvalProjectListOldRoleEvalProject + " since its roleEvalFk field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<RoleEvaluation> attachedRoleEvaluationListNew = new ArrayList<RoleEvaluation>();
            for (RoleEvaluation roleEvaluationListNewRoleEvaluationToAttach : roleEvaluationListNew) {
                roleEvaluationListNewRoleEvaluationToAttach = em.getReference(roleEvaluationListNewRoleEvaluationToAttach.getClass(), roleEvaluationListNewRoleEvaluationToAttach.getId());
                attachedRoleEvaluationListNew.add(roleEvaluationListNewRoleEvaluationToAttach);
            }
            roleEvaluationListNew = attachedRoleEvaluationListNew;
            roleEval.setRoleEvaluationList(roleEvaluationListNew);
            List<RoleEvalProject> attachedRoleEvalProjectListNew = new ArrayList<RoleEvalProject>();
            for (RoleEvalProject roleEvalProjectListNewRoleEvalProjectToAttach : roleEvalProjectListNew) {
                roleEvalProjectListNewRoleEvalProjectToAttach = em.getReference(roleEvalProjectListNewRoleEvalProjectToAttach.getClass(), roleEvalProjectListNewRoleEvalProjectToAttach.getId());
                attachedRoleEvalProjectListNew.add(roleEvalProjectListNewRoleEvalProjectToAttach);
            }
            roleEvalProjectListNew = attachedRoleEvalProjectListNew;
            roleEval.setRoleEvalProjectList(roleEvalProjectListNew);
            roleEval = em.merge(roleEval);
            for (RoleEvaluation roleEvaluationListNewRoleEvaluation : roleEvaluationListNew) {
                if (!roleEvaluationListOld.contains(roleEvaluationListNewRoleEvaluation)) {
                    RoleEval oldRolEvalFkOfRoleEvaluationListNewRoleEvaluation = roleEvaluationListNewRoleEvaluation.getRolEvalFk();
                    roleEvaluationListNewRoleEvaluation.setRolEvalFk(roleEval);
                    roleEvaluationListNewRoleEvaluation = em.merge(roleEvaluationListNewRoleEvaluation);
                    if (oldRolEvalFkOfRoleEvaluationListNewRoleEvaluation != null && !oldRolEvalFkOfRoleEvaluationListNewRoleEvaluation.equals(roleEval)) {
                        oldRolEvalFkOfRoleEvaluationListNewRoleEvaluation.getRoleEvaluationList().remove(roleEvaluationListNewRoleEvaluation);
                        oldRolEvalFkOfRoleEvaluationListNewRoleEvaluation = em.merge(oldRolEvalFkOfRoleEvaluationListNewRoleEvaluation);
                    }
                }
            }
            for (RoleEvalProject roleEvalProjectListNewRoleEvalProject : roleEvalProjectListNew) {
                if (!roleEvalProjectListOld.contains(roleEvalProjectListNewRoleEvalProject)) {
                    RoleEval oldRoleEvalFkOfRoleEvalProjectListNewRoleEvalProject = roleEvalProjectListNewRoleEvalProject.getRoleEvalFk();
                    roleEvalProjectListNewRoleEvalProject.setRoleEvalFk(roleEval);
                    roleEvalProjectListNewRoleEvalProject = em.merge(roleEvalProjectListNewRoleEvalProject);
                    if (oldRoleEvalFkOfRoleEvalProjectListNewRoleEvalProject != null && !oldRoleEvalFkOfRoleEvalProjectListNewRoleEvalProject.equals(roleEval)) {
                        oldRoleEvalFkOfRoleEvalProjectListNewRoleEvalProject.getRoleEvalProjectList().remove(roleEvalProjectListNewRoleEvalProject);
                        oldRoleEvalFkOfRoleEvalProjectListNewRoleEvalProject = em.merge(oldRoleEvalFkOfRoleEvalProjectListNewRoleEvalProject);
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
                Long id = roleEval.getId();
                if (findRoleEval(id) == null) {
                    throw new NonexistentEntityException("The roleEval with id " + id + " no longer exists.");
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
            RoleEval roleEval;
            try {
                roleEval = em.getReference(RoleEval.class, id);
                roleEval.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The roleEval with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<RoleEvaluation> roleEvaluationListOrphanCheck = roleEval.getRoleEvaluationList();
            for (RoleEvaluation roleEvaluationListOrphanCheckRoleEvaluation : roleEvaluationListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This RoleEval (" + roleEval + ") cannot be destroyed since the RoleEvaluation " + roleEvaluationListOrphanCheckRoleEvaluation + " in its roleEvaluationList field has a non-nullable rolEvalFk field.");
            }
            List<RoleEvalProject> roleEvalProjectListOrphanCheck = roleEval.getRoleEvalProjectList();
            for (RoleEvalProject roleEvalProjectListOrphanCheckRoleEvalProject : roleEvalProjectListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This RoleEval (" + roleEval + ") cannot be destroyed since the RoleEvalProject " + roleEvalProjectListOrphanCheckRoleEvalProject + " in its roleEvalProjectList field has a non-nullable roleEvalFk field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(roleEval);
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

    public List<RoleEval> findRoleEvalEntities() {
        return findRoleEvalEntities(true, -1, -1);
    }

    public List<RoleEval> findRoleEvalEntities(int maxResults, int firstResult) {
        return findRoleEvalEntities(false, maxResults, firstResult);
    }

    private List<RoleEval> findRoleEvalEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(RoleEval.class));
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

    public RoleEval findRoleEval(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RoleEval.class, id);
        } finally {
            em.close();
        }
    }

    public int getRoleEvalCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<RoleEval> rt = cq.from(RoleEval.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
