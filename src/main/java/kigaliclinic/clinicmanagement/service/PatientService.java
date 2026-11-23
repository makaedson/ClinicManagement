package kigaliclinic.clinicmanagement.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigaliclinic.clinicmanagement.domain.Patient;
import kigaliclinic.clinicmanagement.repository.PatientRepository;

@Service 
public class PatientService {
    

    @Autowired 
    private PatientRepository patientRepo;


    public String savePatient(Patient patient){

        patientRepo.save(patient);

        return "Patient is saved Successfully";
    }


    public List<Patient> getAllPatients(){
        return patientRepo.findAll();
    }


    public Patient getPatientById(UUID id){
        return patientRepo.findById(id).orElse(null);
    }


    public String updatePatient(UUID id, Patient newPatient){

        Patient patient = patientRepo.findById(id).orElse(null);

        if(patient == null){
            return "Patient not found";
        }

        patient.setFirstName(newPatient.getFirstName());
        patient.setLastName(newPatient.getLastName());
        patient.setDateOfBirth(newPatient.getDateOfBirth());
        patientRepo.save(patient);

        return "Patient is updated Successfully";
    }


    public String deletePatient(UUID id){

        Patient patient = patientRepo.findById(id).orElse(null);

        if(patient == null){
            return "Patient not found";
        }

        if(!patient.getAppointments().isEmpty()){
            return "Patient has appointments and cannot be deleted";
        }

        patientRepo.delete(patient);

        return "Patient is deleted Successfully";
    }
}
