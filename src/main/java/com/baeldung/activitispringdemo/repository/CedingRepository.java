package com.baeldung.activitispringdemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.baeldung.activitispringdemo.entity.Ceding;

@Repository
public interface CedingRepository extends JpaRepository<Ceding, Long> {
}
