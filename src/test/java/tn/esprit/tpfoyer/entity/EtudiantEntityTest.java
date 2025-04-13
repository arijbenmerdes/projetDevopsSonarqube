package tn.esprit.tpfoyer.entity;

import org.junit.jupiter.api.Test;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class EtudiantEntityTest {

    @Test
    void testEtudiantConstructor() {
        Date testDate = new Date();
        Etudiant etudiant = new Etudiant(1L, "Arij", "Ben Merdes", 12345678L, testDate, null);

        assertEquals(1L, etudiant.getIdEtudiant());
        assertEquals("Arij", etudiant.getNomEtudiant());
        assertEquals("Ben Merdes", etudiant.getPrenomEtudiant());
        assertEquals(12345678L, etudiant.getCinEtudiant());
        assertEquals(testDate, etudiant.getDateNaissance());
    }

    @Test
    void testSettersAndGetters() {
        Etudiant etudiant = new Etudiant();
        etudiant.setIdEtudiant(2L);
        etudiant.setNomEtudiant("Etudiant2");
        etudiant.setPrenomEtudiant("Etudiant2");
        etudiant.setCinEtudiant(87654321L);
        etudiant.setDateNaissance(new Date());

        assertEquals(2L, etudiant.getIdEtudiant());
        assertEquals("Etudiant2", etudiant.getNomEtudiant());
        assertEquals("Etudiant2", etudiant.getPrenomEtudiant());
        assertEquals(87654321L, etudiant.getCinEtudiant());
        assertNotNull(etudiant.getDateNaissance());
    }

    @Test
    void testManyToManyRelation() {
        Etudiant etudiant = new Etudiant();
        Set<Reservation> reservations = Set.of(new Reservation("res1", new Date(), true, null),
                new Reservation("res2", new Date(), false, null));

        etudiant.setReservations(reservations);
        assertEquals(2, etudiant.getReservations().size());
    }
}
