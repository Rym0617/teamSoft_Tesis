/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author yoyo
 */
@Entity
@Table(name = "competence")
@NamedQueries({
    @NamedQuery(name = "Competence.findAll", query = "SELECT c FROM Competence c")
    , @NamedQuery(name = "Competence.findById", query = "SELECT c FROM Competence c WHERE c.id = :id")
    , @NamedQuery(name = "Competence.findByCompetitionName", query = "SELECT c FROM Competence c WHERE c.competitionName = :competitionName")
    , @NamedQuery(name = "Competence.findByDescription", query = "SELECT c FROM Competence c WHERE c.description = :description")
    , @NamedQuery(name = "Competence.findByTechnical", query = "SELECT c FROM Competence c WHERE c.technical = :technical")})
public class Competence implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CUST_SEQ")
    @SequenceGenerator(sequenceName = "hibernate_sequence", allocationSize = 1, name = "CUST_SEQ")
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "competition_name")
    private String competitionName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "description")
    private String description;
    @Column(name = "technical")
    private Boolean technical;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "competenceFk")
    private List<RoleCompetition> roleCompetitionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "competenceFk")
    private List<ProjectTechCompetence> projectTechCompetenceList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "competenceFk")
    private List<CompetenceValue> competenceValueList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "competenceFk")
    private List<CompetenceDimension> competenceDimensionList;

    public Competence() {
    }

    public Competence(Long id) {
        this.id = id;
    }

    public Competence(Long id, String competitionName, String description) {
        this.id = id;
        this.competitionName = competitionName;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompetitionName() {
        return competitionName;
    }

    public void setCompetitionName(String competitionName) {
        this.competitionName = competitionName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getTechnical() {
        return technical;
    }

    public void setTechnical(Boolean technical) {
        this.technical = technical;
    }

    public List<RoleCompetition> getRoleCompetitionList() {
        return roleCompetitionList;
    }

    public void setRoleCompetitionList(List<RoleCompetition> roleCompetitionList) {
        this.roleCompetitionList = roleCompetitionList;
    }

    public List<ProjectTechCompetence> getProjectTechCompetenceList() {
        return projectTechCompetenceList;
    }

    public void setProjectTechCompetenceList(List<ProjectTechCompetence> projectTechCompetenceList) {
        this.projectTechCompetenceList = projectTechCompetenceList;
    }

    public List<CompetenceValue> getCompetenceValueList() {
        return competenceValueList;
    }

    public void setCompetenceValueList(List<CompetenceValue> competenceValueList) {
        this.competenceValueList = competenceValueList;
    }

    public List<CompetenceDimension> getCompetenceDimensionList() {
        return competenceDimensionList;
    }

    public void setCompetenceDimensionList(List<CompetenceDimension> competenceDimensionList) {
        this.competenceDimensionList = competenceDimensionList;
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
        if (!(object instanceof Competence)) {
            return false;
        }
        Competence other = (Competence) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.naredo.tesis.entity.Competence[ id=" + id + " ]";
    }
    
}
