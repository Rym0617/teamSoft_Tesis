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
 * @author yoyo
 */
@Entity
@Table(name = "app_user")
@NamedQueries({
        @NamedQuery(name = "AppUser.findAll", query = "SELECT a FROM AppUser a")
        , @NamedQuery(name = "AppUser.findByUserId", query = "SELECT a FROM AppUser a WHERE a.userId = :userId")
        , @NamedQuery(name = "AppUser.findByUserName", query = "SELECT a FROM AppUser a WHERE a.userName = :userName")
        , @NamedQuery(name = "AppUser.findByEncrytedPassword", query = "SELECT a FROM AppUser a WHERE a.encrytedPassword = :encrytedPassword")
        , @NamedQuery(name = "AppUser.findByEnabled", query = "SELECT a FROM AppUser a WHERE a.enabled = :enabled")})
public class AppUser implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CUST_SEQ")
    @SequenceGenerator(sequenceName = "hibernate_sequence", allocationSize = 1, name = "CUST_SEQ")
    @Column(name = "user_id")
    private Long userId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 36)
    @Column(name = "user_name")
    private String userName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "encryted_password")
    private String encrytedPassword;
    @Basic(optional = false)
    @NotNull
    @Column(name = "enabled")
    private int enabled;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private List<UserRole> userRoleList;

    public AppUser() {
    }

    public AppUser(Long userId) {
        this.userId = userId;
    }

    public AppUser(Long userId, String userName, String encrytedPassword, int enabled) {
        this.userId = userId;
        this.userName = userName;
        this.encrytedPassword = encrytedPassword;
        this.enabled = enabled;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEncrytedPassword() {
        return encrytedPassword;
    }

    public void setEncrytedPassword(String encrytedPassword) {
        this.encrytedPassword = encrytedPassword;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
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
        hash += (userId != null ? userId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AppUser)) {
            return false;
        }
        AppUser other = (AppUser) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.naredo.tesis.entity.AppUser[ userId=" + userId + " ]";
    }

}
