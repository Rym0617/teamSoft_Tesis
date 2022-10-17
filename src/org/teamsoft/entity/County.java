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
@Table(name = "county")
@NamedQueries({
    @NamedQuery(name = "County.findAll", query = "SELECT c FROM County c")
    , @NamedQuery(name = "County.findById", query = "SELECT c FROM County c WHERE c.id = :id")
    , @NamedQuery(name = "County.findByCountyName", query = "SELECT c FROM County c WHERE c.countyName = :countyName")
    , @NamedQuery(name = "County.findByCode", query = "SELECT c FROM County c WHERE c.code = :code")})
public class County implements Serializable {

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
    @Column(name = "county_name")
    private String countyName;
    @Size(max = 1024)
    @Column(name = "code")
    private String code;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "countyFk")
    private List<Municipality> municipalityList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "provinceFk")
    private List<Project> projectList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "countyFk")
    private List<Worker> workerList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "countyAFk")
    private List<CostDistance> costDistanceList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "countyBFk")
    private List<CostDistance> costDistanceList1;

    public County() {
    }

    public County(Long id) {
        this.id = id;
    }

    public County(Long id, String countyName) {
        this.id = id;
        this.countyName = countyName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Municipality> getMunicipalityList() {
        return municipalityList;
    }

    public void setMunicipalityList(List<Municipality> municipalityList) {
        this.municipalityList = municipalityList;
    }

    public List<Project> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<Project> projectList) {
        this.projectList = projectList;
    }

    public List<Worker> getWorkerList() {
        return workerList;
    }

    public void setWorkerList(List<Worker> workerList) {
        this.workerList = workerList;
    }

    public List<CostDistance> getCostDistanceList() {
        return costDistanceList;
    }

    public void setCostDistanceList(List<CostDistance> costDistanceList) {
        this.costDistanceList = costDistanceList;
    }

    public List<CostDistance> getCostDistanceList1() {
        return costDistanceList1;
    }

    public void setCostDistanceList1(List<CostDistance> costDistanceList1) {
        this.costDistanceList1 = costDistanceList1;
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
        if (!(object instanceof County)) {
            return false;
        }
        County other = (County) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.naredo.tesis.entity.County[ id=" + id + " ]";
    }
    
}
