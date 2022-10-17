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
public class RoleJpaController implements Serializable {

    public RoleJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Role role) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (role.getRoleList() == null) {
            role.setRoleList(new ArrayList<Role>());
        }
        if (role.getRoleList1() == null) {
            role.setRoleList1(new ArrayList<Role>());
        }
        if (role.getAssignedRoleList() == null) {
            role.setAssignedRoleList(new ArrayList<AssignedRole>());
        }
        if (role.getRoleCompetitionList() == null) {
            role.setRoleCompetitionList(new ArrayList<RoleCompetition>());
        }
        if (role.getRoleExperienceList() == null) {
            role.setRoleExperienceList(new ArrayList<RoleExperience>());
        }
        if (role.getProjectRoleIncompList() == null) {
            role.setProjectRoleIncompList(new ArrayList<ProjectRoleIncomp>());
        }
        if (role.getProjectRoleIncompList1() == null) {
            role.setProjectRoleIncompList1(new ArrayList<ProjectRoleIncomp>());
        }
        if (role.getPersonalInterestsList() == null) {
            role.setPersonalInterestsList(new ArrayList<PersonalInterests>());
        }
        if (role.getRoleEvaluationList() == null) {
            role.setRoleEvaluationList(new ArrayList<RoleEvaluation>());
        }
        if (role.getProjectRolesList() == null) {
            role.setProjectRolesList(new ArrayList<ProjectRoles>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Role> attachedRoleList = new ArrayList<Role>();
            for (Role roleListRoleToAttach : role.getRoleList()) {
                roleListRoleToAttach = em.getReference(roleListRoleToAttach.getClass(), roleListRoleToAttach.getId());
                attachedRoleList.add(roleListRoleToAttach);
            }
            role.setRoleList(attachedRoleList);
            List<Role> attachedRoleList1 = new ArrayList<Role>();
            for (Role roleList1RoleToAttach : role.getRoleList1()) {
                roleList1RoleToAttach = em.getReference(roleList1RoleToAttach.getClass(), roleList1RoleToAttach.getId());
                attachedRoleList1.add(roleList1RoleToAttach);
            }
            role.setRoleList1(attachedRoleList1);
            List<AssignedRole> attachedAssignedRoleList = new ArrayList<AssignedRole>();
            for (AssignedRole assignedRoleListAssignedRoleToAttach : role.getAssignedRoleList()) {
                assignedRoleListAssignedRoleToAttach = em.getReference(assignedRoleListAssignedRoleToAttach.getClass(), assignedRoleListAssignedRoleToAttach.getId());
                attachedAssignedRoleList.add(assignedRoleListAssignedRoleToAttach);
            }
            role.setAssignedRoleList(attachedAssignedRoleList);
            List<RoleCompetition> attachedRoleCompetitionList = new ArrayList<RoleCompetition>();
            for (RoleCompetition roleCompetitionListRoleCompetitionToAttach : role.getRoleCompetitionList()) {
                roleCompetitionListRoleCompetitionToAttach = em.getReference(roleCompetitionListRoleCompetitionToAttach.getClass(), roleCompetitionListRoleCompetitionToAttach.getId());
                attachedRoleCompetitionList.add(roleCompetitionListRoleCompetitionToAttach);
            }
            role.setRoleCompetitionList(attachedRoleCompetitionList);
            List<RoleExperience> attachedRoleExperienceList = new ArrayList<RoleExperience>();
            for (RoleExperience roleExperienceListRoleExperienceToAttach : role.getRoleExperienceList()) {
                roleExperienceListRoleExperienceToAttach = em.getReference(roleExperienceListRoleExperienceToAttach.getClass(), roleExperienceListRoleExperienceToAttach.getId());
                attachedRoleExperienceList.add(roleExperienceListRoleExperienceToAttach);
            }
            role.setRoleExperienceList(attachedRoleExperienceList);
            List<ProjectRoleIncomp> attachedProjectRoleIncompList = new ArrayList<ProjectRoleIncomp>();
            for (ProjectRoleIncomp projectRoleIncompListProjectRoleIncompToAttach : role.getProjectRoleIncompList()) {
                projectRoleIncompListProjectRoleIncompToAttach = em.getReference(projectRoleIncompListProjectRoleIncompToAttach.getClass(), projectRoleIncompListProjectRoleIncompToAttach.getId());
                attachedProjectRoleIncompList.add(projectRoleIncompListProjectRoleIncompToAttach);
            }
            role.setProjectRoleIncompList(attachedProjectRoleIncompList);
            List<ProjectRoleIncomp> attachedProjectRoleIncompList1 = new ArrayList<ProjectRoleIncomp>();
            for (ProjectRoleIncomp projectRoleIncompList1ProjectRoleIncompToAttach : role.getProjectRoleIncompList1()) {
                projectRoleIncompList1ProjectRoleIncompToAttach = em.getReference(projectRoleIncompList1ProjectRoleIncompToAttach.getClass(), projectRoleIncompList1ProjectRoleIncompToAttach.getId());
                attachedProjectRoleIncompList1.add(projectRoleIncompList1ProjectRoleIncompToAttach);
            }
            role.setProjectRoleIncompList1(attachedProjectRoleIncompList1);
            List<PersonalInterests> attachedPersonalInterestsList = new ArrayList<PersonalInterests>();
            for (PersonalInterests personalInterestsListPersonalInterestsToAttach : role.getPersonalInterestsList()) {
                personalInterestsListPersonalInterestsToAttach = em.getReference(personalInterestsListPersonalInterestsToAttach.getClass(), personalInterestsListPersonalInterestsToAttach.getId());
                attachedPersonalInterestsList.add(personalInterestsListPersonalInterestsToAttach);
            }
            role.setPersonalInterestsList(attachedPersonalInterestsList);
            List<RoleEvaluation> attachedRoleEvaluationList = new ArrayList<RoleEvaluation>();
            for (RoleEvaluation roleEvaluationListRoleEvaluationToAttach : role.getRoleEvaluationList()) {
                roleEvaluationListRoleEvaluationToAttach = em.getReference(roleEvaluationListRoleEvaluationToAttach.getClass(), roleEvaluationListRoleEvaluationToAttach.getId());
                attachedRoleEvaluationList.add(roleEvaluationListRoleEvaluationToAttach);
            }
            role.setRoleEvaluationList(attachedRoleEvaluationList);
            List<ProjectRoles> attachedProjectRolesList = new ArrayList<ProjectRoles>();
            for (ProjectRoles projectRolesListProjectRolesToAttach : role.getProjectRolesList()) {
                projectRolesListProjectRolesToAttach = em.getReference(projectRolesListProjectRolesToAttach.getClass(), projectRolesListProjectRolesToAttach.getId());
                attachedProjectRolesList.add(projectRolesListProjectRolesToAttach);
            }
            role.setProjectRolesList(attachedProjectRolesList);
            em.persist(role);
            for (Role roleListRole : role.getRoleList()) {
                roleListRole.getRoleList().add(role);
                roleListRole = em.merge(roleListRole);
            }
            for (Role roleList1Role : role.getRoleList1()) {
                roleList1Role.getRoleList().add(role);
                roleList1Role = em.merge(roleList1Role);
            }
            for (AssignedRole assignedRoleListAssignedRole : role.getAssignedRoleList()) {
                Role oldRolesFkOfAssignedRoleListAssignedRole = assignedRoleListAssignedRole.getRolesFk();
                assignedRoleListAssignedRole.setRolesFk(role);
                assignedRoleListAssignedRole = em.merge(assignedRoleListAssignedRole);
                if (oldRolesFkOfAssignedRoleListAssignedRole != null) {
                    oldRolesFkOfAssignedRoleListAssignedRole.getAssignedRoleList().remove(assignedRoleListAssignedRole);
                    oldRolesFkOfAssignedRoleListAssignedRole = em.merge(oldRolesFkOfAssignedRoleListAssignedRole);
                }
            }
            for (RoleCompetition roleCompetitionListRoleCompetition : role.getRoleCompetitionList()) {
                Role oldRolesFkOfRoleCompetitionListRoleCompetition = roleCompetitionListRoleCompetition.getRolesFk();
                roleCompetitionListRoleCompetition.setRolesFk(role);
                roleCompetitionListRoleCompetition = em.merge(roleCompetitionListRoleCompetition);
                if (oldRolesFkOfRoleCompetitionListRoleCompetition != null) {
                    oldRolesFkOfRoleCompetitionListRoleCompetition.getRoleCompetitionList().remove(roleCompetitionListRoleCompetition);
                    oldRolesFkOfRoleCompetitionListRoleCompetition = em.merge(oldRolesFkOfRoleCompetitionListRoleCompetition);
                }
            }
            for (RoleExperience roleExperienceListRoleExperience : role.getRoleExperienceList()) {
                Role oldRoleFkOfRoleExperienceListRoleExperience = roleExperienceListRoleExperience.getRoleFk();
                roleExperienceListRoleExperience.setRoleFk(role);
                roleExperienceListRoleExperience = em.merge(roleExperienceListRoleExperience);
                if (oldRoleFkOfRoleExperienceListRoleExperience != null) {
                    oldRoleFkOfRoleExperienceListRoleExperience.getRoleExperienceList().remove(roleExperienceListRoleExperience);
                    oldRoleFkOfRoleExperienceListRoleExperience = em.merge(oldRoleFkOfRoleExperienceListRoleExperience);
                }
            }
            for (ProjectRoleIncomp projectRoleIncompListProjectRoleIncomp : role.getProjectRoleIncompList()) {
                Role oldRoleAFkOfProjectRoleIncompListProjectRoleIncomp = projectRoleIncompListProjectRoleIncomp.getRoleAFk();
                projectRoleIncompListProjectRoleIncomp.setRoleAFk(role);
                projectRoleIncompListProjectRoleIncomp = em.merge(projectRoleIncompListProjectRoleIncomp);
                if (oldRoleAFkOfProjectRoleIncompListProjectRoleIncomp != null) {
                    oldRoleAFkOfProjectRoleIncompListProjectRoleIncomp.getProjectRoleIncompList().remove(projectRoleIncompListProjectRoleIncomp);
                    oldRoleAFkOfProjectRoleIncompListProjectRoleIncomp = em.merge(oldRoleAFkOfProjectRoleIncompListProjectRoleIncomp);
                }
            }
            for (ProjectRoleIncomp projectRoleIncompList1ProjectRoleIncomp : role.getProjectRoleIncompList1()) {
                Role oldRoleBFkOfProjectRoleIncompList1ProjectRoleIncomp = projectRoleIncompList1ProjectRoleIncomp.getRoleBFk();
                projectRoleIncompList1ProjectRoleIncomp.setRoleBFk(role);
                projectRoleIncompList1ProjectRoleIncomp = em.merge(projectRoleIncompList1ProjectRoleIncomp);
                if (oldRoleBFkOfProjectRoleIncompList1ProjectRoleIncomp != null) {
                    oldRoleBFkOfProjectRoleIncompList1ProjectRoleIncomp.getProjectRoleIncompList1().remove(projectRoleIncompList1ProjectRoleIncomp);
                    oldRoleBFkOfProjectRoleIncompList1ProjectRoleIncomp = em.merge(oldRoleBFkOfProjectRoleIncompList1ProjectRoleIncomp);
                }
            }
            for (PersonalInterests personalInterestsListPersonalInterests : role.getPersonalInterestsList()) {
                Role oldRolesFkOfPersonalInterestsListPersonalInterests = personalInterestsListPersonalInterests.getRolesFk();
                personalInterestsListPersonalInterests.setRolesFk(role);
                personalInterestsListPersonalInterests = em.merge(personalInterestsListPersonalInterests);
                if (oldRolesFkOfPersonalInterestsListPersonalInterests != null) {
                    oldRolesFkOfPersonalInterestsListPersonalInterests.getPersonalInterestsList().remove(personalInterestsListPersonalInterests);
                    oldRolesFkOfPersonalInterestsListPersonalInterests = em.merge(oldRolesFkOfPersonalInterestsListPersonalInterests);
                }
            }
            for (RoleEvaluation roleEvaluationListRoleEvaluation : role.getRoleEvaluationList()) {
                Role oldRoleFkOfRoleEvaluationListRoleEvaluation = roleEvaluationListRoleEvaluation.getRoleFk();
                roleEvaluationListRoleEvaluation.setRoleFk(role);
                roleEvaluationListRoleEvaluation = em.merge(roleEvaluationListRoleEvaluation);
                if (oldRoleFkOfRoleEvaluationListRoleEvaluation != null) {
                    oldRoleFkOfRoleEvaluationListRoleEvaluation.getRoleEvaluationList().remove(roleEvaluationListRoleEvaluation);
                    oldRoleFkOfRoleEvaluationListRoleEvaluation = em.merge(oldRoleFkOfRoleEvaluationListRoleEvaluation);
                }
            }
            for (ProjectRoles projectRolesListProjectRoles : role.getProjectRolesList()) {
                Role oldRoleFkOfProjectRolesListProjectRoles = projectRolesListProjectRoles.getRoleFk();
                projectRolesListProjectRoles.setRoleFk(role);
                projectRolesListProjectRoles = em.merge(projectRolesListProjectRoles);
                if (oldRoleFkOfProjectRolesListProjectRoles != null) {
                    oldRoleFkOfProjectRolesListProjectRoles.getProjectRolesList().remove(projectRolesListProjectRoles);
                    oldRoleFkOfProjectRolesListProjectRoles = em.merge(oldRoleFkOfProjectRolesListProjectRoles);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findRole(role.getId()) != null) {
                throw new PreexistingEntityException("Role " + role + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Role role) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Role persistentRole = em.find(Role.class, role.getId());
            List<Role> roleListOld = persistentRole.getRoleList();
            List<Role> roleListNew = role.getRoleList();
            List<Role> roleList1Old = persistentRole.getRoleList1();
            List<Role> roleList1New = role.getRoleList1();
            List<AssignedRole> assignedRoleListOld = persistentRole.getAssignedRoleList();
            List<AssignedRole> assignedRoleListNew = role.getAssignedRoleList();
            List<RoleCompetition> roleCompetitionListOld = persistentRole.getRoleCompetitionList();
            List<RoleCompetition> roleCompetitionListNew = role.getRoleCompetitionList();
            List<RoleExperience> roleExperienceListOld = persistentRole.getRoleExperienceList();
            List<RoleExperience> roleExperienceListNew = role.getRoleExperienceList();
            List<ProjectRoleIncomp> projectRoleIncompListOld = persistentRole.getProjectRoleIncompList();
            List<ProjectRoleIncomp> projectRoleIncompListNew = role.getProjectRoleIncompList();
            List<ProjectRoleIncomp> projectRoleIncompList1Old = persistentRole.getProjectRoleIncompList1();
            List<ProjectRoleIncomp> projectRoleIncompList1New = role.getProjectRoleIncompList1();
            List<PersonalInterests> personalInterestsListOld = persistentRole.getPersonalInterestsList();
            List<PersonalInterests> personalInterestsListNew = role.getPersonalInterestsList();
            List<RoleEvaluation> roleEvaluationListOld = persistentRole.getRoleEvaluationList();
            List<RoleEvaluation> roleEvaluationListNew = role.getRoleEvaluationList();
            List<ProjectRoles> projectRolesListOld = persistentRole.getProjectRolesList();
            List<ProjectRoles> projectRolesListNew = role.getProjectRolesList();
            List<String> illegalOrphanMessages = null;
            for (AssignedRole assignedRoleListOldAssignedRole : assignedRoleListOld) {
                if (!assignedRoleListNew.contains(assignedRoleListOldAssignedRole)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AssignedRole " + assignedRoleListOldAssignedRole + " since its rolesFk field is not nullable.");
                }
            }
            for (RoleCompetition roleCompetitionListOldRoleCompetition : roleCompetitionListOld) {
                if (!roleCompetitionListNew.contains(roleCompetitionListOldRoleCompetition)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RoleCompetition " + roleCompetitionListOldRoleCompetition + " since its rolesFk field is not nullable.");
                }
            }
            for (RoleExperience roleExperienceListOldRoleExperience : roleExperienceListOld) {
                if (!roleExperienceListNew.contains(roleExperienceListOldRoleExperience)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RoleExperience " + roleExperienceListOldRoleExperience + " since its roleFk field is not nullable.");
                }
            }
            for (ProjectRoleIncomp projectRoleIncompListOldProjectRoleIncomp : projectRoleIncompListOld) {
                if (!projectRoleIncompListNew.contains(projectRoleIncompListOldProjectRoleIncomp)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ProjectRoleIncomp " + projectRoleIncompListOldProjectRoleIncomp + " since its roleAFk field is not nullable.");
                }
            }
            for (ProjectRoleIncomp projectRoleIncompList1OldProjectRoleIncomp : projectRoleIncompList1Old) {
                if (!projectRoleIncompList1New.contains(projectRoleIncompList1OldProjectRoleIncomp)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ProjectRoleIncomp " + projectRoleIncompList1OldProjectRoleIncomp + " since its roleBFk field is not nullable.");
                }
            }
            for (PersonalInterests personalInterestsListOldPersonalInterests : personalInterestsListOld) {
                if (!personalInterestsListNew.contains(personalInterestsListOldPersonalInterests)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PersonalInterests " + personalInterestsListOldPersonalInterests + " since its rolesFk field is not nullable.");
                }
            }
            for (ProjectRoles projectRolesListOldProjectRoles : projectRolesListOld) {
                if (!projectRolesListNew.contains(projectRolesListOldProjectRoles)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ProjectRoles " + projectRolesListOldProjectRoles + " since its roleFk field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Role> attachedRoleListNew = new ArrayList<Role>();
            for (Role roleListNewRoleToAttach : roleListNew) {
                roleListNewRoleToAttach = em.getReference(roleListNewRoleToAttach.getClass(), roleListNewRoleToAttach.getId());
                attachedRoleListNew.add(roleListNewRoleToAttach);
            }
            roleListNew = attachedRoleListNew;
            role.setRoleList(roleListNew);
            List<Role> attachedRoleList1New = new ArrayList<Role>();
            for (Role roleList1NewRoleToAttach : roleList1New) {
                roleList1NewRoleToAttach = em.getReference(roleList1NewRoleToAttach.getClass(), roleList1NewRoleToAttach.getId());
                attachedRoleList1New.add(roleList1NewRoleToAttach);
            }
            roleList1New = attachedRoleList1New;
            role.setRoleList1(roleList1New);
            List<AssignedRole> attachedAssignedRoleListNew = new ArrayList<AssignedRole>();
            for (AssignedRole assignedRoleListNewAssignedRoleToAttach : assignedRoleListNew) {
                assignedRoleListNewAssignedRoleToAttach = em.getReference(assignedRoleListNewAssignedRoleToAttach.getClass(), assignedRoleListNewAssignedRoleToAttach.getId());
                attachedAssignedRoleListNew.add(assignedRoleListNewAssignedRoleToAttach);
            }
            assignedRoleListNew = attachedAssignedRoleListNew;
            role.setAssignedRoleList(assignedRoleListNew);
            List<RoleCompetition> attachedRoleCompetitionListNew = new ArrayList<RoleCompetition>();
            for (RoleCompetition roleCompetitionListNewRoleCompetitionToAttach : roleCompetitionListNew) {
                roleCompetitionListNewRoleCompetitionToAttach = em.getReference(roleCompetitionListNewRoleCompetitionToAttach.getClass(), roleCompetitionListNewRoleCompetitionToAttach.getId());
                attachedRoleCompetitionListNew.add(roleCompetitionListNewRoleCompetitionToAttach);
            }
            roleCompetitionListNew = attachedRoleCompetitionListNew;
            role.setRoleCompetitionList(roleCompetitionListNew);
            List<RoleExperience> attachedRoleExperienceListNew = new ArrayList<RoleExperience>();
            for (RoleExperience roleExperienceListNewRoleExperienceToAttach : roleExperienceListNew) {
                roleExperienceListNewRoleExperienceToAttach = em.getReference(roleExperienceListNewRoleExperienceToAttach.getClass(), roleExperienceListNewRoleExperienceToAttach.getId());
                attachedRoleExperienceListNew.add(roleExperienceListNewRoleExperienceToAttach);
            }
            roleExperienceListNew = attachedRoleExperienceListNew;
            role.setRoleExperienceList(roleExperienceListNew);
            List<ProjectRoleIncomp> attachedProjectRoleIncompListNew = new ArrayList<ProjectRoleIncomp>();
            for (ProjectRoleIncomp projectRoleIncompListNewProjectRoleIncompToAttach : projectRoleIncompListNew) {
                projectRoleIncompListNewProjectRoleIncompToAttach = em.getReference(projectRoleIncompListNewProjectRoleIncompToAttach.getClass(), projectRoleIncompListNewProjectRoleIncompToAttach.getId());
                attachedProjectRoleIncompListNew.add(projectRoleIncompListNewProjectRoleIncompToAttach);
            }
            projectRoleIncompListNew = attachedProjectRoleIncompListNew;
            role.setProjectRoleIncompList(projectRoleIncompListNew);
            List<ProjectRoleIncomp> attachedProjectRoleIncompList1New = new ArrayList<ProjectRoleIncomp>();
            for (ProjectRoleIncomp projectRoleIncompList1NewProjectRoleIncompToAttach : projectRoleIncompList1New) {
                projectRoleIncompList1NewProjectRoleIncompToAttach = em.getReference(projectRoleIncompList1NewProjectRoleIncompToAttach.getClass(), projectRoleIncompList1NewProjectRoleIncompToAttach.getId());
                attachedProjectRoleIncompList1New.add(projectRoleIncompList1NewProjectRoleIncompToAttach);
            }
            projectRoleIncompList1New = attachedProjectRoleIncompList1New;
            role.setProjectRoleIncompList1(projectRoleIncompList1New);
            List<PersonalInterests> attachedPersonalInterestsListNew = new ArrayList<PersonalInterests>();
            for (PersonalInterests personalInterestsListNewPersonalInterestsToAttach : personalInterestsListNew) {
                personalInterestsListNewPersonalInterestsToAttach = em.getReference(personalInterestsListNewPersonalInterestsToAttach.getClass(), personalInterestsListNewPersonalInterestsToAttach.getId());
                attachedPersonalInterestsListNew.add(personalInterestsListNewPersonalInterestsToAttach);
            }
            personalInterestsListNew = attachedPersonalInterestsListNew;
            role.setPersonalInterestsList(personalInterestsListNew);
            List<RoleEvaluation> attachedRoleEvaluationListNew = new ArrayList<RoleEvaluation>();
            for (RoleEvaluation roleEvaluationListNewRoleEvaluationToAttach : roleEvaluationListNew) {
                roleEvaluationListNewRoleEvaluationToAttach = em.getReference(roleEvaluationListNewRoleEvaluationToAttach.getClass(), roleEvaluationListNewRoleEvaluationToAttach.getId());
                attachedRoleEvaluationListNew.add(roleEvaluationListNewRoleEvaluationToAttach);
            }
            roleEvaluationListNew = attachedRoleEvaluationListNew;
            role.setRoleEvaluationList(roleEvaluationListNew);
            List<ProjectRoles> attachedProjectRolesListNew = new ArrayList<ProjectRoles>();
            for (ProjectRoles projectRolesListNewProjectRolesToAttach : projectRolesListNew) {
                projectRolesListNewProjectRolesToAttach = em.getReference(projectRolesListNewProjectRolesToAttach.getClass(), projectRolesListNewProjectRolesToAttach.getId());
                attachedProjectRolesListNew.add(projectRolesListNewProjectRolesToAttach);
            }
            projectRolesListNew = attachedProjectRolesListNew;
            role.setProjectRolesList(projectRolesListNew);
            role = em.merge(role);
            for (Role roleListOldRole : roleListOld) {
                if (!roleListNew.contains(roleListOldRole)) {
                    roleListOldRole.getRoleList().remove(role);
                    roleListOldRole = em.merge(roleListOldRole);
                }
            }
            for (Role roleListNewRole : roleListNew) {
                if (!roleListOld.contains(roleListNewRole)) {
                    roleListNewRole.getRoleList().add(role);
                    roleListNewRole = em.merge(roleListNewRole);
                }
            }
            for (Role roleList1OldRole : roleList1Old) {
                if (!roleList1New.contains(roleList1OldRole)) {
                    roleList1OldRole.getRoleList().remove(role);
                    roleList1OldRole = em.merge(roleList1OldRole);
                }
            }
            for (Role roleList1NewRole : roleList1New) {
                if (!roleList1Old.contains(roleList1NewRole)) {
                    roleList1NewRole.getRoleList().add(role);
                    roleList1NewRole = em.merge(roleList1NewRole);
                }
            }
            for (AssignedRole assignedRoleListNewAssignedRole : assignedRoleListNew) {
                if (!assignedRoleListOld.contains(assignedRoleListNewAssignedRole)) {
                    Role oldRolesFkOfAssignedRoleListNewAssignedRole = assignedRoleListNewAssignedRole.getRolesFk();
                    assignedRoleListNewAssignedRole.setRolesFk(role);
                    assignedRoleListNewAssignedRole = em.merge(assignedRoleListNewAssignedRole);
                    if (oldRolesFkOfAssignedRoleListNewAssignedRole != null && !oldRolesFkOfAssignedRoleListNewAssignedRole.equals(role)) {
                        oldRolesFkOfAssignedRoleListNewAssignedRole.getAssignedRoleList().remove(assignedRoleListNewAssignedRole);
                        oldRolesFkOfAssignedRoleListNewAssignedRole = em.merge(oldRolesFkOfAssignedRoleListNewAssignedRole);
                    }
                }
            }
            for (RoleCompetition roleCompetitionListNewRoleCompetition : roleCompetitionListNew) {
                if (!roleCompetitionListOld.contains(roleCompetitionListNewRoleCompetition)) {
                    Role oldRolesFkOfRoleCompetitionListNewRoleCompetition = roleCompetitionListNewRoleCompetition.getRolesFk();
                    roleCompetitionListNewRoleCompetition.setRolesFk(role);
                    roleCompetitionListNewRoleCompetition = em.merge(roleCompetitionListNewRoleCompetition);
                    if (oldRolesFkOfRoleCompetitionListNewRoleCompetition != null && !oldRolesFkOfRoleCompetitionListNewRoleCompetition.equals(role)) {
                        oldRolesFkOfRoleCompetitionListNewRoleCompetition.getRoleCompetitionList().remove(roleCompetitionListNewRoleCompetition);
                        oldRolesFkOfRoleCompetitionListNewRoleCompetition = em.merge(oldRolesFkOfRoleCompetitionListNewRoleCompetition);
                    }
                }
            }
            for (RoleExperience roleExperienceListNewRoleExperience : roleExperienceListNew) {
                if (!roleExperienceListOld.contains(roleExperienceListNewRoleExperience)) {
                    Role oldRoleFkOfRoleExperienceListNewRoleExperience = roleExperienceListNewRoleExperience.getRoleFk();
                    roleExperienceListNewRoleExperience.setRoleFk(role);
                    roleExperienceListNewRoleExperience = em.merge(roleExperienceListNewRoleExperience);
                    if (oldRoleFkOfRoleExperienceListNewRoleExperience != null && !oldRoleFkOfRoleExperienceListNewRoleExperience.equals(role)) {
                        oldRoleFkOfRoleExperienceListNewRoleExperience.getRoleExperienceList().remove(roleExperienceListNewRoleExperience);
                        oldRoleFkOfRoleExperienceListNewRoleExperience = em.merge(oldRoleFkOfRoleExperienceListNewRoleExperience);
                    }
                }
            }
            for (ProjectRoleIncomp projectRoleIncompListNewProjectRoleIncomp : projectRoleIncompListNew) {
                if (!projectRoleIncompListOld.contains(projectRoleIncompListNewProjectRoleIncomp)) {
                    Role oldRoleAFkOfProjectRoleIncompListNewProjectRoleIncomp = projectRoleIncompListNewProjectRoleIncomp.getRoleAFk();
                    projectRoleIncompListNewProjectRoleIncomp.setRoleAFk(role);
                    projectRoleIncompListNewProjectRoleIncomp = em.merge(projectRoleIncompListNewProjectRoleIncomp);
                    if (oldRoleAFkOfProjectRoleIncompListNewProjectRoleIncomp != null && !oldRoleAFkOfProjectRoleIncompListNewProjectRoleIncomp.equals(role)) {
                        oldRoleAFkOfProjectRoleIncompListNewProjectRoleIncomp.getProjectRoleIncompList().remove(projectRoleIncompListNewProjectRoleIncomp);
                        oldRoleAFkOfProjectRoleIncompListNewProjectRoleIncomp = em.merge(oldRoleAFkOfProjectRoleIncompListNewProjectRoleIncomp);
                    }
                }
            }
            for (ProjectRoleIncomp projectRoleIncompList1NewProjectRoleIncomp : projectRoleIncompList1New) {
                if (!projectRoleIncompList1Old.contains(projectRoleIncompList1NewProjectRoleIncomp)) {
                    Role oldRoleBFkOfProjectRoleIncompList1NewProjectRoleIncomp = projectRoleIncompList1NewProjectRoleIncomp.getRoleBFk();
                    projectRoleIncompList1NewProjectRoleIncomp.setRoleBFk(role);
                    projectRoleIncompList1NewProjectRoleIncomp = em.merge(projectRoleIncompList1NewProjectRoleIncomp);
                    if (oldRoleBFkOfProjectRoleIncompList1NewProjectRoleIncomp != null && !oldRoleBFkOfProjectRoleIncompList1NewProjectRoleIncomp.equals(role)) {
                        oldRoleBFkOfProjectRoleIncompList1NewProjectRoleIncomp.getProjectRoleIncompList1().remove(projectRoleIncompList1NewProjectRoleIncomp);
                        oldRoleBFkOfProjectRoleIncompList1NewProjectRoleIncomp = em.merge(oldRoleBFkOfProjectRoleIncompList1NewProjectRoleIncomp);
                    }
                }
            }
            for (PersonalInterests personalInterestsListNewPersonalInterests : personalInterestsListNew) {
                if (!personalInterestsListOld.contains(personalInterestsListNewPersonalInterests)) {
                    Role oldRolesFkOfPersonalInterestsListNewPersonalInterests = personalInterestsListNewPersonalInterests.getRolesFk();
                    personalInterestsListNewPersonalInterests.setRolesFk(role);
                    personalInterestsListNewPersonalInterests = em.merge(personalInterestsListNewPersonalInterests);
                    if (oldRolesFkOfPersonalInterestsListNewPersonalInterests != null && !oldRolesFkOfPersonalInterestsListNewPersonalInterests.equals(role)) {
                        oldRolesFkOfPersonalInterestsListNewPersonalInterests.getPersonalInterestsList().remove(personalInterestsListNewPersonalInterests);
                        oldRolesFkOfPersonalInterestsListNewPersonalInterests = em.merge(oldRolesFkOfPersonalInterestsListNewPersonalInterests);
                    }
                }
            }
            for (RoleEvaluation roleEvaluationListOldRoleEvaluation : roleEvaluationListOld) {
                if (!roleEvaluationListNew.contains(roleEvaluationListOldRoleEvaluation)) {
                    roleEvaluationListOldRoleEvaluation.setRoleFk(null);
                    roleEvaluationListOldRoleEvaluation = em.merge(roleEvaluationListOldRoleEvaluation);
                }
            }
            for (RoleEvaluation roleEvaluationListNewRoleEvaluation : roleEvaluationListNew) {
                if (!roleEvaluationListOld.contains(roleEvaluationListNewRoleEvaluation)) {
                    Role oldRoleFkOfRoleEvaluationListNewRoleEvaluation = roleEvaluationListNewRoleEvaluation.getRoleFk();
                    roleEvaluationListNewRoleEvaluation.setRoleFk(role);
                    roleEvaluationListNewRoleEvaluation = em.merge(roleEvaluationListNewRoleEvaluation);
                    if (oldRoleFkOfRoleEvaluationListNewRoleEvaluation != null && !oldRoleFkOfRoleEvaluationListNewRoleEvaluation.equals(role)) {
                        oldRoleFkOfRoleEvaluationListNewRoleEvaluation.getRoleEvaluationList().remove(roleEvaluationListNewRoleEvaluation);
                        oldRoleFkOfRoleEvaluationListNewRoleEvaluation = em.merge(oldRoleFkOfRoleEvaluationListNewRoleEvaluation);
                    }
                }
            }
            for (ProjectRoles projectRolesListNewProjectRoles : projectRolesListNew) {
                if (!projectRolesListOld.contains(projectRolesListNewProjectRoles)) {
                    Role oldRoleFkOfProjectRolesListNewProjectRoles = projectRolesListNewProjectRoles.getRoleFk();
                    projectRolesListNewProjectRoles.setRoleFk(role);
                    projectRolesListNewProjectRoles = em.merge(projectRolesListNewProjectRoles);
                    if (oldRoleFkOfProjectRolesListNewProjectRoles != null && !oldRoleFkOfProjectRolesListNewProjectRoles.equals(role)) {
                        oldRoleFkOfProjectRolesListNewProjectRoles.getProjectRolesList().remove(projectRolesListNewProjectRoles);
                        oldRoleFkOfProjectRolesListNewProjectRoles = em.merge(oldRoleFkOfProjectRolesListNewProjectRoles);
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
                Long id = role.getId();
                if (findRole(id) == null) {
                    throw new NonexistentEntityException("The role with id " + id + " no longer exists.");
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
            Role role;
            try {
                role = em.getReference(Role.class, id);
                role.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The role with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<AssignedRole> assignedRoleListOrphanCheck = role.getAssignedRoleList();
            for (AssignedRole assignedRoleListOrphanCheckAssignedRole : assignedRoleListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Role (" + role + ") cannot be destroyed since the AssignedRole " + assignedRoleListOrphanCheckAssignedRole + " in its assignedRoleList field has a non-nullable rolesFk field.");
            }
            List<RoleCompetition> roleCompetitionListOrphanCheck = role.getRoleCompetitionList();
            for (RoleCompetition roleCompetitionListOrphanCheckRoleCompetition : roleCompetitionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Role (" + role + ") cannot be destroyed since the RoleCompetition " + roleCompetitionListOrphanCheckRoleCompetition + " in its roleCompetitionList field has a non-nullable rolesFk field.");
            }
            List<RoleExperience> roleExperienceListOrphanCheck = role.getRoleExperienceList();
            for (RoleExperience roleExperienceListOrphanCheckRoleExperience : roleExperienceListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Role (" + role + ") cannot be destroyed since the RoleExperience " + roleExperienceListOrphanCheckRoleExperience + " in its roleExperienceList field has a non-nullable roleFk field.");
            }
            List<ProjectRoleIncomp> projectRoleIncompListOrphanCheck = role.getProjectRoleIncompList();
            for (ProjectRoleIncomp projectRoleIncompListOrphanCheckProjectRoleIncomp : projectRoleIncompListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Role (" + role + ") cannot be destroyed since the ProjectRoleIncomp " + projectRoleIncompListOrphanCheckProjectRoleIncomp + " in its projectRoleIncompList field has a non-nullable roleAFk field.");
            }
            List<ProjectRoleIncomp> projectRoleIncompList1OrphanCheck = role.getProjectRoleIncompList1();
            for (ProjectRoleIncomp projectRoleIncompList1OrphanCheckProjectRoleIncomp : projectRoleIncompList1OrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Role (" + role + ") cannot be destroyed since the ProjectRoleIncomp " + projectRoleIncompList1OrphanCheckProjectRoleIncomp + " in its projectRoleIncompList1 field has a non-nullable roleBFk field.");
            }
            List<PersonalInterests> personalInterestsListOrphanCheck = role.getPersonalInterestsList();
            for (PersonalInterests personalInterestsListOrphanCheckPersonalInterests : personalInterestsListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Role (" + role + ") cannot be destroyed since the PersonalInterests " + personalInterestsListOrphanCheckPersonalInterests + " in its personalInterestsList field has a non-nullable rolesFk field.");
            }
            List<ProjectRoles> projectRolesListOrphanCheck = role.getProjectRolesList();
            for (ProjectRoles projectRolesListOrphanCheckProjectRoles : projectRolesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Role (" + role + ") cannot be destroyed since the ProjectRoles " + projectRolesListOrphanCheckProjectRoles + " in its projectRolesList field has a non-nullable roleFk field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Role> roleList = role.getRoleList();
            for (Role roleListRole : roleList) {
                roleListRole.getRoleList().remove(role);
                roleListRole = em.merge(roleListRole);
            }
            List<Role> roleList1 = role.getRoleList1();
            for (Role roleList1Role : roleList1) {
                roleList1Role.getRoleList().remove(role);
                roleList1Role = em.merge(roleList1Role);
            }
            List<RoleEvaluation> roleEvaluationList = role.getRoleEvaluationList();
            for (RoleEvaluation roleEvaluationListRoleEvaluation : roleEvaluationList) {
                roleEvaluationListRoleEvaluation.setRoleFk(null);
                roleEvaluationListRoleEvaluation = em.merge(roleEvaluationListRoleEvaluation);
            }
            em.remove(role);
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

    public List<Role> findRoleEntities() {
        return findRoleEntities(true, -1, -1);
    }

    public List<Role> findRoleEntities(int maxResults, int firstResult) {
        return findRoleEntities(false, maxResults, firstResult);
    }

    private List<Role> findRoleEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Role.class));
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

    public Role findRole(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Role.class, id);
        } finally {
            em.close();
        }
    }

    public int getRoleCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Role> rt = cq.from(Role.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
