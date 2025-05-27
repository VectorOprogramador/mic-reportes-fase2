package com.perfulandia.mic_reportes_fase2.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.perfulandia.mic_reportes_fase2.model.Reportes;
import com.perfulandia.mic_reportes_fase2.service.ReportesService;


@RestController
@RequestMapping("/api/v1/reportes")
public class ReportesController {
    @Autowired
    private ReportesService reportesService;

    @GetMapping
    public ResponseEntity<List<Reportes>> getAllReportes(){
        List<Reportes> reportes = reportesService.findAll();
        return reportes.isEmpty()
        ? new ResponseEntity<>(HttpStatus.NO_CONTENT) 
            : new ResponseEntity<>(reportes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reportes> getReportesById(@PathVariable Long id) {
        if (reportesService.existsById(id)){
            return new ResponseEntity<>(reportesService.findById(id).get(),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Metodos adicionales 

    @PostMapping
    public ResponseEntity<Reportes> crearReporte(@RequestBody Reportes reporte) {
        return new ResponseEntity<>(reportesService.save(reporte), HttpStatus.CREATED);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrarReporte(@PathVariable Long id){
        reportesService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
public ResponseEntity<Reportes> actualizarReporte(
    @PathVariable Long id,
    @RequestBody Reportes detalleReportes
) {
    return reportesService.findById(id)
        .map(reporteExistente -> {
            reporteExistente.setMensajeReporte(detalleReportes.getMensajeReporte());
            Reportes reporteActualizado = reportesService.save(reporteExistente);
            return new ResponseEntity<>(reporteActualizado, HttpStatus.OK);
        })
        .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
}
}



/*Este controlador REST (`@RestController`) maneja las operaciones CRUD para reportes mediante endpoints como 
`/api/v1/reportes`. Se comunica con `ReportesService` para:  

1. **Obtener todos** (`GET /`), **buscar por ID** (`GET /{id}`)  
2. **Crear** (`POST /`) y **eliminar** (`DELETE /{id}`) reportes  
3. **Actualizar** (`PUT /{id}`) modificando el mensaje, respondiendo con códigos HTTP adecuados (200, 404, etc.).  

Usa `ResponseEntity` para encapsular respuestas y estados HTTP, delegando la lógica de negocio al servicio inyectado (`@Autowired`). */ 



/*
 Este controlador implementa los siguientes métodos REST, cada uno con una función específica:

    getAllReportes() (GET /api/v1/reportes)

        Función: Retorna todos los reportes almacenados.

        Respuestas:

            200 OK con la lista de reportes (si existen).

            204 NO_CONTENT (si no hay registros).

    getReportesById() (GET /api/v1/reportes/{id})

        Función: Busca un reporte por su ID.

        Respuestas:

            200 OK con el reporte (si existe).

            404 NOT_FOUND (si no existe).

    crearReporte() (POST /api/v1/reportes)

        Función: Crea un nuevo reporte con los datos enviados en el cuerpo de la solicitud (@RequestBody).

        Respuesta: 201 CREATED con el reporte guardado.

    borrarReporte() (DELETE /api/v1/reportes/{id})

        Función: Elimina un reporte por su ID.

        Respuesta: 204 NO_CONTENT (éxito sin retorno de datos).

    actualizarReporte() (PUT /api/v1/reportes/{id})

        Función: Actualiza el mensaje de un reporte existente (identificado por id).

        Lógica:

            Busca el reporte. Si existe, actualiza su campo mensajeReporte y lo guarda.

        Respuestas:

            200 OK con el reporte actualizado.

            404 NOT_FOUND (si el ID no existe).

Flujo general:

    Cada método delega la operación al ReportesService (inyectado) y maneja respuestas HTTP adecuadas para éxito/error.

    Usa anotaciones como @PathVariable para IDs en la URL y @RequestBody para datos enviados en POST/PUT.
 */