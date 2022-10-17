/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 *
 * @author yoyo
 */
@Entity
@Table(name = "project_tech_competence")
@NamedQueries({
    @NamedQuery(name = "ProjectTechCompetence.findAll", query = "SELECT p FROM ProjectTechCompetence p")
    , @NamedQuery(name = "ProjectTechCompetence.findById", query = "SELECT p FROM ProjectTechCompetence p WHERE p.id = :id")})
public class ProjectTechCompetence implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CUST_SEQ")
    @SequenceGenerator(sequenceName = "hibernate_sequence", allocationSize = 1, name = "CUST_SEQ")
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "competence_fk", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Competence competenceFk;
    @JoinColumn(name = "competence_importance_fk", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private CompetenceImportance competenceImportanceFk;
    @JoinColumn(name = "level_fk", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Levels levelFk;
    @JoinColumn(name = "project_roles", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ProjectRoles projectRoles;

    public ProjectTechCompetence() {
    }

    public ProjectTechCompetence(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Competence getCompetenceFk() {
        return competenceFk;
    }

    public void setCompetenceFk(Competence competenceFk) {
        this.competenceFk = competenceFk;
    }

    public CompetenceImportance getCompetenceImportanceFk() {
        return competenceImportanceFk;
    }

    public void setCompetenceImportanceFk(CompetenceImportance competenceImportanceFk) {
        this.competenceImportanceFk = competenceImportanceFk;
    }

    public Levels getLevelFk() {
        return levelFk;
    }

    public void setLevelFk(Levels levelFk) {
        this.levelFk = levelFk;
    }

    public ProjectRoles getProjectRoles() {
        return projectRoles;
    }

    public void setProjectRoles(ProjectRoles projectRoles) {
        this.projectRoles = projectRoles;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProjectTechCompetence)) {
            return false;
        }
        ProjectTechCompetence other = (ProjectTechCompetence) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.naredo.tesis.entity.ProjectTechCompetence[ id=" + id + " ]";
    }
    
}
