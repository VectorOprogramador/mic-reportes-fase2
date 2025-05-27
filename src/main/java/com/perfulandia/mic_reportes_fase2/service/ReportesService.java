package com.perfulandia.mic_reportes_fase2.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perfulandia.mic_reportes_fase2.model.Reportes;
import com.perfulandia.mic_reportes_fase2.repository.ReportesRepository;

/*El `ReportesService` (anotado con `@Service`) es un componente Spring que actúa como intermediario entre 
el controlador y el repositorio (`JpaRepository`). Usando el repositorio inyectado (`@Autowired`), 
gestiona operaciones CRUD básicas como buscar, guardar o eliminar reportes,
 y permite añadir lógica de negocio adicional (validaciones, transformaciones) alrededor de estas operaciones. */

@Service
public class ReportesService {
    @Autowired
    private ReportesRepository reportesRepository;

    public List<Reportes> findAll(){
        return reportesRepository.findAll();
    }

    public Optional<Reportes> findById(Long id){
        return reportesRepository.findById(id);
    }

    public boolean existsById(Long id) {
        return reportesRepository.existsById(id);
    }

    // metodos adicionales 

    public Reportes save(Reportes reporte){
        return reportesRepository.save(reporte);
    }

    public void deleteById(Long id){
        reportesRepository.deleteById(id);
    }

}


/*
 El ReportesService es la capa de servicio que implementa la lógica de negocio utilizando los métodos proporcionados por ReportesRepository (que hereda de JpaRepository). A continuación, se explica el funcionamiento de cada método:
1. Métodos de Consulta

    findAll()

        Función: Obtiene todos los reportes de la base de datos.

        Retorna: Una lista de objetos Reportes (vacía si no hay registros).

    findById(Long id)

        Función: Busca un reporte por su ID.

        Retorna: Un Optional<Reportes> (contiene el reporte si existe, o Optional.empty() si no).

    existsById(Long id)

        Función: Verifica si un reporte existe en la base de datos.

        Retorna: true (si existe) o false (si no).

2. Métodos de Modificación

    save(Reportes reporte)

        Función: Guarda o actualiza un reporte en la base de datos.

            Si el reporte no tiene ID, lo inserta como nuevo registro.

            Si el reporte tiene ID, actualiza el registro existente.

        Retorna: El reporte guardado (con ID asignado si era nuevo).

    deleteById(Long id)

        Función: Elimina un reporte por su ID.

        No retorna nada (operación void), pero lanza una excepción si el ID no existe.

3. Flujo de Trabajo

    El controlador (ReportesController) recibe una petición HTTP (ej: GET /api/v1/reportes).

    El servicio (ReportesService) ejecuta la lógica (ej: findAll()) usando el repositorio.

    El repositorio (ReportesRepository) interactúa con la base de datos y devuelve los resultados.

    El servicio retorna los datos al controlador, que los envía como respuesta HTTP.
 */
