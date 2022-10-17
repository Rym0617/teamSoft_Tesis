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

/**
 *
 * @author yoyo
 */
@Entity
@Table(name = "assigned_role")
@NamedQueries({
    @NamedQuery(name = "AssignedRole.findAll", query = "SELECT a FROM AssignedRole a")
    , @NamedQuery(name = "AssignedRole.findById", query = "SELECT a FROM AssignedRole a WHERE a.id = :id")
    , @NamedQuery(name = "AssignedRole.findByStatus", query = "SELECT a FROM AssignedRole a WHERE a.status = :status")
    , @NamedQuery(name = "AssignedRole.findByObservation", query = "SELECT a FROM AssignedRole a WHERE a.observation = :observation")
    , @NamedQuery(name = "AssignedRole.findByEndDate", query = "SELECT a FROM AssignedRole a WHERE a.endDate = :endDate")
    , @NamedQuery(name = "AssignedRole.findByBeginDate", query = "SELECT a FROM AssignedRole a WHERE a.beginDate = :beginDate")})
public class AssignedRole implements Serializable {

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
    @Column(name = "status")
    private String status;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "observation")
    private String observation;
    @Column(name = "end_date")
    @Temporal(TemporalType.DATE)
    private Date endDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "begin_date")
    @Temporal(TemporalType.DATE)
    private Date beginDate;
    @JoinColumn(name = "cycles_fk", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Cycle cyclesFk;
    @JoinColumn(name = "roles_fk", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Role rolesFk;
    @JoinColumn(name = "workers_fk", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Worker workersFk;

    public AssignedRole() {
    }

    public AssignedRole(Long id) {
        this.id = id;
    }

    public AssignedRole(Long id, String status, String observation, Date beginDate) {
        this.id = id;
        this.status = status;
        this.observation = observation;
        this.beginDate = beginDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Cycle getCyclesFk() {
        return cyclesFk;
    }

    public void setCyclesFk(Cycle cyclesFk) {
        this.cyclesFk = cyclesFk;
    }

    public Role getRolesFk() {
        return rolesFk;
    }

    public void setRolesFk(Role rolesFk) {
        this.rolesFk = rolesFk;
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
        if (!(object instanceof AssignedRole)) {
            return false;
        }
        AssignedRole other = (AssignedRole) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.naredo.tesis.entity.AssignedRole[ id=" + id + " ]";
    }
    
}
