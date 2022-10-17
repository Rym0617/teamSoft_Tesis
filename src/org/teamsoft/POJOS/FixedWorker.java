/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.POJOS;

import org.teamsoft.entity.Project;
import org.teamsoft.entity.Role;
import org.teamsoft.entity.Worker;

/**
 *
 * @author G1lb3rt
 */
public class FixedWorker {

    Project project;
    Role role;
    Worker boss;

    public FixedWorker() {
    }

    public FixedWorker(Project project, Role role, Worker boss) {
        this.project = project;
        this.role = role;
        this.boss = boss;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Worker getBoss() {
        return boss;
    }

    public void setBoss(Worker boss) {
        this.boss = boss;
    }

}
