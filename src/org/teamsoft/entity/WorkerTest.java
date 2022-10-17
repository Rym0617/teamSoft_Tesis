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

/**
 *
 * @author yoyo
 */
@Entity
@Table(name = "worker_test")
@NamedQueries({
    @NamedQuery(name = "WorkerTest.findAll", query = "SELECT w FROM WorkerTest w")
    , @NamedQuery(name = "WorkerTest.findById", query = "SELECT w FROM WorkerTest w WHERE w.id = :id")
    , @NamedQuery(name = "WorkerTest.findByES", query = "SELECT w FROM WorkerTest w WHERE w.eS = :eS")
    , @NamedQuery(name = "WorkerTest.findByID", query = "SELECT w FROM WorkerTest w WHERE w.iD = :iD")
    , @NamedQuery(name = "WorkerTest.findByCO", query = "SELECT w FROM WorkerTest w WHERE w.cO = :cO")
    , @NamedQuery(name = "WorkerTest.findByIS", query = "SELECT w FROM WorkerTest w WHERE w.iS = :iS")
    , @NamedQuery(name = "WorkerTest.findByCE", query = "SELECT w FROM WorkerTest w WHERE w.cE = :cE")
    , @NamedQuery(name = "WorkerTest.findByIR", query = "SELECT w FROM WorkerTest w WHERE w.iR = :iR")
    , @NamedQuery(name = "WorkerTest.findByME", query = "SELECT w FROM WorkerTest w WHERE w.mE = :mE")
    , @NamedQuery(name = "WorkerTest.findByCH", query = "SELECT w FROM WorkerTest w WHERE w.cH = :cH")
    , @NamedQuery(name = "WorkerTest.findByIF", query = "SELECT w FROM WorkerTest w WHERE w.iF = :iF")
    , @NamedQuery(name = "WorkerTest.findByTipoMB", query = "SELECT w FROM WorkerTest w WHERE w.tipoMB = :tipoMB")})
public class WorkerTest implements Serializable {

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
    @Column(name = "e_s")
    private Character eS;
    @Basic(optional = false)
    @NotNull
    @Column(name = "i_d")
    private Character iD;
    @Basic(optional = false)
    @NotNull
    @Column(name = "c_o")
    private Character cO;
    @Basic(optional = false)
    @NotNull
    @Column(name = "i_s")
    private Character iS;
    @Basic(optional = false)
    @NotNull
    @Column(name = "c_e")
    private Character cE;
    @Basic(optional = false)
    @NotNull
    @Column(name = "i_r")
    private Character iR;
    @Basic(optional = false)
    @NotNull
    @Column(name = "m_e")
    private Character mE;
    @Basic(optional = false)
    @NotNull
    @Column(name = "c_h")
    private Character cH;
    @Basic(optional = false)
    @NotNull
    @Column(name = "i_f")
    private Character iF;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "tipo_m_b")
    private String tipoMB;
    @JoinColumn(name = "worker_fk", referencedColumnName = "id")
    @OneToOne(optional = false)
    private Worker workerFk;

    public WorkerTest() {
    }

    public WorkerTest(Long id) {
        this.id = id;
    }

    public WorkerTest(Long id, Character eS, Character iD, Character cO, Character iS, Character cE, Character iR, Character mE, Character cH, Character iF, String tipoMB) {
        this.id = id;
        this.eS = eS;
        this.iD = iD;
        this.cO = cO;
        this.iS = iS;
        this.cE = cE;
        this.iR = iR;
        this.mE = mE;
        this.cH = cH;
        this.iF = iF;
        this.tipoMB = tipoMB;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Character getES() {
        return eS;
    }

    public void setES(Character eS) {
        this.eS = eS;
    }

    public Character getID() {
        return iD;
    }

    public void setID(Character iD) {
        this.iD = iD;
    }

    public Character getCO() {
        return cO;
    }

    public void setCO(Character cO) {
        this.cO = cO;
    }

    public Character getIS() {
        return iS;
    }

    public void setIS(Character iS) {
        this.iS = iS;
    }

    public Character getCE() {
        return cE;
    }

    public void setCE(Character cE) {
        this.cE = cE;
    }

    public Character getIR() {
        return iR;
    }

    public void setIR(Character iR) {
        this.iR = iR;
    }

    public Character getME() {
        return mE;
    }

    public void setME(Character mE) {
        this.mE = mE;
    }

    public Character getCH() {
        return cH;
    }

    public void setCH(Character cH) {
        this.cH = cH;
    }

    public Character getIF() {
        return iF;
    }

    public void setIF(Character iF) {
        this.iF = iF;
    }

    public String getTipoMB() {
        return tipoMB;
    }

    public void setTipoMB(String tipoMB) {
        this.tipoMB = tipoMB;
    }

    public Worker getWorkerFk() {
        return workerFk;
    }

    public void setWorkerFk(Worker workerFk) {
        this.workerFk = workerFk;
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
        if (!(object instanceof WorkerTest)) {
            return false;
        }
        WorkerTest other = (WorkerTest) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.naredo.tesis.entity.WorkerTest[ id=" + id + " ]";
    }
    
}
