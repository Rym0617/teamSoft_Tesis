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
@Table(name = "app_role")
@NamedQueries({
    @NamedQuery(name = "AppRole.findAll", query = "SELECT a FROM AppRole a")
    , @NamedQuery(name = "AppRole.findByRoleId", query = "SELECT a FROM AppRole a WHERE a.roleId = :roleId")
    , @NamedQuery(name = "AppRole.findByRoleName", query = "SELECT a FROM AppRole a WHERE a.roleName = :roleName")})
public class AppRole implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CUST_SEQ")
    @SequenceGenerator(sequenceName = "hibernate_sequence", allocationSize = 1, name = "CUST_SEQ")
    @Column(name = "role_id")
    private Long roleId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "role_name")
    private String roleName;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "roleId")
    private List<UserRole> userRoleList;

    public AppRole() {
    }

    public AppRole(Long roleId) {
        this.roleId = roleId;
    }

    public AppRole(Long roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<UserRole> getUserRoleList() {
        return userRoleList;
    }

    public void setUserRoleList(List<UserRole> userRoleList) {
        this.userRoleList = userRoleList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roleId != null ? roleId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AppRole)) {
            return false;
        }
        AppRole other = (AppRole) object;
        if ((this.roleId == null && other.roleId != null) || (this.roleId != null && !this.roleId.equals(other.roleId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.naredo.tesis.entity.AppRole[ roleId=" + roleId + " ]";
    }

}
