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
 * @author yoyo
 */
@Entity
@Table(name = "worker")
@NamedQueries({
        @NamedQuery(name = "Worker.findAll", query = "SELECT w FROM Worker w")
        , @NamedQuery(name = "Worker.findById", query = "SELECT w FROM Worker w WHERE w.id = :id")
        , @NamedQuery(name = "Worker.findByName", query = "SELECT w FROM Worker w WHERE trim(concat(trim(w.personName), ' ', trim(w.surName))) = :personName")
        , @NamedQuery(name = "Worker.findByIdCard", query = "SELECT w FROM Worker w WHERE w.idCard = :idCard")
        , @NamedQuery(name = "Worker.findBySurName", query = "SELECT w FROM Worker w WHERE w.surName = :surName")
        , @NamedQuery(name = "Worker.findByAddress", query = "SELECT w FROM Worker w WHERE w.address = :address")
        , @NamedQuery(name = "Worker.findByPhone", query = "SELECT w FROM Worker w WHERE w.phone = :phone")
        , @NamedQuery(name = "Worker.findBySex", query = "SELECT w FROM Worker w WHERE w.sex = :sex")
        , @NamedQuery(name = "Worker.findByEmail", query = "SELECT w FROM Worker w WHERE w.email = :email")
        , @NamedQuery(name = "Worker.findByInDate", query = "SELECT w FROM Worker w WHERE w.inDate = :inDate")
        , @NamedQuery(name = "Worker.findByWorkload", query = "SELECT w FROM Worker w WHERE w.workload = :workload")
        , @NamedQuery(name = "Worker.findByStatus", query = "SELECT w FROM Worker w WHERE w.status = :status")})
public class Worker implements Serializable {

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
    @Column(name = "person_name")
    private String personName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "id_card")
    private String idCard;
    @Size(max = 1024)
    @Column(name = "sur_name")
    private String surName;
    @Size(max = 1024)
    @Column(name = "address")
    private String address;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Size(max = 1024)
    @Column(name = "phone")
    private String phone;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sex")
    private Character sex;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 1024)
    @Column(name = "email")
    private String email;
    @Column(name = "in_date")
    @Temporal(TemporalType.DATE)
    private Date inDate;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "workload")
    private Float workload;
    @Column(name = "experience")
    private Integer experience;
    @Size(max = 1024)
    @Column(name = "status")
    private String status;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "workersFk")
    private List<AssignedRole> assignedRoleList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "workerFk")
    private List<RoleExperience> roleExperienceList;
    @JoinColumn(name = "county_fk", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private County countyFk;
    @JoinColumn(name = "group_fk", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private PersonGroup groupFk;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "workersFk")
    private List<PersonalInterests> personalInterestsList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "workersFk")
    private List<PersonalProjectInterests> personalProjectInterestsList;
    @OneToMany(mappedBy = "workerFk")
    private List<RoleEvaluation> roleEvaluationList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "workersFk")
    private List<CompetenceValue> competenceValueList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "workerConflictFk")
    private List<WorkerConflict> workerConflictList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "workerFk")
    private List<WorkerConflict> workerConflictList1;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "workerFk")
    private WorkerTest workerTest;

    public Worker() {
    }

    public Worker(Long id) {
        this.id = id;
    }

    public Worker(Long id, String personName, String idCard, Character sex) {
        this.id = id;
        this.personName = personName;
        this.idCard = idCard;
        this.sex = sex;
    }

    public RoleExperience getRoleExperience(Long idRol) {
        RoleExperience xp = new RoleExperience();
        for (RoleExperience roleExperience : this.roleExperienceList) {
            if ((roleExperience.getRoleFk().getId()).equals(idRol)) {
                xp = roleExperience;
                break;
            }
        }
        return xp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getSurName() {
        return surName != null ? surName : "";
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Character getSex() {
        return sex;
    }

    public void setSex(Character sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getInDate() {
        return inDate;
    }

    public void setInDate(Date inDate) {
        this.inDate = inDate;
    }

    public Float getWorkload() {
        return workload;
    }

    public void setWorkload(Float workload) {
        this.workload = workload;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<AssignedRole> getAssignedRoleList() {
        return assignedRoleList;
    }

    public void setAssignedRoleList(List<AssignedRole> assignedRoleList) {
        this.assignedRoleList = assignedRoleList;
    }

    public List<RoleExperience> getRoleExperienceList() {
        return roleExperienceList;
    }

    public void setRoleExperienceList(List<RoleExperience> roleExperienceList) {
        this.roleExperienceList = roleExperienceList;
    }

    public County getCountyFk() {
        return countyFk;
    }

    public void setCountyFk(County countyFk) {
        this.countyFk = countyFk;
    }

    public PersonGroup getGroupFk() {
        return groupFk;
    }

    public void setGroupFk(PersonGroup groupFk) {
        this.groupFk = groupFk;
    }

    public List<PersonalInterests> getPersonalInterestsList() {
        return personalInterestsList;
    }

    public void setPersonalInterestsList(List<PersonalInterests> personalInterestsList) {
        this.personalInterestsList = personalInterestsList;
    }

    public List<PersonalProjectInterests> getPersonalProjectInterestsList() {
        return personalProjectInterestsList;
    }

    public void setPersonalProjectInterestsList(List<PersonalProjectInterests> personalProjectInterestsList) {
        this.personalProjectInterestsList = personalProjectInterestsList;
    }

    public List<RoleEvaluation> getRoleEvaluationList() {
        return roleEvaluationList;
    }

    public void setRoleEvaluationList(List<RoleEvaluation> roleEvaluationList) {
        this.roleEvaluationList = roleEvaluationList;
    }

    public List<CompetenceValue> getCompetenceValueList() {
        return competenceValueList;
    }

    public void setCompetenceValueList(List<CompetenceValue> competenceValueList) {
        this.competenceValueList = competenceValueList;
    }

    public List<WorkerConflict> getWorkerConflictList() {
        return workerConflictList;
    }

    public void setWorkerConflictList(List<WorkerConflict> workerConflictList) {
        this.workerConflictList = workerConflictList;
    }

    public List<WorkerConflict> getWorkerConflictList1() {
        return workerConflictList1;
    }

    public void setWorkerConflictList1(List<WorkerConflict> workerConflictList1) {
        this.workerConflictList1 = workerConflictList1;
    }

    public WorkerTest getWorkerTest() {
        return workerTest;
    }

    public void setWorkerTest(WorkerTest workerTest) {
        this.workerTest = workerTest;
    }

    public Integer getExperience() {
        return experience != null ? experience : 0;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public String getFullName() {
        String result = "";
        result += getPersonName();
        result += " " + getSurName();
        return result.trim();
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
        if (!(object instanceof Worker)) {
            return false;
        }
        Worker other = (Worker) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return "org.naredo.tesis.entity.Worker[ id=" + id + " ]";
    }

}
