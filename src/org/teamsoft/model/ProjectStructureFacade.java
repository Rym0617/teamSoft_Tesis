/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.model;

import org.teamsoft.entity.Cycle;
import org.teamsoft.entity.ProjectRoles;
import org.teamsoft.entity.ProjectStructure;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author G1lb3rt
 */
@Stateless
public class ProjectStructureFacade extends AbstractFacade<ProjectStructure> implements Serializable {

    @PersistenceContext(unitName = "tesisPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public void createMultipleProjects(ProjectStructure projectStructure) {
        // getEntityManager().merge(entity);
        if (projectStructure.getCycleList() == null) {
            projectStructure.setCycleList(new ArrayList<Cycle>());
        }
        if (projectStructure.getProjectRolesList() == null) {
            projectStructure.setProjectRolesList(new ArrayList<ProjectRoles>());
        }
        List<Cycle> attachedCycleList = new ArrayList<Cycle>();
        for (Cycle cycleListCycleToAttach : projectStructure.getCycleList()) {
            attachedCycleList.add(cycleListCycleToAttach);
        }
        projectStructure.setCycleList(attachedCycleList);
        List<ProjectRoles> attachedProjectRolesList = new ArrayList<ProjectRoles>();
        for (ProjectRoles projectRolesListProjectRolesToAttach : projectStructure.getProjectRolesList()) {
            attachedProjectRolesList.add(projectRolesListProjectRolesToAttach);
        }
        projectStructure.setProjectRolesList(attachedProjectRolesList);
        em.persist(projectStructure);
        for (Cycle cycleListCycle : projectStructure.getCycleList()) {
            ProjectStructure oldStructureFkOfCycleListCycle = cycleListCycle.getStructureFk();
            cycleListCycle.setStructureFk(projectStructure);
            cycleListCycle = em.merge(cycleListCycle);
            if (oldStructureFkOfCycleListCycle != null) {
                oldStructureFkOfCycleListCycle.getCycleList().remove(cycleListCycle);
                oldStructureFkOfCycleListCycle = em.merge(oldStructureFkOfCycleListCycle);
            }
        }
        for (ProjectRoles projectRolesListProjectRoles : projectStructure.getProjectRolesList()) {
            ProjectStructure oldProjectStructureFkOfProjectRolesListProjectRoles = projectRolesListProjectRoles.getProjectStructureFk();
            projectRolesListProjectRoles.setProjectStructureFk(projectStructure);
            projectRolesListProjectRoles = em.merge(projectRolesListProjectRoles);
            if (oldProjectStructureFkOfProjectRolesListProjectRoles != null) {
                oldProjectStructureFkOfProjectRolesListProjectRoles.getProjectRolesList().remove(projectRolesListProjectRoles);
                oldProjectStructureFkOfProjectRolesListProjectRoles = em.merge(oldProjectStructureFkOfProjectRolesListProjectRoles);
            }
        }
    }

    /**
     * Para saber si existe una estructura dado el nombre
     *
     * @param name
     * @return
     */
    public boolean nameExist(String name) {

        List<ProjectStructure> psList = findAll();
        int i = 0;
        boolean find = false;
        while (i < psList.size() && !find) {
            if (name.equalsIgnoreCase(psList.get(i).getName())) {
                find = true;
            } else {
                i++;
            }
        }
        return find;
    }

    /**
     * Para buscar una estructura dado el nombre
     *
     * @param name
     * @return
     */
    public ProjectStructure findByName(String name) {
        ProjectStructure structure = new ProjectStructure();
        List<ProjectStructure> psList = findAll();
        int i = 0;
        boolean find = false;
        while (i < psList.size() && !find) {
            if (name.equalsIgnoreCase(psList.get(i).getName())) {
                find = true;
                structure = psList.get(i);
            } else {
                i++;
            }
        }
        return structure;
    }

    public ProjectStructureFacade() {
        super(ProjectStructure.class);
    }

}
