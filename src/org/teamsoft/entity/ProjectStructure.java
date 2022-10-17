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
@Table(name = "project_structure")
@NamedQueries({
    @NamedQuery(name = "ProjectStructure.findAll", query = "SELECT p FROM ProjectStructure p")
    , @NamedQuery(name = "ProjectStructure.findById", query = "SELECT p FROM ProjectStructure p WHERE p.id = :id")
    , @NamedQuery(name = "ProjectStructure.findByName", query = "SELECT p FROM ProjectStructure p WHERE p.name = :name")})
public class ProjectStructure implements Serializable {

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
    @Column(name = "name")
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "structureFk")
    private List<Cycle> cycleList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "projectStructureFk")
    private List<ProjectRoles> projectRolesList;

    public ProjectStructure() {
    }

    public ProjectStructure(Long id) {
        this.id = id;
    }

    public ProjectStructure(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Cycle> getCycleList() {
        return cycleList;
    }

    public void setCycleList(List<Cycle> cycleList) {
        this.cycleList = cycleList;
    }

    public List<ProjectRoles> getProjectRolesList() {
        return projectRolesList;
    }

    public void setProjectRolesList(List<ProjectRoles> projectRolesList) {
        this.projectRolesList = projectRolesList;
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
        if (!(object instanceof ProjectStructure)) {
            return false;
        }
        ProjectStructure other = (ProjectStructure) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.naredo.tesis.entity.ProjectStructure[ id=" + id + " ]";
    }
    
}
