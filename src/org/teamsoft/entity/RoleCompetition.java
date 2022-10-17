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
@Table(name = "role_competition")
@NamedQueries({
    @NamedQuery(name = "RoleCompetition.findAll", query = "SELECT r FROM RoleCompetition r")
    , @NamedQuery(name = "RoleCompetition.findById", query = "SELECT r FROM RoleCompetition r WHERE r.id = :id")})
public class RoleCompetition implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CUST_SEQ")
    @SequenceGenerator(sequenceName = "hibernate_sequence", allocationSize = 1, name = "CUST_SEQ")
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "competence_fk", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Competence competenceFk;
    @JoinColumn(name = "comp_importance_fk", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private CompetenceImportance compImportanceFk;
    @JoinColumn(name = "levels_fk", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Levels levelsFk;
    @JoinColumn(name = "roles_fk", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Role rolesFk;

    public RoleCompetition() {
    }

    public RoleCompetition(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Competence getCompetenceFk() {
        return competenceFk;
    }

    public void setCompetenceFk(Competence competenceFk) {
        this.competenceFk = competenceFk;
    }

    public CompetenceImportance getCompImportanceFk() {
        return compImportanceFk;
    }

    public void setCompImportanceFk(CompetenceImportance compImportanceFk) {
        this.compImportanceFk = compImportanceFk;
    }

    public Levels getLevelsFk() {
        return levelsFk;
    }

    public void setLevelsFk(Levels levelsFk) {
        this.levelsFk = levelsFk;
    }

    public Role getRolesFk() {
        return rolesFk;
    }

    public void setRolesFk(Role rolesFk) {
        this.rolesFk = rolesFk;
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
        if (!(object instanceof RoleCompetition)) {
            return false;
        }
        RoleCompetition other = (RoleCompetition) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.naredo.tesis.entity.RoleCompetition[ id=" + id + " ]";
    }
    
}
