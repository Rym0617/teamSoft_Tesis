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
@Table(name = "person_group")
@NamedQueries({
    @NamedQuery(name = "PersonGroup.findAll", query = "SELECT p FROM PersonGroup p")
    , @NamedQuery(name = "PersonGroup.findById", query = "SELECT p FROM PersonGroup p WHERE p.id = :id")
    , @NamedQuery(name = "PersonGroup.findByName", query = "SELECT p FROM PersonGroup p WHERE p.name = :name")})
public class PersonGroup implements Serializable {

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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "groupFk")
    private List<Worker> workerList;
    @OneToMany(mappedBy = "parentGroup")
    private List<PersonGroup> personGroupList;
    @JoinColumn(name = "parent_group", referencedColumnName = "id")
    @ManyToOne
    private PersonGroup parentGroup;

    public PersonGroup() {
    }

    public PersonGroup(Long id) {
        this.id = id;
    }

    public PersonGroup(Long id, String name) {
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

    public List<Worker> getWorkerList() {
        return workerList;
    }

    public void setWorkerList(List<Worker> workerList) {
        this.workerList = workerList;
    }

    public List<PersonGroup> getPersonGroupList() {
        return personGroupList;
    }

    public void setPersonGroupList(List<PersonGroup> personGroupList) {
        this.personGroupList = personGroupList;
    }

    public PersonGroup getParentGroup() {
        return parentGroup;
    }

    public void setParentGroup(PersonGroup parentGroup) {
        this.parentGroup = parentGroup;
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
        if (!(object instanceof PersonGroup)) {
            return false;
        }
        PersonGroup other = (PersonGroup) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.naredo.tesis.entity.PersonGroup[ id=" + id + " ]";
    }
    
}
