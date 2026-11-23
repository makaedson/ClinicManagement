package kigaliclinic.clinicmanagement.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigaliclinic.clinicmanagement.domain.Doctor;
import kigaliclinic.clinicmanagement.domain.Office;
import kigaliclinic.clinicmanagement.repository.DoctorRepository;
import kigaliclinic.clinicmanagement.repository.OfficeRepository;

@Service 
public class DoctorService {
    

    @Autowired 
    private DoctorRepository doctorRepo;

    @Autowired 
    private OfficeRepository officeRepo;


    public String saveDoctor(Doctor doctor){

        if(doctor.getOffice() != null){

            if(doctor.getOffice().getId() == null){
                return "Office not found";
            }

            Office office = officeRepo.findById(doctor.getOffice().getId()).orElse(null);

            if(office == null){
                return "Office not found";
            }

            if(doctorRepo.findByOfficeId(office.getId()) != null){
                return "Office is already assigned to another doctor";
            }

            doctor.setOffice(office);
        }

        doctorRepo.save(doctor);

        return "Doctor is saved Successfully";
    }


    public List<Doctor> getAllDoctors(){
        return doctorRepo.findAll();
    }


    public Doctor getDoctorById(UUID id){
        return doctorRepo.findById(id).orElse(null);
    }


    public String updateDoctor(UUID id, Doctor newDoctor){

        Doctor doctor = doctorRepo.findById(id).orElse(null);

        if(doctor == null){
            return "Doctor not found";
        }

        Office office = null;

        if(newDoctor.getOffice() != null){

            if(newDoctor.getOffice().getId() == null){
                return "Office not found";
            }

            office = officeRepo.findById(newDoctor.getOffice().getId()).orElse(null);

            if(office == null){
                return "Office not found";
            }

            Doctor officeOwner = doctorRepo.findByOfficeId(office.getId());

            if(officeOwner != null && !officeOwner.getId().equals(id)){
                return "Office is already assigned to another doctor";
            }
        }

        doctor.setFirstName(newDoctor.getFirstName());
        doctor.setLastName(newDoctor.getLastName());
        doctor.setDateOfBirth(newDoctor.getDateOfBirth());
        doctor.setOffice(office);
        doctorRepo.save(doctor);

        return "Doctor is updated Successfully";
    }


    public String deleteDoctor(UUID id){

        Doctor doctor = doctorRepo.findById(id).orElse(null);

        if(doctor == null){
            return "Doctor not found";
        }

        if(!doctor.getAppointments().isEmpty()){
            return "Doctor has appointments and cannot be deleted";
        }

        doctorRepo.delete(doctor);

        return "Doctor is deleted Successfully";
    }
}
