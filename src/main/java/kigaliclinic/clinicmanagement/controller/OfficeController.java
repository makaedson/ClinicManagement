package kigaliclinic.clinicmanagement.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kigaliclinic.clinicmanagement.domain.Office;
import kigaliclinic.clinicmanagement.service.OfficeService;

@RestController
@RequestMapping("/api/office")
public class OfficeController {

    private final OfficeService officeService;

    public OfficeController(OfficeService officeService) {
        this.officeService = officeService;
    }

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveOffice(@RequestBody Office office) {
        String message = officeService.saveOffice(office);
        if (message.equals("Office is saved Successfully")) {
            return ResponseEntity.status(HttpStatus.CREATED).body(message);
        }
        if (message.contains("required") || message.contains("at most")) {
            return ResponseEntity.badRequest().body(message);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllOffices(@RequestParam(required = false) String search) {
        return ResponseEntity.ok(officeService.getAllOffices(search));
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getOfficeById(@PathVariable UUID id) {
        Office office = officeService.getOfficeById(id);
        if (office == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Office not found");
        }
        return ResponseEntity.ok(office);
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateOffice(@PathVariable UUID id, @RequestBody Office office) {
        String message = officeService.updateOffice(id, office);
        if (message.equals("Office is updated Successfully")) {
            return ResponseEntity.ok(message);
        }
        if (message.contains("not found")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        if (message.contains("required") || message.contains("allowed length")) {
            return ResponseEntity.badRequest().body(message);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
    }

    @PutMapping(value = "/{id}/disable", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> disableOffice(@PathVariable UUID id) {
        String message = officeService.disableOffice(id);
        if (message.equals("Office is disabled Successfully")) {
            return ResponseEntity.ok(message);
        }
        if (message.equals("Office not found")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteOffice(@PathVariable UUID id) {
        String message = officeService.deleteOffice(id);
        if (message.equals("Office is deleted Successfully")) {
            return ResponseEntity.ok(message);
        }
        if (message.contains("not found")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
    }
}
