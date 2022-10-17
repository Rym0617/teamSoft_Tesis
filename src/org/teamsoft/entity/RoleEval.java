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
@Table(name = "role_eval")
@NamedQueries({
    @NamedQuery(name = "RoleEval.findAll", query = "SELECT r FROM RoleEval r")
    , @NamedQuery(name = "RoleEval.findById", query = "SELECT r FROM RoleEval r WHERE r.id = :id")
    , @NamedQuery(name = "RoleEval.findByLevels", query = "SELECT r FROM RoleEval r WHERE r.levels = :levels")
    , @NamedQuery(name = "RoleEval.findBySignificance", query = "SELECT r FROM RoleEval r WHERE r.significance = :significance")})
public class RoleEval implements Serializable {

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
    private float levels;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "significance")
    private String significance;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "rolEvalFk")
    private List<RoleEvaluation> roleEvaluationList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "roleEvalFk")
    private List<RoleEvalProject> roleEvalProjectList;

    public RoleEval() {
    }

    public RoleEval(Long id) {
        this.id = id;
    }

    public RoleEval(Long id, float levels, String significance) {
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

    public float getLevels() {
        return levels;
    }

    public void setLevels(float levels) {
        this.levels = levels;
    }

    public String getSignificance() {
        return significance;
    }

    public void setSignificance(String significance) {
        this.significance = significance;
    }

    public List<RoleEvaluation> getRoleEvaluationList() {
        return roleEvaluationList;
    }

    public void setRoleEvaluationList(List<RoleEvaluation> roleEvaluationList) {
        this.roleEvaluationList = roleEvaluationList;
    }

    public List<RoleEvalProject> getRoleEvalProjectList() {
        return roleEvalProjectList;
    }

    public void setRoleEvalProjectList(List<RoleEvalProject> roleEvalProjectList) {
        this.roleEvalProjectList = roleEvalProjectList;
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
        if (!(object instanceof RoleEval)) {
            return false;
        }
        RoleEval other = (RoleEval) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.naredo.tesis.entity.RoleEval[ id=" + id + " ]";
    }
    
}
