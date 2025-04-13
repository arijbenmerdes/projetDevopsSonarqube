package tn.esprit.tpfoyer.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.tpfoyer.entity.Etudiant;
import tn.esprit.tpfoyer.repository.EtudiantRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;  // ✅ Import de JUnit
import static org.mockito.Mockito.*;  // ✅ Import de Mockito

@ExtendWith(MockitoExtension.class)  // ✅ Active JUnit et Mockito
public class EtudiantServiceTest {

    @Mock  // ✅ Simule la base de données
    private EtudiantRepository etudiantRepository;

    @InjectMocks  // ✅ Injecte le mock dans le service
    private EtudiantServiceImpl etudiantService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRetrieveAllEtudiants() {
        List<Etudiant> mockList = List.of(new Etudiant(1L, "Arij", "Ben Merdes", 12345678L, null, null));
        when(etudiantRepository.findAll()).thenReturn(mockList);

        List<Etudiant> result = etudiantService.retrieveAllEtudiants();

        assertEquals(1, result.size());  // ✅ JUnit vérifie la taille de la liste
        verify(etudiantRepository).findAll();
    }

    @Test
    void testRetrieveEtudiant() {
        Etudiant mockEtudiant = new Etudiant(1L, "Arij", "Ben Merdes", 12345678L, null, null);
        when(etudiantRepository.findById(1L)).thenReturn(Optional.of(mockEtudiant));

        Etudiant result = etudiantService.retrieveEtudiant(1L);

        assertNotNull(result);  // ✅ Vérification JUnit
        assertEquals(1L, result.getIdEtudiant());  // ✅ Vérification JUnit
        assertEquals("Arij", result.getNomEtudiant());  // ✅ Vérification JUnit
        verify(etudiantRepository).findById(1L);
    }

    @Test
    void testRetrieveEtudiant_NotFound() {
        when(etudiantRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            etudiantService.retrieveEtudiant(2L);
        });

        assertEquals("Etudiant non trouvé !", exception.getMessage());  // ✅ JUnit vérifie le message d'erreur
        verify(etudiantRepository).findById(2L);
    }

    @Test
    void testAddEtudiant() {
        Etudiant newEtudiant = new Etudiant(2L, "Etudiant2", "Etudiant2", 87654321L, null, null);
        when(etudiantRepository.save(newEtudiant)).thenReturn(newEtudiant);

        Etudiant result = etudiantService.addEtudiant(newEtudiant);

        assertNotNull(result);
        assertEquals("Etudiant2", result.getNomEtudiant());
        verify(etudiantRepository).save(newEtudiant);
    }

    @Test
    void testRemoveEtudiant() {
        Long id = 1L;
        doNothing().when(etudiantRepository).deleteById(id);

        etudiantService.removeEtudiant(id);

        verify(etudiantRepository, times(1)).deleteById(id);
    }

    @Test
    void testRecupererEtudiantParCin() {
        Etudiant mockEtudiant = new Etudiant(3L, "Etudiant3", "Etudiant3", 98765432L, null, null);
        when(etudiantRepository.findEtudiantByCinEtudiant(98765432L)).thenReturn(mockEtudiant);

        Etudiant result = etudiantService.recupererEtudiantParCin(98765432L);

        assertNotNull(result);
        assertEquals(98765432L, result.getCinEtudiant());
        verify(etudiantRepository).findEtudiantByCinEtudiant(98765432L);
    }
}
