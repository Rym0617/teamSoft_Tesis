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
@Table(name = "client_entity")
@NamedQueries({
    @NamedQuery(name = "ClientEntity.findAll", query = "SELECT c FROM ClientEntity c")
    , @NamedQuery(name = "ClientEntity.findById", query = "SELECT c FROM ClientEntity c WHERE c.id = :id")
    , @NamedQuery(name = "ClientEntity.findByEntityName", query = "SELECT c FROM ClientEntity c WHERE c.entityName = :entityName")
    , @NamedQuery(name = "ClientEntity.findByAddress", query = "SELECT c FROM ClientEntity c WHERE c.address = :address")
    , @NamedQuery(name = "ClientEntity.findByPhone", query = "SELECT c FROM ClientEntity c WHERE c.phone = :phone")})
public class ClientEntity implements Serializable {

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
    @Column(name = "entity_name")
    private String entityName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "address")
    private String address;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "phone")
    private String phone;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clientEntityFk")
    private List<Project> projectList;

    public ClientEntity() {
    }

    public ClientEntity(Long id) {
        this.id = id;
    }

    public ClientEntity(Long id, String entityName, String address, String phone) {
        this.id = id;
        this.entityName = entityName;
        this.address = address;
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
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

    public List<Project> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<Project> projectList) {
        this.projectList = projectList;
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
        if (!(object instanceof ClientEntity)) {
            return false;
        }
        ClientEntity other = (ClientEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.naredo.tesis.entity.ClientEntity[ id=" + id + " ]";
    }
    
}
