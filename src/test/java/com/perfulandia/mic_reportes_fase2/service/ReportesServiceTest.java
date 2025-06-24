package com.perfulandia.mic_reportes_fase2.service;

import com.perfulandia.mic_reportes_fase2.model.Reportes;
import com.perfulandia.mic_reportes_fase2.repository.ReportesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Clase de pruebas unitarias para {@link ReportesService}.
 * Se utiliza Mockito para simular el comportamiento del repositorio
 * y asegurar que las pruebas se centren en la lógica de negocio del servicio.
 */
class ReportesServiceTest {

    @Mock // Se crea un mock del repositorio de reportes 
    private ReportesRepository reportesRepository;

    @InjectMocks // Se inyecta el mock del repositorio en el servicio que se va a probar 
    private ReportesService reportesService;

    /**
     * Configuración inicial para cada prueba.
     * Se inicializan los mocks antes de cada método de prueba. 
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Prueba unitaria para el método `findAll()` del servicio.
     * Verifica que el servicio devuelve una lista de reportes y que se llama al método `findAll()` del repositorio.
     */
    @Test
    void testFindAll() {
        // Arrange: Preparar los datos de prueba
        Reportes reporte1 = new Reportes(1L, "Ventas", "user1", "Reporte de ventas del mes", Date.valueOf("2023-01-15")); 
        Reportes reporte2 = new Reportes(2L, "Inventario", "user2", "Stock bajo de productos", Date.valueOf("2023-01-20")); 
        List<Reportes> listaReportes = Arrays.asList(reporte1, reporte2);

        // Configurar el comportamiento del mock del repositorio: cuando se llame a findAll(), devuelve la lista de reportes
        when(reportesRepository.findAll()).thenReturn(listaReportes); 

        // Act: Ejecutar el método del servicio
        List<Reportes> resultado = reportesService.findAll(); 

        // Assert: Verificar el resultado
        assertThat(resultado).isNotNull(); // Asegura que el resultado no es nulo
        assertThat(resultado).hasSize(2); // Asegura que la lista tiene el tamaño esperado
        assertThat(resultado).contains(reporte1, reporte2); // Asegura que contiene los reportes esperados

        // Verify: Verificar las interacciones con el mock
        verify(reportesRepository, times(1)).findAll(); // Verifica que findAll() fue llamado exactamente una vez
    }

    /**
     * Prueba unitaria para el método `findById(Long id)` cuando el reporte existe.
     * Verifica que el servicio devuelve el reporte correcto y que se llama al método `findById()` del repositorio.
     */
    @Test
    void testFindByIdExistente() {
        // Arrange
        Long idExistente = 1L;
        Reportes reporte = new Reportes(idExistente, "Ventas", "user1", "Reporte de ventas del mes", Date.valueOf("2023-01-15")); 

        // Configurar el mock: cuando se llame a findById() con el ID, devuelve un Optional con el reporte
        when(reportesRepository.findById(idExistente)).thenReturn(Optional.of(reporte));

        // Act
        Optional<Reportes> resultado = reportesService.findById(idExistente); 

        // Assert
        assertThat(resultado).isPresent(); // Verifica que el Optional contiene un valor
        assertThat(resultado.get()).isEqualTo(reporte); // Verifica que el reporte es el esperado

        // Verify
        verify(reportesRepository, times(1)).findById(idExistente); // Verifica que findById() fue llamado una vez con el ID
    }

    /**
     * Prueba unitaria para el método `findById(Long id)` cuando el reporte no existe.
     * Verifica que el servicio devuelve un `Optional.empty()`.
     */
    @Test
    void testFindByIdNoExistente() {
        // Arrange
        Long idNoExistente = 99L;

        // Configurar el mock: cuando se llame a findById() con el ID, devuelve un Optional vacío
        when(reportesRepository.findById(idNoExistente)).thenReturn(Optional.empty()); 

        // Act
        Optional<Reportes> resultado = reportesService.findById(idNoExistente); 

        // Assert
        assertThat(resultado).isEmpty(); // Verifica que el Optional está vacío

        // Verify
        verify(reportesRepository, times(1)).findById(idNoExistente); // Verifica que findById() fue llamado una vez con el ID
    }

    /**
     * Prueba unitaria para el método `existsById(Long id)` cuando el reporte existe.
     * Verifica que el servicio devuelve `true`.
     */
    @Test
    void testExistsByIdTrue() {
        // Arrange
        Long idExistente = 1L;

        // Configurar el mock: cuando se llame a existsById() con el ID, devuelve true
        when(reportesRepository.existsById(idExistente)).thenReturn(true); 

        // Act
        boolean resultado = reportesService.existsById(idExistente); 

        // Assert
        assertThat(resultado).isTrue(); // Verifica que el resultado es true

        // Verify
        verify(reportesRepository, times(1)).existsById(idExistente); // Verifica que existsById() fue llamado una vez con el ID
    }

    /**
     * Prueba unitaria para el método `existsById(Long id)` cuando el reporte no existe.
     * Verifica que el servicio devuelve `false`.
     */
    @Test
    void testExistsByIdFalse() {
        // Arrange
        Long idNoExistente = 99L;

        // Configurar el mock: cuando se llame a existsById() con el ID, devuelve false
        when(reportesRepository.existsById(idNoExistente)).thenReturn(false);

        // Act
        boolean resultado = reportesService.existsById(idNoExistente);

        // Assert
        assertThat(resultado).isFalse(); // Verifica que el resultado es false

        // Verify
        verify(reportesRepository, times(1)).existsById(idNoExistente); // Verifica que existsById() fue llamado una vez con el ID
    }

    /**
     * Prueba unitaria para el método `save(Reportes reporte)`.
     * Verifica que el servicio guarda un reporte y que se llama al método `save()` del repositorio.
     */
    @Test
    void testSaveReporte() {
        // Arrange
        Reportes nuevoReporte = new Reportes(null, "Usuarios", "user3", "Nuevo usuario registrado", Date.valueOf("2023-02-01")); 
        Reportes reporteGuardado = new Reportes(3L, "Usuarios", "user3", "Nuevo usuario registrado", Date.valueOf("2023-02-01")); 

        // Configurar el mock: cuando se llame a save() con cualquier objeto Reportes, devuelve el reporteGuardado
        when(reportesRepository.save(any(Reportes.class))).thenReturn(reporteGuardado); 

        // Act
        Reportes resultado = reportesService.save(nuevoReporte); 

        // Assert
        assertThat(resultado).isNotNull(); // Asegura que el resultado no es nulo
        assertThat(resultado.getId()).isEqualTo(3L); // Verifica que el ID fue asignado
        assertThat(resultado.getMensajeReporte()).isEqualTo("Nuevo usuario registrado"); // Verifica que el mensaje es correcto

        // Verify
        verify(reportesRepository, times(1)).save(nuevoReporte); // Verifica que save() fue llamado una vez con el reporte
    }

    /**
     * Prueba unitaria para el método `deleteById(Long id)`.
     * Verifica que el servicio elimina un reporte y que se llama al método `deleteById()` del repositorio.
     */
    @Test
    void testDeleteById() {
        // Arrange
        Long idAEliminar = 1L;

        // Act
        reportesService.deleteById(idAEliminar); 

        // Assert: No hay un valor de retorno para verificar, solo la interacción con el mock
        // Verify
        verify(reportesRepository, times(1)).deleteById(idAEliminar); // Verifica que deleteById() fue llamado una vez con el ID
        verifyNoMoreInteractions(reportesRepository); // Opcional: asegura que no hubo más interacciones con el repositorio
    }
}