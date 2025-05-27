package com.perfulandia.mic_reportes_fase2.model;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity 
@Table(name="reportes")


/*-- */
/* Este código define una entidad JPA llamada
 "Reportes" que mapea a una tabla de base de datos, con campos para id, mensaje y fecha,
  usando Lombok para reducir código boilerplate. */
  
public class Reportes {
    
    @Id 
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000, nullable = false)
    private String mensajeReporte;

    @Column(nullable = false)
    private Date fechaReporte;
}










