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
@Table(name = "municipality")
@NamedQueries({
    @NamedQuery(name = "Municipality.findAll", query = "SELECT m FROM Municipality m")
    , @NamedQuery(name = "Municipality.findById", query = "SELECT m FROM Municipality m WHERE m.id = :id")
    , @NamedQuery(name = "Municipality.findByName", query = "SELECT m FROM Municipality m WHERE m.name = :name")})
public class Municipality implements Serializable {

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
    @JoinColumn(name = "county_fk", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private County countyFk;

    public Municipality() {
    }

    public Municipality(Long id) {
        this.id = id;
    }

    public Municipality(Long id, String name) {
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

    public County getCountyFk() {
        return countyFk;
    }

    public void setCountyFk(County countyFk) {
        this.countyFk = countyFk;
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
        if (!(object instanceof Municipality)) {
            return false;
        }
        Municipality other = (Municipality) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.naredo.tesis.entity.Municipality[ id=" + id + " ]";
    }
    
}
