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
@Table(name = "project_role_incomp")
@NamedQueries({
    @NamedQuery(name = "ProjectRoleIncomp.findAll", query = "SELECT p FROM ProjectRoleIncomp p")
    , @NamedQuery(name = "ProjectRoleIncomp.findById", query = "SELECT p FROM ProjectRoleIncomp p WHERE p.id = :id")})
public class ProjectRoleIncomp implements Serializable {

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
    @JoinColumn(name = "role_a_fk", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Role roleAFk;
    @JoinColumn(name = "role_b_fk", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Role roleBFk;

    public ProjectRoleIncomp() {
    }

    public ProjectRoleIncomp(Long id) {
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

    public Role getRoleAFk() {
        return roleAFk;
    }

    public void setRoleAFk(Role roleAFk) {
        this.roleAFk = roleAFk;
    }

    public Role getRoleBFk() {
        return roleBFk;
    }

    public void setRoleBFk(Role roleBFk) {
        this.roleBFk = roleBFk;
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
        if (!(object instanceof ProjectRoleIncomp)) {
            return false;
        }
        ProjectRoleIncomp other = (ProjectRoleIncomp) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.naredo.tesis.entity.ProjectRoleIncomp[ id=" + id + " ]";
    }
    
}
