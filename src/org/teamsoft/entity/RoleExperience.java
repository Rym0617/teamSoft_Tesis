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
@Table(name = "role_experience")
@NamedQueries({
    @NamedQuery(name = "RoleExperience.findAll", query = "SELECT r FROM RoleExperience r")
    , @NamedQuery(name = "RoleExperience.findById", query = "SELECT r FROM RoleExperience r WHERE r.id = :id")
    , @NamedQuery(name = "RoleExperience.findByRoleAndWorker", query = "SELECT r FROM RoleExperience r WHERE r.workerFk = :worker and r.roleFk = :role")
    , @NamedQuery(name = "RoleExperience.deleteByWorker", query = "delete from RoleExperience r where r.workerFk = :workerId")
    , @NamedQuery(name = "RoleExperience.findByIndexes", query = "SELECT r FROM RoleExperience r WHERE r.indexes = :indexes")})
public class RoleExperience implements Serializable {

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
    @Column(name = "indexes")
    private float indexes;
    @JoinColumn(name = "role_fk", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Role roleFk;
    @JoinColumn(name = "worker_fk", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Worker workerFk;

    public RoleExperience() {
    }

    public RoleExperience(Long id) {
        this.id = id;
    }

    public RoleExperience(Long id, float indexes) {
        this.id = id;
        this.indexes = indexes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getIndexes() {
        return indexes;
    }

    public void setIndexes(float indexes) {
        this.indexes = indexes;
    }

    public Role getRoleFk() {
        return roleFk;
    }

    public void setRoleFk(Role roleFk) {
        this.roleFk = roleFk;
    }

    public Worker getWorkerFk() {
        return workerFk;
    }

    public void setWorkerFk(Worker workerFk) {
        this.workerFk = workerFk;
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
        if (!(object instanceof RoleExperience)) {
            return false;
        }
        RoleExperience other = (RoleExperience) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.naredo.tesis.entity.RoleExperience[ id=" + id + " ]";
    }
    
}
