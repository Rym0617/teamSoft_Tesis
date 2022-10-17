/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.POJOS;

import org.teamsoft.entity.Project;

import java.util.List;

/**
 * Para almacenar la propuesta de jefe de proyecto
 *
 * @author G1lb3rt
 */
public class ProjectBossProposal {

    private Project project;
    private List<BossProposal> bossProposalList;

    public ProjectBossProposal(Project project, List<BossProposal> boss) {
        this.project = project;
        this.bossProposalList = boss;
    }

    public ProjectBossProposal() {
        super();
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<BossProposal> getBossProposalList() {
        return bossProposalList;
    }

    public void setBossProposalList(List<BossProposal> bossProposalList) {
        this.bossProposalList = bossProposalList;
    }

}
