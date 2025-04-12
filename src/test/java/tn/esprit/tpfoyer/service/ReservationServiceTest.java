package tn.esprit.tpfoyer.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.tpfoyer.entity.Reservation;
import tn.esprit.tpfoyer.repository.ReservationRepository;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRetrieveAllReservations() {
        List<Reservation> mockList = List.of(new Reservation(), new Reservation());
        when(reservationRepository.findAll()).thenReturn(mockList);

        List<Reservation> result = reservationService.retrieveAllReservations();

        assertEquals(2, result.size());
        verify(reservationRepository).findAll();
    }

    @Test
    void testRetrieveReservation() {
        Reservation r = new Reservation();
        r.setIdReservation("res1");
        when(reservationRepository.findById("res1")).thenReturn(Optional.of(r));

        Reservation result = reservationService.retrieveReservation("res1");

        assertNotNull(result);
        assertEquals("res1", result.getIdReservation());
        verify(reservationRepository).findById("res1");
    }

    @Test
    void testRetrieveReservation_NotFound() {
        when(reservationRepository.findById("res2")).thenReturn(Optional.empty());

        Reservation result = reservationService.retrieveReservation("res2");

        assertNull(result);
        verify(reservationRepository).findById("res2");
    }

    @Test
    void testAddReservation() {
        Reservation r = new Reservation();
        when(reservationRepository.save(r)).thenReturn(r);

        Reservation result = reservationService.addReservation(r);

        assertNotNull(result);
        verify(reservationRepository).save(r);
    }

    @Test
    void testRemoveReservation() {
        String id = "res1";
        doNothing().when(reservationRepository).deleteById(id);

        reservationService.removeReservation(id);

        verify(reservationRepository, times(1)).deleteById(id);
    }

    @Test
    void testFindByDateAndStatus() {
        Date date = new Date();
        boolean status = true;
        List<Reservation> reservations = List.of(new Reservation());

        when(reservationRepository.findAllByAnneeUniversitaireBeforeAndEstValide(date, status))
                .thenReturn(reservations);

        List<Reservation> result = reservationService.retrieveAllReservations();

        assertEquals(1, result.size());
        verify(reservationRepository).findAllByAnneeUniversitaireBeforeAndEstValide(date, status);
    }
}
