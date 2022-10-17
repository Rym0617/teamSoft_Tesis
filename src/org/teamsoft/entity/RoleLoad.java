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
@Table(name = "role_load")
@NamedQueries({
    @NamedQuery(name = "RoleLoad.findAll", query = "SELECT r FROM RoleLoad r")
    , @NamedQuery(name = "RoleLoad.findById", query = "SELECT r FROM RoleLoad r WHERE r.id = :id")
    , @NamedQuery(name = "RoleLoad.findByValue", query = "SELECT r FROM RoleLoad r WHERE r.value = :value")
    , @NamedQuery(name = "RoleLoad.findBySignificance", query = "SELECT r FROM RoleLoad r WHERE r.significance = :significance")})
public class RoleLoad implements Serializable {

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
    @Column(name = "value")
    private float value;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "significance")
    private String significance;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "roleLoadFk")
    private List<ProjectRoles> projectRolesList;

    public RoleLoad() {
    }

    public RoleLoad(Long id) {
        this.id = id;
    }

    public RoleLoad(Long id, float value, String significance) {
        this.id = id;
        this.value = value;
        this.significance = significance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getSignificance() {
        return significance;
    }

    public void setSignificance(String significance) {
        this.significance = significance;
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
        if (!(object instanceof RoleLoad)) {
            return false;
        }
        RoleLoad other = (RoleLoad) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.naredo.tesis.entity.RoleLoad[ id=" + id + " ]";
    }
    
}
