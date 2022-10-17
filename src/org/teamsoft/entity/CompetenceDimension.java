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

/**
 *
 * @author yoyo
 */
@Entity
@Table(name = "competence_dimension")
@NamedQueries({
    @NamedQuery(name = "CompetenceDimension.findAll", query = "SELECT c FROM CompetenceDimension c")
    , @NamedQuery(name = "CompetenceDimension.findById", query = "SELECT c FROM CompetenceDimension c WHERE c.id = :id")
    , @NamedQuery(name = "CompetenceDimension.findByName", query = "SELECT c FROM CompetenceDimension c WHERE c.name = :name")})
public class CompetenceDimension implements Serializable {

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
    @Column(name = "name")
    private String name;
    @JoinColumn(name = "competence_fk", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Competence competenceFk;
    @JoinColumn(name = "level_fk", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Levels levelFk;

    public CompetenceDimension() {
    }

    public CompetenceDimension(Long id) {
        this.id = id;
    }

    public CompetenceDimension(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CompetenceDimension)) {
            return false;
        }
        CompetenceDimension other = (CompetenceDimension) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.naredo.tesis.entity.CompetenceDimension[ id=" + id + " ]";
    }
    
}
