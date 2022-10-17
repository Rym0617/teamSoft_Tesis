/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.JPAControllerClasses;

import org.teamsoft.entity.ClientEntity;
import org.teamsoft.entity.County;
import org.teamsoft.entity.Cycle;
import org.teamsoft.entity.Project;
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
public class ProjectJpaController implements Serializable {

    public ProjectJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Project project) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (project.getCycleList() == null) {
            project.setCycleList(new ArrayList<Cycle>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ClientEntity clientEntityFk = project.getClientEntityFk();
            if (clientEntityFk != null) {
                clientEntityFk = em.getReference(clientEntityFk.getClass(), clientEntityFk.getId());
                project.setClientEntityFk(clientEntityFk);
            }
            County provinceFk = project.getProvinceFk();
            if (provinceFk != null) {
                provinceFk = em.getReference(provinceFk.getClass(), provinceFk.getId());
                project.setProvinceFk(provinceFk);
            }
            List<Cycle> attachedCycleList = new ArrayList<Cycle>();
            for (Cycle cycleListCycleToAttach : project.getCycleList()) {
                cycleListCycleToAttach = em.getReference(cycleListCycleToAttach.getClass(), cycleListCycleToAttach.getId());
                attachedCycleList.add(cycleListCycleToAttach);
            }
            project.setCycleList(attachedCycleList);
            em.persist(project);
            if (clientEntityFk != null) {
                clientEntityFk.getProjectList().add(project);
                clientEntityFk = em.merge(clientEntityFk);
            }
            if (provinceFk != null) {
                provinceFk.getProjectList().add(project);
                provinceFk = em.merge(provinceFk);
            }
            for (Cycle cycleListCycle : project.getCycleList()) {
                Project oldProjectFkOfCycleListCycle = cycleListCycle.getProjectFk();
                cycleListCycle.setProjectFk(project);
                cycleListCycle = em.merge(cycleListCycle);
                if (oldProjectFkOfCycleListCycle != null) {
                    oldProjectFkOfCycleListCycle.getCycleList().remove(cycleListCycle);
                    oldProjectFkOfCycleListCycle = em.merge(oldProjectFkOfCycleListCycle);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findProject(project.getId()) != null) {
                throw new PreexistingEntityException("Project " + project + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Project project) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Project persistentProject = em.find(Project.class, project.getId());
            ClientEntity clientEntityFkOld = persistentProject.getClientEntityFk();
            ClientEntity clientEntityFkNew = project.getClientEntityFk();
            County provinceFkOld = persistentProject.getProvinceFk();
            County provinceFkNew = project.getProvinceFk();
            List<Cycle> cycleListOld = persistentProject.getCycleList();
            List<Cycle> cycleListNew = project.getCycleList();
            if (clientEntityFkNew != null) {
                clientEntityFkNew = em.getReference(clientEntityFkNew.getClass(), clientEntityFkNew.getId());
                project.setClientEntityFk(clientEntityFkNew);
            }
            if (provinceFkNew != null) {
                provinceFkNew = em.getReference(provinceFkNew.getClass(), provinceFkNew.getId());
                project.setProvinceFk(provinceFkNew);
            }
            List<Cycle> attachedCycleListNew = new ArrayList<Cycle>();
            for (Cycle cycleListNewCycleToAttach : cycleListNew) {
                cycleListNewCycleToAttach = em.getReference(cycleListNewCycleToAttach.getClass(), cycleListNewCycleToAttach.getId());
                attachedCycleListNew.add(cycleListNewCycleToAttach);
            }
            cycleListNew = attachedCycleListNew;
            project.setCycleList(cycleListNew);
            project = em.merge(project);
            if (clientEntityFkOld != null && !clientEntityFkOld.equals(clientEntityFkNew)) {
                clientEntityFkOld.getProjectList().remove(project);
                clientEntityFkOld = em.merge(clientEntityFkOld);
            }
            if (clientEntityFkNew != null && !clientEntityFkNew.equals(clientEntityFkOld)) {
                clientEntityFkNew.getProjectList().add(project);
                clientEntityFkNew = em.merge(clientEntityFkNew);
            }
            if (provinceFkOld != null && !provinceFkOld.equals(provinceFkNew)) {
                provinceFkOld.getProjectList().remove(project);
                provinceFkOld = em.merge(provinceFkOld);
            }
            if (provinceFkNew != null && !provinceFkNew.equals(provinceFkOld)) {
                provinceFkNew.getProjectList().add(project);
                provinceFkNew = em.merge(provinceFkNew);
            }
            for (Cycle cycleListOldCycle : cycleListOld) {
                if (!cycleListNew.contains(cycleListOldCycle)) {
                    cycleListOldCycle.setProjectFk(null);
                    cycleListOldCycle = em.merge(cycleListOldCycle);
                }
            }
            for (Cycle cycleListNewCycle : cycleListNew) {
                if (!cycleListOld.contains(cycleListNewCycle)) {
                    Project oldProjectFkOfCycleListNewCycle = cycleListNewCycle.getProjectFk();
                    cycleListNewCycle.setProjectFk(project);
                    cycleListNewCycle = em.merge(cycleListNewCycle);
                    if (oldProjectFkOfCycleListNewCycle != null && !oldProjectFkOfCycleListNewCycle.equals(project)) {
                        oldProjectFkOfCycleListNewCycle.getCycleList().remove(cycleListNewCycle);
                        oldProjectFkOfCycleListNewCycle = em.merge(oldProjectFkOfCycleListNewCycle);
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
                Long id = project.getId();
                if (findProject(id) == null) {
                    throw new NonexistentEntityException("The project with id " + id + " no longer exists.");
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
            Project project;
            try {
                project = em.getReference(Project.class, id);
                project.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The project with id " + id + " no longer exists.", enfe);
            }
            ClientEntity clientEntityFk = project.getClientEntityFk();
            if (clientEntityFk != null) {
                clientEntityFk.getProjectList().remove(project);
                clientEntityFk = em.merge(clientEntityFk);
            }
            County provinceFk = project.getProvinceFk();
            if (provinceFk != null) {
                provinceFk.getProjectList().remove(project);
                provinceFk = em.merge(provinceFk);
            }
            List<Cycle> cycleList = project.getCycleList();
            for (Cycle cycleListCycle : cycleList) {
                cycleListCycle.setProjectFk(null);
                cycleListCycle = em.merge(cycleListCycle);
            }
            em.remove(project);
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

    public List<Project> findProjectEntities() {
        return findProjectEntities(true, -1, -1);
    }

    public List<Project> findProjectEntities(int maxResults, int firstResult) {
        return findProjectEntities(false, maxResults, firstResult);
    }

    private List<Project> findProjectEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Project.class));
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

    public Project findProject(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Project.class, id);
        } finally {
            em.close();
        }
    }

    public int getProjectCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Project> rt = cq.from(Project.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
