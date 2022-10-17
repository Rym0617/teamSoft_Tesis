/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author yoyo
 */
@Entity
@Table(name = "cycle")
@NamedQueries({
    @NamedQuery(name = "Cycle.findAll", query = "SELECT c FROM Cycle c")
    , @NamedQuery(name = "Cycle.findById", query = "SELECT c FROM Cycle c WHERE c.id = :id")
    , @NamedQuery(name = "Cycle.findByBeginDate", query = "SELECT c FROM Cycle c WHERE c.beginDate = :beginDate")
    , @NamedQuery(name = "Cycle.findByEndDate", query = "SELECT c FROM Cycle c WHERE c.endDate = :endDate")})
public class Cycle implements Serializable {

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
    @Column(name = "begin_date")
    @Temporal(TemporalType.DATE)
    private Date beginDate;
    @Column(name = "end_date")
    @Temporal(TemporalType.DATE)
    private Date endDate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cyclesFk")
    private List<AssignedRole> assignedRoleList;
    @JoinColumn(name = "project_fk", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Project projectFk;
    @JoinColumn(name = "structure_fk", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ProjectStructure structureFk;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cycleFk")
    private List<ProjectRoleIncomp> projectRoleIncompList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cycleFk")
    private List<RoleEvaluation> roleEvaluationList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cycleFk")
    private List<RoleEvalProject> roleEvalProjectList;

    public Cycle() {
    }

    public Cycle(Long id) {
        this.id = id;
    }

    public Cycle(Long id, Date beginDate) {
        this.id = id;
        this.beginDate = beginDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<AssignedRole> getAssignedRoleList() {
        return assignedRoleList;
    }

    public void setAssignedRoleList(List<AssignedRole> assignedRoleList) {
        this.assignedRoleList = assignedRoleList;
    }

    public Project getProjectFk() {
        return projectFk;
    }

    public void setProjectFk(Project projectFk) {
        this.projectFk = projectFk;
    }

    public ProjectStructure getStructureFk() {
        return structureFk;
    }

    public void setStructureFk(ProjectStructure structureFk) {
        this.structureFk = structureFk;
    }

    public List<ProjectRoleIncomp> getProjectRoleIncompList() {
        return projectRoleIncompList;
    }

    public void setProjectRoleIncompList(List<ProjectRoleIncomp> projectRoleIncompList) {
        this.projectRoleIncompList = projectRoleIncompList;
    }

    public List<RoleEvaluation> getRoleEvaluationList() {
        return roleEvaluationList;
    }

    public void setRoleEvaluationList(List<RoleEvaluation> roleEvaluationList) {
        this.roleEvaluationList = roleEvaluationList;
    }

    public List<RoleEvalProject> getRoleEvalProjectList() {
        return roleEvalProjectList;
    }

    public void setRoleEvalProjectList(List<RoleEvalProject> roleEvalProjectList) {
        this.roleEvalProjectList = roleEvalProjectList;
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
        if (!(object instanceof Cycle)) {
            return false;
        }
        Cycle other = (Cycle) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.naredo.tesis.entity.Cycle[ id=" + id + " ]";
    }
    
}
