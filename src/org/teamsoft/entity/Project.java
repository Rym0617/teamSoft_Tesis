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
import java.util.Date;
import java.util.List;

/**
 *
 * @author yoyo
 */
@Entity
@Table(name = "project")
@NamedQueries({
    @NamedQuery(name = "Project.findAll", query = "SELECT p FROM Project p")
    , @NamedQuery(name = "Project.findById", query = "SELECT p FROM Project p WHERE p.id = :id")
    , @NamedQuery(name = "Project.findByProjectName", query = "SELECT p FROM Project p WHERE p.projectName = :projectName")
    , @NamedQuery(name = "Project.findByClose", query = "SELECT p FROM Project p WHERE p.close = :close")
    , @NamedQuery(name = "Project.findByInitialDate", query = "SELECT p FROM Project p WHERE p.initialDate = :initialDate")
    , @NamedQuery(name = "Project.findByFinalize", query = "SELECT p FROM Project p WHERE p.finalize = :finalize")
    , @NamedQuery(name = "Project.findByEndDate", query = "SELECT p FROM Project p WHERE p.endDate = :endDate")})
public class Project implements Serializable {

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
    @Column(name = "project_name")
    private String projectName;
    @Basic(optional = false)
    @NotNull
    @Column(name = "close")
    private boolean close;
    @Basic(optional = false)
    @NotNull
    @Column(name = "initial_date")
    @Temporal(TemporalType.DATE)
    private Date initialDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "finalize")
    private boolean finalize;
    @Column(name = "end_date")
    @Temporal(TemporalType.DATE)
    private Date endDate;
    @JoinColumn(name = "client_entity_fk", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ClientEntity clientEntityFk;
    @JoinColumn(name = "province_fk", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private County provinceFk;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "projectFk")
    private List<Cycle> cycleList;
     @OneToMany(cascade = CascadeType.ALL, mappedBy = "projectFk")
    private List<PersonalProjectInterests> personalProjectInterestsList;

    public Project() {
    }

    public Project(Long id) {
        this.id = id;
    }

    public Project(Long id, String projectName, boolean close, Date initialDate, boolean finalize) {
        this.id = id;
        this.projectName = projectName;
        this.close = close;
        this.initialDate = initialDate;
        this.finalize = finalize;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public boolean getClose() {
        return close;
    }

    public void setClose(boolean close) {
        this.close = close;
    }

    public Date getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(Date initialDate) {
        this.initialDate = initialDate;
    }

    public boolean getFinalize() {
        return finalize;
    }

    public void setFinalize(boolean finalize) {
        this.finalize = finalize;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public ClientEntity getClientEntityFk() {
        return clientEntityFk;
    }

    public void setClientEntityFk(ClientEntity clientEntityFk) {
        this.clientEntityFk = clientEntityFk;
    }

    public County getProvinceFk() {
        return provinceFk;
    }

    public void setProvinceFk(County provinceFk) {
        this.provinceFk = provinceFk;
    }

    public List<Cycle> getCycleList() {
        return cycleList;
    }

    public void setCycleList(List<Cycle> cycleList) {
        this.cycleList = cycleList;
    }

    public List<PersonalProjectInterests> getPersonalProjectInterestsList() {
        return personalProjectInterestsList;
    }

    public void setPersonalProjectInterestsList(List<PersonalProjectInterests> personalProjectInterestsList) {
        this.personalProjectInterestsList = personalProjectInterestsList;
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
        if (!(object instanceof Project)) {
            return false;
        }
        Project other = (Project) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.naredo.tesis.entity.Project[ id=" + id + " ]";
    }
    
}
