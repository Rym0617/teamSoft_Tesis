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
@Table(name = "competence_value")
@NamedQueries({
    @NamedQuery(name = "CompetenceValue.findAll", query = "SELECT c FROM CompetenceValue c"),
    @NamedQuery(name = "CompetenceValue.findByCompAndWorker", query = "SELECT c FROM CompetenceValue c where c.competenceFk = :competenceId and c.workersFk = :workerId"),
    @NamedQuery(name = "CompetenceValue.deleteCompetencesByWorker", query = "delete from CompetenceValue c where c.workersFk = :workerId"),
    @NamedQuery(name = "CompetenceValue.findById", query = "SELECT c FROM CompetenceValue c WHERE c.id = :id")})
public class CompetenceValue implements Serializable {

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
    @JoinColumn(name = "level_fk", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Levels levelFk;
    @JoinColumn(name = "workers_fk", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Worker workersFk;

    public CompetenceValue() {
    }

    public CompetenceValue(Long id) {
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

    public Levels getLevelFk() {
        return levelFk;
    }

    public void setLevelFk(Levels levelFk) {
        this.levelFk = levelFk;
    }

    public Worker getWorkersFk() {
        return workersFk;
    }

    public void setWorkersFk(Worker workersFk) {
        this.workersFk = workersFk;
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
        if (!(object instanceof CompetenceValue)) {
            return false;
        }
        CompetenceValue other = (CompetenceValue) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.naredo.tesis.entity.CompetenceValue[ id=" + id + " ]";
    }
    
}
