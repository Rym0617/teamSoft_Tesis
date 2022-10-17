/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author yoyo
 */
@Entity
@Table(name = "project_roles")
@NamedQueries({
    @NamedQuery(name = "ProjectRoles.findAll", query = "SELECT p FROM ProjectRoles p")
    , @NamedQuery(name = "ProjectRoles.findById", query = "SELECT p FROM ProjectRoles p WHERE p.id = :id")
    , @NamedQuery(name = "ProjectRoles.findByAmountWorkersRole", query = "SELECT p FROM ProjectRoles p WHERE p.amountWorkersRole = :amountWorkersRole")})
public class ProjectRoles implements Serializable {

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
    @Column(name = "amount_workers_role")
    private long amountWorkersRole;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "projectRoles")
    private List<ProjectTechCompetence> projectTechCompetenceList;
    @JoinColumn(name = "project_structure_fk", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ProjectStructure projectStructureFk;
    @JoinColumn(name = "role_fk", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Role roleFk;
    @JoinColumn(name = "role_load_fk", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private RoleLoad roleLoadFk;

    public ProjectRoles() {
    }

    public ProjectRoles(Long id) {
        this.id = id;
    }

    public ProjectRoles(Long id, long amountWorkersRole) {
        this.id = id;
        this.amountWorkersRole = amountWorkersRole;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getAmountWorkersRole() {
        return amountWorkersRole;
    }

    public void setAmountWorkersRole(long amountWorkersRole) {
        this.amountWorkersRole = amountWorkersRole;
    }

    public List<ProjectTechCompetence> getProjectTechCompetenceList() {
        return projectTechCompetenceList;
    }

    public void setProjectTechCompetenceList(List<ProjectTechCompetence> projectTechCompetenceList) {
        this.projectTechCompetenceList = projectTechCompetenceList;
    }

    public ProjectStructure getProjectStructureFk() {
        return projectStructureFk;
    }

    public void setProjectStructureFk(ProjectStructure projectStructureFk) {
        this.projectStructureFk = projectStructureFk;
    }

    public Role getRoleFk() {
        return roleFk;
    }

    public void setRoleFk(Role roleFk) {
        this.roleFk = roleFk;
    }

    public RoleLoad getRoleLoadFk() {
        return roleLoadFk;
    }

    public void setRoleLoadFk(RoleLoad roleLoadFk) {
        this.roleLoadFk = roleLoadFk;
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
        if (!(object instanceof ProjectRoles)) {
            return false;
        }
        ProjectRoles other = (ProjectRoles) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.naredo.tesis.entity.ProjectRoles[ id=" + id + " ]";
    }
    
}
