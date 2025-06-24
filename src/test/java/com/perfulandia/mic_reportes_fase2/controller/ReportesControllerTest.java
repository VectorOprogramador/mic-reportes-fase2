package com.perfulandia.mic_reportes_fase2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perfulandia.mic_reportes_fase2.model.Reportes;
import com.perfulandia.mic_reportes_fase2.service.ReportesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Clase de pruebas unitarias para {@link ReportesController}.
 * Utiliza {@code @WebMvcTest} para enfocar las pruebas en la capa web
 * y {@code @MockBean} para simular el servicio de reportes.
 */
@WebMvcTest(ReportesController.class) // Anotación para probar el controlador en un contexto MVC
class ReportesControllerTest {

    @Autowired
    private MockMvc mockMvc; // Objeto para realizar solicitudes HTTP simuladas

    @MockBean // Crea un mock del servicio para inyectarlo en el controlador
    private ReportesService reportesService;

    @Autowired
    private ObjectMapper objectMapper; // Utilidad para convertir objetos Java a JSON y viceversa

    /**
     * Prueba para el endpoint GET /api/v1/reportes cuando hay reportes.
     * Verifica que se retorna un estado HTTP 200 OK y la lista de reportes.
     */
    @Test
    void testGetAllReportes() throws Exception {
        // Arrange
        Reportes reporte1 = new Reportes(1L, "Ventas", "user1", "Reporte de ventas", Date.valueOf("2023-01-01"));
        Reportes reporte2 = new Reportes(2L, "Inventario", "user2", "Reporte de stock", Date.valueOf("2023-01-02"));
        // Configurar el mock del servicio para que devuelva una lista de reportes
        when(reportesService.findAll()).thenReturn(Arrays.asList(reporte1, reporte2));

        // Act & Assert
        mockMvc.perform(get("/api/v1/reportes")) // Realiza una solicitud GET a la URL
                .andExpect(status().isOk()) // Espera un estado HTTP 200 OK
                .andExpect(jsonPath("$[0].id").value(1L)) // Verifica el ID del primer reporte
                .andExpect(jsonPath("$[0].id_areaReporte").value("Ventas")) // Verifica el id_areaReporte del primer reporte
                .andExpect(jsonPath("$[1].id").value(2L)) // Verifica el ID del segundo reporte
                .andExpect(jsonPath("$[1].id_areaReporte").value("Inventario")); // Verifica el id_areaReporte del segundo reporte

        // Verify
        verify(reportesService).findAll(); // Verifica que el método findAll() del servicio fue llamado
    }

    /**
     * Prueba para el endpoint GET /api/v1/reportes cuando no hay reportes.
     * Verifica que se retorna un estado HTTP 204 NO_CONTENT.
     */
    @Test
    void testGetAllReportesNoContent() throws Exception {
        // Arrange
        // Configurar el mock del servicio para que devuelva una lista vacía
        when(reportesService.findAll()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/v1/reportes"))
                .andExpect(status().isNoContent()); // Espera un estado HTTP 204 NO_CONTENT

        // Verify
        verify(reportesService).findAll();
    }

    /**
     * Prueba para el endpoint GET /api/v1/reportes/{id} cuando el reporte existe.
     * Verifica que se retorna un estado HTTP 200 OK y el reporte específico.
     */
    @Test
    void testGetReportesByIdExistente() throws Exception {
        // Arrange
        Long reporteId = 1L;
        Reportes reporte = new Reportes(reporteId, "Ventas", "user1", "Reporte de ventas", Date.valueOf("2023-01-01"));

        // Configurar el mock del servicio
        when(reportesService.existsById(reporteId)).thenReturn(true);
        when(reportesService.findById(reporteId)).thenReturn(Optional.of(reporte));

        // Act & Assert
        mockMvc.perform(get("/api/v1/reportes/{id}", reporteId))
                .andExpect(status().isOk()) // Espera un estado HTTP 200 OK
                .andExpect(jsonPath("$.id").value(reporteId))
                .andExpect(jsonPath("$.mensajeReporte").value("Reporte de ventas"));

        // Verify
        verify(reportesService).existsById(reporteId);
        verify(reportesService).findById(reporteId);
    }

    /**
     * Prueba para el endpoint GET /api/v1/reportes/{id} cuando el reporte no existe.
     * Verifica que se retorna un estado HTTP 404 NOT_FOUND.
     */
    @Test
    void testGetReportesByIdNoExistente() throws Exception {
        // Arrange
        Long reporteId = 99L;

        // Configurar el mock del servicio para indicar que el reporte no existe
        when(reportesService.existsById(reporteId)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(get("/api/v1/reportes/{id}", reporteId))
                .andExpect(status().isNotFound()); // Espera un estado HTTP 404 NOT_FOUND

        // Verify
        verify(reportesService).existsById(reporteId);
        // No se debe llamar a findById si existsById es falso
        verify(reportesService, never()).findById(anyLong());
    }

    /**
     * Prueba para el endpoint POST /api/v1/reportes.
     * Verifica que se puede crear un nuevo reporte y se retorna un estado HTTP 201 CREATED.
     */
    @Test
    void testCrearReporte() throws Exception {
        // Arrange
        Reportes nuevoReporte = new Reportes(null, "Contabilidad", "user4", "Nuevo reporte contable", Date.valueOf("2023-03-01"));
        Reportes reporteGuardado = new Reportes(3L, "Contabilidad", "user4", "Nuevo reporte contable", Date.valueOf("2023-03-01"));

        // Configurar el mock del servicio para que devuelva el reporte guardado
        when(reportesService.save(any(Reportes.class))).thenReturn(reporteGuardado);

        // Act & Assert
        mockMvc.perform(post("/api/v1/reportes") // Realiza una solicitud POST
                .contentType(MediaType.APPLICATION_JSON) // Establece el tipo de contenido
                .content(objectMapper.writeValueAsString(nuevoReporte))) // Convierte el objeto a JSON y lo envía como cuerpo
                .andExpect(status().isCreated()) // Espera un estado HTTP 201 CREATED
                .andExpect(jsonPath("$.id").value(3L)) // Verifica el ID del reporte creado
                .andExpect(jsonPath("$.mensajeReporte").value("Nuevo reporte contable"));

        // Verify
        verify(reportesService).save(any(Reportes.class));
    }

    /**
     * Prueba para el endpoint PUT /api/v1/reportes/{id} cuando el reporte existe.
     * Verifica que se actualiza el reporte y se retorna un estado HTTP 200 OK.
     */
    @Test
    void testActualizarReporteExistente() throws Exception {
        // Arrange
        Long reporteId = 1L;
        Reportes reporteExistente = new Reportes(reporteId, "Ventas", "user1", "Reporte de ventas original", Date.valueOf("2023-01-01"));
        Reportes detalleReportes = new Reportes(reporteId, "Ventas", "user1", "Reporte de ventas actualizado", Date.valueOf("2023-01-01"));
        Reportes reporteActualizado = new Reportes(reporteId, "Ventas", "user1", "Reporte de ventas actualizado", Date.valueOf("2023-01-01"));

        // Configurar el mock del servicio
        when(reportesService.findById(reporteId)).thenReturn(Optional.of(reporteExistente));
        when(reportesService.save(any(Reportes.class))).thenReturn(reporteActualizado);

        // Act & Assert
        mockMvc.perform(put("/api/v1/reportes/{id}", reporteId) // Realiza una solicitud PUT
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(detalleReportes)))
                .andExpect(status().isOk()) // Espera un estado HTTP 200 OK
                .andExpect(jsonPath("$.id").value(reporteId))
                .andExpect(jsonPath("$.mensajeReporte").value("Reporte de ventas actualizado"));

        // Verify
        verify(reportesService).findById(reporteId);
        verify(reportesService).save(any(Reportes.class)); // Se llama a save con el reporte actualizado
    }

    /**
     * Prueba para el endpoint PUT /api/v1/reportes/{id} cuando el reporte no existe.
     * Verifica que se retorna un estado HTTP 404 NOT_FOUND.
     */
    @Test
    void testActualizarReporteNoExistente() throws Exception {
        // Arrange
        Long reporteId = 99L;
        Reportes detalleReportes = new Reportes(reporteId, "Ventas", "user1", "Reporte de ventas actualizado", Date.valueOf("2023-01-01"));

        // Configurar el mock del servicio para que no encuentre el reporte
        when(reportesService.findById(reporteId)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(put("/api/v1/reportes/{id}", reporteId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(detalleReportes)))
                .andExpect(status().isNotFound()); // Espera un estado HTTP 404 NOT_FOUND

        // Verify
        verify(reportesService).findById(reporteId);
        // Verificar que save() nunca fue llamado porque el reporte no se encontró
        verify(reportesService, never()).save(any(Reportes.class));
    }

    /**
     * Prueba para el endpoint DELETE /api/v1/reportes/{id}.
     * Verifica que se elimina el reporte y se retorna un estado HTTP 204 NO_CONTENT.
     */
    @Test
    void testBorrarReporte() throws Exception {
        // Arrange
        Long reporteId = 1L;

        // Act & Assert
        mockMvc.perform(delete("/api/v1/reportes/{id}", reporteId)) // Realiza una solicitud DELETE
                .andExpect(status().isNoContent()); // Espera un estado HTTP 204 NO_CONTENT

        // Verify
        verify(reportesService).deleteById(reporteId); // Verifica que el método deleteById() del servicio fue llamado
    }
}