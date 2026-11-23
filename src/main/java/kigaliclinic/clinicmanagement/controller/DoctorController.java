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

import kigaliclinic.clinicmanagement.domain.Doctor;
import kigaliclinic.clinicmanagement.service.DoctorService;



@RestController 
@RequestMapping (value = "/api/doctor")
public class DoctorController {
    

    @Autowired 
    private DoctorService doctorServes;


    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces  = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?>  saveDoctor(@RequestBody  Doctor doctor){

        String returnedMessage = doctorServes.saveDoctor(doctor);

        if(returnedMessage.equalsIgnoreCase("Doctor is saved Successfully")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.CREATED);
        }else if(returnedMessage.contains("not found")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<>(returnedMessage, HttpStatus.CONFLICT);
        }
        
    }


    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllDoctors(){

        return new ResponseEntity<>(doctorServes.getAllDoctors(), HttpStatus.OK);
    }


    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getDoctorById(@PathVariable UUID id){

        Doctor doctor = doctorServes.getDoctorById(id);

        if(doctor == null){
            return new ResponseEntity<>("Doctor not found", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(doctor, HttpStatus.OK);
    }


    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces  = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> updateDoctor(@PathVariable UUID id, @RequestBody Doctor doctor){

        String returnedMessage = doctorServes.updateDoctor(id, doctor);

        if(returnedMessage.equalsIgnoreCase("Doctor is updated Successfully")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        }else if(returnedMessage.contains("not found")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<>(returnedMessage, HttpStatus.CONFLICT);
        }

    }


    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteDoctor(@PathVariable UUID id){

        String returnedMessage = doctorServes.deleteDoctor(id);

        if(returnedMessage.equalsIgnoreCase("Doctor is deleted Successfully")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        }else if(returnedMessage.contains("not found")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<>(returnedMessage, HttpStatus.CONFLICT);
        }

    }


}
