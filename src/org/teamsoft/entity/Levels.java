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
@Table(name = "levels")
@NamedQueries({
    @NamedQuery(name = "Levels.findAll", query = "SELECT l FROM Levels l")
    , @NamedQuery(name = "Levels.findById", query = "SELECT l FROM Levels l WHERE l.id = :id")
    , @NamedQuery(name = "Levels.findByLevels", query = "SELECT l FROM Levels l WHERE l.levels = :levels")
    , @NamedQuery(name = "Levels.findBySignificance", query = "SELECT l FROM Levels l WHERE l.significance = :significance")})
public class Levels implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "levels")
    private long levels;
    @Size(max = 500)
    @Column(name = "significance")
    private String significance;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "levelsFk")
    private List<RoleCompetition> roleCompetitionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "levelFk")
    private List<ProjectTechCompetence> projectTechCompetenceList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "levelFk")
    private List<CompetenceValue> competenceValueList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "levelFk")
    private List<CompetenceDimension> competenceDimensionList;

    public Levels() {
    }

    public Levels(Long id) {
        this.id = id;
    }

    public Levels(Long id, long levels) {
        this.id = id;
        this.levels = levels;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getLevels() {
        return levels;
    }

    public void setLevels(long levels) {
        this.levels = levels;
    }

    public String getSignificance() {
        return significance;
    }

    public void setSignificance(String significance) {
        this.significance = significance;
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
        if (!(object instanceof Levels)) {
            return false;
        }
        Levels other = (Levels) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.naredo.tesis.entity.Levels[ id=" + id + " ]";
    }
    
}
