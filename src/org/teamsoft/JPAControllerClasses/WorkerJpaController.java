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
 * @author yoyo
 */
public class WorkerJpaController implements Serializable {

    public WorkerJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }

    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Worker worker) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (worker.getAssignedRoleList() == null) {
            worker.setAssignedRoleList(new ArrayList<AssignedRole>());
        }
        if (worker.getRoleExperienceList() == null) {
            worker.setRoleExperienceList(new ArrayList<RoleExperience>());
        }
        if (worker.getPersonalInterestsList() == null) {
            worker.setPersonalInterestsList(new ArrayList<PersonalInterests>());
        }
        if (worker.getRoleEvaluationList() == null) {
            worker.setRoleEvaluationList(new ArrayList<RoleEvaluation>());
        }
        if (worker.getCompetenceValueList() == null) {
            worker.setCompetenceValueList(new ArrayList<CompetenceValue>());
        }
        if (worker.getWorkerConflictList() == null) {
            worker.setWorkerConflictList(new ArrayList<WorkerConflict>());
        }
        if (worker.getWorkerConflictList1() == null) {
            worker.setWorkerConflictList1(new ArrayList<WorkerConflict>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            County countyFk = worker.getCountyFk();
            if (countyFk != null) {
                countyFk = em.getReference(countyFk.getClass(), countyFk.getId());
                worker.setCountyFk(countyFk);
            }
            PersonGroup groupFk = worker.getGroupFk();
            if (groupFk != null) {
                groupFk = em.getReference(groupFk.getClass(), groupFk.getId());
                worker.setGroupFk(groupFk);
            }
            WorkerTest workerTest = worker.getWorkerTest();
            if (workerTest != null) {
                workerTest = em.getReference(workerTest.getClass(), workerTest.getId());
                worker.setWorkerTest(workerTest);
            }
            List<AssignedRole> attachedAssignedRoleList = new ArrayList<AssignedRole>();
            for (AssignedRole assignedRoleListAssignedRoleToAttach : worker.getAssignedRoleList()) {
                assignedRoleListAssignedRoleToAttach = em.getReference(assignedRoleListAssignedRoleToAttach.getClass(), assignedRoleListAssignedRoleToAttach.getId());
                attachedAssignedRoleList.add(assignedRoleListAssignedRoleToAttach);
            }
            worker.setAssignedRoleList(attachedAssignedRoleList);
            List<RoleExperience> attachedRoleExperienceList = new ArrayList<RoleExperience>();
            for (RoleExperience roleExperienceListRoleExperienceToAttach : worker.getRoleExperienceList()) {
                roleExperienceListRoleExperienceToAttach = em.getReference(roleExperienceListRoleExperienceToAttach.getClass(), roleExperienceListRoleExperienceToAttach.getId());
                attachedRoleExperienceList.add(roleExperienceListRoleExperienceToAttach);
            }
            worker.setRoleExperienceList(attachedRoleExperienceList);
            List<PersonalInterests> attachedPersonalInterestsList = new ArrayList<PersonalInterests>();
            for (PersonalInterests personalInterestsListPersonalInterestsToAttach : worker.getPersonalInterestsList()) {
                personalInterestsListPersonalInterestsToAttach = em.getReference(personalInterestsListPersonalInterestsToAttach.getClass(), personalInterestsListPersonalInterestsToAttach.getId());
                attachedPersonalInterestsList.add(personalInterestsListPersonalInterestsToAttach);
            }
            worker.setPersonalInterestsList(attachedPersonalInterestsList);
            List<RoleEvaluation> attachedRoleEvaluationList = new ArrayList<RoleEvaluation>();
            for (RoleEvaluation roleEvaluationListRoleEvaluationToAttach : worker.getRoleEvaluationList()) {
                roleEvaluationListRoleEvaluationToAttach = em.getReference(roleEvaluationListRoleEvaluationToAttach.getClass(), roleEvaluationListRoleEvaluationToAttach.getId());
                attachedRoleEvaluationList.add(roleEvaluationListRoleEvaluationToAttach);
            }
            worker.setRoleEvaluationList(attachedRoleEvaluationList);
            List<CompetenceValue> attachedCompetenceValueList = new ArrayList<CompetenceValue>();
            for (CompetenceValue competenceValueListCompetenceValueToAttach : worker.getCompetenceValueList()) {
                competenceValueListCompetenceValueToAttach = em.getReference(competenceValueListCompetenceValueToAttach.getClass(), competenceValueListCompetenceValueToAttach.getId());
                attachedCompetenceValueList.add(competenceValueListCompetenceValueToAttach);
            }
            worker.setCompetenceValueList(attachedCompetenceValueList);
            List<WorkerConflict> attachedWorkerConflictList = new ArrayList<WorkerConflict>();
            for (WorkerConflict workerConflictListWorkerConflictToAttach : worker.getWorkerConflictList()) {
                workerConflictListWorkerConflictToAttach = em.getReference(workerConflictListWorkerConflictToAttach.getClass(), workerConflictListWorkerConflictToAttach.getId());
                attachedWorkerConflictList.add(workerConflictListWorkerConflictToAttach);
            }
            worker.setWorkerConflictList(attachedWorkerConflictList);
            List<WorkerConflict> attachedWorkerConflictList1 = new ArrayList<WorkerConflict>();
            for (WorkerConflict workerConflictList1WorkerConflictToAttach : worker.getWorkerConflictList1()) {
                workerConflictList1WorkerConflictToAttach = em.getReference(workerConflictList1WorkerConflictToAttach.getClass(), workerConflictList1WorkerConflictToAttach.getId());
                attachedWorkerConflictList1.add(workerConflictList1WorkerConflictToAttach);
            }
            worker.setWorkerConflictList1(attachedWorkerConflictList1);
            em.persist(worker);
            if (countyFk != null) {
                countyFk.getWorkerList().add(worker);
                countyFk = em.merge(countyFk);
            }
            if (groupFk != null) {
                groupFk.getWorkerList().add(worker);
                groupFk = em.merge(groupFk);
            }
            if (workerTest != null) {
                Worker oldWorkerFkOfWorkerTest = workerTest.getWorkerFk();
                if (oldWorkerFkOfWorkerTest != null) {
                    oldWorkerFkOfWorkerTest.setWorkerTest(null);
                    oldWorkerFkOfWorkerTest = em.merge(oldWorkerFkOfWorkerTest);
                }
                workerTest.setWorkerFk(worker);
                workerTest = em.merge(workerTest);
            }
            for (AssignedRole assignedRoleListAssignedRole : worker.getAssignedRoleList()) {
                Worker oldWorkersFkOfAssignedRoleListAssignedRole = assignedRoleListAssignedRole.getWorkersFk();
                assignedRoleListAssignedRole.setWorkersFk(worker);
                assignedRoleListAssignedRole = em.merge(assignedRoleListAssignedRole);
                if (oldWorkersFkOfAssignedRoleListAssignedRole != null) {
                    oldWorkersFkOfAssignedRoleListAssignedRole.getAssignedRoleList().remove(assignedRoleListAssignedRole);
                    oldWorkersFkOfAssignedRoleListAssignedRole = em.merge(oldWorkersFkOfAssignedRoleListAssignedRole);
                }
            }
            for (RoleExperience roleExperienceListRoleExperience : worker.getRoleExperienceList()) {
                Worker oldWorkerFkOfRoleExperienceListRoleExperience = roleExperienceListRoleExperience.getWorkerFk();
                roleExperienceListRoleExperience.setWorkerFk(worker);
                roleExperienceListRoleExperience = em.merge(roleExperienceListRoleExperience);
                if (oldWorkerFkOfRoleExperienceListRoleExperience != null) {
                    oldWorkerFkOfRoleExperienceListRoleExperience.getRoleExperienceList().remove(roleExperienceListRoleExperience);
                    oldWorkerFkOfRoleExperienceListRoleExperience = em.merge(oldWorkerFkOfRoleExperienceListRoleExperience);
                }
            }
            for (PersonalInterests personalInterestsListPersonalInterests : worker.getPersonalInterestsList()) {
                Worker oldWorkersFkOfPersonalInterestsListPersonalInterests = personalInterestsListPersonalInterests.getWorkersFk();
                personalInterestsListPersonalInterests.setWorkersFk(worker);
                personalInterestsListPersonalInterests = em.merge(personalInterestsListPersonalInterests);
                if (oldWorkersFkOfPersonalInterestsListPersonalInterests != null) {
                    oldWorkersFkOfPersonalInterestsListPersonalInterests.getPersonalInterestsList().remove(personalInterestsListPersonalInterests);
                    oldWorkersFkOfPersonalInterestsListPersonalInterests = em.merge(oldWorkersFkOfPersonalInterestsListPersonalInterests);
                }
            }
            for (RoleEvaluation roleEvaluationListRoleEvaluation : worker.getRoleEvaluationList()) {
                Worker oldWorkerFkOfRoleEvaluationListRoleEvaluation = roleEvaluationListRoleEvaluation.getWorkerFk();
                roleEvaluationListRoleEvaluation.setWorkerFk(worker);
                roleEvaluationListRoleEvaluation = em.merge(roleEvaluationListRoleEvaluation);
                if (oldWorkerFkOfRoleEvaluationListRoleEvaluation != null) {
                    oldWorkerFkOfRoleEvaluationListRoleEvaluation.getRoleEvaluationList().remove(roleEvaluationListRoleEvaluation);
                    oldWorkerFkOfRoleEvaluationListRoleEvaluation = em.merge(oldWorkerFkOfRoleEvaluationListRoleEvaluation);
                }
            }
            for (CompetenceValue competenceValueListCompetenceValue : worker.getCompetenceValueList()) {
                Worker oldWorkersFkOfCompetenceValueListCompetenceValue = competenceValueListCompetenceValue.getWorkersFk();
                competenceValueListCompetenceValue.setWorkersFk(worker);
                competenceValueListCompetenceValue = em.merge(competenceValueListCompetenceValue);
                if (oldWorkersFkOfCompetenceValueListCompetenceValue != null) {
                    oldWorkersFkOfCompetenceValueListCompetenceValue.getCompetenceValueList().remove(competenceValueListCompetenceValue);
                    oldWorkersFkOfCompetenceValueListCompetenceValue = em.merge(oldWorkersFkOfCompetenceValueListCompetenceValue);
                }
            }
            for (WorkerConflict workerConflictListWorkerConflict : worker.getWorkerConflictList()) {
                Worker oldWorkerConflictFkOfWorkerConflictListWorkerConflict = workerConflictListWorkerConflict.getWorkerConflictFk();
                workerConflictListWorkerConflict.setWorkerConflictFk(worker);
                workerConflictListWorkerConflict = em.merge(workerConflictListWorkerConflict);
                if (oldWorkerConflictFkOfWorkerConflictListWorkerConflict != null) {
                    oldWorkerConflictFkOfWorkerConflictListWorkerConflict.getWorkerConflictList().remove(workerConflictListWorkerConflict);
                    oldWorkerConflictFkOfWorkerConflictListWorkerConflict = em.merge(oldWorkerConflictFkOfWorkerConflictListWorkerConflict);
                }
            }
            for (WorkerConflict workerConflictList1WorkerConflict : worker.getWorkerConflictList1()) {
                Worker oldWorkerFkOfWorkerConflictList1WorkerConflict = workerConflictList1WorkerConflict.getWorkerFk();
                workerConflictList1WorkerConflict.setWorkerFk(worker);
                workerConflictList1WorkerConflict = em.merge(workerConflictList1WorkerConflict);
                if (oldWorkerFkOfWorkerConflictList1WorkerConflict != null) {
                    oldWorkerFkOfWorkerConflictList1WorkerConflict.getWorkerConflictList1().remove(workerConflictList1WorkerConflict);
                    oldWorkerFkOfWorkerConflictList1WorkerConflict = em.merge(oldWorkerFkOfWorkerConflictList1WorkerConflict);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findWorker(worker.getId()) != null) {
                throw new PreexistingEntityException("Worker " + worker + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Worker worker) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Worker persistentWorker = em.find(Worker.class, worker.getId());
            County countyFkOld = persistentWorker.getCountyFk();
            County countyFkNew = worker.getCountyFk();
            PersonGroup groupFkOld = persistentWorker.getGroupFk();
            PersonGroup groupFkNew = worker.getGroupFk();
            WorkerTest workerTestOld = persistentWorker.getWorkerTest();
            WorkerTest workerTestNew = worker.getWorkerTest();
            List<AssignedRole> assignedRoleListOld = persistentWorker.getAssignedRoleList();
            List<AssignedRole> assignedRoleListNew = worker.getAssignedRoleList();
            List<RoleExperience> roleExperienceListOld = persistentWorker.getRoleExperienceList();
            List<RoleExperience> roleExperienceListNew = worker.getRoleExperienceList();
            List<PersonalInterests> personalInterestsListOld = persistentWorker.getPersonalInterestsList();
            List<PersonalInterests> personalInterestsListNew = worker.getPersonalInterestsList();
            List<RoleEvaluation> roleEvaluationListOld = persistentWorker.getRoleEvaluationList();
            List<RoleEvaluation> roleEvaluationListNew = worker.getRoleEvaluationList();
            List<CompetenceValue> competenceValueListOld = persistentWorker.getCompetenceValueList();
            List<CompetenceValue> competenceValueListNew = worker.getCompetenceValueList();
            List<WorkerConflict> workerConflictListOld = persistentWorker.getWorkerConflictList();
            List<WorkerConflict> workerConflictListNew = worker.getWorkerConflictList();
            List<WorkerConflict> workerConflictList1Old = persistentWorker.getWorkerConflictList1();
            List<WorkerConflict> workerConflictList1New = worker.getWorkerConflictList1();
            List<String> illegalOrphanMessages = null;
            if (workerTestOld != null && !workerTestOld.equals(workerTestNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain WorkerTest " + workerTestOld + " since its workerFk field is not nullable.");
            }
            for (AssignedRole assignedRoleListOldAssignedRole : assignedRoleListOld) {
                if (!assignedRoleListNew.contains(assignedRoleListOldAssignedRole)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AssignedRole " + assignedRoleListOldAssignedRole + " since its workersFk field is not nullable.");
                }
            }
            for (RoleExperience roleExperienceListOldRoleExperience : roleExperienceListOld) {
                if (!roleExperienceListNew.contains(roleExperienceListOldRoleExperience)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RoleExperience " + roleExperienceListOldRoleExperience + " since its workerFk field is not nullable.");
                }
            }
            for (PersonalInterests personalInterestsListOldPersonalInterests : personalInterestsListOld) {
                if (!personalInterestsListNew.contains(personalInterestsListOldPersonalInterests)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PersonalInterests " + personalInterestsListOldPersonalInterests + " since its workersFk field is not nullable.");
                }
            }
            for (CompetenceValue competenceValueListOldCompetenceValue : competenceValueListOld) {
                if (!competenceValueListNew.contains(competenceValueListOldCompetenceValue)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain CompetenceValue " + competenceValueListOldCompetenceValue + " since its workersFk field is not nullable.");
                }
            }
            for (WorkerConflict workerConflictListOldWorkerConflict : workerConflictListOld) {
                if (!workerConflictListNew.contains(workerConflictListOldWorkerConflict)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain WorkerConflict " + workerConflictListOldWorkerConflict + " since its workerConflictFk field is not nullable.");
                }
            }
            for (WorkerConflict workerConflictList1OldWorkerConflict : workerConflictList1Old) {
                if (!workerConflictList1New.contains(workerConflictList1OldWorkerConflict)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain WorkerConflict " + workerConflictList1OldWorkerConflict + " since its workerFk field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (countyFkNew != null) {
                countyFkNew = em.getReference(countyFkNew.getClass(), countyFkNew.getId());
                worker.setCountyFk(countyFkNew);
            }
            if (groupFkNew != null) {
                groupFkNew = em.getReference(groupFkNew.getClass(), groupFkNew.getId());
                worker.setGroupFk(groupFkNew);
            }
            if (workerTestNew != null) {
                workerTestNew = em.getReference(workerTestNew.getClass(), workerTestNew.getId());
                worker.setWorkerTest(workerTestNew);
            }
            List<AssignedRole> attachedAssignedRoleListNew = new ArrayList<AssignedRole>();
            for (AssignedRole assignedRoleListNewAssignedRoleToAttach : assignedRoleListNew) {
                assignedRoleListNewAssignedRoleToAttach = em.getReference(assignedRoleListNewAssignedRoleToAttach.getClass(), assignedRoleListNewAssignedRoleToAttach.getId());
                attachedAssignedRoleListNew.add(assignedRoleListNewAssignedRoleToAttach);
            }
            assignedRoleListNew = attachedAssignedRoleListNew;
            worker.setAssignedRoleList(assignedRoleListNew);
            List<RoleExperience> attachedRoleExperienceListNew = new ArrayList<RoleExperience>();
            for (RoleExperience roleExperienceListNewRoleExperienceToAttach : roleExperienceListNew) {
                roleExperienceListNewRoleExperienceToAttach = em.getReference(roleExperienceListNewRoleExperienceToAttach.getClass(), roleExperienceListNewRoleExperienceToAttach.getId());
                attachedRoleExperienceListNew.add(roleExperienceListNewRoleExperienceToAttach);
            }
            roleExperienceListNew = attachedRoleExperienceListNew;
            worker.setRoleExperienceList(roleExperienceListNew);
            List<PersonalInterests> attachedPersonalInterestsListNew = new ArrayList<PersonalInterests>();
            for (PersonalInterests personalInterestsListNewPersonalInterestsToAttach : personalInterestsListNew) {
                personalInterestsListNewPersonalInterestsToAttach = em.getReference(personalInterestsListNewPersonalInterestsToAttach.getClass(), personalInterestsListNewPersonalInterestsToAttach.getId());
                attachedPersonalInterestsListNew.add(personalInterestsListNewPersonalInterestsToAttach);
            }
            personalInterestsListNew = attachedPersonalInterestsListNew;
            worker.setPersonalInterestsList(personalInterestsListNew);
            List<RoleEvaluation> attachedRoleEvaluationListNew = new ArrayList<RoleEvaluation>();
            for (RoleEvaluation roleEvaluationListNewRoleEvaluationToAttach : roleEvaluationListNew) {
                roleEvaluationListNewRoleEvaluationToAttach = em.getReference(roleEvaluationListNewRoleEvaluationToAttach.getClass(), roleEvaluationListNewRoleEvaluationToAttach.getId());
                attachedRoleEvaluationListNew.add(roleEvaluationListNewRoleEvaluationToAttach);
            }
            roleEvaluationListNew = attachedRoleEvaluationListNew;
            worker.setRoleEvaluationList(roleEvaluationListNew);
            List<CompetenceValue> attachedCompetenceValueListNew = new ArrayList<CompetenceValue>();
            for (CompetenceValue competenceValueListNewCompetenceValueToAttach : competenceValueListNew) {
                competenceValueListNewCompetenceValueToAttach = em.getReference(competenceValueListNewCompetenceValueToAttach.getClass(), competenceValueListNewCompetenceValueToAttach.getId());
                attachedCompetenceValueListNew.add(competenceValueListNewCompetenceValueToAttach);
            }
            competenceValueListNew = attachedCompetenceValueListNew;
            worker.setCompetenceValueList(competenceValueListNew);
            List<WorkerConflict> attachedWorkerConflictListNew = new ArrayList<WorkerConflict>();
            for (WorkerConflict workerConflictListNewWorkerConflictToAttach : workerConflictListNew) {
                workerConflictListNewWorkerConflictToAttach = em.getReference(workerConflictListNewWorkerConflictToAttach.getClass(), workerConflictListNewWorkerConflictToAttach.getId());
                attachedWorkerConflictListNew.add(workerConflictListNewWorkerConflictToAttach);
            }
            workerConflictListNew = attachedWorkerConflictListNew;
            worker.setWorkerConflictList(workerConflictListNew);
            List<WorkerConflict> attachedWorkerConflictList1New = new ArrayList<WorkerConflict>();
            for (WorkerConflict workerConflictList1NewWorkerConflictToAttach : workerConflictList1New) {
                workerConflictList1NewWorkerConflictToAttach = em.getReference(workerConflictList1NewWorkerConflictToAttach.getClass(), workerConflictList1NewWorkerConflictToAttach.getId());
                attachedWorkerConflictList1New.add(workerConflictList1NewWorkerConflictToAttach);
            }
            workerConflictList1New = attachedWorkerConflictList1New;
            worker.setWorkerConflictList1(workerConflictList1New);
            worker = em.merge(worker);
            if (countyFkOld != null && !countyFkOld.equals(countyFkNew)) {
                countyFkOld.getWorkerList().remove(worker);
                countyFkOld = em.merge(countyFkOld);
            }
            if (countyFkNew != null && !countyFkNew.equals(countyFkOld)) {
                countyFkNew.getWorkerList().add(worker);
                countyFkNew = em.merge(countyFkNew);
            }
            if (groupFkOld != null && !groupFkOld.equals(groupFkNew)) {
                groupFkOld.getWorkerList().remove(worker);
                groupFkOld = em.merge(groupFkOld);
            }
            if (groupFkNew != null && !groupFkNew.equals(groupFkOld)) {
                groupFkNew.getWorkerList().add(worker);
                groupFkNew = em.merge(groupFkNew);
            }
            if (workerTestNew != null && !workerTestNew.equals(workerTestOld)) {
                Worker oldWorkerFkOfWorkerTest = workerTestNew.getWorkerFk();
                if (oldWorkerFkOfWorkerTest != null) {
                    oldWorkerFkOfWorkerTest.setWorkerTest(null);
                    oldWorkerFkOfWorkerTest = em.merge(oldWorkerFkOfWorkerTest);
                }
                workerTestNew.setWorkerFk(worker);
                workerTestNew = em.merge(workerTestNew);
            }
            for (AssignedRole assignedRoleListNewAssignedRole : assignedRoleListNew) {
                if (!assignedRoleListOld.contains(assignedRoleListNewAssignedRole)) {
                    Worker oldWorkersFkOfAssignedRoleListNewAssignedRole = assignedRoleListNewAssignedRole.getWorkersFk();
                    assignedRoleListNewAssignedRole.setWorkersFk(worker);
                    assignedRoleListNewAssignedRole = em.merge(assignedRoleListNewAssignedRole);
                    if (oldWorkersFkOfAssignedRoleListNewAssignedRole != null && !oldWorkersFkOfAssignedRoleListNewAssignedRole.equals(worker)) {
                        oldWorkersFkOfAssignedRoleListNewAssignedRole.getAssignedRoleList().remove(assignedRoleListNewAssignedRole);
                        oldWorkersFkOfAssignedRoleListNewAssignedRole = em.merge(oldWorkersFkOfAssignedRoleListNewAssignedRole);
                    }
                }
            }
            for (RoleExperience roleExperienceListNewRoleExperience : roleExperienceListNew) {
                if (!roleExperienceListOld.contains(roleExperienceListNewRoleExperience)) {
                    Worker oldWorkerFkOfRoleExperienceListNewRoleExperience = roleExperienceListNewRoleExperience.getWorkerFk();
                    roleExperienceListNewRoleExperience.setWorkerFk(worker);
                    roleExperienceListNewRoleExperience = em.merge(roleExperienceListNewRoleExperience);
                    if (oldWorkerFkOfRoleExperienceListNewRoleExperience != null && !oldWorkerFkOfRoleExperienceListNewRoleExperience.equals(worker)) {
                        oldWorkerFkOfRoleExperienceListNewRoleExperience.getRoleExperienceList().remove(roleExperienceListNewRoleExperience);
                        oldWorkerFkOfRoleExperienceListNewRoleExperience = em.merge(oldWorkerFkOfRoleExperienceListNewRoleExperience);
                    }
                }
            }
            for (PersonalInterests personalInterestsListNewPersonalInterests : personalInterestsListNew) {
                if (!personalInterestsListOld.contains(personalInterestsListNewPersonalInterests)) {
                    Worker oldWorkersFkOfPersonalInterestsListNewPersonalInterests = personalInterestsListNewPersonalInterests.getWorkersFk();
                    personalInterestsListNewPersonalInterests.setWorkersFk(worker);
                    personalInterestsListNewPersonalInterests = em.merge(personalInterestsListNewPersonalInterests);
                    if (oldWorkersFkOfPersonalInterestsListNewPersonalInterests != null && !oldWorkersFkOfPersonalInterestsListNewPersonalInterests.equals(worker)) {
                        oldWorkersFkOfPersonalInterestsListNewPersonalInterests.getPersonalInterestsList().remove(personalInterestsListNewPersonalInterests);
                        oldWorkersFkOfPersonalInterestsListNewPersonalInterests = em.merge(oldWorkersFkOfPersonalInterestsListNewPersonalInterests);
                    }
                }
            }
            for (RoleEvaluation roleEvaluationListOldRoleEvaluation : roleEvaluationListOld) {
                if (!roleEvaluationListNew.contains(roleEvaluationListOldRoleEvaluation)) {
                    roleEvaluationListOldRoleEvaluation.setWorkerFk(null);
                    roleEvaluationListOldRoleEvaluation = em.merge(roleEvaluationListOldRoleEvaluation);
                }
            }
            for (RoleEvaluation roleEvaluationListNewRoleEvaluation : roleEvaluationListNew) {
                if (!roleEvaluationListOld.contains(roleEvaluationListNewRoleEvaluation)) {
                    Worker oldWorkerFkOfRoleEvaluationListNewRoleEvaluation = roleEvaluationListNewRoleEvaluation.getWorkerFk();
                    roleEvaluationListNewRoleEvaluation.setWorkerFk(worker);
                    roleEvaluationListNewRoleEvaluation = em.merge(roleEvaluationListNewRoleEvaluation);
                    if (oldWorkerFkOfRoleEvaluationListNewRoleEvaluation != null && !oldWorkerFkOfRoleEvaluationListNewRoleEvaluation.equals(worker)) {
                        oldWorkerFkOfRoleEvaluationListNewRoleEvaluation.getRoleEvaluationList().remove(roleEvaluationListNewRoleEvaluation);
                        oldWorkerFkOfRoleEvaluationListNewRoleEvaluation = em.merge(oldWorkerFkOfRoleEvaluationListNewRoleEvaluation);
                    }
                }
            }
            for (CompetenceValue competenceValueListNewCompetenceValue : competenceValueListNew) {
                if (!competenceValueListOld.contains(competenceValueListNewCompetenceValue)) {
                    Worker oldWorkersFkOfCompetenceValueListNewCompetenceValue = competenceValueListNewCompetenceValue.getWorkersFk();
                    competenceValueListNewCompetenceValue.setWorkersFk(worker);
                    competenceValueListNewCompetenceValue = em.merge(competenceValueListNewCompetenceValue);
                    if (oldWorkersFkOfCompetenceValueListNewCompetenceValue != null && !oldWorkersFkOfCompetenceValueListNewCompetenceValue.equals(worker)) {
                        oldWorkersFkOfCompetenceValueListNewCompetenceValue.getCompetenceValueList().remove(competenceValueListNewCompetenceValue);
                        oldWorkersFkOfCompetenceValueListNewCompetenceValue = em.merge(oldWorkersFkOfCompetenceValueListNewCompetenceValue);
                    }
                }
            }
            for (WorkerConflict workerConflictListNewWorkerConflict : workerConflictListNew) {
                if (!workerConflictListOld.contains(workerConflictListNewWorkerConflict)) {
                    Worker oldWorkerConflictFkOfWorkerConflictListNewWorkerConflict = workerConflictListNewWorkerConflict.getWorkerConflictFk();
                    workerConflictListNewWorkerConflict.setWorkerConflictFk(worker);
                    workerConflictListNewWorkerConflict = em.merge(workerConflictListNewWorkerConflict);
                    if (oldWorkerConflictFkOfWorkerConflictListNewWorkerConflict != null && !oldWorkerConflictFkOfWorkerConflictListNewWorkerConflict.equals(worker)) {
                        oldWorkerConflictFkOfWorkerConflictListNewWorkerConflict.getWorkerConflictList().remove(workerConflictListNewWorkerConflict);
                        oldWorkerConflictFkOfWorkerConflictListNewWorkerConflict = em.merge(oldWorkerConflictFkOfWorkerConflictListNewWorkerConflict);
                    }
                }
            }
            for (WorkerConflict workerConflictList1NewWorkerConflict : workerConflictList1New) {
                if (!workerConflictList1Old.contains(workerConflictList1NewWorkerConflict)) {
                    Worker oldWorkerFkOfWorkerConflictList1NewWorkerConflict = workerConflictList1NewWorkerConflict.getWorkerFk();
                    workerConflictList1NewWorkerConflict.setWorkerFk(worker);
                    workerConflictList1NewWorkerConflict = em.merge(workerConflictList1NewWorkerConflict);
                    if (oldWorkerFkOfWorkerConflictList1NewWorkerConflict != null && !oldWorkerFkOfWorkerConflictList1NewWorkerConflict.equals(worker)) {
                        oldWorkerFkOfWorkerConflictList1NewWorkerConflict.getWorkerConflictList1().remove(workerConflictList1NewWorkerConflict);
                        oldWorkerFkOfWorkerConflictList1NewWorkerConflict = em.merge(oldWorkerFkOfWorkerConflictList1NewWorkerConflict);
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
                Long id = worker.getId();
                if (findWorker(id) == null) {
                    throw new NonexistentEntityException("The worker with id " + id + " no longer exists.");
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
            Worker worker;
            try {
                worker = em.getReference(Worker.class, id);
                worker.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The worker with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            WorkerTest workerTestOrphanCheck = worker.getWorkerTest();
            if (workerTestOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Worker (" + worker + ") cannot be destroyed since the WorkerTest " + workerTestOrphanCheck + " in its workerTest field has a non-nullable workerFk field.");
            }
            List<AssignedRole> assignedRoleListOrphanCheck = worker.getAssignedRoleList();
            for (AssignedRole assignedRoleListOrphanCheckAssignedRole : assignedRoleListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Worker (" + worker + ") cannot be destroyed since the AssignedRole " + assignedRoleListOrphanCheckAssignedRole + " in its assignedRoleList field has a non-nullable workersFk field.");
            }
            List<RoleExperience> roleExperienceListOrphanCheck = worker.getRoleExperienceList();
            for (RoleExperience roleExperienceListOrphanCheckRoleExperience : roleExperienceListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Worker (" + worker + ") cannot be destroyed since the RoleExperience " + roleExperienceListOrphanCheckRoleExperience + " in its roleExperienceList field has a non-nullable workerFk field.");
            }
            List<PersonalInterests> personalInterestsListOrphanCheck = worker.getPersonalInterestsList();
            for (PersonalInterests personalInterestsListOrphanCheckPersonalInterests : personalInterestsListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Worker (" + worker + ") cannot be destroyed since the PersonalInterests " + personalInterestsListOrphanCheckPersonalInterests + " in its personalInterestsList field has a non-nullable workersFk field.");
            }
            List<CompetenceValue> competenceValueListOrphanCheck = worker.getCompetenceValueList();
            for (CompetenceValue competenceValueListOrphanCheckCompetenceValue : competenceValueListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Worker (" + worker + ") cannot be destroyed since the CompetenceValue " + competenceValueListOrphanCheckCompetenceValue + " in its competenceValueList field has a non-nullable workersFk field.");
            }
            List<WorkerConflict> workerConflictListOrphanCheck = worker.getWorkerConflictList();
            for (WorkerConflict workerConflictListOrphanCheckWorkerConflict : workerConflictListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Worker (" + worker + ") cannot be destroyed since the WorkerConflict " + workerConflictListOrphanCheckWorkerConflict + " in its workerConflictList field has a non-nullable workerConflictFk field.");
            }
            List<WorkerConflict> workerConflictList1OrphanCheck = worker.getWorkerConflictList1();
            for (WorkerConflict workerConflictList1OrphanCheckWorkerConflict : workerConflictList1OrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Worker (" + worker + ") cannot be destroyed since the WorkerConflict " + workerConflictList1OrphanCheckWorkerConflict + " in its workerConflictList1 field has a non-nullable workerFk field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            County countyFk = worker.getCountyFk();
            if (countyFk != null) {
                countyFk.getWorkerList().remove(worker);
                countyFk = em.merge(countyFk);
            }
            PersonGroup groupFk = worker.getGroupFk();
            if (groupFk != null) {
                groupFk.getWorkerList().remove(worker);
                groupFk = em.merge(groupFk);
            }
            List<RoleEvaluation> roleEvaluationList = worker.getRoleEvaluationList();
            for (RoleEvaluation roleEvaluationListRoleEvaluation : roleEvaluationList) {
                roleEvaluationListRoleEvaluation.setWorkerFk(null);
                roleEvaluationListRoleEvaluation = em.merge(roleEvaluationListRoleEvaluation);
            }
            em.remove(worker);
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

    public List<Worker> findWorkerEntities() {
        return findWorkerEntities(true, -1, -1);
    }

    public List<Worker> findWorkerEntities(int maxResults, int firstResult) {
        return findWorkerEntities(false, maxResults, firstResult);
    }

    private List<Worker> findWorkerEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Worker.class));
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

    public Worker findWorker(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Worker.class, id);
        } finally {
            em.close();
        }
    }

    public int getWorkerCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Worker> rt = cq.from(Worker.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
