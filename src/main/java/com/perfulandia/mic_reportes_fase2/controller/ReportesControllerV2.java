package com.perfulandia.mic_reportes_fase2.controller;



import com.perfulandia.mic_reportes_fase2.model.Reportes;
import com.perfulandia.mic_reportes_fase2.service.ReportesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/reportes") // Mantén la ruta actual o cámbiala a v2 si quieres una nueva versión
public class ReportesControllerV2 {

    @Autowired
    private ReportesService reportesService;

    @Autowired
    private com.perfulandia.mic_reportes_fase2.assembler.ReportesModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<Reportes>>> getAllReportes() {
        List<EntityModel<Reportes>> reportes = reportesService.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return reportes.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(CollectionModel.of(reportes,
                linkTo(methodOn(ReportesControllerV2.class).getAllReportes()).withSelfRel()), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Reportes>> getReportesById(@PathVariable Long id) {
        return reportesService.findById(id)
                .map(assembler::toModel)
                .map(entityModel -> new ResponseEntity<>(entityModel, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Reportes>> crearReporte(@RequestBody Reportes reporte) {
        Reportes newReporte = reportesService.save(reporte);
        return ResponseEntity
                .created(linkTo(methodOn(ReportesControllerV2.class).getReportesById(newReporte.getId())).toUri())
                .body(assembler.toModel(newReporte));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> borrarReporte(@PathVariable Long id) {
        reportesService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Reportes>> actualizarReporte(
            @PathVariable Long id,
            @RequestBody Reportes detalleReportes
    ) {
        return reportesService.findById(id)
                .map(reporteExistente -> {
                    reporteExistente.setMensajeReporte(detalleReportes.getMensajeReporte());
                    // Puedes actualizar otros campos aquí si es necesario
                    Reportes updatedReporte = reportesService.save(reporteExistente);
                    return new ResponseEntity<>(assembler.toModel(updatedReporte), HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}