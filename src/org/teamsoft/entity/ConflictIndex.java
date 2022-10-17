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
@Table(name = "conflict_index")
@NamedQueries({
    @NamedQuery(name = "ConflictIndex.findAll", query = "SELECT c FROM ConflictIndex c")
    , @NamedQuery(name = "ConflictIndex.findById", query = "SELECT c FROM ConflictIndex c WHERE c.id = :id")
    , @NamedQuery(name = "ConflictIndex.findByDescription", query = "SELECT c FROM ConflictIndex c WHERE c.description = :description")
    , @NamedQuery(name = "ConflictIndex.findByWeight", query = "SELECT c FROM ConflictIndex c WHERE c.weight = :weight")})
public class ConflictIndex implements Serializable {

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
    @Size(min = 1, max = 1024)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Column(name = "weight")
    private long weight;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "indexFk")
    private List<WorkerConflict> workerConflictList;

    public ConflictIndex() {
    }

    public ConflictIndex(Long id) {
        this.id = id;
    }

    public ConflictIndex(Long id, String description, long weight) {
        this.id = id;
        this.description = description;
        this.weight = weight;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getWeight() {
        return weight;
    }

    public void setWeight(long weight) {
        this.weight = weight;
    }

    public List<WorkerConflict> getWorkerConflictList() {
        return workerConflictList;
    }

    public void setWorkerConflictList(List<WorkerConflict> workerConflictList) {
        this.workerConflictList = workerConflictList;
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
        if (!(object instanceof ConflictIndex)) {
            return false;
        }
        ConflictIndex other = (ConflictIndex) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.naredo.tesis.entity.ConflictIndex[ id=" + id + " ]";
    }
    
}
