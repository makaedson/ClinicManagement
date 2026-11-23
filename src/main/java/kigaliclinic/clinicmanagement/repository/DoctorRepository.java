package kigaliclinic.clinicmanagement.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kigaliclinic.clinicmanagement.domain.Doctor;

@Repository 
public interface DoctorRepository extends JpaRepository<Doctor, UUID> {

    Doctor findByOfficeId(UUID officeId);
}
