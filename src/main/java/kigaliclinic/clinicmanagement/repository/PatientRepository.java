package kigaliclinic.clinicmanagement.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kigaliclinic.clinicmanagement.domain.Patient;

@Repository 
public interface PatientRepository extends JpaRepository<Patient, UUID> {

}
