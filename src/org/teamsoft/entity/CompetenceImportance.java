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
@Table(name = "competence_importance")
@NamedQueries({
    @NamedQuery(name = "CompetenceImportance.findAll", query = "SELECT c FROM CompetenceImportance c")
    , @NamedQuery(name = "CompetenceImportance.findById", query = "SELECT c FROM CompetenceImportance c WHERE c.id = :id")
    , @NamedQuery(name = "CompetenceImportance.findByLevels", query = "SELECT c FROM CompetenceImportance c WHERE c.levels = :levels")
    , @NamedQuery(name = "CompetenceImportance.findBySignificance", query = "SELECT c FROM CompetenceImportance c WHERE c.significance = :significance")})
public class CompetenceImportance implements Serializable {

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
    @Column(name = "levels")
    private long levels;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "significance")
    private String significance;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "compImportanceFk")
    private List<RoleCompetition> roleCompetitionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "competenceImportanceFk")
    private List<ProjectTechCompetence> projectTechCompetenceList;

    public CompetenceImportance() {
    }

    public CompetenceImportance(Long id) {
        this.id = id;
    }

    public CompetenceImportance(Long id, long levels, String significance) {
        this.id = id;
        this.levels = levels;
        this.significance = significance;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CompetenceImportance)) {
            return false;
        }
        CompetenceImportance other = (CompetenceImportance) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.naredo.tesis.entity.CompetenceImportance[ id=" + id + " ]";
    }
    
}
