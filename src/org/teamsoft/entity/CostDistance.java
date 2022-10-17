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
@Table(name = "cost_distance")
@NamedQueries({
    @NamedQuery(name = "CostDistance.findAll", query = "SELECT c FROM CostDistance c")
    , @NamedQuery(name = "CostDistance.findById", query = "SELECT c FROM CostDistance c WHERE c.id = :id")
    , @NamedQuery(name = "CostDistance.findByCostDistance", query = "SELECT c FROM CostDistance c WHERE c.costDistance = :costDistance")})
public class CostDistance implements Serializable {

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
    @Column(name = "cost_distance")
    private float costDistance;
    @JoinColumn(name = "county_a_fk", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private County countyAFk;
    @JoinColumn(name = "county_b_fk", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private County countyBFk;

    public CostDistance() {
    }

    public CostDistance(Long id) {
        this.id = id;
    }

    public CostDistance(Long id, float costDistance) {
        this.id = id;
        this.costDistance = costDistance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getCostDistance() {
        return costDistance;
    }

    public void setCostDistance(float costDistance) {
        this.costDistance = costDistance;
    }

    public County getCountyAFk() {
        return countyAFk;
    }

    public void setCountyAFk(County countyAFk) {
        this.countyAFk = countyAFk;
    }

    public County getCountyBFk() {
        return countyBFk;
    }

    public void setCountyBFk(County countyBFk) {
        this.countyBFk = countyBFk;
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
        if (!(object instanceof CostDistance)) {
            return false;
        }
        CostDistance other = (CostDistance) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.naredo.tesis.entity.CostDistance[ id=" + id + " ]";
    }
    
}
