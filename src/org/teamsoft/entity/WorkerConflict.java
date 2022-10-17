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
@Table(name = "worker_conflict")
@NamedQueries({
    @NamedQuery(name = "WorkerConflict.findAll", query = "SELECT w FROM WorkerConflict w")
    , @NamedQuery(name = "WorkerConflict.findById", query = "SELECT w FROM WorkerConflict w WHERE w.id = :id")})
public class WorkerConflict implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CUST_SEQ")
    @SequenceGenerator(sequenceName = "hibernate_sequence", allocationSize = 1, name = "CUST_SEQ")
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "index_fk", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ConflictIndex indexFk;
    @JoinColumn(name = "worker_conflict_fk", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Worker workerConflictFk;
    @JoinColumn(name = "worker_fk", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Worker workerFk;

    public WorkerConflict() {
    }

    public WorkerConflict(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ConflictIndex getIndexFk() {
        return indexFk;
    }

    public void setIndexFk(ConflictIndex indexFk) {
        this.indexFk = indexFk;
    }

    public Worker getWorkerConflictFk() {
        return workerConflictFk;
    }

    public void setWorkerConflictFk(Worker workerConflictFk) {
        this.workerConflictFk = workerConflictFk;
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
        if (!(object instanceof WorkerConflict)) {
            return false;
        }
        WorkerConflict other = (WorkerConflict) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.naredo.tesis.entity.WorkerConflict[ id=" + id + " ]";
    }
    
}
