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
@Table(name = "role_evaluation")
@NamedQueries({
    @NamedQuery(name = "RoleEvaluation.findAll", query = "SELECT r FROM RoleEvaluation r")
    , @NamedQuery(name = "RoleEvaluation.findById", query = "SELECT r FROM RoleEvaluation r WHERE r.id = :id")})
public class RoleEvaluation implements Serializable {

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
    @JoinColumn(name = "role_fk", referencedColumnName = "id")
    @ManyToOne
    private Role roleFk;
    @JoinColumn(name = "rol_eval_fk", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private RoleEval rolEvalFk;
    @JoinColumn(name = "worker_fk", referencedColumnName = "id")
    @ManyToOne
    private Worker workerFk;

    public RoleEvaluation() {
    }

    public RoleEvaluation(Long id) {
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

    public Role getRoleFk() {
        return roleFk;
    }

    public void setRoleFk(Role roleFk) {
        this.roleFk = roleFk;
    }

    public RoleEval getRolEvalFk() {
        return rolEvalFk;
    }

    public void setRolEvalFk(RoleEval rolEvalFk) {
        this.rolEvalFk = rolEvalFk;
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
        if (!(object instanceof RoleEvaluation)) {
            return false;
        }
        RoleEvaluation other = (RoleEvaluation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.naredo.tesis.entity.RoleEvaluation[ id=" + id + " ]";
    }
    
}
