package kigaliclinic.clinicmanagement.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kigaliclinic.clinicmanagement.domain.Patient;
import kigaliclinic.clinicmanagement.service.PatientService;



@RestController 
@RequestMapping (value = "/api/patient")
public class PatientController {
    

    @Autowired 
    private PatientService patientServes;


    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces  = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?>  savePatient(@RequestBody  Patient patient){

        String returnedMessage = patientServes.savePatient(patient);

        if(returnedMessage.equalsIgnoreCase("Patient is saved Successfully")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.CREATED);
        }else if(returnedMessage.contains("not found")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<>(returnedMessage, HttpStatus.CONFLICT);
        }
        
    }


    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllPatients(){

        return new ResponseEntity<>(patientServes.getAllPatients(), HttpStatus.OK);
    }


    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPatientById(@PathVariable UUID id){

        Patient patient = patientServes.getPatientById(id);

        if(patient == null){
            return new ResponseEntity<>("Patient not found", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(patient, HttpStatus.OK);
    }


    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces  = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> updatePatient(@PathVariable UUID id, @RequestBody Patient patient){

        String returnedMessage = patientServes.updatePatient(id, patient);

        if(returnedMessage.equalsIgnoreCase("Patient is updated Successfully")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        }else if(returnedMessage.contains("not found")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<>(returnedMessage, HttpStatus.CONFLICT);
        }

    }


    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deletePatient(@PathVariable UUID id){

        String returnedMessage = patientServes.deletePatient(id);

        if(returnedMessage.equalsIgnoreCase("Patient is deleted Successfully")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        }else if(returnedMessage.contains("not found")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<>(returnedMessage, HttpStatus.CONFLICT);
        }

    }


}
