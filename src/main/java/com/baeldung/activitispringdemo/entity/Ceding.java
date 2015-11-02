package com.baeldung.activitispringdemo.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="ceding")
public class Ceding implements Serializable {

    private static final long serialVersionUID = -7270163188563737527L;

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="ceding_id")
    private Long id;

    @Column(name="ceding_name", nullable=false, length=150)
    private String name;

    @Column(name="ceding_address", nullable=false)
    private String address;

    @Column(name="ceding_email", nullable=false, length=75)
    private String email;

    @Column(name="phone_number", nullable=false, length=25)
    private String phone;

    @OneToMany(fetch=FetchType.LAZY, mappedBy="ceding", 
            cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private Set<CedingApplication> applications;

    public Ceding() {}

    public Ceding(String name, String address, String email, String phone) {
        super();
        this.name = name;
        this.address = address;
        this.email = email;
        this.phone = phone;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Set<CedingApplication> getApplications() {
        return applications;
    }

    public void setApplications(Set<CedingApplication> applications) {
        this.applications = applications;
    }

}
