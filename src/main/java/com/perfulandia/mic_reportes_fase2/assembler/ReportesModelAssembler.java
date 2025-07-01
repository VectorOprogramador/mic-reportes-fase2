package com.perfulandia.mic_reportes_fase2.assembler;



import com.perfulandia.mic_reportes_fase2.controller.ReportesController;
import com.perfulandia.mic_reportes_fase2.model.Reportes;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ReportesModelAssembler implements RepresentationModelAssembler<Reportes, EntityModel<Reportes>> {

    @Override
    public EntityModel<Reportes> toModel(Reportes reporte) {
        return EntityModel.of(reporte,
                linkTo(methodOn(ReportesController.class).getReportesById(reporte.getId())).withSelfRel(),
                linkTo(methodOn(ReportesController.class).getAllReportes()).withRel("reportes"));
    }
}
