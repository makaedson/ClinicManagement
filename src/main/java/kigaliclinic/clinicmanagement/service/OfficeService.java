package kigaliclinic.clinicmanagement.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import kigaliclinic.clinicmanagement.domain.Office;
import kigaliclinic.clinicmanagement.repository.OfficeRepository;

@Service
public class OfficeService {

    private final OfficeRepository officeRepo;

    public OfficeService(OfficeRepository officeRepo) {
        this.officeRepo = officeRepo;
    }

    public String saveOffice(Office office) {
        if (office.getName() == null || office.getName().isBlank()) {
            return "Office name is required";
        }
        if (office.getOfficeNumber() == null || office.getOfficeNumber().isBlank()) {
            return "Office number is required";
        }
        if (office.getName().trim().length() > 100) {
            return "Office name must be at most 100 characters";
        }
        if (office.getOfficeNumber().trim().length() > 20) {
            return "Office number must be at most 20 characters";
        }

        office.setName(office.getName().trim());
        office.setOfficeNumber(office.getOfficeNumber().trim());
        if (officeRepo.findByOfficeNumberIgnoreCase(office.getOfficeNumber()) != null) {
            return "Office with that number " + office.getOfficeNumber() + " already exists";
        }

        office.setActive(true);
        officeRepo.save(office);
        return "Office is saved Successfully";
    }

    public List<Office> getAllOffices(String search) {
        if (search == null || search.isBlank()) {
            return officeRepo.findAll();
        }
        String query = search.trim();
        return officeRepo.findByNameContainingIgnoreCaseOrOfficeNumberContainingIgnoreCase(query, query);
    }

    public Office getOfficeById(UUID id) {
        return officeRepo.findById(id).orElse(null);
    }

    public String updateOffice(UUID id, Office newOffice) {
        Office office = officeRepo.findById(id).orElse(null);
        if (office == null) {
            return "Office not found";
        }

        if (newOffice.getName() == null || newOffice.getName().isBlank()
            || newOffice.getOfficeNumber() == null || newOffice.getOfficeNumber().isBlank()) {
            return "Office name and number are required";
        }
        if (newOffice.getName().trim().length() > 100 || newOffice.getOfficeNumber().trim().length() > 20) {
            return "Office name or number exceeds the allowed length";
        }

        String officeNumber = newOffice.getOfficeNumber().trim();
        Office sameNumber = officeRepo.findByOfficeNumberIgnoreCase(officeNumber);
        if (sameNumber != null && !sameNumber.getId().equals(id)) {
            return "Office with that number " + officeNumber + " already exists";
        }

        office.setName(newOffice.getName().trim());
        office.setOfficeNumber(officeNumber);
        officeRepo.save(office);
        return "Office is updated Successfully";
    }

    public String disableOffice(UUID id) {
        Office office = officeRepo.findById(id).orElse(null);
        if (office == null) {
            return "Office not found";
        }
        if (!office.isActive()) {
            return "Office is already disabled";
        }

        office.setActive(false);
        officeRepo.save(office);
        return "Office is disabled Successfully";
    }

    public String deleteOffice(UUID id) {
        Office office = officeRepo.findById(id).orElse(null);
        if (office == null) {
            return "Office not found";
        }
        if (office.getDoctor() != null) {
            return "Office is assigned to a doctor and cannot be deleted";
        }
        officeRepo.delete(office);
        return "Office is deleted Successfully";
    }
}
