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
@Table(name = "role_eval_project")
@NamedQueries({
    @NamedQuery(name = "RoleEvalProject.findAll", query = "SELECT r FROM RoleEvalProject r")
    , @NamedQuery(name = "RoleEvalProject.findById", query = "SELECT r FROM RoleEvalProject r WHERE r.id = :id")})
public class RoleEvalProject implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CUST_SEQ")
    @SequenceGenerator(sequenceName = "hibernate_sequence", allocationSize = 1, name = "CUST_SEQ")
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "cycle_fk", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Cycle cycleFk;
    @JoinColumn(name = "role_eval_fk", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private RoleEval roleEvalFk;

    public RoleEvalProject() {
    }

    public RoleEvalProject(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cycle getCycleFk() {
        return cycleFk;
    }

    public void setCycleFk(Cycle cycleFk) {
        this.cycleFk = cycleFk;
    }

    public RoleEval getRoleEvalFk() {
        return roleEvalFk;
    }

    public void setRoleEvalFk(RoleEval roleEvalFk) {
        this.roleEvalFk = roleEvalFk;
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
        if (!(object instanceof RoleEvalProject)) {
            return false;
        }
        RoleEvalProject other = (RoleEvalProject) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.naredo.tesis.entity.RoleEvalProject[ id=" + id + " ]";
    }
    
}
