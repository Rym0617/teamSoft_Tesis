/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.JPAControllerClasses;

import org.teamsoft.entity.ClientEntity;
import org.teamsoft.entity.Project;
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
public class ClientEntityJpaController implements Serializable {

    public ClientEntityJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ClientEntity clientEntity) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (clientEntity.getProjectList() == null) {
            clientEntity.setProjectList(new ArrayList<Project>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Project> attachedProjectList = new ArrayList<Project>();
            for (Project projectListProjectToAttach : clientEntity.getProjectList()) {
                projectListProjectToAttach = em.getReference(projectListProjectToAttach.getClass(), projectListProjectToAttach.getId());
                attachedProjectList.add(projectListProjectToAttach);
            }
            clientEntity.setProjectList(attachedProjectList);
            em.persist(clientEntity);
            for (Project projectListProject : clientEntity.getProjectList()) {
                ClientEntity oldClientEntityFkOfProjectListProject = projectListProject.getClientEntityFk();
                projectListProject.setClientEntityFk(clientEntity);
                projectListProject = em.merge(projectListProject);
                if (oldClientEntityFkOfProjectListProject != null) {
                    oldClientEntityFkOfProjectListProject.getProjectList().remove(projectListProject);
                    oldClientEntityFkOfProjectListProject = em.merge(oldClientEntityFkOfProjectListProject);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findClientEntity(clientEntity.getId()) != null) {
                throw new PreexistingEntityException("ClientEntity " + clientEntity + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ClientEntity clientEntity) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ClientEntity persistentClientEntity = em.find(ClientEntity.class, clientEntity.getId());
            List<Project> projectListOld = persistentClientEntity.getProjectList();
            List<Project> projectListNew = clientEntity.getProjectList();
            List<String> illegalOrphanMessages = null;
            for (Project projectListOldProject : projectListOld) {
                if (!projectListNew.contains(projectListOldProject)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Project " + projectListOldProject + " since its clientEntityFk field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Project> attachedProjectListNew = new ArrayList<Project>();
            for (Project projectListNewProjectToAttach : projectListNew) {
                projectListNewProjectToAttach = em.getReference(projectListNewProjectToAttach.getClass(), projectListNewProjectToAttach.getId());
                attachedProjectListNew.add(projectListNewProjectToAttach);
            }
            projectListNew = attachedProjectListNew;
            clientEntity.setProjectList(projectListNew);
            clientEntity = em.merge(clientEntity);
            for (Project projectListNewProject : projectListNew) {
                if (!projectListOld.contains(projectListNewProject)) {
                    ClientEntity oldClientEntityFkOfProjectListNewProject = projectListNewProject.getClientEntityFk();
                    projectListNewProject.setClientEntityFk(clientEntity);
                    projectListNewProject = em.merge(projectListNewProject);
                    if (oldClientEntityFkOfProjectListNewProject != null && !oldClientEntityFkOfProjectListNewProject.equals(clientEntity)) {
                        oldClientEntityFkOfProjectListNewProject.getProjectList().remove(projectListNewProject);
                        oldClientEntityFkOfProjectListNewProject = em.merge(oldClientEntityFkOfProjectListNewProject);
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
                Long id = clientEntity.getId();
                if (findClientEntity(id) == null) {
                    throw new NonexistentEntityException("The clientEntity with id " + id + " no longer exists.");
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
            ClientEntity clientEntity;
            try {
                clientEntity = em.getReference(ClientEntity.class, id);
                clientEntity.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The clientEntity with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Project> projectListOrphanCheck = clientEntity.getProjectList();
            for (Project projectListOrphanCheckProject : projectListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ClientEntity (" + clientEntity + ") cannot be destroyed since the Project " + projectListOrphanCheckProject + " in its projectList field has a non-nullable clientEntityFk field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(clientEntity);
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

    public List<ClientEntity> findClientEntityEntities() {
        return findClientEntityEntities(true, -1, -1);
    }

    public List<ClientEntity> findClientEntityEntities(int maxResults, int firstResult) {
        return findClientEntityEntities(false, maxResults, firstResult);
    }

    private List<ClientEntity> findClientEntityEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ClientEntity.class));
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

    public ClientEntity findClientEntity(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ClientEntity.class, id);
        } finally {
            em.close();
        }
    }

    public int getClientEntityCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ClientEntity> rt = cq.from(ClientEntity.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
