package tn.esprit.tpfoyer.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;
import java.util.Set;

public class ReservationEntityTest {

    @Test
    void testReservationConstructor() {
        Date testDate = new Date();
        Reservation reservation = new Reservation("res123", testDate, true, null);

        assertEquals("res123", reservation.getIdReservation());
        assertEquals(testDate, reservation.getAnneeUniversitaire());
        assertTrue(reservation.isEstValide());
    }

    @Test
    void testSettersAndGetters() {
        Reservation reservation = new Reservation();
        reservation.setIdReservation("res456");
        reservation.setAnneeUniversitaire(new Date());
        reservation.setEstValide(false);

        assertEquals("res456", reservation.getIdReservation());
        assertNotNull(reservation.getAnneeUniversitaire());
        assertFalse(reservation.isEstValide());
    }

    @Test
    void testManyToManyRelation() {
        Reservation reservation = new Reservation();
        Set<Etudiant> etudiants = Set.of(new Etudiant(), new Etudiant());

        reservation.setEtudiants(etudiants);
        assertEquals(2, reservation.getEtudiants().size());
    }
}
