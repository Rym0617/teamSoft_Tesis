/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.JPAControllerClasses;

import org.teamsoft.entity.PersonGroup;
import org.teamsoft.entity.Worker;
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
 * @author yoyo
 */
public class PersonGroupJpaController implements Serializable {

    public PersonGroupJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PersonGroup personGroup) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (personGroup.getWorkerList() == null) {
            personGroup.setWorkerList(new ArrayList<Worker>());
        }
        if (personGroup.getPersonGroupList() == null) {
            personGroup.setPersonGroupList(new ArrayList<PersonGroup>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            PersonGroup parentGroup = personGroup.getParentGroup();
            if (parentGroup != null) {
                parentGroup = em.getReference(parentGroup.getClass(), parentGroup.getId());
                personGroup.setParentGroup(parentGroup);
            }
            List<Worker> attachedWorkerList = new ArrayList<Worker>();
            for (Worker workerListWorkerToAttach : personGroup.getWorkerList()) {
                workerListWorkerToAttach = em.getReference(workerListWorkerToAttach.getClass(), workerListWorkerToAttach.getId());
                attachedWorkerList.add(workerListWorkerToAttach);
            }
            personGroup.setWorkerList(attachedWorkerList);
            List<PersonGroup> attachedPersonGroupList = new ArrayList<PersonGroup>();
            for (PersonGroup personGroupListPersonGroupToAttach : personGroup.getPersonGroupList()) {
                personGroupListPersonGroupToAttach = em.getReference(personGroupListPersonGroupToAttach.getClass(), personGroupListPersonGroupToAttach.getId());
                attachedPersonGroupList.add(personGroupListPersonGroupToAttach);
            }
            personGroup.setPersonGroupList(attachedPersonGroupList);
            em.persist(personGroup);
            if (parentGroup != null) {
                parentGroup.getPersonGroupList().add(personGroup);
                parentGroup = em.merge(parentGroup);
            }
            for (Worker workerListWorker : personGroup.getWorkerList()) {
                PersonGroup oldGroupFkOfWorkerListWorker = workerListWorker.getGroupFk();
                workerListWorker.setGroupFk(personGroup);
                workerListWorker = em.merge(workerListWorker);
                if (oldGroupFkOfWorkerListWorker != null) {
                    oldGroupFkOfWorkerListWorker.getWorkerList().remove(workerListWorker);
                    oldGroupFkOfWorkerListWorker = em.merge(oldGroupFkOfWorkerListWorker);
                }
            }
            for (PersonGroup personGroupListPersonGroup : personGroup.getPersonGroupList()) {
                PersonGroup oldParentGroupOfPersonGroupListPersonGroup = personGroupListPersonGroup.getParentGroup();
                personGroupListPersonGroup.setParentGroup(personGroup);
                personGroupListPersonGroup = em.merge(personGroupListPersonGroup);
                if (oldParentGroupOfPersonGroupListPersonGroup != null) {
                    oldParentGroupOfPersonGroupListPersonGroup.getPersonGroupList().remove(personGroupListPersonGroup);
                    oldParentGroupOfPersonGroupListPersonGroup = em.merge(oldParentGroupOfPersonGroupListPersonGroup);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPersonGroup(personGroup.getId()) != null) {
                throw new PreexistingEntityException("PersonGroup " + personGroup + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PersonGroup personGroup) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            PersonGroup persistentPersonGroup = em.find(PersonGroup.class, personGroup.getId());
            PersonGroup parentGroupOld = persistentPersonGroup.getParentGroup();
            PersonGroup parentGroupNew = personGroup.getParentGroup();
            List<Worker> workerListOld = persistentPersonGroup.getWorkerList();
            List<Worker> workerListNew = personGroup.getWorkerList();
            List<PersonGroup> personGroupListOld = persistentPersonGroup.getPersonGroupList();
            List<PersonGroup> personGroupListNew = personGroup.getPersonGroupList();
            List<String> illegalOrphanMessages = null;
            for (Worker workerListOldWorker : workerListOld) {
                if (!workerListNew.contains(workerListOldWorker)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Worker " + workerListOldWorker + " since its groupFk field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (parentGroupNew != null) {
                parentGroupNew = em.getReference(parentGroupNew.getClass(), parentGroupNew.getId());
                personGroup.setParentGroup(parentGroupNew);
            }
            List<Worker> attachedWorkerListNew = new ArrayList<Worker>();
            for (Worker workerListNewWorkerToAttach : workerListNew) {
                workerListNewWorkerToAttach = em.getReference(workerListNewWorkerToAttach.getClass(), workerListNewWorkerToAttach.getId());
                attachedWorkerListNew.add(workerListNewWorkerToAttach);
            }
            workerListNew = attachedWorkerListNew;
            personGroup.setWorkerList(workerListNew);
            List<PersonGroup> attachedPersonGroupListNew = new ArrayList<PersonGroup>();
            for (PersonGroup personGroupListNewPersonGroupToAttach : personGroupListNew) {
                personGroupListNewPersonGroupToAttach = em.getReference(personGroupListNewPersonGroupToAttach.getClass(), personGroupListNewPersonGroupToAttach.getId());
                attachedPersonGroupListNew.add(personGroupListNewPersonGroupToAttach);
            }
            personGroupListNew = attachedPersonGroupListNew;
            personGroup.setPersonGroupList(personGroupListNew);
            personGroup = em.merge(personGroup);
            if (parentGroupOld != null && !parentGroupOld.equals(parentGroupNew)) {
                parentGroupOld.getPersonGroupList().remove(personGroup);
                parentGroupOld = em.merge(parentGroupOld);
            }
            if (parentGroupNew != null && !parentGroupNew.equals(parentGroupOld)) {
                parentGroupNew.getPersonGroupList().add(personGroup);
                parentGroupNew = em.merge(parentGroupNew);
            }
            for (Worker workerListNewWorker : workerListNew) {
                if (!workerListOld.contains(workerListNewWorker)) {
                    PersonGroup oldGroupFkOfWorkerListNewWorker = workerListNewWorker.getGroupFk();
                    workerListNewWorker.setGroupFk(personGroup);
                    workerListNewWorker = em.merge(workerListNewWorker);
                    if (oldGroupFkOfWorkerListNewWorker != null && !oldGroupFkOfWorkerListNewWorker.equals(personGroup)) {
                        oldGroupFkOfWorkerListNewWorker.getWorkerList().remove(workerListNewWorker);
                        oldGroupFkOfWorkerListNewWorker = em.merge(oldGroupFkOfWorkerListNewWorker);
                    }
                }
            }
            for (PersonGroup personGroupListOldPersonGroup : personGroupListOld) {
                if (!personGroupListNew.contains(personGroupListOldPersonGroup)) {
                    personGroupListOldPersonGroup.setParentGroup(null);
                    personGroupListOldPersonGroup = em.merge(personGroupListOldPersonGroup);
                }
            }
            for (PersonGroup personGroupListNewPersonGroup : personGroupListNew) {
                if (!personGroupListOld.contains(personGroupListNewPersonGroup)) {
                    PersonGroup oldParentGroupOfPersonGroupListNewPersonGroup = personGroupListNewPersonGroup.getParentGroup();
                    personGroupListNewPersonGroup.setParentGroup(personGroup);
                    personGroupListNewPersonGroup = em.merge(personGroupListNewPersonGroup);
                    if (oldParentGroupOfPersonGroupListNewPersonGroup != null && !oldParentGroupOfPersonGroupListNewPersonGroup.equals(personGroup)) {
                        oldParentGroupOfPersonGroupListNewPersonGroup.getPersonGroupList().remove(personGroupListNewPersonGroup);
                        oldParentGroupOfPersonGroupListNewPersonGroup = em.merge(oldParentGroupOfPersonGroupListNewPersonGroup);
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
                Long id = personGroup.getId();
                if (findPersonGroup(id) == null) {
                    throw new NonexistentEntityException("The personGroup with id " + id + " no longer exists.");
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
            PersonGroup personGroup;
            try {
                personGroup = em.getReference(PersonGroup.class, id);
                personGroup.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The personGroup with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Worker> workerListOrphanCheck = personGroup.getWorkerList();
            for (Worker workerListOrphanCheckWorker : workerListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This PersonGroup (" + personGroup + ") cannot be destroyed since the Worker " + workerListOrphanCheckWorker + " in its workerList field has a non-nullable groupFk field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            PersonGroup parentGroup = personGroup.getParentGroup();
            if (parentGroup != null) {
                parentGroup.getPersonGroupList().remove(personGroup);
                parentGroup = em.merge(parentGroup);
            }
            List<PersonGroup> personGroupList = personGroup.getPersonGroupList();
            for (PersonGroup personGroupListPersonGroup : personGroupList) {
                personGroupListPersonGroup.setParentGroup(null);
                personGroupListPersonGroup = em.merge(personGroupListPersonGroup);
            }
            em.remove(personGroup);
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

    public List<PersonGroup> findPersonGroupEntities() {
        return findPersonGroupEntities(true, -1, -1);
    }

    public List<PersonGroup> findPersonGroupEntities(int maxResults, int firstResult) {
        return findPersonGroupEntities(false, maxResults, firstResult);
    }

    private List<PersonGroup> findPersonGroupEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PersonGroup.class));
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

    public PersonGroup findPersonGroup(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PersonGroup.class, id);
        } finally {
            em.close();
        }
    }

    public int getPersonGroupCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PersonGroup> rt = cq.from(PersonGroup.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
