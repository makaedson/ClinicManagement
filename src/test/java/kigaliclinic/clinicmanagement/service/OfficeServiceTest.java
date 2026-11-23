package kigaliclinic.clinicmanagement.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import kigaliclinic.clinicmanagement.domain.Office;
import kigaliclinic.clinicmanagement.repository.OfficeRepository;

class OfficeServiceTest {

    private final OfficeRepository officeRepository = mock(OfficeRepository.class);
    private final OfficeService officeService = new OfficeService(officeRepository);

    @Test
    void registersTrimmedOfficeAsActive() {
        when(officeRepository.findByOfficeNumberIgnoreCase("204")).thenReturn(null);
        Office office = new Office(" Consultation Room ", " 204 ");

        assertEquals("Office is saved Successfully", officeService.saveOffice(office));
        assertEquals("Consultation Room", office.getName());
        assertEquals("204", office.getOfficeNumber());
        assertTrue(office.isActive());
        verify(officeRepository).save(office);
    }

    @Test
    void rejectsDuplicateOfficeNumber() {
        when(officeRepository.findByOfficeNumberIgnoreCase("204"))
            .thenReturn(new Office("Existing room", "204"));

        assertTrue(officeService.saveOffice(new Office("New room", "204")).contains("already exists"));
    }

    @Test
    void disablesOfficeWithoutDeletingIt() {
        UUID id = UUID.randomUUID();
        Office office = new Office("Consultation Room", "204");
        office.setId(id);
        when(officeRepository.findById(id)).thenReturn(Optional.of(office));

        assertEquals("Office is disabled Successfully", officeService.disableOffice(id));
        assertFalse(office.isActive());
        verify(officeRepository).save(office);
    }
}
