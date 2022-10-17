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
import java.util.Objects;

/**
 * @author yoyo & jpinas
 */
@Entity
@Table(name = "role")
@NamedQueries({
        @NamedQuery(name = "Role.findAll", query = "SELECT r FROM Role r")
        , @NamedQuery(name = "Role.findById", query = "SELECT r FROM Role r WHERE r.id = :id")
        , @NamedQuery(name = "Role.findByRoleName", query = "SELECT r FROM Role r WHERE r.roleName = :roleName")
        , @NamedQuery(name = "Role.findByRoleDesc", query = "SELECT r FROM Role r WHERE r.roleDesc = :roleDesc")
        , @NamedQuery(name = "Role.findBoss", query = "SELECT r FROM Role r WHERE r.isBoss = true")
        , @NamedQuery(name = "Role.findByImpact", query = "SELECT r FROM Role r WHERE r.impact = :impact")})
public class Role implements Serializable {

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
    @Column(name = "role_name")
    private String roleName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "role_desc")
    private String roleDesc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "impact")
    private float impact;
    @NotNull
    @Column(name = "is_boss")
    private boolean isBoss;
    @JoinTable(name = "incompatible_roles", joinColumns = {
            @JoinColumn(name = "compatibilities_fk", referencedColumnName = "id")}, inverseJoinColumns = {
            @JoinColumn(name = "incompatibilities_fk", referencedColumnName = "id")})
    @ManyToMany
    private List<Role> roleList;
    @ManyToMany(mappedBy = "roleList")
    private List<Role> roleList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "rolesFk")
    private List<AssignedRole> assignedRoleList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "rolesFk")
    private List<RoleCompetition> roleCompetitionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "roleFk")
    private List<RoleExperience> roleExperienceList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "roleAFk")
    private List<ProjectRoleIncomp> projectRoleIncompList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "roleBFk")
    private List<ProjectRoleIncomp> projectRoleIncompList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "rolesFk")
    private List<PersonalInterests> personalInterestsList;
    @OneToMany(mappedBy = "roleFk")
    private List<RoleEvaluation> roleEvaluationList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "roleFk")
    private List<ProjectRoles> projectRolesList;

    public Role() {
    }

    public Role(Long id) {
        this.id = id;
    }

    public Role(Long id, String roleName, String roleDesc, float impact) {
        this.id = id;
        this.roleName = roleName;
        this.roleDesc = roleDesc;
        this.impact = impact;
    }

    public boolean isBoss() {
        return isBoss;
    }

    public void setBoss(boolean boss) {
        isBoss = boss;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    public float getImpact() {
        return impact;
    }

    public void setImpact(float impact) {
        this.impact = impact;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    public List<Role> getRoleList1() {
        return roleList1;
    }

    public void setRoleList1(List<Role> roleList1) {
        this.roleList1 = roleList1;
    }

    public List<AssignedRole> getAssignedRoleList() {
        return assignedRoleList;
    }

    public void setAssignedRoleList(List<AssignedRole> assignedRoleList) {
        this.assignedRoleList = assignedRoleList;
    }

    public List<RoleCompetition> getRoleCompetitionList() {
        return roleCompetitionList;
    }

    public void setRoleCompetitionList(List<RoleCompetition> roleCompetitionList) {
        this.roleCompetitionList = roleCompetitionList;
    }

    public List<RoleExperience> getRoleExperienceList() {
        return roleExperienceList;
    }

    public void setRoleExperienceList(List<RoleExperience> roleExperienceList) {
        this.roleExperienceList = roleExperienceList;
    }

    public List<ProjectRoleIncomp> getProjectRoleIncompList() {
        return projectRoleIncompList;
    }

    public void setProjectRoleIncompList(List<ProjectRoleIncomp> projectRoleIncompList) {
        this.projectRoleIncompList = projectRoleIncompList;
    }

    public List<ProjectRoleIncomp> getProjectRoleIncompList1() {
        return projectRoleIncompList1;
    }

    public void setProjectRoleIncompList1(List<ProjectRoleIncomp> projectRoleIncompList1) {
        this.projectRoleIncompList1 = projectRoleIncompList1;
    }

    public List<PersonalInterests> getPersonalInterestsList() {
        return personalInterestsList;
    }

    public void setPersonalInterestsList(List<PersonalInterests> personalInterestsList) {
        this.personalInterestsList = personalInterestsList;
    }

    public List<RoleEvaluation> getRoleEvaluationList() {
        return roleEvaluationList;
    }

    public void setRoleEvaluationList(List<RoleEvaluation> roleEvaluationList) {
        this.roleEvaluationList = roleEvaluationList;
    }

    public List<ProjectRoles> getProjectRolesList() {
        return projectRolesList;
    }

    public void setProjectRolesList(List<ProjectRoles> projectRolesList) {
        this.projectRolesList = projectRolesList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(id, role.id) &&
                Objects.equals(roleName, role.roleName);
    }

    @Override
    public String toString() {
        return roleName;
    }

}
