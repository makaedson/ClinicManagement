package kigaliclinic.clinicmanagement.repository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kigaliclinic.clinicmanagement.domain.Office;

@Repository
public interface OfficeRepository extends JpaRepository<Office, UUID> {

    Office findByOfficeNumberIgnoreCase(String officeNumber);

    List<Office> findByNameContainingIgnoreCaseOrOfficeNumberContainingIgnoreCase(
        String name, String officeNumber);
}
