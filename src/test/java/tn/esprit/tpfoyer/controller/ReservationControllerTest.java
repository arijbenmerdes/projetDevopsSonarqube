package tn.esprit.tpfoyer.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tn.esprit.tpfoyer.control.ReservationRestController;
import tn.esprit.tpfoyer.entity.Reservation;
import tn.esprit.tpfoyer.service.IReservationService;

import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservationRestController.class)
@ExtendWith(MockitoExtension.class)
public class ReservationControllerTest {

    @MockBean  // ✅ Simule le service
    private IReservationService reservationService;

    @InjectMocks
    private ReservationRestController reservationRestController;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(reservationRestController).build();
    }

    @Test
    void testGetReservations() throws Exception {
        List<Reservation> mockList = List.of(new Reservation("res1", new Date(), true, null));

        when(reservationService.retrieveAllReservations()).thenReturn(mockList);

        mockMvc.perform(get("/reservation/retrieve-all-reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].idReservation").value("res1"));

        verify(reservationService).retrieveAllReservations();
    }

    @Test
    void testRetrieveReservation() throws Exception {
        Reservation mockReservation = new Reservation("res1", new Date(), true, null);

        when(reservationService.retrieveReservation("res1")).thenReturn(mockReservation);

        mockMvc.perform(get("/reservation/retrieve-reservation/res1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idReservation").value("res1"));

        verify(reservationService).retrieveReservation("res1");
    }

    @Test
    void testAddReservation() throws Exception {
        Reservation newReservation = new Reservation("res123", new Date(), true, null);
        when(reservationService.addReservation(any(Reservation.class))).thenReturn(newReservation);

        mockMvc.perform(post("/reservation/add-reservation")
                        .contentType("application/json")
                        .content("{\"idReservation\": \"res123\", \"anneeUniversitaire\": \"2024-01-01\", \"estValide\": true}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idReservation").value("res123"));

        verify(reservationService).addReservation(any(Reservation.class));
    }

    @Test
    void testRemoveReservation() throws Exception {
        doNothing().when(reservationService).removeReservation("res1");

        mockMvc.perform(delete("/reservation/remove-reservation/res1"))
                .andExpect(status().isOk());

        verify(reservationService).removeReservation("res1");
    }
}
