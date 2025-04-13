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
import tn.esprit.tpfoyer.control.EtudiantRestController;
import tn.esprit.tpfoyer.entity.Etudiant;
import tn.esprit.tpfoyer.service.IEtudiantService;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EtudiantRestController.class)
@ExtendWith(MockitoExtension.class)
public class EtudiantControllerTest {

    @MockBean  // ✅ Simule le service
    private IEtudiantService etudiantService;

    @InjectMocks
    private EtudiantRestController etudiantRestController;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(etudiantRestController).build();
    }

    @Test
    void testGetEtudiants() throws Exception {
        List<Etudiant> mockList = List.of(new Etudiant(1L, "Arij", "Ben Merdes", 12345678L, null, null));

        when(etudiantService.retrieveAllEtudiants()).thenReturn(mockList);

        mockMvc.perform(get("/etudiant/retrieve-all-etudiants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].idEtudiant").value(1));

        verify(etudiantService).retrieveAllEtudiants();
    }

    @Test
    void testRetrieveEtudiantParCin() throws Exception {
        Etudiant mockEtudiant = new Etudiant(1L, "Arij", "Ben Merdes", 12345678L, null, null);
        when(etudiantService.recupererEtudiantParCin(12345678L)).thenReturn(mockEtudiant);

        mockMvc.perform(get("/etudiant/retrieve-etudiant-cin/12345678"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cinEtudiant").value(12345678L));

        verify(etudiantService).recupererEtudiantParCin(12345678L);
    }

    @Test
    void testRetrieveEtudiant() throws Exception {
        Etudiant mockEtudiant = new Etudiant(1L, "Arij", "Ben Merdes", 12345678L, null, null);
        when(etudiantService.retrieveEtudiant(1L)).thenReturn(mockEtudiant);

        mockMvc.perform(get("/etudiant/retrieve-etudiant/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEtudiant").value(1));

        verify(etudiantService).retrieveEtudiant(1L);
    }

    @Test
    void testAddEtudiant() throws Exception {
        Etudiant newEtudiant = new Etudiant(2L, "Etudiant2", "Etudiant2", 87654321L, null, null);
        when(etudiantService.addEtudiant(any(Etudiant.class))).thenReturn(newEtudiant);

        mockMvc.perform(post("/etudiant/add-etudiant")
                        .contentType("application/json")
                        .content("{\"idEtudiant\": 2, \"nomEtudiant\": \"Etudiant2\", \"prenomEtudiant\": \"Etudiant2\", \"cinEtudiant\": 87654321}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEtudiant").value(2));

        verify(etudiantService).addEtudiant(any(Etudiant.class));
    }

    @Test
    void testRemoveEtudiant() throws Exception {
        doNothing().when(etudiantService).removeEtudiant(1L);

        mockMvc.perform(delete("/etudiant/remove-etudiant/1"))
                .andExpect(status().isOk());

        verify(etudiantService).removeEtudiant(1L);
    }

    @Test
    void testModifyEtudiant() throws Exception {
        Etudiant updatedEtudiant = new Etudiant(1L, "EtudiantModifié", "EtudiantModifié", 98765432L, null, null);
        when(etudiantService.modifyEtudiant(any(Etudiant.class))).thenReturn(updatedEtudiant);

        mockMvc.perform(put("/etudiant/modify-etudiant")
                        .contentType("application/json")
                        .content("{\"idEtudiant\": 1, \"nomEtudiant\": \"EtudiantModifié\", \"prenomEtudiant\": \"EtudiantModifié\", \"cinEtudiant\": 98765432}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomEtudiant").value("EtudiantModifié"));

        verify(etudiantService).modifyEtudiant(any(Etudiant.class));
    }
}
