package com.baeldung.activitispringdemo.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.baeldung.activitispringdemo.activiti.BusinessClassified;
import com.baeldung.activitispringdemo.activiti.CedingApplicationStatus;

@Entity
@Table(name="ceding_application")
public class CedingApplication implements Serializable {

    private static final long serialVersionUID = 5596023413053405664L;

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="ceding_application_id")
    private Long id;

    @Column(name="facultative_code", nullable=false, length=30)
    private String facultativeCode;

    @Enumerated(EnumType.STRING)
    @Column(name="business_classified", nullable=false, length=100)
    private BusinessClassified classified;

    @Enumerated(EnumType.STRING)
    @Column(name="application_status", nullable=false, length=100)
    private CedingApplicationStatus status;

    @Column(name="share_in_percent", nullable=false)
    private Double shareInPercent;

    @Column(name="amount", nullable=false)
    private BigDecimal amount;

    @ManyToOne(fetch=FetchType.LAZY, targetEntity=Ceding.class, 
            cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name="ceding_id", nullable=false)
    private Ceding ceding;

    public CedingApplication() {}

    public CedingApplication(String facultativeCode, BusinessClassified classified, 
            CedingApplicationStatus status, Double shareInPercent, 
            BigDecimal amount, Ceding ceding) {
        super();
        this.facultativeCode = facultativeCode;
        this.classified = classified;
        this.status = status;
        this.shareInPercent = shareInPercent;
        this.amount = amount;
        this.ceding = ceding;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFacultativeCode() {
        return facultativeCode;
    }

    public void setFacultativeCode(String facultativeCode) {
        this.facultativeCode = facultativeCode;
    }

    public BusinessClassified getClassified() {
        return classified;
    }

    public void setClassified(BusinessClassified classified) {
        this.classified = classified;
    }

    public CedingApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(CedingApplicationStatus status) {
        this.status = status;
    }

    public Double getShareInPercent() {
        return shareInPercent;
    }

    public void setShareInPercent(Double shareInPercent) {
        this.shareInPercent = shareInPercent;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Ceding getCeding() {
        return ceding;
    }

    public void setCeding(Ceding ceding) {
        this.ceding = ceding;
    }

}
