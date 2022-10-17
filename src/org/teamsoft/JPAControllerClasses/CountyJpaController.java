/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.JPAControllerClasses;

import org.teamsoft.entity.*;
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
public class CountyJpaController implements Serializable {

    public CountyJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(County county) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (county.getMunicipalityList() == null) {
            county.setMunicipalityList(new ArrayList<Municipality>());
        }
        if (county.getProjectList() == null) {
            county.setProjectList(new ArrayList<Project>());
        }
        if (county.getWorkerList() == null) {
            county.setWorkerList(new ArrayList<Worker>());
        }
        if (county.getCostDistanceList() == null) {
            county.setCostDistanceList(new ArrayList<CostDistance>());
        }
        if (county.getCostDistanceList1() == null) {
            county.setCostDistanceList1(new ArrayList<CostDistance>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Municipality> attachedMunicipalityList = new ArrayList<Municipality>();
            for (Municipality municipalityListMunicipalityToAttach : county.getMunicipalityList()) {
                municipalityListMunicipalityToAttach = em.getReference(municipalityListMunicipalityToAttach.getClass(), municipalityListMunicipalityToAttach.getId());
                attachedMunicipalityList.add(municipalityListMunicipalityToAttach);
            }
            county.setMunicipalityList(attachedMunicipalityList);
            List<Project> attachedProjectList = new ArrayList<Project>();
            for (Project projectListProjectToAttach : county.getProjectList()) {
                projectListProjectToAttach = em.getReference(projectListProjectToAttach.getClass(), projectListProjectToAttach.getId());
                attachedProjectList.add(projectListProjectToAttach);
            }
            county.setProjectList(attachedProjectList);
            List<Worker> attachedWorkerList = new ArrayList<Worker>();
            for (Worker workerListWorkerToAttach : county.getWorkerList()) {
                workerListWorkerToAttach = em.getReference(workerListWorkerToAttach.getClass(), workerListWorkerToAttach.getId());
                attachedWorkerList.add(workerListWorkerToAttach);
            }
            county.setWorkerList(attachedWorkerList);
            List<CostDistance> attachedCostDistanceList = new ArrayList<CostDistance>();
            for (CostDistance costDistanceListCostDistanceToAttach : county.getCostDistanceList()) {
                costDistanceListCostDistanceToAttach = em.getReference(costDistanceListCostDistanceToAttach.getClass(), costDistanceListCostDistanceToAttach.getId());
                attachedCostDistanceList.add(costDistanceListCostDistanceToAttach);
            }
            county.setCostDistanceList(attachedCostDistanceList);
            List<CostDistance> attachedCostDistanceList1 = new ArrayList<CostDistance>();
            for (CostDistance costDistanceList1CostDistanceToAttach : county.getCostDistanceList1()) {
                costDistanceList1CostDistanceToAttach = em.getReference(costDistanceList1CostDistanceToAttach.getClass(), costDistanceList1CostDistanceToAttach.getId());
                attachedCostDistanceList1.add(costDistanceList1CostDistanceToAttach);
            }
            county.setCostDistanceList1(attachedCostDistanceList1);
            em.persist(county);
            for (Municipality municipalityListMunicipality : county.getMunicipalityList()) {
                County oldCountyFkOfMunicipalityListMunicipality = municipalityListMunicipality.getCountyFk();
                municipalityListMunicipality.setCountyFk(county);
                municipalityListMunicipality = em.merge(municipalityListMunicipality);
                if (oldCountyFkOfMunicipalityListMunicipality != null) {
                    oldCountyFkOfMunicipalityListMunicipality.getMunicipalityList().remove(municipalityListMunicipality);
                    oldCountyFkOfMunicipalityListMunicipality = em.merge(oldCountyFkOfMunicipalityListMunicipality);
                }
            }
            for (Project projectListProject : county.getProjectList()) {
                County oldProvinceFkOfProjectListProject = projectListProject.getProvinceFk();
                projectListProject.setProvinceFk(county);
                projectListProject = em.merge(projectListProject);
                if (oldProvinceFkOfProjectListProject != null) {
                    oldProvinceFkOfProjectListProject.getProjectList().remove(projectListProject);
                    oldProvinceFkOfProjectListProject = em.merge(oldProvinceFkOfProjectListProject);
                }
            }
            for (Worker workerListWorker : county.getWorkerList()) {
                County oldCountyFkOfWorkerListWorker = workerListWorker.getCountyFk();
                workerListWorker.setCountyFk(county);
                workerListWorker = em.merge(workerListWorker);
                if (oldCountyFkOfWorkerListWorker != null) {
                    oldCountyFkOfWorkerListWorker.getWorkerList().remove(workerListWorker);
                    oldCountyFkOfWorkerListWorker = em.merge(oldCountyFkOfWorkerListWorker);
                }
            }
            for (CostDistance costDistanceListCostDistance : county.getCostDistanceList()) {
                County oldCountyAFkOfCostDistanceListCostDistance = costDistanceListCostDistance.getCountyAFk();
                costDistanceListCostDistance.setCountyAFk(county);
                costDistanceListCostDistance = em.merge(costDistanceListCostDistance);
                if (oldCountyAFkOfCostDistanceListCostDistance != null) {
                    oldCountyAFkOfCostDistanceListCostDistance.getCostDistanceList().remove(costDistanceListCostDistance);
                    oldCountyAFkOfCostDistanceListCostDistance = em.merge(oldCountyAFkOfCostDistanceListCostDistance);
                }
            }
            for (CostDistance costDistanceList1CostDistance : county.getCostDistanceList1()) {
                County oldCountyBFkOfCostDistanceList1CostDistance = costDistanceList1CostDistance.getCountyBFk();
                costDistanceList1CostDistance.setCountyBFk(county);
                costDistanceList1CostDistance = em.merge(costDistanceList1CostDistance);
                if (oldCountyBFkOfCostDistanceList1CostDistance != null) {
                    oldCountyBFkOfCostDistanceList1CostDistance.getCostDistanceList1().remove(costDistanceList1CostDistance);
                    oldCountyBFkOfCostDistanceList1CostDistance = em.merge(oldCountyBFkOfCostDistanceList1CostDistance);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findCounty(county.getId()) != null) {
                throw new PreexistingEntityException("County " + county + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(County county) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            County persistentCounty = em.find(County.class, county.getId());
            List<Municipality> municipalityListOld = persistentCounty.getMunicipalityList();
            List<Municipality> municipalityListNew = county.getMunicipalityList();
            List<Project> projectListOld = persistentCounty.getProjectList();
            List<Project> projectListNew = county.getProjectList();
            List<Worker> workerListOld = persistentCounty.getWorkerList();
            List<Worker> workerListNew = county.getWorkerList();
            List<CostDistance> costDistanceListOld = persistentCounty.getCostDistanceList();
            List<CostDistance> costDistanceListNew = county.getCostDistanceList();
            List<CostDistance> costDistanceList1Old = persistentCounty.getCostDistanceList1();
            List<CostDistance> costDistanceList1New = county.getCostDistanceList1();
            List<String> illegalOrphanMessages = null;
            for (Municipality municipalityListOldMunicipality : municipalityListOld) {
                if (!municipalityListNew.contains(municipalityListOldMunicipality)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Municipality " + municipalityListOldMunicipality + " since its countyFk field is not nullable.");
                }
            }
            for (Project projectListOldProject : projectListOld) {
                if (!projectListNew.contains(projectListOldProject)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Project " + projectListOldProject + " since its provinceFk field is not nullable.");
                }
            }
            for (Worker workerListOldWorker : workerListOld) {
                if (!workerListNew.contains(workerListOldWorker)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Worker " + workerListOldWorker + " since its countyFk field is not nullable.");
                }
            }
            for (CostDistance costDistanceListOldCostDistance : costDistanceListOld) {
                if (!costDistanceListNew.contains(costDistanceListOldCostDistance)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain CostDistance " + costDistanceListOldCostDistance + " since its countyAFk field is not nullable.");
                }
            }
            for (CostDistance costDistanceList1OldCostDistance : costDistanceList1Old) {
                if (!costDistanceList1New.contains(costDistanceList1OldCostDistance)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain CostDistance " + costDistanceList1OldCostDistance + " since its countyBFk field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Municipality> attachedMunicipalityListNew = new ArrayList<Municipality>();
            for (Municipality municipalityListNewMunicipalityToAttach : municipalityListNew) {
                municipalityListNewMunicipalityToAttach = em.getReference(municipalityListNewMunicipalityToAttach.getClass(), municipalityListNewMunicipalityToAttach.getId());
                attachedMunicipalityListNew.add(municipalityListNewMunicipalityToAttach);
            }
            municipalityListNew = attachedMunicipalityListNew;
            county.setMunicipalityList(municipalityListNew);
            List<Project> attachedProjectListNew = new ArrayList<Project>();
            for (Project projectListNewProjectToAttach : projectListNew) {
                projectListNewProjectToAttach = em.getReference(projectListNewProjectToAttach.getClass(), projectListNewProjectToAttach.getId());
                attachedProjectListNew.add(projectListNewProjectToAttach);
            }
            projectListNew = attachedProjectListNew;
            county.setProjectList(projectListNew);
            List<Worker> attachedWorkerListNew = new ArrayList<Worker>();
            for (Worker workerListNewWorkerToAttach : workerListNew) {
                workerListNewWorkerToAttach = em.getReference(workerListNewWorkerToAttach.getClass(), workerListNewWorkerToAttach.getId());
                attachedWorkerListNew.add(workerListNewWorkerToAttach);
            }
            workerListNew = attachedWorkerListNew;
            county.setWorkerList(workerListNew);
            List<CostDistance> attachedCostDistanceListNew = new ArrayList<CostDistance>();
            for (CostDistance costDistanceListNewCostDistanceToAttach : costDistanceListNew) {
                costDistanceListNewCostDistanceToAttach = em.getReference(costDistanceListNewCostDistanceToAttach.getClass(), costDistanceListNewCostDistanceToAttach.getId());
                attachedCostDistanceListNew.add(costDistanceListNewCostDistanceToAttach);
            }
            costDistanceListNew = attachedCostDistanceListNew;
            county.setCostDistanceList(costDistanceListNew);
            List<CostDistance> attachedCostDistanceList1New = new ArrayList<CostDistance>();
            for (CostDistance costDistanceList1NewCostDistanceToAttach : costDistanceList1New) {
                costDistanceList1NewCostDistanceToAttach = em.getReference(costDistanceList1NewCostDistanceToAttach.getClass(), costDistanceList1NewCostDistanceToAttach.getId());
                attachedCostDistanceList1New.add(costDistanceList1NewCostDistanceToAttach);
            }
            costDistanceList1New = attachedCostDistanceList1New;
            county.setCostDistanceList1(costDistanceList1New);
            county = em.merge(county);
            for (Municipality municipalityListNewMunicipality : municipalityListNew) {
                if (!municipalityListOld.contains(municipalityListNewMunicipality)) {
                    County oldCountyFkOfMunicipalityListNewMunicipality = municipalityListNewMunicipality.getCountyFk();
                    municipalityListNewMunicipality.setCountyFk(county);
                    municipalityListNewMunicipality = em.merge(municipalityListNewMunicipality);
                    if (oldCountyFkOfMunicipalityListNewMunicipality != null && !oldCountyFkOfMunicipalityListNewMunicipality.equals(county)) {
                        oldCountyFkOfMunicipalityListNewMunicipality.getMunicipalityList().remove(municipalityListNewMunicipality);
                        oldCountyFkOfMunicipalityListNewMunicipality = em.merge(oldCountyFkOfMunicipalityListNewMunicipality);
                    }
                }
            }
            for (Project projectListNewProject : projectListNew) {
                if (!projectListOld.contains(projectListNewProject)) {
                    County oldProvinceFkOfProjectListNewProject = projectListNewProject.getProvinceFk();
                    projectListNewProject.setProvinceFk(county);
                    projectListNewProject = em.merge(projectListNewProject);
                    if (oldProvinceFkOfProjectListNewProject != null && !oldProvinceFkOfProjectListNewProject.equals(county)) {
                        oldProvinceFkOfProjectListNewProject.getProjectList().remove(projectListNewProject);
                        oldProvinceFkOfProjectListNewProject = em.merge(oldProvinceFkOfProjectListNewProject);
                    }
                }
            }
            for (Worker workerListNewWorker : workerListNew) {
                if (!workerListOld.contains(workerListNewWorker)) {
                    County oldCountyFkOfWorkerListNewWorker = workerListNewWorker.getCountyFk();
                    workerListNewWorker.setCountyFk(county);
                    workerListNewWorker = em.merge(workerListNewWorker);
                    if (oldCountyFkOfWorkerListNewWorker != null && !oldCountyFkOfWorkerListNewWorker.equals(county)) {
                        oldCountyFkOfWorkerListNewWorker.getWorkerList().remove(workerListNewWorker);
                        oldCountyFkOfWorkerListNewWorker = em.merge(oldCountyFkOfWorkerListNewWorker);
                    }
                }
            }
            for (CostDistance costDistanceListNewCostDistance : costDistanceListNew) {
                if (!costDistanceListOld.contains(costDistanceListNewCostDistance)) {
                    County oldCountyAFkOfCostDistanceListNewCostDistance = costDistanceListNewCostDistance.getCountyAFk();
                    costDistanceListNewCostDistance.setCountyAFk(county);
                    costDistanceListNewCostDistance = em.merge(costDistanceListNewCostDistance);
                    if (oldCountyAFkOfCostDistanceListNewCostDistance != null && !oldCountyAFkOfCostDistanceListNewCostDistance.equals(county)) {
                        oldCountyAFkOfCostDistanceListNewCostDistance.getCostDistanceList().remove(costDistanceListNewCostDistance);
                        oldCountyAFkOfCostDistanceListNewCostDistance = em.merge(oldCountyAFkOfCostDistanceListNewCostDistance);
                    }
                }
            }
            for (CostDistance costDistanceList1NewCostDistance : costDistanceList1New) {
                if (!costDistanceList1Old.contains(costDistanceList1NewCostDistance)) {
                    County oldCountyBFkOfCostDistanceList1NewCostDistance = costDistanceList1NewCostDistance.getCountyBFk();
                    costDistanceList1NewCostDistance.setCountyBFk(county);
                    costDistanceList1NewCostDistance = em.merge(costDistanceList1NewCostDistance);
                    if (oldCountyBFkOfCostDistanceList1NewCostDistance != null && !oldCountyBFkOfCostDistanceList1NewCostDistance.equals(county)) {
                        oldCountyBFkOfCostDistanceList1NewCostDistance.getCostDistanceList1().remove(costDistanceList1NewCostDistance);
                        oldCountyBFkOfCostDistanceList1NewCostDistance = em.merge(oldCountyBFkOfCostDistanceList1NewCostDistance);
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
                Long id = county.getId();
                if (findCounty(id) == null) {
                    throw new NonexistentEntityException("The county with id " + id + " no longer exists.");
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
            County county;
            try {
                county = em.getReference(County.class, id);
                county.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The county with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Municipality> municipalityListOrphanCheck = county.getMunicipalityList();
            for (Municipality municipalityListOrphanCheckMunicipality : municipalityListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This County (" + county + ") cannot be destroyed since the Municipality " + municipalityListOrphanCheckMunicipality + " in its municipalityList field has a non-nullable countyFk field.");
            }
            List<Project> projectListOrphanCheck = county.getProjectList();
            for (Project projectListOrphanCheckProject : projectListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This County (" + county + ") cannot be destroyed since the Project " + projectListOrphanCheckProject + " in its projectList field has a non-nullable provinceFk field.");
            }
            List<Worker> workerListOrphanCheck = county.getWorkerList();
            for (Worker workerListOrphanCheckWorker : workerListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This County (" + county + ") cannot be destroyed since the Worker " + workerListOrphanCheckWorker + " in its workerList field has a non-nullable countyFk field.");
            }
            List<CostDistance> costDistanceListOrphanCheck = county.getCostDistanceList();
            for (CostDistance costDistanceListOrphanCheckCostDistance : costDistanceListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This County (" + county + ") cannot be destroyed since the CostDistance " + costDistanceListOrphanCheckCostDistance + " in its costDistanceList field has a non-nullable countyAFk field.");
            }
            List<CostDistance> costDistanceList1OrphanCheck = county.getCostDistanceList1();
            for (CostDistance costDistanceList1OrphanCheckCostDistance : costDistanceList1OrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This County (" + county + ") cannot be destroyed since the CostDistance " + costDistanceList1OrphanCheckCostDistance + " in its costDistanceList1 field has a non-nullable countyBFk field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(county);
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

    public List<County> findCountyEntities() {
        return findCountyEntities(true, -1, -1);
    }

    public List<County> findCountyEntities(int maxResults, int firstResult) {
        return findCountyEntities(false, maxResults, firstResult);
    }

    private List<County> findCountyEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(County.class));
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

    public County findCounty(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(County.class, id);
        } finally {
            em.close();
        }
    }

    public int getCountyCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<County> rt = cq.from(County.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
