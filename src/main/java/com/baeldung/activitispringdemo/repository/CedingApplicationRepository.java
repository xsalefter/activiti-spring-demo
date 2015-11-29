package com.baeldung.activitispringdemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.baeldung.activitispringdemo.entity.CedingApplication;

@Repository
public interface CedingApplicationRepository 
extends JpaRepository<CedingApplication, Long> {

    CedingApplication findByFacultativeCode(final String facultativeCode);
}
