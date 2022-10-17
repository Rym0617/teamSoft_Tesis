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
 * @author yoyo
 */
@Entity
@Table(name = "personal_interests")
@NamedQueries({
        @NamedQuery(name = "PersonalInterests.findAll", query = "SELECT p FROM PersonalInterests p")
        , @NamedQuery(name = "PersonalInterests.findByPreference", query = "SELECT p FROM PersonalInterests p WHERE p.preference = :preference")
        , @NamedQuery(name = "PersonalInterests.findByRoleAndWorker", query = "SELECT p FROM PersonalInterests p WHERE p.workersFk = :worker and p.rolesFk = :role")
        , @NamedQuery(name = "PersonalInterests.deleteByWorker", query = "delete from PersonalInterests p where p.workersFk = :workerId")
        , @NamedQuery(name = "PersonalInterests.findById", query = "SELECT p FROM PersonalInterests p WHERE p.id = :id")})
public class PersonalInterests implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Column(name = "preference")
    private boolean preference;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CUST_SEQ")
    @SequenceGenerator(sequenceName = "hibernate_sequence", allocationSize = 1, name = "CUST_SEQ")
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "roles_fk", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Role rolesFk;
    @JoinColumn(name = "workers_fk", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Worker workersFk;

    public PersonalInterests() {
    }

    public PersonalInterests(Long id) {
        this.id = id;
    }

    public PersonalInterests(Long id, boolean preference) {
        this.id = id;
        this.preference = preference;
    }

    public boolean getPreference() {
        return preference;
    }

    public void setPreference(boolean preference) {
        this.preference = preference;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(object instanceof PersonalInterests)) {
            return false;
        }
        PersonalInterests other = (PersonalInterests) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.naredo.tesis.entity.PersonalInterests[ id=" + id + " ]";
    }

}
